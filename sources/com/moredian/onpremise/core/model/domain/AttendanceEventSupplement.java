package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_supplement")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceEventSupplement.class */
public class AttendanceEventSupplement {

    @Id
    @Column(name = "supplement_id")
    private Long supplementId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "supplement_type")
    private Integer supplementType;

    @Column(name = "supplement_time")
    private String supplementTime;

    @Column(name = "supplement_reason")
    private String supplementReason;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getSupplementId() {
        return this.supplementId;
    }

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
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

    public Integer getSupplementType() {
        return this.supplementType;
    }

    public void setSupplementType(Integer supplementType) {
        this.supplementType = supplementType;
    }

    public String getSupplementTime() {
        return this.supplementTime;
    }

    public void setSupplementTime(String supplementTime) {
        this.supplementTime = supplementTime;
    }

    public String getSupplementReason() {
        return this.supplementReason;
    }

    public void setSupplementReason(String supplementReason) {
        this.supplementReason = supplementReason;
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
