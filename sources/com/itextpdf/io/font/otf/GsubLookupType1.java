package com.itextpdf.io.font.otf;

import com.itextpdf.io.util.IntHashtable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GsubLookupType1.class */
public class GsubLookupType1 extends OpenTableLookup {
    private static final long serialVersionUID = 1047931810962199937L;
    private IntHashtable substMap;

    public GsubLookupType1(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.substMap = new IntHashtable();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        int substCode;
        if (line.idx >= line.end) {
            return false;
        }
        Glyph g = line.get(line.idx);
        boolean changed = false;
        if (!this.openReader.isSkip(g.getCode(), this.lookupFlag) && (substCode = this.substMap.get(g.getCode())) != 0 && substCode != g.getCode()) {
            line.substituteOneToOne(this.openReader, substCode);
            changed = true;
        }
        line.idx++;
        return changed;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        int substFormat = this.openReader.rf.readShort();
        if (substFormat == 1) {
            int coverage = this.openReader.rf.readUnsignedShort();
            int deltaGlyphID = this.openReader.rf.readShort();
            List<Integer> coverageGlyphIds = this.openReader.readCoverageFormat(subTableLocation + coverage);
            Iterator<Integer> it = coverageGlyphIds.iterator();
            while (it.hasNext()) {
                int coverageGlyphId = it.next().intValue();
                int substituteGlyphId = coverageGlyphId + deltaGlyphID;
                this.substMap.put(coverageGlyphId, substituteGlyphId);
            }
            return;
        }
        if (substFormat == 2) {
            int coverage2 = this.openReader.rf.readUnsignedShort();
            int glyphCount = this.openReader.rf.readUnsignedShort();
            int[] substitute = new int[glyphCount];
            for (int k = 0; k < glyphCount; k++) {
                substitute[k] = this.openReader.rf.readUnsignedShort();
            }
            List<Integer> coverageGlyphIds2 = this.openReader.readCoverageFormat(subTableLocation + coverage2);
            for (int k2 = 0; k2 < glyphCount; k2++) {
                this.substMap.put(coverageGlyphIds2.get(k2).intValue(), substitute[k2]);
            }
            return;
        }
        throw new IllegalArgumentException("Bad substFormat: " + substFormat);
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean hasSubstitution(int index) {
        return this.substMap.containsKey(index);
    }
}
