package com.itextpdf.io.font.otf.lookuptype5;

import com.itextpdf.io.font.otf.ContextualSubTable;
import com.itextpdf.io.font.otf.ContextualSubstRule;
import com.itextpdf.io.font.otf.OpenTypeFontTableReader;
import com.itextpdf.io.font.otf.OtfClass;
import com.itextpdf.io.font.otf.SubstLookupRecord;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format2.class */
public class SubTableLookup5Format2 extends ContextualSubTable {
    private static final long serialVersionUID = -2184080481143798249L;
    private Set<Integer> substCoverageGlyphIds;
    private List<List<ContextualSubstRule>> subClassSets;
    private OtfClass classDefinition;

    public SubTableLookup5Format2(OpenTypeFontTableReader openReader, int lookupFlag, Set<Integer> substCoverageGlyphIds, OtfClass classDefinition) {
        super(openReader, lookupFlag);
        this.substCoverageGlyphIds = substCoverageGlyphIds;
        this.classDefinition = classDefinition;
    }

    public void setSubClassSets(List<List<ContextualSubstRule>> subClassSets) {
        this.subClassSets = subClassSets;
    }

    @Override // com.itextpdf.io.font.otf.ContextualSubTable
    protected List<ContextualSubstRule> getSetOfRulesForStartGlyph(int startId) {
        if (this.substCoverageGlyphIds.contains(Integer.valueOf(startId)) && !this.openReader.isSkip(startId, this.lookupFlag)) {
            int gClass = this.classDefinition.getOtfClass(startId);
            return this.subClassSets.get(gClass);
        }
        return Collections.emptyList();
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format2$SubstRuleFormat2.class */
    public static class SubstRuleFormat2 extends ContextualSubstRule {
        private static final long serialVersionUID = 652574134066355802L;
        private int[] inputClassIds;
        private SubstLookupRecord[] substLookupRecords;
        private OtfClass classDefinition;

        public SubstRuleFormat2(SubTableLookup5Format2 subTable, int[] inputClassIds, SubstLookupRecord[] substLookupRecords) {
            this.inputClassIds = inputClassIds;
            this.substLookupRecords = substLookupRecords;
            this.classDefinition = subTable.classDefinition;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getContextLength() {
            return this.inputClassIds.length + 1;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public SubstLookupRecord[] getSubstLookupRecords() {
            return this.substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesInput(int glyphId, int atIdx) {
            return this.classDefinition.getOtfClass(glyphId) == this.inputClassIds[atIdx - 1];
        }
    }
}
