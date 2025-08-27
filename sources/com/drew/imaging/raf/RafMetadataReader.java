package com.drew.imaging.raf;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/raf/RafMetadataReader.class */
public class RafMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws IOException, JpegProcessingException {
        if (!inputStream.markSupported()) {
            throw new IOException("Stream must support mark/reset");
        }
        inputStream.mark(512);
        byte[] data = new byte[512];
        int bytesRead = inputStream.read(data);
        if (bytesRead == -1) {
            throw new IOException("Stream is empty");
        }
        inputStream.reset();
        int i = 0;
        while (true) {
            if (i >= bytesRead - 2) {
                break;
            }
            if (data[i] != -1 || data[i + 1] != -40 || data[i + 2] != -1) {
                i++;
            } else {
                long bytesSkipped = inputStream.skip(i);
                if (bytesSkipped != i) {
                    throw new IOException("Skipping stream bytes failed");
                }
            }
        }
        return JpegMetadataReader.readMetadata(inputStream);
    }

    private RafMetadataReader() throws Exception {
        throw new Exception("Not intended for instantiation");
    }
}
