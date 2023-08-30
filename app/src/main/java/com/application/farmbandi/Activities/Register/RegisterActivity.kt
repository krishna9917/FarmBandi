package com.application.farmbandi.Activities.Register

import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.Activities.AccountStatus.AccountStatusActivity
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.AppInterfaces.CallBackListener
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.Bottomsheet.OtpVerification.OtpVerification.Companion.fieldEmptyValidator
import com.application.farmbandi.PopUpDialogs.DatePickerDialog
import com.application.farmbandi.PopUpDialogs.SelectableOptionDialog
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.ActivityRegisterBinding
import com.application.farmbandi.model.IdName
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RegisterActivity : AppCompatActivity(), View.OnClickListener, OnDateSetListener,
    DialogClickListener {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        RegisterRepository(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]
    }


    private var profile: MultipartBody.Part? = null
    private var aadharFront: MultipartBody.Part? = null
    private var aadharBack: MultipartBody.Part? = null
    private var dlFront: MultipartBody.Part? = null
    private var dlBack: MultipartBody.Part? = null

    private var stateList: ArrayList<IdName> = ArrayList()
    private var cityList: ArrayList<IdName> = ArrayList()

    private var stateId = ""
    private var cityId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {
        binding.edtMobile.setText(intent?.getStringExtra(getString(R.string.mobile)))
        binding.rbMale.isChecked=true
        binding.imgBack.setOnClickListener(this)
        binding.llDob.setOnClickListener(this)
        binding.llUploadProfile.setOnClickListener(this)
        binding.llAadharFront.setOnClickListener(this)
        binding.llAadharBack.setOnClickListener(this)
        binding.llDriverFront.setOnClickListener(this)
        binding.llDriverBack.setOnClickListener(this)
        binding.llState.setOnClickListener(this)
        binding.llCity.setOnClickListener(this)
        binding.llGetStartBtn.btn.setOnClickListener(this)

        setDataObserver()

    }

    private fun setDataObserver() {

        viewModel.stateResponse.observe(this) {
            binding.isStateLoading = false
            stateList = it.states
            SelectableOptionDialog("your State", stateList, this, 101, this).show()
        }
        viewModel.citiesResponse.observe(this) {
            binding.isCityLoading = false
            cityList = it.cities
        }

        viewModel.registerResponse.observe(this){
            if (it.status) {
                MyLocalMemory.putString(MyApp.MySingleton.USER_TOKEN, it.access_token)
                MyLocalMemory.putBoolean(
                    MyApp.MySingleton.IS_LOGIN,
                    true
                )
                MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS, it.driver)
                if (it.driver?.status == 2)
                {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this, AccountStatusActivity::class.java))
                }
                finishAffinity()
            } else {
                UtilsFunction.errorToast(this, it.message)
            }
        }

    }

    private fun validator(): Boolean {
        var isValid = true

        if (!fieldEmptyValidator(
                binding.edtFirstName,
                MyApp.MySingleton.getAppContext().getString(R.string.first_name_required)
            )
        ) {
            binding.nsView.fullScroll(View.FOCUS_UP)
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtDob,
                MyApp.MySingleton.getAppContext().getString(R.string.dob_required)
            )
        ) {
            binding.nsView.fullScroll(View.FOCUS_UP)
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtProfilePic,
                MyApp.MySingleton.getAppContext().getString(R.string.profile_required)
            )
        ) {
            isValid = false
        } else if (!emailValidator(binding.edtEmail)) {
            isValid = false
        } else if (!passwordValidator(binding.edtPassword)) {
            isValid = false
        } else if (!confirmPassword(
                binding.edtConfirmPassword,
                binding.edtPassword.text.toString()
            )
        ) {
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtAadharFront,
                MyApp.MySingleton.getAppContext().getString(R.string.adhar_front_required)
            )
        ) {
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtAadharBack,
                MyApp.MySingleton.getAppContext().getString(R.string.adhar_back_required)
            )
        ) {
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtDlFront,
                MyApp.MySingleton.getAppContext().getString(R.string.dl_front_required)
            )
        ) {
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtDlBack,
                MyApp.MySingleton.getAppContext().getString(R.string.dl_back_required)
            )
        ) {
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtState,
                MyApp.MySingleton.getAppContext().getString(R.string.state_required)
            )
        ) {
            binding.nsView.fullScroll(View.FOCUS_DOWN)
            isValid = false
        } else if (!fieldEmptyValidator(
                binding.edtCity,
                MyApp.MySingleton.getAppContext().getString(R.string.city_required)
            )
        ) {
            binding.nsView.fullScroll(View.FOCUS_DOWN)
            isValid = false
        }
        if (!isValid) UtilsFunction.vibration(this)
        return isValid
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.llDob -> {
                DatePickerDialog(this).show(supportFragmentManager, "DOB")
            }

            R.id.llUploadProfile -> {
                ImagePicker.with(this)
                    .provider(ImageProvider.BOTH)
                    .crop()
                    .cropOval()
                    .maxResultSize(512, 512, true)
                    .createIntentFromDialog { profileLauncher.launch(it) }
            }

            R.id.llAadharFront -> {
                ImagePicker.with(this)
                    .crop()
                    .provider(ImageProvider.BOTH)
                    .createIntentFromDialog { adharFrontLauncher.launch(it) }
            }

            R.id.llAadharBack -> {
                ImagePicker.with(this)
                    .crop()
                    .provider(ImageProvider.BOTH)
                    .createIntentFromDialog { adharBackLauncher.launch(it) }
            }

            R.id.llDriverFront -> {
                ImagePicker.with(this)
                    .crop()
                    .provider(ImageProvider.BOTH)
                    .createIntentFromDialog { licenseFrontLauncher.launch(it) }
            }

            R.id.llDriverBack -> {
                ImagePicker.with(this)
                    .crop()
                    .provider(ImageProvider.BOTH)
                    .createIntentFromDialog { licenseBackLauncher.launch(it) }
            }

            R.id.llState -> {
                if (stateList.size == 0) {
                    binding.isStateLoading = true
                    repository.getStates()
                } else {
                    SelectableOptionDialog("your State", stateList, this, 101, this).show()
                }
            }

            R.id.llCity -> {
                SelectableOptionDialog("your City", cityList, this, 102, this).show()
            }

            R.id.btn -> {
                if (validator()) {
                    registrationRequest()
                }
            }
            R.id.imgBack->{
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun registrationRequest()
    {
        var gender = "Male"
        if (binding.rbOther.isChecked) gender = "Others"
        else if (binding.rbFemale.isChecked) gender = "Female"
        else gender = "Male"

        repository.register(
            binding.edtFirstName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtLastName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtDob.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            gender.toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtMobile.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtEmail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtPassword.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            binding.edtConfirmPassword.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            profile!!,
            aadharFront!!,
            aadharBack!!,
            dlFront!!,
            dlBack!!,
            stateId.toRequestBody("text/plain".toMediaTypeOrNull()),
            cityId.toRequestBody("text/plain".toMediaTypeOrNull())
        )


    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding.edtDob.error=null
        binding.edtDob.setText("$day-${month + 1}-$year")
    }

    private val profileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!

                val imageFile = File(uri.path.toString())
                profile = MultipartBody.Part.createFormData(
                    "profile_pic",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )

                binding.edtProfilePic.error=null
                binding.edtProfilePic.setText(File(uri.path).name)
            } else {
                parseError(it)
            }
        }

    private val adharFrontLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!

                val imageFile = File(uri.path.toString())
                aadharFront = MultipartBody.Part.createFormData(
                    "aadhar_front",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
                binding.edtAadharFront.error=null
                binding.edtAadharFront.setText(File(uri.path).name)
            } else {
                parseError(it)
            }
        }

    private val adharBackLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!


                val imageFile = File(uri.path.toString())
                aadharBack = MultipartBody.Part.createFormData(
                    "aadhar_back",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
                binding.edtAadharBack.error=null
                binding.edtAadharBack.setText(File(uri.path).name)
            } else {
                parseError(it)
            }
        }

    private val licenseFrontLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                val imageFile = File(uri.path.toString())
                dlFront = MultipartBody.Part.createFormData(
                    "driving_license_front",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )

                binding.edtDlFront.error=null
                binding.edtDlFront.setText(File(uri.path).name)
            } else {
                parseError(it)
            }
        }

    private val licenseBackLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                val imageFile = File(uri.path.toString())
                dlBack = MultipartBody.Part.createFormData(
                    "driving_license_back",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
                binding.edtDlBack.error=null
                binding.edtDlBack.setText(File(uri.path).name)
            } else {
                parseError(it)
            }
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(clickCode: Int, data: Any?, callBack: CallBackListener?) {
        val item = data as IdName
        when (clickCode) {
            101 -> {
                if (stateId != item.id.toString())
                {
                    stateId = item.id.toString()
                    binding.edtCity.setText("")
                    binding.edtState.error=null
                    binding.edtState.setText(item.name)
                    binding.llCity.visibility = View.VISIBLE
                    binding.isCityLoading = true
                    repository.getCities(item.id.toString())
                }
            }
            102 -> {
                cityId=item.id.toString()
                binding.edtCity.error=null
                binding.edtCity.setText(item.name)
            }
        }
    }

    companion object {
        fun emailValidator(editText: EditText): Boolean {
            var isValid = true
            if (!fieldEmptyValidator(editText, MyApp.MySingleton.getAppContext().getString(R.string.email_required)
                )
            ) {
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
                editText.error =
                    MyApp.MySingleton.getAppContext().getString(R.string.invalid_email_address)
                isValid = false
            }
            return isValid
        }

        fun passwordValidator(editField: EditText): Boolean {
            var isValid = true
            if (!fieldEmptyValidator(editField, MyApp.MySingleton.getAppContext().getString(R.string.please_enter_password))
            ) {
                isValid = false
            } else if (editField.text.toString().length < 8) {
                editField.error = MyApp.MySingleton.getAppContext()
                    .getString(R.string.invalid_password_min_8_char)
                isValid = false
            }
            return isValid
        }

        fun confirmPassword(editText: EditText, password: String): Boolean {
            var isValid = true
            if (!editText.text.toString().equals(password)) {
                editText.error =
                    MyApp.MySingleton.getAppContext().getString(R.string.password_mismatch)
                isValid = false
            }
            return isValid
        }
    }


}