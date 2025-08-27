package com.itextpdf.layout.font;

import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.font.PdfFont;
import java.lang.Character;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/ComplexFontSelectorStrategy.class */
public class ComplexFontSelectorStrategy extends FontSelectorStrategy {
    private PdfFont font;
    private FontSelector selector;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ComplexFontSelectorStrategy.class.desiredAssertionStatus();
    }

    public ComplexFontSelectorStrategy(String text, FontSelector selector, FontProvider provider, FontSet tempFonts) {
        super(text, provider, tempFonts);
        this.font = null;
        this.selector = selector;
    }

    public ComplexFontSelectorStrategy(String text, FontSelector selector, FontProvider provider) {
        super(text, provider, null);
        this.font = null;
        this.selector = selector;
    }

    @Override // com.itextpdf.layout.font.FontSelectorStrategy
    public PdfFont getCurrentFont() {
        return this.font;
    }

    @Override // com.itextpdf.layout.font.FontSelectorStrategy
    public List<Glyph> nextGlyphs() {
        int iCharAt;
        PdfFont currentFont;
        Glyph glyph;
        this.font = null;
        int nextUnignorable = nextSignificantIndex();
        if (nextUnignorable < this.text.length()) {
            Iterator<FontInfo> it = this.selector.getFonts().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                FontInfo f = it.next();
                if (isSurrogatePair(this.text, nextUnignorable)) {
                    iCharAt = TextUtil.convertToUtf32(this.text, nextUnignorable);
                } else {
                    iCharAt = this.text.charAt(nextUnignorable);
                }
                int codePoint = iCharAt;
                if (f.getFontUnicodeRange().contains(codePoint) && null != (glyph = (currentFont = getPdfFont(f)).getGlyph(codePoint)) && 0 != glyph.getCode()) {
                    this.font = currentFont;
                    break;
                }
            }
        }
        List<Glyph> glyphs = new ArrayList<>();
        boolean anyGlyphsAppended = false;
        if (this.font != null) {
            Character.UnicodeScript unicodeScript = nextSignificantUnicodeScript(nextUnignorable);
            int to = nextUnignorable;
            int i = nextUnignorable;
            while (i < this.text.length()) {
                int codePoint2 = isSurrogatePair(this.text, i) ? TextUtil.convertToUtf32(this.text, i) : this.text.charAt(i);
                Character.UnicodeScript currScript = Character.UnicodeScript.of(codePoint2);
                if (isSignificantUnicodeScript(currScript) && currScript != unicodeScript) {
                    break;
                }
                if (codePoint2 > 65535) {
                    i++;
                }
                to = i;
                i++;
            }
            int numOfAppendedGlyphs = this.font.appendGlyphs(this.text, this.index, to, glyphs);
            anyGlyphsAppended = numOfAppendedGlyphs > 0;
            if (!$assertionsDisabled && !anyGlyphsAppended) {
                throw new AssertionError();
            }
            this.index += numOfAppendedGlyphs;
        }
        if (!anyGlyphsAppended) {
            this.font = getPdfFont(this.selector.bestMatch());
            if (this.index != nextUnignorable) {
                this.index += this.font.appendGlyphs(this.text, this.index, nextUnignorable - 1, glyphs);
            }
            while (this.index <= nextUnignorable && this.index < this.text.length()) {
                this.index += this.font.appendAnyGlyph(this.text, this.index, glyphs);
            }
        }
        return glyphs;
    }

    private int nextSignificantIndex() {
        int nextValidChar = this.index;
        while (nextValidChar < this.text.length() && TextUtil.isWhitespaceOrNonPrintable(this.text.charAt(nextValidChar))) {
            nextValidChar++;
        }
        return nextValidChar;
    }

    private Character.UnicodeScript nextSignificantUnicodeScript(int from) {
        int codePoint;
        int i = from;
        while (i < this.text.length()) {
            if (isSurrogatePair(this.text, i)) {
                codePoint = TextUtil.convertToUtf32(this.text, i);
                i++;
            } else {
                codePoint = this.text.charAt(i);
            }
            Character.UnicodeScript unicodeScript = Character.UnicodeScript.of(codePoint);
            if (!isSignificantUnicodeScript(unicodeScript)) {
                i++;
            } else {
                return unicodeScript;
            }
        }
        return Character.UnicodeScript.COMMON;
    }

    private static boolean isSignificantUnicodeScript(Character.UnicodeScript unicodeScript) {
        return (unicodeScript == Character.UnicodeScript.COMMON || unicodeScript == Character.UnicodeScript.INHERITED) ? false : true;
    }

    private static boolean isSurrogatePair(String text, int idx) {
        return TextUtil.isSurrogateHigh(text.charAt(idx)) && idx < text.length() - 1 && TextUtil.isSurrogateLow(text.charAt(idx + 1));
    }
}
