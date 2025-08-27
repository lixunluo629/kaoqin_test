package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_upgrade_server_schedule")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/UpgradeServerSchedule.class */
public class UpgradeServerSchedule {

    @Id
    @GeneratedValue
    @Column(name = "upgrade_server_schedule_id")
    private Long upgradeServerScheduleId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "upgrade_package_name")
    private String upgradePackageName;

    @Column(name = "upgrade_version")
    private String upgradeVersion;

    @Column(name = "upgrade_release_time")
    private String upgradeReleaseTime;

    @Column(name = "upgrade_time")
    private Date upgradeTime;

    @Column(name = "current_version_flag")
    private Integer currentVersionFlag;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getUpgradeServerScheduleId() {
        return this.upgradeServerScheduleId;
    }

    public void setUpgradeServerScheduleId(Long upgradeServerScheduleId) {
        this.upgradeServerScheduleId = upgradeServerScheduleId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUpgradePackageName() {
        return this.upgradePackageName;
    }

    public void setUpgradePackageName(String upgradePackageName) {
        this.upgradePackageName = upgradePackageName;
    }

    public String getUpgradeVersion() {
        return this.upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public String getUpgradeReleaseTime() {
        return this.upgradeReleaseTime;
    }

    public void setUpgradeReleaseTime(String upgradeReleaseTime) {
        this.upgradeReleaseTime = upgradeReleaseTime;
    }

    public Date getUpgradeTime() {
        return this.upgradeTime;
    }

    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public Integer getCurrentVersionFlag() {
        return this.currentVersionFlag;
    }

    public void setCurrentVersionFlag(Integer currentVersionFlag) {
        this.currentVersionFlag = currentVersionFlag;
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
