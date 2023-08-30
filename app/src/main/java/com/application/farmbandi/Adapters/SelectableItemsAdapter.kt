package com.application.farmbandi.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.application.farmbandi.AppInterfaces.RecyclerViewClickListener
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutSelectableItemsBinding
import com.application.farmbandi.model.IdName

class SelectableItemsAdapter(val clickListener: RecyclerViewClickListener) :
    RecyclerView.Adapter<SelectableItemsAdapter.SelectableItemsViewHolder>() {



    private val differItems = AsyncListDiffer(this, object : ItemCallback<IdName>() {
        override fun areItemsTheSame(oldItem: IdName, newItem: IdName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IdName, newItem: IdName): Boolean {
            return oldItem == newItem
        }
    })


    class SelectableItemsViewHolder(val binding: LayoutSelectableItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableItemsViewHolder {
        return SelectableItemsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_selectable_items, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differItems.currentList.size
    }

    override fun onBindViewHolder(holder: SelectableItemsViewHolder, position: Int)
    {
        holder.binding.itemData = differItems.currentList[holder.adapterPosition]
        holder.binding.clItem.setOnClickListener {
                    clickListener.onClick(differItems.currentList[holder.adapterPosition],holder.adapterPosition)
        }
    }



    fun submitList(itemList: ArrayList<IdName>)
    {

        differItems.submitList(itemList)
    }


}