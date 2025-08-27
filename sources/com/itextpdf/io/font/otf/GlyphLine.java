package com.itextpdf.io.font.otf;

import com.itextpdf.io.font.otf.OpenTableLookup;
import com.itextpdf.io.util.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphLine.class */
public class GlyphLine implements Serializable {
    private static final long serialVersionUID = 4689818013371677649L;
    public int start;
    public int end;
    public int idx;
    protected List<Glyph> glyphs;
    protected List<ActualText> actualText;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphLine$IGlyphLineFilter.class */
    public interface IGlyphLineFilter {
        boolean accept(Glyph glyph);
    }

    public GlyphLine() {
        this.glyphs = new ArrayList();
    }

    public GlyphLine(List<Glyph> glyphs) {
        this.glyphs = glyphs;
        this.start = 0;
        this.end = glyphs.size();
    }

    public GlyphLine(List<Glyph> glyphs, int start, int end) {
        this.glyphs = glyphs;
        this.start = start;
        this.end = end;
    }

    protected GlyphLine(List<Glyph> glyphs, List<ActualText> actualText, int start, int end) {
        this(glyphs, start, end);
        this.actualText = actualText;
    }

    public GlyphLine(GlyphLine other) {
        this.glyphs = other.glyphs;
        this.actualText = other.actualText;
        this.start = other.start;
        this.end = other.end;
        this.idx = other.idx;
    }

    public GlyphLine(GlyphLine other, int start, int end) {
        this.glyphs = other.glyphs.subList(start, end);
        if (other.actualText != null) {
            this.actualText = other.actualText.subList(start, end);
        }
        this.start = 0;
        this.end = end - start;
        this.idx = other.idx - start;
    }

    public String toUnicodeString(int start, int end) {
        ActualTextIterator iter = new ActualTextIterator(this, start, end);
        StringBuilder str = new StringBuilder();
        while (iter.hasNext()) {
            GlyphLinePart part = iter.next();
            if (part.actualText != null) {
                str.append(part.actualText);
            } else {
                for (int i = part.start; i < part.end; i++) {
                    str.append(this.glyphs.get(i).getUnicodeChars());
                }
            }
        }
        return str.toString();
    }

    public String toString() {
        return toUnicodeString(this.start, this.end);
    }

    public GlyphLine copy(int left, int right) {
        GlyphLine glyphLine = new GlyphLine();
        glyphLine.start = 0;
        glyphLine.end = right - left;
        glyphLine.glyphs = new ArrayList(this.glyphs.subList(left, right));
        glyphLine.actualText = this.actualText == null ? null : new ArrayList(this.actualText.subList(left, right));
        return glyphLine;
    }

    public Glyph get(int index) {
        return this.glyphs.get(index);
    }

    public Glyph set(int index, Glyph glyph) {
        return this.glyphs.set(index, glyph);
    }

    public void add(Glyph glyph) {
        this.glyphs.add(glyph);
        if (this.actualText != null) {
            this.actualText.add(null);
        }
    }

    public void add(int index, Glyph glyph) {
        this.glyphs.add(index, glyph);
        if (this.actualText != null) {
            this.actualText.add(index, null);
        }
    }

    public void setGlyphs(List<Glyph> replacementGlyphs) {
        this.glyphs = new ArrayList(replacementGlyphs);
        this.start = 0;
        this.end = replacementGlyphs.size();
        this.actualText = null;
    }

    public void add(GlyphLine other) {
        if (other.actualText != null) {
            if (this.actualText == null) {
                this.actualText = new ArrayList(this.glyphs.size());
                for (int i = 0; i < this.glyphs.size(); i++) {
                    this.actualText.add(null);
                }
            }
            this.actualText.addAll(other.actualText.subList(other.start, other.end));
        }
        this.glyphs.addAll(other.glyphs.subList(other.start, other.end));
    }

    public void replaceContent(GlyphLine other) {
        this.glyphs.clear();
        this.glyphs.addAll(other.glyphs);
        if (other.actualText != null) {
            if (this.actualText == null) {
                this.actualText = new ArrayList();
            } else {
                this.actualText.clear();
            }
            this.actualText.addAll(other.actualText);
        } else {
            this.actualText = null;
        }
        this.start = other.start;
        this.end = other.end;
    }

    public int size() {
        return this.glyphs.size();
    }

    public void substituteManyToOne(OpenTypeFontTableReader tableReader, int lookupFlag, int rightPartLen, int substitutionGlyphIndex) {
        OpenTableLookup.GlyphIndexer gidx = new OpenTableLookup.GlyphIndexer();
        gidx.line = this;
        gidx.idx = this.idx;
        StringBuilder chars = new StringBuilder();
        Glyph currentGlyph = this.glyphs.get(this.idx);
        if (currentGlyph.getChars() != null) {
            chars.append(currentGlyph.getChars());
        } else if (currentGlyph.hasValidUnicode()) {
            chars.append(TextUtil.convertFromUtf32(currentGlyph.getUnicode()));
        }
        for (int j = 0; j < rightPartLen; j++) {
            gidx.nextGlyph(tableReader, lookupFlag);
            Glyph currentGlyph2 = this.glyphs.get(gidx.idx);
            if (currentGlyph2.getChars() != null) {
                chars.append(currentGlyph2.getChars());
            } else if (currentGlyph2.hasValidUnicode()) {
                chars.append(TextUtil.convertFromUtf32(currentGlyph2.getUnicode()));
            }
            int i = gidx.idx;
            gidx.idx = i - 1;
            removeGlyph(i);
        }
        char[] newChars = new char[chars.length()];
        chars.getChars(0, chars.length(), newChars, 0);
        Glyph newGlyph = tableReader.getGlyph(substitutionGlyphIndex);
        newGlyph.setChars(newChars);
        this.glyphs.set(this.idx, newGlyph);
        this.end -= rightPartLen;
    }

