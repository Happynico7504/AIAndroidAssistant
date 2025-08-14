package net.nicochristmann.AIAutomationAssistant

class LLMEngine {
    init { System.loadLibrary("llama_jni") }

    external fun generate(prompt: String): String
}
