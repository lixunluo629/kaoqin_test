package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bouncycastle.i18n.TextBundle;

@ApiModel(description = "页面配置请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ViewConfigRequest.class */
public class ViewConfigRequest extends BaseRequest {
    private static final long serialVersionUID = 4135456540787112675L;

    @ApiModelProperty(name = "id", value = "id，编辑时必传")
    private Long id;

    @ApiModelProperty(name = "type", value = "类型：1-登录页；2-app模块；3-文字替换")
    private Integer type;

    @ApiModelProperty(name = "bizKey", value = "同一type业务内的唯一key，app模块传appType")
    private String bizKey;

    @ApiModelProperty(name = "icon", value = "图标")
    private String icon;

    @ApiModelProperty(name = TextBundle.TEXT_ENTRY, value = "显示的文字")
    private String text;

    @ApiModelProperty(name = "replacedText", value = "要被替换的文字")
    private String replacedText;

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setReplacedText(String replacedText) {
        this.replacedText = replacedText;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewConfigRequest)) {
            return false;
        }
        ViewConfigRequest other = (ViewConfigRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$bizKey = getBizKey();
        Object other$bizKey = other.getBizKey();
        if (this$bizKey == null) {
            if (other$bizKey != null) {
                return false;
            }
        } else if (!this$bizKey.equals(other$bizKey)) {
            return false;
        }
        Object this$icon = getIcon();
        Object other$icon = other.getIcon();
        if (this$icon == null) {
            if (other$icon != null) {
                return false;
            }
        } else if (!this$icon.equals(other$icon)) {
            return false;
        }
        Object this$text = getText();
        Object other$text = other.getText();
        if (this$text == null) {
            if (other$text != null) {
                return false;
            }
        } else if (!this$text.equals(other$text)) {
            return false;
        }
        Object this$replacedText = getReplacedText();
        Object other$replacedText = other.getReplacedText();
        return this$replacedText == null ? other$replacedText == null : this$replacedText.equals(other$replacedText);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ViewConfigRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $bizKey = getBizKey();
        int result3 = (result2 * 59) + ($bizKey == null ? 43 : $bizKey.hashCode());
        Object $icon = getIcon();
        int result4 = (result3 * 59) + ($icon == null ? 43 : $icon.hashCode());
        Object $text = getText();
        int result5 = (result4 * 59) + ($text == null ? 43 : $text.hashCode());
        Object $replacedText = getReplacedText();
        return (result5 * 59) + ($replacedText == null ? 43 : $replacedText.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ViewConfigRequest(id=" + getId() + ", type=" + getType() + ", bizKey=" + getBizKey() + ", icon=" + getIcon() + ", text=" + getText() + ", replacedText=" + getReplacedText() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Integer getType() {
        return this.type;
    }

    public String getBizKey() {
        return this.bizKey;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public String getReplacedText() {
        return this.replacedText;
    }
}
