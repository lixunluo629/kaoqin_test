package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存就餐记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveMealRecordRequest.class */
public class SaveMealRecordRequest implements Serializable {

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "snapFaceBase64", value = "抓拍照base64")
    private String snapFaceBase64;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "verifyScore", value = "比对分数")
    private Integer verifyScore;

    @ApiModelProperty(name = "verifyResult", value = "识别结果：1-成功，2-失败")
    private Integer verifyResult;

    @ApiModelProperty(name = "recordType", value = "就餐类型：1-早餐，2-午餐，3-晚餐，4-宵夜")
    private Integer recordType;

    @ApiModelProperty(name = "secondMemberId", value = "识别第二名成员id")
    private Long secondMemberId;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名识别分数")
    private Integer secondVerifyScore;

    @ApiModelProperty(name = "mirrorVerifyScore", value = "镜像识别分数")
    private Integer mirrorVerifyScore;

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

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setSecondMemberId(Long secondMemberId) {
        this.secondMemberId = secondMemberId;
    }

    public void setSecondVerifyScore(Integer secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public void setMirrorVerifyScore(Integer mirrorVerifyScore) {
        this.mirrorVerifyScore = mirrorVerifyScore;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMealRecordRequest)) {
            return false;
        }
        SaveMealRecordRequest other = (SaveMealRecordRequest) o;
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
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        if (this$verifyTime == null) {
            if (other$verifyTime != null) {
                return false;
            }
        } else if (!this$verifyTime.equals(other$verifyTime)) {
            return false;
        }
        Object this$mealCanteenId = getMealCanteenId();
        Object other$mealCanteenId = other.getMealCanteenId();
        if (this$mealCanteenId == null) {
            if (other$mealCanteenId != null) {
                return false;
            }
        } else if (!this$mealCanteenId.equals(other$mealCanteenId)) {
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
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
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
        Object this$mirrorVerifyScore = getMirrorVerifyScore();
        Object other$mirrorVerifyScore = other.getMirrorVerifyScore();
        return this$mirrorVerifyScore == null ? other$mirrorVerifyScore == null : this$mirrorVerifyScore.equals(other$mirrorVerifyScore);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveMealRecordRequest;
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
        Object $verifyTime = getVerifyTime();
        int result5 = (result4 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
        Object $mealCanteenId = getMealCanteenId();
        int result6 = (result5 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $verifyScore = getVerifyScore();
        int result7 = (result6 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $verifyResult = getVerifyResult();
        int result8 = (result7 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
        Object $recordType = getRecordType();
        int result9 = (result8 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $secondMemberId = getSecondMemberId();
        int result10 = (result9 * 59) + ($secondMemberId == null ? 43 : $secondMemberId.hashCode());
        Object $secondVerifyScore = getSecondVerifyScore();
        int result11 = (result10 * 59) + ($secondVerifyScore == null ? 43 : $secondVerifyScore.hashCode());
        Object $mirrorVerifyScore = getMirrorVerifyScore();
        return (result11 * 59) + ($mirrorVerifyScore == null ? 43 : $mirrorVerifyScore.hashCode());
    }

    public String toString() {
        return "SaveMealRecordRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", snapFaceBase64=" + getSnapFaceBase64() + ", memberId=" + getMemberId() + ", verifyTime=" + getVerifyTime() + ", mealCanteenId=" + getMealCanteenId() + ", verifyScore=" + getVerifyScore() + ", verifyResult=" + getVerifyResult() + ", recordType=" + getRecordType() + ", secondMemberId=" + getSecondMemberId() + ", secondVerifyScore=" + getSecondVerifyScore() + ", mirrorVerifyScore=" + getMirrorVerifyScore() + ")";
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

    public Long getVerifyTime() {
        return this.verifyTime;
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public Long getSecondMemberId() {
        return this.secondMemberId;
    }

    public Integer getSecondVerifyScore() {
        return this.secondVerifyScore;
    }

    public Integer getMirrorVerifyScore() {
        return this.mirrorVerifyScore;
    }
}
