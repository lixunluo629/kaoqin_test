package org.apache.poi.xssf.usermodel.charts;

import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisOrientation;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.AxisTickMark;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
import org.openxmlformats.schemas.drawingml.x2006.chart.STAxPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses;
import org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/charts/XSSFChartAxis.class */
public abstract class XSSFChartAxis implements ChartAxis {
    protected XSSFChart chart;
    private static final double MIN_LOG_BASE = 2.0d;
    private static final double MAX_LOG_BASE = 1000.0d;

    protected abstract CTAxPos getCTAxPos();

    protected abstract CTNumFmt getCTNumFmt();

    protected abstract CTScaling getCTScaling();

    protected abstract CTCrosses getCTCrosses();

    protected abstract CTBoolean getDelete();

    protected abstract CTTickMark getMajorCTTickMark();

    protected abstract CTTickMark getMinorCTTickMark();

    @Internal
    public abstract CTChartLines getMajorGridLines();

    @Internal
    public abstract CTShapeProperties getLine();

    protected XSSFChartAxis(XSSFChart chart) {
        this.chart = chart;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public AxisPosition getPosition() {
        return toAxisPosition(getCTAxPos());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setPosition(AxisPosition position) {
        getCTAxPos().setVal(fromAxisPosition(position));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setNumberFormat(String format) {
        getCTNumFmt().setFormatCode(format);
        getCTNumFmt().setSourceLinked(true);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public String getNumberFormat() {
        return getCTNumFmt().getFormatCode();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public boolean isSetLogBase() {
        return getCTScaling().isSetLogBase();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setLogBase(double logBase) {
        if (logBase < MIN_LOG_BASE || MAX_LOG_BASE < logBase) {
            throw new IllegalArgumentException("Axis log base must be between 2 and 1000 (inclusive), got: " + logBase);
        }
        CTScaling scaling = getCTScaling();
        if (scaling.isSetLogBase()) {
            scaling.getLogBase().setVal(logBase);
        } else {
            scaling.addNewLogBase().setVal(logBase);
        }
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public double getLogBase() {
        CTLogBase logBase = getCTScaling().getLogBase();
        if (logBase != null) {
            return logBase.getVal();
        }
        return 0.0d;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public boolean isSetMinimum() {
        return getCTScaling().isSetMin();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setMinimum(double min) {
        CTScaling scaling = getCTScaling();
        if (scaling.isSetMin()) {
            scaling.getMin().setVal(min);
        } else {
            scaling.addNewMin().setVal(min);
        }
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public double getMinimum() {
        CTScaling scaling = getCTScaling();
        if (scaling.isSetMin()) {
            return scaling.getMin().getVal();
        }
        return 0.0d;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public boolean isSetMaximum() {
        return getCTScaling().isSetMax();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setMaximum(double max) {
        CTScaling scaling = getCTScaling();
        if (scaling.isSetMax()) {
            scaling.getMax().setVal(max);
        } else {
            scaling.addNewMax().setVal(max);
        }
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public double getMaximum() {
        CTScaling scaling = getCTScaling();
        if (scaling.isSetMax()) {
            return scaling.getMax().getVal();
        }
        return 0.0d;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public AxisOrientation getOrientation() {
        return toAxisOrientation(getCTScaling().getOrientation());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setOrientation(AxisOrientation orientation) {
        CTScaling scaling = getCTScaling();
        STOrientation.Enum stOrientation = fromAxisOrientation(orientation);
        if (scaling.isSetOrientation()) {
            scaling.getOrientation().setVal(stOrientation);
        } else {
            getCTScaling().addNewOrientation().setVal(stOrientation);
        }
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public AxisCrosses getCrosses() {
        return toAxisCrosses(getCTCrosses());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setCrosses(AxisCrosses crosses) {
        getCTCrosses().setVal(fromAxisCrosses(crosses));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public boolean isVisible() {
        return !getDelete().getVal();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setVisible(boolean value) {
        getDelete().setVal(!value);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public AxisTickMark getMajorTickMark() {
        return toAxisTickMark(getMajorCTTickMark());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setMajorTickMark(AxisTickMark tickMark) {
        getMajorCTTickMark().setVal(fromAxisTickMark(tickMark));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public AxisTickMark getMinorTickMark() {
        return toAxisTickMark(getMinorCTTickMark());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ChartAxis
    public void setMinorTickMark(AxisTickMark tickMark) {
        getMinorCTTickMark().setVal(fromAxisTickMark(tickMark));
    }

    private static STOrientation.Enum fromAxisOrientation(AxisOrientation orientation) {
        switch (orientation) {
            case MIN_MAX:
                return STOrientation.MIN_MAX;
            case MAX_MIN:
                return STOrientation.MAX_MIN;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static AxisOrientation toAxisOrientation(CTOrientation ctOrientation) {
        switch (ctOrientation.getVal().intValue()) {
            case 1:
                return AxisOrientation.MAX_MIN;
            case 2:
                return AxisOrientation.MIN_MAX;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static STCrosses.Enum fromAxisCrosses(AxisCrosses crosses) {
        switch (crosses) {
            case AUTO_ZERO:
                return STCrosses.AUTO_ZERO;
            case MIN:
                return STCrosses.MIN;
            case MAX:
                return STCrosses.MAX;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static AxisCrosses toAxisCrosses(CTCrosses ctCrosses) {
        switch (ctCrosses.getVal().intValue()) {
            case 1:
                return AxisCrosses.AUTO_ZERO;
            case 2:
                return AxisCrosses.MAX;
            case 3:
                return AxisCrosses.MIN;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static STAxPos.Enum fromAxisPosition(AxisPosition position) {
        switch (position) {
            case BOTTOM:
                return STAxPos.B;
            case LEFT:
                return STAxPos.L;
            case RIGHT:
                return STAxPos.R;
            case TOP:
                return STAxPos.T;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static AxisPosition toAxisPosition(CTAxPos ctAxPos) {
        switch (ctAxPos.getVal().intValue()) {
            case 1:
                return AxisPosition.BOTTOM;
            case 2:
                return AxisPosition.LEFT;
            case 3:
                return AxisPosition.RIGHT;
            case 4:
                return AxisPosition.TOP;
            default:
                return AxisPosition.BOTTOM;
        }
    }

    private static STTickMark.Enum fromAxisTickMark(AxisTickMark tickMark) {
        switch (tickMark) {
            case NONE:
                return STTickMark.NONE;
            case IN:
                return STTickMark.IN;
            case OUT:
                return STTickMark.OUT;
            case CROSS:
                return STTickMark.CROSS;
            default:
                throw new IllegalArgumentException("Unknown AxisTickMark: " + tickMark);
        }
    }

    private static AxisTickMark toAxisTickMark(CTTickMark ctTickMark) {
        switch (ctTickMark.getVal().intValue()) {
            case 1:
                return AxisTickMark.CROSS;
            case 2:
                return AxisTickMark.IN;
            case 3:
                return AxisTickMark.NONE;
            case 4:
                return AxisTickMark.OUT;
            default:
                return AxisTickMark.CROSS;
        }
    }
}
