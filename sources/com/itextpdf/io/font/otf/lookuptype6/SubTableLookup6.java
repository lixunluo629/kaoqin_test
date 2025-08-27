package com.itextpdf.io.font.otf.lookuptype6;

import com.itextpdf.io.font.otf.ContextualSubTable;
import com.itextpdf.io.font.otf.ContextualSubstRule;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.font.otf.OpenTableLookup;
import com.itextpdf.io.font.otf.OpenTypeFontTableReader;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/lookuptype6/SubTableLookup6.class */
public abstract class SubTableLookup6 extends ContextualSubTable {
    private static final long serialVersionUID = -7471613803606544198L;

    protected SubTableLookup6(OpenTypeFontTableReader openReader, int lookupFlag) {
        super(openReader, lookupFlag);
    }

    @Override // com.itextpdf.io.font.otf.ContextualSubTable
    public ContextualSubstRule getMatchingContextRule(GlyphLine line) {
        if (line.idx >= line.end) {
            return null;
        }
        Glyph g = line.get(line.idx);
        List<ContextualSubstRule> rules = getSetOfRulesForStartGlyph(g.getCode());
        for (ContextualSubstRule rule : rules) {
            int lastGlyphIndex = checkIfContextMatch(line, rule);
            if (lastGlyphIndex != -1 && checkIfLookaheadContextMatch(line, rule, lastGlyphIndex) && checkIfBacktrackContextMatch(line, rule)) {
                line.start = line.idx;
                line.end = lastGlyphIndex + 1;
                return rule;
            }
        }
        return null;
    }

    protected boolean checkIfLookaheadContextMatch(GlyphLine line, ContextualSubstRule rule, int startIdx) {
        OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
        gidx.line = line;
        gidx.idx = startIdx;
        int j = 0;
        while (j < rule.getLookaheadContextLength()) {
            gidx.nextGlyph(this.openReader, this.lookupFlag);
            if (gidx.glyph == null || !rule.isGlyphMatchesLookahead(gidx.glyph.getCode(), j)) {
                break;
            }
            j++;
        }
        return j == rule.getLookaheadContextLength();
    }

    protected boolean checkIfBacktrackContextMatch(GlyphLine line, ContextualSubstRule rule) {
        OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
        gidx.line = line;
        gidx.idx = line.idx;
        int j = 0;
        while (j < rule.getBacktrackContextLength()) {
            gidx.previousGlyph(this.openReader, this.lookupFlag);
            if (gidx.glyph == null || !rule.isGlyphMatchesBacktrack(gidx.glyph.getCode(), j)) {
                break;
            }
            j++;
        }
        return j == rule.getBacktrackContextLength();
    }
}
