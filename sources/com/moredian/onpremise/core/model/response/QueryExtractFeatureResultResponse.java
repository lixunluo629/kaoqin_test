package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Map;

@ApiModel(description = "抽取特征值查询结果返回信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryExtractFeatureResultResponse.class */
public class QueryExtractFeatureResultResponse implements Serializable {

    @ApiModelProperty(name = "extractResut", value = "抽取结果")
    private Map<String, CacheExtractFeatureStatusInfo> extractResut;

    public void setExtractResut(Map<String, CacheExtractFeatureStatusInfo> extractResut) {
        this.extractResut = extractResut;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryExtractFeatureResultResponse)) {
            return false;
        }
        QueryExtractFeatureResultResponse other = (QueryExtractFeatureResultResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$extractResut = getExtractResut();
        Object other$extractResut = other.getExtractResut();
        return this$extractResut == null ? other$extractResut == null : this$extractResut.equals(other$extractResut);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryExtractFeatureResultResponse;
    }

    public int hashCode() {
        Object $extractResut = getExtractResut();
        int result = (1 * 59) + ($extractResut == null ? 43 : $extractResut.hashCode());
        return result;
    }

    public String toString() {
        return "QueryExtractFeatureResultResponse(extractResut=" + getExtractResut() + ")";
    }

    public Map<String, CacheExtractFeatureStatusInfo> getExtractResut() {
        return this.extractResut;
    }
}
