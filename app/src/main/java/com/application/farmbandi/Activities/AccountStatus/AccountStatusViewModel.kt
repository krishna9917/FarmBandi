package com.application.farmbandi.Activities.AccountStatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.farmbandi.model.AccountStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountStatusViewModel(val repository: AccountStatusRepository):ViewModel()
{

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAccountStatus(true)
        }
    }

    val accountStatusResponse: LiveData<AccountStatus>
        get() = repository.accountStatusResponse

}