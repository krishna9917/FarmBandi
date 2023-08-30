package com.application.farmbandi.Utils

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.application.farmbandi.ApiService.ApiCall
import com.application.farmbandi.ApiService.ApiInstance
import com.application.farmbandi.ApiService.ApiInterface
import com.application.farmbandi.AppInterfaces.ApiCallListener
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.model.DeliveryBoy
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call

public class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MySingleton.init(applicationContext)
    }

    class MySingleton {
        companion object {



            val BASE_URL = "https://www.website4test.com/farm-bandi/api/"
            val SITE_URL = "https://www.website4test.com/farm-bandi/"

            // Local storage keys
            const val IS_FIRST_TIME_OPEN = "IS_FIRST_TIME_OPEN"
            const val USER_TOKEN = "USER_TOKEN"
            const val IS_LOGIN = "IS_LOGIN"
            const val DRIVER_DETAILS = "DRIVER_DETAILS"
            const val DEVICE_ID = "DEVICE_ID"
            const val DEVICE_TOKEN = "DEVICE_TOKEN"


            //            apis
            const val OTP_SEND = "otp-send"
            const val GET_STATES = "states"
            const val GET_CITIES = "cities"
            const val REGISTRATION = "register"
            const val LOGIN = "driver-login"
            const val GET_ACCOUNT_STATUS = "check_account_status"
            const val AVAILABILITY_STATUS = "driver-available-status"
            const val UPDATE_ORDER_REQUEST_STATUS = "order-request-status"
            const val ORDER_DETAIL = "order-details"




            private lateinit var appContext: Context

            fun init(context: Context) {
                appContext = context.applicationContext
            }

            fun getAppContext(): Context {
                return appContext
            }


            public fun getApiInterface(): ApiInterface {
                return ApiInstance.instance().create(ApiInterface::class.java);
            }


            public fun getLocTrackingFirebaseDb():DatabaseReference
            {
                 val refPath = "loc_provider_" + MyLocalMemory.getObject(DRIVER_DETAILS, DeliveryBoy()).id.toString()
                 val database = Firebase.database
                 return database.getReference("delivery_boy").child(refPath)
            }

            public fun  getOrderFirebaseDb():DatabaseReference
            {
                val refPath = "loc_provider_" + MyLocalMemory.getObject(DRIVER_DETAILS, DeliveryBoy()).id.toString()
                val database = Firebase.database
                return database.getReference("order").child(refPath)
            }

            public fun <T : Any> callApi(
                call: Call<T>,
                apiName: String,
                apiCallListener: ApiCallListener,
                context: Context,
                showProgressBar: Boolean = true
            ) {
                ApiCall(call, apiName, apiCallListener, context, showProgressBar)
            }


        }
    }
}