package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "重抽人脸进度信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/FaceReloadProgressResponse.class */
public class FaceReloadProgressResponse implements Serializable {
    private static final long serialVersionUID = -1022983632685311955L;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "任务状态：1-抽取中；0-完成")
    private Integer status;

    @ApiModelProperty(name = "total", value = "总数")
    private Integer total;

    @ApiModelProperty(name = Constants.SUCCESS, value = "成功数量")
    private Integer success;

    @ApiModelProperty(name = Constants.FAIL, value = "失败数量")
    private Integer fail;

    @ApiModelProperty(name = "failList", value = "失败详情列表")
    private List<FaceReloadFailResponse> failList;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public void setFailList(List<FaceReloadFailResponse> failList) {
        this.failList = failList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FaceReloadProgressResponse)) {
            return false;
        }
        FaceReloadProgressResponse other = (FaceReloadProgressResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$total = getTotal();
        Object other$total = other.getTotal();
        if (this$total == null) {
            if (other$total != null) {
                return false;
            }
        } else if (!this$total.equals(other$total)) {
            return false;
        }
        Object this$success = getSuccess();
        Object other$success = other.getSuccess();
        if (this$success == null) {
            if (other$success != null) {
                return false;
            }
        } else if (!this$success.equals(other$success)) {
            return false;
        }
        Object this$fail = getFail();
        Object other$fail = other.getFail();
        if (this$fail == null) {
            if (other$fail != null) {
                return false;
            }
        } else if (!this$fail.equals(other$fail)) {
            return false;
        }
        Object this$failList = getFailList();
        Object other$failList = other.getFailList();
        return this$failList == null ? other$failList == null : this$failList.equals(other$failList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof FaceReloadProgressResponse;
    }

    public int hashCode() {
        Object $status = getStatus();
        int result = (1 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $total = getTotal();
        int result2 = (result * 59) + ($total == null ? 43 : $total.hashCode());
        Object $success = getSuccess();
        int result3 = (result2 * 59) + ($success == null ? 43 : $success.hashCode());
        Object $fail = getFail();
        int result4 = (result3 * 59) + ($fail == null ? 43 : $fail.hashCode());
        Object $failList = getFailList();
        return (result4 * 59) + ($failList == null ? 43 : $failList.hashCode());
    }

    public String toString() {
        return "FaceReloadProgressResponse(status=" + getStatus() + ", total=" + getTotal() + ", success=" + getSuccess() + ", fail=" + getFail() + ", failList=" + getFailList() + ")";
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Integer getSuccess() {
        return this.success;
    }

    public Integer getFail() {
        return this.fail;
    }

    public List<FaceReloadFailResponse> getFailList() {
        return this.failList;
    }

    public FaceReloadProgressResponse() {
    }

    public FaceReloadProgressResponse(Integer status, Integer total, Integer success, Integer fail, List<FaceReloadFailResponse> failList) {
        this.status = status;
        this.total = total;
        this.success = success;
        this.fail = fail;
        this.failList = failList;
    }
}
