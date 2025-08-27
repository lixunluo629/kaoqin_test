package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "成员列表查询对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MemberListRequest.class */
public class MemberListRequest extends BaseRequest {
    private static final long serialVersionUID = 2635448859029240136L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "hasFace", value = "是否有底库照片：1-有，2-无")
    private Integer hasFace;

    @ApiModelProperty(name = "needSubMember", value = "是否需要子部门下成员：1-是，2-否")
    private Integer needSubMember;

    @ApiModelProperty(name = "needDeptName", value = "是否显示部门名称：1-是，2-否;默认显示")
    private Integer needDeptName;

    @ApiModelProperty(name = "deptIds", value = "部门ids", hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty(name = "manageDeptIds", value = "部门ids", hidden = true)
    private List<String> manageDeptIds;

    @ApiModelProperty(name = "healthCodeStatus", value = "是否绿码：1-绿码，2-非绿码")
    private Integer healthCodeStatus;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setHasFace(Integer hasFace) {
        this.hasFace = hasFace;
    }

    public void setNeedSubMember(Integer needSubMember) {
        this.needSubMember = needSubMember;
    }

    public void setNeedDeptName(Integer needDeptName) {
        this.needDeptName = needDeptName;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setManageDeptIds(List<String> manageDeptIds) {
        this.manageDeptIds = manageDeptIds;
    }

    public void setHealthCodeStatus(Integer healthCodeStatus) {
        this.healthCodeStatus = healthCodeStatus;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberListRequest)) {
            return false;
        }
        MemberListRequest other = (MemberListRequest) o;
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
        Object this$hasFace = getHasFace();
        Object other$hasFace = other.getHasFace();
        if (this$hasFace == null) {
            if (other$hasFace != null) {
                return false;
            }
        } else if (!this$hasFace.equals(other$hasFace)) {
            return false;
        }
        Object this$needSubMember = getNeedSubMember();
        Object other$needSubMember = other.getNeedSubMember();
        if (this$needSubMember == null) {
            if (other$needSubMember != null) {
                return false;
            }
        } else if (!this$needSubMember.equals(other$needSubMember)) {
            return false;
        }
        Object this$needDeptName = getNeedDeptName();
        Object other$needDeptName = other.getNeedDeptName();
        if (this$needDeptName == null) {
            if (other$needDeptName != null) {
                return false;
            }
        } else if (!this$needDeptName.equals(other$needDeptName)) {
            return false;
        }
        Object this$deptIds = getDeptIds();
        Object other$deptIds = other.getDeptIds();
        if (this$deptIds == null) {
            if (other$deptIds != null) {
                return false;
            }
        } else if (!this$deptIds.equals(other$deptIds)) {
            return false;
        }
        Object this$manageDeptIds = getManageDeptIds();
        Object other$manageDeptIds = other.getManageDeptIds();
        if (this$manageDeptIds == null) {
            if (other$manageDeptIds != null) {
                return false;
            }
        } else if (!this$manageDeptIds.equals(other$manageDeptIds)) {
            return false;
        }
        Object this$healthCodeStatus = getHealthCodeStatus();
        Object other$healthCodeStatus = other.getHealthCodeStatus();
        return this$healthCodeStatus == null ? other$healthCodeStatus == null : this$healthCodeStatus.equals(other$healthCodeStatus);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof MemberListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $hasFace = getHasFace();
        int result3 = (result2 * 59) + ($hasFace == null ? 43 : $hasFace.hashCode());
        Object $needSubMember = getNeedSubMember();
        int result4 = (result3 * 59) + ($needSubMember == null ? 43 : $needSubMember.hashCode());
        Object $needDeptName = getNeedDeptName();
        int result5 = (result4 * 59) + ($needDeptName == null ? 43 : $needDeptName.hashCode());
        Object $deptIds = getDeptIds();
        int result6 = (result5 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $manageDeptIds = getManageDeptIds();
        int result7 = (result6 * 59) + ($manageDeptIds == null ? 43 : $manageDeptIds.hashCode());
        Object $healthCodeStatus = getHealthCodeStatus();
        return (result7 * 59) + ($healthCodeStatus == null ? 43 : $healthCodeStatus.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "MemberListRequest(paginator=" + getPaginator() + ", deptId=" + getDeptId() + ", hasFace=" + getHasFace() + ", needSubMember=" + getNeedSubMember() + ", needDeptName=" + getNeedDeptName() + ", deptIds=" + getDeptIds() + ", manageDeptIds=" + getManageDeptIds() + ", healthCodeStatus=" + getHealthCodeStatus() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Integer getHasFace() {
        return this.hasFace;
    }

    public Integer getNeedSubMember() {
        return this.needSubMember;
    }

    public Integer getNeedDeptName() {
        return this.needDeptName;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public List<String> getManageDeptIds() {
        return this.manageDeptIds;
    }

    public Integer getHealthCodeStatus() {
        return this.healthCodeStatus;
    }
}
