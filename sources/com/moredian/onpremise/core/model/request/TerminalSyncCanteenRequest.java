package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "终端同步签到任务数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncCanteenRequest.class */
public class TerminalSyncCanteenRequest extends TerminalSyncRequest {
    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncCanteenRequest)) {
            return false;
        }
        TerminalSyncCanteenRequest other = (TerminalSyncCanteenRequest) o;
        return other.canEqual(this);
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncCanteenRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public int hashCode() {
        return 1;
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public String toString() {
        return "TerminalSyncCanteenRequest()";
    }
}
