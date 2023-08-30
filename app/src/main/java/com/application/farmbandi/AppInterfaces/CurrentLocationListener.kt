package com.application.farmbandi.AppInterfaces

interface CurrentLocationListener
{
    fun onGetCurrentLocation(string: String)
    fun onError(string: String)
}