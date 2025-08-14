package net.nicochristmann.AIAutomationAssistant

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            androidx.compose.material3.MaterialTheme {
                androidx.compose.material3.Surface {
                    androidx.compose.material3.Text("AI Assistant running...")
                }
            }        
         }

        // Start AI service
        val intent = Intent(this, AIService::class.java)
        startForegroundService(intent)
    }
}
