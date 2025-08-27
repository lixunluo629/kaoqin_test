package com.itextpdf.layout.renderer;

import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.FontMetrics;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.util.EnumUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfType0Font;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.font.FontCharacteristics;
import com.itextpdf.layout.font.FontFamilySplitter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSelectorStrategy;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.TextLayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.Background;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.FontKerning;
import com.itextpdf.layout.property.TransparentColor;
import com.itextpdf.layout.property.Underline;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.lang.Character;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TextRenderer.class */
public class TextRenderer extends AbstractRenderer implements ILeafElementRenderer {
    protected static final float TEXT_SPACE_COEFF = 1000.0f;
    private static final float ITALIC_ANGLE = 0.21256f;
    private static final float BOLD_SIMULATION_STROKE_COEFF = 0.033333335f;
    private static final float TYPO_ASCENDER_SCALE_COEFF = 1.2f;
    protected float yLineOffset;
    private PdfFont font;
    protected GlyphLine text;
    protected GlyphLine line;
    protected String strToBeConverted;
    protected boolean otfFeaturesApplied;
    protected float tabAnchorCharacterPosition;
    protected List<int[]> reversedRanges;
    protected GlyphLine savedWordBreakAtLineEnding;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TextRenderer.class.desiredAssertionStatus();
    }

    public TextRenderer(Text textElement) {
        this(textElement, textElement.getText());
    }

    public TextRenderer(Text textElement, String text) {
        super(textElement);
        this.otfFeaturesApplied = false;
        this.tabAnchorCharacterPosition = -1.0f;
        this.strToBeConverted = text;
    }

    protected TextRenderer(TextRenderer other) {
        super(other);
        this.otfFeaturesApplied = false;
        this.tabAnchorCharacterPosition = -1.0f;
        this.text = other.text;
        this.line = other.line;
        this.font = other.font;
        this.yLineOffset = other.yLineOffset;
        this.strToBeConverted = other.strToBeConverted;
        this.otfFeaturesApplied = other.otfFeaturesApplied;
        this.tabAnchorCharacterPosition = other.tabAnchorCharacterPosition;
        this.reversedRanges = other.reversedRanges;
    }

    /* JADX WARN: Code restructure failed: missing block: B:65:0x0393, code lost:
    
        r53 = r64;
     */
    @Override // com.itextpdf.layout.renderer.IRenderer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.itextpdf.layout.layout.LayoutResult layout(com.itextpdf.layout.layout.LayoutContext r12) {
        /*
            Method dump skipped, instructions count: 2889
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.layout.renderer.TextRenderer.layout(com.itextpdf.layout.layout.LayoutContext):com.itextpdf.layout.layout.LayoutResult");
    }

    public void applyOtf() {
        updateFontAndText();
        Character.UnicodeScript script = (Character.UnicodeScript) getProperty(23);
        if (!this.otfFeaturesApplied && TypographyUtils.isPdfCalligraphAvailable() && this.text.start < this.text.end) {
            if (hasOtfFont()) {
                Object typographyConfig = getProperty(117);
                Collection<Character.UnicodeScript> supportedScripts = null;
                if (typographyConfig != null) {
                    supportedScripts = TypographyUtils.getSupportedScripts(typographyConfig);
                }
                if (supportedScripts == null) {
                    supportedScripts = TypographyUtils.getSupportedScripts();
                }
                List<ScriptRange> scriptsRanges = new ArrayList<>();
                if (script != null) {
                    scriptsRanges.add(new ScriptRange(script, this.text.end));
                } else {
                    ScriptRange currRange = new ScriptRange(null, this.text.end);
                    scriptsRanges.add(currRange);
                    for (int i = this.text.start; i < this.text.end; i++) {
                        int unicode = this.text.get(i).getUnicode();
                        if (unicode > -1) {
                            Character.UnicodeScript glyphScript = Character.UnicodeScript.of(unicode);
                            if (!Character.UnicodeScript.COMMON.equals(glyphScript) && !Character.UnicodeScript.UNKNOWN.equals(glyphScript) && !Character.UnicodeScript.INHERITED.equals(glyphScript) && glyphScript != currRange.script) {
                                if (currRange.script == null) {
                                    currRange.script = glyphScript;
                                } else {
                                    currRange.rangeEnd = i;
                                    currRange = new ScriptRange(glyphScript, this.text.end);
                                    scriptsRanges.add(currRange);
                                }
                            }
                        }
                    }
                }
                int delta = 0;
                int origTextStart = this.text.start;
                int origTextEnd = this.text.end;
                int shapingRangeStart = this.text.start;
                for (ScriptRange scriptsRange : scriptsRanges) {
                    if (scriptsRange.script != null && supportedScripts.contains(EnumUtil.throwIfNull(scriptsRange.script))) {
                        scriptsRange.rangeEnd += delta;
                        this.text.start = shapingRangeStart;
                        this.text.end = scriptsRange.rangeEnd;
                        if ((scriptsRange.script == Character.UnicodeScript.ARABIC || scriptsRange.script == Character.UnicodeScript.HEBREW) && (this.parent instanceof LineRenderer)) {
                            setProperty(7, BaseDirection.DEFAULT_BIDI);
                        }
                        TypographyUtils.applyOtfScript(this.font.getFontProgram(), this.text, scriptsRange.script, typographyConfig);
                        delta += this.text.end - scriptsRange.rangeEnd;
                        int i2 = this.text.end;
                        shapingRangeStart = i2;
                        scriptsRange.rangeEnd = i2;
                    }
                }
                this.text.start = origTextStart;
                this.text.end = origTextEnd + delta;
            }
            FontKerning fontKerning = (FontKerning) getProperty(22, FontKerning.NO);
            if (fontKerning == FontKerning.YES) {
                TypographyUtils.applyKerning(this.font.getFontProgram(), this.text);
            }
            this.otfFeaturesApplied = true;
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TextRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        boolean isTagged = drawContext.isTaggingEnabled();
        LayoutTaggingHelper taggingHelper = null;
        boolean isArtifact = false;
        TagTreePointer tagPointer = null;
        if (isTagged) {
            taggingHelper = (LayoutTaggingHelper) getProperty(108);
            if (taggingHelper == null) {
                isArtifact = true;
            } else {
                isArtifact = taggingHelper.isArtifact(this);
                if (!isArtifact) {
                    tagPointer = taggingHelper.useAutoTaggingPointerAndRememberItsPosition(this);
                    if (taggingHelper.createTag(this, tagPointer)) {
                        tagPointer.getProperties().addAttributes(0, AccessibleAttributesApplier.getLayoutAttributes(this, tagPointer));
                    }
                }
            }
        }
        super.draw(drawContext);
        applyMargins(this.occupiedArea.getBBox(), getMargins(), false);
        applyBorderBox(this.occupiedArea.getBBox(), false);
        applyPaddings(this.occupiedArea.getBBox(), getPaddings(), false);
        boolean isRelativePosition = isRelativePosition();
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
        }
        float leftBBoxX = this.occupiedArea.getBBox().getX();
        if (this.line.end > this.line.start || this.savedWordBreakAtLineEnding != null) {
            UnitValue fontSize = getPropertyAsUnitValue(24);
            if (!fontSize.isPointValue()) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) TextRenderer.class);
                logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
            }
            TransparentColor fontColor = getPropertyAsTransparentColor(21);
            Integer textRenderingMode = (Integer) getProperty(71);
            Float textRise = getPropertyAsFloat(72);
            Float characterSpacing = getPropertyAsFloat(15);
            Float wordSpacing = getPropertyAsFloat(78);
            Float horizontalScaling = (Float) getProperty(29);
            float[] skew = (float[]) getProperty(65);
            boolean italicSimulation = Boolean.TRUE.equals(getPropertyAsBoolean(31));
            boolean boldSimulation = Boolean.TRUE.equals(getPropertyAsBoolean(8));
            Float strokeWidth = null;
            if (boldSimulation) {
                textRenderingMode = 2;
                strokeWidth = Float.valueOf(fontSize.getValue() / 30.0f);
            }
            PdfCanvas canvas = drawContext.getCanvas();
            if (isTagged) {
                if (isArtifact) {
                    canvas.openTag(new CanvasArtifact());
                } else {
                    canvas.openTag(tagPointer.getTagReference());
                }
            }
            beginElementOpacityApplying(drawContext);
            canvas.saveState().beginText().setFontAndSize(this.font, fontSize.getValue());
            if (skew != null && skew.length == 2) {
                canvas.setTextMatrix(1.0f, skew[0], skew[1], 1.0f, leftBBoxX, getYLine());
            } else if (italicSimulation) {
                canvas.setTextMatrix(1.0f, 0.0f, ITALIC_ANGLE, 1.0f, leftBBoxX, getYLine());
            } else {
                canvas.moveText(leftBBoxX, getYLine());
            }
            if (textRenderingMode.intValue() != 0) {
                canvas.setTextRenderingMode(textRenderingMode.intValue());
            }
            if (textRenderingMode.intValue() == 1 || textRenderingMode.intValue() == 2) {
                if (strokeWidth == null) {
                    strokeWidth = getPropertyAsFloat(64);
                }
                if (strokeWidth != null && strokeWidth.floatValue() != 1.0f) {
                    canvas.setLineWidth(strokeWidth.floatValue());
                }
                Color strokeColor = getPropertyAsColor(63);
                if (strokeColor == null && fontColor != null) {
                    strokeColor = fontColor.getColor();
                }
                if (strokeColor != null) {
                    canvas.setStrokeColor(strokeColor);
                }
            }
            if (fontColor != null) {
                canvas.setFillColor(fontColor.getColor());
                fontColor.applyFillTransparency(canvas);
            }
            if (textRise != null && textRise.floatValue() != 0.0f) {
                canvas.setTextRise(textRise.floatValue());
            }
            if (characterSpacing != null && characterSpacing.floatValue() != 0.0f) {
                canvas.setCharacterSpacing(characterSpacing.floatValue());
            }
            if (wordSpacing != null && wordSpacing.floatValue() != 0.0f) {
                if (this.font instanceof PdfType0Font) {
                    for (int gInd = this.line.start; gInd < this.line.end; gInd++) {
                        if (TextUtil.isUni0020(this.line.get(gInd))) {
                            short advance = (short) ((TEXT_SPACE_COEFF * wordSpacing.floatValue()) / fontSize.getValue());
                            Glyph copy = new Glyph(this.line.get(gInd));
                            copy.setXAdvance(advance);
                            this.line.set(gInd, copy);
                        }
                    }
                } else {
                    canvas.setWordSpacing(wordSpacing.floatValue());
                }
            }
            if (horizontalScaling != null && horizontalScaling.floatValue() != 1.0f) {
                canvas.setHorizontalScaling(horizontalScaling.floatValue() * 100.0f);
            }
            GlyphLine.IGlyphLineFilter filter = new GlyphLine.IGlyphLineFilter() { // from class: com.itextpdf.layout.renderer.TextRenderer.1
                @Override // com.itextpdf.io.font.otf.GlyphLine.IGlyphLineFilter
                public boolean accept(Glyph glyph) {
                    return !TextRenderer.noPrint(glyph);
                }
            };
            boolean appearanceStreamLayout = Boolean.TRUE.equals(getPropertyAsBoolean(82));
            if (getReversedRanges() != null) {
                boolean writeReversedChars = !appearanceStreamLayout;
                ArrayList<Integer> removedIds = new ArrayList<>();
                for (int i = this.line.start; i < this.line.end; i++) {
                    if (!filter.accept(this.line.get(i))) {
                        removedIds.add(Integer.valueOf(i));
                    }
                }
                for (int[] range : getReversedRanges()) {
                    updateRangeBasedOnRemovedCharacters(removedIds, range);
                }
                this.line = this.line.filter(filter);
                if (writeReversedChars) {
                    canvas.showText(this.line, new ReversedCharsIterator(this.reversedRanges, this.line).setUseReversed(true));
                } else {
                    canvas.showText(this.line);
                }
            } else {
                if (appearanceStreamLayout) {
                    this.line.setActualText(this.line.start, this.line.end, null);
                }
                canvas.showText(this.line.filter(filter));
            }
            if (this.savedWordBreakAtLineEnding != null) {
                canvas.showText(this.savedWordBreakAtLineEnding);
            }
            canvas.endText().restoreState();
            endElementOpacityApplying(drawContext);
            Object underlines = getProperty(74);
            if (underlines instanceof List) {
                for (Object underline : (List) underlines) {
                    if (underline instanceof Underline) {
                        drawSingleUnderline((Underline) underline, fontColor, canvas, fontSize.getValue(), italicSimulation ? ITALIC_ANGLE : 0.0f);
                    }
                }
            } else if (underlines instanceof Underline) {
                drawSingleUnderline((Underline) underlines, fontColor, canvas, fontSize.getValue(), italicSimulation ? ITALIC_ANGLE : 0.0f);
            }
            if (isTagged) {
                canvas.closeTag();
            }
        }
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
        }
        applyPaddings(this.occupiedArea.getBBox(), true);
        applyBorderBox(this.occupiedArea.getBBox(), true);
        applyMargins(this.occupiedArea.getBBox(), getMargins(), true);
        if (isTagged && !isArtifact) {
            if (this.isLastRendererForModelElement) {
                taggingHelper.finishTaggingHint(this);
            }
            taggingHelper.restoreAutoTaggingPointerPosition(this);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawBackground(DrawContext drawContext) {
        Background background = (Background) getProperty(6);
        Float textRise = getPropertyAsFloat(72);
        Rectangle bBox = getOccupiedAreaBBox();
        Rectangle backgroundArea = applyMargins(bBox, false);
        float bottomBBoxY = backgroundArea.getY();
        float leftBBoxX = backgroundArea.getX();
        if (background != null) {
            boolean isTagged = drawContext.isTaggingEnabled();
            PdfCanvas canvas = drawContext.getCanvas();
            if (isTagged) {
                canvas.openTag(new CanvasArtifact());
            }
            boolean backgroundAreaIsClipped = clipBackgroundArea(drawContext, backgroundArea);
            canvas.saveState().setFillColor(background.getColor());
            TransparentColor backgroundColor = new TransparentColor(background.getColor(), background.getOpacity());
            backgroundColor.applyFillTransparency(drawContext.getCanvas());
            canvas.rectangle(leftBBoxX - background.getExtraLeft(), (bottomBBoxY + textRise.floatValue()) - background.getExtraBottom(), backgroundArea.getWidth() + background.getExtraLeft() + background.getExtraRight(), (backgroundArea.getHeight() - textRise.floatValue()) + background.getExtraTop() + background.getExtraBottom());
            canvas.fill().restoreState();
            if (backgroundAreaIsClipped) {
                drawContext.getCanvas().restoreState();
            }
            if (isTagged) {
                canvas.closeTag();
            }
        }
    }

    public void trimFirst() {
        updateFontAndText();
        if (this.text != null) {
            while (this.text.start < this.text.end) {
                Glyph glyph = this.text.get(this.text.start);
                if (TextUtil.isWhitespace(glyph) && !TextUtil.isNewLine(glyph)) {
                    this.text.start++;
                } else {
                    return;
                }
            }
        }
    }

    float trimLast() {
        float trimmedSpace = 0.0f;
        if (this.line.end <= 0) {
            return 0.0f;
        }
        UnitValue fontSize = getPropertyAsUnitValue(24);
        if (!fontSize.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TextRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
        }
        Float characterSpacing = getPropertyAsFloat(15);
        Float wordSpacing = getPropertyAsFloat(78);
        float hScale = getPropertyAsFloat(29, Float.valueOf(1.0f)).floatValue();
        int firstNonSpaceCharIndex = this.line.end - 1;
        while (firstNonSpaceCharIndex >= this.line.start) {
            Glyph currentGlyph = this.line.get(firstNonSpaceCharIndex);
            if (!TextUtil.isWhitespace(currentGlyph)) {
                break;
            }
            saveWordBreakIfNotYetSaved(currentGlyph);
            float currentCharWidth = getCharWidth(currentGlyph, fontSize.getValue(), Float.valueOf(hScale), characterSpacing, wordSpacing) / TEXT_SPACE_COEFF;
            float xAdvance = firstNonSpaceCharIndex > this.line.start ? scaleXAdvance(this.line.get(firstNonSpaceCharIndex - 1).getXAdvance(), fontSize.getValue(), Float.valueOf(hScale)) / TEXT_SPACE_COEFF : 0.0f;
            trimmedSpace += currentCharWidth - xAdvance;
            this.occupiedArea.getBBox().setWidth(this.occupiedArea.getBBox().getWidth() - currentCharWidth);
            firstNonSpaceCharIndex--;
        }
        this.line.end = firstNonSpaceCharIndex + 1;
        return trimmedSpace;
    }

    @Override // com.itextpdf.layout.renderer.ILeafElementRenderer
    public float getAscent() {
        return this.yLineOffset;
    }

    @Override // com.itextpdf.layout.renderer.ILeafElementRenderer
    public float getDescent() {
        return -((this.occupiedArea.getBBox().getHeight() - this.yLineOffset) - getPropertyAsFloat(72).floatValue());
    }

    public float getYLine() {
        return ((this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()) - this.yLineOffset) - getPropertyAsFloat(72).floatValue();
    }

    public void moveYLineTo(float y) {
        float curYLine = getYLine();
        float delta = y - curYLine;
        this.occupiedArea.getBBox().setY(this.occupiedArea.getBBox().getY() + delta);
    }

    public void setText(String text) {
        this.strToBeConverted = text;
        updateFontAndText();
    }

    public void setText(GlyphLine text, int leftPos, int rightPos) {
        this.strToBeConverted = null;
        this.text = new GlyphLine(text);
        this.text.start = leftPos;
        this.text.end = rightPos;
        this.otfFeaturesApplied = false;
    }

    public GlyphLine getText() {
        updateFontAndText();
        return this.text;
    }

    public int length() {
        if (this.text == null) {
            return 0;
        }
        return this.text.end - this.text.start;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public String toString() {
        if (this.line != null) {
            return this.line.toString();
        }
        return null;
    }

    public int charAt(int pos) {
        return this.text.get(pos + this.text.start).getUnicode();
    }

    public float getTabAnchorCharacterPosition() {
        return this.tabAnchorCharacterPosition;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return new TextRenderer((Text) this.modelElement);
    }

    List<int[]> getReversedRanges() {
        return this.reversedRanges;
    }

    List<int[]> initReversedRanges() {
        if (this.reversedRanges == null) {
            this.reversedRanges = new ArrayList();
        }
        return this.reversedRanges;
    }

    TextRenderer removeReversedRanges() {
        this.reversedRanges = null;
        return this;
    }

    static float[] calculateAscenderDescender(PdfFont font) {
        float ascender;
        float descender;
        FontMetrics fontMetrics = font.getFontProgram().getFontMetrics();
        if (fontMetrics.getWinAscender() == 0 || fontMetrics.getWinDescender() == 0 || (fontMetrics.getTypoAscender() == fontMetrics.getWinAscender() && fontMetrics.getTypoDescender() == fontMetrics.getWinDescender())) {
            ascender = fontMetrics.getTypoAscender() * TYPO_ASCENDER_SCALE_COEFF;
            descender = fontMetrics.getTypoDescender() * TYPO_ASCENDER_SCALE_COEFF;
        } else {
            ascender = fontMetrics.getWinAscender();
            descender = fontMetrics.getWinDescender();
        }
        return new float[]{ascender, descender};
    }

    private TextRenderer[] splitIgnoreFirstNewLine(int currentTextPos) {
        if (TextUtil.isCarriageReturnFollowedByLineFeed(this.text, currentTextPos)) {
            return split(currentTextPos + 2);
        }
        return split(currentTextPos + 1);
    }

    private GlyphLine convertToGlyphLine(String text) {
        return this.font.createGlyphLine(text);
    }

    private boolean hasOtfFont() {
        return (this.font instanceof PdfType0Font) && (this.font.getFontProgram() instanceof TrueTypeFont);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getFirstYLineRecursively() {
        return Float.valueOf(getYLine());
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float getLastYLineRecursively() {
        return Float.valueOf(getYLine());
    }

    protected int lineLength() {
        if (this.line.end > 0) {
            return this.line.end - this.line.start;
        }
        return 0;
    }

    protected int baseCharactersCount() {
        int count = 0;
        for (int i = this.line.start; i < this.line.end; i++) {
            Glyph glyph = this.line.get(i);
            if (!glyph.hasPlacement()) {
                count++;
            }
        }
        return count;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        TextLayoutResult result = (TextLayoutResult) layout(new LayoutContext(new LayoutArea(1, new Rectangle(MinMaxWidthUtils.getInfWidth(), 1000000.0f))));
        return result.getMinMaxWidth();
    }

    protected int getNumberOfSpaces() {
        if (this.line.end <= 0) {
            return 0;
        }
        int spaces = 0;
        for (int i = this.line.start; i < this.line.end; i++) {
            Glyph currentGlyph = this.line.get(i);
            if (currentGlyph.getUnicode() == 32) {
                spaces++;
            }
        }
        return spaces;
    }

    protected TextRenderer createSplitRenderer() {
        return (TextRenderer) getNextRenderer();
    }

    protected TextRenderer createOverflowRenderer() {
        return (TextRenderer) getNextRenderer();
    }

    protected TextRenderer[] split(int initialOverflowTextPos) {
        TextRenderer splitRenderer = createSplitRenderer();
        splitRenderer.setText(this.text, this.text.start, initialOverflowTextPos);
        splitRenderer.font = this.font;
        splitRenderer.line = this.line;
        splitRenderer.occupiedArea = this.occupiedArea.mo950clone();
        splitRenderer.parent = this.parent;
        splitRenderer.yLineOffset = this.yLineOffset;
        splitRenderer.otfFeaturesApplied = this.otfFeaturesApplied;
        splitRenderer.isLastRendererForModelElement = false;
        splitRenderer.addAllProperties(getOwnProperties());
        TextRenderer overflowRenderer = createOverflowRenderer();
        overflowRenderer.setText(this.text, initialOverflowTextPos, this.text.end);
        overflowRenderer.font = this.font;
        overflowRenderer.otfFeaturesApplied = this.otfFeaturesApplied;
        overflowRenderer.parent = this.parent;
        overflowRenderer.addAllProperties(getOwnProperties());
        return new TextRenderer[]{splitRenderer, overflowRenderer};
    }

    protected void drawSingleUnderline(Underline underline, TransparentColor fontStrokeColor, PdfCanvas canvas, float fontSize, float italicAngleTan) {
        TransparentColor underlineColor = underline.getColor() != null ? new TransparentColor(underline.getColor(), underline.getOpacity()) : fontStrokeColor;
        canvas.saveState();
        if (underlineColor != null) {
            canvas.setStrokeColor(underlineColor.getColor());
            underlineColor.applyStrokeTransparency(canvas);
        }
        canvas.setLineCapStyle(underline.getLineCapStyle());
        float underlineThickness = underline.getThickness(fontSize);
        if (underlineThickness != 0.0f) {
            canvas.setLineWidth(underlineThickness);
            float yLine = getYLine();
            float underlineYPosition = underline.getYPosition(fontSize) + yLine;
            float italicWidthSubstraction = 0.5f * fontSize * italicAngleTan;
            canvas.moveTo(this.occupiedArea.getBBox().getX(), underlineYPosition).lineTo((this.occupiedArea.getBBox().getX() + this.occupiedArea.getBBox().getWidth()) - italicWidthSubstraction, underlineYPosition).stroke();
        }
        canvas.restoreState();
    }

    protected float calculateLineWidth() {
        UnitValue fontSize = getPropertyAsUnitValue(24);
        if (!fontSize.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TextRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 24));
        }
        return getGlyphLineWidth(this.line, fontSize.getValue(), getPropertyAsFloat(29, Float.valueOf(1.0f)).floatValue(), getPropertyAsFloat(15), getPropertyAsFloat(78));
    }

    protected boolean resolveFonts(List<IRenderer> addTo) {
        Object font = getProperty(20);
        if (font instanceof PdfFont) {
            addTo.add(this);
            return false;
        }
        if ((font instanceof String) || (font instanceof String[])) {
            if (font instanceof String) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.warn(LogMessageConstant.FONT_PROPERTY_OF_STRING_TYPE_IS_DEPRECATED_USE_STRINGS_ARRAY_INSTEAD);
                List<String> splitFontFamily = FontFamilySplitter.splitFontFamily((String) font);
                font = splitFontFamily.toArray(new String[splitFontFamily.size()]);
            }
            FontProvider provider = (FontProvider) getProperty(91);
            FontSet fontSet = (FontSet) getProperty(98);
            if (provider.getFontSet().isEmpty() && (fontSet == null || fontSet.isEmpty())) {
                throw new IllegalStateException(PdfException.FontProviderNotSetFontFamilyNotResolved);
            }
            FontCharacteristics fc = createFontCharacteristics();
            FontSelectorStrategy strategy = provider.getStrategy(this.strToBeConverted, Arrays.asList((String[]) font), fc, fontSet);
            if (null == this.strToBeConverted || this.strToBeConverted.isEmpty()) {
                addTo.add(this);
                return true;
            }
            while (!strategy.endOfText()) {
                GlyphLine nextGlyphs = new GlyphLine(strategy.nextGlyphs());
                PdfFont currentFont = strategy.getCurrentFont();
                TextRenderer textRenderer = createCopy(replaceSpecialWhitespaceGlyphs(nextGlyphs, currentFont), currentFont);
                addTo.add(textRenderer);
            }
            return true;
        }
        throw new IllegalStateException("Invalid FONT property value type.");
    }

    protected void setGlyphLineAndFont(GlyphLine gl, PdfFont font) {
        this.text = gl;
        this.font = font;
        this.otfFeaturesApplied = false;
        this.strToBeConverted = null;
        setProperty(20, font);
    }

    protected TextRenderer createCopy(GlyphLine gl, PdfFont font) {
        TextRenderer copy = new TextRenderer(this);
        copy.setGlyphLineAndFont(gl, font);
        return copy;
    }

    static void updateRangeBasedOnRemovedCharacters(ArrayList<Integer> removedIds, int[] range) {
        int shift = numberOfElementsLessThan(removedIds, range[0]);
        range[0] = range[0] - shift;
        int shift2 = numberOfElementsLessThanOrEqual(removedIds, range[1]);
        range[1] = range[1] - shift2;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    PdfFont resolveFirstPdfFont(String[] font, FontProvider provider, FontCharacteristics fc) {
        FontSelectorStrategy strategy = provider.getStrategy(this.strToBeConverted, Arrays.asList(font), fc);
        while (!strategy.endOfText()) {
            List<Glyph> resolvedGlyphs = strategy.nextGlyphs();
            PdfFont currentFont = strategy.getCurrentFont();
            for (Glyph glyph : resolvedGlyphs) {
                if (currentFont.containsGlyph(glyph.getUnicode())) {
                    return currentFont;
                }
            }
        }
        return super.resolveFirstPdfFont(font, provider, fc);
    }

    private static int numberOfElementsLessThan(ArrayList<Integer> numbers, int n) {
        int x = Collections.binarySearch(numbers, Integer.valueOf(n));
        if (x >= 0) {
            return x;
        }
        return (-x) - 1;
    }

    private static int numberOfElementsLessThanOrEqual(ArrayList<Integer> numbers, int n) {
        int x = Collections.binarySearch(numbers, Integer.valueOf(n));
        if (x >= 0) {
            return x + 1;
        }
        return (-x) - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean noPrint(Glyph g) {
        if (!g.hasValidUnicode()) {
            return false;
        }
        int c = g.getUnicode();
        return TextUtil.isNonPrintable(c);
    }

    private static boolean glyphBelongsToNonBreakingHyphenRelatedChunk(GlyphLine text, int ind) {
        return TextUtil.isNonBreakingHyphen(text.get(ind)) || (ind + 1 < text.end && TextUtil.isNonBreakingHyphen(text.get(ind + 1))) || (ind - 1 >= text.start && TextUtil.isNonBreakingHyphen(text.get(ind - 1)));
    }

    private float getCharWidth(Glyph g, float fontSize, Float hScale, Float characterSpacing, Float wordSpacing) {
        if (hScale == null) {
            hScale = Float.valueOf(1.0f);
        }
        float resultWidth = g.getWidth() * fontSize * hScale.floatValue();
        if (characterSpacing != null) {
            resultWidth += characterSpacing.floatValue() * hScale.floatValue() * TEXT_SPACE_COEFF;
        }
        if (wordSpacing != null && g.getUnicode() == 32) {
            resultWidth += wordSpacing.floatValue() * hScale.floatValue() * TEXT_SPACE_COEFF;
        }
        return resultWidth;
    }

    private float scaleXAdvance(float xAdvance, float fontSize, Float hScale) {
        return xAdvance * fontSize * hScale.floatValue();
    }

    private float getGlyphLineWidth(GlyphLine glyphLine, float fontSize, float hScale, Float characterSpacing, Float wordSpacing) {
        float width = 0.0f;
        int i = glyphLine.start;
        while (i < glyphLine.end) {
            if (!noPrint(glyphLine.get(i))) {
                float charWidth = getCharWidth(glyphLine.get(i), fontSize, Float.valueOf(hScale), characterSpacing, wordSpacing);
                float width2 = width + charWidth;
                float xAdvance = i != glyphLine.start ? scaleXAdvance(glyphLine.get(i - 1).getXAdvance(), fontSize, Float.valueOf(hScale)) : 0.0f;
                width = width2 + xAdvance;
            }
            i++;
        }
        return width / TEXT_SPACE_COEFF;
    }

    private int[] getWordBoundsForHyphenation(GlyphLine text, int leftTextPos, int rightTextPos, int wordMiddleCharPos) {
        while (wordMiddleCharPos >= leftTextPos && !isGlyphPartOfWordForHyphenation(text.get(wordMiddleCharPos)) && !TextUtil.isUni0020(text.get(wordMiddleCharPos))) {
            wordMiddleCharPos--;
        }
        if (wordMiddleCharPos >= leftTextPos) {
            int left = wordMiddleCharPos;
            while (left >= leftTextPos && isGlyphPartOfWordForHyphenation(text.get(left))) {
                left--;
            }
            int right = wordMiddleCharPos;
            while (right < rightTextPos && isGlyphPartOfWordForHyphenation(text.get(right))) {
                right++;
            }
            return new int[]{left + 1, right};
        }
        return null;
    }

    private boolean isGlyphPartOfWordForHyphenation(Glyph g) {
        return Character.isLetter((char) g.getUnicode()) || 173 == g.getUnicode();
    }

    private void updateFontAndText() {
        if (this.strToBeConverted != null) {
            try {
                this.font = getPropertyAsFont(20);
            } catch (ClassCastException e) {
                this.font = resolveFirstPdfFont();
                if (!this.strToBeConverted.isEmpty()) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) TextRenderer.class);
                    logger.error(LogMessageConstant.FONT_PROPERTY_MUST_BE_PDF_FONT_OBJECT);
                }
            }
            this.text = convertToGlyphLine(this.strToBeConverted);
            this.otfFeaturesApplied = false;
            this.strToBeConverted = null;
        }
    }

    private void saveWordBreakIfNotYetSaved(Glyph wordBreak) {
        if (this.savedWordBreakAtLineEnding == null) {
            if (TextUtil.isNewLine(wordBreak)) {
                wordBreak = this.font.getGlyph(32);
            }
            this.savedWordBreakAtLineEnding = new GlyphLine((List<Glyph>) Collections.singletonList(wordBreak));
        }
    }

    private static GlyphLine replaceSpecialWhitespaceGlyphs(GlyphLine line, PdfFont font) {
        if (null != line) {
            Glyph space = font.getGlyph(32);
            for (int i = 0; i < line.size(); i++) {
                Glyph glyph = line.get(i);
                Integer xAdvance = getSpecialWhitespaceXAdvance(glyph, space, font.getFontProgram().getFontMetrics().isFixedPitch());
                if (xAdvance != null) {
                    Glyph newGlyph = new Glyph(space, glyph.getUnicode());
                    if (!$assertionsDisabled && (xAdvance.intValue() > 32767 || xAdvance.intValue() < -32768)) {
                        throw new AssertionError();
                    }
                    newGlyph.setXAdvance((short) xAdvance.intValue());
                    line.set(i, newGlyph);
                }
            }
        }
        return line;
    }

    private static Integer getSpecialWhitespaceXAdvance(Glyph glyph, Glyph spaceGlyph, boolean isMonospaceFont) {
        if (glyph.getCode() > 0) {
            return null;
        }
        switch (glyph.getUnicode()) {
            case 9:
                return Integer.valueOf(3 * spaceGlyph.getWidth());
            case 8194:
                return Integer.valueOf(isMonospaceFont ? 0 : 500 - spaceGlyph.getWidth());
            case 8195:
                return Integer.valueOf(isMonospaceFont ? 0 : 1000 - spaceGlyph.getWidth());
            case SonyType1MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION /* 8201 */:
                return Integer.valueOf(isMonospaceFont ? 0 : 200 - spaceGlyph.getWidth());
            default:
                return null;
        }
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TextRenderer$ReversedCharsIterator.class */
    private static class ReversedCharsIterator implements Iterator<GlyphLine.GlyphLinePart> {
        private boolean useReversed;
        private int currentInd = 0;
        private List<Integer> outStart = new ArrayList();
        private List<Integer> outEnd = new ArrayList();
        private List<Boolean> reversed = new ArrayList();

        public ReversedCharsIterator(List<int[]> reversedRange, GlyphLine line) {
            if (reversedRange != null) {
                if (reversedRange.get(0)[0] > 0) {
                    this.outStart.add(0);
                    this.outEnd.add(Integer.valueOf(reversedRange.get(0)[0]));
                    this.reversed.add(false);
                }
                for (int i = 0; i < reversedRange.size(); i++) {
                    int[] range = reversedRange.get(i);
                    this.outStart.add(Integer.valueOf(range[0]));
                    this.outEnd.add(Integer.valueOf(range[1] + 1));
                    this.reversed.add(true);
                    if (i != reversedRange.size() - 1) {
                        this.outStart.add(Integer.valueOf(range[1] + 1));
                        this.outEnd.add(Integer.valueOf(reversedRange.get(i + 1)[0]));
                        this.reversed.add(false);
                    }
                }
                int lastIndex = reversedRange.get(reversedRange.size() - 1)[1];
                if (lastIndex < line.size() - 1) {
                    this.outStart.add(Integer.valueOf(lastIndex + 1));
                    this.outEnd.add(Integer.valueOf(line.size()));
                    this.reversed.add(false);
                    return;
                }
                return;
            }
            this.outStart.add(Integer.valueOf(line.start));
            this.outEnd.add(Integer.valueOf(line.end));
            this.reversed.add(false);
        }

        public ReversedCharsIterator setUseReversed(boolean useReversed) {
            this.useReversed = useReversed;
            return this;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.currentInd < this.outStart.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public GlyphLine.GlyphLinePart next() {
            GlyphLine.GlyphLinePart part = new GlyphLine.GlyphLinePart(this.outStart.get(this.currentInd).intValue(), this.outEnd.get(this.currentInd).intValue()).setReversed(this.useReversed && this.reversed.get(this.currentInd).booleanValue());
            this.currentInd++;
            return part;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new IllegalStateException("Operation not supported");
        }
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TextRenderer$ScriptRange.class */
    private static class ScriptRange {
        Character.UnicodeScript script;
        int rangeEnd;

        ScriptRange(Character.UnicodeScript script, int rangeEnd) {
            this.script = script;
            this.rangeEnd = rangeEnd;
        }
    }
}
