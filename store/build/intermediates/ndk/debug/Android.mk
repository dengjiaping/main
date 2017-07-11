LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := Crc8Jni
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	F:\seven7\android\trunk\main\store\src\main\jni\crc8.c \
	F:\seven7\android\trunk\main\store\src\main\jni\empty.c \

LOCAL_C_INCLUDES += F:\seven7\android\trunk\main\store\src\main\jni
LOCAL_C_INCLUDES += F:\seven7\android\trunk\main\store\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
