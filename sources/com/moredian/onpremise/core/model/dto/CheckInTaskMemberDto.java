package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "保存签到任务部门成员")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/CheckInTaskMemberDto.class */
public class CheckInTaskMemberDto implements Serializable {
    private static final long serialVersionUID = -8138989829593420339L;

    @ApiModelProperty(name = "type", value = "类型，1：部门；2：成员")
    private Integer type;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "confirmFlag", value = "是否用户提交", hidden = true)
    private Integer confirmFlag;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String toString() {
        return "CheckInTaskMemberDto(type=" + getType() + ", deptId=" + getDeptId() + ", memberId=" + getMemberId() + ", confirmFlag=" + getConfirmFlag() + ")";
    }

    public Integer getType() {
        return this.type;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getMemberId() {
        return this.memberId;
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
        CheckInTaskMemberDto dto = (CheckInTaskMemberDto) o;
        return this.type.intValue() == MemberTypeEnum.DEPT.getValue() ? Objects.equals(this.deptId, dto.deptId) && Objects.equals(this.type, dto.type) && Objects.equals(this.confirmFlag, dto.confirmFlag) : Objects.equals(this.memberId, dto.memberId) && Objects.equals(this.type, dto.type) && Objects.equals(this.confirmFlag, dto.confirmFlag);
    }

    public int hashCode() {
        return Objects.hash(this.memberId, this.deptId, this.type, this.confirmFlag);
    }
}
