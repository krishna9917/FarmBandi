package com.application.farmbandi.AppInterfaces

interface RecyclerViewClickListener
{
    fun onClick(data:Any, selectedPosition:Int, click_layout_code:Int=0,callBack:RecyclerViewClickListener?=null)
}