package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ManualLayout.class */
public interface ManualLayout {
    void setTarget(LayoutTarget layoutTarget);

    LayoutTarget getTarget();

    void setXMode(LayoutMode layoutMode);

    LayoutMode getXMode();

    void setYMode(LayoutMode layoutMode);

    LayoutMode getYMode();

    double getX();

    void setX(double d);

    double getY();

    void setY(double d);

    void setWidthMode(LayoutMode layoutMode);

    LayoutMode getWidthMode();

    void setHeightMode(LayoutMode layoutMode);

    LayoutMode getHeightMode();

    void setWidthRatio(double d);

    double getWidthRatio();

    void setHeightRatio(double d);

    double getHeightRatio();
}
