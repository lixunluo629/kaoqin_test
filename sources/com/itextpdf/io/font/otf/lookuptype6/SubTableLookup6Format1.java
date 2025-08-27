package com.itextpdf.io.font.otf.lookuptype6;

import com.itextpdf.io.font.otf.ContextualSubstRule;
import com.itextpdf.io.font.otf.OpenTypeFontTableReader;
import com.itextpdf.io.font.otf.SubstLookupRecord;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype6/SubTableLookup6Format1.class */
public class SubTableLookup6Format1 extends SubTableLookup6 {
    private static final long serialVersionUID = 4252117327329368679L;
    private Map<Integer, List<ContextualSubstRule>> substMap;

    public SubTableLookup6Format1(OpenTypeFontTableReader openReader, int lookupFlag, Map<Integer, List<ContextualSubstRule>> substMap) {
        super(openReader, lookupFlag);
        this.substMap = substMap;
    }

    @Override // com.itextpdf.io.font.otf.ContextualSubTable
    protected List<ContextualSubstRule> getSetOfRulesForStartGlyph(int startGlyphId) {
        if (this.substMap.containsKey(Integer.valueOf(startGlyphId)) && !this.openReader.isSkip(startGlyphId, this.lookupFlag)) {
            return this.substMap.get(Integer.valueOf(startGlyphId));
        }
        return Collections.emptyList();
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype6/SubTableLookup6Format1$SubstRuleFormat1.class */
    public static class SubstRuleFormat1 extends ContextualSubstRule {
        private static final long serialVersionUID = 6962160437871819250L;
        private int[] inputGlyphIds;
        private int[] backtrackGlyphIds;
        private int[] lookAheadGlyphIds;
        private SubstLookupRecord[] substLookupRecords;

        public SubstRuleFormat1(int[] backtrackGlyphIds, int[] inputGlyphIds, int[] lookAheadGlyphIds, SubstLookupRecord[] substLookupRecords) {
            this.backtrackGlyphIds = backtrackGlyphIds;
            this.inputGlyphIds = inputGlyphIds;
            this.lookAheadGlyphIds = lookAheadGlyphIds;
            this.substLookupRecords = substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getContextLength() {
            return this.inputGlyphIds.length + 1;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getLookaheadContextLength() {
            return this.lookAheadGlyphIds.length;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getBacktrackContextLength() {
            return this.backtrackGlyphIds.length;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public SubstLookupRecord[] getSubstLookupRecords() {
            return this.substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesInput(int glyphId, int atIdx) {
            return glyphId == this.inputGlyphIds[atIdx - 1];
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesLookahead(int glyphId, int atIdx) {
            return glyphId == this.lookAheadGlyphIds[atIdx];
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesBacktrack(int glyphId, int atIdx) {
            return glyphId == this.backtrackGlyphIds[atIdx];
        }
    }
}
