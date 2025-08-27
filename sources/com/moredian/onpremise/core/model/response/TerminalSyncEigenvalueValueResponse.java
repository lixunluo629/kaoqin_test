package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步特征值响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncEigenvalueValueResponse.class */
public class TerminalSyncEigenvalueValueResponse implements Serializable {
    private static final long serialVersionUID = 8369403395983590342L;

    @ApiModelProperty(name = "memberId", value = "机构id")
    private Long memberId;

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalueValue;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncEigenvalueValueResponse)) {
            return false;
        }
        TerminalSyncEigenvalueValueResponse other = (TerminalSyncEigenvalueValueResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        return this$eigenvalueValue == null ? other$eigenvalueValue == null : this$eigenvalueValue.equals(other$eigenvalueValue);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncEigenvalueValueResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        return (result * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
    }

    public String toString() {
        return "TerminalSyncEigenvalueValueResponse(memberId=" + getMemberId() + ", eigenvalueValue=" + getEigenvalueValue() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }
}
