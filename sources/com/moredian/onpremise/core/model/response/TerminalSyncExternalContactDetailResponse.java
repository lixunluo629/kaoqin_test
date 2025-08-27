package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步外部联系人详情响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncExternalContactDetailResponse.class */
public class TerminalSyncExternalContactDetailResponse implements Serializable {
    private static final long serialVersionUID = -6470992236564395542L;

    @ApiModelProperty(name = "id", value = "外部联系人id")
    private Long id;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "idCard", value = "身份证")
    private String idCard;

    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

    @ApiModelProperty(name = "faceUrl", value = "底库url")
    private String faceUrl;

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalueValue;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncExternalContactDetailResponse)) {
            return false;
        }
        TerminalSyncExternalContactDetailResponse other = (TerminalSyncExternalContactDetailResponse) o;
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
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
        Object this$mobile = getMobile();
        Object other$mobile = other.getMobile();
        if (this$mobile == null) {
            if (other$mobile != null) {
                return false;
            }
        } else if (!this$mobile.equals(other$mobile)) {
            return false;
        }
        Object this$faceUrl = getFaceUrl();
        Object other$faceUrl = other.getFaceUrl();
        if (this$faceUrl == null) {
            if (other$faceUrl != null) {
                return false;
            }
        } else if (!this$faceUrl.equals(other$faceUrl)) {
            return false;
        }
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        return this$eigenvalueValue == null ? other$eigenvalueValue == null : this$eigenvalueValue.equals(other$eigenvalueValue);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncExternalContactDetailResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $idCard = getIdCard();
        int result4 = (result3 * 59) + ($idCard == null ? 43 : $idCard.hashCode());
        Object $mobile = getMobile();
        int result5 = (result4 * 59) + ($mobile == null ? 43 : $mobile.hashCode());
        Object $faceUrl = getFaceUrl();
        int result6 = (result5 * 59) + ($faceUrl == null ? 43 : $faceUrl.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        return (result6 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
    }

    public String toString() {
        return "TerminalSyncExternalContactDetailResponse(id=" + getId() + ", orgId=" + getOrgId() + ", name=" + getName() + ", idCard=" + getIdCard() + ", mobile=" + getMobile() + ", faceUrl=" + getFaceUrl() + ", eigenvalueValue=" + getEigenvalueValue() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getName() {
        return this.name;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getFaceUrl() {
        return this.faceUrl;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }
}
