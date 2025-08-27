package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_meal_canteen_member")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MealCanteenMember.class */
public class MealCanteenMember {

    @Id
    @GeneratedValue
    @Column(name = "meal_canteen_member_id")
    private Long mealCanteenMemberId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "meal_canteen_id")
    private Long mealCanteenId;
    private Integer type;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "confirm_flag")
    private Integer confirmFlag;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMealCanteenMemberId() {
        return this.mealCanteenMemberId;
    }

    public void setMealCanteenMemberId(Long mealCanteenMemberId) {
        this.mealCanteenMemberId = mealCanteenMemberId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getConfirmFlag() {
        return this.confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
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