    public void substituteOneToOne(OpenTypeFontTableReader tableReader, int substitutionGlyphIndex) {
        Glyph oldGlyph = this.glyphs.get(this.idx);
        Glyph newGlyph = tableReader.getGlyph(substitutionGlyphIndex);
        if (oldGlyph.getChars() != null) {
            newGlyph.setChars(oldGlyph.getChars());
        } else if (newGlyph.hasValidUnicode()) {
            newGlyph.setChars(TextUtil.convertFromUtf32(newGlyph.getUnicode()));
        } else if (oldGlyph.hasValidUnicode()) {
            newGlyph.setChars(TextUtil.convertFromUtf32(oldGlyph.getUnicode()));
        }
        this.glyphs.set(this.idx, newGlyph);
    }

    public void substituteOneToMany(OpenTypeFontTableReader tableReader, int[] substGlyphIds) {
        int substCode = substGlyphIds[0];
        Glyph glyph = tableReader.getGlyph(substCode);
        this.glyphs.set(this.idx, glyph);
        if (substGlyphIds.length > 1) {
            List<Glyph> additionalGlyphs = new ArrayList<>(substGlyphIds.length - 1);
            for (int i = 1; i < substGlyphIds.length; i++) {
                int substCode2 = substGlyphIds[i];
                Glyph glyph2 = tableReader.getGlyph(substCode2);
                additionalGlyphs.add(glyph2);
            }
            addAllGlyphs(this.idx + 1, additionalGlyphs);
            this.idx += substGlyphIds.length - 1;
            this.end += substGlyphIds.length - 1;
        }
    }

    public GlyphLine filter(IGlyphLineFilter filter) {
        boolean anythingFiltered = false;
        List<Glyph> filteredGlyphs = new ArrayList<>(this.end - this.start);
        List<ActualText> filteredActualText = this.actualText != null ? new ArrayList<>(this.end - this.start) : null;
        for (int i = this.start; i < this.end; i++) {
            if (filter.accept(this.glyphs.get(i))) {
                filteredGlyphs.add(this.glyphs.get(i));
                if (filteredActualText != null) {
                    filteredActualText.add(this.actualText.get(i));
                }
            } else {
                anythingFiltered = true;
            }
        }
        if (anythingFiltered) {
            return new GlyphLine(filteredGlyphs, filteredActualText, 0, filteredGlyphs.size());
        }
        return this;
    }

    public void setActualText(int left, int right, String text) {
        if (this.actualText == null) {
            this.actualText = new ArrayList(this.glyphs.size());
            for (int i = 0; i < this.glyphs.size(); i++) {
                this.actualText.add(null);
            }
        }
        ActualText actualText = new ActualText(text);
        for (int i2 = left; i2 < right; i2++) {
            this.actualText.set(i2, actualText);
        }
    }

    public Iterator<GlyphLinePart> iterator() {
        return new ActualTextIterator(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GlyphLine other = (GlyphLine) obj;
        if (this.end - this.start != other.end - other.start) {
            return false;
        }
        if (this.actualText == null && other.actualText != null) {
            return false;
        }
        if (this.actualText != null && other.actualText == null) {
            return false;
        }
        for (int i = this.start; i < this.end; i++) {
            int otherPos = (other.start + i) - this.start;
            Glyph myGlyph = get(i);
            Glyph otherGlyph = other.get(otherPos);
            if (myGlyph == null && otherGlyph != null) {
                return false;
            }
            if (myGlyph != null && !myGlyph.equals(otherGlyph)) {
                return false;
            }
            ActualText myAT = this.actualText == null ? null : this.actualText.get(i);
            ActualText otherAT = other.actualText == null ? null : other.actualText.get(otherPos);
            if (myAT == null && otherAT != null) {
                return false;
            }
            if (myAT != null && !myAT.equals(otherAT)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = (31 * 0) + this.start;
        int result2 = (31 * result) + this.end;
        for (int i = this.start; i < this.end; i++) {
            result2 = (31 * result2) + this.glyphs.get(i).hashCode();
        }
        if (null != this.actualText) {
            for (int i2 = this.start; i2 < this.end; i2++) {
                result2 = 31 * result2;
                if (null != this.actualText.get(i2)) {
                    result2 += this.actualText.get(i2).hashCode();
                }
            }
        }
        return result2;
    }

    private void removeGlyph(int index) {
        this.glyphs.remove(index);
        if (this.actualText != null) {
            this.actualText.remove(index);
        }
    }

    private void addAllGlyphs(int index, List<Glyph> additionalGlyphs) {
        this.glyphs.addAll(index, additionalGlyphs);
        if (this.actualText != null) {
            for (int i = 0; i < additionalGlyphs.size(); i++) {
                this.actualText.add(index, null);
            }
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphLine$GlyphLinePart.class */
    public static class GlyphLinePart {
        public int start;
        public int end;
        public String actualText;
        public boolean reversed;

        public GlyphLinePart(int start, int end) {
            this(start, end, null);
        }

        public GlyphLinePart(int start, int end, String actualText) {
            this.start = start;
            this.end = end;
            this.actualText = actualText;
        }

        public GlyphLinePart setReversed(boolean reversed) {
            this.reversed = reversed;
            return this;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GlyphLine$ActualText.class */
    protected static class ActualText implements Serializable {
        private static final long serialVersionUID = 5109920013485372966L;
        public String value;

        public ActualText(String value) {
            this.value = value;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ActualText other = (ActualText) obj;
            return (this.value == null && other.value == null) || this.value.equals(other.value);
        }

        public int hashCode() {
            return 31 * this.value.hashCode();
        }
    }
}
