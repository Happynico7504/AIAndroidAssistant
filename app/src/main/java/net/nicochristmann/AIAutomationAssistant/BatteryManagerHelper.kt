// BatteryManagerHelper.kt
package com.example.myaihelper

import android.content.Context
import android.os.BatteryManager

class BatteryManagerHelper(val context: Context) {
    companion object {
        const val SAFE_BATTERY_THRESHOLD = 60
    }

    fun getBatteryLevel(): Int {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    fun isCharging(): Boolean {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.isCharging
    }

    fun canRunHeavyTasks(): Boolean {
        return getBatteryLevel() >= SAFE_BATTERY_THRESHOLD || isCharging()
    }
}
