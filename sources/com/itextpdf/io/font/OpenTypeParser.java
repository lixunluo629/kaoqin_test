package com.itextpdf.io.font;

import com.itextpdf.io.font.constants.FontStretches;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.util.IntHashtable;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser.class */
class OpenTypeParser implements Serializable, Closeable {
    private static final long serialVersionUID = 3399061674525229738L;
    private static final int HEAD_LOCA_FORMAT_OFFSET = 51;
    protected String fileName;
    protected RandomAccessFileOrArray raf;
    protected int ttcIndex;
    protected int directoryOffset;
    protected String fontName;
    protected Map<Integer, List<String[]>> allNameEntries;
    protected boolean cff;
    protected int cffOffset;
    protected int cffLength;
    private int[] glyphWidthsByIndex;
    protected HeaderTable head;
    protected HorizontalHeader hhea;
    protected WindowsMetrics os_2;
    protected PostTable post;
    protected CmapTable cmaps;
    protected Map<String, int[]> tables;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser$HeaderTable.class */
    static class HeaderTable implements Serializable {
        private static final long serialVersionUID = 5849907401352439751L;
        int flags;
        int unitsPerEm;
        short xMin;
        short yMin;
        short xMax;
        short yMax;
        int macStyle;

        HeaderTable() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser$HorizontalHeader.class */
    static class HorizontalHeader implements Serializable {
        private static final long serialVersionUID = -6857266170153679811L;
        short Ascender;
        short Descender;
        short LineGap;
        int advanceWidthMax;
        short minLeftSideBearing;
        short minRightSideBearing;
        short xMaxExtent;
        short caretSlopeRise;
        short caretSlopeRun;
        int numberOfHMetrics;

        HorizontalHeader() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser$WindowsMetrics.class */
    static class WindowsMetrics implements Serializable {
        private static final long serialVersionUID = -9117114979326346658L;
        short xAvgCharWidth;
        int usWeightClass;
        int usWidthClass;
        short fsType;
        short ySubscriptXSize;
        short ySubscriptYSize;
        short ySubscriptXOffset;
        short ySubscriptYOffset;
        short ySuperscriptXSize;
        short ySuperscriptYSize;
        short ySuperscriptXOffset;
        short ySuperscriptYOffset;
        short yStrikeoutSize;
        short yStrikeoutPosition;
        short sFamilyClass;
        byte[] panose = new byte[10];
        byte[] achVendID = new byte[4];
        int fsSelection;
        int usFirstCharIndex;
        int usLastCharIndex;
        short sTypoAscender;
        short sTypoDescender;
        short sTypoLineGap;
        int usWinAscent;
        int usWinDescent;
        int ulCodePageRange1;
        int ulCodePageRange2;
        int sxHeight;
        int sCapHeight;

        WindowsMetrics() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser$PostTable.class */
    static class PostTable implements Serializable {
        private static final long serialVersionUID = 5735677308357646890L;
        float italicAngle;
        int underlinePosition;
        int underlineThickness;
        boolean isFixedPitch;

        PostTable() {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/OpenTypeParser$CmapTable.class */
    static class CmapTable implements Serializable {
        private static final long serialVersionUID = 8923883989692194983L;
        Map<Integer, int[]> cmap10;
        Map<Integer, int[]> cmap31;
        Map<Integer, int[]> cmapExt;
        boolean fontSpecific = false;

        CmapTable() {
        }
    }

    public OpenTypeParser(byte[] ttf) throws IOException {
        this.ttcIndex = -1;
        this.cff = false;
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(ttf));
        initializeSfntTables();
    }

    public OpenTypeParser(byte[] ttc, int ttcIndex) throws IOException {
        this.ttcIndex = -1;
        this.cff = false;
        this.ttcIndex = ttcIndex;
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(ttc));
        initializeSfntTables();
    }

    public OpenTypeParser(String ttcPath, int ttcIndex) throws IOException {
        this.ttcIndex = -1;
        this.cff = false;
        this.ttcIndex = ttcIndex;
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(ttcPath));
        initializeSfntTables();
    }

