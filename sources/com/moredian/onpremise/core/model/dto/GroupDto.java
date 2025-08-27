package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "权限组信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/GroupDto.class */
public class GroupDto implements Serializable {
    private static final long serialVersionUID = 6596898203275702915L;

    @ApiModelProperty(name = "groupId", value = "组id")
    private Long groupId;

    @ApiModelProperty(name = "groupName", value = "组名")
    private String groupName;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String toString() {
        return "GroupDto(groupId=" + getGroupId() + ", groupName=" + getGroupName() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupDto that = (GroupDto) o;
        return Objects.equals(this.groupId, that.groupId);
    }

    public int hashCode() {
        return Objects.hash(this.groupId);
    }
}
