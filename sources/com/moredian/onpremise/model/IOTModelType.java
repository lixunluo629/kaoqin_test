package com.moredian.onpremise.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/IOTModelType.class */
public enum IOTModelType {
    ACTIVATION_REQUEST(1, ActivationRequest.class),
    ACTIVATION_RESPONSE(2, ActivationResponse.class),
    DEVICE_INFO_REQUEST(3, DeviceInfoRequest.class),
    DEVICE_INFO_RESPONSE(4, DeviceInfoResponse.class),
    EXTRACT_FEATURE_REQUEST(5, ExtractFeatureRequest.class),
    EXTRACT_FEATURE_RESPONSE(6, ExtractFeatureResponse.class),
    SYNC_MEMBER_REQUEST(7, SyncMemberRequest.class),
    SYNC_MEMBER_RESPONSE(8, SyncMemberResponse.class),
    SYNC_GROUP_REQUEST(9, SyncGroupRequest.class),
    SYNC_GROUP_RESPONSE(10, SyncGroupResponse.class),
    DEVICE_UPGRADE_REQUEST(11, TerminalDeviceUpgradeRequest.class),
    DEVICE_UPGRADE_RESPONSE(12, TerminalDeviceUpgradeResponse.class),
    TERMINAL_EXTRACT_FEATURE_REQUEST(13, TerminalExtractFeatureRequest.class),
    TERMINAL_EXTRACT_FEATURE_RESPONSE(14, TerminalExtractFeatureResponse.class),
    DEVICE_FAILURE_REQUEST(15, DeviceFailureRequest.class),
    DEVICE_FAILURE_RESPONSE(16, DeviceFailureResponse.class),
    DEVICE_UNBIND_REQUEST(17, DeviceUnbindRequest.class),
    DEVICE_UNBIND_RESPONSE(18, DeviceUnbindResponse.class),
    DEVICE_HEART_BEAT_RESPONSE(19, DeviceHeartBeatResponse.class),
    DEVICE_VOICE_CONFIG_REQUEST(20, DeviceVoiceConfigRequest.class),
    DEVICE_UPGRADE_START_REQUEST(21, DeviceUpgradeStartRequest.class),
    PULL_DEVICE_LOG_REQUEST(22, PullDeviceLogEvent.class),
    SYNC_CHECK_IN_TASK_REQUEST(23, SyncCheckInTaskRequest.class),
    SYNC_CHECK_IN_TASK_RESPONSE(24, SyncCheckInTaskResponse.class),
    SYNC_STRANGER_REMINDER_INFO_REQUEST(25, SyncStrangerReminderInfoRequest.class),
    NOTICE_DEVICE_EXTRACT_REQUEST(26, NoticeDeviceExtractRequest.class),
    TERMINAL_EXTRACT_NOTICE_REQUEST(27, TerminalExtractNoticeRequest.class),
    TERMINAL_OPEN_DOOR_REQUEST(28, TerminalOpenDoorRequest.class),
    DEVICE_UPGRADE_FAIL_REQUEST(29, DeviceUpgradeFailRequest.class),
    REGISTER_ONLINE_CHECK_CALLBACK_REQUEST(30, RegisterOnlineCheckCallbackRequest.class),
    DELETE_ONLINE_CHECK_CALLBACK_REQUEST(31, DeleteOnlineCheckCallbackRequest.class),
    REGISTER_QR_CHECK_CALLBACK_REQUEST(32, RegisterQrCheckCallbackRequest.class),
    DELETE_QR_CHECK_CALLBACK_REQUEST(33, DeleteQrCheckCallbackRequest.class),
    SYNC_ATTENDANCE_GROUP_REQUEST(34, SyncAttendanceGroupRequest.class),
    SYNC_CHECK_IN_TASK_MEMBER_REQUEST(35, SyncCheckInTaskMemberRequest.class),
    SYNC_ATTENDANCE_HOLIDAY_REQUEST(36, SyncAttendanceHolidayRequest.class),
    SYNC_ADVERTISING_INFO_REQUEST(37, SyncAdvertisingInfoRequest.class),
    SYNC_ACCOUNT_REQUEST(38, SyncAccountRequest.class),
    SYNC_DEFAULT_SHOW_TYPE_REQUEST(39, SyncDeviceDefaultShowRequest.class),
    SYNC_CANTEEN_REQUEST(40, SyncCanteenRequest.class),
    SYNC_CANTEEN_MEMBER_REQUEST(41, SyncCanteenMemberRequest.class),
    RESTART_REQUEST(42, TerminalRestartRequest.class),
    RESET_DATA_REQUEST(43, TerminalResetDataRequest.class),
    SYNC_SNAP_MODE_REQUEST(44, SyncSnapModeRequest.class),
    SHOW_DATA_REQUEST(45, TerminalShowDataRequest.class),
    SNAP_MODE_REQUEST(46, SyncSnapModeRequest.class),
    SYNC_TEMPERATURE_CONFIG_REQUEST(47, SyncTemperatureConfigRequest.class),
    SYNC_EXTERNAL_CONTACT_REQUEST(48, SyncExternalContactRequest.class),
    SYNC_VISIT_CONFIG_REQUEST(49, SyncVisitConfigRequest.class),
    PUSH_MSG_REQUEST(50, TerminalPushMsgRequest.class),
    DEVICE_CONSUME_MSG_RESPONSE(51, DeviceConsumeMsgResponse.class);

    private static final Map<Integer, IOTModelType> MAP = new ConcurrentHashMap();
    private int type;
    private Class<?> clazz;

    static {
        for (IOTModelType type : values()) {
            MAP.put(Integer.valueOf(type.type()), type);
        }
    }

    IOTModelType(int type, Class cls) {
        this.clazz = cls;
        this.type = type;
    }

    public int type() {
        return this.type;
    }

    public Class<?> clazz() {
        return this.clazz;
    }

    public static IOTModelType from(int type) {
        IOTModelType ret = MAP.get(Integer.valueOf(type));
        if (ret == null) {
            throw new IllegalArgumentException("Unknown IOT Model type: " + type);
        }
        return ret;
    }
}
