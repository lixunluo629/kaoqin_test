package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "访客记录明细")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveVisitRecordDetailRequest.class */
public class SaveVisitRecordDetailRequest implements Serializable {
    private static final long serialVersionUID = 8030872506413008314L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备Sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "memberId", value = "访客id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "访客姓名")
    private String memberName;

    @ApiModelProperty(name = "idCard", value = "来访者身份证")
    private String idCard;

    @ApiModelProperty(name = "snapFaceUrl", value = "来访者抓拍照片url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveVisitRecordDetailRequest)) {
            return false;
        }
        SaveVisitRecordDetailRequest other = (SaveVisitRecordDetailRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
            return false;
        }
        Object this$idCard = getIdCard();
        Object other$idCard = other.getIdCard();
        if (this$idCard == null) {
            if (other$idCard != null) {
                return false;
            }
        } else if (!this$idCard.equals(other$idCard)) {
            return false;
        }
        Object this$snapFaceUrl = getSnapFaceUrl();
        Object other$snapFaceUrl = other.getSnapFaceUrl();
        if (this$snapFaceUrl == null) {
            if (other$snapFaceUrl != null) {
                return false;
            }
        } else if (!this$snapFaceUrl.equals(other$snapFaceUrl)) {
            return false;
        }
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        return this$verifyTime == null ? other$verifyTime == null : this$verifyTime.equals(other$verifyTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveVisitRecordDetailRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result5 = (result4 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $idCard = getIdCard();
        int result6 = (result5 * 59) + ($idCard == null ? 43 : $idCard.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result7 = (result6 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $verifyTime = getVerifyTime();
        return (result7 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
    }

    public String toString() {
        return "SaveVisitRecordDetailRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", idCard=" + getIdCard() + ", snapFaceUrl=" + getSnapFaceUrl() + ", verifyTime=" + getVerifyTime() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public Long getVerifyTime() {
        return this.verifyTime;
    }
}
