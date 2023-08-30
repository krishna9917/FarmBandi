package com.application.farmbandi.model

data class DeliveryBoy(
    val aadhar: Aadhar?,
    val aadhar_images_data: AadharImagesData?,
    val address: String,
    val city_id: Int,
    val dob: String,
    val driving_license: DrivingLicense?,
    val driving_license_images_data: DrivingLicenseImagesData?,
    val email: String,
    val first_name: String,
    val full_name: String,
    val gender: String,
    val id: Int,
    var isOnline: Int,
    val last_name: String,
    var latitude: String,
    var longitude: String,
    val mobile: String,
    val mobile_verify: Int,
    val profile_pic: String,
    val profile_pic_url: String,
    val status: Int=0,
    val remarks:String,
    val city_name:String,
    val state_name:String
)
{
    constructor():this(null,null,"",0,"",null,null,"","","","",0,0,"","","","",0,"","",0,"","","")
}