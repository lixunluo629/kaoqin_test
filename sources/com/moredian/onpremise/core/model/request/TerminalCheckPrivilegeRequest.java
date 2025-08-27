package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "在线鉴权请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalCheckPrivilegeRequest.class */
public class TerminalCheckPrivilegeRequest implements Serializable {

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "jobNum", value = "工号")
    private String jobNum;

    @ApiModelProperty(name = "memberId", value = "用户id")
    private Long memberId;

    @ApiModelProperty(name = "deviceSn", value = "识别设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "timeStamp", value = "识别时间戳")
    private Long timeStamp;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalCheckPrivilegeRequest)) {
            return false;
        }
        TerminalCheckPrivilegeRequest other = (TerminalCheckPrivilegeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$timeStamp = getTimeStamp();
        Object other$timeStamp = other.getTimeStamp();
        return this$timeStamp == null ? other$timeStamp == null : this$timeStamp.equals(other$timeStamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalCheckPrivilegeRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $jobNum = getJobNum();
        int result2 = (result * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result4 = (result3 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $timeStamp = getTimeStamp();
        return (result4 * 59) + ($timeStamp == null ? 43 : $timeStamp.hashCode());
    }

    public String toString() {
        return "TerminalCheckPrivilegeRequest(orgId=" + getOrgId() + ", jobNum=" + getJobNum() + ", memberId=" + getMemberId() + ", deviceSn=" + getDeviceSn() + ", timeStamp=" + getTimeStamp() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getTimeStamp() {
        return this.timeStamp;
    }
}
