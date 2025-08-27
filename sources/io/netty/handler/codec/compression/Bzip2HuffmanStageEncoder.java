package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2HuffmanStageEncoder.class */
final class Bzip2HuffmanStageEncoder {
    private static final int HUFFMAN_HIGH_SYMBOL_COST = 15;
    private final Bzip2BitWriter writer;
    private final char[] mtfBlock;
    private final int mtfLength;
    private final int mtfAlphabetSize;
    private final int[] mtfSymbolFrequencies;
    private final int[][] huffmanCodeLengths;
    private final int[][] huffmanMergedCodeSymbols;
    private final byte[] selectors;

    Bzip2HuffmanStageEncoder(Bzip2BitWriter writer, char[] mtfBlock, int mtfLength, int mtfAlphabetSize, int[] mtfSymbolFrequencies) {
        this.writer = writer;
        this.mtfBlock = mtfBlock;
        this.mtfLength = mtfLength;
        this.mtfAlphabetSize = mtfAlphabetSize;
        this.mtfSymbolFrequencies = mtfSymbolFrequencies;
        int totalTables = selectTableCount(mtfLength);
        this.huffmanCodeLengths = new int[totalTables][mtfAlphabetSize];
        this.huffmanMergedCodeSymbols = new int[totalTables][mtfAlphabetSize];
        this.selectors = new byte[((mtfLength + 50) - 1) / 50];
    }

    private static int selectTableCount(int mtfLength) {
        if (mtfLength >= 2400) {
            return 6;
        }
        if (mtfLength >= 1200) {
            return 5;
        }
        if (mtfLength >= 600) {
            return 4;
        }
        if (mtfLength >= 200) {
            return 3;
        }
        return 2;
    }

    private static void generateHuffmanCodeLengths(int alphabetSize, int[] symbolFrequencies, int[] codeLengths) {
        int[] mergedFrequenciesAndIndices = new int[alphabetSize];
        int[] sortedFrequencies = new int[alphabetSize];
        for (int i = 0; i < alphabetSize; i++) {
            mergedFrequenciesAndIndices[i] = (symbolFrequencies[i] << 9) | i;
        }
        Arrays.sort(mergedFrequenciesAndIndices);
        for (int i2 = 0; i2 < alphabetSize; i2++) {
            sortedFrequencies[i2] = mergedFrequenciesAndIndices[i2] >>> 9;
        }
        Bzip2HuffmanAllocator.allocateHuffmanCodeLengths(sortedFrequencies, 20);
        for (int i3 = 0; i3 < alphabetSize; i3++) {
            codeLengths[mergedFrequenciesAndIndices[i3] & 511] = sortedFrequencies[i3];
        }
    }

    private void generateHuffmanOptimisationSeeds() {
        int actualCumulativeFrequency;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        int remainingLength = this.mtfLength;
        int lowCostEnd = -1;
        for (int i = 0; i < totalTables; i++) {
            int targetCumulativeFrequency = remainingLength / (totalTables - i);
            int lowCostStart = lowCostEnd + 1;
            int i2 = 0;
            while (true) {
                actualCumulativeFrequency = i2;
                if (actualCumulativeFrequency >= targetCumulativeFrequency || lowCostEnd >= mtfAlphabetSize - 1) {
                    break;
                }
                lowCostEnd++;
                i2 = actualCumulativeFrequency + mtfSymbolFrequencies[lowCostEnd];
            }
            if (lowCostEnd > lowCostStart && i != 0 && i != totalTables - 1 && ((totalTables - i) & 1) == 0) {
                int i3 = lowCostEnd;
                lowCostEnd--;
                actualCumulativeFrequency -= mtfSymbolFrequencies[i3];
            }
            int[] tableCodeLengths = huffmanCodeLengths[i];
            for (int j = 0; j < mtfAlphabetSize; j++) {
                if (j < lowCostStart || j > lowCostEnd) {
                    tableCodeLengths[j] = 15;
                }
            }
            remainingLength -= actualCumulativeFrequency;
        }
    }

