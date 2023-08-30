package com.application.farmbandi.Utils


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.application.farmbandi.Activities.Login.LoginActivity
import com.application.farmbandi.AppInterfaces.CurrentLocationListener
import com.application.farmbandi.R
import com.application.farmbandi.Services.LocationService
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.model.DeliveryBoy
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class UtilsFunction {

    companion object {

        fun setAnimation(context: Context?, viewToAnimate: View, animationId: Int) {
            val animation = AnimationUtils.loadAnimation(context, animationId)
            viewToAnimate.startAnimation(animation)
        }


        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // For devices running Android Q and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val capabilities =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            // For devices running below Android Q
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }


        fun successToast(context: Context, message: String) {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }


        fun errorToast(context: Context, message: String)
        {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
            vibration(context)
        }

        fun infoToast(context: Context, message: String)
        {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }

        fun deletionToast(context: Context,message: String)
        {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }


        fun vibration(context: Context) {
            var vibrator: Vibrator
            var vibratorManager: VibratorManager
            if (Build.VERSION.SDK_INT >= 31) {
                vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibrator = vibratorManager.defaultVibrator
            } else {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                //deprecated in API 26
                vibrator.vibrate(500);
            }
        }




        lateinit var locationListener: CurrentLocationListener
        @SuppressLint("StaticFieldLeak")
        private lateinit var locContext: Context
        @SuppressLint("SuspiciousIndentation")
        fun getLastLocation(context: Context, locListener: CurrentLocationListener)
        {
            locContext = context
            locationListener = locListener
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (isLocationEnabled(context))
                {
                    fusedLocationClient.lastLocation
                        .addOnCompleteListener { task ->
                            val location = task.result
                            try {
                                if (location == null) {
                                    val locationRequest =
                                        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5)
                                            .setWaitForAccurateLocation(false)
                                            .setMinUpdateIntervalMillis(0)
                                            .setMaxUpdateDelayMillis(5)
                                            .build()

                                        fusedLocationClient.requestLocationUpdates(
                                            locationRequest,
                                            mLocationCallback,
                                            Looper.myLooper()
                                        )
                                } else {
                                    saveUserLocation(location.latitude,location.longitude)
                                }
                            } catch (e: Exception) {
                                locListener.onError(e.message!!)
                            }
                        }
                } else {
                    locListener.onError("")
                    infoToast(context, "Please turn on" + " your location...")
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }
            } else {
                locListener.onError("")
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                    101
                )
            }
        }

        private fun isLocationEnabled(context: Context): Boolean {
            val locationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        private val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val mLastLocation = locationResult.lastLocation
                saveUserLocation(mLastLocation?.latitude,mLastLocation?.longitude)
            }
        }

        fun checkLocationPermission(context: Context): Boolean {
            val result = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val result1 = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }


        fun saveUserLocation(latitude: Double?, longitude: Double?)
        {
            val driver =MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS,DeliveryBoy())
            driver.latitude = latitude.toString()
            driver.longitude = longitude.toString()
            MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS,driver)
            LocalBroadcastManager.getInstance(locContext).sendBroadcast(Intent("update_location"))
        }


        fun logoutUser(context: Context)
        {
            context.stopService(Intent(context, LocationService::class.java))
            context.startActivity(Intent(context, LoginActivity::class.java))
            MyLocalMemory.clearMemory()
        }


    }



}