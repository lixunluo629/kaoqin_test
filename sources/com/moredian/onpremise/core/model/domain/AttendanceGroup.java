package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_attendance_group")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/AttendanceGroup.class */
public class AttendanceGroup {

    @Id
    @GeneratedValue
    @Column(name = "attendance_group_id")
    private Long attendanceGroupId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_type")
    private Integer groupType;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "door_flag")
    private Integer doorFlag;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "speech_content")
    private String speechContent;

    @Column(name = "show_content")
    private String showContent;

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return this.groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
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

    public String getSpeechContent() {
        return this.speechContent;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }
}
