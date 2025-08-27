package com.moredian.onpremise.core.model.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_temperature_record")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/TemperatureRecord.class */
public class TemperatureRecord {

    @Id
    @GeneratedValue
    @Column(name = "temperature_record_id")
    private Long temperatureRecordId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_job_num")
    private String memberJobNum;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "temperature_value")
    private BigDecimal temperatureValue;

    @Column(name = "temperature_status")
    private Integer temperatureStatus;

    @Column(name = "snap_face_url")
    private String snapFaceUrl;

    @Column(name = "verify_day")
    private Integer verifyDay;

    @Column(name = "verify_time")
    private Long verifyTime;

    @Column(name = "healthCode")
    private String healthCode;

    @Column(name = "operator")
    private String operator;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getTemperatureRecordId() {
        return this.temperatureRecordId;
    }

    public void setTemperatureRecordId(Long temperatureRecordId) {
        this.temperatureRecordId = temperatureRecordId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public BigDecimal getTemperatureValue() {
        return this.temperatureValue;
    }

    public void setTemperatureValue(BigDecimal temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public Integer getTemperatureStatus() {
        return this.temperatureStatus;
    }

    public void setTemperatureStatus(Integer temperatureStatus) {
        this.temperatureStatus = temperatureStatus;
    }

    public String getSnapFaceUrl() {
        return this.snapFaceUrl;
    }

    public void setSnapFaceUrl(String snapFaceUrl) {
        this.snapFaceUrl = snapFaceUrl;
    }

    public Integer getVerifyDay() {
        return this.verifyDay;
    }

    public void setVerifyDay(Integer verifyDay) {
        this.verifyDay = verifyDay;
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

    public Long getVerifyTime() {
        return this.verifyTime;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getHealthCode() {
        return this.healthCode;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }
}
