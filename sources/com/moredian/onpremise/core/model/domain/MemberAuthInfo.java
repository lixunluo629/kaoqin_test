package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_member_auth_info")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MemberAuthInfo.class */
public class MemberAuthInfo {

    @Id
    @GeneratedValue
    @Column(name = "member_auth_info_id")
    private Long memberAuthInfoId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "last_sync_device_sn")
    private String lastSyncDeviceSn;

    @Column(name = "newest_device_sn")
    private String newestDeviceSn;

    @Column(name = "permissions_group_id")
    private String permissionsGroupId;

    @Column(name = "attendance_group_id")
    private String attendanceGroupId;

    @Column(name = "check_in_group_id")
    private String checkInGroupId;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMemberAuthInfoId() {
        return this.memberAuthInfoId;
    }

    public void setMemberAuthInfoId(Long memberAuthInfoId) {
        this.memberAuthInfoId = memberAuthInfoId;
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

    public String getPermissionsGroupId() {
        return this.permissionsGroupId;
    }

    public void setPermissionsGroupId(String permissionsGroupId) {
        this.permissionsGroupId = permissionsGroupId;
    }

    public String getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(String attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public String getCheckInGroupId() {
        return this.checkInGroupId;
    }

    public void setCheckInGroupId(String checkInGroupId) {
        this.checkInGroupId = checkInGroupId;
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

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getLastSyncDeviceSn() {
        return this.lastSyncDeviceSn;
    }

    public void setLastSyncDeviceSn(String lastSyncDeviceSn) {
        this.lastSyncDeviceSn = lastSyncDeviceSn;
    }

    public String getNewestDeviceSn() {
        return this.newestDeviceSn;
    }

    public void setNewestDeviceSn(String newestDeviceSn) {
        this.newestDeviceSn = newestDeviceSn;
    }
}
