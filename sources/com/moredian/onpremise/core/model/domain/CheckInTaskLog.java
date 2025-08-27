package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_check_in_task_log")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/CheckInTaskLog.class */
public class CheckInTaskLog {

    @Id
    private Long id;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_time")
    private String taskTime;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "checked_in_count")
    private Integer checkedInCount;

    @Column(name = "unchecked_in_count")
    private Integer uncheckedInCount;

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

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTime() {
        return this.taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCheckedInCount() {
        return this.checkedInCount;
    }

    public void setCheckedInCount(Integer checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public Integer getUncheckedInCount() {
        return this.uncheckedInCount;
    }

    public void setUncheckedInCount(Integer uncheckedInCount) {
        this.uncheckedInCount = uncheckedInCount;
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
