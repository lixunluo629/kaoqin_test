package com.itextpdf.io.font.otf.lookuptype5;

import com.itextpdf.io.font.otf.ContextualSubTable;
import com.itextpdf.io.font.otf.ContextualSubstRule;
import com.itextpdf.io.font.otf.OpenTypeFontTableReader;
import com.itextpdf.io.font.otf.SubstLookupRecord;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format3.class */
public class SubTableLookup5Format3 extends ContextualSubTable {
    private static final long serialVersionUID = -9142690964201749548L;
    ContextualSubstRule substitutionRule;

    public SubTableLookup5Format3(OpenTypeFontTableReader openReader, int lookupFlag, SubstRuleFormat3 rule) {
        super(openReader, lookupFlag);
        this.substitutionRule = rule;
    }

    @Override // com.itextpdf.io.font.otf.ContextualSubTable
    protected List<ContextualSubstRule> getSetOfRulesForStartGlyph(int startId) {
        SubstRuleFormat3 ruleFormat3 = (SubstRuleFormat3) this.substitutionRule;
        if (ruleFormat3.coverages.get(0).contains(Integer.valueOf(startId)) && !this.openReader.isSkip(startId, this.lookupFlag)) {
            return Collections.singletonList(this.substitutionRule);
        }
        return Collections.emptyList();
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype5/SubTableLookup5Format3$SubstRuleFormat3.class */
    public static class SubstRuleFormat3 extends ContextualSubstRule {
        private static final long serialVersionUID = -1840126702536353850L;
        List<Set<Integer>> coverages;
        SubstLookupRecord[] substLookupRecords;

        public SubstRuleFormat3(List<Set<Integer>> coverages, SubstLookupRecord[] substLookupRecords) {
            this.coverages = coverages;
            this.substLookupRecords = substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public int getContextLength() {
            return this.coverages.size();
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public SubstLookupRecord[] getSubstLookupRecords() {
            return this.substLookupRecords;
        }

        @Override // com.itextpdf.io.font.otf.ContextualSubstRule
        public boolean isGlyphMatchesInput(int glyphId, int atIdx) {
            return this.coverages.get(atIdx).contains(Integer.valueOf(glyphId));
        }
    }
}
