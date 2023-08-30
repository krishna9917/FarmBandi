package com.application.farmbandi.PopUpDialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.application.farmbandi.Adapters.SelectableItemsAdapter
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.AppInterfaces.RecyclerViewClickListener
import com.application.farmbandi.R
import com.application.farmbandi.databinding.LayoutDropDownDialogBinding
import com.application.farmbandi.model.IdName


class SelectableOptionDialog(
    private val whatOptions:String="Items",
    private val items: ArrayList<IdName>,
    context: Context,
    private val dialogCode:Int,
    private val clickListener: DialogClickListener
) : AlertDialog(context), View.OnClickListener,RecyclerViewClickListener {

    private val binding by lazy {
        LayoutDropDownDialogBinding.inflate(layoutInflater)
    }
    private val itemsAdapter by lazy {
          SelectableItemsAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        initializer()




    }

    private fun initializer() {
        binding.itemsTypeName=whatOptions
        binding.imgClose.setOnClickListener(this)
        binding.rvItems.adapter=itemsAdapter
        itemsAdapter.submitList(items)

        binding.edtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchedItems = items.filter { it.name.contains(binding.edtSearch.text.toString(),true) }
                itemsAdapter.submitList(searchedItems as ArrayList<IdName>)
            }
        })

    }


    override fun onClick(p0: View?) {
      when(p0?.id)
      {
          R.id.imgClose->dismiss()

      }
    }

    override fun onClick(
        data: Any,
        selectedPosition: Int,
        click_layout_code: Int,
        callBack: RecyclerViewClickListener?
    ) {
        val item = data as IdName
        clickListener.onClick(dialogCode,item)
        dismiss()
    }

}