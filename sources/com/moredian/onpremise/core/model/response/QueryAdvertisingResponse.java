package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.info.AdvertistingShowTimeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "广告配置信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryAdvertisingResponse.class */
public class QueryAdvertisingResponse implements Serializable {
    private static final long serialVersionUID = -1483549728837338580L;

    @ApiModelProperty(name = "advertisingPictureUrls", value = "广告图片url")
    private List<String> advertisingPictureUrls;

    @ApiModelProperty(name = "switchTime", value = "幻灯片展示图片切换间隔时间，单位：秒")
    private Integer switchTime;

    @ApiModelProperty(name = "showTimeInfos", value = "播放时间段")
    private List<AdvertistingShowTimeInfo> showTimeInfos;

    public void setAdvertisingPictureUrls(List<String> advertisingPictureUrls) {
        this.advertisingPictureUrls = advertisingPictureUrls;
    }

    public void setSwitchTime(Integer switchTime) {
        this.switchTime = switchTime;
    }

    public void setShowTimeInfos(List<AdvertistingShowTimeInfo> showTimeInfos) {
        this.showTimeInfos = showTimeInfos;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryAdvertisingResponse)) {
            return false;
        }
        QueryAdvertisingResponse other = (QueryAdvertisingResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$advertisingPictureUrls = getAdvertisingPictureUrls();
        Object other$advertisingPictureUrls = other.getAdvertisingPictureUrls();
        if (this$advertisingPictureUrls == null) {
            if (other$advertisingPictureUrls != null) {
                return false;
            }
        } else if (!this$advertisingPictureUrls.equals(other$advertisingPictureUrls)) {
            return false;
        }
        Object this$switchTime = getSwitchTime();
        Object other$switchTime = other.getSwitchTime();
        if (this$switchTime == null) {
            if (other$switchTime != null) {
                return false;
            }
        } else if (!this$switchTime.equals(other$switchTime)) {
            return false;
        }
        Object this$showTimeInfos = getShowTimeInfos();
        Object other$showTimeInfos = other.getShowTimeInfos();
        return this$showTimeInfos == null ? other$showTimeInfos == null : this$showTimeInfos.equals(other$showTimeInfos);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryAdvertisingResponse;
    }

    public int hashCode() {
        Object $advertisingPictureUrls = getAdvertisingPictureUrls();
        int result = (1 * 59) + ($advertisingPictureUrls == null ? 43 : $advertisingPictureUrls.hashCode());
        Object $switchTime = getSwitchTime();
        int result2 = (result * 59) + ($switchTime == null ? 43 : $switchTime.hashCode());
        Object $showTimeInfos = getShowTimeInfos();
        return (result2 * 59) + ($showTimeInfos == null ? 43 : $showTimeInfos.hashCode());
    }

    public String toString() {
        return "QueryAdvertisingResponse(advertisingPictureUrls=" + getAdvertisingPictureUrls() + ", switchTime=" + getSwitchTime() + ", showTimeInfos=" + getShowTimeInfos() + ")";
    }

    public List<String> getAdvertisingPictureUrls() {
        return this.advertisingPictureUrls;
    }

    public Integer getSwitchTime() {
        return this.switchTime;
    }

    public List<AdvertistingShowTimeInfo> getShowTimeInfos() {
        return this.showTimeInfos;
    }
}
