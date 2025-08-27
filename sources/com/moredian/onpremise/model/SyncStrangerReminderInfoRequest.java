package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncStrangerReminderInfoRequest.class */
public class SyncStrangerReminderInfoRequest implements Serializable {
    private static final long serialVersionUID = -11105741279622578L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_STRANGER_REMINDER_INFO_REQUEST;
    private Long lastModifyTime;
    private Integer frame;
    private String showReminderInfo;
    private String speechReminderInfo;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public void setShowReminderInfo(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
    }

    public void setSpeechReminderInfo(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncStrangerReminderInfoRequest)) {
            return false;
        }
        SyncStrangerReminderInfoRequest other = (SyncStrangerReminderInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$lastModifyTime = getLastModifyTime();
        Object other$lastModifyTime = other.getLastModifyTime();
        if (this$lastModifyTime == null) {
            if (other$lastModifyTime != null) {
                return false;
            }
        } else if (!this$lastModifyTime.equals(other$lastModifyTime)) {
            return false;
        }
        Object this$frame = getFrame();
        Object other$frame = other.getFrame();
        if (this$frame == null) {
            if (other$frame != null) {
                return false;
            }
        } else if (!this$frame.equals(other$frame)) {
            return false;
        }
        Object this$showReminderInfo = getShowReminderInfo();
        Object other$showReminderInfo = other.getShowReminderInfo();
        if (this$showReminderInfo == null) {
            if (other$showReminderInfo != null) {
                return false;
            }
        } else if (!this$showReminderInfo.equals(other$showReminderInfo)) {
            return false;
        }
        Object this$speechReminderInfo = getSpeechReminderInfo();
        Object other$speechReminderInfo = other.getSpeechReminderInfo();
        return this$speechReminderInfo == null ? other$speechReminderInfo == null : this$speechReminderInfo.equals(other$speechReminderInfo);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncStrangerReminderInfoRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $frame = getFrame();
        int result2 = (result * 59) + ($frame == null ? 43 : $frame.hashCode());
        Object $showReminderInfo = getShowReminderInfo();
        int result3 = (result2 * 59) + ($showReminderInfo == null ? 43 : $showReminderInfo.hashCode());
        Object $speechReminderInfo = getSpeechReminderInfo();
        return (result3 * 59) + ($speechReminderInfo == null ? 43 : $speechReminderInfo.hashCode());
    }

    public String toString() {
        return "SyncStrangerReminderInfoRequest(lastModifyTime=" + getLastModifyTime() + ", frame=" + getFrame() + ", showReminderInfo=" + getShowReminderInfo() + ", speechReminderInfo=" + getSpeechReminderInfo() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public Integer getFrame() {
        return this.frame;
    }

    public String getShowReminderInfo() {
        return this.showReminderInfo;
    }

    public String getSpeechReminderInfo() {
        return this.speechReminderInfo;
    }

    public SyncStrangerReminderInfoRequest() {
    }

    public SyncStrangerReminderInfoRequest(Integer frame, String showReminderInfo, String speechReminderInfo, Long lastModifyTime) {
        this.frame = frame;
        this.showReminderInfo = showReminderInfo;
        this.speechReminderInfo = speechReminderInfo;
        this.lastModifyTime = lastModifyTime;
    }
}
