package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "🔁 Reiniciando CommandService após boot.")
            val serviceIntent = Intent(context, CommandService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
