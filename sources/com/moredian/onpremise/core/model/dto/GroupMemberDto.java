package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.utils.AssertUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "群组成员关系")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/GroupMemberDto.class */
public class GroupMemberDto implements Serializable {

    @ApiModelProperty(name = "groupMemberId", value = "群组成员关系主键id", hidden = true)
    private Long groupMemberId;

    @ApiModelProperty(name = "groupId", value = "组id", hidden = true)
    private Long groupId;

    @ApiModelProperty(name = "type", value = "组成员类型：1-部门，2-成员")
    private Integer type;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberId", value = "memberId")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "confirmFlag", value = "是否用户提交", hidden = true)
    private Integer confirmFlag;

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String toString() {
        return "GroupMemberDto(groupMemberId=" + getGroupMemberId() + ", groupId=" + getGroupId() + ", type=" + getType() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", confirmFlag=" + getConfirmFlag() + ")";
    }

    public Long getGroupMemberId() {
        return this.groupMemberId;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public Integer getType() {
        return this.type;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
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

    public Integer getConfirmFlag() {
        return this.confirmFlag;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupMemberDto dto = (GroupMemberDto) o;
        return this.type.intValue() == 1 ? Objects.equals(this.deptId, dto.deptId) && Objects.equals(this.confirmFlag, dto.confirmFlag) : Objects.equals(this.memberId, dto.memberId) && Objects.equals(this.confirmFlag, dto.confirmFlag);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.deptId, this.memberId, this.confirmFlag);
    }

    public void checkParam() {
        AssertUtil.isNullOrEmpty(getType(), OnpremiseErrorEnum.MEMBER_TYPE_ERROR);
        MemberTypeEnum memberTypeEnum = MemberTypeEnum.getByValue(getType().intValue());
        AssertUtil.isNullOrEmpty(memberTypeEnum, OnpremiseErrorEnum.MEMBER_TYPE_ERROR);
        switch (memberTypeEnum) {
            case DEPT:
                AssertUtil.checkId(getDeptId(), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
                break;
            case MEMBER:
                AssertUtil.checkId(getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
                break;
        }
    }
}
