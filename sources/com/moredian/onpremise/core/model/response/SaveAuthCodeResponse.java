package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "导入授权码响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveAuthCodeResponse.class */
public class SaveAuthCodeResponse implements Serializable {

    @ApiModelProperty(name = "allowMaxNum", value = "最大接入数量")
    private Integer allowMaxNum;

    @ApiModelProperty(name = "validEndTime", value = "过期时间")
    private Long validEndTime;

    @ApiModelProperty(name = "allowModule", value = "授权开发模块")
    private String allowModule;

    public void setAllowMaxNum(Integer allowMaxNum) {
        this.allowMaxNum = allowMaxNum;
    }

    public void setValidEndTime(Long validEndTime) {
        this.validEndTime = validEndTime;
    }

    public void setAllowModule(String allowModule) {
        this.allowModule = allowModule;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAuthCodeResponse)) {
            return false;
        }
        SaveAuthCodeResponse other = (SaveAuthCodeResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$allowMaxNum = getAllowMaxNum();
        Object other$allowMaxNum = other.getAllowMaxNum();
        if (this$allowMaxNum == null) {
            if (other$allowMaxNum != null) {
                return false;
            }
        } else if (!this$allowMaxNum.equals(other$allowMaxNum)) {
            return false;
        }
        Object this$validEndTime = getValidEndTime();
        Object other$validEndTime = other.getValidEndTime();
        if (this$validEndTime == null) {
            if (other$validEndTime != null) {
                return false;
            }
        } else if (!this$validEndTime.equals(other$validEndTime)) {
            return false;
        }
        Object this$allowModule = getAllowModule();
        Object other$allowModule = other.getAllowModule();
        return this$allowModule == null ? other$allowModule == null : this$allowModule.equals(other$allowModule);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveAuthCodeResponse;
    }

    public int hashCode() {
        Object $allowMaxNum = getAllowMaxNum();
        int result = (1 * 59) + ($allowMaxNum == null ? 43 : $allowMaxNum.hashCode());
        Object $validEndTime = getValidEndTime();
        int result2 = (result * 59) + ($validEndTime == null ? 43 : $validEndTime.hashCode());
        Object $allowModule = getAllowModule();
        return (result2 * 59) + ($allowModule == null ? 43 : $allowModule.hashCode());
    }

    public String toString() {
        return "SaveAuthCodeResponse(allowMaxNum=" + getAllowMaxNum() + ", validEndTime=" + getValidEndTime() + ", allowModule=" + getAllowModule() + ")";
    }

    public Integer getAllowMaxNum() {
        return this.allowMaxNum;
    }

    public Long getValidEndTime() {
        return this.validEndTime;
    }

    public String getAllowModule() {
        return this.allowModule;
    }
}
