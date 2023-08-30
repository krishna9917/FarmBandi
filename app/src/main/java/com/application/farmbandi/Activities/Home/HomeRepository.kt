package com.application.farmbandi.Activities.Home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.AvailabilityStatus
import com.application.farmbandi.model.DeliveryBoy
import com.application.farmbandi.model.Jobs
import retrofit2.Response

class HomeRepository(val context: Context) : ApiCallListener
{
    private val availabilityData = MutableLiveData<AvailabilityStatus>()

    val availabilityStatusResponse: LiveData<AvailabilityStatus>
        get() = availabilityData



    fun availabilityStatus(status: Int) {
        val deliveryBoy = MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
        val statusCall = MyApp.MySingleton.getApiInterface().setAvailabilityStatus(deliveryBoy.latitude, deliveryBoy.longitude, status)
        MyApp.MySingleton.callApi(statusCall, MyApp.MySingleton.AVAILABILITY_STATUS, this, context)
    }


    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String) {
        when (apiName) {
            MyApp.MySingleton.AVAILABILITY_STATUS->{
                val response  = callResponse as Response<AvailabilityStatus>
                availabilityData.postValue(response.body())
            }
        }
    }

    override fun onFailure(message: String, apiName: String)
    {
       when(apiName)
       {
           MyApp.MySingleton.AVAILABILITY_STATUS->{
               availabilityData.postValue(AvailabilityStatus(false,context.getString(R.string.something_went_wrong_retry),0))
           }
       }
    }


}