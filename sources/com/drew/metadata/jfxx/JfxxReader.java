package com.drew.metadata.jfxx;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import java.io.IOException;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jfxx/JfxxReader.class */
public class JfxxReader implements JpegSegmentMetadataReader, MetadataReader {
    public static final String PREAMBLE = "JFXX";

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP0);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= PREAMBLE.length() && PREAMBLE.equals(new String(segmentBytes, 0, PREAMBLE.length()))) {
                extract(new ByteArrayReader(segmentBytes), metadata);
            }
        }
    }

    @Override // com.drew.metadata.MetadataReader
    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata) {
        JfxxDirectory directory = new JfxxDirectory();
        metadata.addDirectory(directory);
        try {
            directory.setInt(5, reader.getUInt8(5));
        } catch (IOException me) {
            directory.addError(me.getMessage());
        }
    }
}
