package net.dongliu.apk.parser.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import net.dongliu.apk.parser.exception.ParserException;
import net.dongliu.apk.parser.parser.StringPoolEntry;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.struct.StringPoolHeader;
import org.apache.poi.ss.usermodel.Font;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/ParseUtils.class */
public class ParseUtils {
    public static Charset charsetUTF8 = Charset.forName("UTF-8");

    public static String readString(ByteBuffer buffer, boolean utf8) {
        if (utf8) {
            readLen(buffer);
            int bytesLen = readLen(buffer);
            byte[] bytes = Buffers.readBytes(buffer, bytesLen);
            String str = new String(bytes, charsetUTF8);
            Buffers.readUByte(buffer);
            return str;
        }
        int strLen = readLen16(buffer);
        String str2 = Buffers.readString(buffer, strLen);
        Buffers.readUShort(buffer);
        return str2;
    }

    public static String readStringUTF16(ByteBuffer buffer, int strLen) {
        String str = Buffers.readString(buffer, strLen);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 0) {
                return str.substring(0, i);
            }
        }
        return str;
    }

    private static int readLen(ByteBuffer buffer) {
        int len;
        int i = Buffers.readUByte(buffer);
        if ((i & 128) != 0) {
            int len2 = 0 | ((i & 127) << 7);
            len = len2 + Buffers.readUByte(buffer);
        } else {
            len = i;
        }
        return len;
    }

    private static int readLen16(ByteBuffer buffer) {
        int len;
        int i = Buffers.readUShort(buffer);
        if ((i & 32768) != 0) {
            int len2 = 0 | ((i & Font.COLOR_NORMAL) << 15);
            len = len2 + Buffers.readUShort(buffer);
        } else {
            len = i;
        }
        return len;
    }

    public static StringPool readStringPool(ByteBuffer buffer, StringPoolHeader stringPoolHeader) {
        long beginPos = buffer.position();
        int[] offsets = new int[stringPoolHeader.getStringCount()];
        if (stringPoolHeader.getStringCount() > 0) {
            for (int idx = 0; idx < stringPoolHeader.getStringCount(); idx++) {
                offsets[idx] = Unsigned.toUInt(Buffers.readUInt(buffer));
            }
        }
        boolean z = (stringPoolHeader.getFlags() & 1) != 0;
        boolean utf8 = (stringPoolHeader.getFlags() & 256) != 0;
        long stringPos = (beginPos + stringPoolHeader.getStringsStart()) - stringPoolHeader.getHeaderSize();
        Buffers.position(buffer, stringPos);
        StringPoolEntry[] entries = new StringPoolEntry[offsets.length];
        for (int i = 0; i < offsets.length; i++) {
            entries[i] = new StringPoolEntry(i, stringPos + Unsigned.toLong(offsets[i]));
        }
        String lastStr = null;
        long lastOffset = -1;
        StringPool stringPool = new StringPool(stringPoolHeader.getStringCount());
        for (StringPoolEntry entry : entries) {
            if (entry.getOffset() == lastOffset) {
                stringPool.set(entry.getIdx(), lastStr);
            } else {
                Buffers.position(buffer, entry.getOffset());
                lastOffset = entry.getOffset();
                String str = readString(buffer, utf8);
                lastStr = str;
                stringPool.set(entry.getIdx(), str);
            }
        }
        if (stringPoolHeader.getStyleCount() > 0) {
        }
        Buffers.position(buffer, beginPos + stringPoolHeader.getBodySize());
        return stringPool;
    }

    @Nullable
    public static ResourceValue readResValue(ByteBuffer buffer, StringPool stringPool) {
        Buffers.readUShort(buffer);
        Buffers.readUByte(buffer);
        short dataType = Buffers.readUByte(buffer);
        switch (dataType) {
            case 0:
                return ResourceValue.nullValue();
            case 1:
                return ResourceValue.reference(buffer.getInt());
            case 2:
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
                return ResourceValue.raw(buffer.getInt(), dataType);
            case 3:
                int strRef = buffer.getInt();
                if (strRef >= 0) {
                    return ResourceValue.string(strRef, stringPool);
                }
                return null;
            case 5:
                return ResourceValue.dimension(buffer.getInt());
            case 6:
                return ResourceValue.fraction(buffer.getInt());
            case 16:
                return ResourceValue.decimal(buffer.getInt());
            case 17:
                return ResourceValue.hexadecimal(buffer.getInt());
            case 18:
                return ResourceValue.bool(buffer.getInt());
            case 28:
            case 30:
                return ResourceValue.rgb(buffer.getInt(), 8);
            case 29:
            case 31:
                return ResourceValue.rgb(buffer.getInt(), 6);
        }
    }

    public static void checkChunkType(int expected, int real) {
        if (expected != real) {
            throw new ParserException("Expect chunk type:" + Integer.toHexString(expected) + ", but got:" + Integer.toHexString(real));
        }
    }
}
