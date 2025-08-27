package com.itextpdf.io.codec.brotli.dec;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Huffman.class */
final class Huffman {
    static final int HUFFMAN_MAX_TABLE_SIZE = 1080;
    private static final int MAX_LENGTH = 15;

    Huffman() {
    }

    private static int getNextKey(int key, int len) {
        int i = 1 << (len - 1);
        while (true) {
            int step = i;
            if ((key & step) != 0) {
                i = step >> 1;
            } else {
                return (key & (step - 1)) + step;
            }
        }
    }

    private static void replicateValue(int[] table, int offset, int step, int end, int item) {
        do {
            end -= step;
            table[offset + end] = item;
        } while (end > 0);
    }

    private static int nextTableBitSize(int[] count, int len, int rootBits) {
        int i = 1;
        int i2 = len - rootBits;
        while (true) {
            int left = i << i2;
            if (len >= 15) {
                break;
            }
            int left2 = left - count[len];
            if (left2 <= 0) {
                break;
            }
            len++;
            i = left2;
            i2 = 1;
        }
        return len - rootBits;
    }

    static void buildHuffmanTable(int[] rootTable, int tableOffset, int rootBits, int[] codeLengths, int codeLengthsSize) {
        int[] sorted = new int[codeLengthsSize];
        int[] count = new int[16];
        int[] offset = new int[16];
        for (int symbol = 0; symbol < codeLengthsSize; symbol++) {
            int i = codeLengths[symbol];
            count[i] = count[i] + 1;
        }
        offset[1] = 0;
        for (int len = 1; len < 15; len++) {
            offset[len + 1] = offset[len] + count[len];
        }
        for (int symbol2 = 0; symbol2 < codeLengthsSize; symbol2++) {
            if (codeLengths[symbol2] != 0) {
                int i2 = codeLengths[symbol2];
                int i3 = offset[i2];
                offset[i2] = i3 + 1;
                sorted[i3] = symbol2;
            }
        }
        int tableSize = 1 << rootBits;
        int totalSize = tableSize;
        if (offset[15] == 1) {
            for (int key = 0; key < totalSize; key++) {
                rootTable[tableOffset + key] = sorted[0];
            }
            return;
        }
        int key2 = 0;
        int symbol3 = 0;
        int len2 = 1;
        int i4 = 2;
        while (true) {
            int step = i4;
            if (len2 > rootBits) {
                break;
            }
            while (count[len2] > 0) {
                int i5 = symbol3;
                symbol3++;
                replicateValue(rootTable, tableOffset + key2, step, tableSize, (len2 << 16) | sorted[i5]);
                key2 = getNextKey(key2, len2);
                int i6 = len2;
                count[i6] = count[i6] - 1;
            }
            len2++;
            i4 = step << 1;
        }
        int mask = totalSize - 1;
        int low = -1;
        int currentOffset = tableOffset;
        int len3 = rootBits + 1;
        int i7 = 2;
        while (true) {
            int step2 = i7;
            if (len3 <= 15) {
                while (count[len3] > 0) {
                    if ((key2 & mask) != low) {
                        currentOffset += tableSize;
                        int tableBits = nextTableBitSize(count, len3, rootBits);
                        tableSize = 1 << tableBits;
                        totalSize += tableSize;
                        low = key2 & mask;
                        rootTable[tableOffset + low] = ((tableBits + rootBits) << 16) | ((currentOffset - tableOffset) - low);
                    }
                    int i8 = symbol3;
                    symbol3++;
                    replicateValue(rootTable, currentOffset + (key2 >> rootBits), step2, tableSize, ((len3 - rootBits) << 16) | sorted[i8]);
                    key2 = getNextKey(key2, len3);
                    int i9 = len3;
                    count[i9] = count[i9] - 1;
                }
                len3++;
                i7 = step2 << 1;
            } else {
                return;
            }
        }
    }
}
