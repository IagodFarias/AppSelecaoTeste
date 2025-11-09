package com.example.myapplication

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d("DeviceAdmin", "✅ Device Admin: Habilitado")
        Toast.makeText(context, "DPC Habilitado", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.d("DeviceAdmin", "❌ Device Admin: Desabilitado")
        Toast.makeText(context, "DPC Desabilitado", Toast.LENGTH_SHORT).show()
    }
}
