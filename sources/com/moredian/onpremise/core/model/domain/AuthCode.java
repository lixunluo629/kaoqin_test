package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_auth_code")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AuthCode.class */
public class AuthCode {

    @Id
    @GeneratedValue
    @Column(name = "auth_code_id")
    private Long authCodeId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "allow_max_num")
    private Integer allowMaxNum;

    @Column(name = "allow_module")
    private String allowModule;

    @Column(name = "generate_time")
    private Long generateTime;

    @Column(name = "valid_start_time")
    private Long validStartTime;

    @Column(name = "valid_end_time")
    private Long validEndTime;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAuthCodeId() {
        return this.authCodeId;
    }

    public void setAuthCodeId(Long authCodeId) {
        this.authCodeId = authCodeId;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Integer getAllowMaxNum() {
        return this.allowMaxNum;
    }

    public void setAllowMaxNum(Integer allowMaxNum) {
        this.allowMaxNum = allowMaxNum;
    }

    public Long getValidStartTime() {
        return this.validStartTime;
    }

    public void setValidStartTime(Long validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Long getValidEndTime() {
        return this.validEndTime;
    }

    public void setValidEndTime(Long validEndTime) {
        this.validEndTime = validEndTime;
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

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAllowModule() {
        return this.allowModule;
    }

    public void setAllowModule(String allowModule) {
        this.allowModule = allowModule;
    }

    public Long getGenerateTime() {
        return this.generateTime;
    }

    public void setGenerateTime(Long generateTime) {
        this.generateTime = generateTime;
    }
}
