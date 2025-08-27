package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_meal_temp_member")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/MealMember.class */
public class MealMember {

    @Id
    @GeneratedValue
    @Column(name = "meal_member_id")
    private Long mealMemberId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_type")
    private Integer memberType;

    @Column(name = "member_job_num")
    private String memberJobNum;

    @Column(name = "member_work_num")
    private String memberWorkNum;

    @Column(name = "member_gender")
    private Integer memberGender;

    @Column(name = "room_status")
    private Integer roomStatus;

    @Column(name = "shift_status")
    private Integer shiftStatus;

    @Column(name = "enable_status")
    private Integer enableStatus;

    @Column(name = "member_join_time")
    private String memberJoinTime;

    @Column(name = "member_retire_time")
    private String memberRetireTime;

    @Column(name = "verify_face_url")
    private String verifyFaceUrl;

    @Column(name = "meal_dept_name")
    private String mealDeptName;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMealMemberId() {
        return this.mealMemberId;
    }

    public void setMealMemberId(Long mealMemberId) {
        this.mealMemberId = mealMemberId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getMemberType() {
        return this.memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public String getMemberWorkNum() {
        return this.memberWorkNum;
    }

    public void setMemberWorkNum(String memberWorkNum) {
        this.memberWorkNum = memberWorkNum;
    }

    public Integer getMemberGender() {
        return this.memberGender;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public Integer getRoomStatus() {
        return this.roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Integer getShiftStatus() {
        return this.shiftStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public Integer getEnableStatus() {
        return this.enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public String getMemberRetireTime() {
        return this.memberRetireTime;
    }

    public void setMemberRetireTime(String memberRetireTime) {
        this.memberRetireTime = memberRetireTime;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
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

    public String getMealDeptName() {
        return this.mealDeptName;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }
}
