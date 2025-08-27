package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_warn_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/WarnRecord.class */
public class WarnRecord {

    @Id
    @GeneratedValue
    @Column(name = "warn_record_id")
    private Long warnRecordId;

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

    @Column(name = "warn_type_code")
    private Integer warnTypeCode;

    @Column(name = "warn_type")
    private String warnType;

    @Column(name = "warn_time")
    private Date warnTime;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getWarnRecordId() {
        return this.warnRecordId;
    }

    public void setWarnRecordId(Long warnRecordId) {
        this.warnRecordId = warnRecordId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public Integer getWarnTypeCode() {
        return this.warnTypeCode;
    }

    public void setWarnTypeCode(Integer warnTypeCode) {
        this.warnTypeCode = warnTypeCode;
    }

    public String getWarnType() {
        return this.warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public Date getWarnTime() {
        return this.warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
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
}
