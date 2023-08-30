package com.application.farmbandi.model

import java.io.Serializable

data class Order(
    val contact_number: Long,
    val created: String,
    val drop_address: String,
    val drop_date: String,
    val drop_location: String,
    val id: Int,
    val job_id: String,
    val job_title: String,
    val pickup_address: String,
    val pickup_date: String,
    val pickup_location: String,
    val product_image_url: String,
    val quantity: Int,
    val short_desc: String,
    val status: Int,
    val weight: String
) : Serializable {

    constructor() : this(0  , "", "", "", "", 0, "", "", "", "", "", "", 0, "", 0, "")
}
