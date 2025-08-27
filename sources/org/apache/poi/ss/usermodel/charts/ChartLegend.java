package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ChartLegend.class */
public interface ChartLegend extends ManuallyPositionable {
    LegendPosition getPosition();

    void setPosition(LegendPosition legendPosition);

    boolean isOverlay();

    void setOverlay(boolean z);
}
