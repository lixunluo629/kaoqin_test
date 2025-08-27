package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_holiday_group")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceHolidayGroup.class */
public class AttendanceHolidayGroup {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "attendance_holiday_id")
    private Long attendanceHolidayId;

    @Column(name = "attendance_group_id")
    private Long attendanceGroupId;

    @Column(name = "delete_flag")
    private Integer deleteFlag;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

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

    public Long getAttendanceHolidayId() {
        return this.attendanceHolidayId;
    }

    public void setAttendanceHolidayId(Long attendanceHolidayId) {
        this.attendanceHolidayId = attendanceHolidayId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
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
}
