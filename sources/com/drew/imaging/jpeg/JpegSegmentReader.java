package com.drew.imaging.jpeg;

import com.drew.lang.SequentialReader;
import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/imaging/jpeg/JpegSegmentReader.class */
public class JpegSegmentReader {
    private static final byte SEGMENT_IDENTIFIER = -1;
    private static final byte SEGMENT_SOS = -38;
    private static final byte MARKER_EOI = -39;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JpegSegmentReader.class.desiredAssertionStatus();
    }

    @NotNull
    public static JpegSegmentData readSegments(@NotNull File file, @Nullable Iterable<JpegSegmentType> segmentTypes) throws IOException, JpegProcessingException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            JpegSegmentData segments = readSegments(new StreamReader(stream), segmentTypes);
            if (stream != null) {
                stream.close();
            }
            return segments;
        } catch (Throwable th) {
            if (stream != null) {
                stream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static JpegSegmentData readSegments(@NotNull SequentialReader reader, @Nullable Iterable<JpegSegmentType> segmentTypes) throws IOException, JpegProcessingException {
        byte segmentType;
        if (!$assertionsDisabled && !reader.isMotorolaByteOrder()) {
            throw new AssertionError();
        }
        int magicNumber = reader.getUInt16();
        if (magicNumber != 65496) {
            throw new JpegProcessingException("JPEG data is expected to begin with 0xFFD8 (ÿØ) not 0x" + Integer.toHexString(magicNumber));
        }
        Set<Byte> segmentTypeBytes = null;
        if (segmentTypes != null) {
            segmentTypeBytes = new HashSet<>();
            Iterator i$ = segmentTypes.iterator();
            while (i$.hasNext()) {
                segmentTypeBytes.add(Byte.valueOf(i$.next().byteValue));
            }
        }
        JpegSegmentData segmentData = new JpegSegmentData();
        while (true) {
            byte segmentIdentifier = reader.getInt8();
            byte int8 = reader.getInt8();
            while (true) {
                segmentType = int8;
                if (segmentIdentifier == -1 && segmentType != -1 && segmentType != 0) {
                    break;
                }
                segmentIdentifier = segmentType;
                int8 = reader.getInt8();
            }
            if (segmentType == SEGMENT_SOS) {
                return segmentData;
            }
            if (segmentType == MARKER_EOI) {
                return segmentData;
            }
            int segmentLength = reader.getUInt16() - 2;
            if (segmentLength < 0) {
                throw new JpegProcessingException("JPEG segment size would be less than zero");
            }
            if (segmentTypeBytes == null || segmentTypeBytes.contains(Byte.valueOf(segmentType))) {
                byte[] segmentBytes = reader.getBytes(segmentLength);
                if (!$assertionsDisabled && segmentLength != segmentBytes.length) {
                    throw new AssertionError();
                }
                segmentData.addSegment(segmentType, segmentBytes);
            } else if (!reader.trySkip(segmentLength)) {
                return segmentData;
            }
        }
    }

    private JpegSegmentReader() throws Exception {
        throw new Exception("Not intended for instantiation.");
    }
}
