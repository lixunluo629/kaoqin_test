package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "批量修改成员部门响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/BatchUpdateMemberDeptResponse.class */
public class BatchUpdateMemberDeptResponse implements Serializable {

    @ApiModelProperty(name = "memberIds", value = "修改失败的成员id集合")
    private List<Long> memberIds;

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchUpdateMemberDeptResponse)) {
            return false;
        }
        BatchUpdateMemberDeptResponse other = (BatchUpdateMemberDeptResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        return this$memberIds == null ? other$memberIds == null : this$memberIds.equals(other$memberIds);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatchUpdateMemberDeptResponse;
    }

    public int hashCode() {
        Object $memberIds = getMemberIds();
        int result = (1 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        return result;
    }

    public String toString() {
        return "BatchUpdateMemberDeptResponse(memberIds=" + getMemberIds() + ")";
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }
}
