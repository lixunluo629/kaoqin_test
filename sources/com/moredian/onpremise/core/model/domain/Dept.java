package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_dept")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/Dept.class */
public class Dept {

    @Id
    @GeneratedValue
    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_grade")
    private Integer deptGrade;

    @Column(name = "super_dept_id")
    private Long superDeptId;

    @Column(name = "super_dept_name")
    private String superDeptName;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getSuperDeptId() {
        return this.superDeptId;
    }

    public void setSuperDeptId(Long superDeptId) {
        this.superDeptId = superDeptId;
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

    public String getSuperDeptName() {
        return this.superDeptName;
    }

    public void setSuperDeptName(String superDeptName) {
        this.superDeptName = superDeptName;
    }

    public Integer getDeptGrade() {
        return this.deptGrade;
    }

    public void setDeptGrade(Integer deptGrade) {
        this.deptGrade = deptGrade;
    }
}
