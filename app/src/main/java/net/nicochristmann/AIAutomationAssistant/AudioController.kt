package net.nicochristmann.AIAutomationAssistant

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.concurrent.thread

class AudioController {

    private val sampleRate = 32000
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val recorder = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_STEREO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    private var isRecording = false

    fun startRecording(onAudioFrame: (ShortArray) -> Unit) {
        isRecording = true
        thread {
            recorder.startRecording()
            val buffer = ShortArray(bufferSize)
            while (isRecording) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) onAudioFrame(buffer.copyOf(read))
            }
            recorder.stop()
        }
    }

    fun stop() {
        isRecording = false
    }
