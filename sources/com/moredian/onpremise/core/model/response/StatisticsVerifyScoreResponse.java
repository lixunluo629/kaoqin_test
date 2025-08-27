package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "识别记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/StatisticsVerifyScoreResponse.class */
public class StatisticsVerifyScoreResponse implements Serializable {
    private static final long serialVersionUID = -2786343159116037361L;

    @ApiModelProperty(name = "verifyScore", value = "识别分数")
    private List<Integer> verifyScore;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名识别分数")
    private List<Integer> secondVerifyScore;

    public void setVerifyScore(List<Integer> verifyScore) {
        this.verifyScore = verifyScore;
    }

    public void setSecondVerifyScore(List<Integer> secondVerifyScore) {
        this.secondVerifyScore = secondVerifyScore;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StatisticsVerifyScoreResponse)) {
            return false;
        }
        StatisticsVerifyScoreResponse other = (StatisticsVerifyScoreResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$verifyScore = getVerifyScore();
        Object other$verifyScore = other.getVerifyScore();
        if (this$verifyScore == null) {
            if (other$verifyScore != null) {
                return false;
            }
        } else if (!this$verifyScore.equals(other$verifyScore)) {
            return false;
        }
        Object this$secondVerifyScore = getSecondVerifyScore();
        Object other$secondVerifyScore = other.getSecondVerifyScore();
        return this$secondVerifyScore == null ? other$secondVerifyScore == null : this$secondVerifyScore.equals(other$secondVerifyScore);
    }

    protected boolean canEqual(Object other) {
        return other instanceof StatisticsVerifyScoreResponse;
    }

    public int hashCode() {
        Object $verifyScore = getVerifyScore();
        int result = (1 * 59) + ($verifyScore == null ? 43 : $verifyScore.hashCode());
        Object $secondVerifyScore = getSecondVerifyScore();
        return (result * 59) + ($secondVerifyScore == null ? 43 : $secondVerifyScore.hashCode());
    }

    public String toString() {
        return "StatisticsVerifyScoreResponse(verifyScore=" + getVerifyScore() + ", secondVerifyScore=" + getSecondVerifyScore() + ")";
    }

    public List<Integer> getVerifyScore() {
        return this.verifyScore;
    }

    public List<Integer> getSecondVerifyScore() {
        return this.secondVerifyScore;
    }
}
