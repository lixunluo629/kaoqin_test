package com.moredian.onpremise.core.model.info;

import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/UploadAndUnpackInfo.class */
public class UploadAndUnpackInfo implements Serializable {
    private String recordName;
    private String backupsTimeStamp;
    private String backupsType;

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setBackupsTimeStamp(String backupsTimeStamp) {
        this.backupsTimeStamp = backupsTimeStamp;
    }

    public void setBackupsType(String backupsType) {
        this.backupsType = backupsType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadAndUnpackInfo)) {
            return false;
        }
        UploadAndUnpackInfo other = (UploadAndUnpackInfo) o;
        if (!other.canEqual(this)) {
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
        Object this$backupsTimeStamp = getBackupsTimeStamp();
        Object other$backupsTimeStamp = other.getBackupsTimeStamp();
        if (this$backupsTimeStamp == null) {
            if (other$backupsTimeStamp != null) {
                return false;
            }
        } else if (!this$backupsTimeStamp.equals(other$backupsTimeStamp)) {
            return false;
        }
        Object this$backupsType = getBackupsType();
        Object other$backupsType = other.getBackupsType();
        return this$backupsType == null ? other$backupsType == null : this$backupsType.equals(other$backupsType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadAndUnpackInfo;
    }

    public int hashCode() {
        Object $recordName = getRecordName();
        int result = (1 * 59) + ($recordName == null ? 43 : $recordName.hashCode());
        Object $backupsTimeStamp = getBackupsTimeStamp();
        int result2 = (result * 59) + ($backupsTimeStamp == null ? 43 : $backupsTimeStamp.hashCode());
        Object $backupsType = getBackupsType();
        return (result2 * 59) + ($backupsType == null ? 43 : $backupsType.hashCode());
    }

    public String toString() {
        return "UploadAndUnpackInfo(recordName=" + getRecordName() + ", backupsTimeStamp=" + getBackupsTimeStamp() + ", backupsType=" + getBackupsType() + ")";
    }

    public String getRecordName() {
        return this.recordName;
    }

    public String getBackupsTimeStamp() {
        return this.backupsTimeStamp;
    }

    public String getBackupsType() {
        return this.backupsType;
    }
}
