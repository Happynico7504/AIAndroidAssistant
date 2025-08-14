package net.nicochristmann.AIAutomationAssistant

import java.io.File

/**
 * LLMEngine is a thin Kotlin wrapper around our native (C++) inference code.
 * Stage A: works as a stub (echoes prompt) so you can test end-to-end.
 * Stage B: replace JNI impl with llama.cpp calls, load a real GGUF model from disk.
 */
class LLMEngine(private val modelPath: File? = null) {

    init {
        // Loads libllm_jni.so produced by CMake
        System.loadLibrary("llm_jni")
    }

    /**
     * Initialize the model. In Stage A, this is a no-op and returns true.
     * In Stage B, pass modelPath.absolutePath to the native layer to load the GGUF.
     */
    fun initModel(): Boolean {
        val p = modelPath?.absolutePath ?: ""
        return nativeInit(p)
    }

    /**
     * Generate text from a prompt (blocking call).
     * DO NOT call on the main thread; run from a background thread.
     */
    fun generate(prompt: String, maxTokens: Int = 128): String {
        return nativeGenerate(prompt, maxTokens)
    }

    // --- JNI bindings ---
    private external fun nativeInit(modelPath: String): Boolean
    private external fun nativeGenerate(prompt: String, maxTokens: Int): String
}
