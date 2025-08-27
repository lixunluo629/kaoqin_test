package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "设备端批量上传识别记录请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyRecordBatchSaveRequest.class */
public class VerifyRecordBatchSaveRequest implements Serializable {

    @ApiModelProperty(name = "saveRequests", value = "批量上传识别记录列表")
    private List<VerifyRecordSaveRequest> saveRequests;

    public void setSaveRequests(List<VerifyRecordSaveRequest> saveRequests) {
        this.saveRequests = saveRequests;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordBatchSaveRequest)) {
            return false;
        }
        VerifyRecordBatchSaveRequest other = (VerifyRecordBatchSaveRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$saveRequests = getSaveRequests();
        Object other$saveRequests = other.getSaveRequests();
        return this$saveRequests == null ? other$saveRequests == null : this$saveRequests.equals(other$saveRequests);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordBatchSaveRequest;
    }

    public int hashCode() {
        Object $saveRequests = getSaveRequests();
        int result = (1 * 59) + ($saveRequests == null ? 43 : $saveRequests.hashCode());
        return result;
    }

    public String toString() {
        return "VerifyRecordBatchSaveRequest(saveRequests=" + getSaveRequests() + ")";
    }

    public List<VerifyRecordSaveRequest> getSaveRequests() {
        return this.saveRequests;
    }
}
