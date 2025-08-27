package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_account_auth")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AccountAuth.class */
public class AccountAuth {

    @Id
    @GeneratedValue
    @Column(name = "account_auth_id")
    private Long accountAuthId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "manage_dept_id")
    private String manageDeptId;

    @Column(name = "manage_device_sn")
    private String manageDeviceSn;

    @Column(name = "manage_app_id")
    private String manageAppId;

    @Column(name = "manage_module_id")
    private String manageModuleId;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAccountAuthId() {
        return this.accountAuthId;
    }

    public void setAccountAuthId(Long accountAuthId) {
        this.accountAuthId = accountAuthId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getManageDeptId() {
        return this.manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getManageDeviceSn() {
        return this.manageDeviceSn;
    }

    public void setManageDeviceSn(String manageDeviceSn) {
        this.manageDeviceSn = manageDeviceSn;
    }

    public String getManageAppId() {
        return this.manageAppId;
    }

    public void setManageAppId(String manageAppId) {
        this.manageAppId = manageAppId;
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

    public String getManageModuleId() {
        return this.manageModuleId;
    }

    public void setManageModuleId(String manageModuleId) {
        this.manageModuleId = manageModuleId;
    }
}
