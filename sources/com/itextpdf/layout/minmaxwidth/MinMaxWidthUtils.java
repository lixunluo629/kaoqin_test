package com.itextpdf.layout.minmaxwidth;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/minmaxwidth/MinMaxWidthUtils.class */
public final class MinMaxWidthUtils {
    private static final float eps = 0.01f;
    private static final float max = 32760.0f;

    public static float getEps() {
        return eps;
    }

    public static float getInfWidth() {
        return max;
    }

    private static float getInfHeight() {
        return 1000000.0f;
    }

    public static boolean isEqual(double x, double y) {
        return Math.abs(x - y) < 0.009999999776482582d;
    }

    public static MinMaxWidth countDefaultMinMaxWidth(IRenderer renderer) {
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(1, new Rectangle(getInfWidth(), getInfHeight()))));
        return result.getStatus() == 3 ? new MinMaxWidth() : new MinMaxWidth(0.0f, result.getOccupiedArea().getBBox().getWidth(), 0.0f);
    }

    public static float getBorderWidth(IPropertyContainer element) {
        Border border = (Border) element.getProperty(9);
        Border rightBorder = (Border) element.getProperty(12);
        Border leftBorder = (Border) element.getProperty(11);
        if (!element.hasOwnProperty(12)) {
            rightBorder = border;
        }
        if (!element.hasOwnProperty(11)) {
            leftBorder = border;
        }
        float rightBorderWidth = rightBorder != null ? rightBorder.getWidth() : 0.0f;
        float leftBorderWidth = leftBorder != null ? leftBorder.getWidth() : 0.0f;
        return rightBorderWidth + leftBorderWidth;
    }

    public static float getMarginsWidth(IPropertyContainer element) {
        UnitValue rightMargin = (UnitValue) element.getProperty(45);
        if (null != rightMargin && !rightMargin.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MinMaxWidthUtils.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
        }
        UnitValue leftMargin = (UnitValue) element.getProperty(44);
        if (null != leftMargin && !leftMargin.isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) MinMaxWidthUtils.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        float rightMarginWidth = rightMargin != null ? rightMargin.getValue() : 0.0f;
        float leftMarginWidth = leftMargin != null ? leftMargin.getValue() : 0.0f;
        return rightMarginWidth + leftMarginWidth;
    }

    public static float getPaddingWidth(IPropertyContainer element) {
        UnitValue rightPadding = (UnitValue) element.getProperty(49);
        if (null != rightPadding && !rightPadding.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) MinMaxWidthUtils.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 49));
        }
        UnitValue leftPadding = (UnitValue) element.getProperty(48);
        if (null != leftPadding && !leftPadding.isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) MinMaxWidthUtils.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 48));
        }
        float rightPaddingWidth = rightPadding != null ? rightPadding.getValue() : 0.0f;
        float leftPaddingWidth = leftPadding != null ? leftPadding.getValue() : 0.0f;
        return rightPaddingWidth + leftPaddingWidth;
    }
}
