package com.itextpdf.io.font.woff2;

import com.itextpdf.io.codec.brotli.dec.BrotliInputStream;
import com.itextpdf.io.font.woff2.Woff2Common;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec.class */
class Woff2Dec {
    private static final int kGlyfOnCurve = 1;
    private static final int kGlyfXShort = 2;
    private static final int kGlyfYShort = 4;
    private static final int kGlyfRepeat = 8;
    private static final int kGlyfThisXIsSame = 16;
    private static final int kGlyfThisYIsSame = 32;
    private static final int FLAG_ARG_1_AND_2_ARE_WORDS = 1;
    private static final int FLAG_WE_HAVE_A_SCALE = 8;
    private static final int FLAG_MORE_COMPONENTS = 32;
    private static final int FLAG_WE_HAVE_AN_X_AND_Y_SCALE = 64;
    private static final int FLAG_WE_HAVE_A_TWO_BY_TWO = 128;
    private static final int FLAG_WE_HAVE_INSTRUCTIONS = 256;
    private static final int kCheckSumAdjustmentOffset = 8;
    private static final int kEndPtsOfContoursOffset = 10;
    private static final int kCompositeGlyphBegin = 10;
    private static final int kDefaultGlyphBuf = 5120;
    private static final float kMaxPlausibleCompressionRatio = 100.0f;
    static final /* synthetic */ boolean $assertionsDisabled;

    Woff2Dec() {
    }

