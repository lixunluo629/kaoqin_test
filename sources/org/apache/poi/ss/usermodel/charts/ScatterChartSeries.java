package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ScatterChartSeries.class */
public interface ScatterChartSeries extends ChartSeries {
    ChartDataSource<?> getXValues();

    ChartDataSource<? extends Number> getYValues();
}
