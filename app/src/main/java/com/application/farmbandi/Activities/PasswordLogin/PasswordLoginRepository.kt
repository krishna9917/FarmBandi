package com.application.farmbandi.Activities.PasswordLogin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.R
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.DriverDetails
import retrofit2.Response

class PasswordLoginRepository(val context: Context): ApiCallListener
{

    private val loginData = MutableLiveData<DriverDetails>()


    val loginResponse:LiveData<DriverDetails>
        get() = loginData


    fun login(email:String,password:String)
    {
        val loginCall = MyApp.MySingleton.getApiInterface().driverLogin(email,password)
        MyApp.MySingleton.callApi(loginCall,MyApp.MySingleton.LOGIN,this,context)
    }

    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String)
    {
        when(apiName)
        {
            MyApp.MySingleton.LOGIN->{
                val  response = callResponse as Response<DriverDetails>
                loginData.postValue(response.body())
            }
        }

    }

    override fun onFailure(message: String, apiName: String)
    {
        when(apiName)
        {
            MyApp.MySingleton.LOGIN->{
                loginData.postValue(DriverDetails(context.getString(R.string.something_went_wrong_retry),false))
            }
        }
    }


}