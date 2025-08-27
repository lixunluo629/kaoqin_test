package net.dongliu.apk.parser.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.dongliu.apk.parser.bean.DexClass;
import net.dongliu.apk.parser.exception.ParserException;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.struct.dex.DexClassStruct;
import net.dongliu.apk.parser.struct.dex.DexHeader;
import net.dongliu.apk.parser.utils.Buffers;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/DexParser.class */
public class DexParser {
    private ByteBuffer buffer;
    private static final int NO_INDEX = -1;

    public DexParser(ByteBuffer buffer) {
        this.buffer = buffer.duplicate();
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public DexClass[] parse() throws NumberFormatException {
        String magic = new String(Buffers.readBytes(this.buffer, 8));
        if (!magic.startsWith("dex\n")) {
            return new DexClass[0];
        }
        int version = Integer.parseInt(magic.substring(4, 7));
        if (version < 35) {
            throw new ParserException("Dex file version: " + version + " is not supported");
        }
        DexHeader header = readDexHeader();
        header.setVersion(version);
        long[] stringOffsets = readStringPool(header.getStringIdsOff(), header.getStringIdsSize());
        int[] typeIds = readTypes(header.getTypeIdsOff(), header.getTypeIdsSize());
        DexClassStruct[] dexClassStructs = readClass(header.getClassDefsOff(), header.getClassDefsSize());
        StringPool stringpool = readStrings(stringOffsets);
        String[] types = new String[typeIds.length];
        for (int i = 0; i < typeIds.length; i++) {
            types[i] = stringpool.get(typeIds[i]);
        }
        DexClass[] dexClasses = new DexClass[dexClassStructs.length];
        for (int i2 = 0; i2 < dexClasses.length; i2++) {
            dexClasses[i2] = new DexClass();
        }
        for (int i3 = 0; i3 < dexClassStructs.length; i3++) {
            DexClassStruct dexClassStruct = dexClassStructs[i3];
            DexClass dexClass = dexClasses[i3];
            dexClass.setClassType(types[dexClassStruct.getClassIdx()]);
            if (dexClassStruct.getSuperclassIdx() != -1) {
                dexClass.setSuperClass(types[dexClassStruct.getSuperclassIdx()]);
            }
            dexClass.setAccessFlags(dexClassStruct.getAccessFlags());
        }
        return dexClasses;
    }

    private DexClassStruct[] readClass(long classDefsOff, int classDefsSize) {
        Buffers.position(this.buffer, classDefsOff);
        DexClassStruct[] dexClassStructs = new DexClassStruct[classDefsSize];
        for (int i = 0; i < classDefsSize; i++) {
            DexClassStruct dexClassStruct = new DexClassStruct();
            dexClassStruct.setClassIdx(this.buffer.getInt());
            dexClassStruct.setAccessFlags(this.buffer.getInt());
            dexClassStruct.setSuperclassIdx(this.buffer.getInt());
            dexClassStruct.setInterfacesOff(Buffers.readUInt(this.buffer));
            dexClassStruct.setSourceFileIdx(this.buffer.getInt());
            dexClassStruct.setAnnotationsOff(Buffers.readUInt(this.buffer));
            dexClassStruct.setClassDataOff(Buffers.readUInt(this.buffer));
            dexClassStruct.setStaticValuesOff(Buffers.readUInt(this.buffer));
            dexClassStructs[i] = dexClassStruct;
        }
        return dexClassStructs;
    }

    private int[] readTypes(long typeIdsOff, int typeIdsSize) {
        Buffers.position(this.buffer, typeIdsOff);
        int[] typeIds = new int[typeIdsSize];
        for (int i = 0; i < typeIdsSize; i++) {
            typeIds[i] = (int) Buffers.readUInt(this.buffer);
        }
        return typeIds;
    }

    private StringPool readStrings(long[] offsets) {
        StringPoolEntry[] entries = new StringPoolEntry[offsets.length];
        for (int i = 0; i < offsets.length; i++) {
            entries[i] = new StringPoolEntry(i, offsets[i]);
        }
        String lastStr = null;
        long lastOffset = -1;
        StringPool stringpool = new StringPool(offsets.length);
        for (StringPoolEntry entry : entries) {
            if (entry.getOffset() == lastOffset) {
                stringpool.set(entry.getIdx(), lastStr);
            } else {
                Buffers.position(this.buffer, entry.getOffset());
                lastOffset = entry.getOffset();
                String str = readString();
                lastStr = str;
                stringpool.set(entry.getIdx(), str);
            }
        }
        return stringpool;
    }

    private long[] readStringPool(long stringIdsOff, int stringIdsSize) {
        Buffers.position(this.buffer, stringIdsOff);
        long[] offsets = new long[stringIdsSize];
        for (int i = 0; i < stringIdsSize; i++) {
            offsets[i] = Buffers.readUInt(this.buffer);
        }
        return offsets;
    }

    private String readString() {
        int strLen = readVarInts();
        return readString(strLen);
    }

    private String readString(int strLen) {
        char[] chars = new char[strLen];
        for (int i = 0; i < strLen; i++) {
            short a = Buffers.readUByte(this.buffer);
            if ((a & 128) == 0) {
                chars[i] = (char) a;
            } else if ((a & 224) == 192) {
                short b = Buffers.readUByte(this.buffer);
                chars[i] = (char) (((a & 31) << 6) | (b & 63));
            } else if ((a & 240) == 224) {
                short b2 = Buffers.readUByte(this.buffer);
                short c = Buffers.readUByte(this.buffer);
                chars[i] = (char) (((a & 15) << 12) | ((b2 & 63) << 6) | (c & 63));
            } else if ((a & 240) == 240) {
            }
            if (chars[i] == 0) {
            }
        }
        return new String(chars);
    }

    private int readVarInts() {
        int i = 0;
        int count = 0;
        while (count <= 4) {
            short s = Buffers.readUByte(this.buffer);
            i |= (s & 127) << (count * 7);
            count++;
            if ((s & 128) == 0) {
                return i;
            }
        }
        throw new ParserException("read varints error.");
    }

    private DexHeader readDexHeader() {
        this.buffer.getInt();
        Buffers.readBytes(this.buffer, 20);
        DexHeader header = new DexHeader();
        header.setFileSize(Buffers.readUInt(this.buffer));
        header.setHeaderSize(Buffers.readUInt(this.buffer));
        Buffers.readUInt(this.buffer);
        header.setLinkSize(Buffers.readUInt(this.buffer));
        header.setLinkOff(Buffers.readUInt(this.buffer));
        header.setMapOff(Buffers.readUInt(this.buffer));
        header.setStringIdsSize(this.buffer.getInt());
        header.setStringIdsOff(Buffers.readUInt(this.buffer));
        header.setTypeIdsSize(this.buffer.getInt());
        header.setTypeIdsOff(Buffers.readUInt(this.buffer));
        header.setProtoIdsSize(this.buffer.getInt());
        header.setProtoIdsOff(Buffers.readUInt(this.buffer));
        header.setFieldIdsSize(this.buffer.getInt());
        header.setFieldIdsOff(Buffers.readUInt(this.buffer));
        header.setMethodIdsSize(this.buffer.getInt());
        header.setMethodIdsOff(Buffers.readUInt(this.buffer));
        header.setClassDefsSize(this.buffer.getInt());
        header.setClassDefsOff(Buffers.readUInt(this.buffer));
        header.setDataSize(this.buffer.getInt());
        header.setDataOff(Buffers.readUInt(this.buffer));
        Buffers.position(this.buffer, header.getHeaderSize());
        return header;
    }
}
