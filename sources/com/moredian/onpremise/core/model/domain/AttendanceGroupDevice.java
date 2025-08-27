package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_group_device")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceGroupDevice.class */
public class AttendanceGroupDevice {

    @Id
    @GeneratedValue
    @Column(name = "attendance_group_device_id")
    private Long attendanceGroupDeviceId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "attendance_group_id")
    private Long attendanceGroupId;

    @Column(name = "device_type")
    private Integer deviceType;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAttendanceGroupDeviceId() {
        return this.attendanceGroupDeviceId;
    }

    public void setAttendanceGroupDeviceId(Long attendanceGroupDeviceId) {
        this.attendanceGroupDeviceId = attendanceGroupDeviceId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
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
