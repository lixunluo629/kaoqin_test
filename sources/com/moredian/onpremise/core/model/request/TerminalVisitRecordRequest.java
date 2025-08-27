package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "访客记录参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalVisitRecordRequest.class */
public class TerminalVisitRecordRequest implements Serializable {
    private static final long serialVersionUID = -4661258532726001914L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId = 1L;

    @ApiModelProperty(name = "visitorName", value = "访客姓名，必填")
    private String visitorName;

    @ApiModelProperty(name = "visitorIdCard", value = "来访者身份证，必填")
    private String visitorIdCard;

    @ApiModelProperty(name = "visitorFaceUrl", value = "来访者人脸照片url，必填")
    private String visitorFaceUrl;

    @ApiModelProperty(name = "visitorMobile", value = "来访者手机号，必填")
    private String visitorMobile;

    @ApiModelProperty(name = "intervieweeMemberId", value = "被访者memberid", hidden = true)
    private Long intervieweeMemberId;

    @ApiModelProperty(name = "intervieweeName", value = "被访者姓名")
    private String intervieweeName;

    @ApiModelProperty(name = "intervieweeMobile", value = "被访者手机号")
    private String intervieweeMobile;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public void setIntervieweeMemberId(Long intervieweeMemberId) {
        this.intervieweeMemberId = intervieweeMemberId;
    }

    public void setIntervieweeName(String intervieweeName) {
        this.intervieweeName = intervieweeName;
    }

    public void setIntervieweeMobile(String intervieweeMobile) {
        this.intervieweeMobile = intervieweeMobile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalVisitRecordRequest)) {
            return false;
        }
        TerminalVisitRecordRequest other = (TerminalVisitRecordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
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
        Object this$intervieweeMemberId = getIntervieweeMemberId();
        Object other$intervieweeMemberId = other.getIntervieweeMemberId();
        if (this$intervieweeMemberId == null) {
            if (other$intervieweeMemberId != null) {
                return false;
            }
        } else if (!this$intervieweeMemberId.equals(other$intervieweeMemberId)) {
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
        Object this$intervieweeMobile = getIntervieweeMobile();
        Object other$intervieweeMobile = other.getIntervieweeMobile();
        return this$intervieweeMobile == null ? other$intervieweeMobile == null : this$intervieweeMobile.equals(other$intervieweeMobile);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalVisitRecordRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $visitorName = getVisitorName();
        int result2 = (result * 59) + ($visitorName == null ? 43 : $visitorName.hashCode());
        Object $visitorIdCard = getVisitorIdCard();
        int result3 = (result2 * 59) + ($visitorIdCard == null ? 43 : $visitorIdCard.hashCode());
        Object $visitorFaceUrl = getVisitorFaceUrl();
        int result4 = (result3 * 59) + ($visitorFaceUrl == null ? 43 : $visitorFaceUrl.hashCode());
        Object $visitorMobile = getVisitorMobile();
        int result5 = (result4 * 59) + ($visitorMobile == null ? 43 : $visitorMobile.hashCode());
        Object $intervieweeMemberId = getIntervieweeMemberId();
        int result6 = (result5 * 59) + ($intervieweeMemberId == null ? 43 : $intervieweeMemberId.hashCode());
        Object $intervieweeName = getIntervieweeName();
        int result7 = (result6 * 59) + ($intervieweeName == null ? 43 : $intervieweeName.hashCode());
        Object $intervieweeMobile = getIntervieweeMobile();
        return (result7 * 59) + ($intervieweeMobile == null ? 43 : $intervieweeMobile.hashCode());
    }

    public String toString() {
        return "TerminalVisitRecordRequest(orgId=" + getOrgId() + ", visitorName=" + getVisitorName() + ", visitorIdCard=" + getVisitorIdCard() + ", visitorFaceUrl=" + getVisitorFaceUrl() + ", visitorMobile=" + getVisitorMobile() + ", intervieweeMemberId=" + getIntervieweeMemberId() + ", intervieweeName=" + getIntervieweeName() + ", intervieweeMobile=" + getIntervieweeMobile() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
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

    public Long getIntervieweeMemberId() {
        return this.intervieweeMemberId;
    }

    public String getIntervieweeName() {
        return this.intervieweeName;
    }

    public String getIntervieweeMobile() {
        return this.intervieweeMobile;
    }
}
