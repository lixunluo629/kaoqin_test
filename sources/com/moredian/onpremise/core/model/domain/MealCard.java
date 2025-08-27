package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_meal_card")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MealCard.class */
public class MealCard {

    @Id
    @Column(name = "meal_card_id")
    private Long mealCardId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_type")
    private Integer cardType;

    @Column(name = "breakfast_times")
    private Integer breakfastTimes;

    @Column(name = "lunch_times")
    private Integer lunchTimes;

    @Column(name = "dinner_times")
    private Integer dinnerTimes;

    @Column(name = "midnight_snack_times")
    private Integer midnightSnackTimes;

    @Column(name = "total_limit_times")
    private Integer totalLimitTimes;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
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

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardType() {
        return this.cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getBreakfastTimes() {
        return this.breakfastTimes;
    }

    public void setBreakfastTimes(Integer breakfastTimes) {
        this.breakfastTimes = breakfastTimes;
    }

    public Integer getLunchTimes() {
        return this.lunchTimes;
    }

    public void setLunchTimes(Integer lunchTimes) {
        this.lunchTimes = lunchTimes;
    }

    public Integer getDinnerTimes() {
        return this.dinnerTimes;
    }

    public void setDinnerTimes(Integer dinnerTimes) {
        this.dinnerTimes = dinnerTimes;
    }

    public Integer getMidnightSnackTimes() {
        return this.midnightSnackTimes;
    }

    public void setMidnightSnackTimes(Integer midnightSnackTimes) {
        this.midnightSnackTimes = midnightSnackTimes;
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

    public Integer getTotalLimitTimes() {
        return this.totalLimitTimes;
    }

    public void setTotalLimitTimes(Integer totalLimitTimes) {
        this.totalLimitTimes = totalLimitTimes;
    }
}
