package com.moredian.onpremise.core.model.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_temperature_device")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/TemperatureDevice.class */
public class TemperatureDevice {

    @Id
    @GeneratedValue
    @Column(name = "temperature_device_id")
    private Long temperatureDeviceId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "temperature_enable")
    private Integer temperatureEnable;

    @Column(name = "temperature_factor")
    private BigDecimal temperatureFactor;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getTemperatureDeviceId() {
        return this.temperatureDeviceId;
    }

    public void setTemperatureDeviceId(Long temperatureDeviceId) {
        this.temperatureDeviceId = temperatureDeviceId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getTemperatureEnable() {
        return this.temperatureEnable;
    }

    public void setTemperatureEnable(Integer temperatureEnable) {
        this.temperatureEnable = temperatureEnable;
    }

    public BigDecimal getTemperatureFactor() {
        return this.temperatureFactor;
    }

    public void setTemperatureFactor(BigDecimal temperatureFactor) {
        this.temperatureFactor = temperatureFactor;
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
