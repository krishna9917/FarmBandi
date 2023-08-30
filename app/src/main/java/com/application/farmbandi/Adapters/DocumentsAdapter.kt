package com.application.farmbandi.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.farmbandi.AppInterfaces.RecyclerViewClickListener
import com.application.farmbandi.PopUpDialogs.ImageZoomer
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutDocumentItemBinding
import com.application.farmbandi.model.Document

class DocumentsAdapter(private val clickListener: RecyclerViewClickListener) :
    RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {

    private var documents: ArrayList<Document>? = null

    private var documentImages: ArrayList<String> = ArrayList()

    class DocumentViewHolder(val binding: LayoutDocumentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_document_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return documents?.size ?: 4
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        if (documents == null) {
            holder.binding.showShimmer = true
        } else {
            holder.binding.showShimmer = false
            holder.binding.document = documents!![position]
            holder.binding.cvItem.setOnClickListener {
                 ImageZoomer(holder.itemView.context, documents!![position].url).show()
            }
        }

    }


    fun submitDocumentList(documents: ArrayList<Document>) {
        this.documents = documents
        documentImages.clear()
        for (i in this.documents!!)
        {
            documentImages.add(i.url)
        }
        notifyDataSetChanged()
    }


}