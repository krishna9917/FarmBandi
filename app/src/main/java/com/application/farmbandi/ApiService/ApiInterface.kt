package com.application.farmbandi.ApiService

import com.application.farmbandi.model.AccountStatus
import com.application.farmbandi.model.AvailabilityStatus
import com.application.farmbandi.model.Cities
import com.application.farmbandi.model.DriverDetails
import com.application.farmbandi.model.Jobs
import com.application.farmbandi.model.OrderDetail
import com.application.farmbandi.model.States
import com.application.farmbandi.model.StatusMessage
import com.application.farmbandi.model.UserOtpData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiInterface {


    @FormUrlEncoded
    @POST("send-otp")
    fun otpSend(@Field("mobile") mobile: String): Call<UserOtpData>


    @GET("states")
    fun getStates(): Call<States>


    @GET("cities/{id}")
    fun getCities(@Path("id") id: String): Call<Cities>


    @Multipart
    @POST("driver-register")
    fun driverRegister(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
        @Part profile_pic: MultipartBody.Part,
        @Part aadhar_front: MultipartBody.Part,
        @Part aadhar_back: MultipartBody.Part,
        @Part driving_licence_front: MultipartBody.Part,
        @Part driving_licence_back: MultipartBody.Part,
        @Part("state_id") state_id: RequestBody,
        @Part("city_id") city_id: RequestBody
    ): Call<DriverDetails>


    @FormUrlEncoded
    @POST("driver-login")
    fun driverLogin(@Field("email") email: String, @Field("password") password: String): Call<DriverDetails>


    @POST("check-account-status")
    fun getAccountStatus():Call<AccountStatus>


    @GET("assignedOrders")
    fun getJobs():Call<Jobs>


    @FormUrlEncoded
    @POST("driver-available-status")
    fun setAvailabilityStatus(@Field("latitude")latitude:String,@Field("longitude")longitude:String,@Field("availability_status")status:Int):Call<AvailabilityStatus>



    @FormUrlEncoded
    @POST("order-request-status")
    fun updateOrderStatus(@Field("job_id")jobId:String,@Field("request_status")request_status:String):Call<StatusMessage>



    @FormUrlEncoded
    @POST("order-details")
    fun orderDetail(@Field("job_id")jobId:String):Call<OrderDetail>

}

