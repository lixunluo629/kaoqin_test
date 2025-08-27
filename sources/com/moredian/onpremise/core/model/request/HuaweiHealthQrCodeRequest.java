package com.moredian.onpremise.core.model.request;

import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/HuaweiHealthQrCodeRequest.class */
public class HuaweiHealthQrCodeRequest implements Serializable {
    private static final long serialVersionUID = -7500474137273483376L;
    private String mffd = "嘉兴市";
    private String mzt = "绿码";
    private String sfzh;

    public void setMffd(String mffd) {
        this.mffd = mffd;
    }

    public void setMzt(String mzt) {
        this.mzt = mzt;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HuaweiHealthQrCodeRequest)) {
            return false;
        }
        HuaweiHealthQrCodeRequest other = (HuaweiHealthQrCodeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mffd = getMffd();
        Object other$mffd = other.getMffd();
        if (this$mffd == null) {
            if (other$mffd != null) {
                return false;
            }
        } else if (!this$mffd.equals(other$mffd)) {
            return false;
        }
        Object this$mzt = getMzt();
        Object other$mzt = other.getMzt();
        if (this$mzt == null) {
            if (other$mzt != null) {
                return false;
            }
        } else if (!this$mzt.equals(other$mzt)) {
            return false;
        }
        Object this$sfzh = getSfzh();
        Object other$sfzh = other.getSfzh();
        return this$sfzh == null ? other$sfzh == null : this$sfzh.equals(other$sfzh);
    }

    protected boolean canEqual(Object other) {
        return other instanceof HuaweiHealthQrCodeRequest;
    }

    public int hashCode() {
        Object $mffd = getMffd();
        int result = (1 * 59) + ($mffd == null ? 43 : $mffd.hashCode());
        Object $mzt = getMzt();
        int result2 = (result * 59) + ($mzt == null ? 43 : $mzt.hashCode());
        Object $sfzh = getSfzh();
        return (result2 * 59) + ($sfzh == null ? 43 : $sfzh.hashCode());
    }

    public String toString() {
        return "HuaweiHealthQrCodeRequest(mffd=" + getMffd() + ", mzt=" + getMzt() + ", sfzh=" + getSfzh() + ")";
    }

    public String getMffd() {
        return this.mffd;
    }

    public String getMzt() {
        return this.mzt;
    }

    public String getSfzh() {
        return this.sfzh;
    }
}
