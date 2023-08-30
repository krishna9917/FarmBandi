package com.application.farmbandi.ApiService

import android.util.Log
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiManager {
    private  val TAG = "LOCATION----->"
    private val client = OkHttpClient()

    suspend fun triggerBroadcastLocation(data:String) {
        val requestBody =   data.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(MyApp.MySingleton.BASE_URL+"broadcast-location")
            .post(requestBody)
            .addHeader("Authorization", "Bearer "+ MyLocalMemory.getString(MyApp.MySingleton.USER_TOKEN))
            .addHeader("Api-Key", "drtngysdrgxdrtse5raeefrserw35")
            .build()

        val response = client.newCall(request).execute()
        Log.d(TAG, "triggerBroadcastLocation: "+response.message)

    }
}