package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "升级任务列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListServerScheduleRequest.class */
public class ListServerScheduleRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "packageName", value = "升级包名")
    private String packageName;

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY, value = "版本号")
    private String versionCode;

    @ApiModelProperty(name = "startImportStr", value = "导入开始时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String startImportStr;

    @ApiModelProperty(name = "endImportStr", value = "导入结束时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String endImportStr;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setStartImportStr(String startImportStr) {
        this.startImportStr = startImportStr;
    }

    public void setEndImportStr(String endImportStr) {
        this.endImportStr = endImportStr;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListServerScheduleRequest)) {
            return false;
        }
        ListServerScheduleRequest other = (ListServerScheduleRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$packageName = getPackageName();
        Object other$packageName = other.getPackageName();
        if (this$packageName == null) {
            if (other$packageName != null) {
                return false;
            }
        } else if (!this$packageName.equals(other$packageName)) {
            return false;
        }
        Object this$versionCode = getVersionCode();
        Object other$versionCode = other.getVersionCode();
        if (this$versionCode == null) {
            if (other$versionCode != null) {
                return false;
            }
        } else if (!this$versionCode.equals(other$versionCode)) {
            return false;
        }
        Object this$startImportStr = getStartImportStr();
        Object other$startImportStr = other.getStartImportStr();
        if (this$startImportStr == null) {
            if (other$startImportStr != null) {
                return false;
            }
        } else if (!this$startImportStr.equals(other$startImportStr)) {
            return false;
        }
        Object this$endImportStr = getEndImportStr();
        Object other$endImportStr = other.getEndImportStr();
        return this$endImportStr == null ? other$endImportStr == null : this$endImportStr.equals(other$endImportStr);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListServerScheduleRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $packageName = getPackageName();
        int result2 = (result * 59) + ($packageName == null ? 43 : $packageName.hashCode());
        Object $versionCode = getVersionCode();
        int result3 = (result2 * 59) + ($versionCode == null ? 43 : $versionCode.hashCode());
        Object $startImportStr = getStartImportStr();
        int result4 = (result3 * 59) + ($startImportStr == null ? 43 : $startImportStr.hashCode());
        Object $endImportStr = getEndImportStr();
        return (result4 * 59) + ($endImportStr == null ? 43 : $endImportStr.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListServerScheduleRequest(paginator=" + getPaginator() + ", packageName=" + getPackageName() + ", versionCode=" + getVersionCode() + ", startImportStr=" + getStartImportStr() + ", endImportStr=" + getEndImportStr() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public String getStartImportStr() {
        return this.startImportStr;
    }

    public String getEndImportStr() {
        return this.endImportStr;
    }
}
