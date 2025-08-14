package net.nicochristmann.AIAutomationAssistant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import java.util.*

class AIService : Service() {

    private lateinit var tts: TextToSpeech
    private lateinit var batteryHelper: BatteryManagerHelper

    override fun onCreate() {
        super.onCreate()
        batteryHelper = BatteryManagerHelper(this)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) tts.language = Locale.US
        }
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "ai_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "AI Service", NotificationManager.IMPORTANCE_LOW)
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("AI Assistant Running")
            .setContentText("Battery-aware AI service")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        startForeground(1, notification)
    }

    fun notifyBatteryLow() {
        tts.speak(
            "Battery is low. Some heavy tasks are paused to save power.",
            TextToSpeech.QUEUE_FLUSH,
            null,
            "battery_low"
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Here you can schedule tasks, call LLM inference, check battery
        if (!batteryHelper.canRunHeavyTasks()) notifyBatteryLow()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
