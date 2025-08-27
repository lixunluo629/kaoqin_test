package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Common.class */
class Woff2Common {
    public static final int kWoff2Signature = 2001684018;
    public static final int kWoff2FlagsTransform = 256;
    public static final int kTtcFontFlavor = 1953784678;
    public static final int kSfntHeaderSize = 12;
    public static final int kSfntEntrySize = 16;

    Woff2Common() {
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Common$Point.class */
    public static class Point {
        public int x;
        public int y;
        public boolean on_curve;

        public Point(int x, int y, boolean on_curve) {
            this.x = x;
            this.y = y;
            this.on_curve = on_curve;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Common$Table.class */
    public static class Table implements Comparable<Table> {
        public int tag;
        public int flags;
        public int src_offset;
        public int src_length;
        public int transform_length;
        public int dst_offset;
        public int dst_length;

        @Override // java.lang.Comparable
        public int compareTo(Table o) {
            return JavaUnsignedUtil.compareAsUnsigned(this.tag, o.tag);
        }
    }

    public static int collectionHeaderSize(int header_version, int num_fonts) {
        int size = 0;
        if (header_version == 131072) {
            size = 0 + 12;
        }
        if (header_version == 65536 || header_version == 131072) {
            size += 12 + (4 * num_fonts);
        }
        return size;
    }

    public static int computeULongSum(byte[] buf, int offset, int size) {
        int checksum = 0;
        int aligned_size = size & (-4);
        for (int i = 0; i < aligned_size; i += 4) {
            checksum += (JavaUnsignedUtil.asU8(buf[offset + i]) << 24) | (JavaUnsignedUtil.asU8(buf[(offset + i) + 1]) << 16) | (JavaUnsignedUtil.asU8(buf[(offset + i) + 2]) << 8) | JavaUnsignedUtil.asU8(buf[offset + i + 3]);
        }
        if (size != aligned_size) {
            int v = 0;
            for (int i2 = aligned_size; i2 < size; i2++) {
                v |= JavaUnsignedUtil.asU8(buf[offset + i2]) << (24 - (8 * (i2 & 3)));
            }
            checksum += v;
        }
        return checksum;
    }
}
