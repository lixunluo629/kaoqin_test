package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/NeedExtractFeatureEnum.class */
public enum NeedExtractFeatureEnum {
    EXTRACT_YES(1, "需要"),
    EXTRACT_NO(0, "不需要");

    private int value;
    private String description;

    NeedExtractFeatureEnum(int value, String description) {
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
