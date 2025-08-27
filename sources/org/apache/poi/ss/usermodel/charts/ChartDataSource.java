package org.apache.poi.ss.usermodel.charts;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ChartDataSource.class */
public interface ChartDataSource<T> {
    int getPointCount();

    T getPointAt(int i);

    boolean isReference();

    boolean isNumeric();

    String getFormulaString();
}
