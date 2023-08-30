package com.application.farmbandi.Activities.JobSettings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.application.farmbandi.R
import com.application.farmbandi.databinding.ActivityJobSettingsBinding
import com.google.android.material.tabs.TabLayout
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class JobSettingsActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy {
        ActivityJobSettingsBinding.inflate(layoutInflater)
    }

    val c: Calendar = Calendar.getInstance()
    val hour: Int = c.get(Calendar.HOUR_OF_DAY)
    val minute: Int = c.get(Calendar.MINUTE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer()
    }

    private fun initializer() {
        binding.imgBack.setOnClickListener(this)
        binding.llStartTime.setOnClickListener(this)
        binding.llReturnTime.setOnClickListener(this)
        binding.tabOrderType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.isJobsAsPerUserInput = tab?.position == 1
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgBack -> onBackPressedDispatcher.onBackPressed()
            R.id.llStartTime -> {
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        setTime(binding.edtGoingTime, "$hourOfDay:$minute")
                    }, hour, minute, false
                )
                timePickerDialog.show()
            }

            R.id.llReturnTime -> {
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        setTime(binding.edtComingTime, "$hourOfDay:$minute")
                    }, hour, minute, false
                )
                timePickerDialog.show()
            }
        }
    }

    private fun setTime(edt: EditText, time: String) {
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj: Date = sdf.parse(time)
            edt.setText(SimpleDateFormat("hh:mm a").format(dateObj))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}