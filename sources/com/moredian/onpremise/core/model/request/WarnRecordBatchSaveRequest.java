package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "批量上传报警记录请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/WarnRecordBatchSaveRequest.class */
public class WarnRecordBatchSaveRequest implements Serializable {

    @ApiModelProperty(name = "saveRequests", value = "上传报警记录列表")
    private List<WarnRecordSaveRequest> saveRequests;

    public void setSaveRequests(List<WarnRecordSaveRequest> saveRequests) {
        this.saveRequests = saveRequests;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WarnRecordBatchSaveRequest)) {
            return false;
        }
        WarnRecordBatchSaveRequest other = (WarnRecordBatchSaveRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$saveRequests = getSaveRequests();
        Object other$saveRequests = other.getSaveRequests();
        return this$saveRequests == null ? other$saveRequests == null : this$saveRequests.equals(other$saveRequests);
    }

    protected boolean canEqual(Object other) {
        return other instanceof WarnRecordBatchSaveRequest;
    }

    public int hashCode() {
        Object $saveRequests = getSaveRequests();
        int result = (1 * 59) + ($saveRequests == null ? 43 : $saveRequests.hashCode());
        return result;
    }

    public String toString() {
        return "WarnRecordBatchSaveRequest(saveRequests=" + getSaveRequests() + ")";
    }

    public List<WarnRecordSaveRequest> getSaveRequests() {
        return this.saveRequests;
    }
}
