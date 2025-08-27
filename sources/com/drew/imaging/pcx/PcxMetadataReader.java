package com.drew.imaging.pcx;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileMetadataReader;
import com.drew.metadata.pcx.PcxReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/pcx/PcxMetadataReader.class */
public class PcxMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            Metadata metadata = readMetadata(inputStream);
            inputStream.close();
            new FileMetadataReader().read(file, metadata);
            return metadata;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws ImageProcessingException {
        Metadata metadata = new Metadata();
        new PcxReader().extract(new StreamReader(inputStream), metadata);
        return metadata;
    }
}
