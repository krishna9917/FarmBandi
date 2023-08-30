package com.application.farmbandi.Activities.PasswordLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PasswordLoginViewModelFactory(val repository: PasswordLoginRepository):ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordLoginViewModel(repository) as T
    }
}