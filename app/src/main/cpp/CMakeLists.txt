cmake_minimum_required(VERSION 3.22.1)
project(llm_jni)

# Build a simple shared library for our JNI bridge
add_library(llm_jni SHARED
        llm_jni.cpp
)

# log for __android_log_print if you need it later
find_library(log-lib log)

target_link_libraries(llm_jni
        ${log-lib}
)
