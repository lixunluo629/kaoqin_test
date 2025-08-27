package org.apache.poi.poifs.storage;

import java.io.IOException;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/storage/BlockList.class */
public interface BlockList {
    void zap(int i);

    ListManagedBlock remove(int i) throws IOException;

    ListManagedBlock[] fetchBlocks(int i, int i2) throws IOException;

    void setBAT(BlockAllocationTableReader blockAllocationTableReader) throws IOException;

    int blockCount();
}
