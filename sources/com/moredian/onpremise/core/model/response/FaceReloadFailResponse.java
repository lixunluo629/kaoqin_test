package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.aspectj.weaver.model.AsmRelationshipUtils;

@ApiModel(description = "重抽人脸失败信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/FaceReloadFailResponse.class */
public class FaceReloadFailResponse implements Serializable {
    private static final long serialVersionUID = 2996653599684573190L;

    @ApiModelProperty(name = "memberName", value = "姓名")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = AsmRelationshipUtils.DECLARE_ERROR, value = "错误信息")
    private String error;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FaceReloadFailResponse)) {
            return false;
        }
        FaceReloadFailResponse other = (FaceReloadFailResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
            return false;
        }
        Object this$error = getError();
        Object other$error = other.getError();
        return this$error == null ? other$error == null : this$error.equals(other$error);
    }

    protected boolean canEqual(Object other) {
        return other instanceof FaceReloadFailResponse;
    }

    public int hashCode() {
        Object $memberName = getMemberName();
        int result = (1 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result2 = (result * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $error = getError();
        return (result2 * 59) + ($error == null ? 43 : $error.hashCode());
    }

    public String toString() {
        return "FaceReloadFailResponse(memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", error=" + getError() + ")";
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getError() {
        return this.error;
    }
}
