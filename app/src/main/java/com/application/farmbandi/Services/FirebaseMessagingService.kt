package com.application.farmbandi.Services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.application.farmbandi.Activities.OrderDetail.OrderDetailActivity
import com.application.farmbandi.R
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.MyApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FirebaseMessagingService : FirebaseMessagingService() {

    private var channelId = "notification_channel"
    private val now: Date = Date()
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        @SuppressLint("HardwareIds") val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        MyLocalMemory.putString(MyApp.MySingleton.DEVICE_ID, deviceId)
        MyLocalMemory.putString(MyApp.MySingleton.DEVICE_TOKEN, token)
    }

        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)

            try {
                val jsonObject = JSONObject(message.notification!!.body)
                val quantity = jsonObject.getString("quantity")
                val jobId = jsonObject.getString("job_id")
                val pickupAddress = jsonObject.getString("pickup_address")
                val jobTitle  = jsonObject.getString("job_title")
                showNotification("$jobTitle ($quantity Items)",pickupAddress,jobId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    @SuppressLint("RemoteViewLayout")
    private fun showNotification(title:String, message: String,jobId:String)
    {
//        val notificationLayout = RemoteViews(packageName, R.layout.layout_job_assign_notification)
        val soundUri = Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.alert_tone)
        val intent = Intent(this,OrderDetailActivity::class.java)
         intent.putExtra("Job_id",jobId)

        channelId = SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt().toString()
        createChannel(message)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.drawable.ic_logo)
        builder.setContentTitle(title)
        builder.setContentText(message)
        builder.setVibrate(longArrayOf(0, 1000, 500, 1000))
        builder.setSound(soundUri)
//        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
//        builder.setCustomBigContentView(notificationLayout)
//        builder.setContent(notificationLayout)
//        builder.setCustomContentView(notificationLayout)
        builder.setAutoCancel(false)


//        if (imageUrl != null) {
//            if (imageUrl.toString().length() > 10 && imageUrl.toString().startsWith("http")) {
//                val bitmap: Bitmap = getBitmapFromUrl(imageUrl)
//                builder.setLargeIcon(bitmap)
//                builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
//            }
//        }

        builder.priority = NotificationCompat.PRIORITY_HIGH


        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setContentIntent(pendingIntent)

        val managerCompat = NotificationManagerCompat.from(
            applicationContext
        )
        val notificationId = SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                managerCompat.notify(notificationId, builder.build())
            }
        } else {
            managerCompat.notify(notificationId, builder.build())
        }
    }


    private fun createChannel(message:String)
    {
        val channelName = getString(R.string.app_name)+ SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = message
            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}