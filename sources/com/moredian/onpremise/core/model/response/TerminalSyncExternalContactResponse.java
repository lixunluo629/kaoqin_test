package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步外部联系人响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncExternalContactResponse.class */
public class TerminalSyncExternalContactResponse implements Serializable {
    private static final long serialVersionUID = 98442838390039179L;

    @ApiModelProperty(name = "id", value = "外部联系人id")
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncExternalContactResponse)) {
            return false;
        }
        TerminalSyncExternalContactResponse other = (TerminalSyncExternalContactResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncExternalContactResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        return "TerminalSyncExternalContactResponse(id=" + getId() + ")";
    }

    public Long getId() {
        return this.id;
    }
}
