package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/ExtractFeatureStatusEnum.class */
public enum ExtractFeatureStatusEnum {
    SUCCESS_STATUS(1, "抽取成功"),
    EXTRACTING_STATUS(2, "抽取中"),
    DOWNLOAD_FAIL_STATUS(3, "访问下载文件端口失败"),
    FAIL_STATUS(0, "抽取失败");

    private int value;
    private String description;

    ExtractFeatureStatusEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }
}
