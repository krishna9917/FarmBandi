package com.application.farmbandi.Activities.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.farmbandi.model.Cities
import com.application.farmbandi.model.DriverDetails
import com.application.farmbandi.model.States

class RegisterViewModel(private val registerRepository: RegisterRepository):ViewModel()
{


    val stateResponse: LiveData<States>
        get() = registerRepository.stateResponse


    val citiesResponse: LiveData<Cities>
        get() =registerRepository.citiesResponse


    val registerResponse:LiveData<DriverDetails>
        get() = registerRepository.registerResponse


}