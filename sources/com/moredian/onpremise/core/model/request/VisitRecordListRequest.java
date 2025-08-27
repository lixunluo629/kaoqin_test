package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "访客记录列表查询参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VisitRecordListRequest.class */
public class VisitRecordListRequest extends BaseRequest {
    private static final long serialVersionUID = -4930828490525832706L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "startTimestamp", value = "查询时间戳（毫秒）开始。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Long startTimestamp;

    @ApiModelProperty(name = "endTimestamp", value = "查询时间戳（毫秒）结束。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Long endTimestamp;

    @ApiModelProperty(name = "startDate", value = "查询时间日，开始。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Integer startDate;

    @ApiModelProperty(name = "endDate", value = "查询时间日，结束。时间查询结果：包含查询开始时间，不包含查询结束时间")
    private Integer endDate;

    @ApiModelProperty(name = "keywords", value = "查询关键字，支持访客姓名或身份证模糊查询")
    private String keywords;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitRecordListRequest)) {
            return false;
        }
        VisitRecordListRequest other = (VisitRecordListRequest) o;
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
        Object this$startTimestamp = getStartTimestamp();
        Object other$startTimestamp = other.getStartTimestamp();
        if (this$startTimestamp == null) {
            if (other$startTimestamp != null) {
                return false;
            }
        } else if (!this$startTimestamp.equals(other$startTimestamp)) {
            return false;
        }
        Object this$endTimestamp = getEndTimestamp();
        Object other$endTimestamp = other.getEndTimestamp();
        if (this$endTimestamp == null) {
            if (other$endTimestamp != null) {
                return false;
            }
        } else if (!this$endTimestamp.equals(other$endTimestamp)) {
            return false;
        }
        Object this$startDate = getStartDate();
        Object other$startDate = other.getStartDate();
        if (this$startDate == null) {
            if (other$startDate != null) {
                return false;
            }
        } else if (!this$startDate.equals(other$startDate)) {
            return false;
        }
        Object this$endDate = getEndDate();
        Object other$endDate = other.getEndDate();
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$keywords = getKeywords();
        Object other$keywords = other.getKeywords();
        return this$keywords == null ? other$keywords == null : this$keywords.equals(other$keywords);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VisitRecordListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $startTimestamp = getStartTimestamp();
        int result2 = (result * 59) + ($startTimestamp == null ? 43 : $startTimestamp.hashCode());
        Object $endTimestamp = getEndTimestamp();
        int result3 = (result2 * 59) + ($endTimestamp == null ? 43 : $endTimestamp.hashCode());
        Object $startDate = getStartDate();
        int result4 = (result3 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result5 = (result4 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $keywords = getKeywords();
        return (result5 * 59) + ($keywords == null ? 43 : $keywords.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VisitRecordListRequest(paginator=" + getPaginator() + ", startTimestamp=" + getStartTimestamp() + ", endTimestamp=" + getEndTimestamp() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", keywords=" + getKeywords() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getStartTimestamp() {
        return this.startTimestamp;
    }

    public Long getEndTimestamp() {
        return this.endTimestamp;
    }

    public Integer getStartDate() {
        return this.startDate;
    }

    public Integer getEndDate() {
        return this.endDate;
    }

    public String getKeywords() {
        return this.keywords;
    }
}
