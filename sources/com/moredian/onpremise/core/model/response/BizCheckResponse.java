package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询是否有业务响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/BizCheckResponse.class */
public class BizCheckResponse implements Serializable {

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务类型")
    private Integer appType;

    @ApiModelProperty(name = "tipText", value = "提示文字")
    private String tipsText = "";

    @ApiModelProperty(name = "tipSpeech", value = "语音播放内容")
    private String tipsSpeech = "";

    public void setAppType(Integer appType) {
        this.appType = appType;
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
        if (!(o instanceof BizCheckResponse)) {
            return false;
        }
        BizCheckResponse other = (BizCheckResponse) o;
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
        return other instanceof BizCheckResponse;
    }

    public int hashCode() {
        Object $appType = getAppType();
        int result = (1 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $tipsText = getTipsText();
        int result2 = (result * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        return (result2 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
    }

    public String toString() {
        return "BizCheckResponse(appType=" + getAppType() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ")";
    }

    public Integer getAppType() {
        return this.appType;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }
}
