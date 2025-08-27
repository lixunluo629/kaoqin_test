package com.drew.lang;

import com.drew.lang.annotations.NotNull;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/ByteConvert.class */
public class ByteConvert {
    public static int toInt32BigEndian(@NotNull byte[] bytes) {
        return ((bytes[0] << 24) & (-16777216)) | ((bytes[1] << 16) & 16711680) | ((bytes[2] << 8) & 65280) | (bytes[3] & 255);
    }

    public static int toInt32LittleEndian(@NotNull byte[] bytes) {
        return (bytes[0] & 255) | ((bytes[1] << 8) & 65280) | ((bytes[2] << 16) & 16711680) | ((bytes[3] << 24) & (-16777216));
    }
}
