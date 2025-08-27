package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.filesystem.BATManaged;
import org.apache.poi.util.IntList;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/storage/BlockAllocationTableWriter.class */
public final class BlockAllocationTableWriter implements BlockWritable, BATManaged {
    private POIFSBigBlockSize _bigBlockSize;
    private int _start_block = -2;
    private IntList _entries = new IntList();
    private BATBlock[] _blocks = new BATBlock[0];

    public BlockAllocationTableWriter(POIFSBigBlockSize bigBlockSize) {
        this._bigBlockSize = bigBlockSize;
    }

    public int createBlocks() {
        int xbat_blocks = 0;
        int bat_blocks = 0;
        while (true) {
            int calculated_bat_blocks = BATBlock.calculateStorageRequirements(this._bigBlockSize, bat_blocks + xbat_blocks + this._entries.size());
            int calculated_xbat_blocks = HeaderBlockWriter.calculateXBATStorageRequirements(this._bigBlockSize, calculated_bat_blocks);
            if (bat_blocks != calculated_bat_blocks || xbat_blocks != calculated_xbat_blocks) {
                bat_blocks = calculated_bat_blocks;
                xbat_blocks = calculated_xbat_blocks;
            } else {
                int startBlock = allocateSpace(bat_blocks);
                allocateSpace(xbat_blocks);
                simpleCreateBlocks();
                return startBlock;
            }
        }
    }

    public int allocateSpace(int blockCount) {
        int startBlock = this._entries.size();
        if (blockCount > 0) {
            int limit = blockCount - 1;
            int index = startBlock + 1;
            for (int k = 0; k < limit; k++) {
                int i = index;
                index++;
                this._entries.add(i);
            }
            this._entries.add(-2);
        }
        return startBlock;
    }

    public int getStartBlock() {
        return this._start_block;
    }

    void simpleCreateBlocks() {
        this._blocks = BATBlock.createBATBlocks(this._bigBlockSize, this._entries.toArray());
    }

    @Override // org.apache.poi.poifs.storage.BlockWritable
    public void writeBlocks(OutputStream stream) throws IOException {
        for (int j = 0; j < this._blocks.length; j++) {
            this._blocks[j].writeBlocks(stream);
        }
    }

    public static void writeBlock(BATBlock bat, ByteBuffer block) throws IOException {
        bat.writeData(block);
    }

    @Override // org.apache.poi.poifs.filesystem.BATManaged
    public int countBlocks() {
        return this._blocks.length;
    }

    @Override // org.apache.poi.poifs.filesystem.BATManaged
    public void setStartBlock(int start_block) {
        this._start_block = start_block;
    }
}
