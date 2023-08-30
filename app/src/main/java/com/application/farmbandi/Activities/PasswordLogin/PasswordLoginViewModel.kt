package com.application.farmbandi.Activities.PasswordLogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.farmbandi.model.DriverDetails

class PasswordLoginViewModel(val repository: PasswordLoginRepository):ViewModel()
{

    val loginResponse: LiveData<DriverDetails>
        get() = repository.loginResponse

}