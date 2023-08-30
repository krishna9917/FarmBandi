package com.application.farmbandi.Activities.Intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.application.farmbandi.Activities.Home.HomeActivity
import com.application.farmbandi.Activities.Login.LoginActivity
import com.application.farmbandi.Adapters.IntroScreenAdapter
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.ActivityIntroBinding
import com.application.farmbandi.model.IntroBanners
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {

        val screens = ArrayList<IntroBanners>()
        screens.add(IntroBanners(R.drawable.img_intro1, "", ""))
        binding.vpIntro.adapter = IntroScreenAdapter(screens)

        if (screens.size > 1) {
            binding.dotsIndicator.attachTo(binding.vpIntro)
        }

        binding.llGetStartBtn.btn.setOnClickListener(this)

        MyLocalMemory.putBoolean(MyApp.MySingleton.IS_FIRST_TIME_OPEN, true)


        binding.vpIntro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                when (position) {
//                    0 -> {
//                        binding.txtTitle.text=getString(R.string.intro_screen1_title)
//                    }
//                    1 -> {
//                        binding.txtTitle.text=getString(R.string.intro_screen2_title)
//                    }
//                    2 -> {
//                        binding.txtTitle.text=getString(R.string.intro_screen3_title)
//                    }
                }
            }
        })


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


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn -> {
                if (MyLocalMemory.getBoolean(MyApp.MySingleton.IS_LOGIN)) {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }
    }
}