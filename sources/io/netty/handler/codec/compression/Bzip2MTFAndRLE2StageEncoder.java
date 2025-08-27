package io.netty.handler.codec.compression;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2MTFAndRLE2StageEncoder.class */
final class Bzip2MTFAndRLE2StageEncoder {
    private final int[] bwtBlock;
    private final int bwtLength;
    private final boolean[] bwtValuesPresent;
    private final char[] mtfBlock;
    private int mtfLength;
    private final int[] mtfSymbolFrequencies = new int[258];
    private int alphabetSize;

    Bzip2MTFAndRLE2StageEncoder(int[] bwtBlock, int bwtLength, boolean[] bwtValuesPresent) {
        this.bwtBlock = bwtBlock;
        this.bwtLength = bwtLength;
        this.bwtValuesPresent = bwtValuesPresent;
        this.mtfBlock = new char[bwtLength + 1];
    }

    void encode() {
        int bwtLength = this.bwtLength;
        boolean[] bwtValuesPresent = this.bwtValuesPresent;
        int[] bwtBlock = this.bwtBlock;
        char[] mtfBlock = this.mtfBlock;
        int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        byte[] huffmanSymbolMap = new byte[256];
        Bzip2MoveToFrontTable symbolMTF = new Bzip2MoveToFrontTable();
        int totalUniqueValues = 0;
        for (int i = 0; i < huffmanSymbolMap.length; i++) {
            if (bwtValuesPresent[i]) {
                int i2 = totalUniqueValues;
                totalUniqueValues++;
                huffmanSymbolMap[i] = (byte) i2;
            }
        }
        int endOfBlockSymbol = totalUniqueValues + 1;
        int mtfIndex = 0;
        int repeatCount = 0;
        int totalRunAs = 0;
        int totalRunBs = 0;
        for (int i3 = 0; i3 < bwtLength; i3++) {
            int mtfPosition = symbolMTF.valueToFront(huffmanSymbolMap[bwtBlock[i3] & 255]);
            if (mtfPosition == 0) {
                repeatCount++;
            } else {
                if (repeatCount > 0) {
                    int repeatCount2 = repeatCount - 1;
                    while (true) {
                        if ((repeatCount2 & 1) == 0) {
                            int i4 = mtfIndex;
                            mtfIndex++;
                            mtfBlock[i4] = 0;
                            totalRunAs++;
                        } else {
                            int i5 = mtfIndex;
                            mtfIndex++;
                            mtfBlock[i5] = 1;
                            totalRunBs++;
                        }
                        if (repeatCount2 <= 1) {
                            break;
                        } else {
                            repeatCount2 = (repeatCount2 - 2) >>> 1;
                        }
                    }
                    repeatCount = 0;
                }
                int i6 = mtfIndex;
                mtfIndex++;
                mtfBlock[i6] = (char) (mtfPosition + 1);
                int i7 = mtfPosition + 1;
                mtfSymbolFrequencies[i7] = mtfSymbolFrequencies[i7] + 1;
            }
        }
        if (repeatCount > 0) {
            int repeatCount3 = repeatCount - 1;
            while (true) {
                if ((repeatCount3 & 1) == 0) {
                    int i8 = mtfIndex;
                    mtfIndex++;
                    mtfBlock[i8] = 0;
                    totalRunAs++;
                } else {
                    int i9 = mtfIndex;
                    mtfIndex++;
                    mtfBlock[i9] = 1;
                    totalRunBs++;
                }
                if (repeatCount3 <= 1) {
                    break;
                } else {
                    repeatCount3 = (repeatCount3 - 2) >>> 1;
                }
            }
        }
        mtfBlock[mtfIndex] = (char) endOfBlockSymbol;
        mtfSymbolFrequencies[endOfBlockSymbol] = mtfSymbolFrequencies[endOfBlockSymbol] + 1;
        mtfSymbolFrequencies[0] = mtfSymbolFrequencies[0] + totalRunAs;
        mtfSymbolFrequencies[1] = mtfSymbolFrequencies[1] + totalRunBs;
        this.mtfLength = mtfIndex + 1;
        this.alphabetSize = endOfBlockSymbol + 1;
    }

    char[] mtfBlock() {
        return this.mtfBlock;
    }

    int mtfLength() {
        return this.mtfLength;
    }

    int mtfAlphabetSize() {
        return this.alphabetSize;
    }

    int[] mtfSymbolFrequencies() {
        return this.mtfSymbolFrequencies;
    }
}
