package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步成员响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncMemberDetailV2Response.class */
public class TerminalSyncMemberDetailV2Response implements Serializable {
    private static final long serialVersionUID = 3730936916334846899L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long id;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private String deptId;

    @ApiModelProperty(name = "verifyFaceUrl", value = "用户底库照片url")
    private String url;

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalue;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String name;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String jobNum;

    @ApiModelProperty(name = "memberGender", value = "性别：1-男，2-女，3-未选中")
    private Integer gender;

    @ApiModelProperty(name = "memberCardNum", value = "卡号")
    private String cardNum;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEigenvalue(String eigenvalue) {
        this.eigenvalue = eigenvalue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncMemberDetailV2Response)) {
            return false;
        }
        TerminalSyncMemberDetailV2Response other = (TerminalSyncMemberDetailV2Response) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
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
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        if (this$url == null) {
            if (other$url != null) {
                return false;
            }
        } else if (!this$url.equals(other$url)) {
            return false;
        }
        Object this$eigenvalue = getEigenvalue();
        Object other$eigenvalue = other.getEigenvalue();
        if (this$eigenvalue == null) {
            if (other$eigenvalue != null) {
                return false;
            }
        } else if (!this$eigenvalue.equals(other$eigenvalue)) {
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
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
            return false;
        }
        Object this$gender = getGender();
        Object other$gender = other.getGender();
        if (this$gender == null) {
            if (other$gender != null) {
                return false;
            }
        } else if (!this$gender.equals(other$gender)) {
            return false;
        }
        Object this$cardNum = getCardNum();
        Object other$cardNum = other.getCardNum();
        if (this$cardNum == null) {
            if (other$cardNum != null) {
                return false;
            }
        } else if (!this$cardNum.equals(other$cardNum)) {
            return false;
        }
        Object this$remark = getRemark();
        Object other$remark = other.getRemark();
        return this$remark == null ? other$remark == null : this$remark.equals(other$remark);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncMemberDetailV2Response;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $url = getUrl();
        int result3 = (result2 * 59) + ($url == null ? 43 : $url.hashCode());
        Object $eigenvalue = getEigenvalue();
        int result4 = (result3 * 59) + ($eigenvalue == null ? 43 : $eigenvalue.hashCode());
        Object $name = getName();
        int result5 = (result4 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $jobNum = getJobNum();
        int result6 = (result5 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $gender = getGender();
        int result7 = (result6 * 59) + ($gender == null ? 43 : $gender.hashCode());
        Object $cardNum = getCardNum();
        int result8 = (result7 * 59) + ($cardNum == null ? 43 : $cardNum.hashCode());
        Object $remark = getRemark();
        return (result8 * 59) + ($remark == null ? 43 : $remark.hashCode());
    }

    public String toString() {
        return "TerminalSyncMemberDetailV2Response(id=" + getId() + ", deptId=" + getDeptId() + ", url=" + getUrl() + ", eigenvalue=" + getEigenvalue() + ", name=" + getName() + ", jobNum=" + getJobNum() + ", gender=" + getGender() + ", cardNum=" + getCardNum() + ", remark=" + getRemark() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getUrl() {
        return this.url;
    }

    public String getEigenvalue() {
        return this.eigenvalue;
    }

    public String getName() {
        return this.name;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Integer getGender() {
        return this.gender;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public String getRemark() {
        return this.remark;
    }
}
