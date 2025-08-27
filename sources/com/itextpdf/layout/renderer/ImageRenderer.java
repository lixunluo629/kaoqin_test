package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.MinMaxWidthLayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/ImageRenderer.class */
public class ImageRenderer extends AbstractRenderer implements ILeafElementRenderer {
    protected Float fixedXPosition;
    protected Float fixedYPosition;
    protected float pivotY;
    protected float deltaX;
    protected float imageWidth;
    protected float imageHeight;
    float[] matrix;
    private Float height;
    private Float width;
    private Rectangle initialOccupiedAreaBBox;
    private float rotatedDeltaX;
    private float rotatedDeltaY;

    public ImageRenderer(Image image) {
        super(image);
        this.matrix = new float[6];
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutArea area = layoutContext.getArea().mo950clone();
        Rectangle layoutBox = area.getBBox().mo825clone();
        AffineTransform t = new AffineTransform();
        Image modelElement = (Image) getModelElement();
        PdfXObject xObject = modelElement.getXObject();
        this.imageWidth = modelElement.getImageWidth();
        this.imageHeight = modelElement.getImageHeight();
        calculateImageDimensions(layoutBox, t, xObject);
        OverflowPropertyValue overflowX = null != this.parent ? (OverflowPropertyValue) this.parent.getProperty(103) : OverflowPropertyValue.FIT;
        boolean nowrap = false;
        if (this.parent instanceof LineRenderer) {
            nowrap = Boolean.TRUE.equals(this.parent.getOwnProperty(118));
        }
        List<Rectangle> floatRendererAreas = layoutContext.getFloatRendererAreas();
        float clearHeightCorrection = FloatingHelper.calculateClearHeightCorrection(this, floatRendererAreas, layoutBox);
        FloatPropertyValue floatPropertyValue = (FloatPropertyValue) getProperty(99);
        if (FloatingHelper.isRendererFloating(this, floatPropertyValue)) {
            layoutBox.decreaseHeight(clearHeightCorrection);
            FloatingHelper.adjustFloatedBlockLayoutBox(this, layoutBox, this.width, floatRendererAreas, floatPropertyValue, overflowX);
        } else {
            clearHeightCorrection = FloatingHelper.adjustLayoutBoxAccordingToFloats(floatRendererAreas, layoutBox, this.width, clearHeightCorrection, null);
        }
        applyMargins(layoutBox, false);
        Border[] borders = getBorders();
        applyBorderBox(layoutBox, borders, false);
        Float declaredMaxHeight = retrieveMaxHeight();
        OverflowPropertyValue overflowY = (null == this.parent || ((null == declaredMaxHeight || declaredMaxHeight.floatValue() > layoutBox.getHeight()) && !layoutContext.isClippedHeight())) ? OverflowPropertyValue.FIT : (OverflowPropertyValue) this.parent.getProperty(104);
        boolean processOverflowX = !isOverflowFit(overflowX) || nowrap;
        boolean processOverflowY = !isOverflowFit(overflowY);
        if (isAbsolutePosition()) {
            applyAbsolutePosition(layoutBox);
        }
        this.occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(layoutBox.getX(), layoutBox.getY() + layoutBox.getHeight(), 0.0f, 0.0f));
        float imageItselfScaledWidth = this.width.floatValue();
        float imageItselfScaledHeight = this.height.floatValue();
        if (isFixedLayout()) {
            this.fixedXPosition = getPropertyAsFloat(34);
            this.fixedYPosition = getPropertyAsFloat(14);
        }
        Float angle = getPropertyAsFloat(55);
        if (null == angle) {
            angle = Float.valueOf(0.0f);
        }
        t.rotate(angle.floatValue());
        this.initialOccupiedAreaBBox = getOccupiedAreaBBox().mo825clone();
        float scaleCoef = adjustPositionAfterRotation(angle.floatValue(), layoutBox.getWidth(), layoutBox.getHeight());
        float imageItselfScaledHeight2 = imageItselfScaledHeight * scaleCoef;
        float imageItselfScaledWidth2 = imageItselfScaledWidth * scaleCoef;
        this.initialOccupiedAreaBBox.moveDown(imageItselfScaledHeight2);
        this.initialOccupiedAreaBBox.setHeight(imageItselfScaledHeight2);
        this.initialOccupiedAreaBBox.setWidth(imageItselfScaledWidth2);
        if (xObject instanceof PdfFormXObject) {
            t.scale(scaleCoef, scaleCoef);
        }
        getMatrix(t, imageItselfScaledWidth2, imageItselfScaledHeight2);
        boolean isPlacingForced = false;
        if (this.width.floatValue() > layoutBox.getWidth() || this.height.floatValue() > layoutBox.getHeight()) {
            if (Boolean.TRUE.equals(getPropertyAsBoolean(26)) || ((this.width.floatValue() > layoutBox.getWidth() && processOverflowX) || (this.height.floatValue() > layoutBox.getHeight() && processOverflowY))) {
                isPlacingForced = true;
            } else {
                applyMargins(this.initialOccupiedAreaBBox, true);
                applyBorderBox(this.initialOccupiedAreaBBox, true);
                this.occupiedArea.getBBox().setHeight(this.initialOccupiedAreaBBox.getHeight());
                return new MinMaxWidthLayoutResult(3, this.occupiedArea, null, this, this);
            }
        }
        this.occupiedArea.getBBox().moveDown(this.height.floatValue());
        if (borders[3] != null) {
            this.height = Float.valueOf(this.height.floatValue() + (((float) Math.sin(angle.floatValue())) * borders[3].getWidth()));
        }
        this.occupiedArea.getBBox().setHeight(this.height.floatValue());
        this.occupiedArea.getBBox().setWidth(this.width.floatValue());
        UnitValue leftMargin = getPropertyAsUnitValue(44);
        if (!leftMargin.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) ImageRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        UnitValue topMargin = getPropertyAsUnitValue(46);
        if (!topMargin.isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) ImageRenderer.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 46));
        }
        if (0.0f != leftMargin.getValue() || 0.0f != topMargin.getValue()) {
            translateImage(leftMargin.getValue(), topMargin.getValue(), t);
            getMatrix(t, imageItselfScaledWidth2, imageItselfScaledHeight2);
        }
        applyBorderBox(this.occupiedArea.getBBox(), borders, true);
        applyMargins(this.occupiedArea.getBBox(), true);
        if (angle.floatValue() != 0.0f) {
            applyRotationLayout(angle.floatValue());
        }
        float unscaledWidth = this.occupiedArea.getBBox().getWidth() / scaleCoef;
        MinMaxWidth minMaxWidth = new MinMaxWidth(unscaledWidth, unscaledWidth, 0.0f);
        UnitValue rendererWidth = (UnitValue) getProperty(77);
        if (rendererWidth != null && rendererWidth.isPercentValue()) {
            minMaxWidth.setChildrenMinWidth(0.0f);
            float coeff = this.imageWidth / retrieveWidth(area.getBBox().getWidth()).floatValue();
            minMaxWidth.setChildrenMaxWidth(unscaledWidth * coeff);
        } else {
            boolean autoScale = hasProperty(3) && ((Boolean) getProperty(3)).booleanValue();
            boolean autoScaleWidth = hasProperty(5) && ((Boolean) getProperty(5)).booleanValue();
            if (autoScale || autoScaleWidth) {
                minMaxWidth.setChildrenMinWidth(0.0f);
            }
        }
        FloatingHelper.removeFloatsAboveRendererBottom(floatRendererAreas, this);
        LayoutArea editedArea = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, floatRendererAreas, layoutContext.getArea().getBBox(), clearHeightCorrection, false);
        applyAbsolutePositionIfNeeded(layoutContext);
        return new MinMaxWidthLayoutResult(1, editedArea, null, null, isPlacingForced ? this : null).setMinMaxWidth(minMaxWidth);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        if (this.occupiedArea == null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) ImageRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Drawing won't be performed."));
            return;
        }
        boolean isRelativePosition = isRelativePosition();
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
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
        beginTransformationIfApplied(drawContext.getCanvas());
        Float angle = getPropertyAsFloat(55);
        if (angle != null) {
            drawContext.getCanvas().saveState();
            applyConcatMatrix(drawContext, angle);
        }
        super.draw(drawContext);
        boolean clipImageInAViewOfBorderRadius = clipBackgroundArea(drawContext, applyMargins(getOccupiedAreaBBox(), false), true);
        applyMargins(this.occupiedArea.getBBox(), false);
        applyBorderBox(this.occupiedArea.getBBox(), getBorders(), false);
        if (this.fixedYPosition == null) {
            this.fixedYPosition = Float.valueOf(this.occupiedArea.getBBox().getY() + this.pivotY);
        }
        if (this.fixedXPosition == null) {
            this.fixedXPosition = Float.valueOf(this.occupiedArea.getBBox().getX());
        }
        if (angle != null) {
            this.fixedXPosition = Float.valueOf(this.fixedXPosition.floatValue() + this.rotatedDeltaX);
            this.fixedYPosition = Float.valueOf(this.fixedYPosition.floatValue() - this.rotatedDeltaY);
            drawContext.getCanvas().restoreState();
        }
        PdfCanvas canvas = drawContext.getCanvas();
        if (isTagged) {
            if (isArtifact) {
                canvas.openTag(new CanvasArtifact());
            } else {
                canvas.openTag(tagPointer.getTagReference());
            }
        }
        PdfXObject xObject = ((Image) getModelElement()).getXObject();
        beginElementOpacityApplying(drawContext);
        canvas.addXObject(xObject, this.matrix[0], this.matrix[1], this.matrix[2], this.matrix[3], this.fixedXPosition.floatValue() + this.deltaX, this.fixedYPosition.floatValue());
        endElementOpacityApplying(drawContext);
        endTransformationIfApplied(drawContext.getCanvas());
        if (Boolean.TRUE.equals(getPropertyAsBoolean(19))) {
            xObject.flush();
        }
        if (isTagged) {
            canvas.closeTag();
        }
        if (clipImageInAViewOfBorderRadius) {
            canvas.restoreState();
        }
        if (isRelativePosition) {
            applyRelativePositioningTranslation(true);
        }
        applyBorderBox(this.occupiedArea.getBBox(), getBorders(), true);
        applyMargins(this.occupiedArea.getBBox(), true);
        if (isTagged && !isArtifact) {
            taggingHelper.finishTaggingHint(this);
            taggingHelper.restoreAutoTaggingPointerPosition(this);
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public Rectangle getBorderAreaBBox() {
        applyMargins(this.initialOccupiedAreaBBox, false);
        applyBorderBox(this.initialOccupiedAreaBBox, getBorders(), false);
        boolean isRelativePosition = isRelativePosition();
        if (isRelativePosition) {
            applyRelativePositioningTranslation(false);
        }
        applyMargins(this.initialOccupiedAreaBBox, true);
        applyBorderBox(this.initialOccupiedAreaBBox, true);
        return this.initialOccupiedAreaBBox;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Rectangle applyPaddings(Rectangle rect, UnitValue[] paddings, boolean reverse) {
        return rect;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void move(float dxRight, float dyUp) {
        super.move(dxRight, dyUp);
        if (this.initialOccupiedAreaBBox != null) {
            this.initialOccupiedAreaBBox.moveRight(dxRight);
            this.initialOccupiedAreaBBox.moveUp(dyUp);
        }
        if (this.fixedXPosition != null) {
            this.fixedXPosition = Float.valueOf(this.fixedXPosition.floatValue() + dxRight);
        }
        if (this.fixedYPosition != null) {
            this.fixedYPosition = Float.valueOf(this.fixedYPosition.floatValue() + dyUp);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        return ((MinMaxWidthLayoutResult) layout(new LayoutContext(new LayoutArea(1, new Rectangle(MinMaxWidthUtils.getInfWidth(), 1000000.0f))))).getMinMaxWidth();
    }

    protected ImageRenderer autoScale(LayoutArea layoutArea) {
        Rectangle area = layoutArea.getBBox().mo825clone();
        applyMargins(area, false);
        applyBorderBox(area, false);
        float angleScaleCoef = this.imageWidth / this.width.floatValue();
        if (this.width.floatValue() > angleScaleCoef * area.getWidth()) {
            updateHeight(UnitValue.createPointValue((area.getWidth() / this.width.floatValue()) * this.imageHeight));
            updateWidth(UnitValue.createPointValue(angleScaleCoef * area.getWidth()));
        }
        return this;
    }

    private void calculateImageDimensions(Rectangle layoutBox, AffineTransform t, PdfXObject xObject) {
        this.width = getProperty(77) != null ? retrieveWidth(layoutBox.getWidth()) : null;
        Float declaredHeight = retrieveHeight();
        this.height = declaredHeight;
        if (this.width == null && this.height == null) {
            this.width = Float.valueOf(this.imageWidth);
            this.height = Float.valueOf((this.width.floatValue() / this.imageWidth) * this.imageHeight);
        } else if (this.width == null) {
            this.width = Float.valueOf((this.height.floatValue() / this.imageHeight) * this.imageWidth);
        } else if (this.height == null) {
            this.height = Float.valueOf((this.width.floatValue() / this.imageWidth) * this.imageHeight);
        }
        Float horizontalScaling = getPropertyAsFloat(29, Float.valueOf(1.0f));
        Float verticalScaling = getPropertyAsFloat(76, Float.valueOf(1.0f));
        if ((xObject instanceof PdfFormXObject) && this.width.floatValue() != this.imageWidth) {
            horizontalScaling = Float.valueOf(horizontalScaling.floatValue() * (this.width.floatValue() / this.imageWidth));
            verticalScaling = Float.valueOf(verticalScaling.floatValue() * (this.height.floatValue() / this.imageHeight));
        }
        if (horizontalScaling.floatValue() != 1.0f) {
            if (xObject instanceof PdfFormXObject) {
                t.scale(horizontalScaling.floatValue(), 1.0d);
                this.width = Float.valueOf(this.imageWidth * horizontalScaling.floatValue());
            } else {
                this.width = Float.valueOf(this.width.floatValue() * horizontalScaling.floatValue());
            }
        }
        if (verticalScaling.floatValue() != 1.0f) {
            if (xObject instanceof PdfFormXObject) {
                t.scale(1.0d, verticalScaling.floatValue());
                this.height = Float.valueOf(this.imageHeight * verticalScaling.floatValue());
            } else {
                this.height = Float.valueOf(this.height.floatValue() * verticalScaling.floatValue());
            }
        }
        Float minWidth = retrieveMinWidth(layoutBox.getWidth());
        Float maxWidth = retrieveMaxWidth(layoutBox.getWidth());
        if (null != minWidth && this.width.floatValue() < minWidth.floatValue()) {
            this.height = Float.valueOf(this.height.floatValue() * (minWidth.floatValue() / this.width.floatValue()));
            this.width = minWidth;
        } else if (null != maxWidth && this.width.floatValue() > maxWidth.floatValue()) {
            this.height = Float.valueOf(this.height.floatValue() * (maxWidth.floatValue() / this.width.floatValue()));
            this.width = maxWidth;
        }
        Float minHeight = retrieveMinHeight();
        Float maxHeight = retrieveMaxHeight();
        if (null != minHeight && this.height.floatValue() < minHeight.floatValue()) {
            this.width = Float.valueOf(this.width.floatValue() * (minHeight.floatValue() / this.height.floatValue()));
            this.height = minHeight;
        } else if (null != maxHeight && this.height.floatValue() > maxHeight.floatValue()) {
            this.width = Float.valueOf(this.width.floatValue() * (maxHeight.floatValue() / this.height.floatValue()));
            this.height = maxHeight;
        } else if (null != declaredHeight && !this.height.equals(declaredHeight)) {
            this.width = Float.valueOf(this.width.floatValue() * (declaredHeight.floatValue() / this.height.floatValue()));
            this.height = declaredHeight;
        }
    }

    private void getMatrix(AffineTransform t, float imageItselfScaledWidth, float imageItselfScaledHeight) {
        t.getMatrix(this.matrix);
        PdfXObject xObject = ((Image) getModelElement()).getXObject();
        if (xObject instanceof PdfImageXObject) {
            float[] fArr = this.matrix;
            fArr[0] = fArr[0] * imageItselfScaledWidth;
            float[] fArr2 = this.matrix;
            fArr2[1] = fArr2[1] * imageItselfScaledWidth;
            float[] fArr3 = this.matrix;
            fArr3[2] = fArr3[2] * imageItselfScaledHeight;
            float[] fArr4 = this.matrix;
            fArr4[3] = fArr4[3] * imageItselfScaledHeight;
        }
    }

    private float adjustPositionAfterRotation(float angle, float maxWidth, float maxHeight) {
        if (angle != 0.0f) {
            AffineTransform t = AffineTransform.getRotateInstance(angle);
            Point p00 = t.transform(new Point(0, 0), new Point());
            Point p01 = t.transform(new Point(0.0d, this.height.floatValue()), new Point());
            Point p10 = t.transform(new Point(this.width.floatValue(), 0.0d), new Point());
            Point p11 = t.transform(new Point(this.width.floatValue(), this.height.floatValue()), new Point());
            double[] xValues = {p01.getX(), p10.getX(), p11.getX()};
            double[] yValues = {p01.getY(), p10.getY(), p11.getY()};
            double minX = p00.getX();
            double minY = p00.getY();
            double maxX = minX;
            double maxY = minY;
            for (double x : xValues) {
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            }
            for (double y : yValues) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
            this.height = Float.valueOf((float) (maxY - minY));
            this.width = Float.valueOf((float) (maxX - minX));
            this.pivotY = (float) (p00.getY() - minY);
            this.deltaX = -((float) minX);
        }
        float scaleCoeff = 1.0f;
        if (Boolean.TRUE.equals(getPropertyAsBoolean(3))) {
            if (maxWidth / this.width.floatValue() < maxHeight / this.height.floatValue()) {
                scaleCoeff = maxWidth / this.width.floatValue();
                this.height = Float.valueOf(this.height.floatValue() * (maxWidth / this.width.floatValue()));
                this.width = Float.valueOf(maxWidth);
            } else {
                scaleCoeff = maxHeight / this.height.floatValue();
                this.width = Float.valueOf(this.width.floatValue() * (maxHeight / this.height.floatValue()));
                this.height = Float.valueOf(maxHeight);
            }
        } else if (Boolean.TRUE.equals(getPropertyAsBoolean(5))) {
            scaleCoeff = maxWidth / this.width.floatValue();
            this.height = Float.valueOf(this.height.floatValue() * scaleCoeff);
            this.width = Float.valueOf(maxWidth);
        } else if (Boolean.TRUE.equals(getPropertyAsBoolean(4))) {
            scaleCoeff = maxHeight / this.height.floatValue();
            this.height = Float.valueOf(maxHeight);
            this.width = Float.valueOf(this.width.floatValue() * scaleCoeff);
        }
        this.pivotY *= scaleCoeff;
        this.deltaX *= scaleCoeff;
        return scaleCoeff;
    }

    private void translateImage(float xDistance, float yDistance, AffineTransform t) {
        t.translate(xDistance, yDistance);
        t.getMatrix(this.matrix);
        if (this.fixedXPosition != null) {
            this.fixedXPosition = Float.valueOf(this.fixedXPosition.floatValue() + ((float) t.getTranslateX()));
        }
        if (this.fixedYPosition != null) {
            this.fixedYPosition = Float.valueOf(this.fixedYPosition.floatValue() + ((float) t.getTranslateY()));
        }
    }

    private void applyConcatMatrix(DrawContext drawContext, Float angle) {
        AffineTransform rotationTransform = AffineTransform.getRotateInstance(angle.floatValue());
        Rectangle rect = getBorderAreaBBox();
        List<Point> rotatedPoints = transformPoints(rectangleToPointsList(rect), rotationTransform);
        float[] shift = calculateShiftToPositionBBoxOfPointsAt(rect.getX(), rect.getY() + rect.getHeight(), rotatedPoints);
        double[] matrix = new double[6];
        rotationTransform.getMatrix(matrix);
        drawContext.getCanvas().concatMatrix(matrix[0], matrix[1], matrix[2], matrix[3], shift[0], shift[1]);
    }

    private void applyRotationLayout(float angle) {
        Border[] borders = getBorders();
        Rectangle rect = getBorderAreaBBox();
        float leftBorderWidth = borders[3] == null ? 0.0f : borders[3].getWidth();
        float rightBorderWidth = borders[1] == null ? 0.0f : borders[1].getWidth();
        float topBorderWidth = borders[0] == null ? 0.0f : borders[0].getWidth();
        if (leftBorderWidth != 0.0f) {
            float gip = (float) Math.sqrt(Math.pow(topBorderWidth, 2.0d) + Math.pow(leftBorderWidth, 2.0d));
            double atan = Math.atan(topBorderWidth / leftBorderWidth);
            if (angle < 0.0f) {
                atan = -atan;
            }
            this.rotatedDeltaX = Math.abs((float) ((gip * Math.cos(angle - atan)) - leftBorderWidth));
        } else {
            this.rotatedDeltaX = 0.0f;
        }
        rect.moveRight(this.rotatedDeltaX);
        this.occupiedArea.getBBox().setWidth(this.occupiedArea.getBBox().getWidth() + this.rotatedDeltaX);
        if (rightBorderWidth != 0.0f) {
            float gip2 = (float) Math.sqrt(Math.pow(topBorderWidth, 2.0d) + Math.pow(leftBorderWidth, 2.0d));
            double atan2 = Math.atan(rightBorderWidth / topBorderWidth);
            if (angle < 0.0f) {
                atan2 = -atan2;
            }
            this.rotatedDeltaY = Math.abs((float) ((gip2 * Math.cos(angle - atan2)) - topBorderWidth));
        } else {
            this.rotatedDeltaY = 0.0f;
        }
        rect.moveDown(this.rotatedDeltaY);
        if (angle < 0.0f) {
            this.rotatedDeltaY += rightBorderWidth;
        }
        this.occupiedArea.getBBox().increaseHeight(this.rotatedDeltaY);
    }

    @Override // com.itextpdf.layout.renderer.ILeafElementRenderer
    public float getAscent() {
        return this.occupiedArea.getBBox().getHeight();
    }

    @Override // com.itextpdf.layout.renderer.ILeafElementRenderer
    public float getDescent() {
        return 0.0f;
    }
}
