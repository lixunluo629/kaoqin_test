package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ApiModel(description = "每日统计列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListInfoForDayDetailResponse.class */
public class ListInfoForDayDetailResponse implements Serializable {

    @ApiModelProperty(name = "memberId", value = "成员id", hidden = true)
    private Long memberId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id", hidden = true)
    private Long attendanceGroupId;

    @ApiModelProperty(name = "memberName", value = "姓名")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "workTime", value = "工时，单位秒")
    private Long workTime = 0L;

    @ApiModelProperty(name = "listInfoDayAttendanceDetails", value = "打卡记录")
    private List<ListInfoDayAttendanceDetail> listInfoDayAttendanceDetails;

    @ApiModelProperty(name = "attendanceResult", value = "打卡结果：1-打卡成功，2-迟到，3-早退，4-缺卡")
    private Integer[] attendanceResult;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public void setListInfoDayAttendanceDetails(List<ListInfoDayAttendanceDetail> listInfoDayAttendanceDetails) {
        this.listInfoDayAttendanceDetails = listInfoDayAttendanceDetails;
    }

    public void setAttendanceResult(Integer[] attendanceResult) {
        this.attendanceResult = attendanceResult;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListInfoForDayDetailResponse)) {
            return false;
        }
        ListInfoForDayDetailResponse other = (ListInfoForDayDetailResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
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
        Object this$workTime = getWorkTime();
        Object other$workTime = other.getWorkTime();
        if (this$workTime == null) {
            if (other$workTime != null) {
                return false;
            }
        } else if (!this$workTime.equals(other$workTime)) {
            return false;
        }
        Object this$listInfoDayAttendanceDetails = getListInfoDayAttendanceDetails();
        Object other$listInfoDayAttendanceDetails = other.getListInfoDayAttendanceDetails();
        if (this$listInfoDayAttendanceDetails == null) {
            if (other$listInfoDayAttendanceDetails != null) {
                return false;
            }
        } else if (!this$listInfoDayAttendanceDetails.equals(other$listInfoDayAttendanceDetails)) {
            return false;
        }
        return Arrays.deepEquals(getAttendanceResult(), other.getAttendanceResult());
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListInfoForDayDetailResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result2 = (result * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $workTime = getWorkTime();
        int result6 = (result5 * 59) + ($workTime == null ? 43 : $workTime.hashCode());
        Object $listInfoDayAttendanceDetails = getListInfoDayAttendanceDetails();
        return (((result6 * 59) + ($listInfoDayAttendanceDetails == null ? 43 : $listInfoDayAttendanceDetails.hashCode())) * 59) + Arrays.deepHashCode(getAttendanceResult());
    }

    public String toString() {
        return "ListInfoForDayDetailResponse(memberId=" + getMemberId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptName=" + getDeptName() + ", workTime=" + getWorkTime() + ", listInfoDayAttendanceDetails=" + getListInfoDayAttendanceDetails() + ", attendanceResult=" + Arrays.deepToString(getAttendanceResult()) + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
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

    public Long getWorkTime() {
        return this.workTime;
    }

    public List<ListInfoDayAttendanceDetail> getListInfoDayAttendanceDetails() {
        return this.listInfoDayAttendanceDetails;
    }

    public Integer[] getAttendanceResult() {
        return this.attendanceResult;
    }

    @ApiModel(description = "打卡响应信息")
    /* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListInfoForDayDetailResponse$ListInfoDayAttendanceDetail.class */
    public class ListInfoDayAttendanceDetail implements Serializable {

        @ApiModelProperty(name = "ruleTime", value = "规定考勤时间")
        private Long ruleTime;

        @ApiModelProperty(name = "attendanceTime", value = "实际打卡时间")
        private Long attendanceTime;

        @ApiModelProperty(name = "attendanceResult", value = "打卡结果：1-打卡成功，2-迟到，3-早退，4-缺卡")
        private Integer attendanceResult;

        @ApiModelProperty(name = "recordType", value = "打卡类型：1-上班，2-下班")
        private Integer recordType;

        public ListInfoDayAttendanceDetail() {
        }

        public void setRuleTime(Long ruleTime) {
            this.ruleTime = ruleTime;
        }

        public void setAttendanceTime(Long attendanceTime) {
            this.attendanceTime = attendanceTime;
        }

        public void setAttendanceResult(Integer attendanceResult) {
            this.attendanceResult = attendanceResult;
        }

        public void setRecordType(Integer recordType) {
            this.recordType = recordType;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ListInfoDayAttendanceDetail)) {
                return false;
            }
            ListInfoDayAttendanceDetail other = (ListInfoDayAttendanceDetail) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$ruleTime = getRuleTime();
            Object other$ruleTime = other.getRuleTime();
            if (this$ruleTime == null) {
                if (other$ruleTime != null) {
                    return false;
                }
            } else if (!this$ruleTime.equals(other$ruleTime)) {
                return false;
            }
            Object this$attendanceTime = getAttendanceTime();
            Object other$attendanceTime = other.getAttendanceTime();
            if (this$attendanceTime == null) {
                if (other$attendanceTime != null) {
                    return false;
                }
            } else if (!this$attendanceTime.equals(other$attendanceTime)) {
                return false;
            }
            Object this$attendanceResult = getAttendanceResult();
            Object other$attendanceResult = other.getAttendanceResult();
            if (this$attendanceResult == null) {
                if (other$attendanceResult != null) {
                    return false;
                }
            } else if (!this$attendanceResult.equals(other$attendanceResult)) {
                return false;
            }
            Object this$recordType = getRecordType();
            Object other$recordType = other.getRecordType();
            return this$recordType == null ? other$recordType == null : this$recordType.equals(other$recordType);
        }

        protected boolean canEqual(Object other) {
            return other instanceof ListInfoDayAttendanceDetail;
        }

        public int hashCode() {
            Object $ruleTime = getRuleTime();
            int result = (1 * 59) + ($ruleTime == null ? 43 : $ruleTime.hashCode());
            Object $attendanceTime = getAttendanceTime();
            int result2 = (result * 59) + ($attendanceTime == null ? 43 : $attendanceTime.hashCode());
            Object $attendanceResult = getAttendanceResult();
            int result3 = (result2 * 59) + ($attendanceResult == null ? 43 : $attendanceResult.hashCode());
            Object $recordType = getRecordType();
            return (result3 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        }

        public String toString() {
            return "ListInfoForDayDetailResponse.ListInfoDayAttendanceDetail(ruleTime=" + getRuleTime() + ", attendanceTime=" + getAttendanceTime() + ", attendanceResult=" + getAttendanceResult() + ", recordType=" + getRecordType() + ")";
        }

        public Long getRuleTime() {
            return this.ruleTime;
        }

        public Long getAttendanceTime() {
            return this.attendanceTime;
        }

        public Integer getAttendanceResult() {
            return this.attendanceResult;
        }

        public Integer getRecordType() {
            return this.recordType;
        }
    }
}
