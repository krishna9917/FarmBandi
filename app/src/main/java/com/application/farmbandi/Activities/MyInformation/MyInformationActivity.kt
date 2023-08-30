package com.application.farmbandi.Activities.MyInformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.databinding.ActivityMyInformationBinding
import com.application.farmbandi.model.DeliveryBoy

class MyInformationActivity : AppCompatActivity(), View.OnClickListener
{
    private val binding by lazy {
        ActivityMyInformationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer()
    {
        binding.driverProfile =MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
        binding.imgBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.imgBack -> onBackPressedDispatcher.onBackPressed()
        }
    }
}