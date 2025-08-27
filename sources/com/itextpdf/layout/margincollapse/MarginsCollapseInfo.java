package com.itextpdf.layout.margincollapse;

import java.io.Serializable;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/margincollapse/MarginsCollapseInfo.class */
public class MarginsCollapseInfo implements Serializable {
    private boolean ignoreOwnMarginTop;
    private boolean ignoreOwnMarginBottom;
    private MarginsCollapse collapseBefore;
    private MarginsCollapse collapseAfter;
    private MarginsCollapse ownCollapseAfter;
    private boolean isSelfCollapsing;
    private float bufferSpaceOnTop;
    private float bufferSpaceOnBottom;
    private float usedBufferSpaceOnTop;
    private float usedBufferSpaceOnBottom;
    private boolean clearanceApplied;

    MarginsCollapseInfo() {
        this.ignoreOwnMarginTop = false;
        this.ignoreOwnMarginBottom = false;
        this.collapseBefore = new MarginsCollapse();
        this.collapseAfter = new MarginsCollapse();
        this.isSelfCollapsing = true;
        this.bufferSpaceOnTop = 0.0f;
        this.bufferSpaceOnBottom = 0.0f;
        this.usedBufferSpaceOnTop = 0.0f;
        this.usedBufferSpaceOnBottom = 0.0f;
        this.clearanceApplied = false;
    }

    MarginsCollapseInfo(boolean ignoreOwnMarginTop, boolean ignoreOwnMarginBottom, MarginsCollapse collapseBefore, MarginsCollapse collapseAfter) {
        this.ignoreOwnMarginTop = ignoreOwnMarginTop;
        this.ignoreOwnMarginBottom = ignoreOwnMarginBottom;
        this.collapseBefore = collapseBefore;
        this.collapseAfter = collapseAfter;
        this.isSelfCollapsing = true;
        this.bufferSpaceOnTop = 0.0f;
        this.bufferSpaceOnBottom = 0.0f;
        this.usedBufferSpaceOnTop = 0.0f;
        this.usedBufferSpaceOnBottom = 0.0f;
        this.clearanceApplied = false;
    }

    public void copyTo(MarginsCollapseInfo destInfo) {
        destInfo.ignoreOwnMarginTop = this.ignoreOwnMarginTop;
        destInfo.ignoreOwnMarginBottom = this.ignoreOwnMarginBottom;
        destInfo.collapseBefore = this.collapseBefore;
        destInfo.collapseAfter = this.collapseAfter;
        destInfo.setOwnCollapseAfter(this.ownCollapseAfter);
        destInfo.setSelfCollapsing(this.isSelfCollapsing);
        destInfo.setBufferSpaceOnTop(this.bufferSpaceOnTop);
        destInfo.setBufferSpaceOnBottom(this.bufferSpaceOnBottom);
        destInfo.setUsedBufferSpaceOnTop(this.usedBufferSpaceOnTop);
        destInfo.setUsedBufferSpaceOnBottom(this.usedBufferSpaceOnBottom);
        destInfo.setClearanceApplied(this.clearanceApplied);
    }

    MarginsCollapse getCollapseBefore() {
        return this.collapseBefore;
    }

    MarginsCollapse getCollapseAfter() {
        return this.collapseAfter;
    }

    void setCollapseAfter(MarginsCollapse collapseAfter) {
        this.collapseAfter = collapseAfter;
    }

    MarginsCollapse getOwnCollapseAfter() {
        return this.ownCollapseAfter;
    }

    void setOwnCollapseAfter(MarginsCollapse marginsCollapse) {
        this.ownCollapseAfter = marginsCollapse;
    }

    void setSelfCollapsing(boolean selfCollapsing) {
        this.isSelfCollapsing = selfCollapsing;
    }

    boolean isSelfCollapsing() {
        return this.isSelfCollapsing;
    }

    boolean isIgnoreOwnMarginTop() {
        return this.ignoreOwnMarginTop;
    }

    boolean isIgnoreOwnMarginBottom() {
        return this.ignoreOwnMarginBottom;
    }

    float getBufferSpaceOnTop() {
        return this.bufferSpaceOnTop;
    }

    void setBufferSpaceOnTop(float bufferSpaceOnTop) {
        this.bufferSpaceOnTop = bufferSpaceOnTop;
    }

    float getBufferSpaceOnBottom() {
        return this.bufferSpaceOnBottom;
    }

    void setBufferSpaceOnBottom(float bufferSpaceOnBottom) {
        this.bufferSpaceOnBottom = bufferSpaceOnBottom;
    }

    float getUsedBufferSpaceOnTop() {
        return this.usedBufferSpaceOnTop;
    }

    void setUsedBufferSpaceOnTop(float usedBufferSpaceOnTop) {
        this.usedBufferSpaceOnTop = usedBufferSpaceOnTop;
    }

    float getUsedBufferSpaceOnBottom() {
        return this.usedBufferSpaceOnBottom;
    }

    void setUsedBufferSpaceOnBottom(float usedBufferSpaceOnBottom) {
        this.usedBufferSpaceOnBottom = usedBufferSpaceOnBottom;
    }

    boolean isClearanceApplied() {
        return this.clearanceApplied;
    }

    void setClearanceApplied(boolean clearanceApplied) {
        this.clearanceApplied = clearanceApplied;
    }
}
