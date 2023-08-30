package com.application.farmbandi.Bottomsheet.OtpVerification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.discount_medica.Dialog.BottomSheet.OtpVerification.OtpRepository
import com.application.farmbandi.model.UserOtpData

class OtpViewModel(private val repository: OtpRepository):ViewModel()
{


    val otpSendData: LiveData<UserOtpData>
        get() = repository.otpSendData


}