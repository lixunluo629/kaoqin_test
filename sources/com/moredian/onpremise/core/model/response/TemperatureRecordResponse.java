package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(description = "测温记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TemperatureRecordResponse.class */
public class TemperatureRecordResponse implements Serializable {
    private static final long serialVersionUID = 9182572178535383802L;

    @ApiModelProperty(name = "memberName", value = "员工名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "员工工号")
    private String memberJobNum;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    @ApiModelProperty(name = "snapFaceUrl", value = "抓拍照url")
    private String snapFaceUrl;

    @ApiModelProperty(name = "temperatureValue", value = "测温值,-1时表示测温异常")
    private BigDecimal temperatureValue;

    @ApiModelProperty(name = "temperatureStatus", value = "温度状态：1-正常，2-高温")
    private Integer temperatureStatus;

    @ApiModelProperty(name = "operator", value = "操作人员")
    private String operator;

    @ApiModelProperty(name = "healthCode", value = "健康码")
    private String healthCode;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public void setTemperatureValue(BigDecimal temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public void setTemperatureStatus(Integer temperatureStatus) {
        this.temperatureStatus = temperatureStatus;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemperatureRecordResponse)) {
            return false;
        }
        TemperatureRecordResponse other = (TemperatureRecordResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
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
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        if (this$verifyTime == null) {
            if (other$verifyTime != null) {
                return false;
            }
        } else if (!this$verifyTime.equals(other$verifyTime)) {
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
        Object this$temperatureValue = getTemperatureValue();
        Object other$temperatureValue = other.getTemperatureValue();
        if (this$temperatureValue == null) {
            if (other$temperatureValue != null) {
                return false;
            }
        } else if (!this$temperatureValue.equals(other$temperatureValue)) {
            return false;
        }
        Object this$temperatureStatus = getTemperatureStatus();
        Object other$temperatureStatus = other.getTemperatureStatus();
        if (this$temperatureStatus == null) {
            if (other$temperatureStatus != null) {
                return false;
            }
        } else if (!this$temperatureStatus.equals(other$temperatureStatus)) {
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
        Object this$healthCode = getHealthCode();
        Object other$healthCode = other.getHealthCode();
        return this$healthCode == null ? other$healthCode == null : this$healthCode.equals(other$healthCode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TemperatureRecordResponse;
    }

    public int hashCode() {
        Object $memberName = getMemberName();
        int result = (1 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result2 = (result * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result4 = (result3 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result5 = (result4 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $verifyTime = getVerifyTime();
        int result6 = (result5 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
        Object $snapFaceUrl = getSnapFaceUrl();
        int result7 = (result6 * 59) + ($snapFaceUrl == null ? 43 : $snapFaceUrl.hashCode());
        Object $temperatureValue = getTemperatureValue();
        int result8 = (result7 * 59) + ($temperatureValue == null ? 43 : $temperatureValue.hashCode());
        Object $temperatureStatus = getTemperatureStatus();
        int result9 = (result8 * 59) + ($temperatureStatus == null ? 43 : $temperatureStatus.hashCode());
        Object $operator = getOperator();
        int result10 = (result9 * 59) + ($operator == null ? 43 : $operator.hashCode());
        Object $healthCode = getHealthCode();
        return (result10 * 59) + ($healthCode == null ? 43 : $healthCode.hashCode());
    }

    public String toString() {
        return "TemperatureRecordResponse(memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptName=" + getDeptName() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", verifyTime=" + getVerifyTime() + ", snapFaceUrl=" + getSnapFaceUrl() + ", temperatureValue=" + getTemperatureValue() + ", temperatureStatus=" + getTemperatureStatus() + ", operator=" + getOperator() + ", healthCode=" + getHealthCode() + ")";
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Long getVerifyTime() {
        return this.verifyTime;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public BigDecimal getTemperatureValue() {
        return this.temperatureValue;
    }

    public Integer getTemperatureStatus() {
        return this.temperatureStatus;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getHealthCode() {
        return this.healthCode;
    }
}
