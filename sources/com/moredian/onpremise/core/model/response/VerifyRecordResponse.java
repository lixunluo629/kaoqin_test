package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "识别记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/VerifyRecordResponse.class */
public class VerifyRecordResponse implements Serializable {

    @ApiModelProperty(name = "verifyRecordId", value = "识别记录id")
    private Long verifyRecordId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "verifyFaceUrl", value = "底库照url")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "jobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Date verifyTime;

    @ApiModelProperty(name = "verifyTimeMillis", value = "识别时间戳")
    private Long verifyTimeMillis;

    @ApiModelProperty(name = "verifyTimestamp", value = "识别时间戳")
    private Long verifyTimestamp;

    @ApiModelProperty(name = "verifyScore", value = "识别分数")
    private Integer verifyScore;

    @ApiModelProperty(name = "verifyResult", value = "识别结果：1-成功，2-失败")
    private Integer verifyResult;

    @ApiModelProperty(name = "secondMemberId", value = "第二名成员id")
    private Long secondMemberId;

    @ApiModelProperty(name = "secondMemberJobNum", value = "第二名成员工号")
    private String secondMemberJobNum;

    @ApiModelProperty(name = "secondMemberName", value = "第二名成员名称")
    private String secondMemberName;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名识别分数")
    private Integer secondVerifyScore;

    @ApiModelProperty(name = "secondVerifyFaceUrl", value = "第二名底库照url")
    private String secondVerifyFaceUrl;

    @ApiModelProperty(name = "verifyType", value = "入场方式：1-刷脸；2-刷卡")
    private Integer verifyType;

    @ApiModelProperty(name = "mirrorVerifyScore", value = "镜像照片识别分数")
    private Integer mirrorVerifyScore;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务类型，1-门禁；1-签到；3-考勤；4-访客；5-团餐；6-温控")
    private Integer appType;

    public void setVerifyRecordId(Long verifyRecordId) {
        this.verifyRecordId = verifyRecordId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setVerifyTimeMillis(Long verifyTimeMillis) {
        this.verifyTimeMillis = verifyTimeMillis;
    }

    public void setVerifyTimestamp(Long verifyTimestamp) {
        this.verifyTimestamp = verifyTimestamp;
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

    public void setSecondMemberJobNum(String secondMemberJobNum) {
        this.secondMemberJobNum = secondMemberJobNum;
    }

    public void setSecondMemberName(String secondMemberName) {
        this.secondMemberName = secondMemberName;
    }

    public void setSecondVerifyScore(Integer secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public void setSecondVerifyFaceUrl(String secondVerifyFaceUrl) {
        this.secondVerifyFaceUrl = secondVerifyFaceUrl;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public void setMirrorVerifyScore(Integer mirrorVerifyScore) {
        this.mirrorVerifyScore = mirrorVerifyScore;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordResponse)) {
            return false;
        }
        VerifyRecordResponse other = (VerifyRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$verifyRecordId = getVerifyRecordId();
        Object other$verifyRecordId = other.getVerifyRecordId();
        if (this$verifyRecordId == null) {
            if (other$verifyRecordId != null) {
                return false;
            }
        } else if (!this$verifyRecordId.equals(other$verifyRecordId)) {
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
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
            return false;
        }
        Object this$snapFaceUrl = getSnapFaceUrl();
        Object other$snapFaceUrl = other.getSnapFaceUrl();
        if (this$snapFaceUrl == null) {
            if (other$snapFaceUrl != null) {
                return false;
            }
        } else if (!this$snapFaceUrl.equals(other$snapFaceUrl)) {
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
            return false;
        }
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
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
        Object this$verifyTimeMillis = getVerifyTimeMillis();
        Object other$verifyTimeMillis = other.getVerifyTimeMillis();
        if (this$verifyTimeMillis == null) {
            if (other$verifyTimeMillis != null) {
                return false;
            }
        } else if (!this$verifyTimeMillis.equals(other$verifyTimeMillis)) {
            return false;
        }
        Object this$verifyTimestamp = getVerifyTimestamp();
        Object other$verifyTimestamp = other.getVerifyTimestamp();
        if (this$verifyTimestamp == null) {
            if (other$verifyTimestamp != null) {
                return false;
            }
        } else if (!this$verifyTimestamp.equals(other$verifyTimestamp)) {
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
        Object this$secondMemberJobNum = getSecondMemberJobNum();
        Object other$secondMemberJobNum = other.getSecondMemberJobNum();
        if (this$secondMemberJobNum == null) {
            if (other$secondMemberJobNum != null) {
                return false;
            }
        } else if (!this$secondMemberJobNum.equals(other$secondMemberJobNum)) {
            return false;
        }
        Object this$secondMemberName = getSecondMemberName();
        Object other$secondMemberName = other.getSecondMemberName();
        if (this$secondMemberName == null) {
            if (other$secondMemberName != null) {
                return false;
            }
        } else if (!this$secondMemberName.equals(other$secondMemberName)) {
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
        Object this$secondVerifyFaceUrl = getSecondVerifyFaceUrl();
        Object other$secondVerifyFaceUrl = other.getSecondVerifyFaceUrl();
        if (this$secondVerifyFaceUrl == null) {
            if (other$secondVerifyFaceUrl != null) {
                return false;
            }
        } else if (!this$secondVerifyFaceUrl.equals(other$secondVerifyFaceUrl)) {
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
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        return this$appType == null ? other$appType == null : this$appType.equals(other$appType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordResponse;
    }

    public int hashCode() {
        Object $verifyRecordId = getVerifyRecordId();
        int result = (1 * 59) + ($verifyRecordId == null ? 43 : $verifyRecordId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result4 = (result3 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result5 = (result4 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberName = getMemberName();
        int result6 = (result5 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberId = getMemberId();
        int result7 = (result6 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $orgId = getOrgId();
        int result8 = (result7 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deptName = getDeptName();
        int result9 = (result8 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $jobNum = getJobNum();
        int result10 = (result9 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $verifyTime = getVerifyTime();
        int result11 = (result10 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
        Object $verifyTimeMillis = getVerifyTimeMillis();
        int result12 = (result11 * 59) + ($verifyTimeMillis == null ? 43 : $verifyTimeMillis.hashCode());
        Object $verifyTimestamp = getVerifyTimestamp();
        int result13 = (result12 * 59) + ($verifyTimestamp == null ? 43 : $verifyTimestamp.hashCode());
        Object $verifyScore = getVerifyScore();
        int result14 = (result13 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $verifyResult = getVerifyResult();
        int result15 = (result14 * 59) + ($verifyResult == null ? 43 : $verifyResult.hashCode());
        Object $secondMemberId = getSecondMemberId();
        int result16 = (result15 * 59) + ($secondMemberId == null ? 43 : $secondMemberId.hashCode());
        Object $secondMemberJobNum = getSecondMemberJobNum();
        int result17 = (result16 * 59) + ($secondMemberJobNum == null ? 43 : $secondMemberJobNum.hashCode());
        Object $secondMemberName = getSecondMemberName();
        int result18 = (result17 * 59) + ($secondMemberName == null ? 43 : $secondMemberName.hashCode());
        Object $secondVerifyScore = getSecondVerifyScore();
        int result19 = (result18 * 59) + ($secondVerifyScore == null ? 43 : $secondVerifyScore.hashCode());
        Object $secondVerifyFaceUrl = getSecondVerifyFaceUrl();
        int result20 = (result19 * 59) + ($secondVerifyFaceUrl == null ? 43 : $secondVerifyFaceUrl.hashCode());
        Object $verifyType = getVerifyType();
        int result21 = (result20 * 59) + ($verifyType == null ? 43 : $verifyType.hashCode());
        Object $mirrorVerifyScore = getMirrorVerifyScore();
        int result22 = (result21 * 59) + ($mirrorVerifyScore == null ? 43 : $mirrorVerifyScore.hashCode());
        Object $appType = getAppType();
        return (result22 * 59) + ($appType == null ? 43 : $appType.hashCode());
    }

    public String toString() {
        return "VerifyRecordResponse(verifyRecordId=" + getVerifyRecordId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", snapFaceUrl=" + getSnapFaceUrl() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberId=" + getMemberId() + ", orgId=" + getOrgId() + ", deptName=" + getDeptName() + ", jobNum=" + getJobNum() + ", verifyTime=" + getVerifyTime() + ", verifyTimeMillis=" + getVerifyTimeMillis() + ", verifyTimestamp=" + getVerifyTimestamp() + ", verifyScore=" + getVerifyScore() + ", verifyResult=" + getVerifyResult() + ", secondMemberId=" + getSecondMemberId() + ", secondMemberJobNum=" + getSecondMemberJobNum() + ", secondMemberName=" + getSecondMemberName() + ", secondVerifyScore=" + getSecondVerifyScore() + ", secondVerifyFaceUrl=" + getSecondVerifyFaceUrl() + ", verifyType=" + getVerifyType() + ", mirrorVerifyScore=" + getMirrorVerifyScore() + ", appType=" + getAppType() + ")";
    }

    public Long getVerifyRecordId() {
        return this.verifyRecordId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Date getVerifyTime() {
        return this.verifyTime;
    }

    public Long getVerifyTimeMillis() {
        return this.verifyTimeMillis;
    }

    public Long getVerifyTimestamp() {
        return this.verifyTimestamp;
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

    public String getSecondMemberJobNum() {
        return this.secondMemberJobNum;
    }

    public String getSecondMemberName() {
        return this.secondMemberName;
    }

    public Integer getSecondVerifyScore() {
        return this.secondVerifyScore;
    }

    public String getSecondVerifyFaceUrl() {
        return this.secondVerifyFaceUrl;
    }

    public Integer getVerifyType() {
        return this.verifyType;
    }

    public Integer getMirrorVerifyScore() {
        return this.mirrorVerifyScore;
    }

    public Integer getAppType() {
        return this.appType;
    }
}
