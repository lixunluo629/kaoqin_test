package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "终端同步通讯录及特征值响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncMemberResponse.class */
public class TerminalSyncMemberResponse implements Serializable {

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

    @ApiModelProperty(name = "gmtModify", value = "修改时间", hidden = true)
    private Date gmtModify;

    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

    @ApiModelProperty(name = "permissionsGroupId", value = "权限组id，逗号分隔")
    private String permissionsGroupId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id，逗号分隔")
    private String attendanceGroupId;

    @ApiModelProperty(name = "checkInGroupId", value = "签到组id，逗号分隔")
    private String checkInGroupId;

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id，逗号分隔")
    private String mealCanteenId;

    @ApiModelProperty(name = "lastSyncDeviceSn", value = "最后一次同步时成员关联设备sn列表，逗号分隔", hidden = true)
    private String lastSyncDeviceSn;

    @ApiModelProperty(name = "newestDeviceSn", value = "最新成员关联设备列表，逗号分隔", hidden = true)
    private String newestDeviceSn;

    @ApiModelProperty(name = "memberGender", value = "性别：1-男，2-女，3-未选中")
    private Integer memberGender;

    @ApiModelProperty(name = "verifyFaceUrl", value = "用户底库照片url")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "memberCardNum", value = "卡号")
    private String memberCardNum;

    @ApiModelProperty(name = "identityCard", value = "证件号")
    private String identityCard;

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

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setPermissionsGroupId(String permissionsGroupId) {
        this.permissionsGroupId = permissionsGroupId;
    }

    public void setAttendanceGroupId(String attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setCheckInGroupId(String checkInGroupId) {
        this.checkInGroupId = checkInGroupId;
    }

    public void setMealCanteenId(String mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setLastSyncDeviceSn(String lastSyncDeviceSn) {
        this.lastSyncDeviceSn = lastSyncDeviceSn;
    }

    public void setNewestDeviceSn(String newestDeviceSn) {
        this.newestDeviceSn = newestDeviceSn;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setMemberCardNum(String memberCardNum) {
        this.memberCardNum = memberCardNum;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncMemberResponse)) {
            return false;
        }
        TerminalSyncMemberResponse other = (TerminalSyncMemberResponse) o;
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
        Object this$gmtModify = getGmtModify();
        Object other$gmtModify = other.getGmtModify();
        if (this$gmtModify == null) {
            if (other$gmtModify != null) {
                return false;
            }
        } else if (!this$gmtModify.equals(other$gmtModify)) {
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
        Object this$permissionsGroupId = getPermissionsGroupId();
        Object other$permissionsGroupId = other.getPermissionsGroupId();
        if (this$permissionsGroupId == null) {
            if (other$permissionsGroupId != null) {
                return false;
            }
        } else if (!this$permissionsGroupId.equals(other$permissionsGroupId)) {
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
        Object this$checkInGroupId = getCheckInGroupId();
        Object other$checkInGroupId = other.getCheckInGroupId();
        if (this$checkInGroupId == null) {
            if (other$checkInGroupId != null) {
                return false;
            }
        } else if (!this$checkInGroupId.equals(other$checkInGroupId)) {
            return false;
        }
        Object this$mealCanteenId = getMealCanteenId();
        Object other$mealCanteenId = other.getMealCanteenId();
        if (this$mealCanteenId == null) {
            if (other$mealCanteenId != null) {
                return false;
            }
        } else if (!this$mealCanteenId.equals(other$mealCanteenId)) {
            return false;
        }
        Object this$lastSyncDeviceSn = getLastSyncDeviceSn();
        Object other$lastSyncDeviceSn = other.getLastSyncDeviceSn();
        if (this$lastSyncDeviceSn == null) {
            if (other$lastSyncDeviceSn != null) {
                return false;
            }
        } else if (!this$lastSyncDeviceSn.equals(other$lastSyncDeviceSn)) {
            return false;
        }
        Object this$newestDeviceSn = getNewestDeviceSn();
        Object other$newestDeviceSn = other.getNewestDeviceSn();
        if (this$newestDeviceSn == null) {
            if (other$newestDeviceSn != null) {
                return false;
            }
        } else if (!this$newestDeviceSn.equals(other$newestDeviceSn)) {
            return false;
        }
        Object this$memberGender = getMemberGender();
        Object other$memberGender = other.getMemberGender();
        if (this$memberGender == null) {
            if (other$memberGender != null) {
                return false;
            }
        } else if (!this$memberGender.equals(other$memberGender)) {
            return false;
        }
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        if (this$remark == null) {
            if (other$remark != null) {
                return false;
            }
        } else if (!this$remark.equals(other$remark)) {
            return false;
        }
        Object this$memberCardNum = getMemberCardNum();
        Object other$memberCardNum = other.getMemberCardNum();
        if (this$memberCardNum == null) {
            if (other$memberCardNum != null) {
                return false;
            }
        } else if (!this$memberCardNum.equals(other$memberCardNum)) {
            return false;
        }
        Object this$identityCard = getIdentityCard();
        Object other$identityCard = other.getIdentityCard();
        return this$identityCard == null ? other$identityCard == null : this$identityCard.equals(other$identityCard);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncMemberResponse;
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
        Object $gmtModify = getGmtModify();
        int result7 = (result6 * 59) + ($gmtModify == null ? 43 : $gmtModify.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result8 = (result7 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $permissionsGroupId = getPermissionsGroupId();
        int result9 = (result8 * 59) + ($permissionsGroupId == null ? 43 : $permissionsGroupId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result10 = (result9 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $checkInGroupId = getCheckInGroupId();
        int result11 = (result10 * 59) + ($checkInGroupId == null ? 43 : $checkInGroupId.hashCode());
        Object $mealCanteenId = getMealCanteenId();
        int result12 = (result11 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $lastSyncDeviceSn = getLastSyncDeviceSn();
        int result13 = (result12 * 59) + ($lastSyncDeviceSn == null ? 43 : $lastSyncDeviceSn.hashCode());
        Object $newestDeviceSn = getNewestDeviceSn();
        int result14 = (result13 * 59) + ($newestDeviceSn == null ? 43 : $newestDeviceSn.hashCode());
        Object $memberGender = getMemberGender();
        int result15 = (result14 * 59) + ($memberGender == null ? 43 : $memberGender.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result16 = (result15 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $deptName = getDeptName();
        int result17 = (result16 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $remark = getRemark();
        int result18 = (result17 * 59) + ($remark == null ? 43 : $remark.hashCode());
        Object $memberCardNum = getMemberCardNum();
        int result19 = (result18 * 59) + ($memberCardNum == null ? 43 : $memberCardNum.hashCode());
        Object $identityCard = getIdentityCard();
        return (result19 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
    }

    public String toString() {
        return "TerminalSyncMemberResponse(eigenvalueValue=" + getEigenvalueValue() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptId=" + getDeptId() + ", deleteOrNot=" + getDeleteOrNot() + ", gmtModify=" + getGmtModify() + ", gmtCreate=" + getGmtCreate() + ", permissionsGroupId=" + getPermissionsGroupId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", checkInGroupId=" + getCheckInGroupId() + ", mealCanteenId=" + getMealCanteenId() + ", lastSyncDeviceSn=" + getLastSyncDeviceSn() + ", newestDeviceSn=" + getNewestDeviceSn() + ", memberGender=" + getMemberGender() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", deptName=" + getDeptName() + ", remark=" + getRemark() + ", memberCardNum=" + getMemberCardNum() + ", identityCard=" + getIdentityCard() + ")";
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

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public String getPermissionsGroupId() {
        return this.permissionsGroupId;
    }

    public String getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getCheckInGroupId() {
        return this.checkInGroupId;
    }

    public String getMealCanteenId() {
        return this.mealCanteenId;
    }

    public String getLastSyncDeviceSn() {
        return this.lastSyncDeviceSn;
    }

    public String getNewestDeviceSn() {
        return this.newestDeviceSn;
    }

    public Integer getMemberGender() {
        return this.memberGender;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getMemberCardNum() {
        return this.memberCardNum;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }
}
