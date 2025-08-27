package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/TypeSpecHeader.class */
public class TypeSpecHeader extends ChunkHeader {
    private byte id;
    private byte res0;
    private short res1;
    private int entryCount;

    public TypeSpecHeader(int headerSize, long chunkSize) {
        super(514, headerSize, chunkSize);
    }

    public short getId() {
        return Unsigned.toShort(this.id);
    }

    public void setId(short id) {
        this.id = Unsigned.toUByte(id);
    }

    public short getRes0() {
        return Unsigned.toShort(this.res0);
    }

    public void setRes0(short res0) {
        this.res0 = Unsigned.toUByte(res0);
    }

    public int getRes1() {
        return Unsigned.toInt(this.res1);
    }

    public void setRes1(int res1) {
        this.res1 = Unsigned.toUShort(res1);
    }

    public int getEntryCount() {
        return this.entryCount;
    }

    public void setEntryCount(long entryCount) {
        this.entryCount = Unsigned.ensureUInt(entryCount);
    }
}
