package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.cache.interceptor.CacheOperationExpressionEvaluator;

@ApiModel(description = "保存图片响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveImageResponse.class */
public class SaveImageResponse implements Serializable {

    @ApiModelProperty(name = "flag", value = "保存标识：true成功，false失败")
    private Boolean flag;

    @ApiModelProperty(name = CacheOperationExpressionEvaluator.RESULT_VARIABLE, value = "保存结果：成功时为图片url，失败时为失败原因(失败时可能为空)")
    private String result;

    @ApiModelProperty(name = "fileName", value = "图片名称，失败时返回")
    private String fileName;

    @ApiModelProperty(name = "queryKey", value = "查询抽取特征值结果的key")
    private String queryKey;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveImageResponse)) {
            return false;
        }
        SaveImageResponse other = (SaveImageResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$flag = getFlag();
        Object other$flag = other.getFlag();
        if (this$flag == null) {
            if (other$flag != null) {
                return false;
            }
        } else if (!this$flag.equals(other$flag)) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result == null) {
            if (other$result != null) {
                return false;
            }
        } else if (!this$result.equals(other$result)) {
            return false;
        }
        Object this$fileName = getFileName();
        Object other$fileName = other.getFileName();
        if (this$fileName == null) {
            if (other$fileName != null) {
                return false;
            }
        } else if (!this$fileName.equals(other$fileName)) {
            return false;
        }
        Object this$queryKey = getQueryKey();
        Object other$queryKey = other.getQueryKey();
        if (this$queryKey == null) {
            if (other$queryKey != null) {
                return false;
            }
        } else if (!this$queryKey.equals(other$queryKey)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        return this$memberJobNum == null ? other$memberJobNum == null : this$memberJobNum.equals(other$memberJobNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveImageResponse;
    }

    public int hashCode() {
        Object $flag = getFlag();
        int result = (1 * 59) + ($flag == null ? 43 : $flag.hashCode());
        Object $result = getResult();
        int result2 = (result * 59) + ($result == null ? 43 : $result.hashCode());
        Object $fileName = getFileName();
        int result3 = (result2 * 59) + ($fileName == null ? 43 : $fileName.hashCode());
        Object $queryKey = getQueryKey();
        int result4 = (result3 * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        Object $memberJobNum = getMemberJobNum();
        return (result4 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
    }

    public String toString() {
        return "SaveImageResponse(flag=" + getFlag() + ", result=" + getResult() + ", fileName=" + getFileName() + ", queryKey=" + getQueryKey() + ", memberJobNum=" + getMemberJobNum() + ")";
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public String getResult() {
        return this.result;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }
}
