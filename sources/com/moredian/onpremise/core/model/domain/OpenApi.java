package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_open_api")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/OpenApi.class */
public class OpenApi {

    @Id
    @GeneratedValue
    @Column(name = "open_api_id")
    private Long openApiId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_key")
    private String appKey;

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

    public Long getOpenApiId() {
        return this.openApiId;
    }

    public void setOpenApiId(Long openApiId) {
        this.openApiId = openApiId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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
}
