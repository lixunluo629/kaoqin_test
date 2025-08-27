package com.moredian.onpremise.core.model.response;

import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AppRemainDayResponse.class */
public class AppRemainDayResponse implements Serializable {
    private static final long serialVersionUID = -218578142693953978L;
    private Integer appType;
    private Integer remainDay;

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setRemainDay(Integer remainDay) {
        this.remainDay = remainDay;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AppRemainDayResponse)) {
            return false;
        }
        AppRemainDayResponse other = (AppRemainDayResponse) o;
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
        Object this$remainDay = getRemainDay();
        Object other$remainDay = other.getRemainDay();
        return this$remainDay == null ? other$remainDay == null : this$remainDay.equals(other$remainDay);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AppRemainDayResponse;
    }

    public int hashCode() {
        Object $appType = getAppType();
        int result = (1 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $remainDay = getRemainDay();
        return (result * 59) + ($remainDay == null ? 43 : $remainDay.hashCode());
    }

    public String toString() {
        return "AppRemainDayResponse(appType=" + getAppType() + ", remainDay=" + getRemainDay() + ")";
    }

    public Integer getAppType() {
        return this.appType;
    }

    public Integer getRemainDay() {
        return this.remainDay;
    }
}
