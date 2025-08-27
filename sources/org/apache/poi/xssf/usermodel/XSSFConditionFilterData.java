package org.apache.poi.xssf.usermodel;

import org.apache.poi.ss.usermodel.ConditionFilterData;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFConditionFilterData.class */
public class XSSFConditionFilterData implements ConditionFilterData {
    private final CTCfRule _cfRule;

    XSSFConditionFilterData(CTCfRule cfRule) {
        this._cfRule = cfRule;
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public boolean getAboveAverage() {
        return this._cfRule.getAboveAverage();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public boolean getBottom() {
        return this._cfRule.getBottom();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public boolean getEqualAverage() {
        return this._cfRule.getEqualAverage();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public boolean getPercent() {
        return this._cfRule.getPercent();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public long getRank() {
        return this._cfRule.getRank();
    }

    @Override // org.apache.poi.ss.usermodel.ConditionFilterData
    public int getStdDev() {
        return this._cfRule.getStdDev();
    }
}
