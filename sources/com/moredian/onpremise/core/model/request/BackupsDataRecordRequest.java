package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存备份数据")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BackupsDataRecordRequest.class */
public class BackupsDataRecordRequest extends BaseRequest {
    private static final long serialVersionUID = 3274520353695138199L;

    @ApiModelProperty(name = "backupsType", value = "备份类型：1-手动备份，2-自动备份", hidden = true)
    private Integer backupsType;

    public void setBackupsType(Integer backupsType) {
        this.backupsType = backupsType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BackupsDataRecordRequest)) {
            return false;
        }
        BackupsDataRecordRequest other = (BackupsDataRecordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$backupsType = getBackupsType();
        Object other$backupsType = other.getBackupsType();
        return this$backupsType == null ? other$backupsType == null : this$backupsType.equals(other$backupsType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BackupsDataRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $backupsType = getBackupsType();
        int result = (1 * 59) + ($backupsType == null ? 43 : $backupsType.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BackupsDataRecordRequest(backupsType=" + getBackupsType() + ")";
    }

    public Integer getBackupsType() {
        return this.backupsType;
    }
}
