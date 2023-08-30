package com.application.farmbandi.Activities.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.farmbandi.model.AvailabilityStatus
import com.application.farmbandi.model.Jobs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repository: HomeRepository):ViewModel()
{
    val availabilityStatusResponse: LiveData<AvailabilityStatus>
        get() = repository.availabilityStatusResponse

}