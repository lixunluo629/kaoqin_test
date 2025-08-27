package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/LineChartSeries.class */
public interface LineChartSeries extends ChartSeries {
    ChartDataSource<?> getCategoryAxisData();

    ChartDataSource<? extends Number> getValues();
}
