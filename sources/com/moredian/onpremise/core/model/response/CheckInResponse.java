package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.aspectj.weaver.model.AsmRelationshipUtils;

@ApiModel(description = "签到结果")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInResponse.class */
public class CheckInResponse implements Serializable {

    @ApiModelProperty(name = "id", value = "签到任务id")
    private Long id;

    @ApiModelProperty(name = "name", value = "签到任务名称")
    private String name;

    @ApiModelProperty(name = "isSuccess", value = "签到是否成功")
    private Boolean isSuccess;

    @ApiModelProperty(name = AsmRelationshipUtils.DECLARE_ERROR, value = "签到失败代码")
    private String error;

    @ApiModelProperty(name = "errorInfo", value = "签到失败原因描述")
    private String errorInfo;

    @ApiModelProperty(name = "tipsText", value = "签到成功后，文字提示")
    private String tipsText;

    @ApiModelProperty(name = "tipsSpeech", value = "签到成功后，语音播报内容")
    private String tipsSpeech;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInResponse)) {
            return false;
        }
        CheckInResponse other = (CheckInResponse) o;
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
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
        return this$tipsSpeech == null ? other$tipsSpeech == null : this$tipsSpeech.equals(other$tipsSpeech);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $isSuccess = getIsSuccess();
        int result3 = (result2 * 59) + ($isSuccess == null ? 43 : $isSuccess.hashCode());
        Object $error = getError();
        int result4 = (result3 * 59) + ($error == null ? 43 : $error.hashCode());
        Object $errorInfo = getErrorInfo();
        int result5 = (result4 * 59) + ($errorInfo == null ? 43 : $errorInfo.hashCode());
        Object $tipsText = getTipsText();
        int result6 = (result5 * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        return (result6 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
    }

    public String toString() {
        return "CheckInResponse(id=" + getId() + ", name=" + getName() + ", isSuccess=" + getIsSuccess() + ", error=" + getError() + ", errorInfo=" + getErrorInfo() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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
}
