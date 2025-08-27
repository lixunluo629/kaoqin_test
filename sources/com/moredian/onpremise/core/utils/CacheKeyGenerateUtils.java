package com.moredian.onpremise.core.utils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/CacheKeyGenerateUtils.class */
public class CacheKeyGenerateUtils {
    private static final String HEART_BEAT_PREFIX = "heart_";
    private static final String UPGRADE_STATUS_PREFIX = "upgrade_status_";
    private static final String EXTRACT_FEATURE_STATUS = "extract_feature_status_";
    private static final String MEMBER_SYNC_PREFIX = "member_sync_";
    private static final String GROUP_SYNC_PREFIX = "group_sync_";
    private static final String LAST_MODIFY_TIME_PREFIX = "last_modify_time_";
    private static final String LAST_MODIFY_MEMBER_TIME_SUFFIX = "_member";
    private static final String LAST_MODIFY_GROUP_TIME_SUFFIX = "_group";
    private static final String LAST_MODIFY_ATTENDANCE_GROUP_TIME_SUFFIX = "_attendance_group";
    private static final String LAST_MODIFY_ATTENDANCE_HOLIDAY_TIME_SUFFIX = "_attendance_holiday";
    private static final String LAST_MODIFY_CHECK_IN_TASK_MEMBER_TIME_SUFFIX = "_check_in_task_member";
    private static final String LAST_MODIFY_CANTEEN_TIME_SUFFIX = "_meal_canteen";
    private static final String LAST_MODIFY_CANTEEN_MEMBER_TIME_SUFFIX = "_meal_canteen_member";
    private static final String LAST_MODIFY_ACCOUNT_TIME_SUFFIX = "_account";
    private static final String LAST_MODIFY_CHECK_IN_TIME_SUFFIX = "_checkIn";
    private static final String CHECK_IN_SYNC_PREFIX = "check_in_sync_";
    private static final String LAST_MODIFY_EXTERNAL_CONTACT_TIME_SUFFIX = "_external_contact";
    private static final String LAST_MODIFY_VISIT_CONFIG_TIME_SUFFIX = "_visit_config";
    private static final String LAST_MODIFY_DEVICE_CONFIG_TIME_SUFFIX = "_device_config";

    public static String restoreHeartBeatCacheKey(String key) {
        return key.replace(HEART_BEAT_PREFIX, "");
    }

    public static String getHeartBeatCacheKey(String deviceSn) {
        return HEART_BEAT_PREFIX + deviceSn;
    }

    public static String restoreUpgradeStatusCacheKey(String key) {
        return key.replace(UPGRADE_STATUS_PREFIX, "");
    }

    public static String getUpgradeStatusCacheKey(String deviceSn) {
        return UPGRADE_STATUS_PREFIX + deviceSn;
    }

    public static String restoreExtractFeatureStatusKey(String key) {
        return key.replace(EXTRACT_FEATURE_STATUS, "");
    }

    public static String getExtractFeatureStatusKey(String mobile) {
        return EXTRACT_FEATURE_STATUS + mobile;
    }

    public static String restoreMemberSyncKey(String key) {
        return key.replace(MEMBER_SYNC_PREFIX, "");
    }

    public static String getMemberSyncKey(String deviceSn) {
        return MEMBER_SYNC_PREFIX + deviceSn;
    }

    public static String restoreGroupSyncKey(String key) {
        return key.replace(GROUP_SYNC_PREFIX, "");
    }

    public static String getGroupSyncKey(String deviceSn) {
        return GROUP_SYNC_PREFIX + deviceSn;
    }

    public static String getMemberLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_MEMBER_TIME_SUFFIX;
    }

    public static String getGroupLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_GROUP_TIME_SUFFIX;
    }

    public static String restoreGroupLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_GROUP_TIME_SUFFIX, "");
    }

    public static String restoreMemberLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_MEMBER_TIME_SUFFIX, "");
    }

    public static String getAttendanceGroupLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_ATTENDANCE_GROUP_TIME_SUFFIX;
    }

    public static String restoreAttendanceGroupLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_ATTENDANCE_GROUP_TIME_SUFFIX, "");
    }

    public static String getAttendanceHolidayLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_ATTENDANCE_HOLIDAY_TIME_SUFFIX;
    }

    public static String restoreAttendanceHolidayLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_ATTENDANCE_HOLIDAY_TIME_SUFFIX, "");
    }

    public static String getCheckInTaskMemberLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_CHECK_IN_TASK_MEMBER_TIME_SUFFIX;
    }

    public static String getCheckInSyncKey(String deviceSn) {
        return CHECK_IN_SYNC_PREFIX + deviceSn;
    }

    public static String restoreCheckInSyncKey(String key) {
        return key.replace(CHECK_IN_SYNC_PREFIX, "");
    }

    public static String getCheckInLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_CHECK_IN_TIME_SUFFIX;
    }

    public static String restoreCheckInLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_CHECK_IN_TIME_SUFFIX, "");
    }

    public static String getAccountLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_ACCOUNT_TIME_SUFFIX;
    }

    public static String restoreAccountLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_ACCOUNT_TIME_SUFFIX, "");
    }

    public static String getCanteenLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_CANTEEN_TIME_SUFFIX;
    }

    public static String restoreCanteenLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_CANTEEN_TIME_SUFFIX, "");
    }

    public static String getMealCanteenMemberLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_CANTEEN_MEMBER_TIME_SUFFIX;
    }

    public static String restoreMealCanteenMemberLastModifyTimeKey(String key) {
        return key.replace(LAST_MODIFY_TIME_PREFIX, "").replace(LAST_MODIFY_CANTEEN_MEMBER_TIME_SUFFIX, "");
    }

    public static String getExternalContactLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_EXTERNAL_CONTACT_TIME_SUFFIX;
    }

    public static String getVisitConfigLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_VISIT_CONFIG_TIME_SUFFIX;
    }

    public static String getDeviceConfigLastModifyTimeKey(String deviceSn) {
        return LAST_MODIFY_TIME_PREFIX + deviceSn + LAST_MODIFY_DEVICE_CONFIG_TIME_SUFFIX;
    }
}
