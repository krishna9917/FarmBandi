package com.application.farmbandi.PopUpDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.application.farmbandi.databinding.LayoutProgressbarBinding


class AppProgressBar(context: Context) : AlertDialog(context) {
    private val binding by lazy {
        LayoutProgressbarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window!!.setDimAmount(0.2f)
        setCancelable(false)
    }
}