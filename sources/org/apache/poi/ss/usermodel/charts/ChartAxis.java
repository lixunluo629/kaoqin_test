package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ChartAxis.class */
public interface ChartAxis {
    long getId();

    AxisPosition getPosition();

    void setPosition(AxisPosition axisPosition);

    String getNumberFormat();

    void setNumberFormat(String str);

    boolean isSetLogBase();

    void setLogBase(double d);

    double getLogBase();

    boolean isSetMinimum();

    double getMinimum();

    void setMinimum(double d);

    boolean isSetMaximum();

    double getMaximum();

    void setMaximum(double d);

    AxisOrientation getOrientation();

    void setOrientation(AxisOrientation axisOrientation);

    void setCrosses(AxisCrosses axisCrosses);

    AxisCrosses getCrosses();

    void crossAxis(ChartAxis chartAxis);

    boolean isVisible();

    void setVisible(boolean z);

    AxisTickMark getMajorTickMark();

    void setMajorTickMark(AxisTickMark axisTickMark);

    AxisTickMark getMinorTickMark();

    void setMinorTickMark(AxisTickMark axisTickMark);

    boolean hasNumberFormat();
}
