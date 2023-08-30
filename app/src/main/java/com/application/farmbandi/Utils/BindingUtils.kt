package com.application.farmbandi.Utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.application.farmbandi.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.squareup.picasso.Picasso


@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("app:netImageSrc","app:placeholder", requireAll = false)
fun loadImage(view:ImageView,url:String?,placeholder:Drawable?)
{
    try
    {
        Picasso.get().load(url).placeholder((placeholder?:view.context.getDrawable(R.drawable.img_loading))!!).error(R.drawable.ic_logo).into(view)
    } catch (e: Exception)
    {
        view.setImageDrawable(placeholder?:view.context.getDrawable(R.drawable.ic_logo))
    }
}

@SuppressLint("UseCompatTextViewDrawableApis")
@BindingAdapter("drawableTint")
fun setDrawableTint(view: TextView, color: ColorStateList?) {
    if (color != null) {
        view.compoundDrawableTintList = color
    }
}

@BindingAdapter("android:textStyle")
fun setTextViewStyle(textView: TextView, textStyle: Int) {
    textView.setTypeface(null, textStyle)
}

@BindingAdapter("strikeThrough")
fun setStrikeThrough(textView: TextView, strikeThrough: Boolean)
{
    if (strikeThrough)
    {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}


@BindingAdapter("app:setDrawableSize")
fun setDrawableSize(textView: TextView, size: Int) {
    val drawables = textView.compoundDrawables
    for (drawable in drawables) {
        drawable?.setBounds(0, 0, size, size)
    }
    textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
}


@BindingAdapter("app:cardDrawable")
fun cardDrawable(cardView: MaterialCardView,drawable:Drawable)
{
    cardView.background=drawable
}


@BindingAdapter("app:showProgress")
fun showProgressOnMaterialButton(button:MaterialButton,isVisible:Boolean)
{
    val spec = CircularProgressIndicatorSpec(button.context,null, 0, com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall)
    spec.indicatorColors= intArrayOf(Color.RED, Color.GREEN, Color.YELLOW)
    val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(button.context, spec)
    if(isVisible)
    {
        button.icon = progressIndicatorDrawable
        button.isEnabled=false
        button.isClickable=false
        button.setTextColor(Color.GRAY)
    }else
    {
        button.icon = null
        button.isEnabled=true
        button.isClickable=true
        button.setTextColor(Color.WHITE)
    }
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}


@BindingAdapter("app:animatedVisibility","app:animationId")
fun showVisibility(view: View, isVisible: Boolean,animationId:Int)
{
    if(isVisible)
    {
        view.visibility= View.VISIBLE
        UtilsFunction.setAnimation(view.context,view,animationId)
    }else
    {
        view.visibility= View.GONE
    }
}


@BindingAdapter("android:textSize")
fun setTextViewTextSize(textView: TextView, textSize: Float) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
}

@BindingAdapter("app:text","app:subString")
fun subStringBold(textView: TextView,mainString:String,subString:String)
{
    var stringChecked = 0
    val spannableString= SpannableStringBuilder(mainString)

    for (i in 0 until mainString.split(subString, ignoreCase = true).size - 1)
    {
        val startIndex = mainString.indexOf(subString, stringChecked, ignoreCase = true)
        stringChecked = startIndex + subString.length
        val mBold = StyleSpan(Typeface.BOLD)
        val black = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(mBold, startIndex, stringChecked, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(black, startIndex, stringChecked, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    textView.text = spannableString
}





















