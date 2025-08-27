package com.itextpdf.io.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.TreeMap;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter.class */
public class TiffWriter {
    private TreeMap<Integer, FieldBase> ifd = new TreeMap<>();

    public void addField(FieldBase field) {
        this.ifd.put(Integer.valueOf(field.getTag()), field);
    }

    public int getIfdSize() {
        return 6 + (this.ifd.size() * 12);
    }

    public void writeFile(OutputStream stream) throws IOException {
        stream.write(77);
        stream.write(77);
        stream.write(0);
        stream.write(42);
        writeLong(8, stream);
        writeShort(this.ifd.size(), stream);
        int offset = 8 + getIfdSize();
        for (FieldBase field : this.ifd.values()) {
            int size = field.getValueSize();
            if (size > 4) {
                field.setOffset(offset);
                offset += size;
            }
            field.writeField(stream);
        }
        writeLong(0, stream);
        Iterator<FieldBase> it = this.ifd.values().iterator();
        while (it.hasNext()) {
            it.next().writeValue(stream);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldBase.class */
    public static abstract class FieldBase {
        private int tag;
        private int fieldType;
        private int count;
        protected byte[] data;
        private int offset;

        protected FieldBase(int tag, int fieldType, int count) {
            this.tag = tag;
            this.fieldType = fieldType;
            this.count = count;
        }

        public int getValueSize() {
            return (this.data.length + 1) & (-2);
        }

        public int getTag() {
            return this.tag;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void writeField(OutputStream stream) throws IOException {
            TiffWriter.writeShort(this.tag, stream);
            TiffWriter.writeShort(this.fieldType, stream);
            TiffWriter.writeLong(this.count, stream);
            if (this.data.length <= 4) {
                stream.write(this.data);
                for (int k = this.data.length; k < 4; k++) {
                    stream.write(0);
                }
                return;
            }
            TiffWriter.writeLong(this.offset, stream);
        }

        public void writeValue(OutputStream stream) throws IOException {
            if (this.data.length <= 4) {
                return;
            }
            stream.write(this.data);
            if ((this.data.length & 1) == 1) {
                stream.write(0);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldShort.class */
    public static class FieldShort extends FieldBase {
        public FieldShort(int tag, int value) {
            super(tag, 3, 1);
            this.data = new byte[2];
            this.data[0] = (byte) (value >> 8);
            this.data[1] = (byte) value;
        }

        public FieldShort(int tag, int[] values) {
            super(tag, 3, values.length);
            this.data = new byte[values.length * 2];
            int ptr = 0;
            for (int value : values) {
                int i = ptr;
                int ptr2 = ptr + 1;
                this.data[i] = (byte) (value >> 8);
                ptr = ptr2 + 1;
                this.data[ptr2] = (byte) value;
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldLong.class */
    public static class FieldLong extends FieldBase {
        public FieldLong(int tag, int value) {
            super(tag, 4, 1);
            this.data = new byte[4];
            this.data[0] = (byte) (value >> 24);
            this.data[1] = (byte) (value >> 16);
            this.data[2] = (byte) (value >> 8);
            this.data[3] = (byte) value;
        }

        public FieldLong(int tag, int[] values) {
            super(tag, 4, values.length);
            this.data = new byte[values.length * 4];
            int ptr = 0;
            for (int value : values) {
                int i = ptr;
                int ptr2 = ptr + 1;
                this.data[i] = (byte) (value >> 24);
                int ptr3 = ptr2 + 1;
                this.data[ptr2] = (byte) (value >> 16);
                int ptr4 = ptr3 + 1;
                this.data[ptr3] = (byte) (value >> 8);
                ptr = ptr4 + 1;
                this.data[ptr4] = (byte) value;
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldRational.class */
    public static class FieldRational extends FieldBase {
        /* JADX WARN: Type inference failed for: r2v1, types: [int[], int[][]] */
        public FieldRational(int tag, int[] value) {
            this(tag, (int[][]) new int[]{value});
        }

        public FieldRational(int tag, int[][] values) {
            super(tag, 5, values.length);
            this.data = new byte[values.length * 8];
            int ptr = 0;
            for (int[] value : values) {
                int i = ptr;
                int ptr2 = ptr + 1;
                this.data[i] = (byte) (value[0] >> 24);
                int ptr3 = ptr2 + 1;
                this.data[ptr2] = (byte) (value[0] >> 16);
                int ptr4 = ptr3 + 1;
                this.data[ptr3] = (byte) (value[0] >> 8);
                int ptr5 = ptr4 + 1;
                this.data[ptr4] = (byte) value[0];
                int ptr6 = ptr5 + 1;
                this.data[ptr5] = (byte) (value[1] >> 24);
                int ptr7 = ptr6 + 1;
                this.data[ptr6] = (byte) (value[1] >> 16);
                int ptr8 = ptr7 + 1;
                this.data[ptr7] = (byte) (value[1] >> 8);
                ptr = ptr8 + 1;
                this.data[ptr8] = (byte) value[1];
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldByte.class */
    public static class FieldByte extends FieldBase {
        public FieldByte(int tag, byte[] values) {
            super(tag, 1, values.length);
            this.data = values;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldUndefined.class */
    public static class FieldUndefined extends FieldBase {
        public FieldUndefined(int tag, byte[] values) {
            super(tag, 7, values.length);
            this.data = values;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldImage.class */
    public static class FieldImage extends FieldBase {
        public FieldImage(byte[] values) {
            super(273, 4, 1);
            this.data = values;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/TiffWriter$FieldAscii.class */
    public static class FieldAscii extends FieldBase {
        public FieldAscii(int tag, String values) {
            super(tag, 2, values.getBytes(StandardCharsets.US_ASCII).length + 1);
            byte[] b = values.getBytes(StandardCharsets.US_ASCII);
            this.data = new byte[b.length + 1];
            System.arraycopy(b, 0, this.data, 0, b.length);
        }
    }

    public static void writeShort(int v, OutputStream stream) throws IOException {
        stream.write((v >> 8) & 255);
        stream.write(v & 255);
    }

    public static void writeLong(int v, OutputStream stream) throws IOException {
        stream.write((v >> 24) & 255);
        stream.write((v >> 16) & 255);
        stream.write((v >> 8) & 255);
        stream.write(v & 255);
    }

    public static void compressLZW(OutputStream stream, int predictor, byte[] b, int height, int samplesPerPixel, int stride) throws IOException {
        LZWCompressor lzwCompressor = new LZWCompressor(stream, 8, true);
        boolean usePredictor = predictor == 2;
        if (!usePredictor) {
            lzwCompressor.compress(b, 0, b.length);
        } else {
            int off = 0;
            byte[] rowBuf = new byte[stride];
            for (int i = 0; i < height; i++) {
                System.arraycopy(b, off, rowBuf, 0, stride);
                for (int j = stride - 1; j >= samplesPerPixel; j--) {
                    int i2 = j;
                    rowBuf[i2] = (byte) (rowBuf[i2] - rowBuf[j - samplesPerPixel]);
                }
                lzwCompressor.compress(rowBuf, 0, stride);
                off += stride;
            }
        }
        lzwCompressor.flush();
    }
}
