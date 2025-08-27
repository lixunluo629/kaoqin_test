package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "识别记录单条详情响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/StatisticsVerifyScoreDetailResponse.class */
public class StatisticsVerifyScoreDetailResponse implements Serializable {
    private static final long serialVersionUID = 8980902010130548795L;

    @ApiModelProperty(name = "verifyScore", value = "识别分数")
    private Integer verifyScore;

    @ApiModelProperty(name = "percent", value = "百分比")
    private String percent;

    @ApiModelProperty(name = "recordCount", value = "数量")
    private Integer recordCount;

    public void setVerifyScore(Integer verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public String toString() {
        return "StatisticsVerifyScoreDetailResponse(verifyScore=" + getVerifyScore() + ", percent=" + getPercent() + ", recordCount=" + getRecordCount() + ")";
    }

    public Integer getVerifyScore() {
        return this.verifyScore;
    }

    public String getPercent() {
        return this.percent;
    }

    public Integer getRecordCount() {
        return this.recordCount;
    }

    public int hashCode() {
        return this.verifyScore.hashCode();
    }

    public boolean equals(Object obj) {
        StatisticsVerifyScoreDetailResponse statisticsVerifyScoreDetailResponse = (StatisticsVerifyScoreDetailResponse) obj;
        return getVerifyScore().equals(statisticsVerifyScoreDetailResponse.getVerifyScore());
    }
}
