package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

@ApiModel(description = "就餐在线鉴权响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveMealRecordResponse.class */
public class SaveMealRecordResponse implements Serializable {
    private static final long serialVersionUID = -1533048450377636331L;
    private Integer result;

    public void setResult(Integer result) {
        this.result = result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMealRecordResponse)) {
            return false;
        }
        SaveMealRecordResponse other = (SaveMealRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        return this$result == null ? other$result == null : this$result.equals(other$result);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveMealRecordResponse;
    }

    public int hashCode() {
        Object $result = getResult();
        int result = (1 * 59) + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    public String toString() {
        return "SaveMealRecordResponse(result=" + getResult() + ")";
    }

    public Integer getResult() {
        return this.result;
    }

    public SaveMealRecordResponse() {
    }

    public SaveMealRecordResponse(Integer result) {
        this.result = result;
    }
}
