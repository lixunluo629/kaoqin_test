package com.drew.imaging.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/jpeg/JpegSegmentMetadataReader.class */
public interface JpegSegmentMetadataReader {
    @NotNull
    Iterable<JpegSegmentType> getSegmentTypes();

    void readJpegSegments(@NotNull Iterable<byte[]> iterable, @NotNull Metadata metadata, @NotNull JpegSegmentType jpegSegmentType);
}
