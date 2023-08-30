package com.application.farmbandi.Activities.OrderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderDetailViewModelFactory(private val orderId:String, val repository: OrderDetailRepository):ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderDetailViewModel(orderId,repository) as T
    }
}