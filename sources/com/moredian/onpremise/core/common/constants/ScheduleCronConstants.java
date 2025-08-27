package com.moredian.onpremise.core.common.constants;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/common/constants/ScheduleCronConstants.class */
public class ScheduleCronConstants {
    public static final String DELETE_CACHE_SCHEDULE_CRON = "30 10 1 28 * ?";
    public static final String BACKUPS_DATA_SCHEDULE_CRON = "30 33 1 ? * MON";
    public static final String DELETE_EXPIRE_DATA_SCHEDULE_CRON = "30 30 4 * * ?";
    public static final String DELETE_OVERFLOW_DATA_SCHEDULE_CRON = "30 30 2 * * ?";
    public static final String PULL_DEVICE_LOG_SCHEDULE_CRON = "30 30 3 * * ?";
    public static final String DELETE_INVALID_FACE_IMAGE_SCHEDULE_CRON = "0 0 1 ? * MON";
    public static final String DELETE_CACHE_DATA_SCHEDULE_CRON = "*/30 * * * * ?";
    public static final String APP_VALID_SCHEDULE_CRON = "30 0 0 * * ?";
    public static final String EXTERNAL_CONTACT_VALID_SCHEDULE_CRON = "30 0 0 * * ?";
}
