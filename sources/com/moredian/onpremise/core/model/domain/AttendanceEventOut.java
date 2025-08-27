package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_business_out")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceEventOut.class */
public class AttendanceEventOut {

    @Id
    @Column(name = "business_out_id")
    private Long businessOutId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "start_frame")
    private Integer startFrame;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "end_frame")
    private Integer endFrame;

    @Column(name = "out_days")
    private String outDays;

    @Column(name = "out_reason")
    private String outReason;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getBusinessOutId() {
        return this.businessOutId;
    }

    public void setBusinessOutId(Long businessOutId) {
        this.businessOutId = businessOutId;
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

    public String getOutDays() {
        return this.outDays;
    }

    public void setOutDays(String outDays) {
        this.outDays = outDays;
    }

    public String getOutReason() {
        return this.outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
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
