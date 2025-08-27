package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ChunkHeader;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/NullHeader.class */
public class NullHeader extends ChunkHeader {
    public NullHeader(int headerSize, int chunkSize) {
        super(0, headerSize, chunkSize);
    }
}
