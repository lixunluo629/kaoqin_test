package com.drew.imaging.tiff;

import com.alibaba.excel.constant.ExcelXmlConstants;
import com.drew.lang.RandomAccessFileReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.RandomAccessStreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifTiffHandler;
import com.drew.metadata.file.FileMetadataReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/tiff/TiffMetadataReader.class */
public class TiffMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws TiffProcessingException, IOException {
        Metadata metadata = new Metadata();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, ExcelXmlConstants.POSITION);
        try {
            ExifTiffHandler handler = new ExifTiffHandler(metadata, null);
            new TiffReader().processTiff(new RandomAccessFileReader(randomAccessFile), handler, 0);
            randomAccessFile.close();
            new FileMetadataReader().read(file, metadata);
            return metadata;
        } catch (Throwable th) {
            randomAccessFile.close();
            throw th;
        }
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws TiffProcessingException, IOException {
        return readMetadata(new RandomAccessStreamReader(inputStream));
    }

    @NotNull
    public static Metadata readMetadata(@NotNull RandomAccessReader reader) throws TiffProcessingException, IOException {
        Metadata metadata = new Metadata();
        ExifTiffHandler handler = new ExifTiffHandler(metadata, null);
        new TiffReader().processTiff(reader, handler, 0);
        return metadata;
    }
}
