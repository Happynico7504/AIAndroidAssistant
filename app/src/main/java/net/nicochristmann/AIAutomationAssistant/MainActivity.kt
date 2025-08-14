// MainActivity.kt
package com.example.myaihelper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Your Compose UI or simple layout
        }

        // Start AI service
        val intent = Intent(this, AIService::class.java)
        startForegroundService(intent)
    }
}