    private void optimiseSelectorsAndHuffmanTables(boolean storeSelectors) {
        char[] mtfBlock = this.mtfBlock;
        byte[] selectors = this.selectors;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int mtfLength = this.mtfLength;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        int[][] tableFrequencies = new int[totalTables][mtfAlphabetSize];
        int selectorIndex = 0;
        int i = 0;
        while (true) {
            int groupStart = i;
            if (groupStart >= mtfLength) {
                break;
            }
            int groupEnd = Math.min(groupStart + 50, mtfLength) - 1;
            short[] cost = new short[totalTables];
            for (int i2 = groupStart; i2 <= groupEnd; i2++) {
                char c = mtfBlock[i2];
                for (int j = 0; j < totalTables; j++) {
                    int i3 = j;
                    cost[i3] = (short) (cost[i3] + huffmanCodeLengths[j][c]);
                }
            }
            byte bestTable = 0;
            int bestCost = cost[0];
            byte b = 1;
            while (true) {
                byte i4 = b;
                if (i4 >= totalTables) {
                    break;
                }
                short s = cost[i4];
                if (s < bestCost) {
                    bestCost = s;
                    bestTable = i4;
                }
                b = (byte) (i4 + 1);
            }
            int[] bestGroupFrequencies = tableFrequencies[bestTable];
            for (int i5 = groupStart; i5 <= groupEnd; i5++) {
                char c2 = mtfBlock[i5];
                bestGroupFrequencies[c2] = bestGroupFrequencies[c2] + 1;
            }
            if (storeSelectors) {
                int i6 = selectorIndex;
                selectorIndex++;
                selectors[i6] = bestTable;
            }
            i = groupEnd + 1;
        }
        for (int i7 = 0; i7 < totalTables; i7++) {
            generateHuffmanCodeLengths(mtfAlphabetSize, tableFrequencies[i7], huffmanCodeLengths[i7]);
        }
    }

    private void assignHuffmanCodeSymbols() {
        int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        for (int i = 0; i < totalTables; i++) {
            int[] tableLengths = huffmanCodeLengths[i];
            int minimumLength = 32;
            int maximumLength = 0;
            for (int j = 0; j < mtfAlphabetSize; j++) {
                int length = tableLengths[j];
                if (length > maximumLength) {
                    maximumLength = length;
                }
                if (length < minimumLength) {
                    minimumLength = length;
                }
            }
            int code = 0;
            for (int j2 = minimumLength; j2 <= maximumLength; j2++) {
                for (int k = 0; k < mtfAlphabetSize; k++) {
                    if ((huffmanCodeLengths[i][k] & 255) == j2) {
                        huffmanMergedCodeSymbols[i][k] = (j2 << 24) | code;
                        code++;
                    }
                }
                code <<= 1;
            }
        }
    }

    private void writeSelectorsAndHuffmanTables(ByteBuf out) {
        Bzip2BitWriter writer = this.writer;
        byte[] selectors = this.selectors;
        int totalSelectors = selectors.length;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int totalTables = huffmanCodeLengths.length;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        writer.writeBits(out, 3, totalTables);
        writer.writeBits(out, 15, totalSelectors);
        Bzip2MoveToFrontTable selectorMTF = new Bzip2MoveToFrontTable();
        for (byte selector : selectors) {
            writer.writeUnary(out, selectorMTF.valueToFront(selector));
        }
        for (int[] tableLengths : huffmanCodeLengths) {
            int currentLength = tableLengths[0];
            writer.writeBits(out, 5, currentLength);
            for (int j = 0; j < mtfAlphabetSize; j++) {
                int codeLength = tableLengths[j];
                int value = currentLength < codeLength ? 2 : 3;
                int delta = Math.abs(codeLength - currentLength);
                while (true) {
                    int i = delta;
                    delta--;
                    if (i > 0) {
                        writer.writeBits(out, 2, value);
                    }
                }
                writer.writeBoolean(out, false);
                currentLength = codeLength;
            }
        }
    }

    private void writeBlockData(ByteBuf out) {
        Bzip2BitWriter writer = this.writer;
        int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        byte[] selectors = this.selectors;
        char[] mtf = this.mtfBlock;
        int mtfLength = this.mtfLength;
        int selectorIndex = 0;
        int mtfIndex = 0;
        while (mtfIndex < mtfLength) {
            int groupEnd = Math.min(mtfIndex + 50, mtfLength) - 1;
            int i = selectorIndex;
            selectorIndex++;
            int[] tableMergedCodeSymbols = huffmanMergedCodeSymbols[selectors[i]];
            while (mtfIndex <= groupEnd) {
                int i2 = mtfIndex;
                mtfIndex++;
                int mergedCodeSymbol = tableMergedCodeSymbols[mtf[i2]];
                writer.writeBits(out, mergedCodeSymbol >>> 24, mergedCodeSymbol);
            }
        }
    }

    void encode(ByteBuf out) {
        generateHuffmanOptimisationSeeds();
        int i = 3;
        while (i >= 0) {
            optimiseSelectorsAndHuffmanTables(i == 0);
            i--;
        }
        assignHuffmanCodeSymbols();
        writeSelectorsAndHuffmanTables(out);
        writeBlockData(out);
    }
}
