package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceRecord.class */
public class AttendanceRecord {

    @Id
    @GeneratedValue
    @Column(name = "attendance_record_id")
    private Long attendanceRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "attendance_group_id")
    private Long attendanceGroupId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_job_num")
    private String memberJobNum;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "record_type")
    private Integer recordType;

    @Column(name = "attendance_picture_url")
    private String attendancePictureUrl;

    @Column(name = "attendance_day")
    private Integer attendanceDay;

    @Column(name = "attendance_time")
    private Long attendanceTime;

    @Column(name = "work_time")
    private Long workTime;

    @Column(name = "rule_time")
    private Long ruleTime;

    @Column(name = "attendance_result")
    private Integer attendanceResult;

    @Column(name = "record_status")
    private Integer recordStatus;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAttendanceRecordId() {
        return this.attendanceRecordId;
    }

    public void setAttendanceRecordId(Long attendanceRecordId) {
        this.attendanceRecordId = attendanceRecordId;
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

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getAttendancePictureUrl() {
        return this.attendancePictureUrl;
    }

    public void setAttendancePictureUrl(String attendancePictureUrl) {
        this.attendancePictureUrl = attendancePictureUrl;
    }

    public Integer getAttendanceDay() {
        return this.attendanceDay;
    }

    public void setAttendanceDay(Integer attendanceDay) {
        this.attendanceDay = attendanceDay;
    }

    public Long getAttendanceTime() {
        return this.attendanceTime;
    }

    public void setAttendanceTime(Long attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public Long getWorkTime() {
        return this.workTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public Long getRuleTime() {
        return this.ruleTime;
    }

    public void setRuleTime(Long ruleTime) {
        this.ruleTime = ruleTime;
    }

    public Integer getAttendanceResult() {
        return this.attendanceResult;
    }

    public void setAttendanceResult(Integer attendanceResult) {
        this.attendanceResult = attendanceResult;
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

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public Integer getRecordStatus() {
        return this.recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
