package com.application.farmbandi.Activities.AccountStatus

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.AccountStatus
import retrofit2.Response

class AccountStatusRepository(val context:Context):ApiCallListener
{

    private val accountStatusData=MutableLiveData<AccountStatus>()

    val accountStatusResponse:LiveData<AccountStatus>
        get() = accountStatusData



    fun getAccountStatus(showProgress:Boolean=false)
    {
       val accountStatusCall = MyApp.MySingleton.getApiInterface().getAccountStatus()
        MyApp.MySingleton.callApi(accountStatusCall,MyApp.MySingleton.GET_ACCOUNT_STATUS,this,context,showProgress)
    }

    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String)
    {
       when(apiName)
       {
           MyApp.MySingleton.GET_ACCOUNT_STATUS -> {
               val response = callResponse as Response<AccountStatus>
               accountStatusData.postValue(response.body())
           }
       }
    }

    override fun onFailure(message: String, apiName: String)
    {

    }


}