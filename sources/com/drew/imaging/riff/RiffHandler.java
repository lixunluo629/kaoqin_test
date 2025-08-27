package com.drew.imaging.riff;

import com.drew.lang.annotations.NotNull;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/riff/RiffHandler.class */
public interface RiffHandler {
    boolean shouldAcceptRiffIdentifier(@NotNull String str);

    boolean shouldAcceptChunk(@NotNull String str);

    void processChunk(@NotNull String str, @NotNull byte[] bArr);
}