    public OpenTypeParser(String name) throws IOException {
        this.ttcIndex = -1;
        this.cff = false;
        String ttcName = getTTCName(name);
        this.fileName = ttcName;
        if (ttcName.length() < name.length()) {
            this.ttcIndex = Integer.parseInt(name.substring(ttcName.length() + 1));
        }
        this.raf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(this.fileName));
        initializeSfntTables();
    }

    public String getPsFontName() {
        if (this.fontName == null) {
            List<String[]> names = this.allNameEntries.get(6);
            if (names != null && names.size() > 0) {
                this.fontName = names.get(0)[3];
            } else {
                this.fontName = new File(this.fileName).getName().replace(' ', '-');
            }
        }
        return this.fontName;
    }

    public Map<Integer, List<String[]>> getAllNameEntries() {
        return this.allNameEntries;
    }

    public PostTable getPostTable() {
        return this.post;
    }

    public WindowsMetrics getOs_2Table() {
        return this.os_2;
    }

    public HorizontalHeader getHheaTable() {
        return this.hhea;
    }

    public HeaderTable getHeadTable() {
        return this.head;
    }

    public CmapTable getCmapTable() {
        return this.cmaps;
    }

    public int[] getGlyphWidthsByIndex() {
        return this.glyphWidthsByIndex;
    }

    public FontNames getFontNames() {
        FontNames fontNames = new FontNames();
        fontNames.setAllNames(getAllNameEntries());
        fontNames.setFontName(getPsFontName());
        fontNames.setFullName(fontNames.getNames(4));
        String[][] otfFamilyName = fontNames.getNames(16);
        if (otfFamilyName != null) {
            fontNames.setFamilyName(otfFamilyName);
        } else {
            fontNames.setFamilyName(fontNames.getNames(1));
        }
        String[][] subfamily = fontNames.getNames(2);
        if (subfamily != null) {
            fontNames.setStyle(subfamily[0][3]);
        }
        String[][] otfSubFamily = fontNames.getNames(17);
        if (otfFamilyName != null) {
            fontNames.setSubfamily(otfSubFamily);
        } else {
            fontNames.setSubfamily(subfamily);
        }
        String[][] cidName = fontNames.getNames(20);
        if (cidName != null) {
            fontNames.setCidFontName(cidName[0][3]);
        }
        fontNames.setFontWeight(this.os_2.usWeightClass);
        fontNames.setFontStretch(FontStretches.fromOpenTypeWidthClass(this.os_2.usWidthClass));
        fontNames.setMacStyle(this.head.macStyle);
        fontNames.setAllowEmbedding(this.os_2.fsType != 2);
        return fontNames;
    }

    public boolean isCff() {
        return this.cff;
    }

