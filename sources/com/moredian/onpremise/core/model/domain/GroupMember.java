package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_group_member")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/GroupMember.class */
public class GroupMember {

    @Id
    @GeneratedValue
    @Column(name = "group_member_id")
    private Long groupMemberId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "group_id")
    private Long groupId;
    private Integer type;

    @Column(name = "confirm_flag")
    private Integer confirmFlag;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getGroupMemberId() {
        return this.groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupMember that = (GroupMember) o;
        return Objects.equals(this.orgId, that.orgId) && Objects.equals(this.groupId, that.groupId) && Objects.equals(this.type, that.type) && (Objects.equals(this.deptId, that.deptId) || Objects.equals(this.memberId, that.memberId)) && Objects.equals(this.deleteOrNot, that.deleteOrNot);
    }

    public int hashCode() {
        return Objects.hash(this.orgId, this.groupId, this.type, this.deleteOrNot);
    }

    public Integer getConfirmFlag() {
        return this.confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}
