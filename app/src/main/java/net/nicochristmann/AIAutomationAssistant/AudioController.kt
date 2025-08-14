package net.nicochristmann.AIAutomationAssistant

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.concurrent.thread

class AudioController {

    private val sampleRate = 32000
    private val channelConfig = AudioFormat.CHANNEL_IN_STEREO
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        channelConfig,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val recorder = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        channelConfig,               // stereo input
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    @Volatile
    private var isRecording = false

    /**
     * Start recording audio frames.
     * @param onAudioFrame Callback for each captured frame.
     */
    fun startRecording(onAudioFrame: (ShortArray) -> Unit) {
        if (isRecording) return
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

    /** Stop recording safely */
    fun stop() {
        isRecording = false
    }

    /**
     * Optional: Downmix stereo buffer to mono
     * Useful if your AI model expects mono audio
     */
    fun downmixStereoToMono(input: ShortArray): ShortArray {
        val mono = ShortArray(input.size / 2)
        for (i in mono.indices) {
            mono[i] = ((input[i * 2] + input[i * 2 + 1]) / 2).toShort()
        }
        return mono
    }
}
