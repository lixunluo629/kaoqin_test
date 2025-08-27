package com.drew.metadata.jpeg;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.ErrorDirectory;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/jpeg/JpegDnlReader.class */
public class JpegDnlReader implements JpegSegmentMetadataReader {
    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.DNL);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            extract(segmentBytes, metadata, segmentType);
        }
    }

    public void extract(byte[] segmentBytes, Metadata metadata, JpegSegmentType segmentType) {
        JpegDirectory directory = (JpegDirectory) metadata.getFirstDirectoryOfType(JpegDirectory.class);
        if (directory == null) {
            ErrorDirectory errorDirectory = new ErrorDirectory();
            metadata.addDirectory(errorDirectory);
            errorDirectory.addError("DNL segment found without SOFx - illegal JPEG format");
            return;
        }
        SequentialReader reader = new SequentialByteArrayReader(segmentBytes);
        try {
            Integer i = directory.getInteger(1);
            if (i == null || i.intValue() == 0) {
                directory.setInt(1, reader.getUInt16());
            }
        } catch (IOException ex) {
            directory.addError(ex.getMessage());
        }
    }
}
