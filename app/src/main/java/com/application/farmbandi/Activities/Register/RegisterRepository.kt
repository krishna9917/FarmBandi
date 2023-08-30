package com.application.farmbandi.Activities.Register

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.Cities
import com.application.farmbandi.model.DriverDetails
import com.application.farmbandi.model.States
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class RegisterRepository(val context: Context) : ApiCallListener {


    private val stateList = MutableLiveData<States>()
    private val cityList = MutableLiveData<Cities>()
    private val register = MutableLiveData<DriverDetails>()


    val stateResponse: LiveData<States>
        get() = stateList


    val citiesResponse: LiveData<Cities>
        get() = cityList

    val registerResponse: LiveData<DriverDetails>
        get() = register

    fun getStates() {
        val statesCall = MyApp.MySingleton.getApiInterface().getStates()
        MyApp.MySingleton.callApi(statesCall, MyApp.MySingleton.GET_STATES, this, context, false)
    }

    fun getCities(id: String) {
        val citiesCall = MyApp.MySingleton.getApiInterface().getCities(id)
        MyApp.MySingleton.callApi(citiesCall, MyApp.MySingleton.GET_CITIES, this, context, false)
    }


    fun register(
        first_name: RequestBody,
        last_name: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        mobile: RequestBody,
        email: RequestBody,
        password: RequestBody,
        confirm_password: RequestBody,
        profile_pic: MultipartBody.Part,
        aadhar_front: MultipartBody.Part,
        aadhar_back: MultipartBody.Part,
        driving_licence_front: MultipartBody.Part,
        driving_licence_back: MultipartBody.Part,
        state_id: RequestBody,
        city_id: RequestBody
    ) {

        val registerCall = MyApp.MySingleton.getApiInterface().driverRegister(
            first_name,
            last_name,
            dob,
            gender,
            mobile,
            email,
            password,
            confirm_password,
            profile_pic,
            aadhar_front,
            aadhar_back,
            driving_licence_front,
            driving_licence_back,
            state_id,
            city_id
        )
        MyApp.MySingleton.callApi(registerCall, MyApp.MySingleton.REGISTRATION, this, context)
    }


    override fun <T : Any> onSuccess(callResponse: Response<T>, apiName: String) {
        when (apiName) {
            MyApp.MySingleton.GET_STATES -> {
                val response = callResponse as Response<States>
                stateList.postValue(response.body())
            }

            MyApp.MySingleton.GET_CITIES -> {
                val response = callResponse as Response<Cities>
                cityList.postValue(response.body())
            }

            MyApp.MySingleton.REGISTRATION -> {
                val response = callResponse as Response<DriverDetails>
                register.postValue(response.body())
            }

        }
    }

    override fun onFailure(message: String, apiName: String) {
        Log.d("DATA---->", "onFailure: " + message)
    }

}