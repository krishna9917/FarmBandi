package com.application.farmbandi.Bottomsheet.OtpVerification

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.application.discount_medica.Dialog.BottomSheet.OtpVerification.OtpRepository
import com.application.farmbandi.Activities.AccountStatus.AccountStatusActivity
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.Activities.PasswordLogin.LoginWithPasswordActivity
import com.application.farmbandi.Activities.Register.RegisterActivity
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.LayoutOtpVerifcationBinding
import com.application.farmbandi.model.UserOtpData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.aabhasjindal.otptextview.OtpTextView
import java.util.concurrent.TimeUnit


class OtpVerification() : BottomSheetDialogFragment(), View.OnClickListener {
    private val binding by lazy {
        LayoutOtpVerifcationBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        OtpRepository(requireContext())
    }

    private val viewModel by lazy {
        ViewModelProvider(this, OtpViewModelFactory(repository))[OtpViewModel::class.java]
    }

    lateinit var countDownTimer: CountDownTimer
    lateinit var otpSentData: UserOtpData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setDimAmount(0f)
        initializer()
        return binding.root
    }

    private fun initializer() {
        binding.llContinueBtn.btn.setOnClickListener(this)
        binding.txtBack.setOnClickListener(this)
        binding.txtCountDown.setOnClickListener(this)
        binding.txtLoginWithPassword.setOnClickListener(this)
        Log.d("FCM---->", "initializer: "+MyLocalMemory.getString(MyApp.MySingleton.DEVICE_TOKEN))
        setDataObserver()
    }

    private fun setDataObserver() {
        viewModel.otpSendData.observe(this) {
            binding.isLoading = false
            if (it.status) {
                otpSentData = it
                makeNumberBold()
                startTimer()
                binding.showOtpScreen = true
                binding.otpView.requestFocus()
            } else {
                UtilsFunction.errorToast(requireContext(), it.message)
            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn -> {
                if (binding.clMobileInput.visibility == View.VISIBLE && mobileValidator(binding.edtNumber))
                {
                    binding.isLoading = true
                    repository.otpSend(binding.edtNumber.text.toString())

                } else if (binding.clOTPInput.visibility == View.VISIBLE && otpValidator(binding.otpView, otpSentData.otp)
                ) {
                    if (otpSentData.is_driver_exits)
                    {
                        MyLocalMemory.putString(MyApp.MySingleton.USER_TOKEN, otpSentData.access_token)
                        MyLocalMemory.putBoolean(
                            MyApp.MySingleton.IS_LOGIN,
                            otpSentData.is_driver_exits
                        )
                        MyLocalMemory.putObject(
                            MyApp.MySingleton.DRIVER_DETAILS,
                            otpSentData.driver
                        )
                        if (otpSentData.driver?.status == 2)
                        {
                            startActivity(Intent(requireContext(), HomeActivity::class.java))

                        } else {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    AccountStatusActivity::class.java
                                )
                            )
                        }
                        activity?.finishAffinity()
                    } else {
                        val goToRegister = Intent(requireContext(), RegisterActivity::class.java)
                        goToRegister.putExtra(
                            getString(R.string.mobile),
                            binding.edtNumber.text.toString()
                        )
                        startActivity(goToRegister)
                    }
                    countDownTimer.cancel()
                }
            }

            R.id.txtBack -> {
                countDownTimer.cancel()
                binding.showOtpScreen = false
            }

            R.id.txtCountDown -> {
                binding.isLoading = true
                repository.otpSend(binding.edtNumber.text.toString())
            }

            R.id.txtLoginWithPassword -> {
                startActivity(Intent(requireContext(), LoginWithPasswordActivity::class.java))
            }
        }
    }

    private fun makeNumberBold() {
        val mainString =
            "A OTP has been sent to +91" + binding.edtNumber.text.toString() + "\nKind enter below the 6 digits code"
        val subString = "+91" + binding.edtNumber.text.toString()
        val spannableString = SpannableStringBuilder(mainString)
        val startIndex = mainString.indexOf(subString, 0, ignoreCase = true)

        val mBold = StyleSpan(Typeface.BOLD)
        val blue = ForegroundColorSpan(context?.getColor(R.color.color_theme_100)!!)
        spannableString.setSpan(
            mBold,
            startIndex,
            startIndex + subString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            blue,
            startIndex,
            startIndex + subString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtOtpEnter.text = spannableString

    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = String.format(
                    "%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                binding.countDown = time
            }

            override fun onFinish() {
                binding.countDown = MyApp.MySingleton.getAppContext().getString(R.string.resend_otp)
            }
        }.start()
    }

    companion object {
        fun otpValidator(otpView: OtpTextView, sentOtp: String): Boolean {
            var isValid = true
            if (otpView.otp.equals("")) {
                UtilsFunction.errorToast(
                    otpView.context,
                    MyApp.MySingleton.getAppContext().getString(R.string.please_enter_otp)
                )
                isValid = false
            } else if (otpView.otp!!.length < 6) {
                UtilsFunction.errorToast(
                    otpView.context,
                    MyApp.MySingleton.getAppContext().getString(R.string.enter_valid_otp_code)
                )
                isValid = false
            } else if (otpView.otp!!.toString() != sentOtp) {
                UtilsFunction.errorToast(
                    otpView.context,
                    MyApp.MySingleton.getAppContext().getString(R.string.incorrect_otp)
                )
                isValid = false
            }
            if (!isValid) otpView.showError()
            return isValid
        }

        fun mobileValidator(editField: EditText): Boolean {
            var isValid = true
            val mobileNumber = editField.text.toString()
            if (!fieldEmptyValidator(
                    editField,
                    MyApp.MySingleton.getAppContext().getString(R.string.please_enter_mobile_number)
                )
            ) {
                isValid = false
            } else if (mobileNumber.length < 10 || !(mobileNumber[0].equals('9') || mobileNumber[0].equals(
                    '8'
                ) || mobileNumber[0].equals('7') || mobileNumber[0].equals('6'))
            ) {
                editField.error =
                    MyApp.MySingleton.getAppContext().getString(R.string.invalid_mobile_number)
                isValid = false
            }
            if (!isValid) UtilsFunction.vibration(MyApp.MySingleton.getAppContext())
            return isValid
        }

        fun fieldEmptyValidator(editText: EditText, message: String): Boolean {
            var isValid = true
            if (editText.text.toString().equals("")) {
                editText.error = message
                isValid = false
            }
            return isValid
        }

    }
}