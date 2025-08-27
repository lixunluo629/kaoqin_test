package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModelProperty;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DataResponse.class */
public class DataResponse<T> extends CommonResponse {

    @ApiModelProperty(name = "data", value = "响应数据")
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataResponse)) {
            return false;
        }
        DataResponse<?> other = (DataResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$data = getData();
        Object other$data = other.getData();
        return this$data == null ? other$data == null : this$data.equals(other$data);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DataResponse;
    }

    public int hashCode() {
        Object $data = getData();
        int result = (1 * 59) + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "DataResponse(data=" + getData() + ")";
    }

    public T getData() {
        return this.data;
    }

    @Override // com.moredian.onpremise.core.model.response.CommonResponse
    public DataResponse<T> success() {
        setCode("0");
        return this;
    }

    @Override // com.moredian.onpremise.core.model.response.CommonResponse
    public DataResponse<T> fail(String errorCode, String message) {
        setCode(errorCode);
        setMessage(message);
        return this;
    }
}
