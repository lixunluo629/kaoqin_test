package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_update_eigenvalue_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/UpdateEigenvalueRecord.class */
public class UpdateEigenvalueRecord {

    @Id
    @GeneratedValue
    @Column(name = "update_eigenvalue_record_id")
    private Long updateEigenvalueRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "old_verify_face_url")
    private String oldVerifyFaceUrl;

    @Column(name = "new_verify_face_url")
    private String newVerifyFaceUrl;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "new_eigenvalue_value")
    private String newEigenvalueValue;

    @Column(name = "old_eigenvalue_value")
    private String oldEigenvalueValue;

    @Column(name = "channel")
    private Integer channel;

    public Long getUpdateEigenvalueRecordId() {
        return this.updateEigenvalueRecordId;
    }

    public void setUpdateEigenvalueRecordId(Long updateEigenvalueRecordId) {
        this.updateEigenvalueRecordId = updateEigenvalueRecordId;
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

    public String getOldVerifyFaceUrl() {
        return this.oldVerifyFaceUrl;
    }

    public void setOldVerifyFaceUrl(String oldVerifyFaceUrl) {
        this.oldVerifyFaceUrl = oldVerifyFaceUrl;
    }

    public String getNewVerifyFaceUrl() {
        return this.newVerifyFaceUrl;
    }

    public void setNewVerifyFaceUrl(String newVerifyFaceUrl) {
        this.newVerifyFaceUrl = newVerifyFaceUrl;
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

    public String getNewEigenvalueValue() {
        return this.newEigenvalueValue;
    }

    public void setNewEigenvalueValue(String newEigenvalueValue) {
        this.newEigenvalueValue = newEigenvalueValue;
    }

    public String getOldEigenvalueValue() {
        return this.oldEigenvalueValue;
    }

    public void setOldEigenvalueValue(String oldEigenvalueValue) {
        this.oldEigenvalueValue = oldEigenvalueValue;
    }

    public Integer getChannel() {
        return this.channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
