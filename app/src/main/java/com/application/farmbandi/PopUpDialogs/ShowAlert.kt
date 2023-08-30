package com.application.farmbandi.PopUpDialogs
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutAlertBinding

class ShowAlert(private val titleMessage:String, val message:String, private val clickListener: DialogClickListener, context:Context, val image: Int? =null):AlertDialog(context),View.OnClickListener
{
   private val binding by lazy {
       LayoutAlertBinding.inflate(layoutInflater)
   }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.windowAnimations= R.style.DialogBounceAnimation

        if(image!=null)
        {
            binding.imgAlert.setImageResource(image)
            binding.imgAlert.visibility= View.VISIBLE
        }

        binding.txtMessage.text = message
        binding.txtTitleMessage.text =titleMessage


        binding.btnNo.setOnClickListener(this)
        binding.btnYes.setOnClickListener(this)

    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
          R.id.btnNo->dismiss()
          R.id.btnYes->{
              clickListener.onClick(1022)
              dismiss()
          }
        }
    }

}