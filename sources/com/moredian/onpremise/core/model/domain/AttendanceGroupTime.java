package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_group_time")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceGroupTime.class */
public class AttendanceGroupTime {

    @Id
    @GeneratedValue
    @Column(name = "attendance_group_time_id")
    private Long attendanceGroupTimeId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "attendance_group_id")
    private Long attendanceGroupId;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "time_scope")
    private String timeScope;

    @Column(name = "attendance_begin_time")
    private String attendanceBeginTime;

    @Column(name = "attendance_begin_before")
    private Integer attendanceBeginBefore;

    @Column(name = "attendance_begin_after")
    private Integer attendanceBeginAfter;

    @Column(name = "attendance_end_time")
    private String attendanceEndTime;

    @Column(name = "attendance_end_after")
    private Integer attendanceEndAfter;

    @Column(name = "attendance_end_before")
    private Integer attendanceEndBefore;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAttendanceGroupTimeId() {
        return this.attendanceGroupTimeId;
    }

    public void setAttendanceGroupTimeId(Long attendanceGroupTimeId) {
        this.attendanceGroupTimeId = attendanceGroupTimeId;
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

    public String getTimeScope() {
        return this.timeScope;
    }

    public void setTimeScope(String timeScope) {
        this.timeScope = timeScope;
    }

    public Integer getAttendanceBeginBefore() {
        return this.attendanceBeginBefore;
    }

    public void setAttendanceBeginBefore(Integer attendanceBeginBefore) {
        this.attendanceBeginBefore = attendanceBeginBefore;
    }

    public Integer getAttendanceBeginAfter() {
        return this.attendanceBeginAfter;
    }

    public void setAttendanceBeginAfter(Integer attendanceBeginAfter) {
        this.attendanceBeginAfter = attendanceBeginAfter;
    }

    public Integer getAttendanceEndAfter() {
        return this.attendanceEndAfter;
    }

    public void setAttendanceEndAfter(Integer attendanceEndAfter) {
        this.attendanceEndAfter = attendanceEndAfter;
    }

    public Integer getAttendanceEndBefore() {
        return this.attendanceEndBefore;
    }

    public void setAttendanceEndBefore(Integer attendanceEndBefore) {
        this.attendanceEndBefore = attendanceEndBefore;
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

    public String getAttendanceBeginTime() {
        return this.attendanceBeginTime;
    }

    public void setAttendanceBeginTime(String attendanceBeginTime) {
        this.attendanceBeginTime = attendanceBeginTime;
    }

    public String getAttendanceEndTime() {
        return this.attendanceEndTime;
    }

    public void setAttendanceEndTime(String attendanceEndTime) {
        this.attendanceEndTime = attendanceEndTime;
    }
}
