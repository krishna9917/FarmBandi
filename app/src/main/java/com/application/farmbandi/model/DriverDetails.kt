package com.application.farmbandi.model

data class DriverDetails(
    val access_token: String,
    val driver: DeliveryBoy?,
    val message: String,
    val status: Boolean
)
{
    constructor(message: String,status: Boolean):this("",null,message, status)
}