package com.application.farmbandi.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationButtonReceiver:BroadcastReceiver()
{
    override fun onReceive(p0: Context?,intent: Intent?) {
        when (intent?.action)
        {
            "ACCEPT_ACTION" -> {
                // Handle accept button click
            }
            "REJECT_ACTION" -> {
                // Handle reject button click
            }
        }
    }

}