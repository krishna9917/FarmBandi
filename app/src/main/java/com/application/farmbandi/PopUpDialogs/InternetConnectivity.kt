package com.application.farmbandi.PopUpDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.R
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.databinding.LayoutNetworkissueBinding


class InternetConnectivity(context: Context, private val clickListener: DialogClickListener?=null, private val isVisibleRetryBtn:Boolean=true) :AlertDialog(context) , View.OnClickListener
{
    private val binding by lazy {
        LayoutNetworkissueBinding.inflate(layoutInflater)
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
        binding.isVisibleRefreshBtn = isVisibleRetryBtn
        binding.imgRefresh.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      when(p0?.id)
      {
          R.id.imgRefresh->{
              if (!UtilsFunction.isInternetConnected(MyApp.MySingleton.getAppContext()))
              {
                  Toast.makeText(context,context.getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show()
              }else
              {
                  dismiss()
                  clickListener?.onClick(101)
              }
          }
      }
    }

}