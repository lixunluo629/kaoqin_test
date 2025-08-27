package com.drew.imaging.webp;

import com.drew.imaging.riff.RiffProcessingException;
import com.drew.imaging.riff.RiffReader;
import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileMetadataReader;
import com.drew.metadata.webp.WebpRiffHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/webp/WebpMetadataReader.class */
public class WebpMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws RiffProcessingException, IOException {
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
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws RiffProcessingException, IOException {
        Metadata metadata = new Metadata();
        new RiffReader().processRiff(new StreamReader(inputStream), new WebpRiffHandler(metadata));
        return metadata;
    }
}
