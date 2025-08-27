package org.apache.poi.sl.draw;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.InvalidObjectException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.Hyperlink;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Units;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawTextParagraph.class */
public class DrawTextParagraph implements Drawable {
    private static final POILogger LOG;
    public static final XlinkAttribute HYPERLINK_HREF;
    public static final XlinkAttribute HYPERLINK_LABEL;
    protected TextParagraph<?, ?, ?> paragraph;
    double x;
    double y;
    protected List<DrawTextFragment> lines = new ArrayList();
    protected String rawText;
    protected DrawTextFragment bullet;
    protected int autoNbrIdx;
    protected double maxLineHeight;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DrawTextParagraph.class.desiredAssertionStatus();
        LOG = POILogFactory.getLogger((Class<?>) DrawTextParagraph.class);
        HYPERLINK_HREF = new XlinkAttribute("href");
        HYPERLINK_LABEL = new XlinkAttribute(AnnotatedPrivateKey.LABEL);
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawTextParagraph$XlinkAttribute.class */
    private static class XlinkAttribute extends AttributedCharacterIterator.Attribute {
        XlinkAttribute(String name) {
            super(name);
        }

        @Override // java.text.AttributedCharacterIterator.Attribute
        protected Object readResolve() throws InvalidObjectException {
            if (DrawTextParagraph.HYPERLINK_HREF.getName().equals(getName())) {
                return DrawTextParagraph.HYPERLINK_HREF;
            }
            if (DrawTextParagraph.HYPERLINK_LABEL.getName().equals(getName())) {
                return DrawTextParagraph.HYPERLINK_LABEL;
            }
            throw new InvalidObjectException("unknown attribute name");
        }
    }

