package com.application.farmbandi.ApiService

import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiInstance
{

    fun instance():Retrofit
    {
        return Retrofit.Builder().baseUrl(MyApp.MySingleton.BASE_URL).client(getHttpClient()!!).addConverterFactory(GsonConverterFactory.create()).build()
    }
    private fun getHttpClient(): OkHttpClient?
    {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor)
            .addInterceptor(HeaderInterceptor())
            .build()
    }
    private class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest: Request = chain.request()
            val modifiedRequest: Request = originalRequest.newBuilder()
                .header("Authorization", "Bearer "+ MyLocalMemory.getString(MyApp.MySingleton.USER_TOKEN))
                .header("Accept", "application/json")
                .header("Api-Key", "drtngysdrgxdrtse5raeefrserw35")
                .header("Device-Token",MyLocalMemory.getString(MyApp.MySingleton.DEVICE_TOKEN))
//                .header("city_name", MyLocalMemory.getString(MyApp.MySingleton.CURRENT_CITY))
                .build()

            return chain.proceed(modifiedRequest)
        }
    }
}