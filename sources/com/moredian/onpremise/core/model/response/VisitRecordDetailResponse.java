package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "访客记录明细列表响应参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/VisitRecordDetailResponse.class */
public class VisitRecordDetailResponse implements Serializable {
    private static final long serialVersionUID = -4729641096582501775L;

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

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

    @ApiModelProperty(name = "verifyTimestamp", value = "识别时间")
    private Long verifyTimestamp;

    public void setId(Long id) {
        this.id = id;
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

    public void setVerifyTimestamp(Long verifyTimestamp) {
        this.verifyTimestamp = verifyTimestamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitRecordDetailResponse)) {
            return false;
        }
        VisitRecordDetailResponse other = (VisitRecordDetailResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
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
        Object this$verifyTimestamp = getVerifyTimestamp();
        Object other$verifyTimestamp = other.getVerifyTimestamp();
        return this$verifyTimestamp == null ? other$verifyTimestamp == null : this$verifyTimestamp.equals(other$verifyTimestamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VisitRecordDetailResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $idCard = getIdCard();
        int result5 = (result4 * 59) + ($idCard == null ? 43 : $idCard.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result6 = (result5 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $verifyTimestamp = getVerifyTimestamp();
        return (result6 * 59) + ($verifyTimestamp == null ? 43 : $verifyTimestamp.hashCode());
    }

    public String toString() {
        return "VisitRecordDetailResponse(id=" + getId() + ", deviceName=" + getDeviceName() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", idCard=" + getIdCard() + ", snapFaceUrl=" + getSnapFaceUrl() + ", verifyTimestamp=" + getVerifyTimestamp() + ")";
    }

    public Long getId() {
        return this.id;
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

    public Long getVerifyTimestamp() {
        return this.verifyTimestamp;
    }
}
