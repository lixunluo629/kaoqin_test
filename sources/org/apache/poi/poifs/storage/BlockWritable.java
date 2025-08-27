package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/storage/BlockWritable.class */
public interface BlockWritable {
    void writeBlocks(OutputStream outputStream) throws IOException;
}
