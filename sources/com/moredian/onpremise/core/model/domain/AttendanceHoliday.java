package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_holiday")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceHoliday.class */
public class AttendanceHoliday {

    @Id
    @Column(name = "attendance_holiday_id")
    private Long attendanceHolidayId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "holiday_name")
    private String holidayName;

    @Column(name = "attendance_group_id")
    private String attendanceGroupId;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "repay_work_date")
    private String repayWorkDate;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    public Long getAttendanceHolidayId() {
        return this.attendanceHolidayId;
    }

    public void setAttendanceHolidayId(Long attendanceHolidayId) {
        this.attendanceHolidayId = attendanceHolidayId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getHolidayName() {
        return this.holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
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

    public String getRepayWorkDate() {
        return this.repayWorkDate;
    }

    public void setRepayWorkDate(String repayWorkDate) {
        this.repayWorkDate = repayWorkDate;
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

    public String getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(String attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }
}
