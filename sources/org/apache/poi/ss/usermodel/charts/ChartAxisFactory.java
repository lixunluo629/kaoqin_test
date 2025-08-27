package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ChartAxisFactory.class */
public interface ChartAxisFactory {
    ValueAxis createValueAxis(AxisPosition axisPosition);

    ChartAxis createCategoryAxis(AxisPosition axisPosition);

    ChartAxis createDateAxis(AxisPosition axisPosition);
}
