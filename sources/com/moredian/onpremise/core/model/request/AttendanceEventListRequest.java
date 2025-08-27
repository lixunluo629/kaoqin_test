package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "考勤事件分页查询请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/AttendanceEventListRequest.class */
public class AttendanceEventListRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "eventType", value = "事件类型 ：1-请假，2-出差，3-外出，4-加班，5-补卡")
    private Integer eventType;

    @ApiModelProperty(name = "startTimeStr", value = "开始时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间，格式：yyyy-MM-dd 或者 yyyy-MM-dd hh:mm:ss")
    private String endTimeStr;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventListRequest)) {
            return false;
        }
        AttendanceEventListRequest other = (AttendanceEventListRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
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
        Object this$eventType = getEventType();
        Object other$eventType = other.getEventType();
        if (this$eventType == null) {
            if (other$eventType != null) {
                return false;
            }
        } else if (!this$eventType.equals(other$eventType)) {
            return false;
        }
        Object this$startTimeStr = getStartTimeStr();
        Object other$startTimeStr = other.getStartTimeStr();
        if (this$startTimeStr == null) {
            if (other$startTimeStr != null) {
                return false;
            }
        } else if (!this$startTimeStr.equals(other$startTimeStr)) {
            return false;
        }
        Object this$endTimeStr = getEndTimeStr();
        Object other$endTimeStr = other.getEndTimeStr();
        return this$endTimeStr == null ? other$endTimeStr == null : this$endTimeStr.equals(other$endTimeStr);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $eventType = getEventType();
        int result3 = (result2 * 59) + ($eventType == null ? 43 : $eventType.hashCode());
        Object $startTimeStr = getStartTimeStr();
        int result4 = (result3 * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        return (result4 * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "AttendanceEventListRequest(paginator=" + getPaginator() + ", memberName=" + getMemberName() + ", eventType=" + getEventType() + ", startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Integer getEventType() {
        return this.eventType;
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }
}
