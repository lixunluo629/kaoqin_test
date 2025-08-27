package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "成员权限组列表查询对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MemberGroupListRequest.class */
public class MemberGroupListRequest extends BaseRequest {
    private static final long serialVersionUID = -4405740040490750774L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    @ApiModelProperty(name = "deptIds", value = "部门id列表", hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty(name = "memberIds", value = "成员id列表", hidden = true)
    private List<Long> memberIds;

    @ApiModelProperty(name = "type", value = "查询条件组装，1：部门；2：部门+权限组；3/4-权限组", hidden = true)
    private Integer type;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberGroupListRequest)) {
            return false;
        }
        MemberGroupListRequest other = (MemberGroupListRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
            if (other$groupId != null) {
                return false;
            }
        } else if (!this$groupId.equals(other$groupId)) {
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
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof MemberGroupListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result3 = (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $groupId = getGroupId();
        int result4 = (result3 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $deptIds = getDeptIds();
        int result5 = (result4 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $memberIds = getMemberIds();
        int result6 = (result5 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $type = getType();
        return (result6 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "MemberGroupListRequest(paginator=" + getPaginator() + ", deptId=" + getDeptId() + ", deviceSn=" + getDeviceSn() + ", groupId=" + getGroupId() + ", deptIds=" + getDeptIds() + ", memberIds=" + getMemberIds() + ", type=" + getType() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public Integer getType() {
        return this.type;
    }
}
