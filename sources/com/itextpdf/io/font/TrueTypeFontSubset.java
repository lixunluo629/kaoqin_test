package com.itextpdf.io.font;

import com.itextpdf.io.source.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/TrueTypeFontSubset.class */
class TrueTypeFontSubset {
    private static final String[] TABLE_NAMES_SUBSET = {"cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep", "cmap", "OS/2"};
    private static final String[] TABLE_NAMES = {"cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep", "cmap", "OS/2", "name", "post"};
    private static final int[] entrySelectors = {0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
    private static final int TABLE_CHECKSUM = 0;
    private static final int TABLE_OFFSET = 1;
    private static final int TABLE_LENGTH = 2;
    private static final int HEAD_LOCA_FORMAT_OFFSET = 51;
    private static final int ARG_1_AND_2_ARE_WORDS = 1;
    private static final int WE_HAVE_A_SCALE = 8;
    private static final int MORE_COMPONENTS = 32;
    private static final int WE_HAVE_AN_X_AND_Y_SCALE = 64;
    private static final int WE_HAVE_A_TWO_BY_TWO = 128;
    private Map<String, int[]> tableDirectory;
    protected RandomAccessFileOrArray rf;
    private String fileName;
    private boolean locaShortTable;
    private int[] locaTable;
    private Set<Integer> glyphsUsed;
    private List<Integer> glyphsInList;
    private int tableGlyphOffset;
    private int[] newLocaTable;
    private byte[] newLocaTableOut;
    private byte[] newGlyfTable;
    private int glyfTableRealSize;
    private int locaTableRealSize;
    private byte[] outFont;
    private int fontPtr;
    private int directoryOffset;
    private final String[] tableNames;

    TrueTypeFontSubset(String fileName, RandomAccessFileOrArray rf, Set<Integer> glyphsUsed, int directoryOffset, boolean subset) {
        this.fileName = fileName;
        this.rf = rf;
        this.glyphsUsed = new HashSet(glyphsUsed);
        this.directoryOffset = directoryOffset;
        if (subset) {
            this.tableNames = TABLE_NAMES_SUBSET;
        } else {
            this.tableNames = TABLE_NAMES;
        }
        this.glyphsInList = new ArrayList(glyphsUsed);
    }

    byte[] process() throws IOException {
        try {
            createTableDirectory();
            readLoca();
            flatGlyphs();
            createNewGlyphTables();
            locaToBytes();
            assembleFont();
            return this.outFont;
        } finally {
            try {
                this.rf.close();
            } catch (Exception e) {
            }
        }
    }

    private void assembleFont() throws IOException {
        int len;
        int[] tableLocation;
        int fullFontSize = 0;
        int tablesUsed = 2;
        for (String name : this.tableNames) {
            if (!name.equals("glyf") && !name.equals("loca") && (tableLocation = this.tableDirectory.get(name)) != null) {
                tablesUsed++;
                fullFontSize += (tableLocation[2] + 3) & (-4);
            }
        }
        int reference = (16 * tablesUsed) + 12;
        this.outFont = new byte[fullFontSize + this.newLocaTableOut.length + this.newGlyfTable.length + reference];
        this.fontPtr = 0;
        writeFontInt(65536);
        writeFontShort(tablesUsed);
        int selector = entrySelectors[tablesUsed];
        writeFontShort((1 << selector) * 16);
        writeFontShort(selector);
        writeFontShort((tablesUsed - (1 << selector)) * 16);
        for (String name2 : this.tableNames) {
            int[] tableLocation2 = this.tableDirectory.get(name2);
            if (tableLocation2 != null) {
                writeFontString(name2);
                switch (name2) {
                    case "glyf":
                        writeFontInt(calculateChecksum(this.newGlyfTable));
                        len = this.glyfTableRealSize;
                        break;
                    case "loca":
                        writeFontInt(calculateChecksum(this.newLocaTableOut));
                        len = this.locaTableRealSize;
                        break;
                    default:
                        writeFontInt(tableLocation2[0]);
                        len = tableLocation2[2];
                        break;
                }
                writeFontInt(reference);
                writeFontInt(len);
                reference += (len + 3) & (-4);
            }
        }
        for (String name3 : this.tableNames) {
            int[] tableLocation3 = this.tableDirectory.get(name3);
            if (tableLocation3 != null) {
                switch (name3) {
                    case "glyf":
                        System.arraycopy(this.newGlyfTable, 0, this.outFont, this.fontPtr, this.newGlyfTable.length);
                        this.fontPtr += this.newGlyfTable.length;
                        this.newGlyfTable = null;
                        break;
                    case "loca":
                        System.arraycopy(this.newLocaTableOut, 0, this.outFont, this.fontPtr, this.newLocaTableOut.length);
                        this.fontPtr += this.newLocaTableOut.length;
                        this.newLocaTableOut = null;
                        break;
                    default:
                        this.rf.seek(tableLocation3[1]);
                        this.rf.readFully(this.outFont, this.fontPtr, tableLocation3[2]);
                        this.fontPtr += (tableLocation3[2] + 3) & (-4);
                        break;
                }
            }
        }
    }

    private void createTableDirectory() throws IOException {
        this.tableDirectory = new HashMap();
        this.rf.seek(this.directoryOffset);
        int id = this.rf.readInt();
        if (id != 65536) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.NotAtTrueTypeFile).setMessageParams(this.fileName);
        }
        int num_tables = this.rf.readUnsignedShort();
        this.rf.skipBytes(6);
        for (int k = 0; k < num_tables; k++) {
            String tag = readStandardString(4);
            int[] tableLocation = {this.rf.readInt(), this.rf.readInt(), this.rf.readInt()};
            this.tableDirectory.put(tag, tableLocation);
        }
    }

    private void readLoca() throws IOException {
        if (this.tableDirectory.get("head") == null) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("head", this.fileName);
        }
        this.rf.seek(r0[1] + 51);
        this.locaShortTable = this.rf.readUnsignedShort() == 0;
        int[] tableLocation = this.tableDirectory.get("loca");
        if (tableLocation == null) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("loca", this.fileName);
        }
        this.rf.seek(tableLocation[1]);
        if (this.locaShortTable) {
            int entries = tableLocation[2] / 2;
            this.locaTable = new int[entries];
            for (int k = 0; k < entries; k++) {
                this.locaTable[k] = this.rf.readUnsignedShort() * 2;
            }
            return;
        }
        int entries2 = tableLocation[2] / 4;
        this.locaTable = new int[entries2];
        for (int k2 = 0; k2 < entries2; k2++) {
            this.locaTable[k2] = this.rf.readInt();
        }
    }

    private void createNewGlyphTables() throws IOException {
        this.newLocaTable = new int[this.locaTable.length];
        int[] activeGlyphs = new int[this.glyphsInList.size()];
        for (int k = 0; k < activeGlyphs.length; k++) {
            activeGlyphs[k] = this.glyphsInList.get(k).intValue();
        }
        Arrays.sort(activeGlyphs);
        int glyfSize = 0;
        for (int glyph : activeGlyphs) {
            glyfSize += this.locaTable[glyph + 1] - this.locaTable[glyph];
        }
        this.glyfTableRealSize = glyfSize;
        this.newGlyfTable = new byte[(glyfSize + 3) & (-4)];
        int glyfPtr = 0;
        int listGlyf = 0;
        for (int k2 = 0; k2 < this.newLocaTable.length; k2++) {
            this.newLocaTable[k2] = glyfPtr;
            if (listGlyf < activeGlyphs.length && activeGlyphs[listGlyf] == k2) {
                listGlyf++;
                this.newLocaTable[k2] = glyfPtr;
                int start = this.locaTable[k2];
                int len = this.locaTable[k2 + 1] - start;
                if (len > 0) {
                    this.rf.seek(this.tableGlyphOffset + start);
                    this.rf.readFully(this.newGlyfTable, glyfPtr, len);
                    glyfPtr += len;
                }
            }
        }
    }

    private void locaToBytes() {
        if (this.locaShortTable) {
            this.locaTableRealSize = this.newLocaTable.length * 2;
        } else {
            this.locaTableRealSize = this.newLocaTable.length * 4;
        }
        this.newLocaTableOut = new byte[(this.locaTableRealSize + 3) & (-4)];
        this.outFont = this.newLocaTableOut;
        this.fontPtr = 0;
        for (int location : this.newLocaTable) {
            if (this.locaShortTable) {
                writeFontShort(location / 2);
            } else {
                writeFontInt(location);
            }
        }
    }

    private void flatGlyphs() throws IOException {
        int[] tableLocation = this.tableDirectory.get("glyf");
        if (tableLocation == null) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("glyf", this.fileName);
        }
        if (!this.glyphsUsed.contains(0)) {
            this.glyphsUsed.add(0);
            this.glyphsInList.add(0);
        }
        this.tableGlyphOffset = tableLocation[1];
        for (int i = 0; i < this.glyphsInList.size(); i++) {
            checkGlyphComposite(this.glyphsInList.get(i).intValue());
        }
    }

    private void checkGlyphComposite(int glyph) throws IOException {
        int skip;
        int start = this.locaTable[glyph];
        if (start == this.locaTable[glyph + 1]) {
            return;
        }
        this.rf.seek(this.tableGlyphOffset + start);
        int numContours = this.rf.readShort();
        if (numContours >= 0) {
            return;
        }
        this.rf.skipBytes(8);
        while (true) {
            int flags = this.rf.readUnsignedShort();
            int cGlyph = this.rf.readUnsignedShort();
            if (!this.glyphsUsed.contains(Integer.valueOf(cGlyph))) {
                this.glyphsUsed.add(Integer.valueOf(cGlyph));
                this.glyphsInList.add(Integer.valueOf(cGlyph));
            }
            if ((flags & 32) == 0) {
                return;
            }
            if ((flags & 1) != 0) {
                skip = 4;
            } else {
                skip = 2;
            }
            if ((flags & 8) != 0) {
                skip += 2;
            } else if ((flags & 64) != 0) {
                skip += 4;
            }
            if ((flags & 128) != 0) {
                skip += 8;
            }
            this.rf.skipBytes(skip);
        }
    }

    private String readStandardString(int length) throws IOException {
        byte[] buf = new byte[length];
        this.rf.readFully(buf);
        try {
            return new String(buf, "Cp1252");
        } catch (Exception e) {
            throw new com.itextpdf.io.IOException("TrueType font", (Throwable) e);
        }
    }

    private void writeFontShort(int n) {
        byte[] bArr = this.outFont;
        int i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 8);
        byte[] bArr2 = this.outFont;
        int i2 = this.fontPtr;
        this.fontPtr = i2 + 1;
        bArr2[i2] = (byte) n;
    }

    private void writeFontInt(int n) {
        byte[] bArr = this.outFont;
        int i = this.fontPtr;
        this.fontPtr = i + 1;
        bArr[i] = (byte) (n >> 24);
        byte[] bArr2 = this.outFont;
        int i2 = this.fontPtr;
        this.fontPtr = i2 + 1;
        bArr2[i2] = (byte) (n >> 16);
        byte[] bArr3 = this.outFont;
        int i3 = this.fontPtr;
        this.fontPtr = i3 + 1;
        bArr3[i3] = (byte) (n >> 8);
        byte[] bArr4 = this.outFont;
        int i4 = this.fontPtr;
        this.fontPtr = i4 + 1;
        bArr4[i4] = (byte) n;
    }

    private void writeFontString(String s) {
        byte[] b = PdfEncodings.convertToBytes(s, "Cp1252");
        System.arraycopy(b, 0, this.outFont, this.fontPtr, b.length);
        this.fontPtr += b.length;
    }

    private int calculateChecksum(byte[] b) {
        int len = b.length / 4;
        int v0 = 0;
        int v1 = 0;
        int v2 = 0;
        int v3 = 0;
        int ptr = 0;
        for (int k = 0; k < len; k++) {
            int i = ptr;
            int ptr2 = ptr + 1;
            v3 += b[i] & 255;
            int ptr3 = ptr2 + 1;
            v2 += b[ptr2] & 255;
            int ptr4 = ptr3 + 1;
            v1 += b[ptr3] & 255;
            ptr = ptr4 + 1;
            v0 += b[ptr4] & 255;
        }
        return v0 + (v1 << 8) + (v2 << 16) + (v3 << 24);
    }
}
