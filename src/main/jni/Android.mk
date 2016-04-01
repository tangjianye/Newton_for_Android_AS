LOCAL_PATH       := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE     := newton
LOCAL_SRC_FILES  := newton/newton.cpp
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LAME_MODULE_DIR := mp3lame
LAME_LIBMP3_DIR := $(LAME_MODULE_DIR)/lame-3.98.4_libmp3lame
LOCAL_MODULE    := pcm2mp3
LOCAL_SRC_FILES := $(LAME_LIBMP3_DIR)/bitstream.c \
                   $(LAME_LIBMP3_DIR)/fft.c \
                   $(LAME_LIBMP3_DIR)/id3tag.c \
                   $(LAME_LIBMP3_DIR)/mpglib_interface.c \
                   $(LAME_LIBMP3_DIR)/presets.c \
                   $(LAME_LIBMP3_DIR)/quantize.c \
                   $(LAME_LIBMP3_DIR)/reservoir.c \
                   $(LAME_LIBMP3_DIR)/tables.c \
                   $(LAME_LIBMP3_DIR)/util.c \
                   $(LAME_LIBMP3_DIR)/VbrTag.c \
                   $(LAME_LIBMP3_DIR)/encoder.c \
                   $(LAME_LIBMP3_DIR)/gain_analysis.c \
                   $(LAME_LIBMP3_DIR)/lame.c \
                   $(LAME_LIBMP3_DIR)/newmdct.c \
                   $(LAME_LIBMP3_DIR)/psymodel.c \
                   $(LAME_LIBMP3_DIR)/quantize_pvt.c \
                   $(LAME_LIBMP3_DIR)/set_get.c \
                   $(LAME_LIBMP3_DIR)/takehiro.c \
                   $(LAME_LIBMP3_DIR)/vbrquantize.c \
                   $(LAME_LIBMP3_DIR)/version.c \
                   $(LAME_MODULE_DIR)/pcm2mp3.c
                   
include $(BUILD_SHARED_LIBRARY)