package com.application.farmbandi.Activities.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.farmbandi.Bottomsheet.OtpVerification.OtpVerification
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {
        MyLocalMemory.putBoolean(MyApp.MySingleton.IS_FIRST_TIME_OPEN,true)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(object : OnCompleteListener<String?> {
                override fun onComplete(task: Task<String?>) {
                    if (!task.isSuccessful)
                    {
                        return
                    }
                    task.result?.let {
                        MyLocalMemory.putString(
                            MyApp.MySingleton.DEVICE_TOKEN,
                            it
                        )
                    }
                }
            })
        OtpVerification().show(supportFragmentManager, "OTP_VERIFICATION")
    }

}