package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "服务支持回调tag类型列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListCallbackTagResponse.class */
public class ListCallbackTagResponse implements Serializable {
    private static final long serialVersionUID = -7175702487302483379L;

    @ApiModelProperty(name = "tag", value = "tag")
    private String tag;

    @ApiModelProperty(name = "description", value = "tag描述")
    private String description;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListCallbackTagResponse)) {
            return false;
        }
        ListCallbackTagResponse other = (ListCallbackTagResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$tag = getTag();
        Object other$tag = other.getTag();
        if (this$tag == null) {
            if (other$tag != null) {
                return false;
            }
        } else if (!this$tag.equals(other$tag)) {
            return false;
        }
        Object this$description = getDescription();
        Object other$description = other.getDescription();
        return this$description == null ? other$description == null : this$description.equals(other$description);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListCallbackTagResponse;
    }

    public int hashCode() {
        Object $tag = getTag();
        int result = (1 * 59) + ($tag == null ? 43 : $tag.hashCode());
        Object $description = getDescription();
        return (result * 59) + ($description == null ? 43 : $description.hashCode());
    }

    public String toString() {
        return "ListCallbackTagResponse(tag=" + getTag() + ", description=" + getDescription() + ")";
    }

    public String getTag() {
        return this.tag;
    }

    public String getDescription() {
        return this.description;
    }
}
