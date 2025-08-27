package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_member_check_in_task_sync")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MemberCheckInTaskSync.class */
public class MemberCheckInTaskSync {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "last_sync_device_sns")
    private String lastSyncDeviceSns;

    @Column(name = "newest_device_sns")
    private String newestDeviceSns;

    @Column(name = "check_in_task_ids")
    private String checkInTaskIds;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getLastSyncDeviceSns() {
        return this.lastSyncDeviceSns;
    }

    public void setLastSyncDeviceSns(String lastSyncDeviceSns) {
        this.lastSyncDeviceSns = lastSyncDeviceSns;
    }

    public String getNewestDeviceSns() {
        return this.newestDeviceSns;
    }

    public void setNewestDeviceSns(String newestDeviceSns) {
        this.newestDeviceSns = newestDeviceSns;
    }

    public String getCheckInTaskIds() {
        return this.checkInTaskIds;
    }

    public void setCheckInTaskIds(String checkInTaskIds) {
        this.checkInTaskIds = checkInTaskIds;
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
}
