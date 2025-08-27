package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import com.itextpdf.io.font.otf.lookuptype5.SubTableLookup5Format1;
import com.itextpdf.io.font.otf.lookuptype5.SubTableLookup5Format2;
import com.itextpdf.io.font.otf.lookuptype5.SubTableLookup5Format3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GsubLookupType5.class */
public class GsubLookupType5 extends OpenTableLookup {
    private static final long serialVersionUID = 1499367592878919320L;
    protected List<ContextualSubTable> subTables;

    protected GsubLookupType5(OpenTypeFontTableReader openReader, int lookupFlag, int[] subTableLocations) throws IOException {
        super(openReader, lookupFlag, subTableLocations);
        this.subTables = new ArrayList();
        readSubTables();
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    public boolean transformOne(GlyphLine line) {
        boolean changed = false;
        int oldLineStart = line.start;
        int oldLineEnd = line.end;
        int initialLineIndex = line.idx;
        for (ContextualSubTable subTable : this.subTables) {
            ContextualSubstRule contextRule = subTable.getMatchingContextRule(line);
            if (contextRule != null) {
                int lineEndBeforeSubstitutions = line.end;
                SubstLookupRecord[] substLookupRecords = contextRule.getSubstLookupRecords();
                OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
                gidx.line = line;
                for (SubstLookupRecord substRecord : substLookupRecords) {
                    gidx.idx = initialLineIndex;
                    for (int i = 0; i < substRecord.sequenceIndex; i++) {
                        gidx.nextGlyph(this.openReader, this.lookupFlag);
                    }
                    line.idx = gidx.idx;
                    OpenTableLookup lookupTable = this.openReader.getLookupTable(substRecord.lookupListIndex);
                    changed = lookupTable.transformOne(line) || changed;
                }
                line.idx = line.end;
                line.start = oldLineStart;
                int lenDelta = lineEndBeforeSubstitutions - line.end;
                line.end = oldLineEnd - lenDelta;
                return changed;
            }
        }
        line.idx++;
        return false;
    }

    @Override // com.itextpdf.io.font.otf.OpenTableLookup
    protected void readSubTable(int subTableLocation) throws IOException {
        this.openReader.rf.seek(subTableLocation);
        int substFormat = this.openReader.rf.readShort();
        if (substFormat == 1) {
            readSubTableFormat1(subTableLocation);
        } else if (substFormat == 2) {
            readSubTableFormat2(subTableLocation);
        } else {
            if (substFormat == 3) {
                readSubTableFormat3(subTableLocation);
                return;
            }
            throw new IllegalArgumentException("Bad substFormat: " + substFormat);
        }
    }

    protected void readSubTableFormat1(int subTableLocation) throws IOException {
        Map<Integer, List<ContextualSubstRule>> substMap = new HashMap<>();
        int coverageOffset = this.openReader.rf.readUnsignedShort();
        int subRuleSetCount = this.openReader.rf.readUnsignedShort();
        int[] subRuleSetOffsets = this.openReader.readUShortArray(subRuleSetCount, subTableLocation);
        List<Integer> coverageGlyphIds = this.openReader.readCoverageFormat(subTableLocation + coverageOffset);
        for (int i = 0; i < subRuleSetCount; i++) {
            this.openReader.rf.seek(subRuleSetOffsets[i]);
            int subRuleCount = this.openReader.rf.readUnsignedShort();
            int[] subRuleOffsets = this.openReader.readUShortArray(subRuleCount, subRuleSetOffsets[i]);
            List<ContextualSubstRule> subRuleSet = new ArrayList<>(subRuleCount);
            for (int j = 0; j < subRuleCount; j++) {
                this.openReader.rf.seek(subRuleOffsets[j]);
                int glyphCount = this.openReader.rf.readUnsignedShort();
                int substCount = this.openReader.rf.readUnsignedShort();
                int[] inputGlyphIds = this.openReader.readUShortArray(glyphCount - 1);
                SubstLookupRecord[] substLookupRecords = this.openReader.readSubstLookupRecords(substCount);
                subRuleSet.add(new SubTableLookup5Format1.SubstRuleFormat1(inputGlyphIds, substLookupRecords));
            }
            substMap.put(coverageGlyphIds.get(i), subRuleSet);
        }
        this.subTables.add(new SubTableLookup5Format1(this.openReader, this.lookupFlag, substMap));
    }

    protected void readSubTableFormat2(int subTableLocation) throws IOException {
        int coverageOffset = this.openReader.rf.readUnsignedShort();
        int classDefOffset = this.openReader.rf.readUnsignedShort();
        int subClassSetCount = this.openReader.rf.readUnsignedShort();
        int[] subClassSetOffsets = this.openReader.readUShortArray(subClassSetCount, subTableLocation);
        Set<Integer> coverageGlyphIds = new HashSet<>(this.openReader.readCoverageFormat(subTableLocation + coverageOffset));
        OtfClass classDefinition = this.openReader.readClassDefinition(subTableLocation + classDefOffset);
        SubTableLookup5Format2 t = new SubTableLookup5Format2(this.openReader, this.lookupFlag, coverageGlyphIds, classDefinition);
        List<List<ContextualSubstRule>> subClassSets = new ArrayList<>(subClassSetCount);
        for (int i = 0; i < subClassSetCount; i++) {
            List<ContextualSubstRule> subClassSet = null;
            if (subClassSetOffsets[i] != 0) {
                this.openReader.rf.seek(subClassSetOffsets[i]);
                int subClassRuleCount = this.openReader.rf.readUnsignedShort();
                int[] subClassRuleOffsets = this.openReader.readUShortArray(subClassRuleCount, subClassSetOffsets[i]);
                subClassSet = new ArrayList<>(subClassRuleCount);
                for (int j = 0; j < subClassRuleCount; j++) {
                    this.openReader.rf.seek(subClassRuleOffsets[j]);
                    int glyphCount = this.openReader.rf.readUnsignedShort();
                    int substCount = this.openReader.rf.readUnsignedShort();
                    int[] inputClassIds = this.openReader.readUShortArray(glyphCount - 1);
                    SubstLookupRecord[] substLookupRecords = this.openReader.readSubstLookupRecords(substCount);
                    ContextualSubstRule rule = new SubTableLookup5Format2.SubstRuleFormat2(t, inputClassIds, substLookupRecords);
                    subClassSet.add(rule);
                }
            }
            subClassSets.add(subClassSet);
        }
        t.setSubClassSets(subClassSets);
        this.subTables.add(t);
    }

    protected void readSubTableFormat3(int subTableLocation) throws IOException {
        int glyphCount = this.openReader.rf.readUnsignedShort();
        int substCount = this.openReader.rf.readUnsignedShort();
        int[] coverageOffsets = this.openReader.readUShortArray(glyphCount, subTableLocation);
        SubstLookupRecord[] substLookupRecords = this.openReader.readSubstLookupRecords(substCount);
        List<Set<Integer>> coverages = new ArrayList<>(glyphCount);
        this.openReader.readCoverages(coverageOffsets, coverages);
        SubTableLookup5Format3.SubstRuleFormat3 rule = new SubTableLookup5Format3.SubstRuleFormat3(coverages, substLookupRecords);
        this.subTables.add(new SubTableLookup5Format3(this.openReader, this.lookupFlag, rule));
    }
}
