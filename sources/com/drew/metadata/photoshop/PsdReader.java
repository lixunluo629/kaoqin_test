package com.drew.metadata.photoshop;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/photoshop/PsdReader.class */
public class PsdReader {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PsdReader.class.desiredAssertionStatus();
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) throws ImageProcessingException {
        PsdHeaderDirectory directory = new PsdHeaderDirectory();
        metadata.addDirectory(directory);
        try {
            int signature = reader.getInt32();
            if (signature != 943870035) {
                directory.addError("Invalid PSD file signature");
                return;
            }
            int version = reader.getUInt16();
            if (version != 1 && version != 2) {
                directory.addError("Invalid PSD file version (must be 1 or 2)");
                return;
            }
            reader.skip(6L);
            int channelCount = reader.getUInt16();
            directory.setInt(1, channelCount);
            int imageHeight = reader.getInt32();
            directory.setInt(2, imageHeight);
            int imageWidth = reader.getInt32();
            directory.setInt(3, imageWidth);
            int bitsPerChannel = reader.getUInt16();
            directory.setInt(4, bitsPerChannel);
            int colorMode = reader.getUInt16();
            directory.setInt(5, colorMode);
            try {
                reader.skip(reader.getUInt32());
                try {
                    long sectionLength = reader.getUInt32();
                    if (!$assertionsDisabled && sectionLength > 2147483647L) {
                        throw new AssertionError();
                    }
                    new PhotoshopReader().extract(reader, (int) sectionLength, metadata);
                } catch (IOException e) {
                }
            } catch (IOException e2) {
            }
        } catch (IOException e3) {
            directory.addError("Unable to read PSD header");
        }
    }
}
