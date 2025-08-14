#include <jni.h>
#include "llama.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_net_nicochristmann_AIAutomationAssistant_LLMEngine_generate(
        JNIEnv* env, jobject, jstring prompt_) {
    const char* prompt = env->GetStringUTFChars(prompt_, 0);

    std::string result = "Stub: model response for ";
    result += prompt;

    env->ReleaseStringUTFChars(prompt_, prompt);
    return env->NewStringUTF(result.c_str());
}
