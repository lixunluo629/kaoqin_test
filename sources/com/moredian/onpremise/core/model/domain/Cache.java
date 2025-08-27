package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_cache")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/Cache.class */
public class Cache {

    @Id
    @GeneratedValue
    @Column(name = "cache_id")
    private Long cacheId;

    @Column(name = "cache_type")
    private String cacheType;

    @Column(name = "value_type")
    private String valueType;

    @Column(name = "cache_key")
    private String cacheKey;

    @Column(name = "cache_expire")
    private Long cacheExpire;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "cache_value")
    private String cacheValue;

    public Cache() {
    }

    public Cache(String cacheType, String cacheKey, String cacheValue, String valueType) {
        this.cacheType = cacheType;
        this.cacheKey = cacheKey;
        this.cacheValue = cacheValue;
        this.valueType = valueType;
    }

    public Long getCacheId() {
        return this.cacheId;
    }

    public void setCacheId(Long cacheId) {
        this.cacheId = cacheId;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Long getCacheExpire() {
        return this.cacheExpire;
    }

    public void setCacheExpire(Long cacheExpire) {
        this.cacheExpire = cacheExpire;
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

    public String getCacheValue() {
        return this.cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }

    public String getCacheType() {
        return this.cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
