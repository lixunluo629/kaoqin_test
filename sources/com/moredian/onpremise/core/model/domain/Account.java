package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_account")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/Account.class */
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "account_grade")
    private Integer accountGrade;

    @Column(name = "first_login_flag")
    private Integer firstLoginFlag;

    @Column(name = "account_mobile")
    private String accountMobile;

    @Column(name = "account_password")
    private String accountPassword;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_sex")
    private Integer accountSex;

    @Column(name = "account_birthday")
    private String accountBirthday;

    @Column(name = "language_type")
    private String languageType;

    @Column(name = "clone_account_id")
    private Long cloneAccountId;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "module_manager")
    private Integer moduleManager;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAccountMobile() {
        return this.accountMobile;
    }

    public void setAccountMobile(String accountMobile) {
        this.accountMobile = accountMobile;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountSex() {
        return this.accountSex;
    }

    public void setAccountSex(Integer accountSex) {
        this.accountSex = accountSex;
    }

    public String getAccountBirthday() {
        return this.accountBirthday;
    }

    public void setAccountBirthday(String accountBirthday) {
        this.accountBirthday = accountBirthday;
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

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public Integer getFirstLoginFlag() {
        return this.firstLoginFlag;
    }

    public void setFirstLoginFlag(Integer firstLoginFlag) {
        this.firstLoginFlag = firstLoginFlag;
    }

    public String getLanguageType() {
        return this.languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public Long getCloneAccountId() {
        return this.cloneAccountId;
    }

    public void setCloneAccountId(Long cloneAccountId) {
        this.cloneAccountId = cloneAccountId;
    }

    public Long getOperatorId() {
        return this.operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getModuleManager() {
        return this.moduleManager;
    }

    public void setModuleManager(Integer moduleManager) {
        this.moduleManager = moduleManager;
    }
}
