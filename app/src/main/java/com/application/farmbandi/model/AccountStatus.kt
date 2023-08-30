package com.application.farmbandi.model

data class AccountStatus(
    val documents: ArrayList<Document>,
    val driver: DeliveryBoy,
    val message: String,
    val status: Boolean
)