package com.application.farmbandi.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.application.farmbandi.Activities.OrderDetail.OrderDetailActivity
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutRvJobItemBinding
import com.application.farmbandi.model.Order

class JobsAdapter : RecyclerView.Adapter<JobsAdapter.JobsViewHolder>()
{


    private val differCallBack = object: ItemCallback<Order>()
    {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean
        {
            return oldItem.job_id == newItem.job_id
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    public class JobsViewHolder(val binding: LayoutRvJobItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_rv_job_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int)
    {
        holder.binding.order = differ.currentList[holder.adapterPosition]
        holder.itemView.setOnClickListener {
            val jobData = Intent(holder.itemView.context,OrderDetailActivity::class.java)
            jobData.putExtra("Job_id",differ.currentList[holder.adapterPosition].job_id.toString())
            holder.itemView.context.startActivity(jobData)
        }
    }

}