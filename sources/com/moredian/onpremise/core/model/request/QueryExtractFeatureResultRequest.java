package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "抽取特征值结果查询")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryExtractFeatureResultRequest.class */
public class QueryExtractFeatureResultRequest extends BaseRequest {

    @ApiModelProperty(name = "jobNum", value = "任务序列号")
    private String jobNum;

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryExtractFeatureResultRequest)) {
            return false;
        }
        QueryExtractFeatureResultRequest other = (QueryExtractFeatureResultRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        return this$jobNum == null ? other$jobNum == null : this$jobNum.equals(other$jobNum);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryExtractFeatureResultRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $jobNum = getJobNum();
        int result = (1 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryExtractFeatureResultRequest(jobNum=" + getJobNum() + ")";
    }

    public String getJobNum() {
        return this.jobNum;
    }
}
