package org.apache.poi.xssf.usermodel.charts;

import org.apache.poi.ss.usermodel.charts.AxisCrossBetween;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisOrientation;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.AxisTickMark;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.STCrossBetween;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/charts/XSSFValueAxis.class */
public class XSSFValueAxis extends XSSFChartAxis implements ValueAxis {
    private CTValAx ctValAx;

    public XSSFValueAxis(XSSFChart chart, long id, AxisPosition pos) {
        super(chart);
        createAxis(id, pos);
    }

    public XSSFValueAxis(XSSFChart chart, CTValAx ctValAx) {
        super(chart);
        this.ctValAx = ctValAx;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public long getId() {
        return this.ctValAx.getAxId().getVal();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    @Internal
    public CTShapeProperties getLine() {
        return this.ctValAx.getSpPr();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ValueAxis
    public void setCrossBetween(AxisCrossBetween crossBetween) {
        this.ctValAx.getCrossBetween().setVal(fromCrossBetween(crossBetween));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ValueAxis
    public AxisCrossBetween getCrossBetween() {
        return toCrossBetween(this.ctValAx.getCrossBetween().getVal());
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTAxPos getCTAxPos() {
        return this.ctValAx.getAxPos();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTNumFmt getCTNumFmt() {
        if (this.ctValAx.isSetNumFmt()) {
            return this.ctValAx.getNumFmt();
        }
        return this.ctValAx.addNewNumFmt();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTScaling getCTScaling() {
        return this.ctValAx.getScaling();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTCrosses getCTCrosses() {
        return this.ctValAx.getCrosses();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTBoolean getDelete() {
        return this.ctValAx.getDelete();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTTickMark getMajorCTTickMark() {
        return this.ctValAx.getMajorTickMark();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    protected CTTickMark getMinorCTTickMark() {
        return this.ctValAx.getMinorTickMark();
    }

    @Override // org.apache.poi.xssf.usermodel.charts.XSSFChartAxis
    @Internal
    public CTChartLines getMajorGridLines() {
        return this.ctValAx.getMajorGridlines();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void crossAxis(ChartAxis axis) {
        this.ctValAx.getCrossAx().setVal(axis.getId());
    }

    private void createAxis(long id, AxisPosition pos) {
        this.ctValAx = this.chart.getCTChart().getPlotArea().addNewValAx();
        this.ctValAx.addNewAxId().setVal(id);
        this.ctValAx.addNewAxPos();
        this.ctValAx.addNewScaling();
        this.ctValAx.addNewCrossBetween();
        this.ctValAx.addNewCrosses();
        this.ctValAx.addNewCrossAx();
        this.ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
        this.ctValAx.addNewDelete();
        this.ctValAx.addNewMajorTickMark();
        this.ctValAx.addNewMinorTickMark();
        setPosition(pos);
        setOrientation(AxisOrientation.MIN_MAX);
        setCrossBetween(AxisCrossBetween.MIDPOINT_CATEGORY);
        setCrosses(AxisCrosses.AUTO_ZERO);
        setVisible(true);
        setMajorTickMark(AxisTickMark.CROSS);
        setMinorTickMark(AxisTickMark.NONE);
    }

    private static STCrossBetween.Enum fromCrossBetween(AxisCrossBetween crossBetween) {
        switch (crossBetween) {
            case BETWEEN:
                return STCrossBetween.BETWEEN;
            case MIDPOINT_CATEGORY:
                return STCrossBetween.MID_CAT;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static AxisCrossBetween toCrossBetween(STCrossBetween.Enum ctCrossBetween) {
        switch (ctCrossBetween.intValue()) {
            case 1:
                return AxisCrossBetween.BETWEEN;
            case 2:
                return AxisCrossBetween.MIDPOINT_CATEGORY;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public boolean hasNumberFormat() {
        return this.ctValAx.isSetNumFmt();
    }
}
