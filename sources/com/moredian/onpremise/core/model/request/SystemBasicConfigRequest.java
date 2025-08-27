package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统基础配置请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SystemBasicConfigRequest.class */
public class SystemBasicConfigRequest extends BaseRequest {
    private static final long serialVersionUID = 5431171764034837866L;

    @ApiModelProperty(name = "recordPeriod", value = "记录存储天数")
    private Integer recordPeriod;

    @ApiModelProperty(name = "snapMode", value = "抓拍照模式，1-小图；2-大图")
    private Integer snapMode;

    @ApiModelProperty(name = "strangerFrame", value = "判断陌生人帧数")
    private Integer strangerFrame;

    @ApiModelProperty(name = "strangerTextTips", value = "陌生人文字提示")
    private String strangerTextTips;

    @ApiModelProperty(name = "strangerSpeechTips", value = "陌生人语音提示")
    private String strangerSpeechTips;

    @ApiModelProperty(name = "timeZoneStr", value = "时区字符串，例：GMT+0800")
    private String timeZoneStr;

    @ApiModelProperty(name = "repeatFace", value = "是否查重，0-不查；1-查，默认1")
    private Integer repeatFace;

    @ApiModelProperty(name = "snapPeriod", value = "抓拍照存储天数")
    private Integer snapPeriod;

    public void setRecordPeriod(Integer recordPeriod) {
        this.recordPeriod = recordPeriod;
    }

    public void setSnapMode(Integer snapMode) {
        this.snapMode = snapMode;
    }

    public void setStrangerFrame(Integer strangerFrame) {
        this.strangerFrame = strangerFrame;
    }

    public void setStrangerTextTips(String strangerTextTips) {
        this.strangerTextTips = strangerTextTips;
    }

    public void setStrangerSpeechTips(String strangerSpeechTips) {
        this.strangerSpeechTips = strangerSpeechTips;
    }

    public void setTimeZoneStr(String timeZoneStr) {
        this.timeZoneStr = timeZoneStr;
    }

    public void setRepeatFace(Integer repeatFace) {
        this.repeatFace = repeatFace;
    }

    public void setSnapPeriod(Integer snapPeriod) {
        this.snapPeriod = snapPeriod;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemBasicConfigRequest)) {
            return false;
        }
        SystemBasicConfigRequest other = (SystemBasicConfigRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$recordPeriod = getRecordPeriod();
        Object other$recordPeriod = other.getRecordPeriod();
        if (this$recordPeriod == null) {
            if (other$recordPeriod != null) {
                return false;
            }
        } else if (!this$recordPeriod.equals(other$recordPeriod)) {
            return false;
        }
        Object this$snapMode = getSnapMode();
        Object other$snapMode = other.getSnapMode();
        if (this$snapMode == null) {
            if (other$snapMode != null) {
                return false;
            }
        } else if (!this$snapMode.equals(other$snapMode)) {
            return false;
        }
        Object this$strangerFrame = getStrangerFrame();
        Object other$strangerFrame = other.getStrangerFrame();
        if (this$strangerFrame == null) {
            if (other$strangerFrame != null) {
                return false;
            }
        } else if (!this$strangerFrame.equals(other$strangerFrame)) {
            return false;
        }
        Object this$strangerTextTips = getStrangerTextTips();
        Object other$strangerTextTips = other.getStrangerTextTips();
        if (this$strangerTextTips == null) {
            if (other$strangerTextTips != null) {
                return false;
            }
        } else if (!this$strangerTextTips.equals(other$strangerTextTips)) {
            return false;
        }
        Object this$strangerSpeechTips = getStrangerSpeechTips();
        Object other$strangerSpeechTips = other.getStrangerSpeechTips();
        if (this$strangerSpeechTips == null) {
            if (other$strangerSpeechTips != null) {
                return false;
            }
        } else if (!this$strangerSpeechTips.equals(other$strangerSpeechTips)) {
            return false;
        }
        Object this$timeZoneStr = getTimeZoneStr();
        Object other$timeZoneStr = other.getTimeZoneStr();
        if (this$timeZoneStr == null) {
            if (other$timeZoneStr != null) {
                return false;
            }
        } else if (!this$timeZoneStr.equals(other$timeZoneStr)) {
            return false;
        }
        Object this$repeatFace = getRepeatFace();
        Object other$repeatFace = other.getRepeatFace();
        if (this$repeatFace == null) {
            if (other$repeatFace != null) {
                return false;
            }
        } else if (!this$repeatFace.equals(other$repeatFace)) {
            return false;
        }
        Object this$snapPeriod = getSnapPeriod();
        Object other$snapPeriod = other.getSnapPeriod();
        return this$snapPeriod == null ? other$snapPeriod == null : this$snapPeriod.equals(other$snapPeriod);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SystemBasicConfigRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $recordPeriod = getRecordPeriod();
        int result = (1 * 59) + ($recordPeriod == null ? 43 : $recordPeriod.hashCode());
        Object $snapMode = getSnapMode();
        int result2 = (result * 59) + ($snapMode == null ? 43 : $snapMode.hashCode());
        Object $strangerFrame = getStrangerFrame();
        int result3 = (result2 * 59) + ($strangerFrame == null ? 43 : $strangerFrame.hashCode());
        Object $strangerTextTips = getStrangerTextTips();
        int result4 = (result3 * 59) + ($strangerTextTips == null ? 43 : $strangerTextTips.hashCode());
        Object $strangerSpeechTips = getStrangerSpeechTips();
        int result5 = (result4 * 59) + ($strangerSpeechTips == null ? 43 : $strangerSpeechTips.hashCode());
        Object $timeZoneStr = getTimeZoneStr();
        int result6 = (result5 * 59) + ($timeZoneStr == null ? 43 : $timeZoneStr.hashCode());
        Object $repeatFace = getRepeatFace();
        int result7 = (result6 * 59) + ($repeatFace == null ? 43 : $repeatFace.hashCode());
        Object $snapPeriod = getSnapPeriod();
        return (result7 * 59) + ($snapPeriod == null ? 43 : $snapPeriod.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SystemBasicConfigRequest(recordPeriod=" + getRecordPeriod() + ", snapMode=" + getSnapMode() + ", strangerFrame=" + getStrangerFrame() + ", strangerTextTips=" + getStrangerTextTips() + ", strangerSpeechTips=" + getStrangerSpeechTips() + ", timeZoneStr=" + getTimeZoneStr() + ", repeatFace=" + getRepeatFace() + ", snapPeriod=" + getSnapPeriod() + ")";
    }

    public Integer getRecordPeriod() {
        return this.recordPeriod;
    }

    public Integer getSnapMode() {
        return this.snapMode;
    }

    public Integer getStrangerFrame() {
        return this.strangerFrame;
    }

    public String getStrangerTextTips() {
        return this.strangerTextTips;
    }

    public String getStrangerSpeechTips() {
        return this.strangerSpeechTips;
    }

    public String getTimeZoneStr() {
        return this.timeZoneStr;
    }

    public Integer getRepeatFace() {
        return this.repeatFace;
    }

    public Integer getSnapPeriod() {
        return this.snapPeriod;
    }
}