    public DrawTextParagraph(TextParagraph<?, ?, ?> paragraph) {
        this.paragraph = paragraph;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public void setAutoNumberingIdx(int index) {
        this.autoNbrIdx = index;
    }

    @Override // org.apache.poi.sl.draw.Drawable
    public void draw(Graphics2D graphics) {
        double penX;
        double d;
        double dDoubleValue;
        if (this.lines.isEmpty()) {
            return;
        }
        double penY = this.y;
        boolean firstLine = true;
        int indentLevel = this.paragraph.getIndentLevel();
        Double leftMargin = this.paragraph.getLeftMargin();
        if (leftMargin == null) {
            leftMargin = Double.valueOf(Units.toPoints(347663 * indentLevel));
        }
        Double indent = this.paragraph.getIndent();
        if (indent == null) {
            indent = Double.valueOf(Units.toPoints(347663 * indentLevel));
        }
        if (isHSLF()) {
            indent = Double.valueOf(indent.doubleValue() - leftMargin.doubleValue());
        }
        Double spacing = this.paragraph.getLineSpacing();
        if (spacing == null) {
            spacing = Double.valueOf(100.0d);
        }
        for (DrawTextFragment line : this.lines) {
            if (firstLine) {
                if (!isEmptyParagraph()) {
                    this.bullet = getBullet(graphics, line.getAttributedString().getIterator());
                }
                if (this.bullet != null) {
                    this.bullet.setPosition(this.x + leftMargin.doubleValue() + indent.doubleValue(), penY);
                    this.bullet.draw(graphics);
                    double bulletWidth = this.bullet.getLayout().getAdvance() + 1.0f;
                    penX = this.x + Math.max(leftMargin.doubleValue(), leftMargin.doubleValue() + indent.doubleValue() + bulletWidth);
                } else {
                    penX = this.x + leftMargin.doubleValue();
                }
            } else {
                penX = this.x + leftMargin.doubleValue();
            }
            Rectangle2D anchor = DrawShape.getAnchor(graphics, this.paragraph.getParentShape());
            Insets2D insets = this.paragraph.getParentShape().getInsets();
            double leftInset = insets.left;
            double rightInset = insets.right;
            TextParagraph.TextAlign ta = this.paragraph.getTextAlign();
            if (ta == null) {
                ta = TextParagraph.TextAlign.LEFT;
            }
            switch (ta) {
                case CENTER:
                    penX += ((((anchor.getWidth() - line.getWidth()) - leftInset) - rightInset) - leftMargin.doubleValue()) / 2.0d;
                    break;
                case RIGHT:
                    penX += ((anchor.getWidth() - line.getWidth()) - leftInset) - rightInset;
                    break;
            }
            line.setPosition(penX, penY);
            line.draw(graphics);
            if (spacing.doubleValue() > 0.0d) {
                d = penY;
                dDoubleValue = spacing.doubleValue() * 0.01d * line.getHeight();
            } else {
                d = penY;
                dDoubleValue = -spacing.doubleValue();
            }
            penY = d + dDoubleValue;
            firstLine = false;
        }
        this.y = penY - this.y;
    }

    public float getFirstLineLeading() {
        if (this.lines.isEmpty()) {
            return 0.0f;
        }
        return this.lines.get(0).getLeading();
    }

    public float getFirstLineHeight() {
        if (this.lines.isEmpty()) {
            return 0.0f;
        }
        return this.lines.get(0).getHeight();
    }

    public float getLastLineHeight() {
        if (this.lines.isEmpty()) {
            return 0.0f;
        }
        return this.lines.get(this.lines.size() - 1).getHeight();
    }

    public boolean isEmptyParagraph() {
        return this.lines.isEmpty() || this.rawText.trim().isEmpty();
    }

    @Override // org.apache.poi.sl.draw.Drawable
    public void applyTransform(Graphics2D graphics) {
    }

    @Override // org.apache.poi.sl.draw.Drawable
    public void drawContent(Graphics2D graphics) {
    }

    protected void breakText(Graphics2D graphics) {
        int endIndex;
        this.lines.clear();
        DrawFactory fact = DrawFactory.getInstance(graphics);
        fact.fixFonts(graphics);
        StringBuilder text = new StringBuilder();
        AttributedString at = getAttributedString(graphics, text);
        boolean emptyParagraph = "".equals(text.toString().trim());
        AttributedCharacterIterator it = at.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(it, graphics.getFontRenderContext());
        do {
            int startIndex = measurer.getPosition();
            double wrappingWidth = getWrappingWidth(this.lines.isEmpty(), graphics) + 1.0d;
            if (wrappingWidth < 0.0d) {
                wrappingWidth = 1.0d;
            }
            int nextBreak = text.indexOf(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, startIndex + 1);
            if (nextBreak == -1) {
                nextBreak = it.getEndIndex();
            }
            TextLayout layout = measurer.nextLayout((float) wrappingWidth, nextBreak, true);
            if (layout == null) {
                layout = measurer.nextLayout((float) wrappingWidth, nextBreak, false);
            }
            if (layout == null) {
                break;
            }
            endIndex = measurer.getPosition();
            if (endIndex < it.getEndIndex() && text.charAt(endIndex) == '\n') {
                measurer.setPosition(endIndex + 1);
            }
            TextParagraph.TextAlign hAlign = this.paragraph.getTextAlign();
            if (hAlign == TextParagraph.TextAlign.JUSTIFY || hAlign == TextParagraph.TextAlign.JUSTIFY_LOW) {
                layout = layout.getJustifiedLayout((float) wrappingWidth);
            }
            AttributedString str = emptyParagraph ? null : new AttributedString(it, startIndex, endIndex);
            DrawTextFragment line = fact.getTextFragment(layout, str);
            this.lines.add(line);
            this.maxLineHeight = Math.max(this.maxLineHeight, line.getHeight());
        } while (endIndex != it.getEndIndex());
        this.rawText = text.toString();
    }

    protected DrawTextFragment getBullet(Graphics2D graphics, AttributedCharacterIterator firstLineAttr) {
        String buCharacter;
        Paint fgPaint;
        float fontSize;
        TextParagraph.BulletStyle bulletStyle = this.paragraph.getBulletStyle();
        if (bulletStyle == null) {
            return null;
        }
        AutoNumberingScheme ans = bulletStyle.getAutoNumberingScheme();
        if (ans != null) {
            buCharacter = ans.format(this.autoNbrIdx);
        } else {
            buCharacter = bulletStyle.getBulletCharacter();
        }
        if (buCharacter == null) {
            return null;
        }
        PlaceableShape<?, ?> ps = getParagraphShape();
        PaintStyle fgPaintStyle = bulletStyle.getBulletFontColor();
        if (fgPaintStyle == null) {
            fgPaint = (Paint) firstLineAttr.getAttribute(TextAttribute.FOREGROUND);
        } else {
            fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
        }
        float fontSize2 = ((Float) firstLineAttr.getAttribute(TextAttribute.SIZE)).floatValue();
        Double buSz = bulletStyle.getBulletFontSize();
        if (buSz == null) {
            buSz = Double.valueOf(100.0d);
        }
        if (buSz.doubleValue() > 0.0d) {
            fontSize = (float) (fontSize2 * buSz.doubleValue() * 0.01d);
        } else {
            fontSize = (float) (-buSz.doubleValue());
        }
        String buFontStr = bulletStyle.getBulletFont();
        if (buFontStr == null) {
            buFontStr = this.paragraph.getDefaultFontFamily();
        }
        if (!$assertionsDisabled && buFontStr == null) {
            throw new AssertionError();
        }
        FontInfo buFont = new DrawFontInfo(buFontStr);
        DrawFontManager dfm = DrawFactory.getInstance(graphics).getFontManager(graphics);
        FontInfo buFont2 = dfm.getMappedFont(graphics, buFont);
        AttributedString str = new AttributedString(dfm.mapFontCharset(graphics, buFont2, buCharacter));
        str.addAttribute(TextAttribute.FOREGROUND, fgPaint);
        str.addAttribute(TextAttribute.FAMILY, buFont2.getTypeface());
        str.addAttribute(TextAttribute.SIZE, Float.valueOf(fontSize));
        TextLayout layout = new TextLayout(str.getIterator(), graphics.getFontRenderContext());
        DrawFactory fact = DrawFactory.getInstance(graphics);
        return fact.getTextFragment(layout, str);
    }

    protected String getRenderableText(Graphics2D graphics, TextRun tr) {
        if (tr.getFieldType() == TextRun.FieldType.SLIDE_NUMBER) {
            Slide<?, ?> slide = (Slide) graphics.getRenderingHint(Drawable.CURRENT_SLIDE);
            return slide == null ? "" : Integer.toString(slide.getSlideNumber());
        }
        StringBuilder buf = new StringBuilder();
        TextRun.TextCap cap = tr.getTextCap();
        String tabs = null;
        char[] arr$ = tr.getRawText().toCharArray();
        for (char c : arr$) {
            switch (c) {
                case '\t':
                    if (tabs == null) {
                        tabs = tab2space(tr);
                    }
                    buf.append(tabs);
                    break;
                case 11:
                    buf.append('\n');
                    break;
                default:
                    switch (cap) {
                        case ALL:
                            c = Character.toUpperCase(c);
                            break;
                        case SMALL:
                            c = Character.toLowerCase(c);
                            break;
                    }
                    buf.append(c);
                    break;
            }
        }
        return buf.toString();
    }

    private String tab2space(TextRun tr) {
        AttributedString string = new AttributedString(SymbolConstants.SPACE_SYMBOL);
        String fontFamily = tr.getFontFamily();
        if (fontFamily == null) {
            fontFamily = "Lucida Sans";
        }
        string.addAttribute(TextAttribute.FAMILY, fontFamily);
        Double fs = tr.getFontSize();
        if (fs == null) {
            fs = Double.valueOf(12.0d);
        }
        string.addAttribute(TextAttribute.SIZE, Float.valueOf(fs.floatValue()));
        TextLayout l = new TextLayout(string.getIterator(), new FontRenderContext((AffineTransform) null, true, true));
        double wspace = l.getAdvance();
        Double tabSz = this.paragraph.getDefaultTabSize();
        if (tabSz == null) {
            tabSz = Double.valueOf(wspace * 4.0d);
        }
        int numSpaces = (int) Math.ceil(tabSz.doubleValue() / wspace);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < numSpaces; i++) {
            buf.append(' ');
        }
        return buf.toString();
    }

