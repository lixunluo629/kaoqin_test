package com.drew.imaging.bmp;

import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.bmp.BmpReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/bmp/BmpMetadataReader.class */
public class BmpMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            Metadata metadata = readMetadata(stream);
            if (stream != null) {
                stream.close();
            }
            return metadata;
        } catch (Throwable th) {
            if (stream != null) {
                stream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) {
        Metadata metadata = new Metadata();
        new BmpReader().extract(new StreamReader(inputStream), metadata);
        return metadata;
    }
}
