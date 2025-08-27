package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_check_in_task")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/CheckInTask.class */
public class CheckInTask {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;
    private String name;

    @Column(name = "all_user")
    private Integer allUser;

    @Column(name = "all_day")
    private Integer allDay;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
    private Integer cycle;

    @Column(name = "cycle_extra")
    private String cycleExtra;

    @Column(name = "tips_text")
    private String tipsText;

    @Column(name = "tips_speech")
    private String tipsSpeech;
    private Integer status;
    private Integer type;

    @Column(name = "door_flag")
    private Integer doorFlag;

    @Column(name = "voice_remind")
    private String voiceRemind;

    @Column(name = "voice_remind_advance_time")
    private Integer voiceRemindAdvanceTime;

    @Column(name = "delete_flag")
    private Integer deleteFlag;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAllUser() {
        return this.allUser;
    }

    public void setAllUser(Integer allUser) {
        this.allUser = allUser;
    }

    public Integer getAllDay() {
        return this.allDay;
    }

    public void setAllDay(Integer allDay) {
        this.allDay = allDay;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCycle() {
        return this.cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getCycleExtra() {
        return this.cycleExtra;
    }

    public void setCycleExtra(String cycleExtra) {
        this.cycleExtra = cycleExtra;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }

    public String getVoiceRemind() {
        return this.voiceRemind;
    }

    public void setVoiceRemind(String voiceRemind) {
        this.voiceRemind = voiceRemind;
    }

    public Integer getVoiceRemindAdvanceTime() {
        return this.voiceRemindAdvanceTime;
    }

    public void setVoiceRemindAdvanceTime(Integer voiceRemindAdvanceTime) {
        this.voiceRemindAdvanceTime = voiceRemindAdvanceTime;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
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
}
