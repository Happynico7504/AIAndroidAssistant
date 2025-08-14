#include <jni.h>
#include <string>

// Stage A: minimal stub implementation so the app builds and runs.
// Stage B: replace this with real llama.cpp code that loads a GGUF model
// and performs inference on-device.

extern "C"
JNIEXPORT jboolean JNICALL
Java_net_nicochristmann_AIAutomationAssistant_LLMEngine_nativeInit(
        JNIEnv* env, jobject thiz, jstring modelPath_) {
    // For Stage A, do nothing and pretend success.
    // In Stage B: read modelPath_, initialize llama.cpp and keep a global context.
    (void) env; (void) thiz; (void) modelPath_;
    return JNI_TRUE;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_net_nicochristmann_AIAutomationAssistant_LLMEngine_nativeGenerate(
        JNIEnv* env, jobject /*thiz*/, jstring prompt_, jint max_tokens) {
    const char* prompt_c = env->GetStringUTFChars(prompt_, nullptr);

    // Stage A: Return a deterministic stub so you can test flow + TTS.
    std::string result = "[LOCAL LLM STUB] You said: ";
    result += prompt_c;
    result += "  | maxTokens=" + std::to_string(max_tokens);

    env->ReleaseStringUTFChars(prompt_, prompt_c);
    return env->NewStringUTF(result.c_str());
}
