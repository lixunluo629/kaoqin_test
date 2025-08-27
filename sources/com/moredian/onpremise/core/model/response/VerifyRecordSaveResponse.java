package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.aspectj.weaver.model.AsmRelationshipUtils;

@ApiModel(description = "识别记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/VerifyRecordSaveResponse.class */
public class VerifyRecordSaveResponse implements Serializable {

    @ApiModelProperty(name = AsmRelationshipUtils.DECLARE_ERROR, value = "失败代码")
    private String error;

    @ApiModelProperty(name = "errorInfo", value = "失败原因描述")
    private String errorInfo;

    @ApiModelProperty(name = "recordId", value = "识别记录ID", hidden = true)
    private Long recordId;

    @ApiModelProperty(name = "isSuccess", value = "是否成功")
    private Boolean isSuccess = true;

    @ApiModelProperty(name = "tipText", value = "提示文字")
    private String tipsText = "";

    @ApiModelProperty(name = "tipSpeech", value = "语音播放内容")
    private String tipsSpeech = "";

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordSaveResponse)) {
            return false;
        }
        VerifyRecordSaveResponse other = (VerifyRecordSaveResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$isSuccess = getIsSuccess();
        Object other$isSuccess = other.getIsSuccess();
        if (this$isSuccess == null) {
            if (other$isSuccess != null) {
                return false;
            }
        } else if (!this$isSuccess.equals(other$isSuccess)) {
            return false;
        }
        Object this$error = getError();
        Object other$error = other.getError();
        if (this$error == null) {
            if (other$error != null) {
                return false;
            }
        } else if (!this$error.equals(other$error)) {
            return false;
        }
        Object this$errorInfo = getErrorInfo();
        Object other$errorInfo = other.getErrorInfo();
        if (this$errorInfo == null) {
            if (other$errorInfo != null) {
                return false;
            }
        } else if (!this$errorInfo.equals(other$errorInfo)) {
            return false;
        }
        Object this$tipsText = getTipsText();
        Object other$tipsText = other.getTipsText();
        if (this$tipsText == null) {
            if (other$tipsText != null) {
                return false;
            }
        } else if (!this$tipsText.equals(other$tipsText)) {
            return false;
        }
        Object this$tipsSpeech = getTipsSpeech();
        Object other$tipsSpeech = other.getTipsSpeech();
        if (this$tipsSpeech == null) {
            if (other$tipsSpeech != null) {
                return false;
            }
        } else if (!this$tipsSpeech.equals(other$tipsSpeech)) {
            return false;
        }
        Object this$recordId = getRecordId();
        Object other$recordId = other.getRecordId();
        return this$recordId == null ? other$recordId == null : this$recordId.equals(other$recordId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordSaveResponse;
    }

    public int hashCode() {
        Object $isSuccess = getIsSuccess();
        int result = (1 * 59) + ($isSuccess == null ? 43 : $isSuccess.hashCode());
        Object $error = getError();
        int result2 = (result * 59) + ($error == null ? 43 : $error.hashCode());
        Object $errorInfo = getErrorInfo();
        int result3 = (result2 * 59) + ($errorInfo == null ? 43 : $errorInfo.hashCode());
        Object $tipsText = getTipsText();
        int result4 = (result3 * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        int result5 = (result4 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
        Object $recordId = getRecordId();
        return (result5 * 59) + ($recordId == null ? 43 : $recordId.hashCode());
    }

    public String toString() {
        return "VerifyRecordSaveResponse(isSuccess=" + getIsSuccess() + ", error=" + getError() + ", errorInfo=" + getErrorInfo() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ", recordId=" + getRecordId() + ")";
    }

    public Boolean getIsSuccess() {
        return this.isSuccess;
    }

    public String getError() {
        return this.error;
    }

    public String getErrorInfo() {
        return this.errorInfo;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }

    public Long getRecordId() {
        return this.recordId;
    }
}
