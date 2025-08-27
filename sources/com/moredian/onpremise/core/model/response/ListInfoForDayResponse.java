package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "每日统计列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListInfoForDayResponse.class */
public class ListInfoForDayResponse implements Serializable {

    @ApiModelProperty(name = "countDay", value = "统计日期")
    private String countDay;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberNums", value = "成员总数")
    private Integer memberNums = 0;

    @ApiModelProperty(name = "normalNums", value = "正常考勤人数")
    private Integer normalNums = 0;

    @ApiModelProperty(name = "lateNums", value = "迟到人数")
    private Integer lateNums = 0;

    @ApiModelProperty(name = "earlyNums", value = "早退人数")
    private Integer earlyNums = 0;

    @ApiModelProperty(name = "workLackNums", value = "上班缺卡人数")
    private Integer workLackNums = 0;

    @ApiModelProperty(name = "restLackNums", value = "下班缺卡人数")
    private Integer restLackNums = 0;

    @ApiModelProperty(name = "totalWorkTime", value = "总工时，单位秒")
    private Long totalWorkTime = 0L;

    @ApiModelProperty(name = "avgWorkTime", value = "平均工时，单位秒")
    private Long avgWorkTime = 0L;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "attendanceGroupName", value = "考勤组名称")
    private String attendanceGroupName;

    public void setCountDay(String countDay) {
        this.countDay = countDay;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setMemberNums(Integer memberNums) {
        this.memberNums = memberNums;
    }

    public void setNormalNums(Integer normalNums) {
        this.normalNums = normalNums;
    }

    public void setLateNums(Integer lateNums) {
        this.lateNums = lateNums;
    }

    public void setEarlyNums(Integer earlyNums) {
        this.earlyNums = earlyNums;
    }

    public void setWorkLackNums(Integer workLackNums) {
        this.workLackNums = workLackNums;
    }

    public void setRestLackNums(Integer restLackNums) {
        this.restLackNums = restLackNums;
    }

    public void setTotalWorkTime(Long totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public void setAvgWorkTime(Long avgWorkTime) {
        this.avgWorkTime = avgWorkTime;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setAttendanceGroupName(String attendanceGroupName) {
        this.attendanceGroupName = attendanceGroupName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListInfoForDayResponse)) {
            return false;
        }
        ListInfoForDayResponse other = (ListInfoForDayResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$countDay = getCountDay();
        Object other$countDay = other.getCountDay();
        if (this$countDay == null) {
            if (other$countDay != null) {
                return false;
            }
        } else if (!this$countDay.equals(other$countDay)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
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
        Object this$memberNums = getMemberNums();
        Object other$memberNums = other.getMemberNums();
        if (this$memberNums == null) {
            if (other$memberNums != null) {
                return false;
            }
        } else if (!this$memberNums.equals(other$memberNums)) {
            return false;
        }
        Object this$normalNums = getNormalNums();
        Object other$normalNums = other.getNormalNums();
        if (this$normalNums == null) {
            if (other$normalNums != null) {
                return false;
            }
        } else if (!this$normalNums.equals(other$normalNums)) {
            return false;
        }
        Object this$lateNums = getLateNums();
        Object other$lateNums = other.getLateNums();
        if (this$lateNums == null) {
            if (other$lateNums != null) {
                return false;
            }
        } else if (!this$lateNums.equals(other$lateNums)) {
            return false;
        }
        Object this$earlyNums = getEarlyNums();
        Object other$earlyNums = other.getEarlyNums();
        if (this$earlyNums == null) {
            if (other$earlyNums != null) {
                return false;
            }
        } else if (!this$earlyNums.equals(other$earlyNums)) {
            return false;
        }
        Object this$workLackNums = getWorkLackNums();
        Object other$workLackNums = other.getWorkLackNums();
        if (this$workLackNums == null) {
            if (other$workLackNums != null) {
                return false;
            }
        } else if (!this$workLackNums.equals(other$workLackNums)) {
            return false;
        }
        Object this$restLackNums = getRestLackNums();
        Object other$restLackNums = other.getRestLackNums();
        if (this$restLackNums == null) {
            if (other$restLackNums != null) {
                return false;
            }
        } else if (!this$restLackNums.equals(other$restLackNums)) {
            return false;
        }
        Object this$totalWorkTime = getTotalWorkTime();
        Object other$totalWorkTime = other.getTotalWorkTime();
        if (this$totalWorkTime == null) {
            if (other$totalWorkTime != null) {
                return false;
            }
        } else if (!this$totalWorkTime.equals(other$totalWorkTime)) {
            return false;
        }
        Object this$avgWorkTime = getAvgWorkTime();
        Object other$avgWorkTime = other.getAvgWorkTime();
        if (this$avgWorkTime == null) {
            if (other$avgWorkTime != null) {
                return false;
            }
        } else if (!this$avgWorkTime.equals(other$avgWorkTime)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
            return false;
        }
        Object this$attendanceGroupName = getAttendanceGroupName();
        Object other$attendanceGroupName = other.getAttendanceGroupName();
        return this$attendanceGroupName == null ? other$attendanceGroupName == null : this$attendanceGroupName.equals(other$attendanceGroupName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListInfoForDayResponse;
    }

    public int hashCode() {
        Object $countDay = getCountDay();
        int result = (1 * 59) + ($countDay == null ? 43 : $countDay.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $memberNums = getMemberNums();
        int result4 = (result3 * 59) + ($memberNums == null ? 43 : $memberNums.hashCode());
        Object $normalNums = getNormalNums();
        int result5 = (result4 * 59) + ($normalNums == null ? 43 : $normalNums.hashCode());
        Object $lateNums = getLateNums();
        int result6 = (result5 * 59) + ($lateNums == null ? 43 : $lateNums.hashCode());
        Object $earlyNums = getEarlyNums();
        int result7 = (result6 * 59) + ($earlyNums == null ? 43 : $earlyNums.hashCode());
        Object $workLackNums = getWorkLackNums();
        int result8 = (result7 * 59) + ($workLackNums == null ? 43 : $workLackNums.hashCode());
        Object $restLackNums = getRestLackNums();
        int result9 = (result8 * 59) + ($restLackNums == null ? 43 : $restLackNums.hashCode());
        Object $totalWorkTime = getTotalWorkTime();
        int result10 = (result9 * 59) + ($totalWorkTime == null ? 43 : $totalWorkTime.hashCode());
        Object $avgWorkTime = getAvgWorkTime();
        int result11 = (result10 * 59) + ($avgWorkTime == null ? 43 : $avgWorkTime.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result12 = (result11 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $attendanceGroupName = getAttendanceGroupName();
        return (result12 * 59) + ($attendanceGroupName == null ? 43 : $attendanceGroupName.hashCode());
    }

    public String toString() {
        return "ListInfoForDayResponse(countDay=" + getCountDay() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", memberNums=" + getMemberNums() + ", normalNums=" + getNormalNums() + ", lateNums=" + getLateNums() + ", earlyNums=" + getEarlyNums() + ", workLackNums=" + getWorkLackNums() + ", restLackNums=" + getRestLackNums() + ", totalWorkTime=" + getTotalWorkTime() + ", avgWorkTime=" + getAvgWorkTime() + ", attendanceGroupId=" + getAttendanceGroupId() + ", attendanceGroupName=" + getAttendanceGroupName() + ")";
    }

    public String getCountDay() {
        return this.countDay;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Integer getMemberNums() {
        return this.memberNums;
    }

    public Integer getNormalNums() {
        return this.normalNums;
    }

    public Integer getLateNums() {
        return this.lateNums;
    }

    public Integer getEarlyNums() {
        return this.earlyNums;
    }

    public Integer getWorkLackNums() {
        return this.workLackNums;
    }

    public Integer getRestLackNums() {
        return this.restLackNums;
    }

    public Long getTotalWorkTime() {
        return this.totalWorkTime;
    }

    public Long getAvgWorkTime() {
        return this.avgWorkTime;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getAttendanceGroupName() {
        return this.attendanceGroupName;
    }
}
