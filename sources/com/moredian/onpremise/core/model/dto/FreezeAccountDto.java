package com.moredian.onpremise.core.model.dto;

import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/FreezeAccountDto.class */
public class FreezeAccountDto implements Serializable {
    private static final long serialVersionUID = -5221470298602035242L;
    private Long expireTime = 0L;
    private Long freezeTime = 0L;
    private Integer mistakeTimes = 0;
    private String accountName;

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setFreezeTime(Long freezeTime) {
        this.freezeTime = freezeTime;
    }

    public void setMistakeTimes(Integer mistakeTimes) {
        this.mistakeTimes = mistakeTimes;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FreezeAccountDto)) {
            return false;
        }
        FreezeAccountDto other = (FreezeAccountDto) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (this$expireTime == null) {
            if (other$expireTime != null) {
                return false;
            }
        } else if (!this$expireTime.equals(other$expireTime)) {
            return false;
        }
        Object this$freezeTime = getFreezeTime();
        Object other$freezeTime = other.getFreezeTime();
        if (this$freezeTime == null) {
            if (other$freezeTime != null) {
                return false;
            }
        } else if (!this$freezeTime.equals(other$freezeTime)) {
            return false;
        }
        Object this$mistakeTimes = getMistakeTimes();
        Object other$mistakeTimes = other.getMistakeTimes();
        if (this$mistakeTimes == null) {
            if (other$mistakeTimes != null) {
                return false;
            }
        } else if (!this$mistakeTimes.equals(other$mistakeTimes)) {
            return false;
        }
        Object this$accountName = getAccountName();
        Object other$accountName = other.getAccountName();
        return this$accountName == null ? other$accountName == null : this$accountName.equals(other$accountName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof FreezeAccountDto;
    }

    public int hashCode() {
        Object $expireTime = getExpireTime();
        int result = (1 * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
        Object $freezeTime = getFreezeTime();
        int result2 = (result * 59) + ($freezeTime == null ? 43 : $freezeTime.hashCode());
        Object $mistakeTimes = getMistakeTimes();
        int result3 = (result2 * 59) + ($mistakeTimes == null ? 43 : $mistakeTimes.hashCode());
        Object $accountName = getAccountName();
        return (result3 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
    }

    public String toString() {
        return "FreezeAccountDto(expireTime=" + getExpireTime() + ", freezeTime=" + getFreezeTime() + ", mistakeTimes=" + getMistakeTimes() + ", accountName=" + getAccountName() + ")";
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public Long getFreezeTime() {
        return this.freezeTime;
    }

    public Integer getMistakeTimes() {
        return this.mistakeTimes;
    }

    public String getAccountName() {
        return this.accountName;
    }
}
