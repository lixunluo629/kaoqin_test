package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.poi.poifs.storage.BATBlock;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/BlockStore.class */
public abstract class BlockStore {
    protected abstract int getBlockStoreBlockSize();

    protected abstract ByteBuffer getBlockAt(int i) throws IOException;

    protected abstract ByteBuffer createBlockIfNeeded(int i) throws IOException;

    protected abstract BATBlock.BATBlockAndIndex getBATBlockAndIndex(int i);

    protected abstract int getNextBlock(int i);

    protected abstract void setNextBlock(int i, int i2);

    protected abstract int getFreeBlock() throws IOException;

    protected abstract ChainLoopDetector getChainLoopDetector() throws IOException;

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/BlockStore$ChainLoopDetector.class */
    protected class ChainLoopDetector {
        private boolean[] used_blocks;

        protected ChainLoopDetector(long rawSize) {
            int blkSize = BlockStore.this.getBlockStoreBlockSize();
            int numBlocks = (int) (rawSize / blkSize);
            this.used_blocks = new boolean[rawSize % ((long) blkSize) != 0 ? numBlocks + 1 : numBlocks];
        }

        protected void claim(int offset) {
            if (offset >= this.used_blocks.length) {
                return;
            }
            if (this.used_blocks[offset]) {
                throw new IllegalStateException("Potential loop detected - Block " + offset + " was already claimed but was just requested again");
            }
            this.used_blocks[offset] = true;
        }
    }
}
