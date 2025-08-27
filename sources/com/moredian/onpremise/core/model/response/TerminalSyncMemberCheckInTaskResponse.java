package com.moredian.onpremise.core.model.response;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "终端同步签到人员底库响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncMemberCheckInTaskResponse.class */
public class TerminalSyncMemberCheckInTaskResponse implements Serializable {

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalueValue;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private String deptId;

    @ApiModelProperty(name = "deleteOrNot", value = "是否删除：1-删除，0-保留", hidden = true)
    private Integer deleteOrNot;

    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

    @ApiModelProperty(name = "gmtModify", value = "更新时间", hidden = true)
    private Date gmtModify;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员；4-非管理员")
    private Integer accountGrade;

    @ApiModelProperty(name = "accountName", value = "账号名称")
    private String accountName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "账号密码")
    private String password;

    @ApiModelProperty(name = "checkInTaskIds", value = "签到组id，逗号分隔")
    private String checkInTaskIds;

    @ApiModelProperty(name = "lastSyncDeviceSns", value = "最后一次同步时成员关联设备sn列表，逗号分隔", hidden = true)
    private String lastSyncDeviceSns;

    @ApiModelProperty(name = "newestDeviceSns", value = "最新成员关联设备列表，逗号分隔", hidden = true)
    private String newestDeviceSns;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

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

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCheckInTaskIds(String checkInTaskIds) {
        this.checkInTaskIds = checkInTaskIds;
    }

    public void setLastSyncDeviceSns(String lastSyncDeviceSns) {
        this.lastSyncDeviceSns = lastSyncDeviceSns;
    }

    public void setNewestDeviceSns(String newestDeviceSns) {
        this.newestDeviceSns = newestDeviceSns;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncMemberCheckInTaskResponse)) {
            return false;
        }
        TerminalSyncMemberCheckInTaskResponse other = (TerminalSyncMemberCheckInTaskResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        if (this$eigenvalueValue == null) {
            if (other$eigenvalueValue != null) {
                return false;
            }
        } else if (!this$eigenvalueValue.equals(other$eigenvalueValue)) {
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
        Object this$deleteOrNot = getDeleteOrNot();
        Object other$deleteOrNot = other.getDeleteOrNot();
        if (this$deleteOrNot == null) {
            if (other$deleteOrNot != null) {
                return false;
            }
        } else if (!this$deleteOrNot.equals(other$deleteOrNot)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null) {
            if (other$gmtCreate != null) {
                return false;
            }
        } else if (!this$gmtCreate.equals(other$gmtCreate)) {
            return false;
        }
        Object this$gmtModify = getGmtModify();
        Object other$gmtModify = other.getGmtModify();
        if (this$gmtModify == null) {
            if (other$gmtModify != null) {
                return false;
            }
        } else if (!this$gmtModify.equals(other$gmtModify)) {
            return false;
        }
        Object this$accountGrade = getAccountGrade();
        Object other$accountGrade = other.getAccountGrade();
        if (this$accountGrade == null) {
            if (other$accountGrade != null) {
                return false;
            }
        } else if (!this$accountGrade.equals(other$accountGrade)) {
            return false;
        }
        Object this$accountName = getAccountName();
        Object other$accountName = other.getAccountName();
        if (this$accountName == null) {
            if (other$accountName != null) {
                return false;
            }
        } else if (!this$accountName.equals(other$accountName)) {
            return false;
        }
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password != null) {
                return false;
            }
        } else if (!this$password.equals(other$password)) {
            return false;
        }
        Object this$checkInTaskIds = getCheckInTaskIds();
        Object other$checkInTaskIds = other.getCheckInTaskIds();
        if (this$checkInTaskIds == null) {
            if (other$checkInTaskIds != null) {
                return false;
            }
        } else if (!this$checkInTaskIds.equals(other$checkInTaskIds)) {
            return false;
        }
        Object this$lastSyncDeviceSns = getLastSyncDeviceSns();
        Object other$lastSyncDeviceSns = other.getLastSyncDeviceSns();
        if (this$lastSyncDeviceSns == null) {
            if (other$lastSyncDeviceSns != null) {
                return false;
            }
        } else if (!this$lastSyncDeviceSns.equals(other$lastSyncDeviceSns)) {
            return false;
        }
        Object this$newestDeviceSns = getNewestDeviceSns();
        Object other$newestDeviceSns = other.getNewestDeviceSns();
        if (this$newestDeviceSns == null) {
            if (other$newestDeviceSns != null) {
                return false;
            }
        } else if (!this$newestDeviceSns.equals(other$newestDeviceSns)) {
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
        Object this$remark = getRemark();
        Object other$remark = other.getRemark();
        return this$remark == null ? other$remark == null : this$remark.equals(other$remark);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncMemberCheckInTaskResponse;
    }

    public int hashCode() {
        Object $eigenvalueValue = getEigenvalueValue();
        int result = (1 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
        Object $memberId = getMemberId();
        int result2 = (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptId = getDeptId();
        int result5 = (result4 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deleteOrNot = getDeleteOrNot();
        int result6 = (result5 * 59) + ($deleteOrNot == null ? 43 : $deleteOrNot.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result7 = (result6 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $gmtModify = getGmtModify();
        int result8 = (result7 * 59) + ($gmtModify == null ? 43 : $gmtModify.hashCode());
        Object $accountGrade = getAccountGrade();
        int result9 = (result8 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $accountName = getAccountName();
        int result10 = (result9 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $password = getPassword();
        int result11 = (result10 * 59) + ($password == null ? 43 : $password.hashCode());
        Object $checkInTaskIds = getCheckInTaskIds();
        int result12 = (result11 * 59) + ($checkInTaskIds == null ? 43 : $checkInTaskIds.hashCode());
        Object $lastSyncDeviceSns = getLastSyncDeviceSns();
        int result13 = (result12 * 59) + ($lastSyncDeviceSns == null ? 43 : $lastSyncDeviceSns.hashCode());
        Object $newestDeviceSns = getNewestDeviceSns();
        int result14 = (result13 * 59) + ($newestDeviceSns == null ? 43 : $newestDeviceSns.hashCode());
        Object $deptName = getDeptName();
        int result15 = (result14 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $remark = getRemark();
        return (result15 * 59) + ($remark == null ? 43 : $remark.hashCode());
    }

    public String toString() {
        return "TerminalSyncMemberCheckInTaskResponse(eigenvalueValue=" + getEigenvalueValue() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptId=" + getDeptId() + ", deleteOrNot=" + getDeleteOrNot() + ", gmtCreate=" + getGmtCreate() + ", gmtModify=" + getGmtModify() + ", accountGrade=" + getAccountGrade() + ", accountName=" + getAccountName() + ", password=" + getPassword() + ", checkInTaskIds=" + getCheckInTaskIds() + ", lastSyncDeviceSns=" + getLastSyncDeviceSns() + ", newestDeviceSns=" + getNewestDeviceSns() + ", deptName=" + getDeptName() + ", remark=" + getRemark() + ")";
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
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

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCheckInTaskIds() {
        return this.checkInTaskIds;
    }

    public String getLastSyncDeviceSns() {
        return this.lastSyncDeviceSns;
    }

    public String getNewestDeviceSns() {
        return this.newestDeviceSns;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getRemark() {
        return this.remark;
    }
}
