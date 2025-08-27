package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "权限模块响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AuthModuleResponse.class */
public class AuthModuleResponse implements Serializable {
    private static final long serialVersionUID = 7517546729510917836L;

    @ApiModelProperty(name = "moduleId", value = "模块id")
    private Long moduleId;

    @ApiModelProperty(name = "modulePath", value = "模块路径")
    private String modulePath;

    @ApiModelProperty(name = "moduleName", value = "模块名称")
    private String moduleName;

    @ApiModelProperty(name = "superModuleId", value = "父模块id")
    private Long superModuleId;

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setSuperModuleId(Long superModuleId) {
        this.superModuleId = superModuleId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthModuleResponse)) {
            return false;
        }
        AuthModuleResponse other = (AuthModuleResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$moduleId = getModuleId();
        Object other$moduleId = other.getModuleId();
        if (this$moduleId == null) {
            if (other$moduleId != null) {
                return false;
            }
        } else if (!this$moduleId.equals(other$moduleId)) {
            return false;
        }
        Object this$modulePath = getModulePath();
        Object other$modulePath = other.getModulePath();
        if (this$modulePath == null) {
            if (other$modulePath != null) {
                return false;
            }
        } else if (!this$modulePath.equals(other$modulePath)) {
            return false;
        }
        Object this$moduleName = getModuleName();
        Object other$moduleName = other.getModuleName();
        if (this$moduleName == null) {
            if (other$moduleName != null) {
                return false;
            }
        } else if (!this$moduleName.equals(other$moduleName)) {
            return false;
        }
        Object this$superModuleId = getSuperModuleId();
        Object other$superModuleId = other.getSuperModuleId();
        return this$superModuleId == null ? other$superModuleId == null : this$superModuleId.equals(other$superModuleId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthModuleResponse;
    }

    public int hashCode() {
        Object $moduleId = getModuleId();
        int result = (1 * 59) + ($moduleId == null ? 43 : $moduleId.hashCode());
        Object $modulePath = getModulePath();
        int result2 = (result * 59) + ($modulePath == null ? 43 : $modulePath.hashCode());
        Object $moduleName = getModuleName();
        int result3 = (result2 * 59) + ($moduleName == null ? 43 : $moduleName.hashCode());
        Object $superModuleId = getSuperModuleId();
        return (result3 * 59) + ($superModuleId == null ? 43 : $superModuleId.hashCode());
    }

    public String toString() {
        return "AuthModuleResponse(moduleId=" + getModuleId() + ", modulePath=" + getModulePath() + ", moduleName=" + getModuleName() + ", superModuleId=" + getSuperModuleId() + ")";
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public String getModulePath() {
        return this.modulePath;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public Long getSuperModuleId() {
        return this.superModuleId;
    }
}
