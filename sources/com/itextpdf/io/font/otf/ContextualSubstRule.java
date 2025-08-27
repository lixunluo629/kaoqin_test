package com.itextpdf.io.font.otf;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/ContextualSubstRule.class */
public abstract class ContextualSubstRule implements Serializable {
    private static final long serialVersionUID = -8640866820690910047L;

    public abstract int getContextLength();

    public abstract SubstLookupRecord[] getSubstLookupRecords();

    public abstract boolean isGlyphMatchesInput(int i, int i2);

    public int getLookaheadContextLength() {
        return 0;
    }

    public int getBacktrackContextLength() {
        return 0;
    }

    public boolean isGlyphMatchesLookahead(int glyphId, int atIdx) {
        return false;
    }

    public boolean isGlyphMatchesBacktrack(int glyphId, int atIdx) {
        return false;
    }
}
