package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_upgrade_device_schedule")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/UpgradeDeviceSchedule.class */
public class UpgradeDeviceSchedule {

    @Id
    @GeneratedValue
    @Column(name = "upgrade_schedule_id")
    private Long upgradeScheduleId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "upgrade_device_num")
    private Integer upgradeDeviceNum;

    @Column(name = "upgrade_type")
    private Integer upgradeType;

    @Column(name = "upgrade_time")
    private Date upgradeTime;

    @Column(name = "upgrade_status")
    private Integer upgradeStatus;

    @Column(name = "upgrade_version")
    private String upgradeVersion;

    @Column(name = "upgrade_package_url")
    private String upgradePackageUrl;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getUpgradeDeviceNum() {
        return this.upgradeDeviceNum;
    }

    public void setUpgradeDeviceNum(Integer upgradeDeviceNum) {
        this.upgradeDeviceNum = upgradeDeviceNum;
    }

    public Integer getUpgradeType() {
        return this.upgradeType;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public Date getUpgradeTime() {
        return this.upgradeTime;
    }

    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public String getUpgradeVersion() {
        return this.upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public String getUpgradePackageUrl() {
        return this.upgradePackageUrl;
    }

    public void setUpgradePackageUrl(String upgradePackageUrl) {
        this.upgradePackageUrl = upgradePackageUrl;
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
