package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存识别阈值")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SavePoseThresholdRequest.class */
public class SavePoseThresholdRequest implements Serializable {
    private static final long serialVersionUID = 5429100624245541910L;

    @ApiModelProperty(name = "poseThreshold", value = "录入底库检测角度配置")
    private String poseThreshold;

    public void setPoseThreshold(String poseThreshold) {
        this.poseThreshold = poseThreshold;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SavePoseThresholdRequest)) {
            return false;
        }
        SavePoseThresholdRequest other = (SavePoseThresholdRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$poseThreshold = getPoseThreshold();
        Object other$poseThreshold = other.getPoseThreshold();
        return this$poseThreshold == null ? other$poseThreshold == null : this$poseThreshold.equals(other$poseThreshold);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SavePoseThresholdRequest;
    }

    public int hashCode() {
        Object $poseThreshold = getPoseThreshold();
        int result = (1 * 59) + ($poseThreshold == null ? 43 : $poseThreshold.hashCode());
        return result;
    }

    public String toString() {
        return "SavePoseThresholdRequest(poseThreshold=" + getPoseThreshold() + ")";
    }

    public String getPoseThreshold() {
        return this.poseThreshold;
    }
}
