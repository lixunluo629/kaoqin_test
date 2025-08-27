package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_upgrade_device_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/UpgradeDeviceRecord.class */
public class UpgradeDeviceRecord {

    @Id
    @GeneratedValue
    @Column(name = "upgrade_record_id")
    private Long upgradeRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "upgrade_schedule_id")
    private Long upgradeScheduleId;

    @Column(name = "upgrade_type")
    private Integer upgradeType;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "remark")
    private String remark;

    @Column(name = "upgrade_status")
    private Integer upgradeStatus;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getUpgradeRecordId() {
        return this.upgradeRecordId;
    }

    public void setUpgradeRecordId(Long upgradeRecordId) {
        this.upgradeRecordId = upgradeRecordId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    public Integer getUpgradeType() {
        return this.upgradeType;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
