package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.model.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

@ApiModel(description = "批量添加成员响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/BatchInsertMemberResponse.class */
public class BatchInsertMemberResponse implements Serializable {

    @ApiModelProperty(name = "rowNum", value = "批量添加失败行数")
    private Integer rowNum;

    @ApiModelProperty(name = ConstraintHelper.MESSAGE, value = "添加失败错误信息")
    private String message;
    private boolean isContinue = false;

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchInsertMemberResponse)) {
            return false;
        }
        BatchInsertMemberResponse other = (BatchInsertMemberResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$rowNum = getRowNum();
        Object other$rowNum = other.getRowNum();
        if (this$rowNum == null) {
            if (other$rowNum != null) {
                return false;
            }
        } else if (!this$rowNum.equals(other$rowNum)) {
            return false;
        }
        Object this$message = getMessage();
        Object other$message = other.getMessage();
        if (this$message == null) {
            if (other$message != null) {
                return false;
            }
        } else if (!this$message.equals(other$message)) {
            return false;
        }
        return isContinue() == other.isContinue();
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatchInsertMemberResponse;
    }

    public int hashCode() {
        Object $rowNum = getRowNum();
        int result = (1 * 59) + ($rowNum == null ? 43 : $rowNum.hashCode());
        Object $message = getMessage();
        return (((result * 59) + ($message == null ? 43 : $message.hashCode())) * 59) + (isContinue() ? 79 : 97);
    }

    public String toString() {
        return "BatchInsertMemberResponse(rowNum=" + getRowNum() + ", message=" + getMessage() + ", isContinue=" + isContinue() + ")";
    }

    public Integer getRowNum() {
        return this.rowNum;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isContinue() {
        return this.isContinue;
    }

    public BatchInsertMemberResponse defaultFail(Integer rowNum) {
        setRowNum(rowNum);
        setMessage(OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode());
        return this;
    }

    public boolean checkMobile(String mobile, Integer rowNum, List<BatchInsertMemberResponse> responses) {
        if (StringUtils.isNotEmpty(mobile)) {
        }
        return this.isContinue;
    }

    public boolean checkMemberJobNum(String memberJobNum, Integer rowNum, List<BatchInsertMemberResponse> responses) {
        if (StringUtils.isEmpty(memberJobNum)) {
            setRowNum(rowNum);
            setMessage(OnpremiseErrorEnum.MEMBER_JOB_NUM_MUST_NOT_NULL.getErrorCode());
            responses.add(this);
            this.isContinue = true;
        }
        return this.isContinue;
    }

    public boolean checkMemberExists(Member member, Integer rowNum, List<BatchInsertMemberResponse> responses, int type) {
        if (member != null) {
            setRowNum(rowNum);
            if (type == 1) {
                setMessage(OnpremiseErrorEnum.MEMBER_NUMBERS_ALEADY_EXIST.getErrorCode());
            } else if (type == 2) {
                setMessage(OnpremiseErrorEnum.MEMBER_IDENTITY_CARD_ALREADY_EXIST.getErrorCode());
            }
            responses.add(this);
            this.isContinue = true;
        }
        return this.isContinue;
    }
}
