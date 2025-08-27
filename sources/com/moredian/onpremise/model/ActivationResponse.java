package com.moredian.onpremise.model;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/ActivationResponse.class */
public class ActivationResponse extends NettyBaseResponse {
    public static final IOTModelType MODEL_TYPE = IOTModelType.ACTIVATION_RESPONSE;
    private Long equipmentId;
    private Long orgId;
    private String equipmentName;
    private Integer equipmentType;
    private String serialNumber;
    private String token;

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ActivationResponse)) {
            return false;
        }
        ActivationResponse other = (ActivationResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$equipmentId = getEquipmentId();
        Object other$equipmentId = other.getEquipmentId();
        if (this$equipmentId == null) {
            if (other$equipmentId != null) {
                return false;
            }
        } else if (!this$equipmentId.equals(other$equipmentId)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$equipmentName = getEquipmentName();
        Object other$equipmentName = other.getEquipmentName();
        if (this$equipmentName == null) {
            if (other$equipmentName != null) {
                return false;
            }
        } else if (!this$equipmentName.equals(other$equipmentName)) {
            return false;
        }
        Object this$equipmentType = getEquipmentType();
        Object other$equipmentType = other.getEquipmentType();
        if (this$equipmentType == null) {
            if (other$equipmentType != null) {
                return false;
            }
        } else if (!this$equipmentType.equals(other$equipmentType)) {
            return false;
        }
        Object this$serialNumber = getSerialNumber();
        Object other$serialNumber = other.getSerialNumber();
        if (this$serialNumber == null) {
            if (other$serialNumber != null) {
                return false;
            }
        } else if (!this$serialNumber.equals(other$serialNumber)) {
            return false;
        }
        Object this$token = getToken();
        Object other$token = other.getToken();
        return this$token == null ? other$token == null : this$token.equals(other$token);
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    protected boolean canEqual(Object other) {
        return other instanceof ActivationResponse;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public int hashCode() {
        Object $equipmentId = getEquipmentId();
        int result = (1 * 59) + ($equipmentId == null ? 43 : $equipmentId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $equipmentName = getEquipmentName();
        int result3 = (result2 * 59) + ($equipmentName == null ? 43 : $equipmentName.hashCode());
        Object $equipmentType = getEquipmentType();
        int result4 = (result3 * 59) + ($equipmentType == null ? 43 : $equipmentType.hashCode());
        Object $serialNumber = getSerialNumber();
        int result5 = (result4 * 59) + ($serialNumber == null ? 43 : $serialNumber.hashCode());
        Object $token = getToken();
        return (result5 * 59) + ($token == null ? 43 : $token.hashCode());
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public String toString() {
        return "ActivationResponse(equipmentId=" + getEquipmentId() + ", orgId=" + getOrgId() + ", equipmentName=" + getEquipmentName() + ", equipmentType=" + getEquipmentType() + ", serialNumber=" + getSerialNumber() + ", token=" + getToken() + ")";
    }

    public Long getEquipmentId() {
        return this.equipmentId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public Integer getEquipmentType() {
        return this.equipmentType;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getToken() {
        return this.token;
    }
}
