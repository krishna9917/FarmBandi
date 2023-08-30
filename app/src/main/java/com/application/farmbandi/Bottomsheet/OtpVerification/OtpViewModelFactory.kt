package com.application.farmbandi.Bottomsheet.OtpVerification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.discount_medica.Dialog.BottomSheet.OtpVerification.OtpRepository

class OtpViewModelFactory(val repository: OtpRepository):ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtpViewModel(repository) as T
    }


}