package com.drew.metadata;

import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/MetadataReader.class */
public interface MetadataReader {
    void extract(@NotNull RandomAccessReader randomAccessReader, @NotNull Metadata metadata);
}
