package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "就餐人员列表查询")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListMealMemberRequest.class */
public class ListMealMemberRequest extends BaseRequest {
    private static final long serialVersionUID = 2987663918378185570L;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "name", value = "员工姓名或者工号")
    private String name;

    @ApiModelProperty(name = "managerDeptId", value = "管理员管理部门id", hidden = true)
    private List<String> managerDeptId;

    @ApiModelProperty(name = "memberType", value = "人员类型：1-正式员工，2-临时员工")
    private Integer memberType;

    @ApiModelProperty(name = "roomStatus", value = "住宿：1-是，0-否")
    private Integer roomStatus;

    @ApiModelProperty(name = "shiftStatus", value = "班次：1-白班，2-夜班")
    private Integer shiftStatus;

    @ApiModelProperty(name = "memberJoinTimeStart", value = "入职时间开始，格式：yyyy-MM-dd")
    private String memberJoinTimeStart;

    @ApiModelProperty(name = "memberJoinTimeEnd", value = "入职时间结束，格式：yyyy-MM-dd")
    private String memberJoinTimeEnd;

    @ApiModelProperty(name = "memberRetireTimeStart", value = "离职时间开始，格式：yyyy-MM-dd")
    private String memberRetireTimeStart;

    @ApiModelProperty(name = "memberRetireTimeEnd", value = "离职时间结束，格式：yyyy-MM-dd")
    private String memberRetireTimeEnd;

    @ApiModelProperty(name = "mealDeptName", value = "组织机构名")
    private String mealDeptName;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManagerDeptId(List<String> managerDeptId) {
        this.managerDeptId = managerDeptId;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public void setMemberJoinTimeStart(String memberJoinTimeStart) {
        this.memberJoinTimeStart = memberJoinTimeStart;
    }

    public void setMemberJoinTimeEnd(String memberJoinTimeEnd) {
        this.memberJoinTimeEnd = memberJoinTimeEnd;
    }

    public void setMemberRetireTimeStart(String memberRetireTimeStart) {
        this.memberRetireTimeStart = memberRetireTimeStart;
    }

    public void setMemberRetireTimeEnd(String memberRetireTimeEnd) {
        this.memberRetireTimeEnd = memberRetireTimeEnd;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListMealMemberRequest)) {
            return false;
        }
        ListMealMemberRequest other = (ListMealMemberRequest) o;
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$managerDeptId = getManagerDeptId();
        Object other$managerDeptId = other.getManagerDeptId();
        if (this$managerDeptId == null) {
            if (other$managerDeptId != null) {
                return false;
            }
        } else if (!this$managerDeptId.equals(other$managerDeptId)) {
            return false;
        }
        Object this$memberType = getMemberType();
        Object other$memberType = other.getMemberType();
        if (this$memberType == null) {
            if (other$memberType != null) {
                return false;
            }
        } else if (!this$memberType.equals(other$memberType)) {
            return false;
        }
        Object this$roomStatus = getRoomStatus();
        Object other$roomStatus = other.getRoomStatus();
        if (this$roomStatus == null) {
            if (other$roomStatus != null) {
                return false;
            }
        } else if (!this$roomStatus.equals(other$roomStatus)) {
            return false;
        }
        Object this$shiftStatus = getShiftStatus();
        Object other$shiftStatus = other.getShiftStatus();
        if (this$shiftStatus == null) {
            if (other$shiftStatus != null) {
                return false;
            }
        } else if (!this$shiftStatus.equals(other$shiftStatus)) {
            return false;
        }
        Object this$memberJoinTimeStart = getMemberJoinTimeStart();
        Object other$memberJoinTimeStart = other.getMemberJoinTimeStart();
        if (this$memberJoinTimeStart == null) {
            if (other$memberJoinTimeStart != null) {
                return false;
            }
        } else if (!this$memberJoinTimeStart.equals(other$memberJoinTimeStart)) {
            return false;
        }
        Object this$memberJoinTimeEnd = getMemberJoinTimeEnd();
        Object other$memberJoinTimeEnd = other.getMemberJoinTimeEnd();
        if (this$memberJoinTimeEnd == null) {
            if (other$memberJoinTimeEnd != null) {
                return false;
            }
        } else if (!this$memberJoinTimeEnd.equals(other$memberJoinTimeEnd)) {
            return false;
        }
        Object this$memberRetireTimeStart = getMemberRetireTimeStart();
        Object other$memberRetireTimeStart = other.getMemberRetireTimeStart();
        if (this$memberRetireTimeStart == null) {
            if (other$memberRetireTimeStart != null) {
                return false;
            }
        } else if (!this$memberRetireTimeStart.equals(other$memberRetireTimeStart)) {
            return false;
        }
        Object this$memberRetireTimeEnd = getMemberRetireTimeEnd();
        Object other$memberRetireTimeEnd = other.getMemberRetireTimeEnd();
        if (this$memberRetireTimeEnd == null) {
            if (other$memberRetireTimeEnd != null) {
                return false;
            }
        } else if (!this$memberRetireTimeEnd.equals(other$memberRetireTimeEnd)) {
            return false;
        }
        Object this$mealDeptName = getMealDeptName();
        Object other$mealDeptName = other.getMealDeptName();
        return this$mealDeptName == null ? other$mealDeptName == null : this$mealDeptName.equals(other$mealDeptName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListMealMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $managerDeptId = getManagerDeptId();
        int result4 = (result3 * 59) + ($managerDeptId == null ? 43 : $managerDeptId.hashCode());
        Object $memberType = getMemberType();
        int result5 = (result4 * 59) + ($memberType == null ? 43 : $memberType.hashCode());
        Object $roomStatus = getRoomStatus();
        int result6 = (result5 * 59) + ($roomStatus == null ? 43 : $roomStatus.hashCode());
        Object $shiftStatus = getShiftStatus();
        int result7 = (result6 * 59) + ($shiftStatus == null ? 43 : $shiftStatus.hashCode());
        Object $memberJoinTimeStart = getMemberJoinTimeStart();
        int result8 = (result7 * 59) + ($memberJoinTimeStart == null ? 43 : $memberJoinTimeStart.hashCode());
        Object $memberJoinTimeEnd = getMemberJoinTimeEnd();
        int result9 = (result8 * 59) + ($memberJoinTimeEnd == null ? 43 : $memberJoinTimeEnd.hashCode());
        Object $memberRetireTimeStart = getMemberRetireTimeStart();
        int result10 = (result9 * 59) + ($memberRetireTimeStart == null ? 43 : $memberRetireTimeStart.hashCode());
        Object $memberRetireTimeEnd = getMemberRetireTimeEnd();
        int result11 = (result10 * 59) + ($memberRetireTimeEnd == null ? 43 : $memberRetireTimeEnd.hashCode());
        Object $mealDeptName = getMealDeptName();
        return (result11 * 59) + ($mealDeptName == null ? 43 : $mealDeptName.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListMealMemberRequest(paginator=" + getPaginator() + ", deptId=" + getDeptId() + ", name=" + getName() + ", managerDeptId=" + getManagerDeptId() + ", memberType=" + getMemberType() + ", roomStatus=" + getRoomStatus() + ", shiftStatus=" + getShiftStatus() + ", memberJoinTimeStart=" + getMemberJoinTimeStart() + ", memberJoinTimeEnd=" + getMemberJoinTimeEnd() + ", memberRetireTimeStart=" + getMemberRetireTimeStart() + ", memberRetireTimeEnd=" + getMemberRetireTimeEnd() + ", mealDeptName=" + getMealDeptName() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getManagerDeptId() {
        return this.managerDeptId;
    }

    public Integer getMemberType() {
        return this.memberType;
    }

    public Integer getRoomStatus() {
        return this.roomStatus;
    }

    public Integer getShiftStatus() {
        return this.shiftStatus;
    }

    public String getMemberJoinTimeStart() {
        return this.memberJoinTimeStart;
    }

    public String getMemberJoinTimeEnd() {
        return this.memberJoinTimeEnd;
    }

    public String getMemberRetireTimeStart() {
        return this.memberRetireTimeStart;
    }

    public String getMemberRetireTimeEnd() {
        return this.memberRetireTimeEnd;
    }

    public String getMealDeptName() {
        return this.mealDeptName;
    }
}
