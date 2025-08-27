package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.minmaxwidth.RotationMinMaxWidth;
import java.util.HashMap;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/RotationUtils.class */
final class RotationUtils {
    private RotationUtils() {
    }

    public static MinMaxWidth countRotationMinMaxWidth(MinMaxWidth minMaxWidth, AbstractRenderer renderer) {
        PropertiesBackup backup = new PropertiesBackup(renderer);
        Float rotation = backup.storeFloatProperty(55);
        if (rotation != null) {
            float angle = rotation.floatValue();
            float layoutWidth = minMaxWidth.getMaxWidth() + MinMaxWidthUtils.getEps();
            LayoutResult layoutResult = renderer.layout(new LayoutContext(new LayoutArea(1, new Rectangle(layoutWidth, 1000000.0f))));
            if (layoutResult.getOccupiedArea() != null) {
                Rectangle layoutBBox = layoutResult.getOccupiedArea().getBBox();
                if (MinMaxWidthUtils.isEqual(minMaxWidth.getMinWidth(), minMaxWidth.getMaxWidth())) {
                    backup.restoreProperty(55);
                    float rotatedWidth = (float) RotationMinMaxWidth.calculateRotatedWidth(layoutBBox, angle);
                    return new MinMaxWidth(rotatedWidth, rotatedWidth, 0.0f);
                }
                double area = layoutResult.getOccupiedArea().getBBox().getWidth() * layoutResult.getOccupiedArea().getBBox().getHeight();
                RotationMinMaxWidth rotationMinMaxWidth = RotationMinMaxWidth.calculate(angle, area, minMaxWidth);
                Float rotatedMinWidth = getLayoutRotatedWidth(renderer, (float) rotationMinMaxWidth.getMinWidthOrigin(), layoutBBox, angle);
                if (rotatedMinWidth != null) {
                    if (rotatedMinWidth.floatValue() > rotationMinMaxWidth.getMaxWidth()) {
                        rotationMinMaxWidth.setChildrenMinWidth(rotatedMinWidth.floatValue());
                        Float rotatedMaxWidth = getLayoutRotatedWidth(renderer, (float) rotationMinMaxWidth.getMaxWidthOrigin(), layoutBBox, angle);
                        if (rotatedMaxWidth != null && rotatedMaxWidth.floatValue() > rotatedMinWidth.floatValue()) {
                            rotationMinMaxWidth.setChildrenMaxWidth(rotatedMaxWidth.floatValue());
                        } else {
                            rotationMinMaxWidth.setChildrenMaxWidth(rotatedMinWidth.floatValue());
                        }
                    } else {
                        rotationMinMaxWidth.setChildrenMinWidth(rotatedMinWidth.floatValue());
                    }
                    backup.restoreProperty(55);
                    return rotationMinMaxWidth;
                }
            }
        }
        backup.restoreProperty(55);
        return minMaxWidth;
    }

    public static Float retrieveRotatedLayoutWidth(float availableWidth, AbstractRenderer renderer) {
        PropertiesBackup backup = new PropertiesBackup(renderer);
        Float rotation = backup.storeFloatProperty(55);
        if (rotation != null && renderer.getProperty(77) == null) {
            float angle = rotation.floatValue();
            backup.storeProperty(27);
            backup.storeProperty(85);
            backup.storeProperty(84);
            MinMaxWidth minMaxWidth = renderer.getMinMaxWidth();
            float length = ((minMaxWidth.getMaxWidth() + minMaxWidth.getMinWidth()) / 2.0f) + MinMaxWidthUtils.getEps();
            LayoutResult layoutResult = renderer.layout(new LayoutContext(new LayoutArea(1, new Rectangle(length, 1000000.0f))));
            backup.restoreProperty(27);
            backup.restoreProperty(85);
            backup.restoreProperty(84);
            Rectangle additions = new Rectangle(0.0f, 0.0f);
            renderer.applyPaddings(additions, true);
            renderer.applyBorderBox(additions, true);
            renderer.applyMargins(additions, true);
            if (layoutResult.getOccupiedArea() != null) {
                double area = layoutResult.getOccupiedArea().getBBox().getWidth() * layoutResult.getOccupiedArea().getBBox().getHeight();
                RotationMinMaxWidth result = RotationMinMaxWidth.calculate(angle, area, minMaxWidth, availableWidth);
                if (result != null) {
                    backup.restoreProperty(55);
                    if (result.getMaxWidthHeight() > result.getMinWidthHeight()) {
                        return Float.valueOf((float) ((result.getMinWidthOrigin() - additions.getWidth()) + MinMaxWidthUtils.getEps()));
                    }
                    return Float.valueOf((float) ((result.getMaxWidthOrigin() - additions.getWidth()) + MinMaxWidthUtils.getEps()));
                }
            }
        }
        backup.restoreProperty(55);
        return renderer.retrieveWidth(availableWidth);
    }

    private static Float getLayoutRotatedWidth(AbstractRenderer renderer, float availableWidth, Rectangle previousBBox, double angle) {
        if (MinMaxWidthUtils.isEqual(availableWidth, previousBBox.getWidth())) {
            return Float.valueOf((float) RotationMinMaxWidth.calculateRotatedWidth(previousBBox, angle));
        }
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(1, new Rectangle(availableWidth + MinMaxWidthUtils.getEps(), 1000000.0f))));
        if (result.getOccupiedArea() != null) {
            return Float.valueOf((float) RotationMinMaxWidth.calculateRotatedWidth(result.getOccupiedArea().getBBox(), angle));
        }
        return null;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/RotationUtils$PropertiesBackup.class */
    private static class PropertiesBackup {
        private AbstractRenderer renderer;
        private HashMap<Integer, PropertyBackup> propertiesBackup = new HashMap<>();

        public PropertiesBackup(AbstractRenderer renderer) {
            this.renderer = renderer;
        }

        public Float storeFloatProperty(int property) {
            Float value = this.renderer.getPropertyAsFloat(property);
            if (value != null) {
                this.propertiesBackup.put(Integer.valueOf(property), new PropertyBackup(value, this.renderer.hasOwnProperty(property)));
                this.renderer.setProperty(property, null);
            }
            return value;
        }

        public <T> T storeProperty(int i) {
            T t = (T) this.renderer.getProperty(i);
            if (t != null) {
                this.propertiesBackup.put(Integer.valueOf(i), new PropertyBackup(t, this.renderer.hasOwnProperty(i)));
                this.renderer.setProperty(i, null);
            }
            return t;
        }

        public void restoreProperty(int property) {
            PropertyBackup backup = this.propertiesBackup.remove(Integer.valueOf(property));
            if (backup != null) {
                if (backup.isOwnedByRender()) {
                    this.renderer.setProperty(property, backup.getValue());
                } else {
                    this.renderer.deleteOwnProperty(property);
                }
            }
        }

        /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/RotationUtils$PropertiesBackup$PropertyBackup.class */
        private static class PropertyBackup {
            private Object propertyValue;
            private boolean isOwnedByRender;

            public PropertyBackup(Object propertyValue, boolean isOwnedByRender) {
                this.propertyValue = propertyValue;
                this.isOwnedByRender = isOwnedByRender;
            }

            public Object getValue() {
                return this.propertyValue;
            }

            public boolean isOwnedByRender() {
                return this.isOwnedByRender;
            }
        }
    }
}
