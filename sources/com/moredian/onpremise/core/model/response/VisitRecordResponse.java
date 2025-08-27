package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "访客记录列表响应参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/VisitRecordResponse.class */
public class VisitRecordResponse implements Serializable {
    private static final long serialVersionUID = 5349330686572086667L;

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "externalContactId", value = "externalContactId")
    private Long externalContactId;

    @ApiModelProperty(name = "visitorName", value = "访客姓名")
    private String visitorName;

    @ApiModelProperty(name = "visitorIdCard", value = "来访者身份证")
    private String visitorIdCard;

    @ApiModelProperty(name = "visitorFaceUrl", value = "来访者人脸照片url")
    private String visitorFaceUrl;

    @ApiModelProperty(name = "visitorMobile", value = "来访者手机号")
    private String visitorMobile;

    @ApiModelProperty(name = "intervieweeName", value = "被访者姓名")
    private String intervieweeName;

    @ApiModelProperty(name = "intervieweeDeptName", value = "被访者部门名称")
    private String intervieweeDeptName;

    @ApiModelProperty(name = "intervieweeMobile", value = "被访者手机号")
    private String intervieweeMobile;

    @ApiModelProperty(name = "signTimestamp", value = "登记时间")
    private Long signTimestamp;

    public void setId(Long id) {
        this.id = id;
    }

    public void setExternalContactId(Long externalContactId) {
        this.externalContactId = externalContactId;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public void setVisitorIdCard(String visitorIdCard) {
        this.visitorIdCard = visitorIdCard;
    }

    public void setVisitorFaceUrl(String visitorFaceUrl) {
        this.visitorFaceUrl = visitorFaceUrl;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public void setIntervieweeName(String intervieweeName) {
        this.intervieweeName = intervieweeName;
    }

    public void setIntervieweeDeptName(String intervieweeDeptName) {
        this.intervieweeDeptName = intervieweeDeptName;
    }

    public void setIntervieweeMobile(String intervieweeMobile) {
        this.intervieweeMobile = intervieweeMobile;
    }

    public void setSignTimestamp(Long signTimestamp) {
        this.signTimestamp = signTimestamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VisitRecordResponse)) {
            return false;
        }
        VisitRecordResponse other = (VisitRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$externalContactId = getExternalContactId();
        Object other$externalContactId = other.getExternalContactId();
        if (this$externalContactId == null) {
            if (other$externalContactId != null) {
                return false;
            }
        } else if (!this$externalContactId.equals(other$externalContactId)) {
            return false;
        }
        Object this$visitorName = getVisitorName();
        Object other$visitorName = other.getVisitorName();
        if (this$visitorName == null) {
            if (other$visitorName != null) {
                return false;
            }
        } else if (!this$visitorName.equals(other$visitorName)) {
            return false;
        }
        Object this$visitorIdCard = getVisitorIdCard();
        Object other$visitorIdCard = other.getVisitorIdCard();
        if (this$visitorIdCard == null) {
            if (other$visitorIdCard != null) {
                return false;
            }
        } else if (!this$visitorIdCard.equals(other$visitorIdCard)) {
            return false;
        }
        Object this$visitorFaceUrl = getVisitorFaceUrl();
        Object other$visitorFaceUrl = other.getVisitorFaceUrl();
        if (this$visitorFaceUrl == null) {
            if (other$visitorFaceUrl != null) {
                return false;
            }
        } else if (!this$visitorFaceUrl.equals(other$visitorFaceUrl)) {
            return false;
        }
        Object this$visitorMobile = getVisitorMobile();
        Object other$visitorMobile = other.getVisitorMobile();
        if (this$visitorMobile == null) {
            if (other$visitorMobile != null) {
                return false;
            }
        } else if (!this$visitorMobile.equals(other$visitorMobile)) {
            return false;
        }
        Object this$intervieweeName = getIntervieweeName();
        Object other$intervieweeName = other.getIntervieweeName();
        if (this$intervieweeName == null) {
            if (other$intervieweeName != null) {
                return false;
            }
        } else if (!this$intervieweeName.equals(other$intervieweeName)) {
            return false;
        }
        Object this$intervieweeDeptName = getIntervieweeDeptName();
        Object other$intervieweeDeptName = other.getIntervieweeDeptName();
        if (this$intervieweeDeptName == null) {
            if (other$intervieweeDeptName != null) {
                return false;
            }
        } else if (!this$intervieweeDeptName.equals(other$intervieweeDeptName)) {
            return false;
        }
        Object this$intervieweeMobile = getIntervieweeMobile();
        Object other$intervieweeMobile = other.getIntervieweeMobile();
        if (this$intervieweeMobile == null) {
            if (other$intervieweeMobile != null) {
                return false;
            }
        } else if (!this$intervieweeMobile.equals(other$intervieweeMobile)) {
            return false;
        }
        Object this$signTimestamp = getSignTimestamp();
        Object other$signTimestamp = other.getSignTimestamp();
        return this$signTimestamp == null ? other$signTimestamp == null : this$signTimestamp.equals(other$signTimestamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof VisitRecordResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $externalContactId = getExternalContactId();
        int result2 = (result * 59) + ($externalContactId == null ? 43 : $externalContactId.hashCode());
        Object $visitorName = getVisitorName();
        int result3 = (result2 * 59) + ($visitorName == null ? 43 : $visitorName.hashCode());
        Object $visitorIdCard = getVisitorIdCard();
        int result4 = (result3 * 59) + ($visitorIdCard == null ? 43 : $visitorIdCard.hashCode());
        Object $visitorFaceUrl = getVisitorFaceUrl();
        int result5 = (result4 * 59) + ($visitorFaceUrl == null ? 43 : $visitorFaceUrl.hashCode());
        Object $visitorMobile = getVisitorMobile();
        int result6 = (result5 * 59) + ($visitorMobile == null ? 43 : $visitorMobile.hashCode());
        Object $intervieweeName = getIntervieweeName();
        int result7 = (result6 * 59) + ($intervieweeName == null ? 43 : $intervieweeName.hashCode());
        Object $intervieweeDeptName = getIntervieweeDeptName();
        int result8 = (result7 * 59) + ($intervieweeDeptName == null ? 43 : $intervieweeDeptName.hashCode());
        Object $intervieweeMobile = getIntervieweeMobile();
        int result9 = (result8 * 59) + ($intervieweeMobile == null ? 43 : $intervieweeMobile.hashCode());
        Object $signTimestamp = getSignTimestamp();
        return (result9 * 59) + ($signTimestamp == null ? 43 : $signTimestamp.hashCode());
    }

    public String toString() {
        return "VisitRecordResponse(id=" + getId() + ", externalContactId=" + getExternalContactId() + ", visitorName=" + getVisitorName() + ", visitorIdCard=" + getVisitorIdCard() + ", visitorFaceUrl=" + getVisitorFaceUrl() + ", visitorMobile=" + getVisitorMobile() + ", intervieweeName=" + getIntervieweeName() + ", intervieweeDeptName=" + getIntervieweeDeptName() + ", intervieweeMobile=" + getIntervieweeMobile() + ", signTimestamp=" + getSignTimestamp() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getExternalContactId() {
        return this.externalContactId;
    }

    public String getVisitorName() {
        return this.visitorName;
    }

    public String getVisitorIdCard() {
        return this.visitorIdCard;
    }

    public String getVisitorFaceUrl() {
        return this.visitorFaceUrl;
    }

    public String getVisitorMobile() {
        return this.visitorMobile;
    }

    public String getIntervieweeName() {
        return this.intervieweeName;
    }

    public String getIntervieweeDeptName() {
        return this.intervieweeDeptName;
    }

    public String getIntervieweeMobile() {
        return this.intervieweeMobile;
    }

    public Long getSignTimestamp() {
        return this.signTimestamp;
    }
}
