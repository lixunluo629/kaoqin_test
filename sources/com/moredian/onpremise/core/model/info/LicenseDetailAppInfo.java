package com.moredian.onpremise.core.model.info;

import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/LicenseDetailAppInfo.class */
public class LicenseDetailAppInfo implements Serializable {
    private static final long serialVersionUID = -6194050153885548416L;
    private Integer appType;
    private Integer appNum;
    private String appUnit;

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setAppNum(Integer appNum) {
        this.appNum = appNum;
    }

    public void setAppUnit(String appUnit) {
        this.appUnit = appUnit;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LicenseDetailAppInfo)) {
            return false;
        }
        LicenseDetailAppInfo other = (LicenseDetailAppInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        if (this$appType == null) {
            if (other$appType != null) {
                return false;
            }
        } else if (!this$appType.equals(other$appType)) {
            return false;
        }
        Object this$appNum = getAppNum();
        Object other$appNum = other.getAppNum();
        if (this$appNum == null) {
            if (other$appNum != null) {
                return false;
            }
        } else if (!this$appNum.equals(other$appNum)) {
            return false;
        }
        Object this$appUnit = getAppUnit();
        Object other$appUnit = other.getAppUnit();
        return this$appUnit == null ? other$appUnit == null : this$appUnit.equals(other$appUnit);
    }

    protected boolean canEqual(Object other) {
        return other instanceof LicenseDetailAppInfo;
    }

    public int hashCode() {
        Object $appType = getAppType();
        int result = (1 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $appNum = getAppNum();
        int result2 = (result * 59) + ($appNum == null ? 43 : $appNum.hashCode());
        Object $appUnit = getAppUnit();
        return (result2 * 59) + ($appUnit == null ? 43 : $appUnit.hashCode());
    }

    public String toString() {
        return "LicenseDetailAppInfo(appType=" + getAppType() + ", appNum=" + getAppNum() + ", appUnit=" + getAppUnit() + ")";
    }

    public Integer getAppType() {
        return this.appType;
    }

    public Integer getAppNum() {
        return this.appNum;
    }

    public String getAppUnit() {
        return this.appUnit;
    }
}
