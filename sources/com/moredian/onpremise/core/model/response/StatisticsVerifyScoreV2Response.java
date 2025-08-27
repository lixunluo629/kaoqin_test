package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "识别记录V2响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/StatisticsVerifyScoreV2Response.class */
public class StatisticsVerifyScoreV2Response implements Serializable {
    private static final long serialVersionUID = -3689652649715034408L;

    @ApiModelProperty(name = "verifyScore", value = "第一名识别")
    private List<StatisticsVerifyScoreDetailResponse> verifyScoreList;

    @ApiModelProperty(name = "secondVerifyScore", value = "第二名识别")
    private List<StatisticsVerifyScoreDetailResponse> secondVerifyScoreList;

    @ApiModelProperty(name = "mirrorVerifyScore", value = "镜像识别")
    private List<StatisticsVerifyScoreDetailResponse> mirrorVerifyScoreList;

    public void setVerifyScoreList(List<StatisticsVerifyScoreDetailResponse> verifyScoreList) {
        this.verifyScoreList = verifyScoreList;
    }

    public void setSecondVerifyScoreList(List<StatisticsVerifyScoreDetailResponse> secondVerifyScoreList) {
        this.secondVerifyScoreList = secondVerifyScoreList;
    }

    public void setMirrorVerifyScoreList(List<StatisticsVerifyScoreDetailResponse> mirrorVerifyScoreList) {
        this.mirrorVerifyScoreList = mirrorVerifyScoreList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StatisticsVerifyScoreV2Response)) {
            return false;
        }
        StatisticsVerifyScoreV2Response other = (StatisticsVerifyScoreV2Response) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$verifyScoreList = getVerifyScoreList();
        Object other$verifyScoreList = other.getVerifyScoreList();
        if (this$verifyScoreList == null) {
            if (other$verifyScoreList != null) {
                return false;
            }
        } else if (!this$verifyScoreList.equals(other$verifyScoreList)) {
            return false;
        }
        Object this$secondVerifyScoreList = getSecondVerifyScoreList();
        Object other$secondVerifyScoreList = other.getSecondVerifyScoreList();
        if (this$secondVerifyScoreList == null) {
            if (other$secondVerifyScoreList != null) {
                return false;
            }
        } else if (!this$secondVerifyScoreList.equals(other$secondVerifyScoreList)) {
            return false;
        }
        Object this$mirrorVerifyScoreList = getMirrorVerifyScoreList();
        Object other$mirrorVerifyScoreList = other.getMirrorVerifyScoreList();
        return this$mirrorVerifyScoreList == null ? other$mirrorVerifyScoreList == null : this$mirrorVerifyScoreList.equals(other$mirrorVerifyScoreList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof StatisticsVerifyScoreV2Response;
    }

    public int hashCode() {
        Object $verifyScoreList = getVerifyScoreList();
        int result = (1 * 59) + ($verifyScoreList == null ? 43 : $verifyScoreList.hashCode());
        Object $secondVerifyScoreList = getSecondVerifyScoreList();
        int result2 = (result * 59) + ($secondVerifyScoreList == null ? 43 : $secondVerifyScoreList.hashCode());
        Object $mirrorVerifyScoreList = getMirrorVerifyScoreList();
        return (result2 * 59) + ($mirrorVerifyScoreList == null ? 43 : $mirrorVerifyScoreList.hashCode());
    }

    public String toString() {
        return "StatisticsVerifyScoreV2Response(verifyScoreList=" + getVerifyScoreList() + ", secondVerifyScoreList=" + getSecondVerifyScoreList() + ", mirrorVerifyScoreList=" + getMirrorVerifyScoreList() + ")";
    }

    public List<StatisticsVerifyScoreDetailResponse> getVerifyScoreList() {
        return this.verifyScoreList;
    }

    public List<StatisticsVerifyScoreDetailResponse> getSecondVerifyScoreList() {
        return this.secondVerifyScoreList;
    }

    public List<StatisticsVerifyScoreDetailResponse> getMirrorVerifyScoreList() {
        return this.mirrorVerifyScoreList;
    }
}
