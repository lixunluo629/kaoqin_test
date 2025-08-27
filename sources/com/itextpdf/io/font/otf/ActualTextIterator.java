package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.TextUtil;
import java.util.Iterator;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/ActualTextIterator.class */
public class ActualTextIterator implements Iterator<GlyphLine.GlyphLinePart> {
    private GlyphLine glyphLine;
    private int pos;

    public ActualTextIterator(GlyphLine glyphLine) {
        this.glyphLine = glyphLine;
        this.pos = glyphLine.start;
    }

    public ActualTextIterator(GlyphLine glyphLine, int start, int end) {
        this(new GlyphLine(glyphLine.glyphs, glyphLine.actualText, start, end));
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.pos < this.glyphLine.end;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public GlyphLine.GlyphLinePart next() {
        GlyphLine.GlyphLinePart nextResult;
        if (this.glyphLine.actualText == null) {
            GlyphLine.GlyphLinePart result = new GlyphLine.GlyphLinePart(this.pos, this.glyphLine.end, null);
            this.pos = this.glyphLine.end;
            return result;
        }
        GlyphLine.GlyphLinePart currentResult = nextGlyphLinePart(this.pos);
        if (currentResult == null) {
            return null;
        }
        this.pos = currentResult.end;
        if (!glyphLinePartNeedsActualText(currentResult)) {
            currentResult.actualText = null;
            while (this.pos < this.glyphLine.end && (nextResult = nextGlyphLinePart(this.pos)) != null && !glyphLinePartNeedsActualText(nextResult)) {
                currentResult.end = nextResult.end;
                this.pos = nextResult.end;
            }
        }
        return currentResult;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new IllegalStateException("Operation not supported");
    }

    private GlyphLine.GlyphLinePart nextGlyphLinePart(int pos) {
        if (pos >= this.glyphLine.end) {
            return null;
        }
        GlyphLine.ActualText startActualText = this.glyphLine.actualText.get(pos);
        while (pos < this.glyphLine.end && this.glyphLine.actualText.get(pos) == startActualText) {
            pos++;
        }
        return new GlyphLine.GlyphLinePart(pos, pos, startActualText != null ? startActualText.value : null);
    }

    private boolean glyphLinePartNeedsActualText(GlyphLine.GlyphLinePart glyphLinePart) {
        if (glyphLinePart.actualText == null) {
            return false;
        }
        boolean needsActualText = false;
        StringBuilder toUnicodeMapResult = new StringBuilder();
        int i = glyphLinePart.start;
        while (true) {
            if (i >= glyphLinePart.end) {
                break;
            }
            Glyph currentGlyph = this.glyphLine.glyphs.get(i);
            if (!currentGlyph.hasValidUnicode()) {
                needsActualText = true;
                break;
            }
            toUnicodeMapResult.append(TextUtil.convertFromUtf32(currentGlyph.getUnicode()));
            i++;
        }
        return needsActualText || !toUnicodeMapResult.toString().equals(glyphLinePart.actualText);
    }
}
