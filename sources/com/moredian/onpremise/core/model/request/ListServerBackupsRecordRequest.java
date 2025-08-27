package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "服务端数据备份记录列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListServerBackupsRecordRequest.class */
public class ListServerBackupsRecordRequest extends BaseRequest {
    private static final long serialVersionUID = -4103674113392660129L;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "recordName", value = "备份记录名称")
    private String recordName;

    @ApiModelProperty(name = "backupsType", value = "备份类型")
    private Integer backupsType;

    @ApiModelProperty(name = "startBackupsTimeStr", value = "发布开始时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String startBackupsTimeStr;

    @ApiModelProperty(name = "endBackupsTimeStr", value = "发布结束时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String endBackupsTimeStr;

    @ApiModelProperty(name = "startImportTimeStr", value = "导入开始时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String startImportTimeStr;

    @ApiModelProperty(name = "endImportTimeStr", value = "导入结束时间字符串，格式 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss")
    private String endImportTimeStr;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setBackupsType(Integer backupsType) {
        this.backupsType = backupsType;
    }

    public void setStartBackupsTimeStr(String startBackupsTimeStr) {
        this.startBackupsTimeStr = startBackupsTimeStr;
    }

    public void setEndBackupsTimeStr(String endBackupsTimeStr) {
        this.endBackupsTimeStr = endBackupsTimeStr;
    }

    public void setStartImportTimeStr(String startImportTimeStr) {
        this.startImportTimeStr = startImportTimeStr;
    }

    public void setEndImportTimeStr(String endImportTimeStr) {
        this.endImportTimeStr = endImportTimeStr;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListServerBackupsRecordRequest)) {
            return false;
        }
        ListServerBackupsRecordRequest other = (ListServerBackupsRecordRequest) o;
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
        Object this$recordName = getRecordName();
        Object other$recordName = other.getRecordName();
        if (this$recordName == null) {
            if (other$recordName != null) {
                return false;
            }
        } else if (!this$recordName.equals(other$recordName)) {
            return false;
        }
        Object this$backupsType = getBackupsType();
        Object other$backupsType = other.getBackupsType();
        if (this$backupsType == null) {
            if (other$backupsType != null) {
                return false;
            }
        } else if (!this$backupsType.equals(other$backupsType)) {
            return false;
        }
        Object this$startBackupsTimeStr = getStartBackupsTimeStr();
        Object other$startBackupsTimeStr = other.getStartBackupsTimeStr();
        if (this$startBackupsTimeStr == null) {
            if (other$startBackupsTimeStr != null) {
                return false;
            }
        } else if (!this$startBackupsTimeStr.equals(other$startBackupsTimeStr)) {
            return false;
        }
        Object this$endBackupsTimeStr = getEndBackupsTimeStr();
        Object other$endBackupsTimeStr = other.getEndBackupsTimeStr();
        if (this$endBackupsTimeStr == null) {
            if (other$endBackupsTimeStr != null) {
                return false;
            }
        } else if (!this$endBackupsTimeStr.equals(other$endBackupsTimeStr)) {
            return false;
        }
        Object this$startImportTimeStr = getStartImportTimeStr();
        Object other$startImportTimeStr = other.getStartImportTimeStr();
        if (this$startImportTimeStr == null) {
            if (other$startImportTimeStr != null) {
                return false;
            }
        } else if (!this$startImportTimeStr.equals(other$startImportTimeStr)) {
            return false;
        }
        Object this$endImportTimeStr = getEndImportTimeStr();
        Object other$endImportTimeStr = other.getEndImportTimeStr();
        return this$endImportTimeStr == null ? other$endImportTimeStr == null : this$endImportTimeStr.equals(other$endImportTimeStr);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListServerBackupsRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $recordName = getRecordName();
        int result2 = (result * 59) + ($recordName == null ? 43 : $recordName.hashCode());
        Object $backupsType = getBackupsType();
        int result3 = (result2 * 59) + ($backupsType == null ? 43 : $backupsType.hashCode());
        Object $startBackupsTimeStr = getStartBackupsTimeStr();
        int result4 = (result3 * 59) + ($startBackupsTimeStr == null ? 43 : $startBackupsTimeStr.hashCode());
        Object $endBackupsTimeStr = getEndBackupsTimeStr();
        int result5 = (result4 * 59) + ($endBackupsTimeStr == null ? 43 : $endBackupsTimeStr.hashCode());
        Object $startImportTimeStr = getStartImportTimeStr();
        int result6 = (result5 * 59) + ($startImportTimeStr == null ? 43 : $startImportTimeStr.hashCode());
        Object $endImportTimeStr = getEndImportTimeStr();
        return (result6 * 59) + ($endImportTimeStr == null ? 43 : $endImportTimeStr.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListServerBackupsRecordRequest(paginator=" + getPaginator() + ", recordName=" + getRecordName() + ", backupsType=" + getBackupsType() + ", startBackupsTimeStr=" + getStartBackupsTimeStr() + ", endBackupsTimeStr=" + getEndBackupsTimeStr() + ", startImportTimeStr=" + getStartImportTimeStr() + ", endImportTimeStr=" + getEndImportTimeStr() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getRecordName() {
        return this.recordName;
    }

    public Integer getBackupsType() {
        return this.backupsType;
    }

    public String getStartBackupsTimeStr() {
        return this.startBackupsTimeStr;
    }

    public String getEndBackupsTimeStr() {
        return this.endBackupsTimeStr;
    }

    public String getStartImportTimeStr() {
        return this.startImportTimeStr;
    }

    public String getEndImportTimeStr() {
        return this.endImportTimeStr;
    }
}
