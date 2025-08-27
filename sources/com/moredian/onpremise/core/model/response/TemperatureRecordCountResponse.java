package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(description = "测温记录统计响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TemperatureRecordCountResponse.class */
public class TemperatureRecordCountResponse implements Serializable {

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "verifyDay", value = "识别日期")
    private Long verifyDay;

    @ApiModelProperty(name = "countNormal", value = "正常人数")
    private Integer countNormal;

    @ApiModelProperty(name = "countHigh", value = "异常人数")
    private Integer countHigh;

    @ApiModelProperty(name = "countNormalTimes", value = "正常次数")
    private Integer countNormalTimes;

    @ApiModelProperty(name = "countHighTimes", value = "异常次数")
    private Integer countHighTimes;

    @ApiModelProperty(name = "min", value = "最低温度")
    private BigDecimal min;

    @ApiModelProperty(name = "max", value = "最高温度")
    private BigDecimal max;

    @ApiModelProperty(name = "avg", value = "平均温度")
    private BigDecimal avg;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setVerifyDay(Long verifyDay) {
        this.verifyDay = verifyDay;
    }

    public void setCountNormal(Integer countNormal) {
        this.countNormal = countNormal;
    }

    public void setCountHigh(Integer countHigh) {
        this.countHigh = countHigh;
    }

    public void setCountNormalTimes(Integer countNormalTimes) {
        this.countNormalTimes = countNormalTimes;
    }

    public void setCountHighTimes(Integer countHighTimes) {
        this.countHighTimes = countHighTimes;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemperatureRecordCountResponse)) {
            return false;
        }
        TemperatureRecordCountResponse other = (TemperatureRecordCountResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$verifyDay = getVerifyDay();
        Object other$verifyDay = other.getVerifyDay();
        if (this$verifyDay == null) {
            if (other$verifyDay != null) {
                return false;
            }
        } else if (!this$verifyDay.equals(other$verifyDay)) {
            return false;
        }
        Object this$countNormal = getCountNormal();
        Object other$countNormal = other.getCountNormal();
        if (this$countNormal == null) {
            if (other$countNormal != null) {
                return false;
            }
        } else if (!this$countNormal.equals(other$countNormal)) {
            return false;
        }
        Object this$countHigh = getCountHigh();
        Object other$countHigh = other.getCountHigh();
        if (this$countHigh == null) {
            if (other$countHigh != null) {
                return false;
            }
        } else if (!this$countHigh.equals(other$countHigh)) {
            return false;
        }
        Object this$countNormalTimes = getCountNormalTimes();
        Object other$countNormalTimes = other.getCountNormalTimes();
        if (this$countNormalTimes == null) {
            if (other$countNormalTimes != null) {
                return false;
            }
        } else if (!this$countNormalTimes.equals(other$countNormalTimes)) {
            return false;
        }
        Object this$countHighTimes = getCountHighTimes();
        Object other$countHighTimes = other.getCountHighTimes();
        if (this$countHighTimes == null) {
            if (other$countHighTimes != null) {
                return false;
            }
        } else if (!this$countHighTimes.equals(other$countHighTimes)) {
            return false;
        }
        Object this$min = getMin();
        Object other$min = other.getMin();
        if (this$min == null) {
            if (other$min != null) {
                return false;
            }
        } else if (!this$min.equals(other$min)) {
            return false;
        }
        Object this$max = getMax();
        Object other$max = other.getMax();
        if (this$max == null) {
            if (other$max != null) {
                return false;
            }
        } else if (!this$max.equals(other$max)) {
            return false;
        }
        Object this$avg = getAvg();
        Object other$avg = other.getAvg();
        return this$avg == null ? other$avg == null : this$avg.equals(other$avg);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TemperatureRecordCountResponse;
    }

    public int hashCode() {
        Object $deptName = getDeptName();
        int result = (1 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $verifyDay = getVerifyDay();
        int result2 = (result * 59) + ($verifyDay == null ? 43 : $verifyDay.hashCode());
        Object $countNormal = getCountNormal();
        int result3 = (result2 * 59) + ($countNormal == null ? 43 : $countNormal.hashCode());
        Object $countHigh = getCountHigh();
        int result4 = (result3 * 59) + ($countHigh == null ? 43 : $countHigh.hashCode());
        Object $countNormalTimes = getCountNormalTimes();
        int result5 = (result4 * 59) + ($countNormalTimes == null ? 43 : $countNormalTimes.hashCode());
        Object $countHighTimes = getCountHighTimes();
        int result6 = (result5 * 59) + ($countHighTimes == null ? 43 : $countHighTimes.hashCode());
        Object $min = getMin();
        int result7 = (result6 * 59) + ($min == null ? 43 : $min.hashCode());
        Object $max = getMax();
        int result8 = (result7 * 59) + ($max == null ? 43 : $max.hashCode());
        Object $avg = getAvg();
        return (result8 * 59) + ($avg == null ? 43 : $avg.hashCode());
    }

    public String toString() {
        return "TemperatureRecordCountResponse(deptName=" + getDeptName() + ", verifyDay=" + getVerifyDay() + ", countNormal=" + getCountNormal() + ", countHigh=" + getCountHigh() + ", countNormalTimes=" + getCountNormalTimes() + ", countHighTimes=" + getCountHighTimes() + ", min=" + getMin() + ", max=" + getMax() + ", avg=" + getAvg() + ")";
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Long getVerifyDay() {
        return this.verifyDay;
    }

    public Integer getCountNormal() {
        return this.countNormal;
    }

    public Integer getCountHigh() {
        return this.countHigh;
    }

    public Integer getCountNormalTimes() {
        return this.countNormalTimes;
    }

    public Integer getCountHighTimes() {
        return this.countHighTimes;
    }

    public BigDecimal getMin() {
        return this.min;
    }

    public BigDecimal getMax() {
        return this.max;
    }

    public BigDecimal getAvg() {
        return this.avg;
    }
}
