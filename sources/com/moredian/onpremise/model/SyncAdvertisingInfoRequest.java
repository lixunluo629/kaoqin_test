package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncAdvertisingInfoRequest.class */
public class SyncAdvertisingInfoRequest implements Serializable {
    private static final long serialVersionUID = -2941474128946377558L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_ADVERTISING_INFO_REQUEST;
    private String advertisingPictureUrls;

    public void setAdvertisingPictureUrls(String advertisingPictureUrls) {
        this.advertisingPictureUrls = advertisingPictureUrls;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncAdvertisingInfoRequest)) {
            return false;
        }
        SyncAdvertisingInfoRequest other = (SyncAdvertisingInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$advertisingPictureUrls = getAdvertisingPictureUrls();
        Object other$advertisingPictureUrls = other.getAdvertisingPictureUrls();
        return this$advertisingPictureUrls == null ? other$advertisingPictureUrls == null : this$advertisingPictureUrls.equals(other$advertisingPictureUrls);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncAdvertisingInfoRequest;
    }

    public int hashCode() {
        Object $advertisingPictureUrls = getAdvertisingPictureUrls();
        int result = (1 * 59) + ($advertisingPictureUrls == null ? 43 : $advertisingPictureUrls.hashCode());
        return result;
    }

    public String toString() {
        return "SyncAdvertisingInfoRequest(advertisingPictureUrls=" + getAdvertisingPictureUrls() + ")";
    }

    public String getAdvertisingPictureUrls() {
        return this.advertisingPictureUrls;
    }

    public SyncAdvertisingInfoRequest() {
    }

    public SyncAdvertisingInfoRequest(String advertisingPictureUrls) {
        this.advertisingPictureUrls = advertisingPictureUrls;
    }
}
