package com.drew.imaging.png;

import com.drew.lang.annotations.NotNull;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/png/PngChunk.class */
public class PngChunk {

    @NotNull
    private final PngChunkType _chunkType;

    @NotNull
    private final byte[] _bytes;

    public PngChunk(@NotNull PngChunkType chunkType, @NotNull byte[] bytes) {
        this._chunkType = chunkType;
        this._bytes = bytes;
    }

    @NotNull
    public PngChunkType getType() {
        return this._chunkType;
    }

    @NotNull
    public byte[] getBytes() {
        return this._bytes;
    }
}
