package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import java.io.Serializable;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/ContextualSubTable.class */
public abstract class ContextualSubTable implements Serializable {
    private static final long serialVersionUID = 1802216575331243298L;
    protected OpenTypeFontTableReader openReader;
    protected int lookupFlag;

    protected abstract List<ContextualSubstRule> getSetOfRulesForStartGlyph(int i);

    protected ContextualSubTable(OpenTypeFontTableReader openReader, int lookupFlag) {
        this.openReader = openReader;
        this.lookupFlag = lookupFlag;
    }

    public ContextualSubstRule getMatchingContextRule(GlyphLine line) {
        if (line.idx >= line.end) {
            return null;
        }
        Glyph g = line.get(line.idx);
        List<ContextualSubstRule> rules = getSetOfRulesForStartGlyph(g.getCode());
        for (ContextualSubstRule rule : rules) {
            int lastGlyphIndex = checkIfContextMatch(line, rule);
            if (lastGlyphIndex != -1) {
                line.start = line.idx;
                line.end = lastGlyphIndex + 1;
                return rule;
            }
        }
        return null;
    }

    protected int checkIfContextMatch(GlyphLine line, ContextualSubstRule rule) {
        OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
        gidx.line = line;
        gidx.idx = line.idx;
        int j = 1;
        while (j < rule.getContextLength()) {
            gidx.nextGlyph(this.openReader, this.lookupFlag);
            if (gidx.glyph == null || !rule.isGlyphMatchesInput(gidx.glyph.getCode(), j)) {
                break;
            }
            j++;
        }
        boolean isMatch = j == rule.getContextLength();
        if (isMatch) {
            return gidx.idx;
        }
        return -1;
    }
}
