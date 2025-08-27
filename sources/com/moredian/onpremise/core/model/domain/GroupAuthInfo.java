package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_group_auth_info")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/GroupAuthInfo.class */
public class GroupAuthInfo {

    @Id
    @GeneratedValue
    @Column(name = "group_auth_info_id")
    private Long groupAuthInfoId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "auth_scope")
    private String authScope;

    @Column(name = "pass_time")
    private String passTime;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getGroupAuthInfoId() {
        return this.groupAuthInfoId;
    }

    public void setGroupAuthInfoId(Long groupAuthInfoId) {
        this.groupAuthInfoId = groupAuthInfoId;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAuthScope() {
        return this.authScope;
    }

    public void setAuthScope(String authScope) {
        this.authScope = authScope;
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

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPassTime() {
        return this.passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }
}
