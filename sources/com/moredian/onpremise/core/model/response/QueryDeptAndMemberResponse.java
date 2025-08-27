package com.moredian.onpremise.core.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "按名称查询部门或成员响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryDeptAndMemberResponse.class */
public class QueryDeptAndMemberResponse implements Serializable {

    @ApiModelProperty(name = "name", value = "根据类型：部门时为部门名称，成员时Wie成员名称")
    private String name;

    @ApiModelProperty(name = "id", value = "部门时为部门id，成员时则为成员id")
    private Long id;

    @ApiModelProperty(name = "type", value = "类型：1-部门，2-成员")
    private Integer type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryDeptAndMemberResponse)) {
            return false;
        }
        QueryDeptAndMemberResponse other = (QueryDeptAndMemberResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        return this$memberJobNum == null ? other$memberJobNum == null : this$memberJobNum.equals(other$memberJobNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryDeptAndMemberResponse;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $memberJobNum = getMemberJobNum();
        return (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
    }

    public String toString() {
        return "QueryDeptAndMemberResponse(name=" + getName() + ", id=" + getId() + ", type=" + getType() + ", memberJobNum=" + getMemberJobNum() + ")";
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public Integer getType() {
        return this.type;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }
}
