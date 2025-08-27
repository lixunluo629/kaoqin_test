package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "授权码列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListAuthCodeResponse.class */
public class ListAuthCodeResponse implements Serializable {
    private static final long serialVersionUID = 4027193034323935212L;

    @ApiModelProperty(name = "authCode", value = "授权码")
    private String authCode;

    @ApiModelProperty(name = "allowMaxNum", value = "允许最大接入数量")
    private Integer allowMaxNum;

    @ApiModelProperty(name = "allowModule", hidden = true)
    private String allowModule;

    @ApiModelProperty(name = "orgId", hidden = true)
    private Long orgId;

    @ApiModelProperty(name = "gmtCreate", hidden = true)
    private Date gmtCreate;

    @ApiModelProperty(name = "validEndTime", value = "到期时间")
    private Long validEndTime;

    @ApiModelProperty(name = "importTime", value = "导入时间")
    private Long importTime;

    @ApiModelProperty(name = "allowModuleName", value = "开放模块名称")
    private String allowModuleName;

    @ApiModelProperty(name = "appValidInfos", value = "模块有效期")
    private List<AppValidInfoResponse> appValidInfos;

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setAllowMaxNum(Integer allowMaxNum) {
        this.allowMaxNum = allowMaxNum;
    }

    public void setAllowModule(String allowModule) {
        this.allowModule = allowModule;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setValidEndTime(Long validEndTime) {
        this.validEndTime = validEndTime;
    }

    public void setImportTime(Long importTime) {
        this.importTime = importTime;
    }

    public void setAllowModuleName(String allowModuleName) {
        this.allowModuleName = allowModuleName;
    }

    public void setAppValidInfos(List<AppValidInfoResponse> appValidInfos) {
        this.appValidInfos = appValidInfos;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAuthCodeResponse)) {
            return false;
        }
        ListAuthCodeResponse other = (ListAuthCodeResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$authCode = getAuthCode();
        Object other$authCode = other.getAuthCode();
        if (this$authCode == null) {
            if (other$authCode != null) {
                return false;
            }
        } else if (!this$authCode.equals(other$authCode)) {
            return false;
        }
        Object this$allowMaxNum = getAllowMaxNum();
        Object other$allowMaxNum = other.getAllowMaxNum();
        if (this$allowMaxNum == null) {
            if (other$allowMaxNum != null) {
                return false;
            }
        } else if (!this$allowMaxNum.equals(other$allowMaxNum)) {
            return false;
        }
        Object this$allowModule = getAllowModule();
        Object other$allowModule = other.getAllowModule();
        if (this$allowModule == null) {
            if (other$allowModule != null) {
                return false;
            }
        } else if (!this$allowModule.equals(other$allowModule)) {
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
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null) {
            if (other$gmtCreate != null) {
                return false;
            }
        } else if (!this$gmtCreate.equals(other$gmtCreate)) {
            return false;
        }
        Object this$validEndTime = getValidEndTime();
        Object other$validEndTime = other.getValidEndTime();
        if (this$validEndTime == null) {
            if (other$validEndTime != null) {
                return false;
            }
        } else if (!this$validEndTime.equals(other$validEndTime)) {
            return false;
        }
        Object this$importTime = getImportTime();
        Object other$importTime = other.getImportTime();
        if (this$importTime == null) {
            if (other$importTime != null) {
                return false;
            }
        } else if (!this$importTime.equals(other$importTime)) {
            return false;
        }
        Object this$allowModuleName = getAllowModuleName();
        Object other$allowModuleName = other.getAllowModuleName();
        if (this$allowModuleName == null) {
            if (other$allowModuleName != null) {
                return false;
            }
        } else if (!this$allowModuleName.equals(other$allowModuleName)) {
            return false;
        }
        Object this$appValidInfos = getAppValidInfos();
        Object other$appValidInfos = other.getAppValidInfos();
        return this$appValidInfos == null ? other$appValidInfos == null : this$appValidInfos.equals(other$appValidInfos);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListAuthCodeResponse;
    }

    public int hashCode() {
        Object $authCode = getAuthCode();
        int result = (1 * 59) + ($authCode == null ? 43 : $authCode.hashCode());
        Object $allowMaxNum = getAllowMaxNum();
        int result2 = (result * 59) + ($allowMaxNum == null ? 43 : $allowMaxNum.hashCode());
        Object $allowModule = getAllowModule();
        int result3 = (result2 * 59) + ($allowModule == null ? 43 : $allowModule.hashCode());
        Object $orgId = getOrgId();
        int result4 = (result3 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result5 = (result4 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $validEndTime = getValidEndTime();
        int result6 = (result5 * 59) + ($validEndTime == null ? 43 : $validEndTime.hashCode());
        Object $importTime = getImportTime();
        int result7 = (result6 * 59) + ($importTime == null ? 43 : $importTime.hashCode());
        Object $allowModuleName = getAllowModuleName();
        int result8 = (result7 * 59) + ($allowModuleName == null ? 43 : $allowModuleName.hashCode());
        Object $appValidInfos = getAppValidInfos();
        return (result8 * 59) + ($appValidInfos == null ? 43 : $appValidInfos.hashCode());
    }

    public String toString() {
        return "ListAuthCodeResponse(authCode=" + getAuthCode() + ", allowMaxNum=" + getAllowMaxNum() + ", allowModule=" + getAllowModule() + ", orgId=" + getOrgId() + ", gmtCreate=" + getGmtCreate() + ", validEndTime=" + getValidEndTime() + ", importTime=" + getImportTime() + ", allowModuleName=" + getAllowModuleName() + ", appValidInfos=" + getAppValidInfos() + ")";
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public Integer getAllowMaxNum() {
        return this.allowMaxNum;
    }

    public String getAllowModule() {
        return this.allowModule;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public Long getValidEndTime() {
        return this.validEndTime;
    }

    public Long getImportTime() {
        return this.importTime;
    }

    public String getAllowModuleName() {
        return this.allowModuleName;
    }

    public List<AppValidInfoResponse> getAppValidInfos() {
        return this.appValidInfos;
    }
}
