package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "在线鉴权响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalCheckPrivilegeResponse.class */
public class TerminalCheckPrivilegeResponse implements Serializable {

    @ApiModelProperty(name = "tipText", value = "界面提示语")
    private String tipText;

    @ApiModelProperty(name = "tipSpeech", value = "语音播报内容")
    private String tipSpeech;

    @ApiModelProperty(name = "openDoor", value = "是否开门，0：不开，1：开")
    private Integer openDoor = 0;

    @ApiModelProperty(name = "extra", value = "")
    private String extra = "{\"mzt\":\"绿码\"}";

    public void setTipText(String tipText) {
        this.tipText = tipText;
    }

    public void setTipSpeech(String tipSpeech) {
        this.tipSpeech = tipSpeech;
    }

    public void setOpenDoor(Integer openDoor) {
        this.openDoor = openDoor;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalCheckPrivilegeResponse)) {
            return false;
        }
        TerminalCheckPrivilegeResponse other = (TerminalCheckPrivilegeResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$tipText = getTipText();
        Object other$tipText = other.getTipText();
        if (this$tipText == null) {
            if (other$tipText != null) {
                return false;
            }
        } else if (!this$tipText.equals(other$tipText)) {
            return false;
        }
        Object this$tipSpeech = getTipSpeech();
        Object other$tipSpeech = other.getTipSpeech();
        if (this$tipSpeech == null) {
            if (other$tipSpeech != null) {
                return false;
            }
        } else if (!this$tipSpeech.equals(other$tipSpeech)) {
            return false;
        }
        Object this$openDoor = getOpenDoor();
        Object other$openDoor = other.getOpenDoor();
        if (this$openDoor == null) {
            if (other$openDoor != null) {
                return false;
            }
        } else if (!this$openDoor.equals(other$openDoor)) {
            return false;
        }
        Object this$extra = getExtra();
        Object other$extra = other.getExtra();
        return this$extra == null ? other$extra == null : this$extra.equals(other$extra);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalCheckPrivilegeResponse;
    }

    public int hashCode() {
        Object $tipText = getTipText();
        int result = (1 * 59) + ($tipText == null ? 43 : $tipText.hashCode());
        Object $tipSpeech = getTipSpeech();
        int result2 = (result * 59) + ($tipSpeech == null ? 43 : $tipSpeech.hashCode());
        Object $openDoor = getOpenDoor();
        int result3 = (result2 * 59) + ($openDoor == null ? 43 : $openDoor.hashCode());
        Object $extra = getExtra();
        return (result3 * 59) + ($extra == null ? 43 : $extra.hashCode());
    }

    public String toString() {
        return "TerminalCheckPrivilegeResponse(tipText=" + getTipText() + ", tipSpeech=" + getTipSpeech() + ", openDoor=" + getOpenDoor() + ", extra=" + getExtra() + ")";
    }

    public String getTipText() {
        return this.tipText;
    }

    public String getTipSpeech() {
        return this.tipSpeech;
    }

    public Integer getOpenDoor() {
        return this.openDoor;
    }

    public String getExtra() {
        return this.extra;
    }
}
