package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

@ApiModel(description = "接口响应数据")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CommonResponse.class */
public class CommonResponse implements Serializable {
    public static final String SUCCEED = "0";
    public static final String FAILURE = "1";
    public static final String ACCESS_KEY_INVALID = "2";

    @ApiModelProperty(name = "code", value = "返回值状态码：0-成功；1或其它失败；")
    private String code;

    @ApiModelProperty(name = ConstraintHelper.MESSAGE, value = "返回信息")
    private String message;

    public CommonResponse() {
        this("0");
    }

    public CommonResponse(String code) {
        this.message = "OK";
        this.code = code;
    }

    public CommonResponse success() {
        setCode("0");
        return this;
    }

    public CommonResponse fail() {
        setCode("1");
        setMessage("请求失败");
        return this;
    }

    public CommonResponse fail(String code, String message) {
        setCode(code);
        setMessage(message);
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
