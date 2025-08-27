package com.moredian.onpremise.core.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "查询配置响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryConfigResponse.class */
public class QueryConfigResponse implements Serializable {
    private static final long serialVersionUID = 6059005001502464396L;

    @ApiModelProperty(name = "configKey", value = "配置key")
    private String configKey;

    @ApiModelProperty(name = "configValue", value = "配置value")
    private String configValue;

    @JsonIgnore
    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

    @JsonIgnore
    @ApiModelProperty(name = "gmtModify", value = "更新时间", hidden = true)
    private Date gmtModify;

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryConfigResponse)) {
            return false;
        }
        QueryConfigResponse other = (QueryConfigResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$configKey = getConfigKey();
        Object other$configKey = other.getConfigKey();
        if (this$configKey == null) {
            if (other$configKey != null) {
                return false;
            }
        } else if (!this$configKey.equals(other$configKey)) {
            return false;
        }
        Object this$configValue = getConfigValue();
        Object other$configValue = other.getConfigValue();
        if (this$configValue == null) {
            if (other$configValue != null) {
                return false;
            }
        } else if (!this$configValue.equals(other$configValue)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null) {
            if (other$gmtCreate != null) {
                return false;
            }
        } else if (!this$gmtCreate.equals(other$gmtCreate)) {
            return false;
        }
        Object this$gmtModify = getGmtModify();
        Object other$gmtModify = other.getGmtModify();
        return this$gmtModify == null ? other$gmtModify == null : this$gmtModify.equals(other$gmtModify);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryConfigResponse;
    }

    public int hashCode() {
        Object $configKey = getConfigKey();
        int result = (1 * 59) + ($configKey == null ? 43 : $configKey.hashCode());
        Object $configValue = getConfigValue();
        int result2 = (result * 59) + ($configValue == null ? 43 : $configValue.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result3 = (result2 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $gmtModify = getGmtModify();
        return (result3 * 59) + ($gmtModify == null ? 43 : $gmtModify.hashCode());
    }

    public String toString() {
        return "QueryConfigResponse(configKey=" + getConfigKey() + ", configValue=" + getConfigValue() + ", gmtCreate=" + getGmtCreate() + ", gmtModify=" + getGmtModify() + ")";
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public String getConfigValue() {
        return this.configValue;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }
}
