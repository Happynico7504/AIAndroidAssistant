package net.nicochristmann.AIAutomationAssistant

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

class CameraController(val context: Context) {

    fun startCamera(onFrame: (ImageProxy) -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                        onFrame(image)
                        image.close()
                    }
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(context as androidx.lifecycle.LifecycleOwner,
                cameraSelector, imageAnalyzer)
        }, ContextCompat.getMainExecutor(context))
    }
}
