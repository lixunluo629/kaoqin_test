package com.itextpdf.io.font;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/WoffConverter.class */
class WoffConverter {
    private static final long woffSignature = 2001684038;

    WoffConverter() {
    }

    public static boolean isWoffFont(byte[] woffBytes) {
        return bytesToUInt(woffBytes, 0) == woffSignature;
    }

    public static byte[] convert(byte[] woffBytes) throws IOException {
        byte[] uncompressedData;
        if (bytesToUInt(woffBytes, 0) != woffSignature) {
            throw new IllegalArgumentException();
        }
        int srcPos = 0 + 4;
        byte[] flavor = new byte[4];
        System.arraycopy(woffBytes, srcPos, flavor, 0, 4);
        int srcPos2 = srcPos + 4;
        if (bytesToUInt(woffBytes, srcPos2) != woffBytes.length) {
            throw new IllegalArgumentException();
        }
        int srcPos3 = srcPos2 + 4;
        byte[] numTables = new byte[2];
        System.arraycopy(woffBytes, srcPos3, numTables, 0, 2);
        int srcPos4 = srcPos3 + 2;
        if (bytesToUShort(woffBytes, srcPos4) != 0) {
            throw new IllegalArgumentException();
        }
        int srcPos5 = srcPos4 + 2;
        long totalSfntSize = bytesToUInt(woffBytes, srcPos5);
        int srcPos6 = srcPos5 + 4 + 2 + 2 + 4 + 4 + 4 + 4 + 4;
        byte[] otfBytes = new byte[(int) totalSfntSize];
        System.arraycopy(flavor, 0, otfBytes, 0, 4);
        int destPos = 0 + 4;
        System.arraycopy(numTables, 0, otfBytes, destPos, 2);
        int destPos2 = destPos + 2;
        int entrySelector = -1;
        int searchRange = -1;
        int numTablesVal = bytesToUShort(numTables, 0);
        int i = 0;
        while (true) {
            if (i >= 17) {
                break;
            }
            int powOfTwo = (int) Math.pow(2.0d, i);
            if (powOfTwo <= numTablesVal) {
                i++;
            } else {
                entrySelector = i;
                searchRange = powOfTwo * 16;
                break;
            }
        }
        if (entrySelector < 0) {
            throw new IllegalArgumentException();
        }
        otfBytes[destPos2] = (byte) (searchRange >> 8);
        otfBytes[destPos2 + 1] = (byte) searchRange;
        int destPos3 = destPos2 + 2;
        otfBytes[destPos3] = (byte) (entrySelector >> 8);
        otfBytes[destPos3 + 1] = (byte) entrySelector;
        int destPos4 = destPos3 + 2;
        int rangeShift = (numTablesVal * 16) - searchRange;
        otfBytes[destPos4] = (byte) (rangeShift >> 8);
        otfBytes[destPos4 + 1] = (byte) rangeShift;
        int destPos5 = destPos4 + 2;
        int outTableOffset = destPos5;
        List<TableDirectory> tdList = new ArrayList<>(numTablesVal);
        for (int i2 = 0; i2 < numTablesVal; i2++) {
            TableDirectory td = new TableDirectory();
            System.arraycopy(woffBytes, srcPos6, td.tag, 0, 4);
            int srcPos7 = srcPos6 + 4;
            td.offset = bytesToUInt(woffBytes, srcPos7);
            int srcPos8 = srcPos7 + 4;
            if (td.offset % 4 != 0) {
                throw new IllegalArgumentException();
            }
            td.compLength = bytesToUInt(woffBytes, srcPos8);
            int srcPos9 = srcPos8 + 4;
            System.arraycopy(woffBytes, srcPos9, td.origLength, 0, 4);
            td.origLengthVal = bytesToUInt(td.origLength, 0);
            int srcPos10 = srcPos9 + 4;
            System.arraycopy(woffBytes, srcPos10, td.origChecksum, 0, 4);
            srcPos6 = srcPos10 + 4;
            tdList.add(td);
            outTableOffset += 16;
        }
        for (TableDirectory td2 : tdList) {
            System.arraycopy(td2.tag, 0, otfBytes, destPos5, 4);
            int destPos6 = destPos5 + 4;
            System.arraycopy(td2.origChecksum, 0, otfBytes, destPos6, 4);
            int destPos7 = destPos6 + 4;
            otfBytes[destPos7] = (byte) (outTableOffset >> 24);
            otfBytes[destPos7 + 1] = (byte) (outTableOffset >> 16);
            otfBytes[destPos7 + 2] = (byte) (outTableOffset >> 8);
            otfBytes[destPos7 + 3] = (byte) outTableOffset;
            int destPos8 = destPos7 + 4;
            System.arraycopy(td2.origLength, 0, otfBytes, destPos8, 4);
            destPos5 = destPos8 + 4;
            td2.outOffset = outTableOffset;
            outTableOffset += (int) td2.origLengthVal;
            if (outTableOffset % 4 != 0) {
                outTableOffset += 4 - (outTableOffset % 4);
            }
        }
        if (outTableOffset != totalSfntSize) {
            throw new IllegalArgumentException();
        }
        for (TableDirectory td3 : tdList) {
            byte[] compressedData = new byte[(int) td3.compLength];
            System.arraycopy(woffBytes, (int) td3.offset, compressedData, 0, (int) td3.compLength);
            int expectedUncompressedLen = (int) td3.origLengthVal;
            if (td3.compLength > td3.origLengthVal) {
                throw new IllegalArgumentException();
            }
            if (td3.compLength != td3.origLengthVal) {
                ByteArrayInputStream stream = new ByteArrayInputStream(compressedData);
                InflaterInputStream zip = new InflaterInputStream(stream);
                uncompressedData = new byte[expectedUncompressedLen];
                int i3 = 0;
                while (true) {
                    int bytesRead = i3;
                    if (expectedUncompressedLen - bytesRead > 0) {
                        int readRes = zip.read(uncompressedData, bytesRead, expectedUncompressedLen - bytesRead);
                        if (readRes < 0) {
                            throw new IllegalArgumentException();
                        }
                        i3 = bytesRead + readRes;
                    } else if (zip.read() >= 0) {
                        throw new IllegalArgumentException();
                    }
                }
            } else {
                uncompressedData = compressedData;
            }
            System.arraycopy(uncompressedData, 0, otfBytes, td3.outOffset, expectedUncompressedLen);
        }
        return otfBytes;
    }

    private static long bytesToUInt(byte[] b, int start) {
        return ((b[start] & 255) << 24) | ((b[start + 1] & 255) << 16) | ((b[start + 2] & 255) << 8) | (b[start + 3] & 255);
    }

    private static int bytesToUShort(byte[] b, int start) {
        return ((b[start] & 255) << 8) | (b[start + 1] & 255);
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/WoffConverter$TableDirectory.class */
    private static class TableDirectory {
        byte[] tag;
        long offset;
        long compLength;
        byte[] origLength;
        long origLengthVal;
        byte[] origChecksum;
        int outOffset;

        private TableDirectory() {
            this.tag = new byte[4];
            this.origLength = new byte[4];
            this.origChecksum = new byte[4];
        }
    }
}
