package io.netty.buffer;

import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBufUtil.class */
public final class ByteBufUtil {
    private static final byte WRITE_UTF_UNKNOWN = 63;
    private static final int MAX_CHAR_BUFFER_SIZE;
    private static final int THREAD_LOCAL_BUFFER_SIZE;
    static final int WRITE_CHUNK_SIZE = 8192;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;
    static final int MAX_TL_ARRAY_LEN = 1024;
    private static final ByteProcessor FIND_NON_ASCII;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ByteBufUtil.class);
    private static final FastThreadLocal<byte[]> BYTE_ARRAYS = new FastThreadLocal<byte[]>() { // from class: io.netty.buffer.ByteBufUtil.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public byte[] initialValue() throws Exception {
            return PlatformDependent.allocateUninitializedArray(1024);
        }
    };
    private static final int MAX_BYTES_PER_CHAR_UTF8 = (int) CharsetUtil.encoder(CharsetUtil.UTF_8).maxBytesPerChar();

    static {
        ByteBufAllocator alloc;
        String allocType = SystemPropertyUtil.get("io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled").toLowerCase(Locale.US).trim();
        if ("unpooled".equals(allocType)) {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else if ("pooled".equals(allocType)) {
            alloc = PooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else {
            alloc = PooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
        }
        DEFAULT_ALLOCATOR = alloc;
        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 0);
        logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", Integer.valueOf(THREAD_LOCAL_BUFFER_SIZE));
        MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16384);
        logger.debug("-Dio.netty.maxThreadLocalCharBufferSize: {}", Integer.valueOf(MAX_CHAR_BUFFER_SIZE));
        FIND_NON_ASCII = new ByteProcessor() { // from class: io.netty.buffer.ByteBufUtil.2
            @Override // io.netty.util.ByteProcessor
            public boolean process(byte value) {
                return value >= 0;
            }
        };
    }

    static byte[] threadLocalTempArray(int minLength) {
        return minLength <= 1024 ? BYTE_ARRAYS.get() : PlatformDependent.allocateUninitializedArray(minLength);
    }

    public static String hexDump(ByteBuf buffer) {
        return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    public static String hexDump(ByteBuf buffer, int fromIndex, int length) {
        return HexUtil.hexDump(buffer, fromIndex, length);
    }

    public static String hexDump(byte[] array) {
        return hexDump(array, 0, array.length);
    }

    public static String hexDump(byte[] array, int fromIndex, int length) {
        return HexUtil.hexDump(array, fromIndex, length);
    }

    public static byte decodeHexByte(CharSequence s, int pos) {
        return StringUtil.decodeHexByte(s, pos);
    }

    public static byte[] decodeHexDump(CharSequence hexDump) {
        return StringUtil.decodeHexDump(hexDump, 0, hexDump.length());
    }

    public static byte[] decodeHexDump(CharSequence hexDump, int fromIndex, int length) {
        return StringUtil.decodeHexDump(hexDump, fromIndex, length);
    }

    public static boolean ensureWritableSuccess(int ensureWritableResult) {
        return ensureWritableResult == 0 || ensureWritableResult == 2;
    }

    public static int hashCode(ByteBuf buffer) {
        int aLen = buffer.readableBytes();
        int intCount = aLen >>> 2;
        int byteCount = aLen & 3;
        int hashCode = 1;
        int arrayIndex = buffer.readerIndex();
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = intCount; i > 0; i--) {
                hashCode = (31 * hashCode) + buffer.getInt(arrayIndex);
                arrayIndex += 4;
            }
        } else {
            for (int i2 = intCount; i2 > 0; i2--) {
                hashCode = (31 * hashCode) + swapInt(buffer.getInt(arrayIndex));
                arrayIndex += 4;
            }
        }
        for (int i3 = byteCount; i3 > 0; i3--) {
            int i4 = arrayIndex;
            arrayIndex++;
            hashCode = (31 * hashCode) + buffer.getByte(i4);
        }
        if (hashCode == 0) {
            hashCode = 1;
        }
        return hashCode;
    }

    public static int indexOf(ByteBuf needle, ByteBuf haystack) {
        int attempts = (haystack.readableBytes() - needle.readableBytes()) + 1;
        for (int i = 0; i < attempts; i++) {
            if (equals(needle, needle.readerIndex(), haystack, haystack.readerIndex() + i, needle.readableBytes())) {
                return haystack.readerIndex() + i;
            }
        }
        return -1;
    }

    public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length) {
        if (aStartIndex < 0 || bStartIndex < 0 || length < 0) {
            throw new IllegalArgumentException("All indexes and lengths must be non-negative");
        }
        if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
            return false;
        }
        int longCount = length >>> 3;
        int byteCount = length & 7;
        if (a.order() == b.order()) {
            for (int i = longCount; i > 0; i--) {
                if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        } else {
            for (int i2 = longCount; i2 > 0; i2--) {
                if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        }
        for (int i3 = byteCount; i3 > 0; i3--) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            aStartIndex++;
            bStartIndex++;
        }
        return true;
    }

    public static boolean equals(ByteBuf bufferA, ByteBuf bufferB) {
        int aLen = bufferA.readableBytes();
        if (aLen != bufferB.readableBytes()) {
            return false;
        }
        return equals(bufferA, bufferA.readerIndex(), bufferB, bufferB.readerIndex(), aLen);
    }

    public static int compare(ByteBuf bufferA, ByteBuf bufferB) {
        long res;
        int aLen = bufferA.readableBytes();
        int bLen = bufferB.readableBytes();
        int minLength = Math.min(aLen, bLen);
        int uintCount = minLength >>> 2;
        int byteCount = minLength & 3;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();
        if (uintCount > 0) {
            boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
            int uintCountIncrement = uintCount << 2;
            if (bufferA.order() == bufferB.order()) {
                res = bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            } else {
                res = bufferAIsBigEndian ? compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            }
            if (res != 0) {
                return (int) Math.min(2147483647L, Math.max(-2147483648L, res));
            }
            aIndex += uintCountIncrement;
            bIndex += uintCountIncrement;
        }
        int aEnd = aIndex + byteCount;
        while (aIndex < aEnd) {
            int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
            if (comp == 0) {
                aIndex++;
                bIndex++;
            } else {
                return comp;
            }
        }
        return aLen - bLen;
    }

    private static long compareUintBigEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintLittleEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintBigEndianA(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintBigEndianB(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    public static int indexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
        if (fromIndex <= toIndex) {
            return firstIndexOf(buffer, fromIndex, toIndex, value);
        }
        return lastIndexOf(buffer, fromIndex, toIndex, value);
    }

    public static short swapShort(short value) {
        return Short.reverseBytes(value);
    }

    public static int swapMedium(int value) {
        int swapped = ((value << 16) & 16711680) | (value & 65280) | ((value >>> 16) & 255);
        if ((swapped & 8388608) != 0) {
            swapped |= -16777216;
        }
        return swapped;
    }

    public static int swapInt(int value) {
        return Integer.reverseBytes(value);
    }

    public static long swapLong(long value) {
        return Long.reverseBytes(value);
    }

    public static ByteBuf writeShortBE(ByteBuf buf, int shortValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeShort(shortValue) : buf.writeShortLE(shortValue);
    }

    public static ByteBuf setShortBE(ByteBuf buf, int index, int shortValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.setShort(index, shortValue) : buf.setShortLE(index, shortValue);
    }

    public static ByteBuf writeMediumBE(ByteBuf buf, int mediumValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeMedium(mediumValue) : buf.writeMediumLE(mediumValue);
    }

    public static ByteBuf readBytes(ByteBufAllocator alloc, ByteBuf buffer, int length) {
        boolean release = true;
        ByteBuf dst = alloc.buffer(length);
        try {
            buffer.readBytes(dst);
            release = false;
            if (0 != 0) {
                dst.release();
            }
            return dst;
        } catch (Throwable th) {
            if (release) {
                dst.release();
            }
            throw th;
        }
    }

    private static int firstIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
        int fromIndex2 = Math.max(fromIndex, 0);
        if (fromIndex2 >= toIndex || buffer.capacity() == 0) {
            return -1;
        }
        return buffer.forEachByte(fromIndex2, toIndex - fromIndex2, new ByteProcessor.IndexOfProcessor(value));
    }

    private static int lastIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
        int capacity = buffer.capacity();
        int fromIndex2 = Math.min(fromIndex, capacity);
        if (fromIndex2 < 0 || capacity == 0) {
            return -1;
        }
        return buffer.forEachByteDesc(toIndex, fromIndex2 - toIndex, new ByteProcessor.IndexOfProcessor(value));
    }

    private static CharSequence checkCharSequenceBounds(CharSequence seq, int start, int end) {
        if (MathUtil.isOutOfBounds(start, end - start, seq.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= seq.length(" + seq.length() + ')');
        }
        return seq;
    }

    public static ByteBuf writeUtf8(ByteBufAllocator alloc, CharSequence seq) {
        ByteBuf buf = alloc.buffer(utf8MaxBytes(seq));
        writeUtf8(buf, seq);
        return buf;
    }

    public static int writeUtf8(ByteBuf buf, CharSequence seq) {
        int seqLength = seq.length();
        return reserveAndWriteUtf8Seq(buf, seq, 0, seqLength, utf8MaxBytes(seqLength));
    }

    public static int writeUtf8(ByteBuf buf, CharSequence seq, int start, int end) {
        checkCharSequenceBounds(seq, start, end);
        return reserveAndWriteUtf8Seq(buf, seq, start, end, utf8MaxBytes(end - start));
    }

    public static int reserveAndWriteUtf8(ByteBuf buf, CharSequence seq, int reserveBytes) {
        return reserveAndWriteUtf8Seq(buf, seq, 0, seq.length(), reserveBytes);
    }

    public static int reserveAndWriteUtf8(ByteBuf buf, CharSequence seq, int start, int end, int reserveBytes) {
        return reserveAndWriteUtf8Seq(buf, checkCharSequenceBounds(seq, start, end), start, end, reserveBytes);
    }

    private static int reserveAndWriteUtf8Seq(ByteBuf buf, CharSequence seq, int start, int end, int reserveBytes) {
        while (true) {
            if (buf instanceof WrappedCompositeByteBuf) {
                buf = buf.unwrap();
            } else {
                if (buf instanceof AbstractByteBuf) {
                    AbstractByteBuf byteBuf = (AbstractByteBuf) buf;
                    byteBuf.ensureWritable0(reserveBytes);
                    int written = writeUtf8(byteBuf, byteBuf.writerIndex, seq, start, end);
                    byteBuf.writerIndex += written;
                    return written;
                }
                if (buf instanceof WrappedByteBuf) {
                    buf = buf.unwrap();
                } else {
                    byte[] bytes = seq.subSequence(start, end).toString().getBytes(CharsetUtil.UTF_8);
                    buf.writeBytes(bytes);
                    return bytes.length;
                }
            }
        }
    }

    static int writeUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {
        return writeUtf8(buffer, writerIndex, seq, 0, len);
    }

    static int writeUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int start, int end) {
        int i = start;
        while (true) {
            if (i >= end) {
                break;
            }
            char c = seq.charAt(i);
            if (c < 128) {
                int i2 = writerIndex;
                writerIndex++;
                buffer._setByte(i2, (byte) c);
            } else if (c < 2048) {
                int i3 = writerIndex;
                int writerIndex2 = writerIndex + 1;
                buffer._setByte(i3, (byte) (192 | (c >> 6)));
                writerIndex = writerIndex2 + 1;
                buffer._setByte(writerIndex2, (byte) (128 | (c & '?')));
            } else if (StringUtil.isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    int i4 = writerIndex;
                    writerIndex++;
                    buffer._setByte(i4, 63);
                } else {
                    i++;
                    if (i == end) {
                        int i5 = writerIndex;
                        writerIndex++;
                        buffer._setByte(i5, 63);
                        break;
                    }
                    writerIndex = writeUtf8Surrogate(buffer, writerIndex, c, seq.charAt(i));
                }
            } else {
                int i6 = writerIndex;
                int writerIndex3 = writerIndex + 1;
                buffer._setByte(i6, (byte) (224 | (c >> '\f')));
                int writerIndex4 = writerIndex3 + 1;
                buffer._setByte(writerIndex3, (byte) (128 | ((c >> 6) & 63)));
                writerIndex = writerIndex4 + 1;
                buffer._setByte(writerIndex4, (byte) (128 | (c & '?')));
            }
            i++;
        }
        return writerIndex - writerIndex;
    }

    private static int writeUtf8Surrogate(AbstractByteBuf buffer, int writerIndex, char c, char c2) {
        if (!Character.isLowSurrogate(c2)) {
            int writerIndex2 = writerIndex + 1;
            buffer._setByte(writerIndex, 63);
            int writerIndex3 = writerIndex2 + 1;
            buffer._setByte(writerIndex2, Character.isHighSurrogate(c2) ? '?' : c2);
            return writerIndex3;
        }
        int codePoint = Character.toCodePoint(c, c2);
        int writerIndex4 = writerIndex + 1;
        buffer._setByte(writerIndex, (byte) (240 | (codePoint >> 18)));
        int writerIndex5 = writerIndex4 + 1;
        buffer._setByte(writerIndex4, (byte) (128 | ((codePoint >> 12) & 63)));
        int writerIndex6 = writerIndex5 + 1;
        buffer._setByte(writerIndex5, (byte) (128 | ((codePoint >> 6) & 63)));
        int writerIndex7 = writerIndex6 + 1;
        buffer._setByte(writerIndex6, (byte) (128 | (codePoint & 63)));
        return writerIndex7;
    }

    public static int utf8MaxBytes(int seqLength) {
        return seqLength * MAX_BYTES_PER_CHAR_UTF8;
    }

    public static int utf8MaxBytes(CharSequence seq) {
        return utf8MaxBytes(seq.length());
    }

    public static int utf8Bytes(CharSequence seq) {
        return utf8ByteCount(seq, 0, seq.length());
    }

    public static int utf8Bytes(CharSequence seq, int start, int end) {
        return utf8ByteCount(checkCharSequenceBounds(seq, start, end), start, end);
    }

    private static int utf8ByteCount(CharSequence seq, int start, int end) {
        if (seq instanceof AsciiString) {
            return end - start;
        }
        int i = start;
        while (i < end && seq.charAt(i) < 128) {
            i++;
        }
        return i < end ? (i - start) + utf8BytesNonAscii(seq, i, end) : i - start;
    }

    private static int utf8BytesNonAscii(CharSequence seq, int start, int end) {
        int encodedLength = 0;
        int i = start;
        while (true) {
            if (i >= end) {
                break;
            }
            char c = seq.charAt(i);
            if (c < 2048) {
                encodedLength += ((127 - c) >>> 31) + 1;
            } else if (StringUtil.isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    encodedLength++;
                } else {
                    i++;
                    if (i == end) {
                        encodedLength++;
                        break;
                    }
                    if (!Character.isLowSurrogate(seq.charAt(i))) {
                        encodedLength += 2;
                    } else {
                        encodedLength += 4;
                    }
                }
            } else {
                encodedLength += 3;
            }
            i++;
        }
        return encodedLength;
    }

    public static ByteBuf writeAscii(ByteBufAllocator alloc, CharSequence seq) {
        ByteBuf buf = alloc.buffer(seq.length());
        writeAscii(buf, seq);
        return buf;
    }

    public static int writeAscii(ByteBuf buf, CharSequence seq) {
        int len = seq.length();
        if (seq instanceof AsciiString) {
            AsciiString asciiString = (AsciiString) seq;
            buf.writeBytes(asciiString.array(), asciiString.arrayOffset(), len);
            return len;
        }
        while (true) {
            if (buf instanceof WrappedCompositeByteBuf) {
                buf = buf.unwrap();
            } else {
                if (buf instanceof AbstractByteBuf) {
                    AbstractByteBuf byteBuf = (AbstractByteBuf) buf;
                    byteBuf.ensureWritable0(len);
                    int written = writeAscii(byteBuf, byteBuf.writerIndex, seq, len);
                    byteBuf.writerIndex += written;
                    return written;
                }
                if (buf instanceof WrappedByteBuf) {
                    buf = buf.unwrap();
                } else {
                    byte[] bytes = seq.toString().getBytes(CharsetUtil.US_ASCII);
                    buf.writeBytes(bytes);
                    return bytes.length;
                }
            }
        }
    }

    static int writeAscii(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {
        for (int i = 0; i < len; i++) {
            int i2 = writerIndex;
            writerIndex++;
            buffer._setByte(i2, AsciiString.c2b(seq.charAt(i)));
        }
        return len;
    }

    public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset) {
        return encodeString0(alloc, false, src, charset, 0);
    }

    public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset, int extraCapacity) {
        return encodeString0(alloc, false, src, charset, extraCapacity);
    }

    static ByteBuf encodeString0(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset, int extraCapacity) {
        ByteBuf dst;
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        int length = ((int) (src.remaining() * encoder.maxBytesPerChar())) + extraCapacity;
        boolean release = true;
        if (enforceHeap) {
            dst = alloc.heapBuffer(length);
        } else {
            dst = alloc.buffer(length);
        }
        try {
            try {
                ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
                int pos = dstBuf.position();
                CoderResult cr = encoder.encode(src, dstBuf, true);
                if (!cr.isUnderflow()) {
                    cr.throwException();
                }
                CoderResult cr2 = encoder.flush(dstBuf);
                if (!cr2.isUnderflow()) {
                    cr2.throwException();
                }
                dst.writerIndex((dst.writerIndex() + dstBuf.position()) - pos);
                release = false;
                ByteBuf byteBuf = dst;
                if (0 != 0) {
                    dst.release();
                }
                return byteBuf;
            } catch (CharacterCodingException x) {
                throw new IllegalStateException(x);
            }
        } catch (Throwable th) {
            if (release) {
                dst.release();
            }
            throw th;
        }
    }

    static String decodeString(ByteBuf src, int readerIndex, int len, Charset charset) {
        byte[] array;
        int offset;
        if (len == 0) {
            return "";
        }
        if (src.hasArray()) {
            array = src.array();
            offset = src.arrayOffset() + readerIndex;
        } else {
            array = threadLocalTempArray(len);
            offset = 0;
            src.getBytes(readerIndex, array, 0, len);
        }
        if (CharsetUtil.US_ASCII.equals(charset)) {
            return new String(array, 0, offset, len);
        }
        return new String(array, offset, len, charset);
    }

    public static ByteBuf threadLocalDirectBuffer() {
        if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }
        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        }
        return ThreadLocalDirectByteBuf.newInstance();
    }

    public static byte[] getBytes(ByteBuf buf) {
        return getBytes(buf, buf.readerIndex(), buf.readableBytes());
    }

    public static byte[] getBytes(ByteBuf buf, int start, int length) {
        return getBytes(buf, start, length, true);
    }

    public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy) {
        int capacity = buf.capacity();
        if (MathUtil.isOutOfBounds(start, length, capacity)) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= buf.capacity(" + capacity + ')');
        }
        if (buf.hasArray()) {
            if (copy || start != 0 || length != capacity) {
                int baseOffset = buf.arrayOffset() + start;
                return Arrays.copyOfRange(buf.array(), baseOffset, baseOffset + length);
            }
            return buf.array();
        }
        byte[] v = PlatformDependent.allocateUninitializedArray(length);
        buf.getBytes(start, v);
        return v;
    }

    public static void copy(AsciiString src, ByteBuf dst) {
        copy(src, 0, dst, src.length());
    }

    public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int dstIdx, int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ((ByteBuf) ObjectUtil.checkNotNull(dst, "dst")).setBytes(dstIdx, src.array(), srcIdx + src.arrayOffset(), length);
    }

    public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ((ByteBuf) ObjectUtil.checkNotNull(dst, "dst")).writeBytes(src.array(), srcIdx + src.arrayOffset(), length);
    }

    public static String prettyHexDump(ByteBuf buffer) {
        return prettyHexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    public static String prettyHexDump(ByteBuf buffer, int offset, int length) {
        return HexUtil.prettyHexDump(buffer, offset, length);
    }

    public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf) {
        appendPrettyHexDump(dump, buf, buf.readerIndex(), buf.readableBytes());
    }

    public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length) {
        HexUtil.appendPrettyHexDump(dump, buf, offset, length);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBufUtil$HexUtil.class */
    private static final class HexUtil {
        private static final char[] BYTE2CHAR = new char[256];
        private static final char[] HEXDUMP_TABLE = new char[1024];
        private static final String[] HEXPADDING = new String[16];
        private static final String[] HEXDUMP_ROWPREFIXES = new String[4096];
        private static final String[] BYTE2HEX = new String[256];
        private static final String[] BYTEPADDING = new String[16];

        private HexUtil() {
        }

        static {
            char[] DIGITS = "0123456789abcdef".toCharArray();
            for (int i = 0; i < 256; i++) {
                HEXDUMP_TABLE[i << 1] = DIGITS[(i >>> 4) & 15];
                HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i & 15];
            }
            for (int i2 = 0; i2 < HEXPADDING.length; i2++) {
                int padding = HEXPADDING.length - i2;
                StringBuilder buf = new StringBuilder(padding * 3);
                for (int j = 0; j < padding; j++) {
                    buf.append("   ");
                }
                HEXPADDING[i2] = buf.toString();
            }
            for (int i3 = 0; i3 < HEXDUMP_ROWPREFIXES.length; i3++) {
                StringBuilder buf2 = new StringBuilder(12);
                buf2.append(StringUtil.NEWLINE);
                buf2.append(Long.toHexString(((i3 << 4) & 4294967295L) | 4294967296L));
                buf2.setCharAt(buf2.length() - 9, '|');
                buf2.append('|');
                HEXDUMP_ROWPREFIXES[i3] = buf2.toString();
            }
            for (int i4 = 0; i4 < BYTE2HEX.length; i4++) {
                BYTE2HEX[i4] = ' ' + StringUtil.byteToHexStringPadded(i4);
            }
            for (int i5 = 0; i5 < BYTEPADDING.length; i5++) {
                int padding2 = BYTEPADDING.length - i5;
                StringBuilder buf3 = new StringBuilder(padding2);
                for (int j2 = 0; j2 < padding2; j2++) {
                    buf3.append(' ');
                }
                BYTEPADDING[i5] = buf3.toString();
            }
            for (int i6 = 0; i6 < BYTE2CHAR.length; i6++) {
                if (i6 <= 31 || i6 >= 127) {
                    BYTE2CHAR[i6] = '.';
                } else {
                    BYTE2CHAR[i6] = (char) i6;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String hexDump(ByteBuf buffer, int fromIndex, int length) {
            ObjectUtil.checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }
            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];
            int srcIdx = fromIndex;
            int dstIdx = 0;
            while (srcIdx < endIndex) {
                System.arraycopy(HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);
                srcIdx++;
                dstIdx += 2;
            }
            return new String(buf);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String hexDump(byte[] array, int fromIndex, int length) {
            ObjectUtil.checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }
            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];
            int srcIdx = fromIndex;
            int dstIdx = 0;
            while (srcIdx < endIndex) {
                System.arraycopy(HEXDUMP_TABLE, (array[srcIdx] & 255) << 1, buf, dstIdx, 2);
                srcIdx++;
                dstIdx += 2;
            }
            return new String(buf);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String prettyHexDump(ByteBuf buffer, int offset, int length) {
            if (length == 0) {
                return "";
            }
            int rows = (length / 16) + ((length & 15) == 0 ? 0 : 1) + 4;
            StringBuilder buf = new StringBuilder(rows * 80);
            appendPrettyHexDump(buf, buffer, offset, length);
            return buf.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length) {
            if (MathUtil.isOutOfBounds(offset, length, buf.capacity())) {
                throw new IndexOutOfBoundsException("expected: 0 <= offset(" + offset + ") <= offset + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
            }
            if (length == 0) {
                return;
            }
            dump.append("         +-------------------------------------------------+" + StringUtil.NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
            int fullRows = length >>> 4;
            int remainder = length & 15;
            for (int row = 0; row < fullRows; row++) {
                int rowStartIndex = (row << 4) + offset;
                appendHexDumpRowPrefix(dump, row, rowStartIndex);
                int rowEndIndex = rowStartIndex + 16;
                for (int j = rowStartIndex; j < rowEndIndex; j++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
                }
                dump.append(" |");
                for (int j2 = rowStartIndex; j2 < rowEndIndex; j2++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j2)]);
                }
                dump.append('|');
            }
            if (remainder != 0) {
                int rowStartIndex2 = (fullRows << 4) + offset;
                appendHexDumpRowPrefix(dump, fullRows, rowStartIndex2);
                int rowEndIndex2 = rowStartIndex2 + remainder;
                for (int j3 = rowStartIndex2; j3 < rowEndIndex2; j3++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j3)]);
                }
                dump.append(HEXPADDING[remainder]);
                dump.append(" |");
                for (int j4 = rowStartIndex2; j4 < rowEndIndex2; j4++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j4)]);
                }
                dump.append(BYTEPADDING[remainder]);
                dump.append('|');
            }
            dump.append(StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
        }

        private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
            if (row < HEXDUMP_ROWPREFIXES.length) {
                dump.append(HEXDUMP_ROWPREFIXES[row]);
                return;
            }
            dump.append(StringUtil.NEWLINE);
            dump.append(Long.toHexString((rowStartIndex & 4294967295L) | 4294967296L));
            dump.setCharAt(dump.length() - 9, '|');
            dump.append('|');
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBufUtil$ThreadLocalUnsafeDirectByteBuf.class */
    static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf {
        private static final ObjectPool<ThreadLocalUnsafeDirectByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<ThreadLocalUnsafeDirectByteBuf>() { // from class: io.netty.buffer.ByteBufUtil.ThreadLocalUnsafeDirectByteBuf.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public ThreadLocalUnsafeDirectByteBuf newObject(ObjectPool.Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
                return new ThreadLocalUnsafeDirectByteBuf(handle);
            }
        });
        private final ObjectPool.Handle<ThreadLocalUnsafeDirectByteBuf> handle;

        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            ThreadLocalUnsafeDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private ThreadLocalUnsafeDirectByteBuf(ObjectPool.Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }

        @Override // io.netty.buffer.UnpooledDirectByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf
        protected void deallocate() {
            if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                this.handle.recycle(this);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBufUtil$ThreadLocalDirectByteBuf.class */
    static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf {
        private static final ObjectPool<ThreadLocalDirectByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<ThreadLocalDirectByteBuf>() { // from class: io.netty.buffer.ByteBufUtil.ThreadLocalDirectByteBuf.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public ThreadLocalDirectByteBuf newObject(ObjectPool.Handle<ThreadLocalDirectByteBuf> handle) {
                return new ThreadLocalDirectByteBuf(handle);
            }
        });
        private final ObjectPool.Handle<ThreadLocalDirectByteBuf> handle;

        static ThreadLocalDirectByteBuf newInstance() {
            ThreadLocalDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private ThreadLocalDirectByteBuf(ObjectPool.Handle<ThreadLocalDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }

        @Override // io.netty.buffer.UnpooledDirectByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf
        protected void deallocate() {
            if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                this.handle.recycle(this);
            }
        }
    }

    public static boolean isText(ByteBuf buf, Charset charset) {
        return isText(buf, buf.readerIndex(), buf.readableBytes(), charset);
    }

    public static boolean isText(ByteBuf buf, int index, int length, Charset charset) throws CharacterCodingException {
        ObjectUtil.checkNotNull(buf, "buf");
        ObjectUtil.checkNotNull(charset, "charset");
        int maxIndex = buf.readerIndex() + buf.readableBytes();
        if (index < 0 || length < 0 || index > maxIndex - length) {
            throw new IndexOutOfBoundsException("index: " + index + " length: " + length);
        }
        if (charset.equals(CharsetUtil.UTF_8)) {
            return isUtf8(buf, index, length);
        }
        if (charset.equals(CharsetUtil.US_ASCII)) {
            return isAscii(buf, index, length);
        }
        CharsetDecoder decoder = CharsetUtil.decoder(charset, CodingErrorAction.REPORT, CodingErrorAction.REPORT);
        try {
            if (buf.nioBufferCount() == 1) {
                decoder.decode(buf.nioBuffer(index, length));
                return true;
            }
            ByteBuf heapBuffer = buf.alloc().heapBuffer(length);
            try {
                heapBuffer.writeBytes(buf, index, length);
                decoder.decode(heapBuffer.internalNioBuffer(heapBuffer.readerIndex(), length));
                heapBuffer.release();
                return true;
            } catch (Throwable th) {
                heapBuffer.release();
                throw th;
            }
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    private static boolean isAscii(ByteBuf buf, int index, int length) {
        return buf.forEachByte(index, length, FIND_NON_ASCII) == -1;
    }

    private static boolean isUtf8(ByteBuf buf, int index, int length) {
        int endIndex = index + length;
        while (index < endIndex) {
            int i = index;
            index++;
            byte b1 = buf.getByte(i);
            if ((b1 & 128) != 0) {
                if ((b1 & 224) == 192) {
                    if (index >= endIndex) {
                        return false;
                    }
                    index++;
                    if ((buf.getByte(index) & 192) != 128 || (b1 & 255) < 194) {
                        return false;
                    }
                } else if ((b1 & 240) == 224) {
                    if (index > endIndex - 2) {
                        return false;
                    }
                    int index2 = index + 1;
                    byte b2 = buf.getByte(index);
                    index = index2 + 1;
                    byte b3 = buf.getByte(index2);
                    if ((b2 & 192) != 128 || (b3 & 192) != 128) {
                        return false;
                    }
                    if ((b1 & 15) == 0 && (b2 & 255) < 160) {
                        return false;
                    }
                    if ((b1 & 15) == 13 && (b2 & 255) > 159) {
                        return false;
                    }
                } else {
                    if ((b1 & 248) != 240 || index > endIndex - 3) {
                        return false;
                    }
                    int index3 = index + 1;
                    byte b22 = buf.getByte(index);
                    int index4 = index3 + 1;
                    byte b32 = buf.getByte(index3);
                    index = index4 + 1;
                    byte b4 = buf.getByte(index4);
                    if ((b22 & 192) != 128 || (b32 & 192) != 128 || (b4 & 192) != 128 || (b1 & 255) > 244) {
                        return false;
                    }
                    if ((b1 & 255) == 240 && (b22 & 255) < 144) {
                        return false;
                    }
                    if ((b1 & 255) == 244 && (b22 & 255) > 143) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    static void readBytes(ByteBufAllocator allocator, ByteBuffer buffer, int position, int length, OutputStream out) throws IOException {
        if (buffer.hasArray()) {
            out.write(buffer.array(), position + buffer.arrayOffset(), length);
            return;
        }
        int chunkLen = Math.min(length, 8192);
        buffer.clear().position(position);
        if (length <= 1024 || !allocator.isDirectBufferPooled()) {
            getBytes(buffer, threadLocalTempArray(chunkLen), 0, chunkLen, out, length);
            return;
        }
        ByteBuf tmpBuf = allocator.heapBuffer(chunkLen);
        try {
            byte[] tmp = tmpBuf.array();
            int offset = tmpBuf.arrayOffset();
            getBytes(buffer, tmp, offset, chunkLen, out, length);
            tmpBuf.release();
        } catch (Throwable th) {
            tmpBuf.release();
            throw th;
        }
    }

    private static void getBytes(ByteBuffer inBuffer, byte[] in, int inOffset, int inLen, OutputStream out, int outLen) throws IOException {
        do {
            int len = Math.min(inLen, outLen);
            inBuffer.get(in, inOffset, len);
            out.write(in, inOffset, len);
            outLen -= len;
        } while (outLen > 0);
    }

    private ByteBufUtil() {
    }
}
