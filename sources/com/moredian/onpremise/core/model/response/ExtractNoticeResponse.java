package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "通知设备侧开始录脸响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ExtractNoticeResponse.class */
public class ExtractNoticeResponse implements Serializable {

    @ApiModelProperty(name = "queryKey", value = "查询抽取特征值结果的key")
    private String queryKey;

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExtractNoticeResponse)) {
            return false;
        }
        ExtractNoticeResponse other = (ExtractNoticeResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$queryKey = getQueryKey();
        Object other$queryKey = other.getQueryKey();
        return this$queryKey == null ? other$queryKey == null : this$queryKey.equals(other$queryKey);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExtractNoticeResponse;
    }

    public int hashCode() {
        Object $queryKey = getQueryKey();
        int result = (1 * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        return result;
    }

    public String toString() {
        return "ExtractNoticeResponse(queryKey=" + getQueryKey() + ")";
    }

    public String getQueryKey() {
        return this.queryKey;
    }
}
