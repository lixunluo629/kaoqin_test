package com.moredian.onpremise.core.model.info;

import java.io.Serializable;
import java.util.List;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/LicenseDetailInfo.class */
public class LicenseDetailInfo implements Serializable {
    private static final long serialVersionUID = 5464495249619397034L;
    private String orgCode;
    private Integer maxNum;
    private Long startTime;
    private Long endTime;
    private String moduleTypes;
    private Long generateTime;
    private List<LicenseDetailAppInfo> licenseDetailApps;
    private String version;
    private String description;

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setModuleTypes(String moduleTypes) {
        this.moduleTypes = moduleTypes;
    }

    public void setGenerateTime(Long generateTime) {
        this.generateTime = generateTime;
    }

    public void setLicenseDetailApps(List<LicenseDetailAppInfo> licenseDetailApps) {
        this.licenseDetailApps = licenseDetailApps;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LicenseDetailInfo)) {
            return false;
        }
        LicenseDetailInfo other = (LicenseDetailInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgCode = getOrgCode();
        Object other$orgCode = other.getOrgCode();
        if (this$orgCode == null) {
            if (other$orgCode != null) {
                return false;
            }
        } else if (!this$orgCode.equals(other$orgCode)) {
            return false;
        }
        Object this$maxNum = getMaxNum();
        Object other$maxNum = other.getMaxNum();
        if (this$maxNum == null) {
            if (other$maxNum != null) {
                return false;
            }
        } else if (!this$maxNum.equals(other$maxNum)) {
            return false;
        }
        Object this$startTime = getStartTime();
        Object other$startTime = other.getStartTime();
        if (this$startTime == null) {
            if (other$startTime != null) {
                return false;
            }
        } else if (!this$startTime.equals(other$startTime)) {
            return false;
        }
        Object this$endTime = getEndTime();
        Object other$endTime = other.getEndTime();
        if (this$endTime == null) {
            if (other$endTime != null) {
                return false;
            }
        } else if (!this$endTime.equals(other$endTime)) {
            return false;
        }
        Object this$moduleTypes = getModuleTypes();
        Object other$moduleTypes = other.getModuleTypes();
        if (this$moduleTypes == null) {
            if (other$moduleTypes != null) {
                return false;
            }
        } else if (!this$moduleTypes.equals(other$moduleTypes)) {
            return false;
        }
        Object this$generateTime = getGenerateTime();
        Object other$generateTime = other.getGenerateTime();
        if (this$generateTime == null) {
            if (other$generateTime != null) {
                return false;
            }
        } else if (!this$generateTime.equals(other$generateTime)) {
            return false;
        }
        Object this$licenseDetailApps = getLicenseDetailApps();
        Object other$licenseDetailApps = other.getLicenseDetailApps();
        if (this$licenseDetailApps == null) {
            if (other$licenseDetailApps != null) {
                return false;
            }
        } else if (!this$licenseDetailApps.equals(other$licenseDetailApps)) {
            return false;
        }
        Object this$version = getVersion();
        Object other$version = other.getVersion();
        if (this$version == null) {
            if (other$version != null) {
                return false;
            }
        } else if (!this$version.equals(other$version)) {
            return false;
        }
        Object this$description = getDescription();
        Object other$description = other.getDescription();
        return this$description == null ? other$description == null : this$description.equals(other$description);
    }

    protected boolean canEqual(Object other) {
        return other instanceof LicenseDetailInfo;
    }

    public int hashCode() {
        Object $orgCode = getOrgCode();
        int result = (1 * 59) + ($orgCode == null ? 43 : $orgCode.hashCode());
        Object $maxNum = getMaxNum();
        int result2 = (result * 59) + ($maxNum == null ? 43 : $maxNum.hashCode());
        Object $startTime = getStartTime();
        int result3 = (result2 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result4 = (result3 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $moduleTypes = getModuleTypes();
        int result5 = (result4 * 59) + ($moduleTypes == null ? 43 : $moduleTypes.hashCode());
        Object $generateTime = getGenerateTime();
        int result6 = (result5 * 59) + ($generateTime == null ? 43 : $generateTime.hashCode());
        Object $licenseDetailApps = getLicenseDetailApps();
        int result7 = (result6 * 59) + ($licenseDetailApps == null ? 43 : $licenseDetailApps.hashCode());
        Object $version = getVersion();
        int result8 = (result7 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $description = getDescription();
        return (result8 * 59) + ($description == null ? 43 : $description.hashCode());
    }

    public String toString() {
        return "LicenseDetailInfo(orgCode=" + getOrgCode() + ", maxNum=" + getMaxNum() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", moduleTypes=" + getModuleTypes() + ", generateTime=" + getGenerateTime() + ", licenseDetailApps=" + getLicenseDetailApps() + ", version=" + getVersion() + ", description=" + getDescription() + ")";
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public Integer getMaxNum() {
        return this.maxNum;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public String getModuleTypes() {
        return this.moduleTypes;
    }

    public Long getGenerateTime() {
        return this.generateTime;
    }

    public List<LicenseDetailAppInfo> getLicenseDetailApps() {
        return this.licenseDetailApps;
    }

    public String getVersion() {
        return this.version;
    }

    public String getDescription() {
        return this.description;
    }
}
