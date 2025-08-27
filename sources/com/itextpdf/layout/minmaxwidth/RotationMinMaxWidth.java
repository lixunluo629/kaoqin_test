package com.itextpdf.layout.minmaxwidth;

import com.itextpdf.kernel.geom.Rectangle;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/minmaxwidth/RotationMinMaxWidth.class */
public class RotationMinMaxWidth extends MinMaxWidth {
    private double minWidthOrigin;
    private double maxWidthOrigin;
    private double minWidthHeight;
    private double maxWidthHeight;

    public RotationMinMaxWidth(double minWidth, double maxWidth, double minWidthOrigin, double maxWidthOrigin, double minWidthHeight, double maxWidthHeight) {
        super((float) minWidth, (float) maxWidth, 0.0f);
        this.maxWidthOrigin = maxWidthOrigin;
        this.minWidthOrigin = minWidthOrigin;
        this.minWidthHeight = minWidthHeight;
        this.maxWidthHeight = maxWidthHeight;
    }

    public double getMinWidthOrigin() {
        return this.minWidthOrigin;
    }

    public double getMaxWidthOrigin() {
        return this.maxWidthOrigin;
    }

    public double getMinWidthHeight() {
        return this.minWidthHeight;
    }

    public double getMaxWidthHeight() {
        return this.maxWidthHeight;
    }

    public static RotationMinMaxWidth calculate(double angle, double area, MinMaxWidth elementMinMaxWidth) {
        WidthFunction function = new WidthFunction(angle, area);
        return calculate(function, elementMinMaxWidth.getMinWidth(), elementMinMaxWidth.getMaxWidth());
    }

    public static RotationMinMaxWidth calculate(double angle, double area, MinMaxWidth elementMinMaxWidth, double availableWidth) {
        WidthFunction function = new WidthFunction(angle, area);
        WidthFunction.Interval validArguments = function.getValidOriginalWidths(availableWidth);
        if (validArguments == null) {
            return null;
        }
        double xMin = Math.max(elementMinMaxWidth.getMinWidth(), validArguments.getMin());
        double xMax = Math.min(elementMinMaxWidth.getMaxWidth(), validArguments.getMax());
        if (xMax < xMin) {
            double rotatedWidth = function.getRotatedWidth(xMin);
            double rotatedHeight = function.getRotatedHeight(xMin);
            return new RotationMinMaxWidth(rotatedWidth, rotatedWidth, xMin, xMin, rotatedHeight, rotatedHeight);
        }
        return calculate(function, xMin, xMax);
    }

    public static double calculateRotatedWidth(Rectangle area, double angle) {
        return (area.getWidth() * cos(angle)) + (area.getHeight() * sin(angle));
    }

    private static RotationMinMaxWidth calculate(WidthFunction func, double xMin, double xMax) {
        double minWidthOrigin;
        double maxWidthOrigin;
        double x0 = func.getWidthDerivativeZeroPoint();
        if (x0 < xMin) {
            minWidthOrigin = xMin;
            maxWidthOrigin = xMax;
        } else if (x0 > xMax) {
            minWidthOrigin = xMax;
            maxWidthOrigin = xMin;
        } else {
            minWidthOrigin = x0;
            maxWidthOrigin = func.getRotatedWidth(xMax) > func.getRotatedWidth(xMin) ? xMax : xMin;
        }
        return new RotationMinMaxWidth(func.getRotatedWidth(minWidthOrigin), func.getRotatedWidth(maxWidthOrigin), minWidthOrigin, maxWidthOrigin, func.getRotatedHeight(minWidthOrigin), func.getRotatedHeight(maxWidthOrigin));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double sin(double angle) {
        return correctSinCos(Math.abs(Math.sin(angle)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double cos(double angle) {
        return correctSinCos(Math.abs(Math.cos(angle)));
    }

    private static double correctSinCos(double value) {
        if (MinMaxWidthUtils.isEqual(value, 0.0d)) {
            return 0.0d;
        }
        if (MinMaxWidthUtils.isEqual(value, 1.0d)) {
            return 1.0d;
        }
        return value;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/minmaxwidth/RotationMinMaxWidth$WidthFunction.class */
    private static class WidthFunction {
        private double sin;
        private double cos;
        private double area;

        public WidthFunction(double angle, double area) {
            this.sin = RotationMinMaxWidth.sin(angle);
            this.cos = RotationMinMaxWidth.cos(angle);
            this.area = area;
        }

        public double getRotatedWidth(double x) {
            return (x * this.cos) + ((this.area * this.sin) / x);
        }

        public double getRotatedHeight(double x) {
            return (x * this.sin) + ((this.area * this.cos) / x);
        }

        public Interval getValidOriginalWidths(double availableWidth) {
            double minWidth;
            double maxWidth;
            if (this.cos == 0.0d) {
                minWidth = (this.area * this.sin) / availableWidth;
                maxWidth = MinMaxWidthUtils.getInfWidth();
            } else if (this.sin == 0.0d) {
                minWidth = 0.0d;
                maxWidth = availableWidth / this.cos;
            } else {
                double D = (availableWidth * availableWidth) - (((4.0d * this.area) * this.sin) * this.cos);
                if (D < 0.0d) {
                    return null;
                }
                minWidth = (availableWidth - Math.sqrt(D)) / (2.0d * this.cos);
                maxWidth = (availableWidth + Math.sqrt(D)) / (2.0d * this.cos);
            }
            return new Interval(minWidth, maxWidth);
        }

        public double getWidthDerivativeZeroPoint() {
            return Math.sqrt((this.area * this.sin) / this.cos);
        }

        /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/minmaxwidth/RotationMinMaxWidth$WidthFunction$Interval.class */
        public static class Interval {
            private double min;
            private double max;

            public Interval(double min, double max) {
                this.min = min;
                this.max = max;
            }

            public double getMin() {
                return this.min;
            }

            public double getMax() {
                return this.max;
            }
        }
    }
}
