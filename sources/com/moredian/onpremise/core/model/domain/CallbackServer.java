package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_callback_server")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/CallbackServer.class */
public class CallbackServer {

    @Id
    @GeneratedValue
    @Column(name = "callback_server_id")
    private Long callbackServerId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "callback_tag")
    private String callbackTag;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "device_sn")
    private String deviceSn;

    public Long getCallbackServerId() {
        return this.callbackServerId;
    }

    public void setCallbackServerId(Long callbackServerId) {
        this.callbackServerId = callbackServerId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCallbackTag() {
        return this.callbackTag;
    }

    public void setCallbackTag(String callbackTag) {
        this.callbackTag = callbackTag;
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

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
}
