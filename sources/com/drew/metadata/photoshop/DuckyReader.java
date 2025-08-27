package com.drew.metadata.photoshop;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.Charsets;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/photoshop/DuckyReader.class */
public class DuckyReader implements JpegSegmentMetadataReader {

    @NotNull
    private static final String JPEG_SEGMENT_PREAMBLE = "Ducky";

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPC);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = JPEG_SEGMENT_PREAMBLE.length();
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength && JPEG_SEGMENT_PREAMBLE.equals(new String(segmentBytes, 0, preambleLength))) {
                extract(new SequentialByteArrayReader(segmentBytes, preambleLength), metadata);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        int tag;
        DuckyDirectory directory = new DuckyDirectory();
        metadata.addDirectory(directory);
        while (true) {
            try {
                tag = reader.getUInt16();
            } catch (IOException e) {
                directory.addError(e.getMessage());
                return;
            }
            if (tag != 0) {
                int length = reader.getUInt16();
                switch (tag) {
                    case 1:
                        if (length != 4) {
                            directory.addError("Unexpected length for the quality tag");
                            return;
                        } else {
                            directory.setInt(tag, reader.getInt32());
                            continue;
                        }
                    case 2:
                    case 3:
                        reader.skip(4L);
                        directory.setStringValue(tag, reader.getStringValue(length - 4, Charsets.UTF_16BE));
                        continue;
                    default:
                        directory.setByteArray(tag, reader.getBytes(length));
                        continue;
                }
                directory.addError(e.getMessage());
                return;
            }
            return;
        }
    }
}
