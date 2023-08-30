package com.application.farmbandi.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutIntroBannerItemBinding
import com.application.farmbandi.model.IntroBanners


class IntroScreenAdapter(private val introScreens: ArrayList<IntroBanners>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private class BannerItemViewHolder(val binding: LayoutIntroBannerItemBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
      return  BannerItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_intro_banner_item,parent,false))
    }

    override fun getItemCount(): Int
    {
        return introScreens.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
    {
        val holder = viewHolder as BannerItemViewHolder
        holder.binding.img.setImageDrawable(viewHolder.itemView.context.getDrawable(introScreens[position].image))
        holder.binding.cv.setCardBackgroundColor(holder.itemView.context.getColor(R.color.transparent))
    }

}