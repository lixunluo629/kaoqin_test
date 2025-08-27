package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_verify_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/VerifyRecord.class */
public class VerifyRecord {

    @Id
    @GeneratedValue
    @Column(name = "verify_record_id")
    private Long verifyRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "snap_face_url")
    private String snapFaceUrl;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "job_num")
    private String jobNum;

    @Column(name = "record_type")
    private Integer recordType;

    @Column(name = "verify_date")
    private Integer verifyDate;

    @Column(name = "verify_timestamp")
    private Long verifyTimestamp;

    @Column(name = "verify_time")
    private Date verifyTime;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "verify_score")
    private Integer verifyScore;

    @Column(name = "verify_result")
    private Integer verifyResult;

    @Column(name = "second_member_id")
    private Long secondMemberId;

    @Column(name = "second_verify_score")
    private Integer secondVerifyScore;

    @Column(name = "verify_type")
    private Integer verifyType;

    @Column(name = "mirror_verify_score")
    private Integer mirrorVerifyScore;

    @Column(name = "app_type")
    private Integer appType;

    public Long getVerifyRecordId() {
        return this.verifyRecordId;
    }

    public void setVerifyRecordId(Long verifyRecordId) {
        this.verifyRecordId = verifyRecordId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getVerifyDate() {
        return this.verifyDate;
    }

    public void setVerifyDate(Integer verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Long getVerifyTimestamp() {
        return this.verifyTimestamp;
    }

    public void setVerifyTimestamp(Long verifyTimestamp) {
        this.verifyTimestamp = verifyTimestamp;
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

    public Date getVerifyTime() {
        return this.verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public Integer getVerifyResult() {
        return this.verifyResult;
    }

    public void setVerifyResult(Integer verifyResult) {
        this.verifyResult = verifyResult;
    }

    public Long getSecondMemberId() {
        return this.secondMemberId;
    }

    public void setSecondMemberId(Long secondMemberId) {
        this.secondMemberId = secondMemberId;
    }

    public Integer getSecondVerifyScore() {
        return this.secondVerifyScore;
    }

    public void setSecondVerifyScore(Integer secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public Integer getVerifyType() {
        return this.verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public Integer getMirrorVerifyScore() {
        return this.mirrorVerifyScore;
    }

    public void setMirrorVerifyScore(Integer mirrorVerifyScore) {
        this.mirrorVerifyScore = mirrorVerifyScore;
    }

    public Integer getAppType() {
        return this.appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }
}
