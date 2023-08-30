package com.application.farmbandi.AppInterfaces

interface DialogClickListener
{
    fun onClick(clickCode: Int,data:Any?=null,callBack: CallBackListener?=null)
}