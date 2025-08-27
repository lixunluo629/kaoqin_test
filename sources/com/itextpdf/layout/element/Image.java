package com.itextpdf.layout.element;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageData;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.AccessibilityProperties;
import com.itextpdf.kernel.pdf.tagutils.DefaultAccessibilityProperties;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ImageRenderer;
import com.itextpdf.layout.tagging.IAccessibleElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/Image.class */
public class Image extends AbstractElement<Image> implements ILeafElement, IAccessibleElement {
    protected PdfXObject xObject;
    protected DefaultAccessibilityProperties tagProperties;

    public Image(PdfImageXObject xObject) {
        this.xObject = xObject;
    }

    public Image(PdfFormXObject xObject) {
        this.xObject = xObject;
    }

    public Image(PdfImageXObject xObject, float width) {
        this.xObject = xObject;
        setWidth(width);
    }

    public Image(PdfImageXObject xObject, float left, float bottom, float width) {
        this.xObject = xObject;
        setProperty(34, Float.valueOf(left));
        setProperty(14, Float.valueOf(bottom));
        setWidth(width);
        setProperty(52, 4);
    }

    public Image(PdfImageXObject xObject, float left, float bottom) {
        this.xObject = xObject;
        setProperty(34, Float.valueOf(left));
        setProperty(14, Float.valueOf(bottom));
        setProperty(52, 4);
    }

    public Image(PdfFormXObject xObject, float left, float bottom) {
        this.xObject = xObject;
        setProperty(34, Float.valueOf(left));
        setProperty(14, Float.valueOf(bottom));
        setProperty(52, 4);
    }

    public Image(ImageData img) {
        this(new PdfImageXObject(checkImageType(img)));
        setProperty(19, true);
    }

    public Image(ImageData img, float left, float bottom) {
        this(new PdfImageXObject(checkImageType(img)), left, bottom);
        setProperty(19, true);
    }

    public Image(ImageData img, float left, float bottom, float width) {
        this(new PdfImageXObject(checkImageType(img)), left, bottom, width);
        setProperty(19, true);
    }

    public PdfXObject getXObject() {
        return this.xObject;
    }

    public Image setRotationAngle(double radAngle) {
        setProperty(55, Double.valueOf(radAngle));
        return this;
    }

    public UnitValue getMarginLeft() {
        return (UnitValue) getProperty(44);
    }

    public Image setMarginLeft(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(44, marginUV);
        return this;
    }

    public UnitValue getMarginRight() {
        return (UnitValue) getProperty(45);
    }

    public Image setMarginRight(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(45, marginUV);
        return this;
    }

    public UnitValue getMarginTop() {
        return (UnitValue) getProperty(46);
    }

    public Image setMarginTop(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(46, marginUV);
        return this;
    }

    public UnitValue getMarginBottom() {
        return (UnitValue) getProperty(43);
    }

    public Image setMarginBottom(float value) {
        UnitValue marginUV = UnitValue.createPointValue(value);
        setProperty(43, marginUV);
        return this;
    }

    public Image setMargins(float marginTop, float marginRight, float marginBottom, float marginLeft) {
        return setMarginTop(marginTop).setMarginRight(marginRight).setMarginBottom(marginBottom).setMarginLeft(marginLeft);
    }

    public UnitValue getPaddingLeft() {
        return (UnitValue) getProperty(48);
    }

    public Image setPaddingLeft(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(48, paddingUV);
        return this;
    }

    public UnitValue getPaddingRight() {
        return (UnitValue) getProperty(49);
    }

    public Image setPaddingRight(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(49, paddingUV);
        return this;
    }

    public UnitValue getPaddingTop() {
        return (UnitValue) getProperty(50);
    }

    public Image setPaddingTop(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(50, paddingUV);
        return this;
    }

    public UnitValue getPaddingBottom() {
        return (UnitValue) getProperty(47);
    }

    public Image setPaddingBottom(float value) {
        UnitValue paddingUV = UnitValue.createPointValue(value);
        setProperty(47, paddingUV);
        return this;
    }

    public Image setPadding(float commonPadding) {
        return setPaddings(commonPadding, commonPadding, commonPadding, commonPadding);
    }

    public Image setPaddings(float paddingTop, float paddingRight, float paddingBottom, float paddingLeft) {
        setPaddingTop(paddingTop);
        setPaddingRight(paddingRight);
        setPaddingBottom(paddingBottom);
        setPaddingLeft(paddingLeft);
        return this;
    }

    public Image scale(float horizontalScaling, float verticalScaling) {
        setProperty(29, Float.valueOf(horizontalScaling));
        setProperty(76, Float.valueOf(verticalScaling));
        return this;
    }

    public Image scaleToFit(float fitWidth, float fitHeight) {
        float horizontalScaling = fitWidth / this.xObject.getWidth();
        float verticalScaling = fitHeight / this.xObject.getHeight();
        return scale(Math.min(horizontalScaling, verticalScaling), Math.min(horizontalScaling, verticalScaling));
    }

