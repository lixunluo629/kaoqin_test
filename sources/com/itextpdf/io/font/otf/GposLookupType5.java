package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType5.class */
public class GposLookupType5 extends OpenTableLookup {
    private static final long serialVersionUID = 6409145706785333023L;
    private final List<MarkToLigature> marksligatures;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType5$MarkToLigature.class */
    public static class MarkToLigature implements Serializable {
        private static final long serialVersionUID = 4249432630962669432L;
        public final Map<Integer, OtfMarkRecord> marks = new HashMap();
        public final Map<Integer, List<GposAnchor[]>> ligatures = new HashMap();
    }

    public GposLookupType5(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.marksligatures = new ArrayList();
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
        Iterator<MarkToLigature> it = this.marksligatures.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MarkToLigature mb = it.next();
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
                List<GposAnchor[]> gpas = mb.ligatures.get(Integer.valueOf(gi.glyph.getCode()));
                if (gpas != null) {
                    int markClass = omr.markClass;
                    int component = 0;
                    while (true) {
                        if (component >= gpas.size()) {
                            break;
                        }
                        if (gpas.get(component)[markClass] == null) {
                            component++;
                        } else {
                            GposAnchor baseAnchor = gpas.get(component)[markClass];
                            GposAnchor markAnchor = omr.anchor;
                            line.add(line.idx, new Glyph(line.get(line.idx), markAnchor.XCoordinate - baseAnchor.XCoordinate, markAnchor.YCoordinate - baseAnchor.YCoordinate, 0, 0, gi.idx - line.idx));
                            changed = true;
                            break;
                        }
                    }
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
        int ligatureCoverageLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int classCount = this.openReader.rf.readUnsignedShort();
        int markArrayLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        int ligatureArrayLocation = this.openReader.rf.readUnsignedShort() + subTableLocation;
        List<Integer> markCoverage = this.openReader.readCoverageFormat(markCoverageLocation);
        List<Integer> ligatureCoverage = this.openReader.readCoverageFormat(ligatureCoverageLocation);
        List<OtfMarkRecord> markRecords = OtfReadCommon.readMarkArray(this.openReader, markArrayLocation);
        MarkToLigature markToLigature = new MarkToLigature();
        for (int k = 0; k < markCoverage.size(); k++) {
            markToLigature.marks.put(markCoverage.get(k), markRecords.get(k));
        }
        List<List<GposAnchor[]>> ligatureArray = OtfReadCommon.readLigatureArray(this.openReader, classCount, ligatureArrayLocation);
        for (int k2 = 0; k2 < ligatureCoverage.size(); k2++) {
            markToLigature.ligatures.put(ligatureCoverage.get(k2), ligatureArray.get(k2));
        }
        this.marksligatures.add(markToLigature);
    }
}
