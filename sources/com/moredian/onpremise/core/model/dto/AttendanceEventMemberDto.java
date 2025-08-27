package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件关联成员")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceEventMemberDto.class */
public class AttendanceEventMemberDto implements Serializable {

    @ApiModelProperty(name = "id", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "name", value = "成员名称")
    private String memberName;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventMemberDto)) {
            return false;
        }
        AttendanceEventMemberDto other = (AttendanceEventMemberDto) o;
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
        return this$memberName == null ? other$memberName == null : this$memberName.equals(other$memberName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventMemberDto;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        return (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
    }

    public String toString() {
        return "AttendanceEventMemberDto(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }
}
