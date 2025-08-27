package com.drew.metadata.jpeg;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jpeg/JpegCommentReader.class */
public class JpegCommentReader implements JpegSegmentMetadataReader {
    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.COM);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            JpegCommentDirectory directory = new JpegCommentDirectory();
            metadata.addDirectory(directory);
            directory.setStringValue(0, new StringValue(segmentBytes, null));
        }
    }
}
