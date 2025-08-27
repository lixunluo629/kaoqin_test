package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_meal_canteen")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MealCanteen.class */
public class MealCanteen {

    @Id
    @GeneratedValue
    @Column(name = "meal_canteen_id")
    private Long mealCanteenId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "canteen_name")
    private String canteenName;

    @Column(name = "canteen_scope")
    private String canteenScope;

    @Column(name = "canteen_owner_name")
    private String canteenOwnerName;

    @Column(name = "canteen_owner_mobile")
    private String canteenOwnerMobile;

    @Column(name = "canteen_address")
    private String canteenAddress;

    @Column(name = "canteen_region")
    private String canteenRegion;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
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

    public String getCanteenName() {
        return this.canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public String getCanteenOwnerName() {
        return this.canteenOwnerName;
    }

    public void setCanteenOwnerName(String canteenOwnerName) {
        this.canteenOwnerName = canteenOwnerName;
    }

    public String getCanteenOwnerMobile() {
        return this.canteenOwnerMobile;
    }

    public void setCanteenOwnerMobile(String canteenOwnerMobile) {
        this.canteenOwnerMobile = canteenOwnerMobile;
    }

    public String getCanteenAddress() {
        return this.canteenAddress;
    }

    public void setCanteenAddress(String canteenAddress) {
        this.canteenAddress = canteenAddress;
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

    public String getCanteenScope() {
        return this.canteenScope;
    }

    public void setCanteenScope(String canteenScope) {
        this.canteenScope = canteenScope;
    }

    public String getCanteenRegion() {
        return this.canteenRegion;
    }

    public void setCanteenRegion(String canteenRegion) {
        this.canteenRegion = canteenRegion;
    }
}
