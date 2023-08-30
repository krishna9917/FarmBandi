package com.application.farmbandi.model

data class UserOtpData(
    val access_token: String,
    val is_driver_exits: Boolean,
    val message: String,
    val otp: String,
    val status: Boolean,
    val driver: DeliveryBoy?
){
  constructor(message: String,status: Boolean):this("",false,message,"",status,null)
}