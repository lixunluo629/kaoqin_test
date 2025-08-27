package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备端录入人脸")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveMemberFaceRequest.class */
public class SaveMemberFaceRequest implements Serializable {

    @ApiModelProperty(value = "底库照url", name = "verifyFaceUrl")
    private String verifyFaceUrl;

    @ApiModelProperty(value = "底库照md5", name = "verifyFaceMd5", hidden = true)
    private String verifyFaceMd5;

    @ApiModelProperty(value = "特征值", name = "eigenvalueValue")
    private String eigenvalueValue;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(value = "机构id", name = "orgId")
    private Long orgId;

    @ApiModelProperty(value = "needSendMsg", name = "是否需要发送netty同步消息")
    private boolean needSendMsg;

    @ApiModelProperty(value = "channel", name = "渠道；1-管理后台录脸，2-MH2认证采脸。缺失值=1")
    private Integer channel;

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setVerifyFaceMd5(String verifyFaceMd5) {
        this.verifyFaceMd5 = verifyFaceMd5;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setNeedSendMsg(boolean needSendMsg) {
        this.needSendMsg = needSendMsg;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMemberFaceRequest)) {
            return false;
        }
        SaveMemberFaceRequest other = (SaveMemberFaceRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
            return false;
        }
        Object this$verifyFaceMd5 = getVerifyFaceMd5();
        Object other$verifyFaceMd5 = other.getVerifyFaceMd5();
        if (this$verifyFaceMd5 == null) {
            if (other$verifyFaceMd5 != null) {
                return false;
            }
        } else if (!this$verifyFaceMd5.equals(other$verifyFaceMd5)) {
            return false;
        }
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        if (this$eigenvalueValue == null) {
            if (other$eigenvalueValue != null) {
                return false;
            }
        } else if (!this$eigenvalueValue.equals(other$eigenvalueValue)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
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
        if (isNeedSendMsg() != other.isNeedSendMsg()) {
            return false;
        }
        Object this$channel = getChannel();
        Object other$channel = other.getChannel();
        return this$channel == null ? other$channel == null : this$channel.equals(other$channel);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveMemberFaceRequest;
    }

    public int hashCode() {
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result = (1 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $verifyFaceMd5 = getVerifyFaceMd5();
        int result2 = (result * 59) + ($verifyFaceMd5 == null ? 43 : $verifyFaceMd5.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        int result3 = (result2 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $orgId = getOrgId();
        int result5 = (((result4 * 59) + ($orgId == null ? 43 : $orgId.hashCode())) * 59) + (isNeedSendMsg() ? 79 : 97);
        Object $channel = getChannel();
        return (result5 * 59) + ($channel == null ? 43 : $channel.hashCode());
    }

    public String toString() {
        return "SaveMemberFaceRequest(verifyFaceUrl=" + getVerifyFaceUrl() + ", verifyFaceMd5=" + getVerifyFaceMd5() + ", eigenvalueValue=" + getEigenvalueValue() + ", memberJobNum=" + getMemberJobNum() + ", orgId=" + getOrgId() + ", needSendMsg=" + isNeedSendMsg() + ", channel=" + getChannel() + ")";
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getVerifyFaceMd5() {
        return this.verifyFaceMd5;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public boolean isNeedSendMsg() {
        return this.needSendMsg;
    }

    public Integer getChannel() {
        return this.channel;
    }
}
