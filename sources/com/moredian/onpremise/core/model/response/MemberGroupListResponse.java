package com.moredian.onpremise.core.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

@ApiModel(description = "成员所在权限组列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/MemberGroupListResponse.class */
public class MemberGroupListResponse implements Serializable {
    private static final long serialVersionUID = -3693918281276159565L;

    @JsonIgnore
    @ApiModelProperty(name = "memberId", value = "成员id", hidden = true)
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @JsonIgnore
    @ApiModelProperty(name = "deptId", value = "部门id", hidden = true)
    private String deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "devices", value = "通行设备")
    private List<DeviceDto> devices;

    @ApiModelProperty(name = ConstraintHelper.GROUPS, value = "所在权限组")
    private List<GroupDto> groups;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDevices(List<DeviceDto> devices) {
        this.devices = devices;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberGroupListResponse)) {
            return false;
        }
        MemberGroupListResponse other = (MemberGroupListResponse) o;
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
        Object this$devices = getDevices();
        Object other$devices = other.getDevices();
        if (this$devices == null) {
            if (other$devices != null) {
                return false;
            }
        } else if (!this$devices.equals(other$devices)) {
            return false;
        }
        Object this$groups = getGroups();
        Object other$groups = other.getGroups();
        return this$groups == null ? other$groups == null : this$groups.equals(other$groups);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MemberGroupListResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result3 = (result2 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptId = getDeptId();
        int result4 = (result3 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $devices = getDevices();
        int result6 = (result5 * 59) + ($devices == null ? 43 : $devices.hashCode());
        Object $groups = getGroups();
        return (result6 * 59) + ($groups == null ? 43 : $groups.hashCode());
    }

    public String toString() {
        return "MemberGroupListResponse(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", devices=" + getDevices() + ", groups=" + getGroups() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public List<DeviceDto> getDevices() {
        return this.devices;
    }

    public List<GroupDto> getGroups() {
        return this.groups;
    }
}
