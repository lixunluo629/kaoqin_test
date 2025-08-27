package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存设备默认展示画面")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveDeviceDefaultShowRequest.class */
public class SaveDeviceDefaultShowRequest implements Serializable {
    private static final long serialVersionUID = -6794677294359509205L;

    @ApiModelProperty(name = "defaultShowType", value = "设备默认展示画面类型：1：人脸识别，2：广告展示，3：熄屏")
    private String defaultShowType;

    public void setDefaultShowType(String defaultShowType) {
        this.defaultShowType = defaultShowType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveDeviceDefaultShowRequest)) {
            return false;
        }
        SaveDeviceDefaultShowRequest other = (SaveDeviceDefaultShowRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$defaultShowType = getDefaultShowType();
        Object other$defaultShowType = other.getDefaultShowType();
        return this$defaultShowType == null ? other$defaultShowType == null : this$defaultShowType.equals(other$defaultShowType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveDeviceDefaultShowRequest;
    }

    public int hashCode() {
        Object $defaultShowType = getDefaultShowType();
        int result = (1 * 59) + ($defaultShowType == null ? 43 : $defaultShowType.hashCode());
        return result;
    }

    public String toString() {
        return "SaveDeviceDefaultShowRequest(defaultShowType=" + getDefaultShowType() + ")";
    }

    public String getDefaultShowType() {
        return this.defaultShowType;
    }
}
