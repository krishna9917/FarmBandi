package com.application.farmbandi.Activities.OrderDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.farmbandi.model.OrderDetail
import com.application.farmbandi.model.StatusMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailViewModel(val orderId:String,val repository: OrderDetailRepository):ViewModel()
{

     init {
         viewModelScope.launch(Dispatchers.IO) {
             repository.getOrderDetail(orderId)
         }
     }

    val updateRequestResponse : LiveData<StatusMessage>
        get() = repository.updateRequestResponse

    val orderDetailResponse :LiveData<OrderDetail>
        get() = repository.orderDetailResponse

}