    static {
        $assertionsDisabled = !Woff2Dec.class.desiredAssertionStatus();
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$TtcFont.class */
    private static class TtcFont {
        public int flavor;
        public int dst_offset;
        public int header_checksum;
        public short[] table_indices;

        private TtcFont() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$Woff2Header.class */
    private static class Woff2Header {
        public int flavor;
        public int header_version;
        public short num_tables;
        public long compressed_offset;
        public int compressed_length;
        public int uncompressed_size;
        public Woff2Common.Table[] tables;
        public TtcFont[] ttc_fonts;

        private Woff2Header() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$Woff2FontInfo.class */
    private static class Woff2FontInfo {
        public short num_glyphs;
        public short index_format;
        public short num_hmetrics;
        public short[] x_mins;
        public Map<Integer, Integer> table_entry_by_tag;

        private Woff2FontInfo() {
            this.table_entry_by_tag = new HashMap();
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$RebuildMetadata.class */
    private static class RebuildMetadata {
        int header_checksum;
        Woff2FontInfo[] font_infos;
        Map<TableChecksumInfo, Integer> checksums;

        private RebuildMetadata() {
            this.checksums = new HashMap();
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$TableChecksumInfo.class */
    private static class TableChecksumInfo {
        public int tag;
        public int offset;

        public TableChecksumInfo(int tag, int offset) {
            this.tag = tag;
            this.offset = offset;
        }

        public int hashCode() {
            return (new Integer(this.tag).hashCode() * 13) + new Integer(this.offset).hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof TableChecksumInfo) {
                TableChecksumInfo info = (TableChecksumInfo) o;
                return this.tag == info.tag && this.offset == info.offset;
            }
            return false;
        }
    }

    private static int withSign(int flag, int baseval) {
        return (flag & 1) != 0 ? baseval : -baseval;
    }

    private static int tripletDecode(byte[] data, int flags_in_offset, int in_offset, int in_size, int n_points, Woff2Common.Point[] result) {
        int n_data_bytes;
        int dx;
        int iWithSign;
        int x = 0;
        int y = 0;
        if (n_points > in_size) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYPH_FAILED);
        }
        int triplet_index = 0;
        for (int i = 0; i < n_points; i++) {
            int flag = JavaUnsignedUtil.asU8(data[i + flags_in_offset]);
            boolean on_curve = (flag >> 7) == 0;
            int flag2 = flag & 127;
            if (flag2 < 84) {
                n_data_bytes = 1;
            } else if (flag2 < 120) {
                n_data_bytes = 2;
            } else if (flag2 < 124) {
                n_data_bytes = 3;
            } else {
                n_data_bytes = 4;
            }
            if (triplet_index + n_data_bytes > in_size || triplet_index + n_data_bytes < triplet_index) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYPH_FAILED);
            }
            if (flag2 < 10) {
                dx = 0;
                iWithSign = withSign(flag2, ((flag2 & 14) << 7) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index]));
            } else if (flag2 < 20) {
                dx = withSign(flag2, (((flag2 - 10) & 14) << 7) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index]));
                iWithSign = 0;
            } else if (flag2 < 84) {
                int b0 = flag2 - 20;
                int b1 = JavaUnsignedUtil.asU8(data[in_offset + triplet_index]);
                dx = withSign(flag2, 1 + (b0 & 48) + (b1 >> 4));
                iWithSign = withSign(flag2 >> 1, 1 + ((b0 & 12) << 2) + (b1 & 15));
            } else if (flag2 < 120) {
                int b02 = flag2 - 84;
                dx = withSign(flag2, 1 + ((b02 / 12) << 8) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index]));
                iWithSign = withSign(flag2 >> 1, 1 + (((b02 % 12) >> 2) << 8) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index + 1]));
            } else if (flag2 < 124) {
                int b2 = JavaUnsignedUtil.asU8(data[in_offset + triplet_index + 1]);
                dx = withSign(flag2, (JavaUnsignedUtil.asU8(data[in_offset + triplet_index]) << 4) + (b2 >> 4));
                iWithSign = withSign(flag2 >> 1, ((b2 & 15) << 8) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index + 2]));
            } else {
                dx = withSign(flag2, (JavaUnsignedUtil.asU8(data[in_offset + triplet_index]) << 8) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index + 1]));
                iWithSign = withSign(flag2 >> 1, (JavaUnsignedUtil.asU8(data[(in_offset + triplet_index) + 2]) << 8) + JavaUnsignedUtil.asU8(data[in_offset + triplet_index + 3]));
            }
            int dy = iWithSign;
            triplet_index += n_data_bytes;
            x += dx;
            y += dy;
            result[i] = new Woff2Common.Point(x, y, on_curve);
        }
        return triplet_index;
    }

    private static int storePoints(int n_points, Woff2Common.Point[] points, int n_contours, int instruction_length, byte[] dst, int dst_size) {
        int flag_offset = 10 + (2 * n_contours) + 2 + instruction_length;
        int last_flag = -1;
        int repeat_count = 0;
        int last_x = 0;
        int last_y = 0;
        int x_bytes = 0;
        int y_bytes = 0;
        for (int i = 0; i < n_points; i++) {
            Woff2Common.Point point = points[i];
            int flag = point.on_curve ? 1 : 0;
            int dx = point.x - last_x;
            int dy = point.y - last_y;
            if (dx == 0) {
                flag |= 16;
            } else if (dx > -256 && dx < 256) {
                flag |= 2 | (dx > 0 ? 16 : 0);
                x_bytes++;
            } else {
                x_bytes += 2;
            }
            if (dy == 0) {
                flag |= 32;
            } else if (dy > -256 && dy < 256) {
                flag |= 4 | (dy > 0 ? 32 : 0);
                y_bytes++;
            } else {
                y_bytes += 2;
            }
            if (flag == last_flag && repeat_count != 255) {
                int i2 = flag_offset - 1;
                dst[i2] = (byte) (dst[i2] | 8);
                repeat_count++;
            } else {
                if (repeat_count != 0) {
                    if (flag_offset >= dst_size) {
                        throw new FontCompressionException(FontCompressionException.RECONSTRUCT_POINT_FAILED);
                    }
                    int i3 = flag_offset;
                    flag_offset++;
                    dst[i3] = (byte) repeat_count;
                }
                if (flag_offset >= dst_size) {
                    throw new FontCompressionException(FontCompressionException.RECONSTRUCT_POINT_FAILED);
                }
                int i4 = flag_offset;
                flag_offset++;
                dst[i4] = (byte) flag;
                repeat_count = 0;
            }
            last_x = point.x;
            last_y = point.y;
            last_flag = flag;
        }
        if (repeat_count != 0) {
            if (flag_offset >= dst_size) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_POINT_FAILED);
            }
            int i5 = flag_offset;
            flag_offset++;
            dst[i5] = (byte) repeat_count;
        }
        int xy_bytes = x_bytes + y_bytes;
        if (xy_bytes < x_bytes || flag_offset + xy_bytes < flag_offset || flag_offset + xy_bytes > dst_size) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_POINT_FAILED);
        }
        int x_offset = flag_offset;
        int y_offset = flag_offset + x_bytes;
        int last_x2 = 0;
        int last_y2 = 0;
        for (int i6 = 0; i6 < n_points; i6++) {
            int dx2 = points[i6].x - last_x2;
            if (dx2 != 0) {
                if (dx2 > -256 && dx2 < 256) {
                    int i7 = x_offset;
                    x_offset++;
                    dst[i7] = (byte) Math.abs(dx2);
                } else {
                    x_offset = StoreBytes.storeU16(dst, x_offset, dx2);
                }
            }
            last_x2 += dx2;
            int dy2 = points[i6].y - last_y2;
            if (dy2 != 0) {
                if (dy2 > -256 && dy2 < 256) {
                    int i8 = y_offset;
                    y_offset++;
                    dst[i8] = (byte) Math.abs(dy2);
                } else {
                    y_offset = StoreBytes.storeU16(dst, y_offset, dy2);
                }
            }
            last_y2 += dy2;
        }
        int glyph_size = y_offset;
        return glyph_size;
    }

    private static void computeBbox(int n_points, Woff2Common.Point[] points, byte[] dst) {
        int x_min = 0;
        int y_min = 0;
        int x_max = 0;
        int y_max = 0;
        if (n_points > 0) {
            x_min = points[0].x;
            x_max = points[0].x;
            y_min = points[0].y;
            y_max = points[0].y;
        }
        for (int i = 1; i < n_points; i++) {
            int x = points[i].x;
            int y = points[i].y;
            x_min = Math.min(x, x_min);
            x_max = Math.max(x, x_max);
            y_min = Math.min(y, y_min);
            y_max = Math.max(y, y_max);
        }
        int offset = StoreBytes.storeU16(dst, 2, x_min);
        StoreBytes.storeU16(dst, StoreBytes.storeU16(dst, StoreBytes.storeU16(dst, offset, y_min), x_max), y_max);
    }

    private static CompositeGlyphInfo sizeOfComposite(Buffer composite_stream) {
        int arg_size;
        Buffer composite_stream2 = new Buffer(composite_stream);
        int start_offset = composite_stream2.getOffset();
        boolean we_have_instructions = false;
        int flags = 32;
        while ((flags & 32) != 0) {
            flags = JavaUnsignedUtil.asU16(composite_stream2.readShort());
            we_have_instructions |= (flags & 256) != 0;
            if ((flags & 1) != 0) {
                arg_size = 2 + 4;
            } else {
                arg_size = 2 + 2;
            }
            if ((flags & 8) != 0) {
                arg_size += 2;
            } else if ((flags & 64) != 0) {
                arg_size += 4;
            } else if ((flags & 128) != 0) {
                arg_size += 8;
            }
            composite_stream2.skip(arg_size);
        }
        int size = composite_stream2.getOffset() - start_offset;
        boolean have_instructions = we_have_instructions;
        return new CompositeGlyphInfo(size, have_instructions);
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$CompositeGlyphInfo.class */
    private static class CompositeGlyphInfo {
        public int size;
        public boolean have_instructions;

        public CompositeGlyphInfo(int size, boolean have_instructions) {
            this.size = size;
            this.have_instructions = have_instructions;
        }
    }

    private static void pad4(Woff2Out out) {
        byte[] zeroes = {0, 0, 0};
        if (out.size() + 3 < out.size()) {
            throw new FontCompressionException(FontCompressionException.PADDING_OVERFLOW);
        }
        int pad_bytes = Round.round4(out.size()) - out.size();
        if (pad_bytes > 0) {
            out.write(zeroes, 0, pad_bytes);
        }
    }

    private static int storeLoca(int[] loca_values, int index_format, Woff2Out out) {
        int iStoreU16;
        long loca_size = loca_values.length;
        long offset_size = index_format != 0 ? 4L : 2L;
        if (((loca_size << 2) >> 2) != loca_size) {
            throw new FontCompressionException(FontCompressionException.LOCA_SIZE_OVERFLOW);
        }
        byte[] loca_content = new byte[(int) (loca_size * offset_size)];
        int offset = 0;
        for (int value : loca_values) {
            if (index_format != 0) {
                iStoreU16 = StoreBytes.storeU32(loca_content, offset, value);
            } else {
                iStoreU16 = StoreBytes.storeU16(loca_content, offset, value >> 1);
            }
            offset = iStoreU16;
        }
        int checksum = Woff2Common.computeULongSum(loca_content, 0, loca_content.length);
        out.write(loca_content, 0, loca_content.length);
        return checksum;
    }

    private static Checksums reconstructGlyf(byte[] data, int data_offset, Woff2Common.Table glyf_table, int glyph_checksum, Woff2Common.Table loca_table, int loca_checksum, Woff2FontInfo info, Woff2Out out) {
        Buffer file = new Buffer(data, data_offset, glyf_table.transform_length);
        ArrayList<StreamInfo> substreams = new ArrayList<>(7);
        int glyf_start = out.size();
        file.readInt();
        info.num_glyphs = file.readShort();
        info.index_format = file.readShort();
        int offset = 36;
        if (36 > glyf_table.transform_length) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
        }
        for (int i = 0; i < 7; i++) {
            int substream_size = file.readInt();
            if (substream_size > glyf_table.transform_length - offset) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
            }
            substreams.add(new StreamInfo(data_offset + offset, substream_size));
            offset += substream_size;
        }
        Buffer n_contour_stream = new Buffer(data, substreams.get(0).offset, substreams.get(0).length);
        Buffer n_points_stream = new Buffer(data, substreams.get(1).offset, substreams.get(1).length);
        Buffer flag_stream = new Buffer(data, substreams.get(2).offset, substreams.get(2).length);
        Buffer glyph_stream = new Buffer(data, substreams.get(3).offset, substreams.get(3).length);
        Buffer composite_stream = new Buffer(data, substreams.get(4).offset, substreams.get(4).length);
        Buffer bbox_stream = new Buffer(data, substreams.get(5).offset, substreams.get(5).length);
        Buffer instruction_stream = new Buffer(data, substreams.get(6).offset, substreams.get(6).length);
        int[] loca_values = new int[JavaUnsignedUtil.asU16(info.num_glyphs) + 1];
        ArrayList<Integer> n_points_vec = new ArrayList<>();
        Woff2Common.Point[] points = new Woff2Common.Point[0];
        int points_size = 0;
        int bbox_bitmap_offset = bbox_stream.getInitialOffset();
        int bitmap_length = ((JavaUnsignedUtil.asU16(info.num_glyphs) + 31) >> 5) << 2;
        bbox_stream.skip(bitmap_length);
        int glyph_buf_size = 5120;
        byte[] glyph_buf = new byte[5120];
        info.x_mins = new short[JavaUnsignedUtil.asU16(info.num_glyphs)];
        for (int i2 = 0; i2 < JavaUnsignedUtil.asU16(info.num_glyphs); i2++) {
            int glyph_size = 0;
            boolean have_bbox = false;
            byte[] bitmap = new byte[bitmap_length];
            System.arraycopy(data, bbox_bitmap_offset, bitmap, 0, bitmap_length);
            if ((data[bbox_bitmap_offset + (i2 >> 3)] & (128 >> (i2 & 7))) != 0) {
                have_bbox = true;
            }
            int n_contours = JavaUnsignedUtil.asU16(n_contour_stream.readShort());
            if (n_contours == 65535) {
                int instruction_size = 0;
                if (!have_bbox) {
                    throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
                }
                CompositeGlyphInfo compositeGlyphInfo = sizeOfComposite(composite_stream);
                boolean have_instructions = compositeGlyphInfo.have_instructions;
                int composite_size = compositeGlyphInfo.size;
                if (have_instructions) {
                    instruction_size = VariableLength.read255UShort(glyph_stream);
                }
                int size_needed = 12 + composite_size + instruction_size;
                if (glyph_buf_size < size_needed) {
                    glyph_buf = new byte[size_needed];
                    glyph_buf_size = size_needed;
                }
                int glyph_size2 = StoreBytes.storeU16(glyph_buf, 0, n_contours);
                bbox_stream.read(glyph_buf, glyph_size2, 8);
                int glyph_size3 = glyph_size2 + 8;
                composite_stream.read(glyph_buf, glyph_size3, composite_size);
                glyph_size = glyph_size3 + composite_size;
                if (have_instructions) {
                    int glyph_size4 = StoreBytes.storeU16(glyph_buf, glyph_size, instruction_size);
                    instruction_stream.read(glyph_buf, glyph_size4, instruction_size);
                    glyph_size = glyph_size4 + instruction_size;
                }
            } else if (n_contours > 0) {
                n_points_vec.clear();
                int total_n_points = 0;
                for (int j = 0; j < n_contours; j++) {
                    int n_points_contour = VariableLength.read255UShort(n_points_stream);
                    n_points_vec.add(Integer.valueOf(n_points_contour));
                    if (total_n_points + n_points_contour < total_n_points) {
                        throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
                    }
                    total_n_points += n_points_contour;
                }
                int flag_size = total_n_points;
                if (flag_size > flag_stream.getLength() - flag_stream.getOffset()) {
                    throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
                }
                int flags_buf_offset = flag_stream.getInitialOffset() + flag_stream.getOffset();
                int triplet_buf_offset = glyph_stream.getInitialOffset() + glyph_stream.getOffset();
                int triplet_size = glyph_stream.getLength() - glyph_stream.getOffset();
                if (points_size < total_n_points) {
                    points_size = total_n_points;
                    points = new Woff2Common.Point[points_size];
                }
                int triplet_bytes_consumed = tripletDecode(data, flags_buf_offset, triplet_buf_offset, triplet_size, total_n_points, points);
                flag_stream.skip(flag_size);
                glyph_stream.skip(triplet_bytes_consumed);
                int instruction_size2 = VariableLength.read255UShort(glyph_stream);
                if (total_n_points >= 134217728 || instruction_size2 >= 1073741824) {
                    throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
                }
                int size_needed2 = 12 + (2 * n_contours) + (5 * total_n_points) + instruction_size2;
                if (glyph_buf_size < size_needed2) {
                    glyph_buf = new byte[size_needed2];
                    glyph_buf_size = size_needed2;
                }
                int glyph_size5 = StoreBytes.storeU16(glyph_buf, 0, n_contours);
                if (have_bbox) {
                    bbox_stream.read(glyph_buf, glyph_size5, 8);
                } else {
                    computeBbox(total_n_points, points, glyph_buf);
                }
                int glyph_size6 = 10;
                int end_point = -1;
                for (int contour_ix = 0; contour_ix < n_contours; contour_ix++) {
                    end_point += n_points_vec.get(contour_ix).intValue();
                    if (end_point >= 65536) {
                        throw new FontCompressionException(FontCompressionException.RECONSTRUCT_GLYF_TABLE_FAILED);
                    }
                    glyph_size6 = StoreBytes.storeU16(glyph_buf, glyph_size6, end_point);
                }
                int glyph_size7 = StoreBytes.storeU16(glyph_buf, glyph_size6, instruction_size2);
                instruction_stream.read(glyph_buf, glyph_size7, instruction_size2);
                int i3 = glyph_size7 + instruction_size2;
                glyph_size = storePoints(total_n_points, points, n_contours, instruction_size2, glyph_buf, glyph_buf_size);
            }
            loca_values[i2] = out.size() - glyf_start;
            out.write(glyph_buf, 0, glyph_size);
            pad4(out);
            glyph_checksum += Woff2Common.computeULongSum(glyph_buf, 0, glyph_size);
            if (n_contours > 0) {
                Buffer x_min_buf = new Buffer(glyph_buf, 2, 2);
                info.x_mins[i2] = x_min_buf.readShort();
            }
        }
        glyf_table.dst_length = out.size() - glyf_table.dst_offset;
        loca_table.dst_offset = out.size();
        loca_values[JavaUnsignedUtil.asU16(info.num_glyphs)] = glyf_table.dst_length;
        int loca_checksum2 = storeLoca(loca_values, info.index_format, out);
        loca_table.dst_length = out.size() - loca_table.dst_offset;
        return new Checksums(loca_checksum2, glyph_checksum);
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$Checksums.class */
    private static class Checksums {
        public int loca_checksum;
        public int glyph_checksum;

        public Checksums(int loca_checksum, int glyph_checksum) {
            this.loca_checksum = loca_checksum;
            this.glyph_checksum = glyph_checksum;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Dec$StreamInfo.class */
    private static class StreamInfo {
        public int offset;
        public int length;

        public StreamInfo(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
    }

    private static Woff2Common.Table findTable(ArrayList<Woff2Common.Table> tables, int tag) {
        Iterator<Woff2Common.Table> it = tables.iterator();
        while (it.hasNext()) {
            Woff2Common.Table table = it.next();
            if (table.tag == tag) {
                return table;
            }
        }
        return null;
    }

    private static short readNumHMetrics(byte[] data, int offset, int data_length) {
        Buffer buffer = new Buffer(data, offset, data_length);
        buffer.skip(34);
        short result = buffer.readShort();
        return result;
    }

    private static int reconstructTransformedHmtx(byte[] transformed_buf, int transformed_offset, int transformed_size, int num_glyphs, int num_hmetrics, short[] x_mins, Woff2Out out) {
        short s;
        short s2;
        Buffer hmtx_buff_in = new Buffer(transformed_buf, transformed_offset, transformed_size);
        int hmtx_flags = JavaUnsignedUtil.asU8(hmtx_buff_in.readByte());
        boolean has_proportional_lsbs = (hmtx_flags & 1) == 0;
        boolean has_monospace_lsbs = (hmtx_flags & 2) == 0;
        if (has_proportional_lsbs && has_monospace_lsbs) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_HMTX_TABLE_FAILED);
        }
        if (x_mins == null || x_mins.length != num_glyphs) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_HMTX_TABLE_FAILED);
        }
        if (num_hmetrics > num_glyphs) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_HMTX_TABLE_FAILED);
        }
        if (num_hmetrics < 1) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_HMTX_TABLE_FAILED);
        }
        short[] advance_widths = new short[num_hmetrics];
        for (int i = 0; i < num_hmetrics; i++) {
            short advance_width = hmtx_buff_in.readShort();
            advance_widths[i] = advance_width;
        }
        short[] lsbs = new short[num_glyphs];
        for (int i2 = 0; i2 < num_hmetrics; i2++) {
            if (has_proportional_lsbs) {
                s2 = hmtx_buff_in.readShort();
            } else {
                s2 = x_mins[i2];
            }
            short lsb = s2;
            lsbs[i2] = lsb;
        }
        for (int i3 = num_hmetrics; i3 < num_glyphs; i3++) {
            if (has_monospace_lsbs) {
                s = hmtx_buff_in.readShort();
            } else {
                s = x_mins[i3];
            }
            short lsb2 = s;
            lsbs[i3] = lsb2;
        }
        int hmtx_output_size = (2 * num_glyphs) + (2 * num_hmetrics);
        byte[] hmtx_table = new byte[hmtx_output_size];
        int dst_offset = 0;
        for (int i4 = 0; i4 < num_glyphs; i4++) {
            if (i4 < num_hmetrics) {
                dst_offset = StoreBytes.storeU16(hmtx_table, dst_offset, advance_widths[i4]);
            }
            dst_offset = StoreBytes.storeU16(hmtx_table, dst_offset, lsbs[i4]);
        }
        int checksum = Woff2Common.computeULongSum(hmtx_table, 0, hmtx_output_size);
        out.write(hmtx_table, 0, hmtx_output_size);
        return checksum;
    }

    private static void woff2Uncompress(byte[] dst_buf, int dst_offset, int dst_length, byte[] src_buf, int src_offset, int src_length) {
        int remain = dst_length;
        try {
            BrotliInputStream stream = new BrotliInputStream(new ByteArrayInputStream(src_buf, src_offset, src_length));
            while (remain > 0) {
                int read = stream.read(dst_buf, dst_offset, dst_length);
                if (read < 0) {
                    throw new FontCompressionException(FontCompressionException.BROTLI_DECODING_FAILED);
                }
                remain -= read;
            }
            if (stream.read() != -1) {
                throw new FontCompressionException(FontCompressionException.BROTLI_DECODING_FAILED);
            }
            if (remain != 0) {
                throw new FontCompressionException(FontCompressionException.BROTLI_DECODING_FAILED);
            }
        } catch (IOException e) {
            throw new FontCompressionException(FontCompressionException.BROTLI_DECODING_FAILED);
        }
    }

    private static void readTableDirectory(Buffer file, Woff2Common.Table[] tables, int num_tables) {
        int tag;
        int src_offset = 0;
        for (int i = 0; i < num_tables; i++) {
            Woff2Common.Table table = new Woff2Common.Table();
            tables[i] = table;
            int flag_byte = JavaUnsignedUtil.asU8(file.readByte());
            if ((flag_byte & 63) == 63) {
                tag = file.readInt();
            } else {
                tag = TableTags.kKnownTags[flag_byte & 63];
            }
            int flags = 0;
            int xform_version = (flag_byte >> 6) & 3;
            if (tag == 1735162214 || tag == 1819239265) {
                if (xform_version == 0) {
                    flags = 0 | 256;
                }
            } else if (xform_version != 0) {
                flags = 0 | 256;
            }
            int flags2 = flags | xform_version;
            int dst_length = VariableLength.readBase128(file);
            int transform_length = dst_length;
            if ((flags2 & 256) != 0) {
                transform_length = VariableLength.readBase128(file);
                if (tag == 1819239265 && transform_length != 0) {
                    throw new FontCompressionException(FontCompressionException.READ_TABLE_DIRECTORY_FAILED);
                }
            }
            if (src_offset + transform_length < src_offset) {
                throw new FontCompressionException(FontCompressionException.READ_TABLE_DIRECTORY_FAILED);
            }
            table.src_offset = src_offset;
            table.src_length = transform_length;
            src_offset += transform_length;
            table.tag = tag;
            table.flags = flags2;
            table.transform_length = transform_length;
            table.dst_length = dst_length;
        }
    }

    private static int storeOffsetTable(byte[] result, int offset, int flavor, int num_tables) {
        int offset2 = StoreBytes.storeU16(result, StoreBytes.storeU32(result, offset, flavor), num_tables);
        int max_pow2 = 0;
        while ((1 << (max_pow2 + 1)) <= num_tables) {
            max_pow2++;
        }
        int output_search_range = (1 << max_pow2) << 4;
        return StoreBytes.storeU16(result, StoreBytes.storeU16(result, StoreBytes.storeU16(result, offset2, output_search_range), max_pow2), (num_tables << 4) - output_search_range);
    }

    private static int storeTableEntry(byte[] result, int offset, int tag) {
        return StoreBytes.storeU32(result, StoreBytes.storeU32(result, StoreBytes.storeU32(result, StoreBytes.storeU32(result, offset, tag), 0), 0), 0);
    }

    private static long computeOffsetToFirstTable(Woff2Header hdr) {
        long offset = 12 + (16 * hdr.num_tables);
        if (hdr.header_version != 0) {
            offset = Woff2Common.collectionHeaderSize(hdr.header_version, hdr.ttc_fonts.length) + (12 * hdr.ttc_fonts.length);
            for (TtcFont ttc_font : hdr.ttc_fonts) {
                offset += 16 * ttc_font.table_indices.length;
            }
        }
        return offset;
    }

    private static ArrayList<Woff2Common.Table> tables(Woff2Header hdr, int font_index) {
        ArrayList<Woff2Common.Table> tables = new ArrayList<>();
        if (hdr.header_version != 0) {
            for (short index : hdr.ttc_fonts[font_index].table_indices) {
                tables.add(hdr.tables[JavaUnsignedUtil.asU16(index)]);
            }
        } else {
            for (Woff2Common.Table table : hdr.tables) {
                tables.add(table);
            }
        }
        return tables;
    }

    private static void reconstructFont(byte[] transformed_buf, int transformed_buf_offset, int transformed_buf_size, RebuildMetadata metadata, Woff2Header hdr, int font_index, Woff2Out out) {
        int checksum;
        int dest_offset = out.size();
        byte[] table_entry = new byte[12];
        Woff2FontInfo info = metadata.font_infos[font_index];
        ArrayList<Woff2Common.Table> tables = tables(hdr, font_index);
        if ((findTable(tables, TableTags.kGlyfTableTag) != null) != (findTable(tables, TableTags.kLocaTableTag) != null)) {
            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
        }
        int font_checksum = metadata.header_checksum;
        if (hdr.header_version != 0) {
            font_checksum = hdr.ttc_fonts[font_index].header_checksum;
        }
        int loca_checksum = 0;
        for (int i = 0; i < tables.size(); i++) {
            Woff2Common.Table table = tables.get(i);
            TableChecksumInfo checksum_key = new TableChecksumInfo(table.tag, table.src_offset);
            boolean reused = metadata.checksums.containsKey(checksum_key);
            if (font_index == 0 && reused) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
            }
            if (table.src_offset + table.src_length > transformed_buf_size) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
            }
            if (table.tag == 1751672161) {
                info.num_hmetrics = readNumHMetrics(transformed_buf, transformed_buf_offset + table.src_offset, table.src_length);
            }
            if (!reused) {
                if ((table.flags & 256) != 256) {
                    if (table.tag == 1751474532) {
                        if (table.src_length < 12) {
                            throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
                        }
                        StoreBytes.storeU32(transformed_buf, transformed_buf_offset + table.src_offset + 8, 0);
                    }
                    table.dst_offset = dest_offset;
                    checksum = Woff2Common.computeULongSum(transformed_buf, transformed_buf_offset + table.src_offset, table.src_length);
                    out.write(transformed_buf, transformed_buf_offset + table.src_offset, table.src_length);
                } else if (table.tag == 1735162214) {
                    table.dst_offset = dest_offset;
                    Woff2Common.Table loca_table = findTable(tables, TableTags.kLocaTableTag);
                    Checksums resultChecksum = reconstructGlyf(transformed_buf, transformed_buf_offset + table.src_offset, table, 0, loca_table, loca_checksum, info, out);
                    checksum = resultChecksum.glyph_checksum;
                    loca_checksum = resultChecksum.loca_checksum;
                } else if (table.tag == 1819239265) {
                    checksum = loca_checksum;
                } else if (table.tag == 1752003704) {
                    table.dst_offset = dest_offset;
                    checksum = reconstructTransformedHmtx(transformed_buf, transformed_buf_offset + table.src_offset, table.src_length, JavaUnsignedUtil.asU16(info.num_glyphs), JavaUnsignedUtil.asU16(info.num_hmetrics), info.x_mins, out);
                } else {
                    throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
                }
                metadata.checksums.put(checksum_key, Integer.valueOf(checksum));
            } else {
                checksum = metadata.checksums.get(checksum_key).intValue();
            }
            StoreBytes.storeU32(table_entry, 0, checksum);
            StoreBytes.storeU32(table_entry, 4, table.dst_offset);
            StoreBytes.storeU32(table_entry, 8, table.dst_length);
            out.write(table_entry, 0, info.table_entry_by_tag.get(Integer.valueOf(table.tag)).intValue() + 4, 12);
            font_checksum = font_checksum + checksum + Woff2Common.computeULongSum(table_entry, 0, 12);
            pad4(out);
            if (table.dst_offset + table.dst_length > out.size()) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
            }
            dest_offset = out.size();
        }
        Woff2Common.Table head_table = findTable(tables, TableTags.kHeadTableTag);
        if (head_table != null) {
            if (head_table.dst_length < 12) {
                throw new FontCompressionException(FontCompressionException.RECONSTRUCT_TABLE_DIRECTORY_FAILED);
            }
            byte[] checksum_adjustment = new byte[4];
            StoreBytes.storeU32(checksum_adjustment, 0, (-1313820742) - font_checksum);
            out.write(checksum_adjustment, 0, head_table.dst_offset + 8, 4);
        }
    }

    private static void readWoff2Header(byte[] data, int length, Woff2Header hdr) {
        Buffer file = new Buffer(data, 0, length);
        int signature = file.readInt();
        if (signature != 2001684018) {
            throw new FontCompressionException(FontCompressionException.INCORRECT_SIGNATURE);
        }
        hdr.flavor = file.readInt();
        int reported_length = file.readInt();
        if (!$assertionsDisabled && reported_length <= 0) {
            throw new AssertionError();
        }
        if (length != reported_length) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        hdr.num_tables = file.readShort();
        if (hdr.num_tables == 0) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        file.skip(6);
        hdr.compressed_length = file.readInt();
        if (!$assertionsDisabled && hdr.compressed_length < 0) {
            throw new AssertionError();
        }
        file.skip(4);
        int meta_offset = file.readInt();
        if (!$assertionsDisabled && meta_offset < 0) {
            throw new AssertionError();
        }
        int meta_length = file.readInt();
        if (!$assertionsDisabled && meta_length < 0) {
            throw new AssertionError();
        }
        int meta_length_orig = file.readInt();
        if (!$assertionsDisabled && meta_length_orig < 0) {
            throw new AssertionError();
        }
        if (meta_offset != 0 && (meta_offset >= length || length - meta_offset < meta_length)) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        int priv_offset = file.readInt();
        if (!$assertionsDisabled && priv_offset < 0) {
            throw new AssertionError();
        }
        int priv_length = file.readInt();
        if (!$assertionsDisabled && priv_length < 0) {
            throw new AssertionError();
        }
        if (priv_offset != 0 && (priv_offset >= length || length - priv_offset < priv_length)) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        hdr.tables = new Woff2Common.Table[hdr.num_tables];
        readTableDirectory(file, hdr.tables, hdr.num_tables);
        Woff2Common.Table last_table = hdr.tables[hdr.tables.length - 1];
        hdr.uncompressed_size = last_table.src_offset + last_table.src_length;
        if (!$assertionsDisabled && hdr.uncompressed_size <= 0) {
            throw new AssertionError();
        }
        if (hdr.uncompressed_size < last_table.src_offset) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        hdr.header_version = 0;
        if (hdr.flavor == 1953784678) {
            hdr.header_version = file.readInt();
            if (hdr.header_version != 65536 && hdr.header_version != 131072) {
                throw new FontCompressionException(FontCompressionException.READ_COLLECTION_HEADER_FAILED);
            }
            int num_fonts = VariableLength.read255UShort(file);
            hdr.ttc_fonts = new TtcFont[num_fonts];
            for (int i = 0; i < num_fonts; i++) {
                TtcFont ttc_font = new TtcFont();
                hdr.ttc_fonts[i] = ttc_font;
                int num_tables = VariableLength.read255UShort(file);
                ttc_font.flavor = file.readInt();
                ttc_font.table_indices = new short[num_tables];
                Woff2Common.Table glyf_table = null;
                Woff2Common.Table loca_table = null;
                for (int j = 0; j < num_tables; j++) {
                    int table_idx = VariableLength.read255UShort(file);
                    if (table_idx >= hdr.tables.length) {
                        throw new FontCompressionException(FontCompressionException.READ_COLLECTION_HEADER_FAILED);
                    }
                    ttc_font.table_indices[j] = (short) table_idx;
                    Woff2Common.Table table = hdr.tables[table_idx];
                    if (table.tag == 1819239265) {
                        loca_table = table;
                    }
                    if (table.tag == 1735162214) {
                        glyf_table = table;
                    }
                }
                if ((glyf_table == null) != (loca_table == null)) {
                    throw new FontCompressionException(FontCompressionException.READ_COLLECTION_HEADER_FAILED);
                }
            }
        }
        computeOffsetToFirstTable(hdr);
        hdr.compressed_offset = file.getOffset();
        if (hdr.compressed_offset > 2147483647L) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        long src_offset = Round.round4(hdr.compressed_offset + hdr.compressed_length);
        if (src_offset > length) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
        if (meta_offset != 0) {
            if (src_offset != meta_offset) {
                throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
            }
            src_offset = Round.round4(meta_offset + meta_length);
            if (src_offset > 2147483647L) {
                throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
            }
        }
        if (priv_offset != 0) {
            if (src_offset != priv_offset) {
                throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
            }
            src_offset = Round.round4(priv_offset + priv_length);
            if (src_offset > 2147483647L) {
                throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
            }
        }
        if (src_offset != Round.round4(length)) {
            throw new FontCompressionException(FontCompressionException.READ_HEADER_FAILED);
        }
    }

    private static void writeHeaders(byte[] data, int length, RebuildMetadata metadata, Woff2Header hdr, Woff2Out out) {
        long firstTableOffset = computeOffsetToFirstTable(hdr);
        if (!$assertionsDisabled && firstTableOffset > 2147483647L) {
            throw new AssertionError();
        }
        byte[] output = new byte[(int) firstTableOffset];
        List<Woff2Common.Table> sorted_tables = new ArrayList<>(Arrays.asList(hdr.tables));
        if (hdr.header_version != 0) {
            for (TtcFont ttc_font : hdr.ttc_fonts) {
                Map<Integer, Short> sorted_index_by_tag = new TreeMap<>();
                for (short table_index : ttc_font.table_indices) {
                    sorted_index_by_tag.put(Integer.valueOf(hdr.tables[table_index].tag), Short.valueOf(table_index));
                }
                short index = 0;
                for (Map.Entry<Integer, Short> i : sorted_index_by_tag.entrySet()) {
                    short s = index;
                    index = (short) (index + 1);
                    ttc_font.table_indices[s] = i.getValue().shortValue();
                }
            }
        } else {
            Collections.sort(sorted_tables);
        }
        if (hdr.header_version != 0) {
            int offset = StoreBytes.storeU32(output, 0, hdr.flavor);
            int offset2 = StoreBytes.storeU32(output, StoreBytes.storeU32(output, offset, hdr.header_version), hdr.ttc_fonts.length);
            int offset_table = offset2;
            for (int i2 = 0; i2 < hdr.ttc_fonts.length; i2++) {
                offset2 = StoreBytes.storeU32(output, offset2, 0);
            }
            if (hdr.header_version == 131072) {
                offset2 = StoreBytes.storeU32(output, StoreBytes.storeU32(output, StoreBytes.storeU32(output, offset2, 0), 0), 0);
            }
            metadata.font_infos = new Woff2FontInfo[hdr.ttc_fonts.length];
            for (int i3 = 0; i3 < hdr.ttc_fonts.length; i3++) {
                TtcFont ttc_font2 = hdr.ttc_fonts[i3];
                offset_table = StoreBytes.storeU32(output, offset_table, offset2);
                ttc_font2.dst_offset = offset2;
                offset2 = storeOffsetTable(output, offset2, ttc_font2.flavor, ttc_font2.table_indices.length);
                metadata.font_infos[i3] = new Woff2FontInfo();
                for (short s2 : ttc_font2.table_indices) {
                    int tag = hdr.tables[s2].tag;
                    metadata.font_infos[i3].table_entry_by_tag.put(Integer.valueOf(tag), Integer.valueOf(offset2));
                    offset2 = storeTableEntry(output, offset2, tag);
                }
                ttc_font2.header_checksum = Woff2Common.computeULongSum(output, ttc_font2.dst_offset, offset2 - ttc_font2.dst_offset);
            }
        } else {
            metadata.font_infos = new Woff2FontInfo[1];
            int offset3 = storeOffsetTable(output, 0, hdr.flavor, hdr.num_tables);
            metadata.font_infos[0] = new Woff2FontInfo();
            for (int i4 = 0; i4 < hdr.num_tables; i4++) {
                metadata.font_infos[0].table_entry_by_tag.put(Integer.valueOf(sorted_tables.get(i4).tag), Integer.valueOf(offset3));
                offset3 = storeTableEntry(output, offset3, sorted_tables.get(i4).tag);
            }
        }
        out.write(output, 0, output.length);
        metadata.header_checksum = Woff2Common.computeULongSum(output, 0, output.length);
    }

    public static int computeWoff2FinalSize(byte[] data, int length) {
        Buffer file = new Buffer(data, 0, length);
        file.skip(16);
        return file.readInt();
    }

    public static void convertWoff2ToTtf(byte[] data, int length, Woff2Out out) {
        RebuildMetadata metadata = new RebuildMetadata();
        Woff2Header hdr = new Woff2Header();
        readWoff2Header(data, length, hdr);
        writeHeaders(data, length, metadata, hdr, out);
        float compression_ratio = hdr.uncompressed_size / length;
        if (compression_ratio > kMaxPlausibleCompressionRatio) {
            throw new FontCompressionException(MessageFormatUtil.format("Implausible compression ratio {0}", Float.valueOf(compression_ratio)));
        }
        byte[] uncompressed_buf = new byte[hdr.uncompressed_size];
        woff2Uncompress(uncompressed_buf, 0, hdr.uncompressed_size, data, (int) hdr.compressed_offset, hdr.compressed_length);
        for (int i = 0; i < metadata.font_infos.length; i++) {
            reconstructFont(uncompressed_buf, 0, hdr.uncompressed_size, metadata, hdr, i, out);
        }
    }
}
