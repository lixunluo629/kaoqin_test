package com.moredian.onpremise.core.model.response;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "终端同步响应信息v2")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncV2Response.class */
public class TerminalSyncV2Response<T> implements Serializable {
    private static final long serialVersionUID = -6545162194715094898L;

    @ApiModelProperty(name = RtspHeaders.Values.TIME, value = "同步时间")
    private Long time;

    @ApiModelProperty(name = "syncInsertResult", value = "新增待同步数据")
    private List<T> ins = new ArrayList();

    @ApiModelProperty(name = "syncModifyResult", value = "修改待同步数据")
    private List<T> mod = new ArrayList();

    @ApiModelProperty(name = "syncDeleteResult", value = "删除待同步数据")
    private List<T> del = new ArrayList();

    public void setTime(Long time) {
        this.time = time;
    }

    public void setIns(List<T> ins) {
        this.ins = ins;
    }

    public void setMod(List<T> mod) {
        this.mod = mod;
    }

    public void setDel(List<T> del) {
        this.del = del;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncV2Response)) {
            return false;
        }
        TerminalSyncV2Response<?> other = (TerminalSyncV2Response) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$time = getTime();
        Object other$time = other.getTime();
        if (this$time == null) {
            if (other$time != null) {
                return false;
            }
        } else if (!this$time.equals(other$time)) {
            return false;
        }
        Object this$ins = getIns();
        Object other$ins = other.getIns();
        if (this$ins == null) {
            if (other$ins != null) {
                return false;
            }
        } else if (!this$ins.equals(other$ins)) {
            return false;
        }
        Object this$mod = getMod();
        Object other$mod = other.getMod();
        if (this$mod == null) {
            if (other$mod != null) {
                return false;
            }
        } else if (!this$mod.equals(other$mod)) {
            return false;
        }
        Object this$del = getDel();
        Object other$del = other.getDel();
        return this$del == null ? other$del == null : this$del.equals(other$del);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncV2Response;
    }

    public int hashCode() {
        Object $time = getTime();
        int result = (1 * 59) + ($time == null ? 43 : $time.hashCode());
        Object $ins = getIns();
        int result2 = (result * 59) + ($ins == null ? 43 : $ins.hashCode());
        Object $mod = getMod();
        int result3 = (result2 * 59) + ($mod == null ? 43 : $mod.hashCode());
        Object $del = getDel();
        return (result3 * 59) + ($del == null ? 43 : $del.hashCode());
    }

    public String toString() {
        return "TerminalSyncV2Response(time=" + getTime() + ", ins=" + getIns() + ", mod=" + getMod() + ", del=" + getDel() + ")";
    }

    public Long getTime() {
        return this.time;
    }

    public List<T> getIns() {
        return this.ins;
    }

    public List<T> getMod() {
        return this.mod;
    }

    public List<T> getDel() {
        return this.del;
    }
}
