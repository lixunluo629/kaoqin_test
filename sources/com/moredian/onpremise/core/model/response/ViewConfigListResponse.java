package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "页面配置列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ViewConfigListResponse.class */
public class ViewConfigListResponse implements Serializable {
    private static final long serialVersionUID = -2638106452718316141L;
    private List<ViewConfigResponse> viewConfigResponseList;

    public void setViewConfigResponseList(List<ViewConfigResponse> viewConfigResponseList) {
        this.viewConfigResponseList = viewConfigResponseList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewConfigListResponse)) {
            return false;
        }
        ViewConfigListResponse other = (ViewConfigListResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$viewConfigResponseList = getViewConfigResponseList();
        Object other$viewConfigResponseList = other.getViewConfigResponseList();
        return this$viewConfigResponseList == null ? other$viewConfigResponseList == null : this$viewConfigResponseList.equals(other$viewConfigResponseList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ViewConfigListResponse;
    }

    public int hashCode() {
        Object $viewConfigResponseList = getViewConfigResponseList();
        int result = (1 * 59) + ($viewConfigResponseList == null ? 43 : $viewConfigResponseList.hashCode());
        return result;
    }

    public String toString() {
        return "ViewConfigListResponse(viewConfigResponseList=" + getViewConfigResponseList() + ")";
    }

    public List<ViewConfigResponse> getViewConfigResponseList() {
        return this.viewConfigResponseList;
    }
}
