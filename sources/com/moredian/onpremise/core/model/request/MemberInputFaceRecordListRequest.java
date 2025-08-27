package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "成员底库照片记录列表查询对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MemberInputFaceRecordListRequest.class */
public class MemberInputFaceRecordListRequest extends BaseRequest {
    private static final long serialVersionUID = -4452654560897167127L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(value = "channel", name = "渠道；1-管理后台录脸，2-MH2认证采脸。缺失值=1")
    private Integer channel;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberInputFaceRecordListRequest)) {
            return false;
        }
        MemberInputFaceRecordListRequest other = (MemberInputFaceRecordListRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$channel = getChannel();
        Object other$channel = other.getChannel();
        return this$channel == null ? other$channel == null : this$channel.equals(other$channel);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof MemberInputFaceRecordListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $channel = getChannel();
        return (result * 59) + ($channel == null ? 43 : $channel.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "MemberInputFaceRecordListRequest(paginator=" + getPaginator() + ", channel=" + getChannel() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Integer getChannel() {
        return this.channel;
    }
}
