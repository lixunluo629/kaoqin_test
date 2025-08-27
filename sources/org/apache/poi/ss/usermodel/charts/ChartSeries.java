package org.apache.poi.ss.usermodel.charts;

import org.apache.poi.ss.util.CellReference;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/charts/ChartSeries.class */
public interface ChartSeries {
    void setTitle(String str);

    void setTitle(CellReference cellReference);

    String getTitleString();

    CellReference getTitleCellReference();

    TitleType getTitleType();
}
