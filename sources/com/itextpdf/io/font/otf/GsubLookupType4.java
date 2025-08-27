package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GsubLookupType4.class */
public class GsubLookupType4 extends OpenTableLookup {
    private static final long serialVersionUID = -8106254947137506056L;
    private Map<Integer, List<int[]>> ligatures;

    public GsubLookupType4(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.ligatures = new HashMap();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        if (line.idx >= line.end) {
            return false;
        }
        boolean changed = false;
        Glyph g = line.get(line.idx);
        boolean match = false;
        if (this.ligatures.containsKey(Integer.valueOf(g.getCode())) && !this.openReader.isSkip(g.getCode(), this.lookupFlag)) {
            OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
            gidx.line = line;
            List<int[]> ligs = this.ligatures.get(Integer.valueOf(g.getCode()));
            Iterator<int[]> it = ligs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int[] lig = it.next();
                match = true;
                gidx.idx = line.idx;
                for (int j = 1; j < lig.length; j++) {
                    gidx.nextGlyph(this.openReader, this.lookupFlag);
                    if (gidx.glyph == null || gidx.glyph.getCode() != lig[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    line.substituteManyToOne(this.openReader, this.lookupFlag, lig.length - 1, lig[0]);
                    break;
                }
            }
        }
        if (match) {
            changed = true;
        }
        line.idx++;
        return changed;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        this.openReader.rf.readShort();
        int coverage = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int ligSetCount = this.openReader.rf.readUnsignedShort();
        int[] ligatureSet = new int[ligSetCount];
        for (int k = 0; k < ligSetCount; k++) {
            ligatureSet[k] = this.openReader.rf.readUnsignedShort() + subTableLocation;
        }
        List<Integer> coverageGlyphIds = this.openReader.readCoverageFormat(coverage);
        for (int k2 = 0; k2 < ligSetCount; k2++) {
            this.openReader.rf.seek(ligatureSet[k2]);
            int ligatureCount = this.openReader.rf.readUnsignedShort();
            int[] ligature = new int[ligatureCount];
            for (int j = 0; j < ligatureCount; j++) {
                ligature[j] = this.openReader.rf.readUnsignedShort() + ligatureSet[k2];
            }
            List<int[]> components = new ArrayList<>(ligatureCount);
            for (int j2 = 0; j2 < ligatureCount; j2++) {
                this.openReader.rf.seek(ligature[j2]);
                int ligGlyph = this.openReader.rf.readUnsignedShort();
                int compCount = this.openReader.rf.readUnsignedShort();
                int[] component = new int[compCount];
                component[0] = ligGlyph;
                for (int i = 1; i < compCount; i++) {
                    component[i] = this.openReader.rf.readUnsignedShort();
                }
                components.add(component);
            }
            this.ligatures.put(coverageGlyphIds.get(k2), components);
        }
    }
}
