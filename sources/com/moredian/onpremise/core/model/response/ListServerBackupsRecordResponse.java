package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "服务端数据备份记录列表响应数据")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListServerBackupsRecordResponse.class */
public class ListServerBackupsRecordResponse implements Serializable {
    private Long backupsDataId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "recordName", value = "备份数据包名称")
    private String recordName;

    @ApiModelProperty(name = "backupsUrl", value = "备份数据包下载地址")
    private String backupsUrl;

    @ApiModelProperty(name = "backupsTime", value = "备份时间戳")
    private Long backupsTime;

    @ApiModelProperty(name = "backupsType", value = "备份类型：1-手动备份，2-自动备份")
    private Integer backupsType;

    @ApiModelProperty(name = "importTime", value = "创建时间戳")
    private Long importTime;

    public void setBackupsDataId(Long backupsDataId) {
        this.backupsDataId = backupsDataId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setBackupsUrl(String backupsUrl) {
        this.backupsUrl = backupsUrl;
    }

    public void setBackupsTime(Long backupsTime) {
        this.backupsTime = backupsTime;
    }

    public void setBackupsType(Integer backupsType) {
        this.backupsType = backupsType;
    }

    public void setImportTime(Long importTime) {
        this.importTime = importTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListServerBackupsRecordResponse)) {
            return false;
        }
        ListServerBackupsRecordResponse other = (ListServerBackupsRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$backupsDataId = getBackupsDataId();
        Object other$backupsDataId = other.getBackupsDataId();
        if (this$backupsDataId == null) {
            if (other$backupsDataId != null) {
                return false;
            }
        } else if (!this$backupsDataId.equals(other$backupsDataId)) {
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
        Object this$recordName = getRecordName();
        Object other$recordName = other.getRecordName();
        if (this$recordName == null) {
            if (other$recordName != null) {
                return false;
            }
        } else if (!this$recordName.equals(other$recordName)) {
            return false;
        }
        Object this$backupsUrl = getBackupsUrl();
        Object other$backupsUrl = other.getBackupsUrl();
        if (this$backupsUrl == null) {
            if (other$backupsUrl != null) {
                return false;
            }
        } else if (!this$backupsUrl.equals(other$backupsUrl)) {
            return false;
        }
        Object this$backupsTime = getBackupsTime();
        Object other$backupsTime = other.getBackupsTime();
        if (this$backupsTime == null) {
            if (other$backupsTime != null) {
                return false;
            }
        } else if (!this$backupsTime.equals(other$backupsTime)) {
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
        Object this$importTime = getImportTime();
        Object other$importTime = other.getImportTime();
        return this$importTime == null ? other$importTime == null : this$importTime.equals(other$importTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListServerBackupsRecordResponse;
    }

    public int hashCode() {
        Object $backupsDataId = getBackupsDataId();
        int result = (1 * 59) + ($backupsDataId == null ? 43 : $backupsDataId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $recordName = getRecordName();
        int result3 = (result2 * 59) + ($recordName == null ? 43 : $recordName.hashCode());
        Object $backupsUrl = getBackupsUrl();
        int result4 = (result3 * 59) + ($backupsUrl == null ? 43 : $backupsUrl.hashCode());
        Object $backupsTime = getBackupsTime();
        int result5 = (result4 * 59) + ($backupsTime == null ? 43 : $backupsTime.hashCode());
        Object $backupsType = getBackupsType();
        int result6 = (result5 * 59) + ($backupsType == null ? 43 : $backupsType.hashCode());
        Object $importTime = getImportTime();
        return (result6 * 59) + ($importTime == null ? 43 : $importTime.hashCode());
    }

    public String toString() {
        return "ListServerBackupsRecordResponse(backupsDataId=" + getBackupsDataId() + ", orgId=" + getOrgId() + ", recordName=" + getRecordName() + ", backupsUrl=" + getBackupsUrl() + ", backupsTime=" + getBackupsTime() + ", backupsType=" + getBackupsType() + ", importTime=" + getImportTime() + ")";
    }

    public Long getBackupsDataId() {
        return this.backupsDataId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getRecordName() {
        return this.recordName;
    }

    public String getBackupsUrl() {
        return this.backupsUrl;
    }

    public Long getBackupsTime() {
        return this.backupsTime;
    }

    public Integer getBackupsType() {
        return this.backupsType;
    }

    public Long getImportTime() {
        return this.importTime;
    }
}
