package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.HpackUtil;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.MathUtil;
import java.util.Arrays;
import java.util.Map;
import org.aspectj.apache.bcel.Constants;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackEncoder.class */
final class HpackEncoder {
    static final int HUFF_CODE_THRESHOLD = 512;
    private final HeaderEntry[] headerFields;
    private final HeaderEntry head;
    private final HpackHuffmanEncoder hpackHuffmanEncoder;
    private final byte hashMask;
    private final boolean ignoreMaxHeaderListSize;
    private final int huffCodeThreshold;
    private long size;
    private long maxHeaderTableSize;
    private long maxHeaderListSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HpackEncoder.class.desiredAssertionStatus();
    }

    HpackEncoder() {
        this(false);
    }

    HpackEncoder(boolean ignoreMaxHeaderListSize) {
        this(ignoreMaxHeaderListSize, 16, 512);
    }

    HpackEncoder(boolean ignoreMaxHeaderListSize, int arraySizeHint, int huffCodeThreshold) {
        this.head = new HeaderEntry(-1, AsciiString.EMPTY_STRING, AsciiString.EMPTY_STRING, Integer.MAX_VALUE, null);
        this.hpackHuffmanEncoder = new HpackHuffmanEncoder();
        this.ignoreMaxHeaderListSize = ignoreMaxHeaderListSize;
        this.maxHeaderTableSize = Constants.NEGATABLE;
        this.maxHeaderListSize = 4294967295L;
        this.headerFields = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte) (this.headerFields.length - 1);
        HeaderEntry headerEntry = this.head;
        HeaderEntry headerEntry2 = this.head;
        HeaderEntry headerEntry3 = this.head;
        headerEntry2.after = headerEntry3;
        headerEntry.before = headerEntry3;
        this.huffCodeThreshold = huffCodeThreshold;
    }

    public void encodeHeaders(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        if (this.ignoreMaxHeaderListSize) {
            encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
        } else {
            encodeHeadersEnforceMaxHeaderListSize(streamId, out, headers, sensitivityDetector);
        }
    }

    private void encodeHeadersEnforceMaxHeaderListSize(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        long headerSize = 0;
        for (Map.Entry<CharSequence, CharSequence> header : headers) {
            CharSequence name = header.getKey();
            CharSequence value = header.getValue();
            headerSize += HpackHeaderField.sizeOf(name, value);
            if (headerSize > this.maxHeaderListSize) {
                Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, false);
            }
        }
        encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
    }

    private void encodeHeadersIgnoreMaxHeaderListSize(ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        for (Map.Entry<CharSequence, CharSequence> header : headers) {
            CharSequence name = header.getKey();
            CharSequence value = header.getValue();
            encodeHeader(out, name, value, sensitivityDetector.isSensitive(name, value), HpackHeaderField.sizeOf(name, value));
        }
    }

    private void encodeHeader(ByteBuf out, CharSequence name, CharSequence value, boolean sensitive, long headerSize) {
        if (sensitive) {
            int nameIndex = getNameIndex(name);
            encodeLiteral(out, name, value, HpackUtil.IndexType.NEVER, nameIndex);
            return;
        }
        if (this.maxHeaderTableSize == 0) {
            int staticTableIndex = HpackStaticTable.getIndexInsensitive(name, value);
            if (staticTableIndex == -1) {
                int nameIndex2 = HpackStaticTable.getIndex(name);
                encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex2);
                return;
            } else {
                encodeInteger(out, 128, 7, staticTableIndex);
                return;
            }
        }
        if (headerSize > this.maxHeaderTableSize) {
            int nameIndex3 = getNameIndex(name);
            encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex3);
            return;
        }
        HeaderEntry headerField = getEntryInsensitive(name, value);
        if (headerField != null) {
            int index = getIndex(headerField.index) + HpackStaticTable.length;
            encodeInteger(out, 128, 7, index);
            return;
        }
        int staticTableIndex2 = HpackStaticTable.getIndexInsensitive(name, value);
        if (staticTableIndex2 != -1) {
            encodeInteger(out, 128, 7, staticTableIndex2);
            return;
        }
        ensureCapacity(headerSize);
        encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, getNameIndex(name));
        add(name, value, headerSize);
    }

    public void setMaxHeaderTableSize(ByteBuf out, long maxHeaderTableSize) throws Http2Exception {
        if (maxHeaderTableSize < 0 || maxHeaderTableSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderTableSize));
        }
        if (this.maxHeaderTableSize == maxHeaderTableSize) {
            return;
        }
        this.maxHeaderTableSize = maxHeaderTableSize;
        ensureCapacity(0L);
        encodeInteger(out, 32, 5, maxHeaderTableSize);
    }

    public long getMaxHeaderTableSize() {
        return this.maxHeaderTableSize;
    }

    public void setMaxHeaderListSize(long maxHeaderListSize) throws Http2Exception {
        if (maxHeaderListSize < 0 || maxHeaderListSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderListSize));
        }
        this.maxHeaderListSize = maxHeaderListSize;
    }

    public long getMaxHeaderListSize() {
        return this.maxHeaderListSize;
    }

    private static void encodeInteger(ByteBuf out, int mask, int n, int i) {
        encodeInteger(out, mask, n, i);
    }

    private static void encodeInteger(ByteBuf out, int mask, int n, long i) {
        if (!$assertionsDisabled && (n < 0 || n > 8)) {
            throw new AssertionError("N: " + n);
        }
        int nbits = 255 >>> (8 - n);
        if (i < nbits) {
            out.writeByte((int) (mask | i));
            return;
        }
        out.writeByte(mask | nbits);
        long j = i - nbits;
        while (true) {
            long length = j;
            if ((length & (-128)) != 0) {
                out.writeByte((int) ((length & 127) | 128));
                j = length >>> 7;
            } else {
                out.writeByte((int) length);
                return;
            }
        }
    }

    private void encodeStringLiteral(ByteBuf out, CharSequence string) {
        int huffmanLength;
        if (string.length() >= this.huffCodeThreshold && (huffmanLength = this.hpackHuffmanEncoder.getEncodedLength(string)) < string.length()) {
            encodeInteger(out, 128, 7, huffmanLength);
            this.hpackHuffmanEncoder.encode(out, string);
            return;
        }
        encodeInteger(out, 0, 7, string.length());
        if (string instanceof AsciiString) {
            AsciiString asciiString = (AsciiString) string;
            out.writeBytes(asciiString.array(), asciiString.arrayOffset(), asciiString.length());
        } else {
            out.writeCharSequence(string, CharsetUtil.ISO_8859_1);
        }
    }

    private void encodeLiteral(ByteBuf out, CharSequence name, CharSequence value, HpackUtil.IndexType indexType, int nameIndex) {
        boolean nameIndexValid = nameIndex != -1;
        switch (indexType) {
            case INCREMENTAL:
                encodeInteger(out, 64, 6, nameIndexValid ? nameIndex : 0);
                break;
            case NONE:
                encodeInteger(out, 0, 4, nameIndexValid ? nameIndex : 0);
                break;
            case NEVER:
                encodeInteger(out, 16, 4, nameIndexValid ? nameIndex : 0);
                break;
            default:
                throw new Error("should not reach here");
        }
        if (!nameIndexValid) {
            encodeStringLiteral(out, name);
        }
        encodeStringLiteral(out, value);
    }

    private int getNameIndex(CharSequence name) {
        int index = HpackStaticTable.getIndex(name);
        if (index == -1) {
            index = getIndex(name);
            if (index >= 0) {
                index += HpackStaticTable.length;
            }
        }
        return index;
    }

    private void ensureCapacity(long headerSize) {
        while (this.maxHeaderTableSize - this.size < headerSize) {
            int index = length();
            if (index != 0) {
                remove();
            } else {
                return;
            }
        }
    }

    int length() {
        if (this.size == 0) {
            return 0;
        }
        return (this.head.after.index - this.head.before.index) + 1;
    }

    long size() {
        return this.size;
    }

    HpackHeaderField getHeaderField(int index) {
        HeaderEntry headerEntry = this.head;
        while (true) {
            HeaderEntry entry = headerEntry;
            int i = index;
            index--;
            if (i >= 0) {
                headerEntry = entry.before;
            } else {
                return entry;
            }
        }
    }

    private HeaderEntry getEntryInsensitive(CharSequence name, CharSequence value) {
        if (length() == 0 || name == null || value == null) {
            return null;
        }
        int h = AsciiString.hashCode(name);
        int i = index(h);
        HeaderEntry headerEntry = this.headerFields[i];
        while (true) {
            HeaderEntry e = headerEntry;
            if (e != null) {
                if (e.hash != h || !HpackUtil.equalsVariableTime(value, e.value) || !HpackUtil.equalsVariableTime(name, e.name)) {
                    headerEntry = e.next;
                } else {
                    return e;
                }
            } else {
                return null;
            }
        }
    }

    private int getIndex(CharSequence name) {
        if (length() == 0 || name == null) {
            return -1;
        }
        int h = AsciiString.hashCode(name);
        int i = index(h);
        HeaderEntry headerEntry = this.headerFields[i];
        while (true) {
            HeaderEntry e = headerEntry;
            if (e != null) {
                if (e.hash != h || HpackUtil.equalsConstantTime(name, e.name) == 0) {
                    headerEntry = e.next;
                } else {
                    return getIndex(e.index);
                }
            } else {
                return -1;
            }
        }
    }

    private int getIndex(int index) {
        if (index == -1) {
            return -1;
        }
        return (index - this.head.before.index) + 1;
    }

    private void add(CharSequence name, CharSequence value, long headerSize) {
        if (headerSize > this.maxHeaderTableSize) {
            clear();
            return;
        }
        while (this.maxHeaderTableSize - this.size < headerSize) {
            remove();
        }
        int h = AsciiString.hashCode(name);
        int i = index(h);
        HeaderEntry old = this.headerFields[i];
        HeaderEntry e = new HeaderEntry(h, name, value, this.head.before.index - 1, old);
        this.headerFields[i] = e;
        e.addBefore(this.head);
        this.size += headerSize;
    }

    private HpackHeaderField remove() {
        if (this.size == 0) {
            return null;
        }
        HeaderEntry eldest = this.head.after;
        int h = eldest.hash;
        int i = index(h);
        HeaderEntry prev = this.headerFields[i];
        HeaderEntry headerEntry = prev;
        while (true) {
            HeaderEntry e = headerEntry;
            if (e != null) {
                HeaderEntry next = e.next;
                if (e == eldest) {
                    if (prev == eldest) {
                        this.headerFields[i] = next;
                    } else {
                        prev.next = next;
                    }
                    eldest.remove();
                    this.size -= eldest.size();
                    return eldest;
                }
                prev = e;
                headerEntry = next;
            } else {
                return null;
            }
        }
    }

    private void clear() {
        Arrays.fill(this.headerFields, (Object) null);
        HeaderEntry headerEntry = this.head;
        HeaderEntry headerEntry2 = this.head;
        HeaderEntry headerEntry3 = this.head;
        headerEntry2.after = headerEntry3;
        headerEntry.before = headerEntry3;
        this.size = 0L;
    }

    private int index(int h) {
        return h & this.hashMask;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackEncoder$HeaderEntry.class */
    private static final class HeaderEntry extends HpackHeaderField {
        HeaderEntry before;
        HeaderEntry after;
        HeaderEntry next;
        int hash;
        int index;

        HeaderEntry(int hash, CharSequence name, CharSequence value, int index, HeaderEntry next) {
            super(name, value);
            this.index = index;
            this.hash = hash;
            this.next = next;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
            this.before = null;
            this.after = null;
            this.next = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addBefore(HeaderEntry existingEntry) {
            this.after = existingEntry;
            this.before = existingEntry.before;
            this.before.after = this;
            this.after.before = this;
        }
    }
}
