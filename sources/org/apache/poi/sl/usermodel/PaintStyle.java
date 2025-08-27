package org.apache.poi.sl.usermodel;

import java.io.InputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle.class */
public interface PaintStyle {

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle$GradientPaint.class */
    public interface GradientPaint extends PaintStyle {

        /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle$GradientPaint$GradientType.class */
        public enum GradientType {
            linear,
            circular,
            shape
        }

        double getGradientAngle();

        ColorStyle[] getGradientColors();

        float[] getGradientFractions();

        boolean isRotatedWithShape();

        GradientType getGradientType();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle$PaintModifier.class */
    public enum PaintModifier {
        NONE,
        NORM,
        LIGHTEN,
        LIGHTEN_LESS,
        DARKEN,
        DARKEN_LESS
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle$SolidPaint.class */
    public interface SolidPaint extends PaintStyle {
        ColorStyle getSolidColor();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/PaintStyle$TexturePaint.class */
    public interface TexturePaint extends PaintStyle {
        InputStream getImageData();

        String getContentType();

        int getAlpha();
    }
}