    protected double getWrappingWidth(boolean firstLine, Graphics2D graphics) {
        double width;
        TextShape<S, P> parentShape = this.paragraph.getParentShape();
        Insets2D insets = parentShape.getInsets();
        double leftInset = insets.left;
        double rightInset = insets.right;
        int indentLevel = this.paragraph.getIndentLevel();
        if (indentLevel == -1) {
            indentLevel = 0;
        }
        Double leftMargin = this.paragraph.getLeftMargin();
        if (leftMargin == null) {
            leftMargin = Double.valueOf(Units.toPoints(347663 * (indentLevel + 1)));
        }
        Double indent = this.paragraph.getIndent();
        if (indent == null) {
            indent = Double.valueOf(Units.toPoints(347663 * indentLevel));
        }
        Double rightMargin = this.paragraph.getRightMargin();
        if (rightMargin == null) {
            rightMargin = Double.valueOf(0.0d);
        }
        Rectangle2D anchor = DrawShape.getAnchor(graphics, parentShape);
        TextShape.TextDirection textDir = parentShape.getTextDirection();
        if (!parentShape.getWordWrap()) {
            Dimension pageDim = parentShape.getSheet().getSlideShow().getPageSize();
            switch (textDir) {
                case VERTICAL:
                    width = pageDim.getHeight() - anchor.getX();
                    break;
                case VERTICAL_270:
                    width = anchor.getX();
                    break;
                default:
                    width = pageDim.getWidth() - anchor.getX();
                    break;
            }
        } else {
            switch (textDir) {
                case VERTICAL:
                case VERTICAL_270:
                    width = (((anchor.getHeight() - leftInset) - rightInset) - leftMargin.doubleValue()) - rightMargin.doubleValue();
                    break;
                default:
                    width = (((anchor.getWidth() - leftInset) - rightInset) - leftMargin.doubleValue()) - rightMargin.doubleValue();
                    break;
            }
            if (firstLine && !isHSLF()) {
                if (this.bullet != null) {
                    if (indent.doubleValue() > 0.0d) {
                        width -= indent.doubleValue();
                    }
                } else if (indent.doubleValue() > 0.0d) {
                    width -= indent.doubleValue();
                } else if (indent.doubleValue() < 0.0d) {
                    width += leftMargin.doubleValue();
                }
            }
        }
        return width;
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/DrawTextParagraph$AttributedStringData.class */
    private static class AttributedStringData {
        AttributedCharacterIterator.Attribute attribute;
        Object value;
        int beginIndex;
        int endIndex;

        AttributedStringData(AttributedCharacterIterator.Attribute attribute, Object value, int beginIndex, int endIndex) {
            this.attribute = attribute;
            this.value = value;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }
    }

    private PlaceableShape<?, ?> getParagraphShape() {
        return new PlaceableShape() { // from class: org.apache.poi.sl.draw.DrawTextParagraph.1
            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public ShapeContainer<?, ?> getParent() {
                return null;
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public Rectangle2D getAnchor() {
                return DrawTextParagraph.this.paragraph.getParentShape().getAnchor();
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public void setAnchor(Rectangle2D anchor) {
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public double getRotation() {
                return 0.0d;
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public void setRotation(double theta) {
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public void setFlipHorizontal(boolean flip) {
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public void setFlipVertical(boolean flip) {
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public boolean getFlipHorizontal() {
                return false;
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public boolean getFlipVertical() {
                return false;
            }

            @Override // org.apache.poi.sl.usermodel.PlaceableShape
            public Sheet<?, ?> getSheet() {
                return DrawTextParagraph.this.paragraph.getParentShape().getSheet();
            }
        };
    }

    protected AttributedString getAttributedString(Graphics2D graphics, StringBuilder text) {
        List<AttributedStringData> attList = new ArrayList<>();
        if (text == null) {
            text = new StringBuilder();
        }
        PlaceableShape<?, ?> ps = getParagraphShape();
        DrawFontManager dfm = DrawFactory.getInstance(graphics).getFontManager(graphics);
        if (!$assertionsDisabled && dfm == null) {
            throw new AssertionError();
        }
        Iterator i$ = this.paragraph.iterator();
        while (i$.hasNext()) {
            TextRun run = (TextRun) i$.next();
            String runText = getRenderableText(graphics, run);
            if (!runText.isEmpty()) {
                String runText2 = dfm.mapFontCharset(graphics, run.getFontInfo(null), runText);
                int beginIndex = text.length();
                text.append(runText2);
                int endIndex = text.length();
                PaintStyle fgPaintStyle = run.getFontColor();
                Paint fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
                attList.add(new AttributedStringData(TextAttribute.FOREGROUND, fgPaint, beginIndex, endIndex));
                Double fontSz = run.getFontSize();
                if (fontSz == null) {
                    fontSz = this.paragraph.getDefaultFontSize();
                }
                attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz.floatValue()), beginIndex, endIndex));
                if (run.isBold()) {
                    attList.add(new AttributedStringData(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, beginIndex, endIndex));
                }
                if (run.isItalic()) {
                    attList.add(new AttributedStringData(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, beginIndex, endIndex));
                }
                if (run.isUnderlined()) {
                    attList.add(new AttributedStringData(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, beginIndex, endIndex));
                    attList.add(new AttributedStringData(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, beginIndex, endIndex));
                }
                if (run.isStrikethrough()) {
                    attList.add(new AttributedStringData(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, beginIndex, endIndex));
                }
                if (run.isSubscript()) {
                    attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, beginIndex, endIndex));
                }
                if (run.isSuperscript()) {
                    attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, beginIndex, endIndex));
                }
                Hyperlink<?, ?> hl = run.getHyperlink();
                if (hl != null) {
                    attList.add(new AttributedStringData(HYPERLINK_HREF, hl.getAddress(), beginIndex, endIndex));
                    attList.add(new AttributedStringData(HYPERLINK_LABEL, hl.getLabel(), beginIndex, endIndex));
                }
                processGlyphs(graphics, dfm, attList, beginIndex, run, runText2);
            }
        }
        if (text.length() == 0) {
            Double fontSz2 = this.paragraph.getDefaultFontSize();
            text.append(SymbolConstants.SPACE_SYMBOL);
            attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz2.floatValue()), 0, 1));
        }
        AttributedString string = new AttributedString(text.toString());
        for (AttributedStringData asd : attList) {
            string.addAttribute(asd.attribute, asd.value, asd.beginIndex, asd.endIndex);
        }
        return string;
    }

    private void processGlyphs(Graphics2D graphics, DrawFontManager dfm, List<AttributedStringData> attList, int beginIndex, TextRun run, String runText) {
        List<FontGroup.FontGroupRange> ttrList = FontGroup.getFontGroupRanges(runText);
        int rangeBegin = 0;
        for (FontGroup.FontGroupRange ttr : ttrList) {
            FontInfo fiRun = run.getFontInfo(ttr.getFontGroup());
            if (fiRun == null) {
                fiRun = run.getFontInfo(FontGroup.LATIN);
            }
            FontInfo fiMapped = dfm.getMappedFont(graphics, fiRun);
            FontInfo fiFallback = dfm.getFallbackFont(graphics, fiRun);
            if (!$assertionsDisabled && fiFallback == null) {
                throw new AssertionError();
            }
            if (fiMapped == null) {
                fiMapped = dfm.getMappedFont(graphics, new DrawFontInfo(this.paragraph.getDefaultFontFamily()));
            }
            if (fiMapped == null) {
                fiMapped = fiFallback;
            }
            Font fontMapped = dfm.createAWTFont(graphics, fiMapped, 10.0d, run.isBold(), run.isItalic());
            Font fontFallback = dfm.createAWTFont(graphics, fiFallback, 10.0d, run.isBold(), run.isItalic());
            int rangeLen = ttr.getLength();
            int partEnd = rangeBegin;
            while (partEnd < rangeBegin + rangeLen) {
                int partBegin = partEnd;
                int partEnd2 = nextPart(fontMapped, runText, partBegin, rangeBegin + rangeLen, true);
                if (partBegin < partEnd2) {
                    attList.add(new AttributedStringData(TextAttribute.FAMILY, fontMapped.getFontName(Locale.ROOT), beginIndex + partBegin, beginIndex + partEnd2));
                    if (LOG.check(1)) {
                        LOG.log(1, "mapped: ", fontMapped.getFontName(Locale.ROOT), SymbolConstants.SPACE_SYMBOL, Integer.valueOf(beginIndex + partBegin), SymbolConstants.SPACE_SYMBOL, Integer.valueOf(beginIndex + partEnd2), " - ", runText.substring(beginIndex + partBegin, beginIndex + partEnd2));
                    }
                }
                partEnd = nextPart(fontMapped, runText, partEnd2, rangeBegin + rangeLen, false);
                if (partEnd2 < partEnd) {
                    attList.add(new AttributedStringData(TextAttribute.FAMILY, fontFallback.getFontName(Locale.ROOT), beginIndex + partEnd2, beginIndex + partEnd));
                    if (LOG.check(1)) {
                        LOG.log(1, "fallback: ", fontFallback.getFontName(Locale.ROOT), SymbolConstants.SPACE_SYMBOL, Integer.valueOf(beginIndex + partEnd2), SymbolConstants.SPACE_SYMBOL, Integer.valueOf(beginIndex + partEnd), " - ", runText.substring(beginIndex + partEnd2, beginIndex + partEnd));
                    }
                }
            }
            rangeBegin += rangeLen;
        }
    }

    private static int nextPart(Font fontMapped, String runText, int beginPart, int endPart, boolean isDisplayed) {
        int rIdx;
        int iCharCount = beginPart;
        while (true) {
            rIdx = iCharCount;
            if (rIdx >= endPart) {
                break;
            }
            int codepoint = runText.codePointAt(rIdx);
            if (fontMapped.canDisplay(codepoint) != isDisplayed) {
                break;
            }
            iCharCount = rIdx + Character.charCount(codepoint);
        }
        return rIdx;
    }

    protected boolean isHSLF() {
        return DrawShape.isHSLF(this.paragraph.getParentShape());
    }
}
