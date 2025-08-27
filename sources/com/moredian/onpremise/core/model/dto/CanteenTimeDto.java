package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "餐厅就餐时间信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/CanteenTimeDto.class */
public class CanteenTimeDto implements Serializable {
    private static final long serialVersionUID = -7040794442657308732L;

    @ApiModelProperty(name = "mealCanteenTimeId", value = "mealCanteenTimeId", hidden = true)
    private Long mealCanteenTimeId;

    @ApiModelProperty(name = "timeType", value = "就餐时间类型：1-早餐，2-午餐，3-晚餐，4-宵夜")
    private Integer timeType;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间")
    private String endTime;

    public void setMealCanteenTimeId(Long mealCanteenTimeId) {
        this.mealCanteenTimeId = mealCanteenTimeId;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String toString() {
        return "CanteenTimeDto(mealCanteenTimeId=" + getMealCanteenTimeId() + ", timeType=" + getTimeType() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ")";
    }

    public Long getMealCanteenTimeId() {
        return this.mealCanteenTimeId;
    }

    public Integer getTimeType() {
        return this.timeType;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CanteenTimeDto timeDto = (CanteenTimeDto) o;
        return Objects.equals(this.timeType, timeDto.timeType) && Objects.equals(this.startTime, timeDto.startTime) && Objects.equals(this.endTime, timeDto.endTime);
    }

    public int hashCode() {
        return Objects.hash(this.timeType, this.startTime, this.endTime);
    }
}
