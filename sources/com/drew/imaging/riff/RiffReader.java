package com.drew.imaging.riff;

import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/riff/RiffReader.class */
public class RiffReader {
    public void processRiff(@NotNull SequentialReader reader, @NotNull RiffHandler handler) throws RiffProcessingException, IOException {
        reader.setMotorolaByteOrder(false);
        String fileFourCC = reader.getString(4);
        if (!fileFourCC.equals("RIFF")) {
            throw new RiffProcessingException("Invalid RIFF header: " + fileFourCC);
        }
        int fileSize = reader.getInt32();
        String identifier = reader.getString(4);
        int sizeLeft = fileSize - 4;
        if (!handler.shouldAcceptRiffIdentifier(identifier)) {
            return;
        }
        while (sizeLeft != 0) {
            String chunkFourCC = reader.getString(4);
            int chunkSize = reader.getInt32();
            int sizeLeft2 = sizeLeft - 8;
            if (chunkSize < 0 || sizeLeft2 < chunkSize) {
                throw new RiffProcessingException("Invalid RIFF chunk size");
            }
            if (handler.shouldAcceptChunk(chunkFourCC)) {
                handler.processChunk(chunkFourCC, reader.getBytes(chunkSize));
            } else {
                reader.skip(chunkSize);
            }
            sizeLeft = sizeLeft2 - chunkSize;
            if (chunkSize % 2 == 1) {
                reader.getInt8();
                sizeLeft--;
            }
        }
    }
}
