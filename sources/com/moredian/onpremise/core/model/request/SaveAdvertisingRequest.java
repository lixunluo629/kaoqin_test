package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.info.AdvertistingShowTimeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "保存广告配置")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAdvertisingRequest.class */
public class SaveAdvertisingRequest extends BaseRequest {
    private static final long serialVersionUID = -1012173745972779509L;

    @ApiModelProperty(name = "advertisingPictureBase64List", value = "广告图片base64集合")
    private List<String> advertisingPictureBase64List;

    @ApiModelProperty(name = "switchTime", value = "幻灯片展示图片切换间隔时间，单位：秒")
    private Integer switchTime;

    @ApiModelProperty(name = "showTimeInfos", value = "播放时间段")
    private List<AdvertistingShowTimeInfo> showTimeInfos;

    public void setAdvertisingPictureBase64List(List<String> advertisingPictureBase64List) {
        this.advertisingPictureBase64List = advertisingPictureBase64List;
    }

    public void setSwitchTime(Integer switchTime) {
        this.switchTime = switchTime;
    }

    public void setShowTimeInfos(List<AdvertistingShowTimeInfo> showTimeInfos) {
        this.showTimeInfos = showTimeInfos;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAdvertisingRequest)) {
            return false;
        }
        SaveAdvertisingRequest other = (SaveAdvertisingRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$advertisingPictureBase64List = getAdvertisingPictureBase64List();
        Object other$advertisingPictureBase64List = other.getAdvertisingPictureBase64List();
        if (this$advertisingPictureBase64List == null) {
            if (other$advertisingPictureBase64List != null) {
                return false;
            }
        } else if (!this$advertisingPictureBase64List.equals(other$advertisingPictureBase64List)) {
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

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAdvertisingRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $advertisingPictureBase64List = getAdvertisingPictureBase64List();
        int result = (1 * 59) + ($advertisingPictureBase64List == null ? 43 : $advertisingPictureBase64List.hashCode());
        Object $switchTime = getSwitchTime();
        int result2 = (result * 59) + ($switchTime == null ? 43 : $switchTime.hashCode());
        Object $showTimeInfos = getShowTimeInfos();
        return (result2 * 59) + ($showTimeInfos == null ? 43 : $showTimeInfos.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAdvertisingRequest(advertisingPictureBase64List=" + getAdvertisingPictureBase64List() + ", switchTime=" + getSwitchTime() + ", showTimeInfos=" + getShowTimeInfos() + ")";
    }

    public List<String> getAdvertisingPictureBase64List() {
        return this.advertisingPictureBase64List;
    }

    public Integer getSwitchTime() {
        return this.switchTime;
    }

    public List<AdvertistingShowTimeInfo> getShowTimeInfos() {
        return this.showTimeInfos;
    }
}
