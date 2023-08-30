package com.application.farmbandi.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutRvOrders1ItemBinding
import com.application.farmbandi.databinding.LayoutRvOrdersItemBinding

class OrderTrackingAdapter(private val layoutType: Int = 0) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public class OrderTracksViewHolder(val binding: LayoutRvOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    public class OrderTracksForDestinationViewHolder(val binding: LayoutRvOrders1ItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType)
        {
            1->OrderTracksForDestinationViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_rv_orders1_item,
                    parent,
                    false
                )
            )
            else->OrderTracksViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_rv_orders_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return 10
    }


    override fun getItemViewType(position: Int): Int {
        return layoutType
    }

}