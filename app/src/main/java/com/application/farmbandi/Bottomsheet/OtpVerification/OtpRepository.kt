package com.application.discount_medica.Dialog.BottomSheet.OtpVerification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.R
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.UserOtpData
import retrofit2.Call
import retrofit2.Response

class OtpRepository(private val context: Context) : ApiCallListener {


    private val otpSendResponse = MutableLiveData<UserOtpData>()


    val otpSendData: LiveData<UserOtpData>
        get() = otpSendResponse

    private var otpSendCall: Call<UserOtpData>? = null


    fun otpSend(mobile: String)
    {
        otpSendCall?.cancel()
        otpSendCall = MyApp.MySingleton.getApiInterface().otpSend(mobile)
        MyApp.MySingleton.callApi(
            otpSendCall!!, MyApp.MySingleton.OTP_SEND,
            this,
            context,
            true
        )
    }


    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String) {
        when (apiName) {
            MyApp.MySingleton.OTP_SEND -> {
                val response = callResponse as Response<UserOtpData>
                otpSendResponse.postValue(response.body())
            }
        }
    }

    override fun onFailure(message: String, apiName: String) {
        when (apiName) {
            MyApp.MySingleton.OTP_SEND -> {
                otpSendResponse.postValue(
                    UserOtpData(
                        context.getString(R.string.something_went_wrong_retry),
                        false
                    )
                )
            }
        }

    }

}