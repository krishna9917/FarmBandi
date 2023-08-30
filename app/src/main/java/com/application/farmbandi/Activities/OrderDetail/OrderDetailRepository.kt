package com.application.farmbandi.Activities.OrderDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.R
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.OrderDetail
import com.application.farmbandi.model.StatusMessage
import retrofit2.Response

class OrderDetailRepository(val context: Context):ApiCallListener
{
    private val updateRequestStatus = MutableLiveData<StatusMessage>()
    private val orderDetailData = MutableLiveData<OrderDetail>()

    val updateRequestResponse :LiveData<StatusMessage>
        get() = updateRequestStatus

    val orderDetailResponse :LiveData<OrderDetail>
        get() = orderDetailData


    fun updateRequestStatus(id:String,status:String)
    {
        val updateRequestStatusCall = MyApp.MySingleton.getApiInterface().updateOrderStatus(id,status)
        MyApp.MySingleton.callApi(updateRequestStatusCall,MyApp.MySingleton.UPDATE_ORDER_REQUEST_STATUS,this,context)
    }

    fun getOrderDetail(order_id:String)
    {
        val orderDetailCall = MyApp.MySingleton.getApiInterface().orderDetail(order_id)
        MyApp.MySingleton.callApi(orderDetailCall,MyApp.MySingleton.ORDER_DETAIL,this,context,false)
    }


    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String) {
        when(apiName)
        {
            MyApp.MySingleton.UPDATE_ORDER_REQUEST_STATUS->{
                val response  = callResponse as Response<StatusMessage>
                updateRequestStatus.postValue(response.body())
            }
            MyApp.MySingleton.ORDER_DETAIL->{
                val response = callResponse as Response<OrderDetail>
                orderDetailData.postValue(response.body())
            }
        }
    }

    override fun onFailure(message: String, apiName: String)
    {
        when(apiName)
        {
            MyApp.MySingleton.UPDATE_ORDER_REQUEST_STATUS->{
                updateRequestStatus.postValue(StatusMessage(false,context.getString(R.string.something_went_wrong_retry)))
            }
        }
    }


}