    public byte[] getFullFont() throws IOException {
        RandomAccessFileOrArray rf2 = null;
        try {
            rf2 = this.raf.createView();
            byte[] b = new byte[(int) rf2.length()];
            rf2.readFully(b);
            if (rf2 != null) {
                try {
                    rf2.close();
                } catch (Exception e) {
                }
            }
            return b;
        } catch (Throwable th) {
            if (rf2 != null) {
                try {
                    rf2.close();
                } catch (Exception e2) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public byte[] readCffFont() throws IOException {
        if (!isCff()) {
            return null;
        }
        RandomAccessFileOrArray rf2 = null;
        try {
            rf2 = this.raf.createView();
            rf2.seek(this.cffOffset);
            byte[] cff = new byte[this.cffLength];
            rf2.readFully(cff);
            if (rf2 != null) {
                try {
                    rf2.close();
                } catch (Exception e) {
                }
            }
            return cff;
        } catch (Throwable th) {
            if (rf2 != null) {
                try {
                    rf2.close();
                } catch (Exception e2) {
                    throw th;
                }
            }
            throw th;
        }
    }

    byte[] getSubset(Set<Integer> glyphs, boolean subset) throws IOException {
        TrueTypeFontSubset sb = new TrueTypeFontSubset(this.fileName, this.raf.createView(), glyphs, this.directoryOffset, subset);
        return sb.process();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
        this.raf = null;
    }

    private void initializeSfntTables() throws IOException {
        this.tables = new LinkedHashMap();
        if (this.ttcIndex >= 0) {
            int dirIdx = this.ttcIndex;
            if (dirIdx < 0) {
                if (this.fileName == null) {
                    throw new com.itextpdf.io.IOException("The font index must be positive.");
                }
                throw new com.itextpdf.io.IOException("The font index for {0} must be positive.").setMessageParams(this.fileName);
            }
            String mainTag = readStandardString(4);
            if (!mainTag.equals("ttcf")) {
                if (this.fileName == null) {
                    throw new com.itextpdf.io.IOException("Not a valid ttc file.");
                }
                throw new com.itextpdf.io.IOException("{0} is not a valid ttc file.").setMessageParams(this.fileName);
            }
            this.raf.skipBytes(4);
            int dirCount = this.raf.readInt();
            if (dirIdx >= dirCount) {
                if (this.fileName == null) {
                    throw new com.itextpdf.io.IOException("The font index must be between 0 and {0}. It is {1}.").setMessageParams(Integer.valueOf(dirCount - 1), Integer.valueOf(dirIdx));
                }
                throw new com.itextpdf.io.IOException("The font index for {0} must be between 0 and {1}. It is {2}.").setMessageParams(this.fileName, Integer.valueOf(dirCount - 1), Integer.valueOf(dirIdx));
            }
            this.raf.skipBytes(dirIdx * 4);
            this.directoryOffset = this.raf.readInt();
        }
        this.raf.seek(this.directoryOffset);
        int ttId = this.raf.readInt();
        if (ttId != 65536 && ttId != 1330926671) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException("Not a valid ttf or otf file.");
            }
            throw new com.itextpdf.io.IOException("{0} is not a valid ttf or otf file.").setMessageParams(this.fileName);
        }
        int num_tables = this.raf.readUnsignedShort();
        this.raf.skipBytes(6);
        for (int k = 0; k < num_tables; k++) {
            String tag = readStandardString(4);
            this.raf.skipBytes(4);
            int[] table_location = {this.raf.readInt(), this.raf.readInt()};
            this.tables.put(tag, table_location);
        }
    }

    protected void loadTables(boolean all) throws IOException {
        readNameTable();
        readHeadTable();
        readOs_2Table();
        readPostTable();
        if (all) {
            checkCff();
            readHheaTable();
            readGlyphWidths();
            readCmapTable();
        }
    }

    protected static String getTTCName(String name) {
        if (name == null) {
            return null;
        }
        int idx = name.toLowerCase().indexOf(".ttc,");
        if (idx < 0) {
            return name;
        }
        return name.substring(0, idx + 4);
    }

    protected void checkCff() {
        int[] table_location = this.tables.get("CFF ");
        if (table_location != null) {
            this.cff = true;
            this.cffOffset = table_location[0];
            this.cffLength = table_location[1];
        }
    }

    protected void readGlyphWidths() throws IOException {
        int numberOfHMetrics = this.hhea.numberOfHMetrics;
        int unitsPerEm = this.head.unitsPerEm;
        int[] table_location = this.tables.get("hmtx");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("hmtx");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("hmtx", this.fileName);
        }
        this.glyphWidthsByIndex = new int[readNumGlyphs()];
        this.raf.seek(table_location[0]);
        for (int k = 0; k < numberOfHMetrics; k++) {
            this.glyphWidthsByIndex[k] = (this.raf.readUnsignedShort() * 1000) / unitsPerEm;
            int i = (this.raf.readShort() * 1000) / unitsPerEm;
        }
        if (numberOfHMetrics > 0) {
            for (int k2 = numberOfHMetrics; k2 < this.glyphWidthsByIndex.length; k2++) {
                this.glyphWidthsByIndex[k2] = this.glyphWidthsByIndex[numberOfHMetrics - 1];
            }
        }
    }

