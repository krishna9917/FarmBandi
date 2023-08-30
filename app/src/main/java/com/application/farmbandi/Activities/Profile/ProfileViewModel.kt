package com.application.farmbandi.Activities.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.farmbandi.model.AvailabilityStatus

class ProfileViewModel(val repository: ProfileRepository):ViewModel()
{


    val availabilityStatusResponse: LiveData<AvailabilityStatus>
        get() = repository.availabilityStatusResponse

}