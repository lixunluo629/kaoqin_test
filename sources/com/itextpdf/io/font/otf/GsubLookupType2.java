package com.itextpdf.io.font.otf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GsubLookupType2.class */
public class GsubLookupType2 extends OpenTableLookup {
    private static final long serialVersionUID = 48861238131801306L;
    private Map<Integer, int[]> substMap;

    public GsubLookupType2(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.substMap = new HashMap();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        int[] substSequence;
        if (line.idx >= line.end) {
            return false;
        }
        Glyph g = line.get(line.idx);
        boolean changed = false;
        if (!this.openReader.isSkip(g.getCode(), this.lookupFlag) && (substSequence = this.substMap.get(Integer.valueOf(g.getCode()))) != null && substSequence.length > 0) {
            line.substituteOneToMany(this.openReader, substSequence);
            changed = true;
        }
        line.idx++;
        return changed;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        int substFormat = this.openReader.rf.readUnsignedShort();
        if (substFormat == 1) {
            int coverage = this.openReader.rf.readUnsignedShort();
            int sequenceCount = this.openReader.rf.readUnsignedShort();
            int[] sequenceLocations = this.openReader.readUShortArray(sequenceCount, subTableLocation);
            List<Integer> coverageGlyphIds = this.openReader.readCoverageFormat(subTableLocation + coverage);
            for (int i = 0; i < sequenceCount; i++) {
                this.openReader.rf.seek(sequenceLocations[i]);
                int glyphCount = this.openReader.rf.readUnsignedShort();
                this.substMap.put(coverageGlyphIds.get(i), this.openReader.readUShortArray(glyphCount));
            }
            return;
        }
        throw new IllegalArgumentException("Bad substFormat: " + substFormat);
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean hasSubstitution(int index) {
        return this.substMap.containsKey(Integer.valueOf(index));
    }
}
