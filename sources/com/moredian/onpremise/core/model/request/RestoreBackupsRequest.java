package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "恢复数据")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/RestoreBackupsRequest.class */
public class RestoreBackupsRequest extends BaseRequest {

    @ApiModelProperty(name = "backupsDataId", value = "备份数据包id")
    private Long backupsDataId;

    public void setBackupsDataId(Long backupsDataId) {
        this.backupsDataId = backupsDataId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RestoreBackupsRequest)) {
            return false;
        }
        RestoreBackupsRequest other = (RestoreBackupsRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$backupsDataId = getBackupsDataId();
        Object other$backupsDataId = other.getBackupsDataId();
        return this$backupsDataId == null ? other$backupsDataId == null : this$backupsDataId.equals(other$backupsDataId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof RestoreBackupsRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $backupsDataId = getBackupsDataId();
        int result = (1 * 59) + ($backupsDataId == null ? 43 : $backupsDataId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "RestoreBackupsRequest(backupsDataId=" + getBackupsDataId() + ")";
    }

    public Long getBackupsDataId() {
        return this.backupsDataId;
    }
}
