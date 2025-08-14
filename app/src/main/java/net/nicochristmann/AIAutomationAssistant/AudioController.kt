package net.nicochristmann.AIAutomationAssistant

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class AudioController(private val context: Context) {

    private val sampleRate = 32000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO // safer across devices
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        channelConfig,
        audioFormat
    )

    private var recorder: AudioRecord? = null
    private var recordingJob: Job? = null

    /** Check if RECORD_AUDIO permission is granted */
    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Start recording audio frames with coroutine */
    fun startRecording(onAudioFrame: (ShortArray) -> Unit) {
        if (!hasAudioPermission()) {
            throw SecurityException("RECORD_AUDIO permission not granted")
        }

        if (recordingJob?.isActive == true) return
        @SuppressLint("MissingPermission")
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        recorder?.startRecording()

        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ShortArray(bufferSize)
            while (isActive) {
                val read = recorder?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) onAudioFrame(buffer.copyOf(read))
            }
        }
    }

    /** Stop recording safely */
    fun stop() {
        recordingJob?.cancel()
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    /** Optional: Downmix stereo to mono if needed */
    fun downmixStereoToMono(input: ShortArray): ShortArray {
        val mono = ShortArray(input.size / 2)
        for (i in mono.indices) {
            mono[i] = ((input[i * 2] + input[i * 2 + 1]) / 2).toShort()
        }
        return mono
    }
}
