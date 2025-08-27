package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_visit_config")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/VisitConfig.class */
public class VisitConfig {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "wallpaper_url")
    private String wallpaperUrl;

    @Column(name = "screensaver_flag")
    private Integer screensaverFlag;

    @Column(name = "screensaver_url")
    private String screensaverUrl;

    @Column(name = "id_card_verify")
    private Integer idCardVerify;

    @Column(name = "input_face")
    private Integer inputFace;

    @Column(name = "interviewee_flag")
    private Integer intervieweeFlag;

    @Column(name = "all_day_flag")
    private Integer allDayFlag;

    @Column(name = "pass_time")
    private String passTime;

    @Column(name = "cycle_flag")
    private Integer cycleFlag;
    private String scope;

    @Column(name = "show_content")
    private String showContent;

    @Column(name = "speech_content")
    private String speechContent;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

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

    public String getWallpaperUrl() {
        return this.wallpaperUrl;
    }

    public void setWallpaperUrl(String wallpaperUrl) {
        this.wallpaperUrl = wallpaperUrl;
    }

    public Integer getScreensaverFlag() {
        return this.screensaverFlag;
    }

    public void setScreensaverFlag(Integer screensaverFlag) {
        this.screensaverFlag = screensaverFlag;
    }

    public Integer getIdCardVerify() {
        return this.idCardVerify;
    }

    public void setIdCardVerify(Integer idCardVerify) {
        this.idCardVerify = idCardVerify;
    }

    public Integer getInputFace() {
        return this.inputFace;
    }

    public void setInputFace(Integer inputFace) {
        this.inputFace = inputFace;
    }

    public Integer getIntervieweeFlag() {
        return this.intervieweeFlag;
    }

    public void setIntervieweeFlag(Integer intervieweeFlag) {
        this.intervieweeFlag = intervieweeFlag;
    }

    public Integer getAllDayFlag() {
        return this.allDayFlag;
    }

    public void setAllDayFlag(Integer allDayFlag) {
        this.allDayFlag = allDayFlag;
    }

    public String getPassTime() {
        return this.passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public Integer getCycleFlag() {
        return this.cycleFlag;
    }

    public void setCycleFlag(Integer cycleFlag) {
        this.cycleFlag = cycleFlag;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getShowContent() {
        return this.showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public String getSpeechContent() {
        return this.speechContent;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
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

    public String getScreensaverUrl() {
        return this.screensaverUrl;
    }

    public void setScreensaverUrl(String screensaverUrl) {
        this.screensaverUrl = screensaverUrl;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }
}
