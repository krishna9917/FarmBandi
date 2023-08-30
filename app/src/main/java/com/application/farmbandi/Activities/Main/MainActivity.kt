package com.application.farmbandi.Activities.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.application.farmbandi.Activities.AccountStatus.AccountStatusActivity
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.Activities.Intro.IntroActivity
import com.application.farmbandi.Activities.Login.LoginActivity
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.ActivityMainBinding
import com.application.farmbandi.model.DeliveryBoy
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity()
{

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val handler by lazy {
        Handler(Looper.myLooper()!!)
    }
    private val runnable by lazy {
        Runnable {

            var driver:DeliveryBoy?=null
            try {
               driver =MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
            }catch (e:Exception)
            {
                Log.d("DATA--->", ": "+e.message)
            }

            Log.d("DATA--->", ": "+driver)

            if(!MyLocalMemory.getBoolean(MyApp.MySingleton.IS_FIRST_TIME_OPEN))
            {
                startActivity(Intent(this,IntroActivity::class.java))

            }else if(MyLocalMemory.getBoolean(MyApp.MySingleton.IS_LOGIN) && driver?.status==2)
            {
                startActivity(Intent(this,HomeActivity::class.java))
            }else if(MyLocalMemory.getBoolean(MyApp.MySingleton.IS_LOGIN) && driver?.status!=2)
            {
                startActivity(Intent(this,AccountStatusActivity::class.java))
            }
            else
            {
                startActivity(Intent(this,LoginActivity::class.java))
            }
            finishAffinity()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize()
    {
//        binding.txtVersion.text = "@Version " + BuildConfig.VERSION_CODE

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,500)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}