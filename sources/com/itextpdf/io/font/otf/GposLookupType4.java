package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType4.class */
public class GposLookupType4 extends OpenTableLookup {
    private static final long serialVersionUID = 8820454200196341970L;
    private final List<MarkToBase> marksbases;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType4$MarkToBase.class */
    public static class MarkToBase implements Serializable {
        private static final long serialVersionUID = 1518537209432079627L;
        public final Map<Integer, OtfMarkRecord> marks = new HashMap();
        public final Map<Integer, GposAnchor[]> bases = new HashMap();
    }

    public GposLookupType4(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.marksbases = new ArrayList();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        if (line.idx >= line.end) {
            return false;
        }
        if (this.openReader.isSkip(line.get(line.idx).getCode(), this.lookupFlag)) {
            line.idx++;
            return false;
        }
        boolean changed = false;
        OpenTableLookup.GlyphIndexer gi = null;
        Iterator<MarkToBase> it = this.marksbases.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MarkToBase mb = it.next();
            OtfMarkRecord omr = mb.marks.get(Integer.valueOf(line.get(line.idx).getCode()));
            if (omr != null) {
                if (gi == null) {
                    gi = new OpenTableLookup.GlyphIndexer();
                    gi.idx = line.idx;
                    gi.line = line;
                    do {
                        gi.previousGlyph(this.openReader, this.lookupFlag);
                        if (gi.glyph == null) {
                            break;
                        }
                    } while (mb.marks.containsKey(Integer.valueOf(gi.glyph.getCode())));
                    if (gi.glyph != null) {
                        break;
                        break;
                    }
                    break;
                }
                GposAnchor[] gpas = mb.bases.get(Integer.valueOf(gi.glyph.getCode()));
                if (gpas != null) {
                    int markClass = omr.markClass;
                    int xPlacement = 0;
                    int yPlacement = 0;
                    GposAnchor baseAnchor = gpas[markClass];
                    if (baseAnchor != null) {
                        xPlacement = baseAnchor.XCoordinate;
                        yPlacement = baseAnchor.YCoordinate;
                    }
                    GposAnchor markAnchor = omr.anchor;
                    if (markAnchor != null) {
                        xPlacement -= markAnchor.XCoordinate;
                        yPlacement -= markAnchor.YCoordinate;
                    }
                    line.set(line.idx, new Glyph(line.get(line.idx), xPlacement, yPlacement, 0, 0, gi.idx - line.idx));
                    changed = true;
                }
            }
        }
        line.idx++;
        return changed;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        this.openReader.rf.readUnsignedShort();
        int markCoverageLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int baseCoverageLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int classCount = this.openReader.rf.readUnsignedShort();
        int markArrayLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int baseArrayLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        List<Integer> markCoverage = this.openReader.readCoverageFormat(markCoverageLocation);
        List<Integer> baseCoverage = this.openReader.readCoverageFormat(baseCoverageLocation);
        List<OtfMarkRecord> markRecords = OtfReadCommon.readMarkArray(this.openReader, markArrayLocation);
        MarkToBase markToBase = new MarkToBase();
        for (int k = 0; k < markCoverage.size(); k++) {
            markToBase.marks.put(markCoverage.get(k), markRecords.get(k));
        }
        List<GposAnchor[]> baseArray = OtfReadCommon.readBaseArray(this.openReader, classCount, baseArrayLocation);
        for (int k2 = 0; k2 < baseCoverage.size(); k2++) {
            markToBase.bases.put(baseCoverage.get(k2), baseArray.get(k2));
        }
        this.marksbases.add(markToBase);
    }
}
