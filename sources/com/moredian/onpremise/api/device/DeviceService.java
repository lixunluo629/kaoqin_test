package com.moredian.onpremise.api.device;

import com.moredian.onpremise.core.model.request.ActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.BatchSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.BindDeviceGroupRequest;
import com.moredian.onpremise.core.model.request.CancelUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.DeleteDeviceRequest;
import com.moredian.onpremise.core.model.request.DeviceCallbackRequest;
import com.moredian.onpremise.core.model.request.DeviceDetailRequest;
import com.moredian.onpremise.core.model.request.DeviceListRequest;
import com.moredian.onpremise.core.model.request.DevicePushLogRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgQueryRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgRequest;
import com.moredian.onpremise.core.model.request.DeviceResetDataRequest;
import com.moredian.onpremise.core.model.request.DeviceRestartRequest;
import com.moredian.onpremise.core.model.request.DeviceShowDataRequest;
import com.moredian.onpremise.core.model.request.DownloadDeviceLogRequest;
import com.moredian.onpremise.core.model.request.GetGroupDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeRecordRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.OpenDoorRequest;
import com.moredian.onpremise.core.model.request.PreActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.PullDeviceLogRequest;
import com.moredian.onpremise.core.model.request.RetryUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.SaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.SaveUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckDeviceActiveRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.UpdateDeviceRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.response.ActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.DeviceConfigResponse;
import com.moredian.onpremise.core.model.response.DevicePushMsgResponse;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.model.response.DownloadDeviceLogResponse;
import com.moredian.onpremise.core.model.response.GroupDeviceResponse;
import com.moredian.onpremise.core.model.response.ListSearchDeviceResponse;
import com.moredian.onpremise.core.model.response.PreActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckDeviceActiveResponse;
import com.moredian.onpremise.core.model.response.UpgradeRecordResponse;
import com.moredian.onpremise.core.model.response.UpgradeScheduleResponse;
import com.moredian.onpremise.core.model.response.UploadUpgradePackageResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/device/DeviceService.class */
public interface DeviceService {
    PageList<DeviceResponse> listDevice(DeviceListRequest deviceListRequest);

    List<ListSearchDeviceResponse> listSearchDevice(DeviceListRequest deviceListRequest);

    List<ListSearchDeviceResponse> listOnlineDevice(DeviceListRequest deviceListRequest);

    List<ListSearchDeviceResponse> listUpgradeDevice(ListUpgradeDeviceRequest listUpgradeDeviceRequest);

    PageList<GroupDeviceResponse> groupDeviceList(DeviceListRequest deviceListRequest);

    DeviceResponse deviceDetail(DeviceDetailRequest deviceDetailRequest);

    DeviceResponse deviceDetailBySn(DeviceDetailRequest deviceDetailRequest);

    boolean deleteDevice(DeleteDeviceRequest deleteDeviceRequest);

    boolean deleteDeviceByDeviceSn(DeleteDeviceRequest deleteDeviceRequest);

    boolean updateDevice(UpdateDeviceRequest updateDeviceRequest);

    boolean saveDeviceConfig(SaveDeviceConfigRequest saveDeviceConfigRequest, Boolean bool);

    boolean batchSaveDeviceConfig(BatchSaveDeviceConfigRequest batchSaveDeviceConfigRequest);

    boolean terminalSaveDeviceConfig(TerminalSaveDeviceConfigRequest terminalSaveDeviceConfigRequest);

    ActiveDeviceResponse activeDevice(ActiveDeviceRequest activeDeviceRequest);

    TerminalCheckDeviceActiveResponse checkDeviceActive(TerminalCheckDeviceActiveRequest terminalCheckDeviceActiveRequest);

    UploadUpgradePackageResponse uploadUpgradePackage(UploadUpgradePackageRequest uploadUpgradePackageRequest);

    UploadUpgradePackageResponse uploadUpgradePackageV2(UploadUpgradePackageRequest uploadUpgradePackageRequest);

    boolean checkDeviceFailure(String str);

    String saveUpgradeSchedule(SaveUpgradeScheduleRequest saveUpgradeScheduleRequest);

    boolean executeUpgradeSchedule();

    PageList<UpgradeScheduleResponse> listUpgradeSchedule(ListUpgradeScheduleRequest listUpgradeScheduleRequest);

    boolean cancelUpgradeSchedule(CancelUpgradeScheduleRequest cancelUpgradeScheduleRequest);

    void forceCloseUpgradeSchedule(CancelUpgradeScheduleRequest cancelUpgradeScheduleRequest);

    boolean retryUpgradeSchedule(RetryUpgradeScheduleRequest retryUpgradeScheduleRequest);

    PageList<UpgradeRecordResponse> listUpgradeRecord(ListUpgradeRecordRequest listUpgradeRecordRequest);

    GroupDeviceResponse getGroupDeviceByDeviceId(GetGroupDeviceRequest getGroupDeviceRequest);

    boolean pullDeviceLog(PullDeviceLogRequest pullDeviceLogRequest);

    boolean devicePushLog(DevicePushLogRequest devicePushLogRequest);

    boolean openTheDoor(OpenDoorRequest openDoorRequest);

    DownloadDeviceLogResponse downloadDeviceLog(DownloadDeviceLogRequest downloadDeviceLogRequest);

    void sendCallback(DeviceCallbackRequest deviceCallbackRequest);

    boolean restart(DeviceRestartRequest deviceRestartRequest);

    boolean resetData(DeviceResetDataRequest deviceResetDataRequest);

    boolean pushShowData(@RequestBody DeviceShowDataRequest deviceShowDataRequest);

    List<String> listDeviceModel(Long l);

    List<Integer> listDeviceType(Long l);

    String pushDeviceMsg(DevicePushMsgRequest devicePushMsgRequest);

    List<DevicePushMsgResponse> queryPushDeviceMsgResult(DevicePushMsgQueryRequest devicePushMsgQueryRequest);

    PreActiveDeviceResponse preActiveDevice(PreActiveDeviceRequest preActiveDeviceRequest);

    DeviceConfigResponse getDeviceConfig(String str, Long l);

    boolean bindDeviceGroup(BindDeviceGroupRequest bindDeviceGroupRequest);
}
