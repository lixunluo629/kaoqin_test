package com.drew.metadata.icc;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.DateUtil;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import java.io.IOException;
import java.util.Collections;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/icc/IccReader.class */
public class IccReader implements JpegSegmentMetadataReader, MetadataReader {
    public static final String JPEG_SEGMENT_PREAMBLE = "ICC_PROFILE";

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP2);
    }

    @Override // com.drew.imaging.jpeg.JpegSegmentMetadataReader
    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = JPEG_SEGMENT_PREAMBLE.length();
        byte[] buffer = null;
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength && JPEG_SEGMENT_PREAMBLE.equalsIgnoreCase(new String(segmentBytes, 0, preambleLength))) {
                if (buffer == null) {
                    buffer = new byte[segmentBytes.length - 14];
                    System.arraycopy(segmentBytes, 14, buffer, 0, segmentBytes.length - 14);
                } else {
                    byte[] newBuffer = new byte[(buffer.length + segmentBytes.length) - 14];
                    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                    System.arraycopy(segmentBytes, 14, newBuffer, buffer.length, segmentBytes.length - 14);
                    buffer = newBuffer;
                }
            }
        }
        if (buffer != null) {
            extract(new ByteArrayReader(buffer), metadata);
        }
    }

    @Override // com.drew.metadata.MetadataReader
    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata) {
        extract(reader, metadata, null);
    }

    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        IccDirectory directory = new IccDirectory();
        if (parentDirectory != null) {
            directory.setParent(parentDirectory);
        }
        try {
            int profileByteCount = reader.getInt32(0);
            directory.setInt(0, profileByteCount);
            set4ByteString(directory, 4, reader);
            setInt32(directory, 8, reader);
            set4ByteString(directory, 12, reader);
            set4ByteString(directory, 16, reader);
            set4ByteString(directory, 20, reader);
            setDate(directory, 24, reader);
            set4ByteString(directory, 36, reader);
            set4ByteString(directory, 40, reader);
            setInt32(directory, 44, reader);
            set4ByteString(directory, 48, reader);
            int temp = reader.getInt32(52);
            if (temp != 0) {
                if (temp <= 538976288) {
                    directory.setInt(52, temp);
                } else {
                    directory.setString(52, getStringFromInt32(temp));
                }
            }
            setInt32(directory, 64, reader);
            setInt64(directory, 56, reader);
            float[] xyz = {reader.getS15Fixed16(68), reader.getS15Fixed16(72), reader.getS15Fixed16(76)};
            directory.setObject(68, xyz);
            int tagCount = reader.getInt32(128);
            directory.setInt(128, tagCount);
            for (int i = 0; i < tagCount; i++) {
                int pos = 132 + (i * 12);
                int tagType = reader.getInt32(pos);
                int tagPtr = reader.getInt32(pos + 4);
                int tagLen = reader.getInt32(pos + 8);
                byte[] b = reader.getBytes(tagPtr, tagLen);
                directory.setByteArray(tagType, b);
            }
        } catch (IOException ex) {
            directory.addError("Exception reading ICC profile: " + ex.getMessage());
        }
        metadata.addDirectory(directory);
    }

    private void set4ByteString(@NotNull Directory directory, int tagType, @NotNull RandomAccessReader reader) throws IOException {
        int i = reader.getInt32(tagType);
        if (i != 0) {
            directory.setString(tagType, getStringFromInt32(i));
        }
    }

    private void setInt32(@NotNull Directory directory, int tagType, @NotNull RandomAccessReader reader) throws IOException {
        int i = reader.getInt32(tagType);
        if (i != 0) {
            directory.setInt(tagType, i);
        }
    }

    private void setInt64(@NotNull Directory directory, int tagType, @NotNull RandomAccessReader reader) throws IOException {
        long l = reader.getInt64(tagType);
        if (l != 0) {
            directory.setLong(tagType, l);
        }
    }

    private void setDate(@NotNull IccDirectory directory, int tagType, @NotNull RandomAccessReader reader) throws IOException {
        int y = reader.getUInt16(tagType);
        int m = reader.getUInt16(tagType + 2);
        int d = reader.getUInt16(tagType + 4);
        int h = reader.getUInt16(tagType + 6);
        int M = reader.getUInt16(tagType + 8);
        int s = reader.getUInt16(tagType + 10);
        if (DateUtil.isValidDate(y, m - 1, d) && DateUtil.isValidTime(h, M, s)) {
            String dateString = String.format("%04d:%02d:%02d %02d:%02d:%02d", Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d), Integer.valueOf(h), Integer.valueOf(M), Integer.valueOf(s));
            directory.setString(tagType, dateString);
        } else {
            directory.addError(String.format("ICC data describes an invalid date/time: year=%d month=%d day=%d hour=%d minute=%d second=%d", Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d), Integer.valueOf(h), Integer.valueOf(M), Integer.valueOf(s)));
        }
    }

    @NotNull
    public static String getStringFromInt32(int d) {
        byte[] b = {(byte) ((d & (-16777216)) >> 24), (byte) ((d & 16711680) >> 16), (byte) ((d & 65280) >> 8), (byte) (d & 255)};
        return new String(b);
    }
}