    protected IntHashtable readKerning(int unitsPerEm) throws IOException {
        int[] table_location = this.tables.get("kern");
        IntHashtable kerning = new IntHashtable();
        if (table_location == null) {
            return kerning;
        }
        this.raf.seek(table_location[0] + 2);
        int nTables = this.raf.readUnsignedShort();
        int checkpoint = table_location[0] + 4;
        int length = 0;
        for (int k = 0; k < nTables; k++) {
            checkpoint += length;
            this.raf.seek(checkpoint);
            this.raf.skipBytes(2);
            length = this.raf.readUnsignedShort();
            int coverage = this.raf.readUnsignedShort();
            if ((coverage & 65527) == 1) {
                int nPairs = this.raf.readUnsignedShort();
                this.raf.skipBytes(6);
                for (int j = 0; j < nPairs; j++) {
                    int pair = this.raf.readInt();
                    int value = (this.raf.readShort() * 1000) / unitsPerEm;
                    kerning.put(pair, value);
                }
            }
        }
        return kerning;
    }

    /* JADX WARN: Type inference failed for: r0v38, types: [int[], int[][]] */
    protected int[][] readBbox(int unitsPerEm) throws IOException {
        int[] locaTable;
        if (this.tables.get("head") == null) {
            if (this.fileName != null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("head", this.fileName);
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("head");
        }
        this.raf.seek(r0[0] + 51);
        boolean locaShortTable = this.raf.readUnsignedShort() == 0;
        int[] tableLocation = this.tables.get("loca");
        if (tableLocation == null) {
            return (int[][]) null;
        }
        this.raf.seek(tableLocation[0]);
        if (locaShortTable) {
            int entries = tableLocation[1] / 2;
            locaTable = new int[entries];
            for (int k = 0; k < entries; k++) {
                locaTable[k] = this.raf.readUnsignedShort() * 2;
            }
        } else {
            int entries2 = tableLocation[1] / 4;
            locaTable = new int[entries2];
            for (int k2 = 0; k2 < entries2; k2++) {
                locaTable[k2] = this.raf.readInt();
            }
        }
        int[] tableLocation2 = this.tables.get("glyf");
        if (tableLocation2 == null) {
            if (this.fileName != null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("glyf", this.fileName);
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("glyf");
        }
        int tableGlyphOffset = tableLocation2[0];
        ?? r0 = new int[locaTable.length - 1];
        for (int glyph = 0; glyph < locaTable.length - 1; glyph++) {
            int start = locaTable[glyph];
            if (start != locaTable[glyph + 1]) {
                this.raf.seek(tableGlyphOffset + start + 2);
                r0[glyph] = new int[]{(this.raf.readShort() * 1000) / unitsPerEm, (this.raf.readShort() * 1000) / unitsPerEm, (this.raf.readShort() * 1000) / unitsPerEm, (this.raf.readShort() * 1000) / unitsPerEm};
            }
        }
        return r0;
    }

    protected int readNumGlyphs() throws IOException {
        int[] table_location = this.tables.get("maxp");
        if (table_location == null) {
            return 65536;
        }
        this.raf.seek(table_location[0] + 4);
        return this.raf.readUnsignedShort();
    }

    private void readNameTable() throws IOException {
        List<String[]> names;
        String unicodeString;
        int[] table_location = this.tables.get("name");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("name");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("name", this.fileName);
        }
        this.allNameEntries = new LinkedHashMap();
        this.raf.seek(table_location[0] + 2);
        int numRecords = this.raf.readUnsignedShort();
        int startOfStorage = this.raf.readUnsignedShort();
        for (int k = 0; k < numRecords; k++) {
            int platformID = this.raf.readUnsignedShort();
            int platformEncodingID = this.raf.readUnsignedShort();
            int languageID = this.raf.readUnsignedShort();
            int nameID = this.raf.readUnsignedShort();
            int length = this.raf.readUnsignedShort();
            int offset = this.raf.readUnsignedShort();
            if (this.allNameEntries.containsKey(Integer.valueOf(nameID))) {
                names = this.allNameEntries.get(Integer.valueOf(nameID));
            } else {
                Map<Integer, List<String[]>> map = this.allNameEntries;
                Integer numValueOf = Integer.valueOf(nameID);
                ArrayList arrayList = new ArrayList();
                names = arrayList;
                map.put(numValueOf, arrayList);
            }
            int pos = (int) this.raf.getPosition();
            this.raf.seek(table_location[0] + startOfStorage + offset);
            if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)) {
                unicodeString = readUnicodeString(length);
            } else {
                unicodeString = readStandardString(length);
            }
            String name = unicodeString;
            names.add(new String[]{Integer.toString(platformID), Integer.toString(platformEncodingID), Integer.toString(languageID), name});
            this.raf.seek(pos);
        }
    }

