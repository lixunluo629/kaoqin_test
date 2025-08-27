package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_visit_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/VisitRecord.class */
public class VisitRecord {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "external_contact_id")
    private Long externalContactId;

    @Column(name = "visitor_name")
    private String visitorName;

    @Column(name = "visitor_id_card")
    private String visitorIdCard;

    @Column(name = "visitor_face_url")
    private String visitorFaceUrl;

    @Column(name = "interviewee_member_id")
    private Long intervieweeMemberId;

    @Column(name = "visitor_mobile")
    private String visitorMobile;

    @Column(name = "interviewee_name")
    private String intervieweeName;

    @Column(name = "interviewee_mobile")
    private String intervieweeMobile;

    @Column(name = "interviewee_dept_id")
    private String intervieweeDeptId;

    @Column(name = "interviewee_dept_name")
    private String intervieweeDeptName;

    @Column(name = "sign_date")
    private Integer signDate;

    @Column(name = "sign_time")
    private Long signTime;

    @Column(name = "visitor_expired")
    private Integer visitorExpired;

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

    public Long getExternalContactId() {
        return this.externalContactId;
    }

    public void setExternalContactId(Long externalContactId) {
        this.externalContactId = externalContactId;
    }

    public String getVisitorName() {
        return this.visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorIdCard() {
        return this.visitorIdCard;
    }

    public void setVisitorIdCard(String visitorIdCard) {
        this.visitorIdCard = visitorIdCard;
    }

    public String getVisitorFaceUrl() {
        return this.visitorFaceUrl;
    }

    public void setVisitorFaceUrl(String visitorFaceUrl) {
        this.visitorFaceUrl = visitorFaceUrl;
    }

    public Long getIntervieweeMemberId() {
        return this.intervieweeMemberId;
    }

    public void setIntervieweeMemberId(Long intervieweeMemberId) {
        this.intervieweeMemberId = intervieweeMemberId;
    }

    public String getIntervieweeDeptId() {
        return this.intervieweeDeptId;
    }

    public void setIntervieweeDeptId(String intervieweeDeptId) {
        this.intervieweeDeptId = intervieweeDeptId;
    }

    public String getIntervieweeDeptName() {
        return this.intervieweeDeptName;
    }

    public void setIntervieweeDeptName(String intervieweeDeptName) {
        this.intervieweeDeptName = intervieweeDeptName;
    }

    public String getVisitorMobile() {
        return this.visitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public String getIntervieweeName() {
        return this.intervieweeName;
    }

    public void setIntervieweeName(String intervieweeName) {
        this.intervieweeName = intervieweeName;
    }

    public String getIntervieweeMobile() {
        return this.intervieweeMobile;
    }

    public void setIntervieweeMobile(String intervieweeMobile) {
        this.intervieweeMobile = intervieweeMobile;
    }

    public Integer getSignDate() {
        return this.signDate;
    }

    public void setSignDate(Integer signDate) {
        this.signDate = signDate;
    }

    public Long getSignTime() {
        return this.signTime;
    }

    public void setSignTime(Long signTime) {
        this.signTime = signTime;
    }

    public Integer getVisitorExpired() {
        return this.visitorExpired;
    }

    public void setVisitorExpired(Integer visitorExpired) {
        this.visitorExpired = visitorExpired;
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
