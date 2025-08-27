package net.dongliu.apk.parser.struct;

import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/ChunkHeader.class */
public class ChunkHeader {
    private short chunkType;
    private short headerSize;
    private int chunkSize;

    public ChunkHeader(int chunkType, int headerSize, long chunkSize) {
        this.chunkType = Unsigned.toUShort(chunkType);
        this.headerSize = Unsigned.toUShort(headerSize);
        this.chunkSize = Unsigned.ensureUInt(chunkSize);
    }

    public int getBodySize() {
        return this.chunkSize - this.headerSize;
    }

    public int getChunkType() {
        return this.chunkType;
    }

    public void setChunkType(int chunkType) {
        this.chunkType = Unsigned.toUShort(chunkType);
    }

    public int getHeaderSize() {
        return this.headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = Unsigned.toUShort(headerSize);
    }

    public long getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = Unsigned.ensureUInt(chunkSize);
    }
}
