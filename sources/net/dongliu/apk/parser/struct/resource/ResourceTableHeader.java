package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceTableHeader.class */
public class ResourceTableHeader extends ChunkHeader {
    private int packageCount;

    public ResourceTableHeader(int headerSize, int chunkSize) {
        super(2, headerSize, chunkSize);
    }

    public long getPackageCount() {
        return Unsigned.toLong(this.packageCount);
    }

    public void setPackageCount(long packageCount) {
        this.packageCount = Unsigned.toUInt(packageCount);
    }
}
