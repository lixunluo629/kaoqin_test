package org.apache.poi.xssf.usermodel.charts;

import org.apache.poi.ss.usermodel.charts.LayoutMode;
import org.apache.poi.ss.usermodel.charts.LayoutTarget;
import org.apache.poi.ss.usermodel.charts.ManualLayout;
import org.apache.poi.util.Internal;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutMode;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutTarget;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/charts/XSSFManualLayout.class */
public final class XSSFManualLayout implements ManualLayout {
    private CTManualLayout layout;
    private static final LayoutMode defaultLayoutMode = LayoutMode.EDGE;
    private static final LayoutTarget defaultLayoutTarget = LayoutTarget.INNER;

    public XSSFManualLayout(CTLayout ctLayout) {
        initLayout(ctLayout);
    }

    public XSSFManualLayout(XSSFChart chart) {
        CTPlotArea ctPlotArea = chart.getCTChart().getPlotArea();
        CTLayout ctLayout = ctPlotArea.isSetLayout() ? ctPlotArea.getLayout() : ctPlotArea.addNewLayout();
        initLayout(ctLayout);
    }

    @Internal
    public CTManualLayout getCTManualLayout() {
        return this.layout;
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setWidthRatio(double ratio) {
        if (!this.layout.isSetW()) {
            this.layout.addNewW();
        }
        this.layout.getW().setVal(ratio);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public double getWidthRatio() {
        if (!this.layout.isSetW()) {
            return 0.0d;
        }
        return this.layout.getW().getVal();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setHeightRatio(double ratio) {
        if (!this.layout.isSetH()) {
            this.layout.addNewH();
        }
        this.layout.getH().setVal(ratio);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public double getHeightRatio() {
        if (!this.layout.isSetH()) {
            return 0.0d;
        }
        return this.layout.getH().getVal();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public LayoutTarget getTarget() {
        if (!this.layout.isSetLayoutTarget()) {
            return defaultLayoutTarget;
        }
        return toLayoutTarget(this.layout.getLayoutTarget());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setTarget(LayoutTarget target) {
        if (!this.layout.isSetLayoutTarget()) {
            this.layout.addNewLayoutTarget();
        }
        this.layout.getLayoutTarget().setVal(fromLayoutTarget(target));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public LayoutMode getXMode() {
        if (!this.layout.isSetXMode()) {
            return defaultLayoutMode;
        }
        return toLayoutMode(this.layout.getXMode());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setXMode(LayoutMode mode) {
        if (!this.layout.isSetXMode()) {
            this.layout.addNewXMode();
        }
        this.layout.getXMode().setVal(fromLayoutMode(mode));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public LayoutMode getYMode() {
        if (!this.layout.isSetYMode()) {
            return defaultLayoutMode;
        }
        return toLayoutMode(this.layout.getYMode());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setYMode(LayoutMode mode) {
        if (!this.layout.isSetYMode()) {
            this.layout.addNewYMode();
        }
        this.layout.getYMode().setVal(fromLayoutMode(mode));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public double getX() {
        if (!this.layout.isSetX()) {
            return 0.0d;
        }
        return this.layout.getX().getVal();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setX(double x) {
        if (!this.layout.isSetX()) {
            this.layout.addNewX();
        }
        this.layout.getX().setVal(x);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public double getY() {
        if (!this.layout.isSetY()) {
            return 0.0d;
        }
        return this.layout.getY().getVal();
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setY(double y) {
        if (!this.layout.isSetY()) {
            this.layout.addNewY();
        }
        this.layout.getY().setVal(y);
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public LayoutMode getWidthMode() {
        if (!this.layout.isSetWMode()) {
            return defaultLayoutMode;
        }
        return toLayoutMode(this.layout.getWMode());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setWidthMode(LayoutMode mode) {
        if (!this.layout.isSetWMode()) {
            this.layout.addNewWMode();
        }
        this.layout.getWMode().setVal(fromLayoutMode(mode));
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public LayoutMode getHeightMode() {
        if (!this.layout.isSetHMode()) {
            return defaultLayoutMode;
        }
        return toLayoutMode(this.layout.getHMode());
    }

    @Override // org.apache.poi.ss.usermodel.charts.ManualLayout
    public void setHeightMode(LayoutMode mode) {
        if (!this.layout.isSetHMode()) {
            this.layout.addNewHMode();
        }
        this.layout.getHMode().setVal(fromLayoutMode(mode));
    }

    private void initLayout(CTLayout ctLayout) {
        if (ctLayout.isSetManualLayout()) {
            this.layout = ctLayout.getManualLayout();
        } else {
            this.layout = ctLayout.addNewManualLayout();
        }
    }

    private STLayoutMode.Enum fromLayoutMode(LayoutMode mode) {
        switch (mode) {
            case EDGE:
                return STLayoutMode.EDGE;
            case FACTOR:
                return STLayoutMode.FACTOR;
            default:
                throw new IllegalArgumentException();
        }
    }

    private LayoutMode toLayoutMode(CTLayoutMode ctLayoutMode) {
        switch (ctLayoutMode.getVal().intValue()) {
            case 1:
                return LayoutMode.EDGE;
            case 2:
                return LayoutMode.FACTOR;
            default:
                throw new IllegalArgumentException();
        }
    }

    private STLayoutTarget.Enum fromLayoutTarget(LayoutTarget target) {
        switch (target) {
            case INNER:
                return STLayoutTarget.INNER;
            case OUTER:
                return STLayoutTarget.OUTER;
            default:
                throw new IllegalArgumentException();
        }
    }

    private LayoutTarget toLayoutTarget(CTLayoutTarget ctLayoutTarget) {
        switch (ctLayoutTarget.getVal().intValue()) {
            case 1:
                return LayoutTarget.INNER;
            case 2:
                return LayoutTarget.OUTER;
            default:
                throw new IllegalArgumentException();
        }
    }
}
