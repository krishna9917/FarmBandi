package com.application.farmbandi.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.model.DeliveryBoy
import com.application.farmbandi.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

//    private val socketManager = SocketManager
//    val apiManager = ApiManager()


    companion object {
        private const val TAG = "LOCATION----->"
        private const val NOTIFICATION_ID = 123
        var isServiceRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onDestroy: "+MyLocalMemory.getString(MyApp.MySingleton.USER_TOKEN))
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationCallback()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
//        socketManager.closeWebSocket()
        stopLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "start service")
//        socketManager.startWebSocket(
//            "wss://www.website4test.com:6001/farm-bandi/app/gfuhygdsafg",
//            object : WebSocketListener() {
//                override fun onOpen(webSocket: WebSocket, response: Response) {
//                    super.onOpen(webSocket, response)
//
//                    Log.d(TAG, "onOpen: $response")
//                }
//
//                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                    super.onFailure(webSocket, t, response)
//                    Log.d(TAG, "onFailure: " + response?.message.toString())
//                    Log.d(TAG, "onFailure: " + t.message)
//
//                }
//
//                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                    super.onClosed(webSocket, code, reason)
//                    Log.d(TAG, "onClose: $reason")
//                }
//            })
        isServiceRunning = true
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createLocationCallback()
    {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for (location in it.locations)
                    {
                        val driver =MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS,DeliveryBoy())
                        driver.latitude = location.latitude.toString()
                        driver.longitude = location.longitude.toString()
                        MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS,driver)

                        try {
                           MyApp.MySingleton.getLocTrackingFirebaseDb().setValue(Location(location.longitude,location.latitude,MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS,DeliveryBoy()).full_name,MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS,DeliveryBoy()).id.toString()))
                        }catch (e:Exception){

                        }
                        // Trigger Laravel route and event
//                        GlobalScope.launch(Dispatchers.IO) {
//                            apiManager.triggerBroadcastLocation(data.toString())
//                        }
                        Log.d(TAG, "New Location: ${location.latitude}, ${location.longitude}")

                    }
                }
            }

        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(4000)
            .setMaxUpdateDelayMillis(5000)
            .build()
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }



    private fun stopLocationUpdates()
    {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }catch (e:Exception)
        { }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            stopForeground(STOP_FOREGROUND_DETACH)

        } else
        {
            stopForeground(true)

        }
        stopSelf()
    }


    private fun createNotification(): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"
        val channelName = "Location Service Channel"
        val channelDescription = "Channel for location service notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking your location")
            .setContentText("Running")
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }




}