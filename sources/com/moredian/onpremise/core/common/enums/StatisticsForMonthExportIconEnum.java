package com.moredian.onpremise.core.common.enums;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/enums/StatisticsForMonthExportIconEnum.class */
public enum StatisticsForMonthExportIconEnum {
    SUCCESS(1, "✔"),
    LATE(2, "▲"),
    LEAVE_EARLY(3, "▼"),
    LACK(4, "◯"),
    ABSENCE(5, "⛝"),
    TRIP(6, "✈"),
    OTHER(7, "㊡"),
    SICK(8, "✚"),
    PERSONAL(9, "✱");

    private int code;
    private String description;

    StatisticsForMonthExportIconEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public static StatisticsForMonthExportIconEnum getByCode(int code) {
        StatisticsForMonthExportIconEnum callbackTagEnum = null;
        for (StatisticsForMonthExportIconEnum item : values()) {
            if (item.getCode() == code) {
                callbackTagEnum = item;
            }
        }
        return callbackTagEnum;
    }

    public static StatisticsForMonthExportIconEnum getByDescription(String description) {
        StatisticsForMonthExportIconEnum callbackTagEnum = null;
        for (StatisticsForMonthExportIconEnum item : values()) {
            if (item.getDescription() == description) {
                callbackTagEnum = item;
            }
        }
        return callbackTagEnum;
    }
}
