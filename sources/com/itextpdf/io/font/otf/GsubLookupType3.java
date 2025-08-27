package com.itextpdf.io.font.otf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GsubLookupType3.class */
public class GsubLookupType3 extends OpenTableLookup {
    private static final long serialVersionUID = -5408042853790920298L;
    private Map<Integer, int[]> substMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GsubLookupType3.class.desiredAssertionStatus();
    }

    public GsubLookupType3(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.substMap = new HashMap();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        int[] substCode;
        if (line.idx >= line.end) {
            return false;
        }
        Glyph g = line.get(line.idx);
        boolean changed = false;
        if (!this.openReader.isSkip(g.getCode(), this.lookupFlag) && (substCode = this.substMap.get(Integer.valueOf(g.getCode()))) != null && substCode[0] != g.getCode()) {
            line.substituteOneToOne(this.openReader, substCode[0]);
            changed = true;
        }
        line.idx++;
        return changed;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        int substFormat = this.openReader.rf.readShort();
        if (!$assertionsDisabled && substFormat != 1) {
            throw new AssertionError();
        }
        int coverage = this.openReader.rf.readUnsignedShort();
        int alternateSetCount = this.openReader.rf.readUnsignedShort();
        int[] iArr = new int[alternateSetCount];
        int[] alternateLocations = this.openReader.readUShortArray(alternateSetCount, subTableLocation);
        for (int k = 0; k < alternateSetCount; k++) {
            this.openReader.rf.seek(alternateLocations[k]);
            int glyphCount = this.openReader.rf.readUnsignedShort();
            iArr[k] = this.openReader.readUShortArray(glyphCount);
        }
        List<Integer> coverageGlyphIds = this.openReader.readCoverageFormat(subTableLocation + coverage);
        for (int k2 = 0; k2 < alternateSetCount; k2++) {
            this.substMap.put(coverageGlyphIds.get(k2), iArr[k2]);
        }
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean hasSubstitution(int index) {
        return this.substMap.containsKey(Integer.valueOf(index));
    }
}
