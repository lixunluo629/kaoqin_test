package com.drew.metadata.adobe;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/adobe/AdobeJpegReader.class */
public class AdobeJpegReader implements JpegSegmentMetadataReader {
    public static final String PREAMBLE = "Adobe";

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPE);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] bytes : segments) {
            if (bytes.length == 12 && PREAMBLE.equalsIgnoreCase(new String(bytes, 0, PREAMBLE.length()))) {
                extract(new SequentialByteArrayReader(bytes), metadata);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        AdobeJpegDirectory adobeJpegDirectory = new AdobeJpegDirectory();
        metadata.addDirectory(adobeJpegDirectory);
        try {
            reader.setMotorolaByteOrder(false);
            if (!reader.getString(PREAMBLE.length()).equals(PREAMBLE)) {
                adobeJpegDirectory.addError("Invalid Adobe JPEG data header.");
                return;
            }
            adobeJpegDirectory.setInt(0, reader.getUInt16());
            adobeJpegDirectory.setInt(1, reader.getUInt16());
            adobeJpegDirectory.setInt(2, reader.getUInt16());
            adobeJpegDirectory.setInt(3, reader.getInt8());
        } catch (IOException ex) {
            adobeJpegDirectory.addError("IO exception processing data: " + ex.getMessage());
        }
    }
}
