package com.application.farmbandi.Activities.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.application.farmbandi.Activities.JobSettings.JobSettingsActivity
import com.application.farmbandi.Activities.Login.LoginActivity
import com.application.farmbandi.Activities.MyInformation.MyInformationActivity
import com.application.farmbandi.AppInterfaces.CallBackListener
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.PopUpDialogs.ShowAlert
import com.application.farmbandi.R
import com.application.farmbandi.Services.LocationService
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.ActivityProfileBinding
import com.application.farmbandi.model.DeliveryBoy

class ProfileActivity : AppCompatActivity(), View.OnClickListener,DialogClickListener
{
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val repository by lazy {
        ProfileRepository(this)
    }
    private val viewModel by lazy {
        ViewModelProvider(this,ProfileViewModelFactory(repository))[ProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intializer()
    }

    private fun intializer()
    {
        binding.driver=MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
        binding.imgBack.setOnClickListener(this)
        binding.cvJobRelatedSettings.setOnClickListener(this)
        binding.notificationSwitch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        binding.cvMyInfo.setOnClickListener {
            startActivity(Intent(this, MyInformationActivity::class.java))
        }
        binding.imgLogout.setOnClickListener(this)
        setObserver()
    }

    private fun setObserver()
    {
       viewModel.availabilityStatusResponse.observe(this){
           if(it.status)
           {
               MyApp.MySingleton.getLocTrackingFirebaseDb().removeValue().addOnSuccessListener {
                   UtilsFunction.logoutUser(this)
               }.addOnFailureListener {
                   UtilsFunction.errorToast(this, it.message.toString())
               }
           }else
           {
               UtilsFunction.errorToast(this,it.message)
           }
       }
    }

    override fun onClick(p0: View?) {
         when(p0?.id)
         {
             R.id.imgBack                  ->    onBackPressedDispatcher.onBackPressed()
             R.id.cvJobRelatedSettings     ->    startActivity(Intent(this, JobSettingsActivity::class.java))
             R.id.imgLogout                ->    ShowAlert(
                 getString(R.string.confirm_exit),
                 getString(R.string.are_you_sure_you_want_to_exit),
                 this,
                 this,
                 R.drawable.img_logout
             ).show()
         }
    }

    override fun onClick(clickCode: Int, data: Any?, callBack: CallBackListener?)
    {
        repository.availabilityStatus(0)
    }

}