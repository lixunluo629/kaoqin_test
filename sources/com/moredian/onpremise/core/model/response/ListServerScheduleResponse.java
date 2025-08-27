package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "服务端升级任务列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListServerScheduleResponse.class */
public class ListServerScheduleResponse implements Serializable {
    private static final long serialVersionUID = 6393510766631899564L;

    @ApiModelProperty(name = "upgradeServerScheduleId", value = "任务id")
    private Long upgradeServerScheduleId;

    @ApiModelProperty(name = "upgradePackageName", value = "发布升级包名")
    private String upgradePackageName;

    @ApiModelProperty(name = "upgradeVersion", value = "发布版本")
    private String upgradeVersion;

    @ApiModelProperty(name = "upgradeReleaseTime", value = "发布时间字符串，yyyy-MM-dd格式")
    private String upgradeReleaseTime;

    @ApiModelProperty(name = "importTimeMillis", value = "导入时间时间戳")
    private Long importTimeMillis;

    public void setUpgradeServerScheduleId(Long upgradeServerScheduleId) {
        this.upgradeServerScheduleId = upgradeServerScheduleId;
    }

    public void setUpgradePackageName(String upgradePackageName) {
        this.upgradePackageName = upgradePackageName;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public void setUpgradeReleaseTime(String upgradeReleaseTime) {
        this.upgradeReleaseTime = upgradeReleaseTime;
    }

    public void setImportTimeMillis(Long importTimeMillis) {
        this.importTimeMillis = importTimeMillis;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListServerScheduleResponse)) {
            return false;
        }
        ListServerScheduleResponse other = (ListServerScheduleResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$upgradeServerScheduleId = getUpgradeServerScheduleId();
        Object other$upgradeServerScheduleId = other.getUpgradeServerScheduleId();
        if (this$upgradeServerScheduleId == null) {
            if (other$upgradeServerScheduleId != null) {
                return false;
            }
        } else if (!this$upgradeServerScheduleId.equals(other$upgradeServerScheduleId)) {
            return false;
        }
        Object this$upgradePackageName = getUpgradePackageName();
        Object other$upgradePackageName = other.getUpgradePackageName();
        if (this$upgradePackageName == null) {
            if (other$upgradePackageName != null) {
                return false;
            }
        } else if (!this$upgradePackageName.equals(other$upgradePackageName)) {
            return false;
        }
        Object this$upgradeVersion = getUpgradeVersion();
        Object other$upgradeVersion = other.getUpgradeVersion();
        if (this$upgradeVersion == null) {
            if (other$upgradeVersion != null) {
                return false;
            }
        } else if (!this$upgradeVersion.equals(other$upgradeVersion)) {
            return false;
        }
        Object this$upgradeReleaseTime = getUpgradeReleaseTime();
        Object other$upgradeReleaseTime = other.getUpgradeReleaseTime();
        if (this$upgradeReleaseTime == null) {
            if (other$upgradeReleaseTime != null) {
                return false;
            }
        } else if (!this$upgradeReleaseTime.equals(other$upgradeReleaseTime)) {
            return false;
        }
        Object this$importTimeMillis = getImportTimeMillis();
        Object other$importTimeMillis = other.getImportTimeMillis();
        return this$importTimeMillis == null ? other$importTimeMillis == null : this$importTimeMillis.equals(other$importTimeMillis);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListServerScheduleResponse;
    }

    public int hashCode() {
        Object $upgradeServerScheduleId = getUpgradeServerScheduleId();
        int result = (1 * 59) + ($upgradeServerScheduleId == null ? 43 : $upgradeServerScheduleId.hashCode());
        Object $upgradePackageName = getUpgradePackageName();
        int result2 = (result * 59) + ($upgradePackageName == null ? 43 : $upgradePackageName.hashCode());
        Object $upgradeVersion = getUpgradeVersion();
        int result3 = (result2 * 59) + ($upgradeVersion == null ? 43 : $upgradeVersion.hashCode());
        Object $upgradeReleaseTime = getUpgradeReleaseTime();
        int result4 = (result3 * 59) + ($upgradeReleaseTime == null ? 43 : $upgradeReleaseTime.hashCode());
        Object $importTimeMillis = getImportTimeMillis();
        return (result4 * 59) + ($importTimeMillis == null ? 43 : $importTimeMillis.hashCode());
    }

    public String toString() {
        return "ListServerScheduleResponse(upgradeServerScheduleId=" + getUpgradeServerScheduleId() + ", upgradePackageName=" + getUpgradePackageName() + ", upgradeVersion=" + getUpgradeVersion() + ", upgradeReleaseTime=" + getUpgradeReleaseTime() + ", importTimeMillis=" + getImportTimeMillis() + ")";
    }

    public Long getUpgradeServerScheduleId() {
        return this.upgradeServerScheduleId;
    }

    public String getUpgradePackageName() {
        return this.upgradePackageName;
    }

    public String getUpgradeVersion() {
        return this.upgradeVersion;
    }

    public String getUpgradeReleaseTime() {
        return this.upgradeReleaseTime;
    }

    public Long getImportTimeMillis() {
        return this.importTimeMillis;
    }
}
