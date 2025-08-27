package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_leave")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceEventLeave.class */
public class AttendanceEventLeave {

    @Id
    @Column(name = "attendance_leave_id")
    private Long attendanceLeaveId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "leave_type")
    private Integer leaveType;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "start_frame")
    private Integer startFrame;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "end_frame")
    private Integer endFrame;

    @Column(name = "leave_days")
    private String leaveDays;

    @Column(name = "leave_reason")
    private String leaveReason;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAttendanceLeaveId() {
        return this.attendanceLeaveId;
    }

    public void setAttendanceLeaveId(Long attendanceLeaveId) {
        this.attendanceLeaveId = attendanceLeaveId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getStartFrame() {
        return this.startFrame;
    }

    public void setStartFrame(Integer startFrame) {
        this.startFrame = startFrame;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getEndFrame() {
        return this.endFrame;
    }

    public void setEndFrame(Integer endFrame) {
        this.endFrame = endFrame;
    }

    public String getLeaveDays() {
        return this.leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getLeaveReason() {
        return this.leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
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

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