    public Image scaleAbsolute(float fitWidth, float fitHeight) {
        float horizontalScaling = fitWidth / this.xObject.getWidth();
        float verticalScaling = fitHeight / this.xObject.getHeight();
        return scale(horizontalScaling, verticalScaling);
    }

    public Image setAutoScale(boolean autoScale) {
        if (hasProperty(5) && hasProperty(4) && autoScale && (((Boolean) getProperty(5)).booleanValue() || ((Boolean) getProperty(4)).booleanValue())) {
            Logger logger = LoggerFactory.getLogger((Class<?>) Image.class);
            logger.warn(LogMessageConstant.IMAGE_HAS_AMBIGUOUS_SCALE);
        }
        setProperty(3, Boolean.valueOf(autoScale));
        return this;
    }

    public Image setAutoScaleHeight(boolean autoScale) {
        if (hasProperty(5) && autoScale && ((Boolean) getProperty(5)).booleanValue()) {
            setProperty(5, false);
            setProperty(4, false);
            setProperty(3, true);
        } else {
            setProperty(5, Boolean.valueOf(autoScale));
        }
        return this;
    }

    public Image setAutoScaleWidth(boolean autoScale) {
        if (hasProperty(4) && autoScale && ((Boolean) getProperty(4)).booleanValue()) {
            setProperty(5, false);
            setProperty(4, false);
            setProperty(3, true);
        } else {
            setProperty(5, Boolean.valueOf(autoScale));
        }
        return this;
    }

    public Image setFixedPosition(float left, float bottom) {
        setFixedPosition(left, bottom, getWidth());
        return this;
    }

    public Image setFixedPosition(int pageNumber, float left, float bottom) {
        setFixedPosition(pageNumber, left, bottom, getWidth());
        return this;
    }

    public float getImageWidth() {
        return this.xObject.getWidth();
    }

    public float getImageHeight() {
        return this.xObject.getHeight();
    }

    public Image setHeight(float height) {
        UnitValue heightAsUV = UnitValue.createPointValue(height);
        setProperty(27, heightAsUV);
        return this;
    }

    public Image setHeight(UnitValue height) {
        setProperty(27, height);
        return this;
    }

    public Image setMaxHeight(float maxHeight) {
        UnitValue maxHeightAsUv = UnitValue.createPointValue(maxHeight);
        setProperty(84, maxHeightAsUv);
        return this;
    }

    public Image setMaxHeight(UnitValue maxHeight) {
        setProperty(84, maxHeight);
        return this;
    }

    public Image setMinHeight(float minHeight) {
        UnitValue minHeightAsUv = UnitValue.createPointValue(minHeight);
        setProperty(85, minHeightAsUv);
        return this;
    }

    public Image setMinHeight(UnitValue minHeight) {
        setProperty(85, minHeight);
        return this;
    }

    public Image setMaxWidth(float maxWidth) {
        UnitValue minHeightAsUv = UnitValue.createPointValue(maxWidth);
        setProperty(79, minHeightAsUv);
        return this;
    }

    public Image setMaxWidth(UnitValue maxWidth) {
        setProperty(79, maxWidth);
        return this;
    }

    public Image setMinWidth(float minWidth) {
        UnitValue minHeightAsUv = UnitValue.createPointValue(minWidth);
        setProperty(80, minHeightAsUv);
        return this;
    }

    public Image setMinWidth(UnitValue minWidth) {
        setProperty(80, minWidth);
        return this;
    }

    public Image setWidth(float width) {
        setProperty(77, UnitValue.createPointValue(width));
        return this;
    }

    public Image setWidth(UnitValue width) {
        setProperty(77, width);
        return this;
    }

    public UnitValue getWidth() {
        return (UnitValue) getProperty(77);
    }

    public float getImageScaledWidth() {
        if (null == getProperty(29)) {
            return this.xObject.getWidth();
        }
        return this.xObject.getWidth() * ((Float) getProperty(29)).floatValue();
    }

    public float getImageScaledHeight() {
        if (null == getProperty(76)) {
            return this.xObject.getHeight();
        }
        return this.xObject.getHeight() * ((Float) getProperty(76)).floatValue();
    }

    @Override // com.itextpdf.layout.tagging.IAccessibleElement
    public AccessibilityProperties getAccessibilityProperties() {
        if (this.tagProperties == null) {
            this.tagProperties = new DefaultAccessibilityProperties(StandardRoles.FIGURE);
        }
        return this.tagProperties;
    }

    @Override // com.itextpdf.layout.element.AbstractElement
    protected IRenderer makeNewRenderer() {
        return new ImageRenderer(this);
    }

    private static ImageData checkImageType(ImageData image) {
        if (image instanceof WmfImageData) {
            throw new PdfException(PdfException.CannotCreateLayoutImageByWmfImage);
        }
        return image;
    }
}
