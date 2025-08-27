package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.utils.AssertUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "人员信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/MemberDto.class */
public class MemberDto implements Serializable {
    private static final long serialVersionUID = -1406559570155437661L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId = 0L;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private String deptId;

    @ApiModelProperty(name = "type", value = "成员类型：1-部门，2-成员")
    private Integer type;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String toString() {
        return "MemberDto(memberId=" + getMemberId() + ", deptId=" + getDeptId() + ", type=" + getType() + ", deptName=" + getDeptName() + ", memberName=" + getMemberName() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public Integer getType() {
        return this.type;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberDto dto = (MemberDto) o;
        return this.type.intValue() == MemberTypeEnum.DEPT.getValue() ? Objects.equals(this.deptId, dto.deptId) && Objects.equals(this.type, dto.type) : Objects.equals(this.memberId, dto.memberId) && Objects.equals(this.type, dto.type);
    }

    public int hashCode() {
        return Objects.hash(this.memberId, this.deptId, this.type);
    }

    public void checkParam() {
        AssertUtil.isNullOrEmpty(getType(), OnpremiseErrorEnum.MEMBER_TYPE_ERROR);
        MemberTypeEnum memberTypeEnum = MemberTypeEnum.getByValue(getType().intValue());
        AssertUtil.isNullOrEmpty(memberTypeEnum, OnpremiseErrorEnum.MEMBER_TYPE_ERROR);
        switch (memberTypeEnum) {
            case DEPT:
                AssertUtil.isNullOrEmpty(getDeptId(), OnpremiseErrorEnum.DEPT_ID_MUST_NOT_NULL);
                break;
            case MEMBER:
                AssertUtil.checkId(getMemberId(), OnpremiseErrorEnum.MEMBER_ID_MUST_NOT_NULL);
                break;
        }
    }
}