    private void readHheaTable() throws IOException {
        int[] table_location = this.tables.get("hhea");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("hhea");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("hhea", this.fileName);
        }
        this.raf.seek(table_location[0] + 4);
        this.hhea = new HorizontalHeader();
        this.hhea.Ascender = this.raf.readShort();
        this.hhea.Descender = this.raf.readShort();
        this.hhea.LineGap = this.raf.readShort();
        this.hhea.advanceWidthMax = this.raf.readUnsignedShort();
        this.hhea.minLeftSideBearing = this.raf.readShort();
        this.hhea.minRightSideBearing = this.raf.readShort();
        this.hhea.xMaxExtent = this.raf.readShort();
        this.hhea.caretSlopeRise = this.raf.readShort();
        this.hhea.caretSlopeRun = this.raf.readShort();
        this.raf.skipBytes(12);
        this.hhea.numberOfHMetrics = this.raf.readUnsignedShort();
    }

    private void readHeadTable() throws IOException {
        int[] table_location = this.tables.get("head");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("head");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("head", this.fileName);
        }
        this.raf.seek(table_location[0] + 16);
        this.head = new HeaderTable();
        this.head.flags = this.raf.readUnsignedShort();
        this.head.unitsPerEm = this.raf.readUnsignedShort();
        this.raf.skipBytes(16);
        this.head.xMin = this.raf.readShort();
        this.head.yMin = this.raf.readShort();
        this.head.xMax = this.raf.readShort();
        this.head.yMax = this.raf.readShort();
        this.head.macStyle = this.raf.readUnsignedShort();
    }

    private void readOs_2Table() throws IOException {
        int[] table_location = this.tables.get("OS/2");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("os/2");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("os/2", this.fileName);
        }
        this.os_2 = new WindowsMetrics();
        this.raf.seek(table_location[0]);
        int version = this.raf.readUnsignedShort();
        this.os_2.xAvgCharWidth = this.raf.readShort();
        this.os_2.usWeightClass = this.raf.readUnsignedShort();
        this.os_2.usWidthClass = this.raf.readUnsignedShort();
        this.os_2.fsType = this.raf.readShort();
        this.os_2.ySubscriptXSize = this.raf.readShort();
        this.os_2.ySubscriptYSize = this.raf.readShort();
        this.os_2.ySubscriptXOffset = this.raf.readShort();
        this.os_2.ySubscriptYOffset = this.raf.readShort();
        this.os_2.ySuperscriptXSize = this.raf.readShort();
        this.os_2.ySuperscriptYSize = this.raf.readShort();
        this.os_2.ySuperscriptXOffset = this.raf.readShort();
        this.os_2.ySuperscriptYOffset = this.raf.readShort();
        this.os_2.yStrikeoutSize = this.raf.readShort();
        this.os_2.yStrikeoutPosition = this.raf.readShort();
        this.os_2.sFamilyClass = this.raf.readShort();
        this.raf.readFully(this.os_2.panose);
        this.raf.skipBytes(16);
        this.raf.readFully(this.os_2.achVendID);
        this.os_2.fsSelection = this.raf.readUnsignedShort();
        this.os_2.usFirstCharIndex = this.raf.readUnsignedShort();
        this.os_2.usLastCharIndex = this.raf.readUnsignedShort();
        this.os_2.sTypoAscender = this.raf.readShort();
        this.os_2.sTypoDescender = this.raf.readShort();
        if (this.os_2.sTypoDescender > 0) {
            this.os_2.sTypoDescender = (short) (-this.os_2.sTypoDescender);
        }
        this.os_2.sTypoLineGap = this.raf.readShort();
        this.os_2.usWinAscent = this.raf.readUnsignedShort();
        this.os_2.usWinDescent = this.raf.readUnsignedShort();
        if (this.os_2.usWinDescent > 0) {
            this.os_2.usWinDescent = (short) (-this.os_2.usWinDescent);
        }
        this.os_2.ulCodePageRange1 = 0;
        this.os_2.ulCodePageRange2 = 0;
        if (version > 0) {
            this.os_2.ulCodePageRange1 = this.raf.readInt();
            this.os_2.ulCodePageRange2 = this.raf.readInt();
        }
        if (version > 1) {
            this.raf.skipBytes(2);
            this.os_2.sCapHeight = this.raf.readShort();
        } else {
            this.os_2.sCapHeight = (int) (0.7d * this.head.unitsPerEm);
        }
    }

    private void readPostTable() throws IOException {
        int[] table_location = this.tables.get("post");
        if (table_location != null) {
            this.raf.seek(table_location[0] + 4);
            short mantissa = this.raf.readShort();
            int fraction = this.raf.readUnsignedShort();
            this.post = new PostTable();
            this.post.italicAngle = (float) (mantissa + (fraction / 16384.0d));
            this.post.underlinePosition = this.raf.readShort();
            this.post.underlineThickness = this.raf.readShort();
            this.post.isFixedPitch = this.raf.readInt() != 0;
            return;
        }
        this.post = new PostTable();
        this.post.italicAngle = (float) (((-Math.atan2(this.hhea.caretSlopeRun, this.hhea.caretSlopeRise)) * 180.0d) / 3.141592653589793d);
    }

    private void readCmapTable() throws IOException {
        int[] table_location = this.tables.get("cmap");
        if (table_location == null) {
            if (this.fileName == null) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExist).setMessageParams("cmap");
            }
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.TableDoesNotExistsIn).setMessageParams("cmap", this.fileName);
        }
        this.raf.seek(table_location[0]);
        this.raf.skipBytes(2);
        int num_tables = this.raf.readUnsignedShort();
        int map10 = 0;
        int map31 = 0;
        int map30 = 0;
        int mapExt = 0;
        this.cmaps = new CmapTable();
        for (int k = 0; k < num_tables; k++) {
            int platId = this.raf.readUnsignedShort();
            int platSpecId = this.raf.readUnsignedShort();
            int offset = this.raf.readInt();
            if (platId == 3 && platSpecId == 0) {
                this.cmaps.fontSpecific = true;
                map30 = offset;
            } else if (platId == 3 && platSpecId == 1) {
                map31 = offset;
            } else if (platId == 3 && platSpecId == 10) {
                mapExt = offset;
            } else if (platId == 1 && platSpecId == 0) {
                map10 = offset;
            }
        }
        if (map10 > 0) {
            this.raf.seek(table_location[0] + map10);
            int format = this.raf.readUnsignedShort();
            switch (format) {
                case 0:
                    this.cmaps.cmap10 = readFormat0();
                    break;
                case 4:
                    this.cmaps.cmap10 = readFormat4(false);
                    break;
                case 6:
                    this.cmaps.cmap10 = readFormat6();
                    break;
            }
        }
        if (map31 > 0) {
            this.raf.seek(table_location[0] + map31);
            int format2 = this.raf.readUnsignedShort();
            if (format2 == 4) {
                this.cmaps.cmap31 = readFormat4(false);
            }
        }
        if (map30 > 0) {
            this.raf.seek(table_location[0] + map30);
            int format3 = this.raf.readUnsignedShort();
            if (format3 == 4) {
                this.cmaps.cmap10 = readFormat4(this.cmaps.fontSpecific);
            } else {
                this.cmaps.fontSpecific = false;
            }
        }
        if (mapExt > 0) {
            this.raf.seek(table_location[0] + mapExt);
            int format4 = this.raf.readUnsignedShort();
            switch (format4) {
                case 0:
                    this.cmaps.cmapExt = readFormat0();
                    return;
                case 4:
                    this.cmaps.cmapExt = readFormat4(false);
                    return;
                case 6:
                    this.cmaps.cmapExt = readFormat6();
                    return;
                case 12:
                    this.cmaps.cmapExt = readFormat12();
                    return;
                default:
                    return;
            }
        }
    }

    private String readStandardString(int length) throws IOException {
        return this.raf.readString(length, "Cp1252");
    }

    private String readUnicodeString(int length) throws IOException {
        StringBuilder buf = new StringBuilder();
        int length2 = length / 2;
        for (int k = 0; k < length2; k++) {
            buf.append(this.raf.readChar());
        }
        return buf.toString();
    }

    protected int getGlyphWidth(int glyph) {
        if (glyph >= this.glyphWidthsByIndex.length) {
            glyph = this.glyphWidthsByIndex.length - 1;
        }
        return this.glyphWidthsByIndex[glyph];
    }

    private Map<Integer, int[]> readFormat0() throws IOException {
        Map<Integer, int[]> h = new LinkedHashMap<>();
        this.raf.skipBytes(4);
        for (int k = 0; k < 256; k++) {
            int[] r = new int[2];
            r[0] = this.raf.readUnsignedByte();
            r[1] = getGlyphWidth(r[0]);
            h.put(Integer.valueOf(k), r);
        }
        return h;
    }

    private Map<Integer, int[]> readFormat4(boolean fontSpecific) throws IOException {
        int glyph;
        Map<Integer, int[]> h = new LinkedHashMap<>();
        int table_lenght = this.raf.readUnsignedShort();
        this.raf.skipBytes(2);
        int segCount = this.raf.readUnsignedShort() / 2;
        this.raf.skipBytes(6);
        int[] endCount = new int[segCount];
        for (int k = 0; k < segCount; k++) {
            endCount[k] = this.raf.readUnsignedShort();
        }
        this.raf.skipBytes(2);
        int[] startCount = new int[segCount];
        for (int k2 = 0; k2 < segCount; k2++) {
            startCount[k2] = this.raf.readUnsignedShort();
        }
        int[] idDelta = new int[segCount];
        for (int k3 = 0; k3 < segCount; k3++) {
            idDelta[k3] = this.raf.readUnsignedShort();
        }
        int[] idRO = new int[segCount];
        for (int k4 = 0; k4 < segCount; k4++) {
            idRO[k4] = this.raf.readUnsignedShort();
        }
        int[] glyphId = new int[((table_lenght / 2) - 8) - (segCount * 4)];
        for (int k5 = 0; k5 < glyphId.length; k5++) {
            glyphId[k5] = this.raf.readUnsignedShort();
        }
        for (int k6 = 0; k6 < segCount; k6++) {
            for (int j = startCount[k6]; j <= endCount[k6] && j != 65535; j++) {
                if (idRO[k6] == 0) {
                    glyph = (j + idDelta[k6]) & 65535;
                } else {
                    int idx = (((k6 + (idRO[k6] / 2)) - segCount) + j) - startCount[k6];
                    if (idx < glyphId.length) {
                        glyph = (glyphId[idx] + idDelta[k6]) & 65535;
                    }
                }
                int[] r = new int[2];
                r[0] = glyph;
                r[1] = getGlyphWidth(r[0]);
                if (fontSpecific && (j & 65280) == 61440) {
                    h.put(Integer.valueOf(j & 255), r);
                }
                h.put(Integer.valueOf(j), r);
            }
        }
        return h;
    }

    private Map<Integer, int[]> readFormat6() throws IOException {
        Map<Integer, int[]> h = new LinkedHashMap<>();
        this.raf.skipBytes(4);
        int start_code = this.raf.readUnsignedShort();
        int code_count = this.raf.readUnsignedShort();
        for (int k = 0; k < code_count; k++) {
            int[] r = new int[2];
            r[0] = this.raf.readUnsignedShort();
            r[1] = getGlyphWidth(r[0]);
            h.put(Integer.valueOf(k + start_code), r);
        }
        return h;
    }

    private Map<Integer, int[]> readFormat12() throws IOException {
        Map<Integer, int[]> h = new LinkedHashMap<>();
        this.raf.skipBytes(2);
        this.raf.readInt();
        this.raf.skipBytes(4);
        int nGroups = this.raf.readInt();
        for (int k = 0; k < nGroups; k++) {
            int startCharCode = this.raf.readInt();
            int endCharCode = this.raf.readInt();
            int startGlyphID = this.raf.readInt();
            for (int i = startCharCode; i <= endCharCode; i++) {
                int[] r = new int[2];
                r[0] = startGlyphID;
                r[1] = getGlyphWidth(r[0]);
                h.put(Integer.valueOf(i), r);
                startGlyphID++;
            }
        }
        return h;
    }
}
