package org.apache.poi.ss.usermodel.charts;

import java.util.List;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ScatterChartData.class */
public interface ScatterChartData extends ChartData {
    ScatterChartSeries addSerie(ChartDataSource<?> chartDataSource, ChartDataSource<? extends Number> chartDataSource2);

    List<? extends ScatterChartSeries> getSeries();
}
