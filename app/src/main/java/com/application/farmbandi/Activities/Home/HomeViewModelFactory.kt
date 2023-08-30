package com.application.farmbandi.Activities.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(val repository: HomeRepository): ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  HomeViewModel(repository) as T
    }
}