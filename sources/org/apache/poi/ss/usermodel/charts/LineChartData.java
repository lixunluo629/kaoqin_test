package org.apache.poi.ss.usermodel.charts;

import java.util.List;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/LineChartData.class */
public interface LineChartData extends ChartData {
    LineChartSeries addSeries(ChartDataSource<?> chartDataSource, ChartDataSource<? extends Number> chartDataSource2);

    List<? extends LineChartSeries> getSeries();
}
