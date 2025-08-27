package com.itextpdf.io.font.otf.lookuptype5;

import com.itextpdf.io.font.otf.ContextualSubTable;
import com.itextpdf.io.font.otf.ContextualSubstRule;
import com.itextpdf.io.font.otf.OpenTypeFontTableReader;
import com.itextpdf.io.font.otf.SubstLookupRecord;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format1.class */
public class SubTableLookup5Format1 extends ContextualSubTable {
    private static final long serialVersionUID = -6061489236592337747L;
    private Map<Integer, List<ContextualSubstRule>> substMap;

    public SubTableLookup5Format1(OpenTypeFontTableReader openReader, int lookupFlag, Map<Integer, List<ContextualSubstRule>> substMap) {
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

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format1$SubstRuleFormat1.class */
    public static class SubstRuleFormat1 extends ContextualSubstRule {
        private static final long serialVersionUID = -540799242670887211L;
        private int[] inputGlyphIds;
        private SubstLookupRecord[] substLookupRecords;

        public SubstRuleFormat1(int[] inputGlyphIds, SubstLookupRecord[] substLookupRecords) {
            this.inputGlyphIds = inputGlyphIds;
            this.substLookupRecords = substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getContextLength() {
            return this.inputGlyphIds.length + 1;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public SubstLookupRecord[] getSubstLookupRecords() {
            return this.substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesInput(int glyphId, int atIdx) {
            return glyphId == this.inputGlyphIds[atIdx - 1];
        }
    }
}
