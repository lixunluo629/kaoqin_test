package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "保存识别记录请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyRecordSaveRequest.class */
public class VerifyRecordSaveRequest implements Serializable {
    private static final long serialVersionUID = 4913770034932690955L;

    @ApiModelProperty(name = "orgId", value = "组织id")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "snapFaceBase64", value = "抓拍照base64")
    private String snapFaceBase64;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "recordType", value = "识别类型：1-设备端上识别，2-服务端上识别", hidden = true)
    private Integer recordType;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务类型，1：魔点门禁；2：魔点考勤；3：魔点签到；4：魔点访客；5：魔点团餐；6：魔点测温")
    private Integer appType;

    @ApiModelProperty(name = "appTypes", value = "业务类型，数组格式")
    private List<Integer> appTypes;

    @ApiModelProperty(name = "bizId", value = "业务id")
    private Long bizId;

    @ApiModelProperty(name = "verifyScore", value = "识别分数")
    private Integer verifyScore;

    @ApiModelProperty(name = "verifyResult", value = "识别结果：1-成功，2-失败")
    private Integer verifyResult;

    @ApiModelProperty(name = "secondMemberId", value = "识别第二名成员id")
    private Long secondMemberId;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名识别分数")
    private Integer secondVerifyScore;

    @ApiModelProperty(name = "verifyType", value = "入场方式：1-刷脸，2-刷卡")
    private Integer verifyType;

    @ApiModelProperty(name = "mirrorVerifyScore", value = "镜像翻转后，识别相似度分数，百分制")
    private Integer mirrorVerifyScore;

    @ApiModelProperty(name = "temperatureValue", value = "测量温度值")
    private BigDecimal temperatureValue;

    @ApiModelProperty(name = "healthCode", value = "健康码")
    private String healthCode;

    @ApiModelProperty(name = "operator", value = "操作人员")
    private String operator;

    @ApiModelProperty(name = "doorFlag", value = "是否开门：0-否，1-是")
    private Integer doorFlag;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setSnapFaceBase64(String snapFaceBase64) {
        this.snapFaceBase64 = snapFaceBase64;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setAppTypes(List<Integer> appTypes) {
        this.appTypes = appTypes;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public void setSecondMemberId(Long secondMemberId) {
        this.secondMemberId = secondMemberId;
    }

    public void setSecondVerifyScore(Integer secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public void setMirrorVerifyScore(Integer mirrorVerifyScore) {
        this.mirrorVerifyScore = mirrorVerifyScore;
    }

    public void setTemperatureValue(BigDecimal temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordSaveRequest)) {
            return false;
        }
        VerifyRecordSaveRequest other = (VerifyRecordSaveRequest) o;
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
        Object this$snapFaceBase64 = getSnapFaceBase64();
        Object other$snapFaceBase64 = other.getSnapFaceBase64();
        if (this$snapFaceBase64 == null) {
            if (other$snapFaceBase64 != null) {
                return false;
            }
        } else if (!this$snapFaceBase64.equals(other$snapFaceBase64)) {
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
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
            return false;
        }
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        if (this$verifyTime == null) {
            if (other$verifyTime != null) {
                return false;
            }
        } else if (!this$verifyTime.equals(other$verifyTime)) {
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
        Object this$appTypes = getAppTypes();
        Object other$appTypes = other.getAppTypes();
        if (this$appTypes == null) {
            if (other$appTypes != null) {
                return false;
            }
        } else if (!this$appTypes.equals(other$appTypes)) {
            return false;
        }
        Object this$bizId = getBizId();
        Object other$bizId = other.getBizId();
        if (this$bizId == null) {
            if (other$bizId != null) {
                return false;
            }
        } else if (!this$bizId.equals(other$bizId)) {
            return false;
        }
        Object this$verifyScore = getVerifyScore();
        Object other$verifyScore = other.getVerifyScore();
        if (this$verifyScore == null) {
            if (other$verifyScore != null) {
                return false;
            }
        } else if (!this$verifyScore.equals(other$verifyScore)) {
            return false;
        }
        Object this$verifyResult = getVerifyResult();
        Object other$verifyResult = other.getVerifyResult();
        if (this$verifyResult == null) {
            if (other$verifyResult != null) {
                return false;
            }
        } else if (!this$verifyResult.equals(other$verifyResult)) {
            return false;
        }
        Object this$secondMemberId = getSecondMemberId();
        Object other$secondMemberId = other.getSecondMemberId();
        if (this$secondMemberId == null) {
            if (other$secondMemberId != null) {
                return false;
            }
        } else if (!this$secondMemberId.equals(other$secondMemberId)) {
            return false;
        }
        Object this$secondVerifyScore = getSecondVerifyScore();
        Object other$secondVerifyScore = other.getSecondVerifyScore();
        if (this$secondVerifyScore == null) {
            if (other$secondVerifyScore != null) {
                return false;
            }
        } else if (!this$secondVerifyScore.equals(other$secondVerifyScore)) {
            return false;
        }
        Object this$verifyType = getVerifyType();
        Object other$verifyType = other.getVerifyType();
        if (this$verifyType == null) {
            if (other$verifyType != null) {
                return false;
            }
        } else if (!this$verifyType.equals(other$verifyType)) {
            return false;
        }
        Object this$mirrorVerifyScore = getMirrorVerifyScore();
        Object other$mirrorVerifyScore = other.getMirrorVerifyScore();
        if (this$mirrorVerifyScore == null) {
            if (other$mirrorVerifyScore != null) {
                return false;
            }
        } else if (!this$mirrorVerifyScore.equals(other$mirrorVerifyScore)) {
            return false;
        }
        Object this$temperatureValue = getTemperatureValue();
        Object other$temperatureValue = other.getTemperatureValue();
        if (this$temperatureValue == null) {
            if (other$temperatureValue != null) {
                return false;
            }
        } else if (!this$temperatureValue.equals(other$temperatureValue)) {
            return false;
        }
        Object this$healthCode = getHealthCode();
        Object other$healthCode = other.getHealthCode();
        if (this$healthCode == null) {
            if (other$healthCode != null) {
                return false;
            }
        } else if (!this$healthCode.equals(other$healthCode)) {
            return false;
        }
        Object this$operator = getOperator();
        Object other$operator = other.getOperator();
        if (this$operator == null) {
            if (other$operator != null) {
                return false;
            }
        } else if (!this$operator.equals(other$operator)) {
            return false;
        }
        Object this$doorFlag = getDoorFlag();
        Object other$doorFlag = other.getDoorFlag();
        return this$doorFlag == null ? other$doorFlag == null : this$doorFlag.equals(other$doorFlag);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordSaveRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $snapFaceBase64 = getSnapFaceBase64();
        int result3 = (result2 * 59) + ($snapFaceBase64 == null ? 43 : $snapFaceBase64.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $recordType = getRecordType();
        int result5 = (result4 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $verifyTime = getVerifyTime();
        int result6 = (result5 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
        Object $appType = getAppType();
        int result7 = (result6 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $appTypes = getAppTypes();
        int result8 = (result7 * 59) + ($appTypes == null ? 43 : $appTypes.hashCode());
        Object $bizId = getBizId();
        int result9 = (result8 * 59) + ($bizId == null ? 43 : $bizId.hashCode());
        Object $verifyScore = getVerifyScore();
        int result10 = (result9 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $verifyResult = getVerifyResult();
        int result11 = (result10 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
        Object $secondMemberId = getSecondMemberId();
        int result12 = (result11 * 59) + ($secondMemberId == null ? 43 : $secondMemberId.hashCode());
        Object $secondVerifyScore = getSecondVerifyScore();
        int result13 = (result12 * 59) + ($secondVerifyScore == null ? 43 : $secondVerifyScore.hashCode());
        Object $verifyType = getVerifyType();
        int result14 = (result13 * 59) + ($verifyType == null ? 43 : $verifyType.hashCode());
        Object $mirrorVerifyScore = getMirrorVerifyScore();
        int result15 = (result14 * 59) + ($mirrorVerifyScore == null ? 43 : $mirrorVerifyScore.hashCode());
        Object $temperatureValue = getTemperatureValue();
        int result16 = (result15 * 59) + ($temperatureValue == null ? 43 : $temperatureValue.hashCode());
        Object $healthCode = getHealthCode();
        int result17 = (result16 * 59) + ($healthCode == null ? 43 : $healthCode.hashCode());
        Object $operator = getOperator();
        int result18 = (result17 * 59) + ($operator == null ? 43 : $operator.hashCode());
        Object $doorFlag = getDoorFlag();
        return (result18 * 59) + ($doorFlag == null ? 43 : $doorFlag.hashCode());
    }

    public String toString() {
        return "VerifyRecordSaveRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", snapFaceBase64=" + getSnapFaceBase64() + ", memberId=" + getMemberId() + ", recordType=" + getRecordType() + ", verifyTime=" + getVerifyTime() + ", appType=" + getAppType() + ", appTypes=" + getAppTypes() + ", bizId=" + getBizId() + ", verifyScore=" + getVerifyScore() + ", verifyResult=" + getVerifyResult() + ", secondMemberId=" + getSecondMemberId() + ", secondVerifyScore=" + getSecondVerifyScore() + ", verifyType=" + getVerifyType() + ", mirrorVerifyScore=" + getMirrorVerifyScore() + ", temperatureValue=" + getTemperatureValue() + ", healthCode=" + getHealthCode() + ", operator=" + getOperator() + ", doorFlag=" + getDoorFlag() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getSnapFaceBase64() {
        return this.snapFaceBase64;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public Long getVerifyTime() {
        return this.verifyTime;
    }

    public Integer getAppType() {
        return this.appType;
    }

    public List<Integer> getAppTypes() {
        return this.appTypes;
    }

    public Long getBizId() {
        return this.bizId;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }

    public Long getSecondMemberId() {
        return this.secondMemberId;
    }

    public Integer getSecondVerifyScore() {
        return this.secondVerifyScore;
    }

    public Integer getVerifyType() {
        return this.verifyType;
    }

    public Integer getMirrorVerifyScore() {
        return this.mirrorVerifyScore;
    }

    public BigDecimal getTemperatureValue() {
        return this.temperatureValue;
    }

    public String getHealthCode() {
        return this.healthCode;
    }

    public String getOperator() {
        return this.operator;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }
}
