package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/LibraryHeader.class */
public class LibraryHeader extends ChunkHeader {
    private int count;

    public LibraryHeader(int headerSize, long chunkSize) {
        super(515, headerSize, chunkSize);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = Unsigned.ensureUInt(count);
    }
}
