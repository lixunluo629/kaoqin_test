package com.drew.metadata.ico;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/ico/IcoReader.class */
public class IcoReader {
    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata) {
        reader.setMotorolaByteOrder(false);
        try {
            int reserved = reader.getUInt16();
            if (reserved != 0) {
                IcoDirectory directory = new IcoDirectory();
                directory.addError("Invalid header bytes");
                metadata.addDirectory(directory);
                return;
            }
            int type = reader.getUInt16();
            if (type != 1 && type != 2) {
                IcoDirectory directory2 = new IcoDirectory();
                directory2.addError("Invalid type " + type + " -- expecting 1 or 2");
                metadata.addDirectory(directory2);
                return;
            }
            int imageCount = reader.getUInt16();
            if (imageCount == 0) {
                IcoDirectory directory3 = new IcoDirectory();
                directory3.addError("Image count cannot be zero");
                metadata.addDirectory(directory3);
                return;
            }
            for (int imageIndex = 0; imageIndex < imageCount; imageIndex++) {
                IcoDirectory directory4 = new IcoDirectory();
                try {
                    directory4.setInt(1, type);
                    directory4.setInt(2, reader.getUInt8());
                    directory4.setInt(3, reader.getUInt8());
                    directory4.setInt(4, reader.getUInt8());
                    reader.getUInt8();
                    if (type == 1) {
                        directory4.setInt(5, reader.getUInt16());
                        directory4.setInt(7, reader.getUInt16());
                    } else {
                        directory4.setInt(6, reader.getUInt16());
                        directory4.setInt(8, reader.getUInt16());
                    }
                    directory4.setLong(9, reader.getUInt32());
                    directory4.setLong(10, reader.getUInt32());
                } catch (IOException ex) {
                    directory4.addError("Exception reading ICO file metadata: " + ex.getMessage());
                }
                metadata.addDirectory(directory4);
            }
        } catch (IOException ex2) {
            IcoDirectory directory5 = new IcoDirectory();
            directory5.addError("Exception reading ICO file metadata: " + ex2.getMessage());
            metadata.addDirectory(directory5);
        }
    }
}
