package com.application.farmbandi.Activities.AccountStatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AccountStatusViewModelFactory(val repository: AccountStatusRepository):ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountStatusViewModel(repository) as T
    }

}