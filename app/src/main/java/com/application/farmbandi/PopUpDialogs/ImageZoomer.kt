package com.application.farmbandi.PopUpDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutImageZoomerBinding


class ImageZoomer(context: Context,val imageUrl:String): AlertDialog(context),View.OnClickListener
{
    private val binding by lazy {
        LayoutImageZoomerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val layoutParams = window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = layoutParams
        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        setCancelable(false)
        binding.imageData=imageUrl
        binding.cvBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.cvBack->dismiss()
        }
    }

}