package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_access_key")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AccessKey.class */
public class AccessKey {

    @Id
    @GeneratedValue
    @Column(name = "access_key_id")
    private Long accessKeyId;

    @Column(name = "access_key_secret")
    private String accessKeySecret;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "key_status")
    private Integer keyStatus;

    @Column(name = "gmt_valid_start")
    private Date gmtValidStart;

    @Column(name = "gmt_valid_end")
    private Date gmtValidEnd;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAccessKeyId() {
        return this.accessKeyId;
    }

    public void setAccessKeyId(Long accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return this.accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public Integer getKeyStatus() {
        return this.keyStatus;
    }

    public void setKeyStatus(Integer keyStatus) {
        this.keyStatus = keyStatus;
    }

    public Date getGmtValidStart() {
        return this.gmtValidStart;
    }

    public void setGmtValidStart(Date gmtValidStart) {
        this.gmtValidStart = gmtValidStart;
    }

    public Date getGmtValidEnd() {
        return this.gmtValidEnd;
    }

    public void setGmtValidEnd(Date gmtValidEnd) {
        this.gmtValidEnd = gmtValidEnd;
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

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
}
