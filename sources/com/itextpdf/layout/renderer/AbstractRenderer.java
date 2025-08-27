package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.NumberUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontCharacteristics;
import com.itextpdf.layout.font.FontFamilySplitter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.PositionedLayoutContext;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.Background;
import com.itextpdf.layout.property.BackgroundImage;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.BoxSizingPropertyValue;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.Transform;
import com.itextpdf.layout.property.TransparentColor;
import com.itextpdf.layout.property.UnitValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/AbstractRenderer.class */
public abstract class AbstractRenderer implements IRenderer {
    protected static final float EPS = 1.0E-4f;
    protected static final float INF = 1000000.0f;
    protected List<IRenderer> childRenderers;
    protected List<IRenderer> positionedRenderers;
    protected IPropertyContainer modelElement;
    protected boolean flushed;
    protected LayoutArea occupiedArea;
    protected IRenderer parent;
    protected Map<Integer, Object> properties;
    protected boolean isLastRendererForModelElement;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractRenderer.class.desiredAssertionStatus();
    }

    protected AbstractRenderer() {
        this.childRenderers = new ArrayList();
        this.positionedRenderers = new ArrayList();
        this.flushed = false;
        this.properties = new HashMap();
        this.isLastRendererForModelElement = true;
    }

    protected AbstractRenderer(IElement modelElement) {
        this.childRenderers = new ArrayList();
        this.positionedRenderers = new ArrayList();
        this.flushed = false;
        this.properties = new HashMap();
        this.isLastRendererForModelElement = true;
        this.modelElement = modelElement;
    }

    protected AbstractRenderer(AbstractRenderer other) {
        this.childRenderers = new ArrayList();
        this.positionedRenderers = new ArrayList();
        this.flushed = false;
        this.properties = new HashMap();
        this.isLastRendererForModelElement = true;
        this.childRenderers = other.childRenderers;
        this.positionedRenderers = other.positionedRenderers;
        this.modelElement = other.modelElement;
        this.flushed = other.flushed;
        this.occupiedArea = other.occupiedArea != null ? other.occupiedArea.mo950clone() : null;
        this.parent = other.parent;
        this.properties.putAll(other.properties);
        this.isLastRendererForModelElement = other.isLastRendererForModelElement;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void addChild(IRenderer renderer) {
        AbstractRenderer root;
        Integer positioning = (Integer) renderer.getProperty(52);
        if (positioning == null || positioning.intValue() == 2 || positioning.intValue() == 1) {
            this.childRenderers.add(renderer);
        } else if (positioning.intValue() == 4) {
            AbstractRenderer abstractRenderer = this;
            while (true) {
                root = abstractRenderer;
                if (!(root.parent instanceof AbstractRenderer)) {
                    break;
                } else {
                    abstractRenderer = (AbstractRenderer) root.parent;
                }
            }
            if (root == this) {
                this.positionedRenderers.add(renderer);
            } else {
                root.addChild(renderer);
            }
        } else if (positioning.intValue() == 3) {
            AbstractRenderer positionedParent = this;
            boolean noPositionInfo = noAbsolutePositionInfo(renderer);
            while (!positionedParent.isPositioned() && !noPositionInfo) {
                IRenderer parent = positionedParent.parent;
                if (!(parent instanceof AbstractRenderer)) {
                    break;
                } else {
                    positionedParent = (AbstractRenderer) parent;
                }
            }
            if (positionedParent == this) {
                this.positionedRenderers.add(renderer);
            } else {
                positionedParent.addChild(renderer);
            }
        }
        if ((renderer instanceof AbstractRenderer) && !((AbstractRenderer) renderer).isPositioned() && ((AbstractRenderer) renderer).positionedRenderers.size() > 0) {
            int pos = 0;
            List<IRenderer> childPositionedRenderers = ((AbstractRenderer) renderer).positionedRenderers;
            while (pos < childPositionedRenderers.size()) {
                if (noAbsolutePositionInfo(childPositionedRenderers.get(pos))) {
                    pos++;
                } else {
                    this.positionedRenderers.add(childPositionedRenderers.get(pos));
                    childPositionedRenderers.remove(pos);
                }
            }
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IPropertyContainer getModelElement() {
        return this.modelElement;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public List<IRenderer> getChildRenderers() {
        return this.childRenderers;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public boolean hasProperty(int property) {
        return hasOwnProperty(property) || (this.modelElement != null && this.modelElement.hasProperty(property)) || (this.parent != null && Property.isPropertyInherited(property) && this.parent.hasProperty(property));
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public boolean hasOwnProperty(int property) {
        return this.properties.containsKey(Integer.valueOf(property));
    }

    public boolean hasOwnOrModelProperty(int property) {
        return hasOwnOrModelProperty(this, property);
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public void deleteOwnProperty(int property) {
        this.properties.remove(Integer.valueOf(property));
    }

    public void deleteProperty(int property) {
        if (this.properties.containsKey(Integer.valueOf(property))) {
            this.properties.remove(Integer.valueOf(property));
        } else if (this.modelElement != null) {
            this.modelElement.deleteOwnProperty(property);
        }
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getProperty(int i) {
        T1 t1;
        T1 t12;
        T1 t13 = (T1) this.properties.get(Integer.valueOf(i));
        if (t13 != null || this.properties.containsKey(Integer.valueOf(i))) {
            return t13;
        }
        if (this.modelElement != null && ((t12 = (T1) this.modelElement.getProperty(i)) != null || this.modelElement.hasProperty(i))) {
            return t12;
        }
        if (this.parent != null && Property.isPropertyInherited(i) && (t1 = (T1) this.parent.getProperty(i)) != null) {
            return t1;
        }
        T1 t14 = (T1) getDefaultProperty(i);
        if (t14 != null) {
            return t14;
        }
        if (this.modelElement != null) {
            return (T1) this.modelElement.getDefaultProperty(i);
        }
        return null;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getOwnProperty(int i) {
        return (T1) this.properties.get(Integer.valueOf(i));
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public <T1> T1 getProperty(int i, T1 t1) {
        T1 t12 = (T1) getProperty(i);
        return t12 != null ? t12 : t1;
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public void setProperty(int property, Object value) {
        this.properties.put(Integer.valueOf(property), value);
    }

    @Override // com.itextpdf.layout.IPropertyContainer
    public <T1> T1 getDefaultProperty(int property) {
        return null;
    }

    public PdfFont getPropertyAsFont(int property) {
        return (PdfFont) getProperty(property);
    }

    public Color getPropertyAsColor(int property) {
        return (Color) getProperty(property);
    }

    public TransparentColor getPropertyAsTransparentColor(int property) {
        return (TransparentColor) getProperty(property);
    }

    public Float getPropertyAsFloat(int property) {
        return NumberUtil.asFloat(getProperty(property));
    }

    public Float getPropertyAsFloat(int property, Float defaultValue) {
        return NumberUtil.asFloat(getProperty(property, defaultValue));
    }

    public Boolean getPropertyAsBoolean(int property) {
        return (Boolean) getProperty(property);
    }

    public UnitValue getPropertyAsUnitValue(int property) {
        return (UnitValue) getProperty(property);
    }

    public Integer getPropertyAsInteger(int property) {
        return NumberUtil.asInteger(getProperty(property));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IRenderer renderer : this.childRenderers) {
            sb.append(renderer.toString());
        }
        return sb.toString();
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutArea getOccupiedArea() {
        return this.occupiedArea;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        applyDestinationsAndAnnotation(drawContext);
        boolean relativePosition = isRelativePosition();
        if (relativePosition) {
            applyRelativePositioningTranslation(false);
        }
        beginElementOpacityApplying(drawContext);
        drawBackground(drawContext);
        drawBorder(drawContext);
        drawChildren(drawContext);
        drawPositionedChildren(drawContext);
        endElementOpacityApplying(drawContext);
        if (relativePosition) {
            applyRelativePositioningTranslation(true);
        }
        this.flushed = true;
    }

    protected void beginElementOpacityApplying(DrawContext drawContext) {
        Float opacity = getPropertyAsFloat(92);
        if (opacity != null && opacity.floatValue() < 1.0f) {
            PdfExtGState extGState = new PdfExtGState();
            extGState.setStrokeOpacity(opacity.floatValue()).setFillOpacity(opacity.floatValue());
            drawContext.getCanvas().saveState().setExtGState(extGState);
        }
    }

    protected void endElementOpacityApplying(DrawContext drawContext) {
        Float opacity = getPropertyAsFloat(92);
        if (opacity != null && opacity.floatValue() < 1.0f) {
            drawContext.getCanvas().restoreState();
        }
    }

    public void drawBackground(DrawContext drawContext) {
        Background background = (Background) getProperty(6);
        BackgroundImage backgroundImage = (BackgroundImage) getProperty(90);
        if (background != null || backgroundImage != null) {
            Rectangle bBox = getOccupiedAreaBBox();
            boolean isTagged = drawContext.isTaggingEnabled();
            if (isTagged) {
                drawContext.getCanvas().openTag(new CanvasArtifact());
            }
            Rectangle backgroundArea = applyMargins(bBox, false);
            if (backgroundArea.getWidth() <= 0.0f || backgroundArea.getHeight() <= 0.0f) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.RECTANGLE_HAS_NEGATIVE_OR_ZERO_SIZES, "background"));
            } else {
                boolean backgroundAreaIsClipped = false;
                if (background != null) {
                    backgroundAreaIsClipped = clipBackgroundArea(drawContext, backgroundArea);
                    TransparentColor backgroundColor = new TransparentColor(background.getColor(), background.getOpacity());
                    drawContext.getCanvas().saveState().setFillColor(backgroundColor.getColor());
                    backgroundColor.applyFillTransparency(drawContext.getCanvas());
                    drawContext.getCanvas().rectangle(backgroundArea.getX() - background.getExtraLeft(), backgroundArea.getY() - background.getExtraBottom(), backgroundArea.getWidth() + background.getExtraLeft() + background.getExtraRight(), backgroundArea.getHeight() + background.getExtraTop() + background.getExtraBottom()).fill().restoreState();
                }
                if (backgroundImage != null && backgroundImage.isBackgroundSpecified()) {
                    if (!backgroundAreaIsClipped) {
                        backgroundAreaIsClipped = clipBackgroundArea(drawContext, backgroundArea);
                    }
                    applyBorderBox(backgroundArea, false);
                    PdfXObject backgroundXObject = backgroundImage.getImage();
                    if (backgroundXObject == null) {
                        backgroundXObject = backgroundImage.getForm();
                    }
                    Rectangle imageRectangle = new Rectangle(backgroundArea.getX(), backgroundArea.getTop() - backgroundXObject.getHeight(), backgroundXObject.getWidth(), backgroundXObject.getHeight());
                    if (imageRectangle.getWidth() <= 0.0f || imageRectangle.getHeight() <= 0.0f) {
                        Logger logger2 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                        logger2.warn(MessageFormatUtil.format(LogMessageConstant.RECTANGLE_HAS_NEGATIVE_OR_ZERO_SIZES, "background-image"));
                    } else {
                        applyBorderBox(backgroundArea, true);
                        drawContext.getCanvas().saveState().rectangle(backgroundArea).clip().endPath();
                        float initialX = backgroundImage.isRepeatX() ? imageRectangle.getX() - imageRectangle.getWidth() : imageRectangle.getX();
                        float initialY = backgroundImage.isRepeatY() ? imageRectangle.getTop() : imageRectangle.getY();
                        imageRectangle.setY(initialY);
                        do {
                            imageRectangle.setX(initialX);
                            do {
                                drawContext.getCanvas().addXObject(backgroundXObject, imageRectangle);
                                imageRectangle.moveRight(imageRectangle.getWidth());
                                if (!backgroundImage.isRepeatX()) {
                                    break;
                                }
                            } while (imageRectangle.getLeft() < backgroundArea.getRight());
                            imageRectangle.moveDown(imageRectangle.getHeight());
                            if (!backgroundImage.isRepeatY()) {
                                break;
                            }
                        } while (imageRectangle.getTop() > backgroundArea.getBottom());
                        drawContext.getCanvas().restoreState();
                    }
                }
                if (backgroundAreaIsClipped) {
                    drawContext.getCanvas().restoreState();
                }
            }
            if (isTagged) {
                drawContext.getCanvas().closeTag();
            }
        }
    }

    protected boolean clipBorderArea(DrawContext drawContext, Rectangle outerBorderBox) {
        return clipArea(drawContext, outerBorderBox, true, true, false, true);
    }

    protected boolean clipBackgroundArea(DrawContext drawContext, Rectangle outerBorderBox) {
        return clipArea(drawContext, outerBorderBox, true, false, false, false);
    }

    protected boolean clipBackgroundArea(DrawContext drawContext, Rectangle outerBorderBox, boolean considerBordersBeforeClipping) {
        return clipArea(drawContext, outerBorderBox, true, false, considerBordersBeforeClipping, false);
    }

    private boolean clipArea(DrawContext drawContext, Rectangle outerBorderBox, boolean clipOuter, boolean clipInner, boolean considerBordersBeforeOuterClipping, boolean considerBordersBeforeInnerClipping) {
        if (!$assertionsDisabled && false != considerBordersBeforeOuterClipping && false != considerBordersBeforeInnerClipping) {
            throw new AssertionError();
        }
        float[] borderWidths = {0.0f, 0.0f, 0.0f, 0.0f};
        float[] outerBox = {outerBorderBox.getTop(), outerBorderBox.getRight(), outerBorderBox.getBottom(), outerBorderBox.getLeft()};
        boolean hasNotNullRadius = false;
        BorderRadius[] borderRadii = getBorderRadii();
        float[] verticalRadii = calculateRadii(borderRadii, outerBorderBox, false);
        float[] horizontalRadii = calculateRadii(borderRadii, outerBorderBox, true);
        for (int i = 0; i < 4; i++) {
            verticalRadii[i] = Math.min(verticalRadii[i], outerBorderBox.getHeight() / 2.0f);
            horizontalRadii[i] = Math.min(horizontalRadii[i], outerBorderBox.getWidth() / 2.0f);
            if (!hasNotNullRadius && (0.0f != verticalRadii[i] || 0.0f != horizontalRadii[i])) {
                hasNotNullRadius = true;
            }
        }
        if (hasNotNullRadius) {
            float[] cornersX = {outerBox[3] + horizontalRadii[0], outerBox[1] - horizontalRadii[1], outerBox[1] - horizontalRadii[2], outerBox[3] + horizontalRadii[3]};
            float[] cornersY = {outerBox[0] - verticalRadii[0], outerBox[0] - verticalRadii[1], outerBox[2] + verticalRadii[2], outerBox[2] + verticalRadii[3]};
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.saveState();
            if (considerBordersBeforeOuterClipping) {
                borderWidths = decreaseBorderRadiiWithBorders(horizontalRadii, verticalRadii, outerBox, cornersX, cornersY);
            }
            if (clipOuter) {
                clipOuterArea(canvas, 0.44769999384880066d, horizontalRadii, verticalRadii, outerBox, cornersX, cornersY);
            }
            if (considerBordersBeforeInnerClipping) {
                borderWidths = decreaseBorderRadiiWithBorders(horizontalRadii, verticalRadii, outerBox, cornersX, cornersY);
            }
            if (clipInner) {
                clipInnerArea(canvas, 0.44769999384880066d, horizontalRadii, verticalRadii, outerBox, cornersX, cornersY, borderWidths);
            }
        }
        return hasNotNullRadius;
    }

    private void clipOuterArea(PdfCanvas canvas, double curv, float[] horizontalRadii, float[] verticalRadii, float[] outerBox, float[] cornersX, float[] cornersY) {
        float top = outerBox[0];
        float right = outerBox[1];
        float bottom = outerBox[2];
        float left = outerBox[3];
        float x1 = cornersX[0];
        float y1 = cornersY[0];
        float x2 = cornersX[1];
        float y2 = cornersY[1];
        float x3 = cornersX[2];
        float y3 = cornersY[2];
        float x4 = cornersX[3];
        float y4 = cornersY[3];
        if (0.0f != horizontalRadii[0] || 0.0f != verticalRadii[0]) {
            canvas.moveTo(left, bottom).lineTo(left, y1).curveTo(left, y1 + (verticalRadii[0] * curv), x1 - (horizontalRadii[0] * curv), top, x1, top).lineTo(right, top).lineTo(right, bottom).lineTo(left, bottom);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[1] || 0.0f != verticalRadii[1]) {
            canvas.moveTo(left, top).lineTo(x2, top).curveTo(x2 + (horizontalRadii[1] * curv), top, right, y2 + (verticalRadii[1] * curv), right, y2).lineTo(right, bottom).lineTo(left, bottom).lineTo(left, top);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[2] || 0.0f != verticalRadii[2]) {
            canvas.moveTo(right, top).lineTo(right, y3).curveTo(right, y3 - (verticalRadii[2] * curv), x3 + (horizontalRadii[2] * curv), bottom, x3, bottom).lineTo(left, bottom).lineTo(left, top).lineTo(right, top);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[3] || 0.0f != verticalRadii[3]) {
            canvas.moveTo(right, bottom).lineTo(x4, bottom).curveTo(x4 - (horizontalRadii[3] * curv), bottom, left, y4 - (verticalRadii[3] * curv), left, y4).lineTo(left, top).lineTo(right, top).lineTo(right, bottom);
            canvas.clip().endPath();
        }
    }

    private void clipInnerArea(PdfCanvas canvas, double curv, float[] horizontalRadii, float[] verticalRadii, float[] outerBox, float[] cornersX, float[] cornersY, float[] borderWidths) {
        float top = outerBox[0];
        float right = outerBox[1];
        float bottom = outerBox[2];
        float left = outerBox[3];
        float x1 = cornersX[0];
        float y1 = cornersY[0];
        float x2 = cornersX[1];
        float y2 = cornersY[1];
        float x3 = cornersX[2];
        float y3 = cornersY[2];
        float x4 = cornersX[3];
        float y4 = cornersY[3];
        float topBorderWidth = borderWidths[0];
        float rightBorderWidth = borderWidths[1];
        float bottomBorderWidth = borderWidths[2];
        float leftBorderWidth = borderWidths[3];
        if (0.0f != horizontalRadii[0] || 0.0f != verticalRadii[0]) {
            canvas.moveTo(left, y1).curveTo(left, y1 + (verticalRadii[0] * curv), x1 - (horizontalRadii[0] * curv), top, x1, top).lineTo(x2, top).lineTo(right, y2).lineTo(right, y3).lineTo(x3, bottom).lineTo(x4, bottom).lineTo(left, y4).lineTo(left, y1).lineTo(left - leftBorderWidth, y1).lineTo(left - leftBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, y1);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[1] || 0.0f != verticalRadii[1]) {
            canvas.moveTo(x2, top).curveTo(x2 + (horizontalRadii[1] * curv), top, right, y2 + (verticalRadii[1] * curv), right, y2).lineTo(right, y3).lineTo(x3, bottom).lineTo(x4, bottom).lineTo(left, y4).lineTo(left, y1).lineTo(x1, top).lineTo(x2, top).lineTo(x2, top + topBorderWidth).lineTo(left - leftBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, top + topBorderWidth).lineTo(x2, top + topBorderWidth);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[2] || 0.0f != verticalRadii[2]) {
            canvas.moveTo(right, y3).curveTo(right, y3 - (verticalRadii[2] * curv), x3 + (horizontalRadii[2] * curv), bottom, x3, bottom).lineTo(x4, bottom).lineTo(left, y4).lineTo(left, y1).lineTo(x1, top).lineTo(x2, top).lineTo(right, y2).lineTo(right, y3).lineTo(right + rightBorderWidth, y3).lineTo(right + rightBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, y3);
            canvas.clip().endPath();
        }
        if (0.0f != horizontalRadii[3] || 0.0f != verticalRadii[3]) {
            canvas.moveTo(x4, bottom).curveTo(x4 - (horizontalRadii[3] * curv), bottom, left, y4 - (verticalRadii[3] * curv), left, y4).lineTo(left, y1).lineTo(x1, top).lineTo(x2, top).lineTo(right, y2).lineTo(right, y3).lineTo(x3, bottom).lineTo(x4, bottom).lineTo(x4, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, bottom - bottomBorderWidth).lineTo(right + rightBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, top + topBorderWidth).lineTo(left - leftBorderWidth, bottom - bottomBorderWidth).lineTo(x4, bottom - bottomBorderWidth);
            canvas.clip().endPath();
        }
    }

    private float[] decreaseBorderRadiiWithBorders(float[] horizontalRadii, float[] verticalRadii, float[] outerBox, float[] cornersX, float[] cornersY) {
        Border[] borders = getBorders();
        float[] borderWidths = {0.0f, 0.0f, 0.0f, 0.0f};
        if (borders[0] != null) {
            borderWidths[0] = borders[0].getWidth();
            outerBox[0] = outerBox[0] - borders[0].getWidth();
            if (cornersY[1] > outerBox[0]) {
                cornersY[1] = outerBox[0];
            }
            if (cornersY[0] > outerBox[0]) {
                cornersY[0] = outerBox[0];
            }
            verticalRadii[0] = Math.max(0.0f, verticalRadii[0] - borders[0].getWidth());
            verticalRadii[1] = Math.max(0.0f, verticalRadii[1] - borders[0].getWidth());
        }
        if (borders[1] != null) {
            borderWidths[1] = borders[1].getWidth();
            outerBox[1] = outerBox[1] - borders[1].getWidth();
            if (cornersX[1] > outerBox[1]) {
                cornersX[1] = outerBox[1];
            }
            if (cornersX[2] > outerBox[1]) {
                cornersX[2] = outerBox[1];
            }
            horizontalRadii[1] = Math.max(0.0f, horizontalRadii[1] - borders[1].getWidth());
            horizontalRadii[2] = Math.max(0.0f, horizontalRadii[2] - borders[1].getWidth());
        }
        if (borders[2] != null) {
            borderWidths[2] = borders[2].getWidth();
            outerBox[2] = outerBox[2] + borders[2].getWidth();
            if (cornersY[2] < outerBox[2]) {
                cornersY[2] = outerBox[2];
            }
            if (cornersY[3] < outerBox[2]) {
                cornersY[3] = outerBox[2];
            }
            verticalRadii[2] = Math.max(0.0f, verticalRadii[2] - borders[2].getWidth());
            verticalRadii[3] = Math.max(0.0f, verticalRadii[3] - borders[2].getWidth());
        }
        if (borders[3] != null) {
            borderWidths[3] = borders[3].getWidth();
            outerBox[3] = outerBox[3] + borders[3].getWidth();
            if (cornersX[3] < outerBox[3]) {
                cornersX[3] = outerBox[3];
            }
            if (cornersX[0] < outerBox[3]) {
                cornersX[0] = outerBox[3];
            }
            horizontalRadii[3] = Math.max(0.0f, horizontalRadii[3] - borders[3].getWidth());
            horizontalRadii[0] = Math.max(0.0f, horizontalRadii[0] - borders[3].getWidth());
        }
        return borderWidths;
    }

    public void drawChildren(DrawContext drawContext) {
        List<IRenderer> waitingRenderers = new ArrayList<>();
        for (IRenderer child : this.childRenderers) {
            Transform transformProp = (Transform) child.getProperty(53);
            RootRenderer rootRenderer = getRootRenderer();
            List<IRenderer> waiting = (rootRenderer == null || rootRenderer.waitingDrawingElements.contains(child)) ? waitingRenderers : rootRenderer.waitingDrawingElements;
            processWaitingDrawing(child, transformProp, waiting);
            if (!FloatingHelper.isRendererFloating(child) && transformProp == null) {
                child.draw(drawContext);
            }
        }
        for (IRenderer waitingRenderer : waitingRenderers) {
            waitingRenderer.draw(drawContext);
        }
    }

    public void drawBorder(DrawContext drawContext) {
        Border[] borders = getBorders();
        boolean gotBorders = false;
        int length = borders.length;
        for (int i = 0; i < length; i++) {
            Border border = borders[i];
            gotBorders = gotBorders || border != null;
        }
        if (gotBorders) {
            float topWidth = borders[0] != null ? borders[0].getWidth() : 0.0f;
            float rightWidth = borders[1] != null ? borders[1].getWidth() : 0.0f;
            float bottomWidth = borders[2] != null ? borders[2].getWidth() : 0.0f;
            float leftWidth = borders[3] != null ? borders[3].getWidth() : 0.0f;
            Rectangle bBox = getBorderAreaBBox();
            if (bBox.getWidth() < 0.0f || bBox.getHeight() < 0.0f) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.RECTANGLE_HAS_NEGATIVE_SIZE, "border"));
                return;
            }
            float x1 = bBox.getX();
            float y1 = bBox.getY();
            float x2 = bBox.getX() + bBox.getWidth();
            float y2 = bBox.getY() + bBox.getHeight();
            boolean isTagged = drawContext.isTaggingEnabled();
            PdfCanvas canvas = drawContext.getCanvas();
            if (isTagged) {
                canvas.openTag(new CanvasArtifact());
            }
            Rectangle borderRect = applyMargins(this.occupiedArea.getBBox().mo825clone(), getMargins(), false);
            boolean isAreaClipped = clipBorderArea(drawContext, borderRect);
            BorderRadius[] borderRadii = getBorderRadii();
            float[] verticalRadii = calculateRadii(borderRadii, borderRect, false);
            float[] horizontalRadii = calculateRadii(borderRadii, borderRect, true);
            for (int i2 = 0; i2 < 4; i2++) {
                verticalRadii[i2] = Math.min(verticalRadii[i2], borderRect.getHeight() / 2.0f);
                horizontalRadii[i2] = Math.min(horizontalRadii[i2], borderRect.getWidth() / 2.0f);
            }
            if (borders[0] != null) {
                if (0.0f != horizontalRadii[0] || 0.0f != verticalRadii[0] || 0.0f != horizontalRadii[1] || 0.0f != verticalRadii[1]) {
                    borders[0].draw(canvas, x1, y2, x2, y2, horizontalRadii[0], verticalRadii[0], horizontalRadii[1], verticalRadii[1], Border.Side.TOP, leftWidth, rightWidth);
                } else {
                    borders[0].draw(canvas, x1, y2, x2, y2, Border.Side.TOP, leftWidth, rightWidth);
                }
            }
            if (borders[1] != null) {
                if (0.0f != horizontalRadii[1] || 0.0f != verticalRadii[1] || 0.0f != horizontalRadii[2] || 0.0f != verticalRadii[2]) {
                    borders[1].draw(canvas, x2, y2, x2, y1, horizontalRadii[1], verticalRadii[1], horizontalRadii[2], verticalRadii[2], Border.Side.RIGHT, topWidth, bottomWidth);
                } else {
                    borders[1].draw(canvas, x2, y2, x2, y1, Border.Side.RIGHT, topWidth, bottomWidth);
                }
            }
            if (borders[2] != null) {
                if (0.0f != horizontalRadii[2] || 0.0f != verticalRadii[2] || 0.0f != horizontalRadii[3] || 0.0f != verticalRadii[3]) {
                    borders[2].draw(canvas, x2, y1, x1, y1, horizontalRadii[2], verticalRadii[2], horizontalRadii[3], verticalRadii[3], Border.Side.BOTTOM, rightWidth, leftWidth);
                } else {
                    borders[2].draw(canvas, x2, y1, x1, y1, Border.Side.BOTTOM, rightWidth, leftWidth);
                }
            }
            if (borders[3] != null) {
                if (0.0f != horizontalRadii[3] || 0.0f != verticalRadii[3] || 0.0f != horizontalRadii[0] || 0.0f != verticalRadii[0]) {
                    borders[3].draw(canvas, x1, y1, x1, y2, horizontalRadii[3], verticalRadii[3], horizontalRadii[0], verticalRadii[0], Border.Side.LEFT, bottomWidth, topWidth);
                } else {
                    borders[3].draw(canvas, x1, y1, x1, y2, Border.Side.LEFT, bottomWidth, topWidth);
                }
            }
            if (isAreaClipped) {
                drawContext.getCanvas().restoreState();
            }
            if (isTagged) {
                canvas.closeTag();
            }
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public boolean isFlushed() {
        return this.flushed;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer setParent(IRenderer parent) {
        this.parent = parent;
        return this;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getParent() {
        return this.parent;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public void move(float dxRight, float dyUp) {
        this.occupiedArea.getBBox().moveRight(dxRight);
        this.occupiedArea.getBBox().moveUp(dyUp);
        for (IRenderer childRenderer : this.childRenderers) {
            childRenderer.move(dxRight, dyUp);
        }
        for (IRenderer childRenderer2 : this.positionedRenderers) {
            childRenderer2.move(dxRight, dyUp);
        }
    }

    public List<Rectangle> initElementAreas(LayoutArea area) {
        return Collections.singletonList(area.getBBox());
    }

    public Rectangle getOccupiedAreaBBox() {
        return this.occupiedArea.getBBox().mo825clone();
    }

    public Rectangle getBorderAreaBBox() {
        Rectangle rect = getOccupiedAreaBBox();
        applyMargins(rect, false);
        applyBorderBox(rect, false);
        return rect;
    }

    public Rectangle getInnerAreaBBox() {
        Rectangle rect = getOccupiedAreaBBox();
        applyMargins(rect, false);
        applyBorderBox(rect, false);
        applyPaddings(rect, false);
        return rect;
    }

    public Rectangle applyMargins(Rectangle rect, boolean reverse) {
        return applyMargins(rect, getMargins(), reverse);
    }

    public Rectangle applyBorderBox(Rectangle rect, boolean reverse) {
        Border[] borders = getBorders();
        return applyBorderBox(rect, borders, reverse);
    }

    public Rectangle applyPaddings(Rectangle rect, boolean reverse) {
        return applyPaddings(rect, getPaddings(), reverse);
    }

    public boolean isFirstOnRootArea() {
        return isFirstOnRootArea(false);
    }

    protected void applyDestinationsAndAnnotation(DrawContext drawContext) {
        applyDestination(drawContext.getDocument());
        applyAction(drawContext.getDocument());
        applyLinkAnnotation(drawContext.getDocument());
    }

    protected static boolean isBorderBoxSizing(IRenderer renderer) {
        BoxSizingPropertyValue boxSizing = (BoxSizingPropertyValue) renderer.getProperty(105);
        return boxSizing != null && boxSizing.equals(BoxSizingPropertyValue.BORDER_BOX);
    }

    protected boolean isOverflowProperty(OverflowPropertyValue equalsTo, int overflowProperty) {
        return isOverflowProperty(equalsTo, (OverflowPropertyValue) getProperty(overflowProperty));
    }

    protected static boolean isOverflowProperty(OverflowPropertyValue equalsTo, IRenderer renderer, int overflowProperty) {
        return isOverflowProperty(equalsTo, (OverflowPropertyValue) renderer.getProperty(overflowProperty));
    }

    protected static boolean isOverflowProperty(OverflowPropertyValue equalsTo, OverflowPropertyValue rendererOverflowProperty) {
        return equalsTo.equals(rendererOverflowProperty) || (equalsTo.equals(OverflowPropertyValue.FIT) && rendererOverflowProperty == null);
    }

    protected static boolean isOverflowFit(OverflowPropertyValue rendererOverflowProperty) {
        return rendererOverflowProperty == null || OverflowPropertyValue.FIT.equals(rendererOverflowProperty);
    }

    static void processWaitingDrawing(IRenderer child, Transform transformProp, List<IRenderer> waitingDrawing) {
        if (FloatingHelper.isRendererFloating(child) || transformProp != null) {
            waitingDrawing.add(child);
        }
        Border outlineProp = (Border) child.getProperty(106);
        if (outlineProp != null && (child instanceof AbstractRenderer)) {
            AbstractRenderer abstractChild = (AbstractRenderer) child;
            if (abstractChild.isRelativePosition()) {
                abstractChild.applyRelativePositioningTranslation(false);
            }
            Div outlines = new Div();
            outlines.getAccessibilityProperties().setRole(null);
            if (transformProp != null) {
                outlines.setProperty(53, transformProp);
            }
            outlines.setProperty(9, outlineProp);
            float offset = ((Border) outlines.getProperty(9)).getWidth();
            if (abstractChild.getPropertyAsFloat(107) != null) {
                offset += abstractChild.getPropertyAsFloat(107).floatValue();
            }
            DivRenderer div = new DivRenderer(outlines);
            div.setParent(abstractChild.getParent());
            Rectangle divOccupiedArea = abstractChild.applyMargins(abstractChild.occupiedArea.mo950clone().getBBox(), false).moveLeft(offset).moveDown(offset);
            divOccupiedArea.setWidth(divOccupiedArea.getWidth() + (2.0f * offset)).setHeight(divOccupiedArea.getHeight() + (2.0f * offset));
            div.occupiedArea = new LayoutArea(abstractChild.getOccupiedArea().getPageNumber(), divOccupiedArea);
            float outlineWidth = ((Border) div.getProperty(9)).getWidth();
            if (divOccupiedArea.getWidth() >= outlineWidth * 2.0f && divOccupiedArea.getHeight() >= outlineWidth * 2.0f) {
                waitingDrawing.add(div);
            }
            if (abstractChild.isRelativePosition()) {
                abstractChild.applyRelativePositioningTranslation(true);
            }
        }
    }

    protected Float retrieveWidth(float parentBoxWidth) {
        Float minWidth = retrieveUnitValue(parentBoxWidth, 80);
        Float maxWidth = retrieveUnitValue(parentBoxWidth, 79);
        if (maxWidth != null && minWidth != null && minWidth.floatValue() > maxWidth.floatValue()) {
            maxWidth = minWidth;
        }
        Float width = retrieveUnitValue(parentBoxWidth, 77);
        if (width != null) {
            if (maxWidth != null) {
                width = width.floatValue() > maxWidth.floatValue() ? maxWidth : width;
            }
            if (minWidth != null) {
                width = width.floatValue() < minWidth.floatValue() ? minWidth : width;
            }
        } else if (maxWidth != null) {
            width = maxWidth.floatValue() < parentBoxWidth ? maxWidth : null;
        }
        if (width != null && isBorderBoxSizing(this)) {
            width = Float.valueOf(width.floatValue() - calculatePaddingBorderWidth(this));
        }
        if (width != null) {
            return Float.valueOf(Math.max(0.0f, width.floatValue()));
        }
        return null;
    }

    protected Float retrieveMaxWidth(float parentBoxWidth) {
        Float maxWidth = retrieveUnitValue(parentBoxWidth, 79);
        if (maxWidth != null) {
            Float minWidth = retrieveUnitValue(parentBoxWidth, 80);
            if (minWidth != null && minWidth.floatValue() > maxWidth.floatValue()) {
                maxWidth = minWidth;
            }
            if (isBorderBoxSizing(this)) {
                maxWidth = Float.valueOf(maxWidth.floatValue() - calculatePaddingBorderWidth(this));
            }
            return Float.valueOf(maxWidth.floatValue() > 0.0f ? maxWidth.floatValue() : 0.0f);
        }
        return null;
    }

    protected Float retrieveMinWidth(float parentBoxWidth) {
        Float minWidth = retrieveUnitValue(parentBoxWidth, 80);
        if (minWidth != null) {
            if (isBorderBoxSizing(this)) {
                minWidth = Float.valueOf(minWidth.floatValue() - calculatePaddingBorderWidth(this));
            }
            return Float.valueOf(minWidth.floatValue() > 0.0f ? minWidth.floatValue() : 0.0f);
        }
        return null;
    }

    protected void updateWidth(UnitValue updatedWidthValue) {
        if (updatedWidthValue.isPointValue() && isBorderBoxSizing(this)) {
            updatedWidthValue.setValue(updatedWidthValue.getValue() + calculatePaddingBorderWidth(this));
        }
        setProperty(77, updatedWidthValue);
    }

    protected Float retrieveHeight() {
        Float height = null;
        UnitValue heightUV = getPropertyAsUnitValue(27);
        Float parentResolvedHeight = retrieveResolvedParentDeclaredHeight();
        Float minHeight = null;
        Float maxHeight = null;
        if (heightUV != null) {
            if (parentResolvedHeight == null) {
                if (heightUV.isPercentValue()) {
                    height = null;
                } else {
                    UnitValue minHeightUV = getPropertyAsUnitValue(85);
                    if (minHeightUV != null && minHeightUV.isPointValue()) {
                        minHeight = Float.valueOf(minHeightUV.getValue());
                    }
                    UnitValue maxHeightUV = getPropertyAsUnitValue(84);
                    if (maxHeightUV != null && maxHeightUV.isPointValue()) {
                        maxHeight = Float.valueOf(maxHeightUV.getValue());
                    }
                    height = Float.valueOf(heightUV.getValue());
                }
            } else {
                minHeight = retrieveUnitValue(parentResolvedHeight.floatValue(), 85);
                maxHeight = retrieveUnitValue(parentResolvedHeight.floatValue(), 84);
                height = retrieveUnitValue(parentResolvedHeight.floatValue(), 27);
            }
            if (maxHeight != null && minHeight != null && minHeight.floatValue() > maxHeight.floatValue()) {
                maxHeight = minHeight;
            }
            if (height != null) {
                if (maxHeight != null) {
                    height = height.floatValue() > maxHeight.floatValue() ? maxHeight : height;
                }
                if (minHeight != null) {
                    height = height.floatValue() < minHeight.floatValue() ? minHeight : height;
                }
            }
            if (height != null && isBorderBoxSizing(this)) {
                height = Float.valueOf(height.floatValue() - calculatePaddingBorderHeight(this));
            }
        }
        if (height != null) {
            return Float.valueOf(Math.max(0.0f, height.floatValue()));
        }
        return null;
    }

    private float[] calculateRadii(BorderRadius[] radii, Rectangle area, boolean horizontal) {
        float[] results = new float[4];
        for (int i = 0; i < 4; i++) {
            if (null != radii[i]) {
                UnitValue value = horizontal ? radii[i].getHorizontalRadius() : radii[i].getVerticalRadius();
                if (value != null) {
                    if (value.getUnitType() == 2) {
                        results[i] = (value.getValue() * (horizontal ? area.getWidth() : area.getHeight())) / 100.0f;
                    } else {
                        if (!$assertionsDisabled && value.getUnitType() != 1) {
                            throw new AssertionError();
                        }
                        results[i] = value.getValue();
                    }
                } else {
                    results[i] = 0.0f;
                }
            } else {
                results[i] = 0.0f;
            }
        }
        return results;
    }

    protected void updateHeight(UnitValue updatedHeight) {
        if (isBorderBoxSizing(this) && updatedHeight.isPointValue()) {
            updatedHeight.setValue(updatedHeight.getValue() + calculatePaddingBorderHeight(this));
        }
        setProperty(27, updatedHeight);
    }

    protected Float retrieveMaxHeight() {
        Float maxHeight;
        Float minHeight = null;
        Float directParentDeclaredHeight = retrieveDirectParentDeclaredHeight();
        UnitValue maxHeightAsUV = getPropertyAsUnitValue(84);
        if (maxHeightAsUV != null) {
            if (directParentDeclaredHeight == null) {
                if (maxHeightAsUV.isPercentValue()) {
                    maxHeight = null;
                } else {
                    minHeight = retrieveMinHeight();
                    UnitValue minHeightUV = getPropertyAsUnitValue(85);
                    if (minHeightUV != null && minHeightUV.isPointValue()) {
                        minHeight = Float.valueOf(minHeightUV.getValue());
                    }
                    maxHeight = Float.valueOf(maxHeightAsUV.getValue());
                }
            } else {
                maxHeight = retrieveUnitValue(directParentDeclaredHeight.floatValue(), 84);
            }
            if (maxHeight != null) {
                if (minHeight != null && minHeight.floatValue() > maxHeight.floatValue()) {
                    maxHeight = minHeight;
                }
                if (isBorderBoxSizing(this)) {
                    maxHeight = Float.valueOf(maxHeight.floatValue() - calculatePaddingBorderHeight(this));
                }
                return Float.valueOf(maxHeight.floatValue() > 0.0f ? maxHeight.floatValue() : 0.0f);
            }
        }
        return retrieveHeight();
    }

    protected void updateMaxHeight(UnitValue updatedMaxHeight) {
        if (isBorderBoxSizing(this) && updatedMaxHeight.isPointValue()) {
            updatedMaxHeight.setValue(updatedMaxHeight.getValue() + calculatePaddingBorderHeight(this));
        }
        setProperty(84, updatedMaxHeight);
    }

    protected Float retrieveMinHeight() {
        Float minHeight;
        Float directParentDeclaredHeight = retrieveDirectParentDeclaredHeight();
        UnitValue minHeightUV = getPropertyAsUnitValue(this, 85);
        if (minHeightUV != null) {
            if (directParentDeclaredHeight == null) {
                if (minHeightUV.isPercentValue()) {
                    minHeight = null;
                } else {
                    minHeight = Float.valueOf(minHeightUV.getValue());
                }
            } else {
                minHeight = retrieveUnitValue(directParentDeclaredHeight.floatValue(), 85);
            }
            if (minHeight != null) {
                if (isBorderBoxSizing(this)) {
                    minHeight = Float.valueOf(minHeight.floatValue() - calculatePaddingBorderHeight(this));
                }
                return Float.valueOf(minHeight.floatValue() > 0.0f ? minHeight.floatValue() : 0.0f);
            }
        }
        return retrieveHeight();
    }

    protected void updateMinHeight(UnitValue updatedMinHeight) {
        if (isBorderBoxSizing(this) && updatedMinHeight.isPointValue()) {
            updatedMinHeight.setValue(updatedMinHeight.getValue() + calculatePaddingBorderHeight(this));
        }
        setProperty(85, updatedMinHeight);
    }

    protected Float retrieveUnitValue(float baseValue, int property) {
        return retrieveUnitValue(baseValue, property, false);
    }

    protected Float retrieveUnitValue(float baseValue, int property, boolean pointOnly) {
        UnitValue value = (UnitValue) getProperty(property);
        if (pointOnly && value.getUnitType() == 1) {
            Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, Integer.valueOf(property)));
        }
        if (value != null) {
            if (value.getUnitType() == 2) {
                return Float.valueOf(value.getValue() != 100.0f ? (baseValue * value.getValue()) / 100.0f : baseValue);
            }
            if ($assertionsDisabled || value.getUnitType() == 1) {
                return Float.valueOf(value.getValue());
            }
            throw new AssertionError();
        }
        return null;
    }

    protected Map<Integer, Object> getOwnProperties() {
        return this.properties;
    }

    protected void addAllProperties(Map<Integer, Object> properties) {
        this.properties.putAll(properties);
    }

    protected Float getFirstYLineRecursively() {
        if (this.childRenderers.size() == 0) {
            return null;
        }
        return ((AbstractRenderer) this.childRenderers.get(0)).getFirstYLineRecursively();
    }

    protected Float getLastYLineRecursively() {
        Float lastYLine;
        if (!allowLastYLineRecursiveExtraction()) {
            return null;
        }
        for (int i = this.childRenderers.size() - 1; i >= 0; i--) {
            IRenderer child = this.childRenderers.get(i);
            if ((child instanceof AbstractRenderer) && (lastYLine = ((AbstractRenderer) child).getLastYLineRecursively()) != null) {
                return lastYLine;
            }
        }
        return null;
    }

    protected boolean allowLastYLineRecursiveExtraction() {
        return (isOverflowProperty(OverflowPropertyValue.HIDDEN, 103) || isOverflowProperty(OverflowPropertyValue.HIDDEN, 104)) ? false : true;
    }

    protected Rectangle applyMargins(Rectangle rect, UnitValue[] margins, boolean reverse) {
        if (!margins[0].isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 46));
        }
        if (!margins[1].isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
        }
        if (!margins[2].isPointValue()) {
            Logger logger3 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger3.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 43));
        }
        if (!margins[3].isPointValue()) {
            Logger logger4 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger4.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        return rect.applyMargins(margins[0].getValue(), margins[1].getValue(), margins[2].getValue(), margins[3].getValue(), reverse);
    }

    protected UnitValue[] getMargins() {
        return getMargins(this);
    }

    protected UnitValue[] getPaddings() {
        return getPaddings(this);
    }

    protected Rectangle applyPaddings(Rectangle rect, UnitValue[] paddings, boolean reverse) {
        if (!paddings[0].isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 50));
        }
        if (!paddings[1].isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 49));
        }
        if (!paddings[2].isPointValue()) {
            Logger logger3 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger3.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 47));
        }
        if (!paddings[3].isPointValue()) {
            Logger logger4 = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
            logger4.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 48));
        }
        return rect.applyMargins(paddings[0].getValue(), paddings[1].getValue(), paddings[2].getValue(), paddings[3].getValue(), reverse);
    }

    protected Rectangle applyBorderBox(Rectangle rect, Border[] borders, boolean reverse) {
        float topWidth = borders[0] != null ? borders[0].getWidth() : 0.0f;
        float rightWidth = borders[1] != null ? borders[1].getWidth() : 0.0f;
        float bottomWidth = borders[2] != null ? borders[2].getWidth() : 0.0f;
        float leftWidth = borders[3] != null ? borders[3].getWidth() : 0.0f;
        return rect.applyMargins(topWidth, rightWidth, bottomWidth, leftWidth, reverse);
    }

    protected void applyAbsolutePosition(Rectangle parentRect) {
        Float top = getPropertyAsFloat(73);
        Float bottom = getPropertyAsFloat(14);
        Float left = getPropertyAsFloat(34);
        Float right = getPropertyAsFloat(54);
        if (left == null && right == null && BaseDirection.RIGHT_TO_LEFT.equals(getProperty(7))) {
            right = Float.valueOf(0.0f);
        }
        if (top == null && bottom == null) {
            top = Float.valueOf(0.0f);
        }
        if (right != null) {
            try {
                move((parentRect.getRight() - right.floatValue()) - this.occupiedArea.getBBox().getRight(), 0.0f);
            } catch (Exception e) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Absolute positioning might be applied incorrectly."));
                return;
            }
        }
        if (left != null) {
            move((parentRect.getLeft() + left.floatValue()) - this.occupiedArea.getBBox().getLeft(), 0.0f);
        }
        if (top != null) {
            move(0.0f, (parentRect.getTop() - top.floatValue()) - this.occupiedArea.getBBox().getTop());
        }
        if (bottom != null) {
            move(0.0f, (parentRect.getBottom() + bottom.floatValue()) - this.occupiedArea.getBBox().getBottom());
        }
    }

    protected void applyRelativePositioningTranslation(boolean reverse) {
        float top = getPropertyAsFloat(73, Float.valueOf(0.0f)).floatValue();
        float bottom = getPropertyAsFloat(14, Float.valueOf(0.0f)).floatValue();
        float left = getPropertyAsFloat(34, Float.valueOf(0.0f)).floatValue();
        float right = getPropertyAsFloat(54, Float.valueOf(0.0f)).floatValue();
        int reverseMultiplier = reverse ? -1 : 1;
        float dxRight = left != 0.0f ? left * reverseMultiplier : (-right) * reverseMultiplier;
        float dyUp = top != 0.0f ? (-top) * reverseMultiplier : bottom * reverseMultiplier;
        if (dxRight != 0.0f || dyUp != 0.0f) {
            move(dxRight, dyUp);
        }
    }

    protected void applyDestination(PdfDocument document) {
        String destination = (String) getProperty(17);
        if (destination != null) {
            int pageNumber = this.occupiedArea.getPageNumber();
            if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.UNABLE_TO_APPLY_PAGE_DEPENDENT_PROP_UNKNOWN_PAGE_ON_WHICH_ELEMENT_IS_DRAWN, "Property.DESTINATION, which specifies this element location as destination, see ElementPropertyContainer.setDestination."));
                return;
            }
            PdfArray array = new PdfArray();
            array.add(document.getPage(pageNumber).getPdfObject());
            array.add(PdfName.XYZ);
            array.add(new PdfNumber(this.occupiedArea.getBBox().getX()));
            array.add(new PdfNumber(this.occupiedArea.getBBox().getY() + this.occupiedArea.getBBox().getHeight()));
            array.add(new PdfNumber(0));
            document.addNamedDestination(destination, array.makeIndirect(document));
            deleteProperty(17);
        }
    }

    protected void applyAction(PdfDocument document) {
        PdfAction action = (PdfAction) getProperty(1);
        if (action != null) {
            PdfLinkAnnotation link = (PdfLinkAnnotation) getProperty(88);
            if (link == null) {
                link = (PdfLinkAnnotation) new PdfLinkAnnotation(new Rectangle(0.0f, 0.0f, 0.0f, 0.0f)).setFlags(4);
                Border border = (Border) getProperty(9);
                if (border != null) {
                    link.setBorder(new PdfArray(new float[]{0.0f, 0.0f, border.getWidth()}));
                } else {
                    link.setBorder(new PdfArray(new float[]{0.0f, 0.0f, 0.0f}));
                }
                setProperty(88, link);
            }
            link.setAction(action);
        }
    }

    protected void applyLinkAnnotation(PdfDocument document) {
        PdfLinkAnnotation linkAnnotation = (PdfLinkAnnotation) getProperty(88);
        if (linkAnnotation != null) {
            int pageNumber = this.occupiedArea.getPageNumber();
            if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.UNABLE_TO_APPLY_PAGE_DEPENDENT_PROP_UNKNOWN_PAGE_ON_WHICH_ELEMENT_IS_DRAWN, "Property.LINK_ANNOTATION, which specifies a link associated with this element content area, see com.itextpdf.layout.element.Link."));
                return;
            }
            Rectangle pdfBBox = calculateAbsolutePdfBBox();
            if (linkAnnotation.getPage() != null) {
                PdfDictionary oldAnnotation = (PdfDictionary) linkAnnotation.getPdfObject().m850clone();
                linkAnnotation = (PdfLinkAnnotation) PdfAnnotation.makeAnnotation(oldAnnotation);
            }
            linkAnnotation.setRectangle(new PdfArray(pdfBBox));
            PdfPage page = document.getPage(pageNumber);
            page.addAnnotation(linkAnnotation);
        }
    }

    private Float retrieveResolvedParentDeclaredHeight() {
        if (this.parent != null && this.parent.getProperty(27) != null) {
            UnitValue parentHeightUV = getPropertyAsUnitValue(this.parent, 27);
            if (parentHeightUV.isPointValue()) {
                return Float.valueOf(parentHeightUV.getValue());
            }
            return ((AbstractRenderer) this.parent).retrieveHeight();
        }
        return null;
    }

    private Float retrieveDirectParentDeclaredHeight() {
        if (this.parent != null && this.parent.getProperty(27) != null) {
            UnitValue parentHeightUV = getPropertyAsUnitValue(this.parent, 27);
            if (parentHeightUV.isPointValue()) {
                return Float.valueOf(parentHeightUV.getValue());
            }
            return null;
        }
        return null;
    }

    protected void updateHeightsOnSplit(boolean wasHeightClipped, AbstractRenderer splitRenderer, AbstractRenderer overflowRenderer) {
        updateHeightsOnSplit(this.occupiedArea.getBBox().getHeight(), wasHeightClipped, splitRenderer, overflowRenderer, true);
    }

    void updateHeightsOnSplit(float usedHeight, boolean wasHeightClipped, AbstractRenderer splitRenderer, AbstractRenderer overflowRenderer, boolean enlargeOccupiedAreaOnHeightWasClipped) {
        if (wasHeightClipped) {
            Logger logger = LoggerFactory.getLogger((Class<?>) BlockRenderer.class);
            logger.warn(LogMessageConstant.CLIP_ELEMENT);
            if (enlargeOccupiedAreaOnHeightWasClipped) {
                Float maxHeight = retrieveMaxHeight();
                splitRenderer.occupiedArea.getBBox().moveDown(maxHeight.floatValue() - usedHeight).setHeight(maxHeight.floatValue());
                usedHeight = maxHeight.floatValue();
            }
        }
        if (overflowRenderer == null || isKeepTogether()) {
            return;
        }
        Float parentResolvedHeightPropertyValue = retrieveResolvedParentDeclaredHeight();
        UnitValue maxHeightUV = getPropertyAsUnitValue(this, 84);
        if (maxHeightUV != null) {
            if (maxHeightUV.isPointValue()) {
                UnitValue updateMaxHeight = UnitValue.createPointValue(retrieveMaxHeight().floatValue() - usedHeight);
                overflowRenderer.updateMaxHeight(updateMaxHeight);
            } else if (parentResolvedHeightPropertyValue != null) {
                float currentOccupiedFraction = (usedHeight / parentResolvedHeightPropertyValue.floatValue()) * 100.0f;
                float newFraction = maxHeightUV.getValue() - currentOccupiedFraction;
                overflowRenderer.updateMinHeight(UnitValue.createPercentValue(newFraction));
            }
        }
        UnitValue minHeightUV = getPropertyAsUnitValue(this, 85);
        if (minHeightUV != null) {
            if (minHeightUV.isPointValue()) {
                Float minHeight = retrieveMinHeight();
                UnitValue updateminHeight = UnitValue.createPointValue(minHeight.floatValue() - usedHeight);
                overflowRenderer.updateMinHeight(updateminHeight);
            } else if (parentResolvedHeightPropertyValue != null) {
                float currentOccupiedFraction2 = (usedHeight / parentResolvedHeightPropertyValue.floatValue()) * 100.0f;
                float newFraction2 = minHeightUV.getValue() - currentOccupiedFraction2;
                overflowRenderer.updateMinHeight(UnitValue.createPercentValue(newFraction2));
            }
        }
        UnitValue heightUV = getPropertyAsUnitValue(this, 27);
        if (heightUV != null) {
            if (heightUV.isPointValue()) {
                Float height = retrieveHeight();
                UnitValue updateHeight = UnitValue.createPointValue(height.floatValue() - usedHeight);
                overflowRenderer.updateHeight(updateHeight);
            } else if (parentResolvedHeightPropertyValue != null) {
                float currentOccupiedFraction3 = (usedHeight / parentResolvedHeightPropertyValue.floatValue()) * 100.0f;
                float newFraction3 = heightUV.getValue() - currentOccupiedFraction3;
                overflowRenderer.updateMinHeight(UnitValue.createPercentValue(newFraction3));
            }
        }
    }

    public MinMaxWidth getMinMaxWidth() {
        return MinMaxWidthUtils.countDefaultMinMaxWidth(this);
    }

    protected boolean setMinMaxWidthBasedOnFixedWidth(MinMaxWidth minMaxWidth) {
        Float width;
        if (hasAbsoluteUnitValue(77) && (width = retrieveWidth(0.0f)) != null) {
            minMaxWidth.setChildrenMaxWidth(width.floatValue());
            minMaxWidth.setChildrenMinWidth(width.floatValue());
            return true;
        }
        return false;
    }

    protected boolean isNotFittingHeight(LayoutArea layoutArea) {
        return !isPositioned() && this.occupiedArea.getBBox().getHeight() > layoutArea.getBBox().getHeight();
    }

    protected boolean isNotFittingWidth(LayoutArea layoutArea) {
        return !isPositioned() && this.occupiedArea.getBBox().getWidth() > layoutArea.getBBox().getWidth();
    }

    protected boolean isNotFittingLayoutArea(LayoutArea layoutArea) {
        return isNotFittingHeight(layoutArea) || isNotFittingWidth(layoutArea);
    }

    protected boolean isPositioned() {
        return !isStaticLayout();
    }

    protected boolean isFixedLayout() {
        Object positioning = getProperty(52);
        Integer num = 4;
        return num.equals(positioning);
    }

    protected boolean isStaticLayout() {
        Object positioning = getProperty(52);
        if (positioning != null) {
            Integer num = 1;
            if (!num.equals(positioning)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isRelativePosition() {
        Integer positioning = getPropertyAsInteger(52);
        Integer num = 2;
        return num.equals(positioning);
    }

    protected boolean isAbsolutePosition() {
        Integer positioning = getPropertyAsInteger(52);
        Integer num = 3;
        return num.equals(positioning);
    }

    protected boolean isKeepTogether() {
        return Boolean.TRUE.equals(getPropertyAsBoolean(32));
    }

    protected void alignChildHorizontally(IRenderer childRenderer, Rectangle currentArea) {
        float availableWidth = currentArea.getWidth();
        HorizontalAlignment horizontalAlignment = (HorizontalAlignment) childRenderer.getProperty(28);
        if (horizontalAlignment != null && horizontalAlignment != HorizontalAlignment.LEFT) {
            float freeSpace = availableWidth - childRenderer.getOccupiedArea().getBBox().getWidth();
            if (freeSpace > 0.0f) {
                try {
                    switch (horizontalAlignment) {
                        case RIGHT:
                            childRenderer.move(freeSpace, 0.0f);
                            break;
                        case CENTER:
                            childRenderer.move(freeSpace / 2.0f, 0.0f);
                            break;
                    }
                } catch (NullPointerException e) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                    logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Some of the children might not end up aligned horizontally."));
                }
            }
        }
    }

    protected Border[] getBorders() {
        return getBorders(this);
    }

    protected BorderRadius[] getBorderRadii() {
        return getBorderRadii(this);
    }

    protected AbstractRenderer setBorders(Border border, int borderNumber) {
        switch (borderNumber) {
            case 0:
                setProperty(13, border);
                break;
            case 1:
                setProperty(12, border);
                break;
            case 2:
                setProperty(10, border);
                break;
            case 3:
                setProperty(11, border);
                break;
        }
        return this;
    }

    protected Rectangle calculateAbsolutePdfBBox() {
        Rectangle contentBox = getOccupiedAreaBBox();
        List<Point> contentBoxPoints = rectangleToPointsList(contentBox);
        AbstractRenderer abstractRenderer = this;
        while (true) {
            AbstractRenderer renderer = abstractRenderer;
            if (renderer.parent != null) {
                if (renderer instanceof BlockRenderer) {
                    Float angle = (Float) renderer.getProperty(55);
                    if (angle != null) {
                        BlockRenderer blockRenderer = (BlockRenderer) renderer;
                        AffineTransform rotationTransform = blockRenderer.createRotationTransformInsideOccupiedArea();
                        transformPoints(contentBoxPoints, rotationTransform);
                    }
                }
                if (renderer.getProperty(53) != null && ((renderer instanceof BlockRenderer) || (renderer instanceof ImageRenderer) || (renderer instanceof TableRenderer))) {
                    AffineTransform rotationTransform2 = renderer.createTransformationInsideOccupiedArea();
                    transformPoints(contentBoxPoints, rotationTransform2);
                }
                abstractRenderer = (AbstractRenderer) renderer.parent;
            } else {
                return calculateBBox(contentBoxPoints);
            }
        }
    }

    protected Rectangle calculateBBox(List<Point> points) {
        return Rectangle.calculateBBox(points);
    }

    protected List<Point> rectangleToPointsList(Rectangle rect) {
        return Arrays.asList(rect.toPointsArray());
    }

    protected List<Point> transformPoints(List<Point> points, AffineTransform transform) {
        for (Point point : points) {
            transform.transform(point, point);
        }
        return points;
    }

    protected float[] calculateShiftToPositionBBoxOfPointsAt(float left, float top, List<Point> points) {
        double minX = Double.MAX_VALUE;
        double maxY = -1.7976931348623157E308d;
        for (Point point : points) {
            minX = Math.min(point.getX(), minX);
            maxY = Math.max(point.getY(), maxY);
        }
        float dx = (float) (left - minX);
        float dy = (float) (top - maxY);
        return new float[]{dx, dy};
    }

    protected boolean hasAbsoluteUnitValue(int property) {
        UnitValue value = (UnitValue) getProperty(property);
        return value != null && value.isPointValue();
    }

    protected boolean hasRelativeUnitValue(int property) {
        UnitValue value = (UnitValue) getProperty(property);
        return value != null && value.isPercentValue();
    }

    boolean isFirstOnRootArea(boolean checkRootAreaOnly) {
        boolean isFirstOnRootArea = true;
        IRenderer iRenderer = this;
        while (true) {
            IRenderer ancestor = iRenderer;
            if (!isFirstOnRootArea || ancestor.getParent() == null) {
                break;
            }
            IRenderer parent = ancestor.getParent();
            if (parent instanceof RootRenderer) {
                isFirstOnRootArea = ((RootRenderer) parent).currentArea.isEmptyArea();
            } else {
                if (parent.getOccupiedArea() == null) {
                    break;
                }
                if (!checkRootAreaOnly) {
                    isFirstOnRootArea = parent.getOccupiedArea().getBBox().getHeight() < EPS;
                }
            }
            iRenderer = parent;
        }
        return isFirstOnRootArea;
    }

    RootRenderer getRootRenderer() {
        IRenderer parent = this;
        while (true) {
            IRenderer currentRenderer = parent;
            if (currentRenderer instanceof AbstractRenderer) {
                if (currentRenderer instanceof RootRenderer) {
                    return (RootRenderer) currentRenderer;
                }
                parent = ((AbstractRenderer) currentRenderer).getParent();
            } else {
                return null;
            }
        }
    }

    static float calculateAdditionalWidth(AbstractRenderer renderer) {
        Rectangle dummy = new Rectangle(0.0f, 0.0f);
        renderer.applyMargins(dummy, true);
        renderer.applyBorderBox(dummy, true);
        renderer.applyPaddings(dummy, true);
        return dummy.getWidth();
    }

    static boolean noAbsolutePositionInfo(IRenderer renderer) {
        return (renderer.hasProperty(73) || renderer.hasProperty(14) || renderer.hasProperty(34) || renderer.hasProperty(54)) ? false : true;
    }

    static Float getPropertyAsFloat(IRenderer renderer, int property) {
        return NumberUtil.asFloat(renderer.getProperty(property));
    }

    static UnitValue getPropertyAsUnitValue(IRenderer renderer, int property) {
        UnitValue result = (UnitValue) renderer.getProperty(property);
        return result;
    }

    void shrinkOccupiedAreaForAbsolutePosition() {
        if (isAbsolutePosition()) {
            Float left = getPropertyAsFloat(34);
            Float right = getPropertyAsFloat(54);
            UnitValue width = (UnitValue) getProperty(77);
            if (left == null && right == null && width == null) {
                this.occupiedArea.getBBox().setWidth(0.0f);
            }
        }
    }

    void drawPositionedChildren(DrawContext drawContext) {
        for (IRenderer positionedChild : this.positionedRenderers) {
            positionedChild.draw(drawContext);
        }
    }

    FontCharacteristics createFontCharacteristics() {
        FontCharacteristics fc = new FontCharacteristics();
        if (hasProperty(95)) {
            fc.setFontWeight((String) getProperty(95));
        }
        if (hasProperty(94)) {
            fc.setFontStyle((String) getProperty(94));
        }
        return fc;
    }

    PdfFont resolveFirstPdfFont() {
        Object font = getProperty(20);
        if (font instanceof PdfFont) {
            return (PdfFont) font;
        }
        if ((font instanceof String) || (font instanceof String[])) {
            if (font instanceof String) {
                Logger logger = LoggerFactory.getLogger((Class<?>) AbstractRenderer.class);
                logger.warn(LogMessageConstant.FONT_PROPERTY_OF_STRING_TYPE_IS_DEPRECATED_USE_STRINGS_ARRAY_INSTEAD);
                List<String> splitFontFamily = FontFamilySplitter.splitFontFamily((String) font);
                font = splitFontFamily.toArray(new String[splitFontFamily.size()]);
            }
            FontProvider provider = (FontProvider) getProperty(91);
            if (provider == null) {
                throw new IllegalStateException(PdfException.FontProviderNotSetFontFamilyNotResolved);
            }
            FontCharacteristics fc = createFontCharacteristics();
            return resolveFirstPdfFont((String[]) font, provider, fc);
        }
        throw new IllegalStateException("String[] or PdfFont expected as value of FONT property");
    }

    PdfFont resolveFirstPdfFont(String[] font, FontProvider provider, FontCharacteristics fc) {
        return provider.getPdfFont(provider.getFontSelector(Arrays.asList(font), fc).bestMatch());
    }

    static Border[] getBorders(IRenderer renderer) {
        Border border = (Border) renderer.getProperty(9);
        Border topBorder = (Border) renderer.getProperty(13);
        Border rightBorder = (Border) renderer.getProperty(12);
        Border bottomBorder = (Border) renderer.getProperty(10);
        Border leftBorder = (Border) renderer.getProperty(11);
        Border[] borders = {topBorder, rightBorder, bottomBorder, leftBorder};
        if (!hasOwnOrModelProperty(renderer, 13)) {
            borders[0] = border;
        }
        if (!hasOwnOrModelProperty(renderer, 12)) {
            borders[1] = border;
        }
        if (!hasOwnOrModelProperty(renderer, 10)) {
            borders[2] = border;
        }
        if (!hasOwnOrModelProperty(renderer, 11)) {
            borders[3] = border;
        }
        return borders;
    }

    void applyAbsolutePositionIfNeeded(LayoutContext layoutContext) {
        if (isAbsolutePosition()) {
            applyAbsolutePosition(layoutContext instanceof PositionedLayoutContext ? ((PositionedLayoutContext) layoutContext).getParentOccupiedArea().getBBox() : layoutContext.getArea().getBBox());
        }
    }

    void preparePositionedRendererAndAreaForLayout(IRenderer childPositionedRenderer, Rectangle fullBbox, Rectangle parentBbox) {
        Float left = getPropertyAsFloat(childPositionedRenderer, 34);
        Float right = getPropertyAsFloat(childPositionedRenderer, 54);
        Float top = getPropertyAsFloat(childPositionedRenderer, 73);
        Float bottom = getPropertyAsFloat(childPositionedRenderer, 14);
        childPositionedRenderer.setParent(this);
        adjustPositionedRendererLayoutBoxWidth(childPositionedRenderer, fullBbox, left, right);
        Integer num = 3;
        if (num.equals(childPositionedRenderer.getProperty(52))) {
            updateMinHeightForAbsolutelyPositionedRenderer(childPositionedRenderer, parentBbox, top, bottom);
        }
    }

    private void updateMinHeightForAbsolutelyPositionedRenderer(IRenderer renderer, Rectangle parentRendererBox, Float top, Float bottom) {
        if (top != null && bottom != null && !renderer.hasProperty(27)) {
            UnitValue currentMaxHeight = getPropertyAsUnitValue(renderer, 84);
            UnitValue currentMinHeight = getPropertyAsUnitValue(renderer, 85);
            float resolvedMinHeight = Math.max(0.0f, ((parentRendererBox.getTop() - top.floatValue()) - parentRendererBox.getBottom()) - bottom.floatValue());
            Rectangle dummy = new Rectangle(0.0f, 0.0f);
            if (!isBorderBoxSizing(renderer)) {
                applyPaddings(dummy, getPaddings(renderer), true);
                applyBorderBox(dummy, getBorders(renderer), true);
            }
            applyMargins(dummy, getMargins(renderer), true);
            float resolvedMinHeight2 = resolvedMinHeight - dummy.getHeight();
            if (currentMinHeight != null) {
                resolvedMinHeight2 = Math.max(resolvedMinHeight2, currentMinHeight.getValue());
            }
            if (currentMaxHeight != null) {
                resolvedMinHeight2 = Math.min(resolvedMinHeight2, currentMaxHeight.getValue());
            }
            renderer.setProperty(85, UnitValue.createPointValue(resolvedMinHeight2));
        }
    }

    private void adjustPositionedRendererLayoutBoxWidth(IRenderer renderer, Rectangle fullBbox, Float left, Float right) {
        if (left != null) {
            fullBbox.setWidth(fullBbox.getWidth() - left.floatValue()).setX(fullBbox.getX() + left.floatValue());
        }
        if (right != null) {
            fullBbox.setWidth(fullBbox.getWidth() - right.floatValue());
        }
        if (left == null && right == null && !renderer.hasProperty(77)) {
            MinMaxWidth minMaxWidth = renderer instanceof BlockRenderer ? ((BlockRenderer) renderer).getMinMaxWidth() : null;
            if (minMaxWidth != null && minMaxWidth.getMaxWidth() < fullBbox.getWidth()) {
                fullBbox.setWidth(minMaxWidth.getMaxWidth() + EPS);
            }
        }
    }

    private static float calculatePaddingBorderWidth(AbstractRenderer renderer) {
        Rectangle dummy = new Rectangle(0.0f, 0.0f);
        renderer.applyBorderBox(dummy, true);
        renderer.applyPaddings(dummy, true);
        return dummy.getWidth();
    }

    private static float calculatePaddingBorderHeight(AbstractRenderer renderer) {
        Rectangle dummy = new Rectangle(0.0f, 0.0f);
        renderer.applyBorderBox(dummy, true);
        renderer.applyPaddings(dummy, true);
        return dummy.getHeight();
    }

    private AffineTransform createTransformationInsideOccupiedArea() {
        Rectangle backgroundArea = applyMargins(this.occupiedArea.mo950clone().getBBox(), false);
        float x = backgroundArea.getX();
        float y = backgroundArea.getY();
        float height = backgroundArea.getHeight();
        float width = backgroundArea.getWidth();
        AffineTransform transform = AffineTransform.getTranslateInstance((-1.0f) * (x + (width / 2.0f)), (-1.0f) * (y + (height / 2.0f)));
        transform.preConcatenate(Transform.getAffineTransform((Transform) getProperty(53), width, height));
        transform.preConcatenate(AffineTransform.getTranslateInstance(x + (width / 2.0f), y + (height / 2.0f)));
        return transform;
    }

    protected void beginTransformationIfApplied(PdfCanvas canvas) {
        if (getProperty(53) != null) {
            AffineTransform transform = createTransformationInsideOccupiedArea();
            canvas.saveState().concatMatrix(transform);
        }
    }

    protected void endTransformationIfApplied(PdfCanvas canvas) {
        if (getProperty(53) != null) {
            canvas.restoreState();
        }
    }

    private static UnitValue[] getMargins(IRenderer renderer) {
        return new UnitValue[]{(UnitValue) renderer.getProperty(46), (UnitValue) renderer.getProperty(45), (UnitValue) renderer.getProperty(43), (UnitValue) renderer.getProperty(44)};
    }

    private static BorderRadius[] getBorderRadii(IRenderer renderer) {
        BorderRadius radius = (BorderRadius) renderer.getProperty(101);
        BorderRadius topLeftRadius = (BorderRadius) renderer.getProperty(110);
        BorderRadius topRightRadius = (BorderRadius) renderer.getProperty(111);
        BorderRadius bottomRightRadius = (BorderRadius) renderer.getProperty(112);
        BorderRadius bottomLeftRadius = (BorderRadius) renderer.getProperty(113);
        BorderRadius[] borderRadii = {topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius};
        if (!hasOwnOrModelProperty(renderer, 110)) {
            borderRadii[0] = radius;
        }
        if (!hasOwnOrModelProperty(renderer, 111)) {
            borderRadii[1] = radius;
        }
        if (!hasOwnOrModelProperty(renderer, 112)) {
            borderRadii[2] = radius;
        }
        if (!hasOwnOrModelProperty(renderer, 113)) {
            borderRadii[3] = radius;
        }
        return borderRadii;
    }

    private static UnitValue[] getPaddings(IRenderer renderer) {
        return new UnitValue[]{(UnitValue) renderer.getProperty(50), (UnitValue) renderer.getProperty(49), (UnitValue) renderer.getProperty(47), (UnitValue) renderer.getProperty(48)};
    }

    private static boolean hasOwnOrModelProperty(IRenderer renderer, int property) {
        return renderer.hasOwnProperty(property) || (null != renderer.getModelElement() && renderer.getModelElement().hasProperty(property));
    }
}
