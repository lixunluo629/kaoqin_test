package org.apache.poi.xssf.usermodel.charts;

import org.apache.poi.ss.usermodel.charts.ChartSeries;
import org.apache.poi.ss.usermodel.charts.TitleType;
import org.apache.poi.ss.util.CellReference;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/charts/AbstractXSSFChartSeries.class */
public abstract class AbstractXSSFChartSeries implements ChartSeries {
    private String titleValue;
    private CellReference titleRef;
    private TitleType titleType;

    @Override // org.apache.poi.ss.usermodel.charts.ChartSeries
    public void setTitle(CellReference titleReference) {
        this.titleType = TitleType.CELL_REFERENCE;
        this.titleRef = titleReference;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartSeries
    public void setTitle(String title) {
        this.titleType = TitleType.STRING;
        this.titleValue = title;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartSeries
    public CellReference getTitleCellReference() {
        if (TitleType.CELL_REFERENCE.equals(this.titleType)) {
            return this.titleRef;
        }
        throw new IllegalStateException("Title type is not CellReference.");
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartSeries
    public String getTitleString() {
        if (TitleType.STRING.equals(this.titleType)) {
            return this.titleValue;
        }
        throw new IllegalStateException("Title type is not String.");
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartSeries
    public TitleType getTitleType() {
        return this.titleType;
    }

    protected boolean isTitleSet() {
        return this.titleType != null;
    }

    protected CTSerTx getCTSerTx() {
        CTSerTx tx = CTSerTx.Factory.newInstance();
        switch (this.titleType) {
            case CELL_REFERENCE:
                tx.addNewStrRef().setF(this.titleRef.formatAsString());
                return tx;
            case STRING:
                tx.setV(this.titleValue);
                return tx;
            default:
                throw new IllegalStateException("Unkown title type: " + this.titleType);
        }
    }
}
