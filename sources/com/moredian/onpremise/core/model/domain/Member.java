package com.moredian.onpremise.core.model.domain;

import com.moredian.onpremise.core.utils.MyListUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_member")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/Member.class */
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "verify_face_url")
    private String verifyFaceUrl;

    @Column(name = "verify_face_md5")
    private String verifyFaceMd5;

    @Column(name = "eigenvalue_value")
    private String eigenvalueValue;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_mobile_pre")
    private String memberMobilePre;

    @Column(name = "member_mobile")
    private String memberMobile;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_telphone")
    private String memberTelphone;

    @Column(name = "member_position")
    private String memberPosition;

    @Column(name = "member_job_num")
    private String memberJobNum;

    @Column(name = "member_join_time")
    private String memberJoinTime;

    @Column(name = "member_gender")
    private Integer memberGender;

    @Column(name = "identity_card")
    private String identityCard;

    @Column(name = "member_card_num")
    private String memberCardNum;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;
    private String remark;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return this.memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberMobilePre() {
        return this.memberMobilePre;
    }

    public void setMemberMobilePre(String memberMobilePre) {
        this.memberMobilePre = memberMobilePre;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMemberEmail() {
        return this.memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberTelphone() {
        return this.memberTelphone;
    }

    public void setMemberTelphone(String memberTelphone) {
        this.memberTelphone = memberTelphone;
    }

    public String getMemberPosition() {
        return this.memberPosition;
    }

    public void setMemberPosition(String memberPosition) {
        this.memberPosition = memberPosition;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public String getMemberCardNum() {
        return this.memberCardNum;
    }

    public void setMemberCardNum(String memberCardNum) {
        this.memberCardNum = memberCardNum;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public String getVerifyFaceMd5() {
        return this.verifyFaceMd5;
    }

    public void setVerifyFaceMd5(String verifyFaceMd5) {
        this.verifyFaceMd5 = verifyFaceMd5;
    }

    public Integer getMemberGender() {
        return this.memberGender;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public static Map<Long, Member> listToMap(List<Member> members) {
        Map<Long, Member> result = new HashMap<>();
        if (MyListUtils.listIsEmpty(members)) {
            for (Member member : members) {
                if (member != null && member.getMemberId() != null) {
                    result.put(member.getMemberId(), member);
                }
            }
        }
        return result;
    }
}
