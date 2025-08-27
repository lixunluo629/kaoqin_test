package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "终端同步响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncResponse.class */
public class TerminalSyncResponse<T> implements Serializable {

    @ApiModelProperty(name = "syncTime", value = "同步时间")
    private Long syncTime;

    @ApiModelProperty(name = "syncResult", value = "新增待同步数据")
    private List<T> syncInsertResult = new ArrayList();

    @ApiModelProperty(name = "syncResult", value = "修改待同步数据")
    private List<T> syncModifyResult = new ArrayList();

    @ApiModelProperty(name = "syncResult", value = "删除待同步数据")
    private List<T> syncDeleteResult = new ArrayList();

    public void setSyncTime(Long syncTime) {
        this.syncTime = syncTime;
    }

    public void setSyncInsertResult(List<T> syncInsertResult) {
        this.syncInsertResult = syncInsertResult;
    }

    public void setSyncModifyResult(List<T> syncModifyResult) {
        this.syncModifyResult = syncModifyResult;
    }

    public void setSyncDeleteResult(List<T> syncDeleteResult) {
        this.syncDeleteResult = syncDeleteResult;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncResponse)) {
            return false;
        }
        TerminalSyncResponse<?> other = (TerminalSyncResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$syncTime = getSyncTime();
        Object other$syncTime = other.getSyncTime();
        if (this$syncTime == null) {
            if (other$syncTime != null) {
                return false;
            }
        } else if (!this$syncTime.equals(other$syncTime)) {
            return false;
        }
        Object this$syncInsertResult = getSyncInsertResult();
        Object other$syncInsertResult = other.getSyncInsertResult();
        if (this$syncInsertResult == null) {
            if (other$syncInsertResult != null) {
                return false;
            }
        } else if (!this$syncInsertResult.equals(other$syncInsertResult)) {
            return false;
        }
        Object this$syncModifyResult = getSyncModifyResult();
        Object other$syncModifyResult = other.getSyncModifyResult();
        if (this$syncModifyResult == null) {
            if (other$syncModifyResult != null) {
                return false;
            }
        } else if (!this$syncModifyResult.equals(other$syncModifyResult)) {
            return false;
        }
        Object this$syncDeleteResult = getSyncDeleteResult();
        Object other$syncDeleteResult = other.getSyncDeleteResult();
        return this$syncDeleteResult == null ? other$syncDeleteResult == null : this$syncDeleteResult.equals(other$syncDeleteResult);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncResponse;
    }

    public int hashCode() {
        Object $syncTime = getSyncTime();
        int result = (1 * 59) + ($syncTime == null ? 43 : $syncTime.hashCode());
        Object $syncInsertResult = getSyncInsertResult();
        int result2 = (result * 59) + ($syncInsertResult == null ? 43 : $syncInsertResult.hashCode());
        Object $syncModifyResult = getSyncModifyResult();
        int result3 = (result2 * 59) + ($syncModifyResult == null ? 43 : $syncModifyResult.hashCode());
        Object $syncDeleteResult = getSyncDeleteResult();
        return (result3 * 59) + ($syncDeleteResult == null ? 43 : $syncDeleteResult.hashCode());
    }

    public String toString() {
        return "TerminalSyncResponse(syncTime=" + getSyncTime() + ", syncInsertResult=" + getSyncInsertResult() + ", syncModifyResult=" + getSyncModifyResult() + ", syncDeleteResult=" + getSyncDeleteResult() + ")";
    }

    public Long getSyncTime() {
        return this.syncTime;
    }

    public List<T> getSyncInsertResult() {
        return this.syncInsertResult;
    }

    public List<T> getSyncModifyResult() {
        return this.syncModifyResult;
    }

    public List<T> getSyncDeleteResult() {
        return this.syncDeleteResult;
    }
}
