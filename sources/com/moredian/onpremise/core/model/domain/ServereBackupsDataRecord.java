package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_servere_backups_data_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/ServereBackupsDataRecord.class */
public class ServereBackupsDataRecord {

    @Id
    @GeneratedValue
    @Column(name = "server_backups_data_record_id")
    private Long serverBackupsDataRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "record_name")
    private String recordName;

    @Column(name = "backups_time")
    private Date backupsTime;

    @Column(name = "backups_type")
    private Integer backupsType;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getServerBackupsDataRecordId() {
        return this.serverBackupsDataRecordId;
    }

    public void setServerBackupsDataRecordId(Long serverBackupsDataRecordId) {
        this.serverBackupsDataRecordId = serverBackupsDataRecordId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getRecordName() {
        return this.recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public Date getBackupsTime() {
        return this.backupsTime;
    }

    public void setBackupsTime(Date backupsTime) {
        this.backupsTime = backupsTime;
    }

    public Integer getBackupsType() {
        return this.backupsType;
    }

    public void setBackupsType(Integer backupsType) {
        this.backupsType = backupsType;
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
