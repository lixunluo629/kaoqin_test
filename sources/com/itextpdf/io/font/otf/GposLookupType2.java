package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType2.class */
public class GposLookupType2 extends OpenTableLookup {
    private static final long serialVersionUID = 4781829862270887603L;
    private List<OpenTableLookup> listRules;

    public GposLookupType2(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.listRules = new ArrayList();
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
        for (OpenTableLookup lookup : this.listRules) {
            if (lookup.transformOne(line)) {
                return true;
            }
        }
        line.idx++;
        return false;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        int gposFormat = this.openReader.rf.readShort();
        switch (gposFormat) {
            case 1:
                PairPosAdjustmentFormat1 format1 = new PairPosAdjustmentFormat1(this.openReader, this.lookupFlag, subTableLocation);
                this.listRules.add(format1);
                break;
            case 2:
                PairPosAdjustmentFormat2 format2 = new PairPosAdjustmentFormat2(this.openReader, this.lookupFlag, subTableLocation);
                this.listRules.add(format2);
                break;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType2$PairPosAdjustmentFormat1.class */
    private static class PairPosAdjustmentFormat1 extends OpenTableLookup {
        private static final long serialVersionUID = -5556528810086852702L;
        private Map<Integer, Map<Integer, PairValueFormat>> gposMap;

        public PairPosAdjustmentFormat1(OpenTypeFontTableReader openReader, int lookupFlag, int subtableLocation) throws IOException {
            super(openReader, lookupFlag, null);
            this.gposMap = new HashMap();
            readFormat(subtableLocation);
        }

        @Override // com.itextpdf.io.font.otf.OpenTableLookup
        public boolean transformOne(GlyphLine line) {
            PairValueFormat pv;
            if (line.idx >= line.end || line.idx < line.start) {
                return false;
            }
            boolean changed = false;
            Glyph g1 = line.get(line.idx);
            Map<Integer, PairValueFormat> m = this.gposMap.get(Integer.valueOf(g1.getCode()));
            if (m != null) {
                OpenTableLookup.GlyphIndexer gi = new OpenTableLookup.GlyphIndexer();
                gi.line = line;
                gi.idx = line.idx;
                gi.nextGlyph(this.openReader, this.lookupFlag);
                if (gi.glyph != null && (pv = m.get(Integer.valueOf(gi.glyph.getCode()))) != null) {
                    Glyph g2 = gi.glyph;
                    line.set(line.idx, new Glyph(g1, 0, 0, pv.first.XAdvance, pv.first.YAdvance, 0));
                    line.set(gi.idx, new Glyph(g2, 0, 0, pv.second.XAdvance, pv.second.YAdvance, 0));
                    line.idx = gi.idx;
                    changed = true;
                }
            }
            return changed;
        }

        protected void readFormat(int subTableLocation) throws IOException {
            int coverage = this.openReader.rf.readUnsignedShort() + subTableLocation;
            int valueFormat1 = this.openReader.rf.readUnsignedShort();
            int valueFormat2 = this.openReader.rf.readUnsignedShort();
            int pairSetCount = this.openReader.rf.readUnsignedShort();
            int[] locationRule = this.openReader.readUShortArray(pairSetCount, subTableLocation);
            List<Integer> coverageList = this.openReader.readCoverageFormat(coverage);
            for (int k = 0; k < pairSetCount; k++) {
                this.openReader.rf.seek(locationRule[k]);
                Map<Integer, PairValueFormat> pairs = new HashMap<>();
                this.gposMap.put(coverageList.get(k), pairs);
                int pairValueCount = this.openReader.rf.readUnsignedShort();
                for (int j = 0; j < pairValueCount; j++) {
                    int glyph2 = this.openReader.rf.readUnsignedShort();
                    PairValueFormat pair = new PairValueFormat();
                    pair.first = OtfReadCommon.readGposValueRecord(this.openReader, valueFormat1);
                    pair.second = OtfReadCommon.readGposValueRecord(this.openReader, valueFormat2);
                    pairs.put(Integer.valueOf(glyph2), pair);
                }
            }
        }

        @Override // com.itextpdf.io.font.otf.OpenTableLookup
        protected void readSubTable(int subTableLocation) throws IOException {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType2$PairPosAdjustmentFormat2.class */
    private static class PairPosAdjustmentFormat2 extends OpenTableLookup {
        private static final long serialVersionUID = 3056620748845862393L;
        private OtfClass classDef1;
        private OtfClass classDef2;
        private HashSet<Integer> coverageSet;
        private Map<Integer, PairValueFormat[]> posSubs;

        public PairPosAdjustmentFormat2(OpenTypeFontTableReader openReader, int lookupFlag, int subtableLocation) throws IOException {
            super(openReader, lookupFlag, null);
            this.posSubs = new HashMap();
            readFormat(subtableLocation);
        }

        @Override // com.itextpdf.io.font.otf.OpenTableLookup
        public boolean transformOne(GlyphLine line) {
            if (line.idx >= line.end || line.idx < line.start) {
                return false;
            }
            Glyph g1 = line.get(line.idx);
            if (!this.coverageSet.contains(Integer.valueOf(g1.getCode()))) {
                return false;
            }
            int c1 = this.classDef1.getOtfClass(g1.getCode());
            PairValueFormat[] pvs = this.posSubs.get(Integer.valueOf(c1));
            if (pvs == null) {
                return false;
            }
            OpenTableLookup.GlyphIndexer gi = new OpenTableLookup.GlyphIndexer();
            gi.line = line;
            gi.idx = line.idx;
            gi.nextGlyph(this.openReader, this.lookupFlag);
            if (gi.glyph == null) {
                return false;
            }
            Glyph g2 = gi.glyph;
            int c2 = this.classDef2.getOtfClass(g2.getCode());
            if (c2 >= pvs.length) {
                return false;
            }
            PairValueFormat pv = pvs[c2];
            line.set(line.idx, new Glyph(g1, 0, 0, pv.first.XAdvance, pv.first.YAdvance, 0));
            line.set(gi.idx, new Glyph(g2, 0, 0, pv.second.XAdvance, pv.second.YAdvance, 0));
            line.idx = gi.idx;
            return true;
        }

        protected void readFormat(int subTableLocation) throws IOException {
            int coverage = this.openReader.rf.readUnsignedShort() + subTableLocation;
            int valueFormat1 = this.openReader.rf.readUnsignedShort();
            int valueFormat2 = this.openReader.rf.readUnsignedShort();
            int locationClass1 = this.openReader.rf.readUnsignedShort() + subTableLocation;
            int locationClass2 = this.openReader.rf.readUnsignedShort() + subTableLocation;
            int class1Count = this.openReader.rf.readUnsignedShort();
            int class2Count = this.openReader.rf.readUnsignedShort();
            for (int k = 0; k < class1Count; k++) {
                PairValueFormat[] pairs = new PairValueFormat[class2Count];
                this.posSubs.put(Integer.valueOf(k), pairs);
                for (int j = 0; j < class2Count; j++) {
                    PairValueFormat pair = new PairValueFormat();
                    pair.first = OtfReadCommon.readGposValueRecord(this.openReader, valueFormat1);
                    pair.second = OtfReadCommon.readGposValueRecord(this.openReader, valueFormat2);
                    pairs[j] = pair;
                }
            }
            this.coverageSet = new HashSet<>(this.openReader.readCoverageFormat(coverage));
            this.classDef1 = this.openReader.readClassDefinition(locationClass1);
            this.classDef2 = this.openReader.readClassDefinition(locationClass2);
        }

        @Override // com.itextpdf.io.font.otf.OpenTableLookup
        protected void readSubTable(int subTableLocation) throws IOException {
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposLookupType2$PairValueFormat.class */
    private static class PairValueFormat implements Serializable {
        private static final long serialVersionUID = -6442882035589529495L;
        public GposValueRecord first;
        public GposValueRecord second;

        private PairValueFormat() {
        }
    }
}
