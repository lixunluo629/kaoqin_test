package com.moredian.onpremise.device.service.impl;

import com.moredian.onpremise.api.device.DeviceGroupService;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.DeviceGroupMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.DeviceGroup;
import com.moredian.onpremise.core.model.request.DeviceGroupDeleteRequest;
import com.moredian.onpremise.core.model.request.DeviceGroupRequest;
import com.moredian.onpremise.core.model.response.DeviceGroupResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/impl/DeviceGroupServiceImpl.class */
public class DeviceGroupServiceImpl implements DeviceGroupService {

    @Autowired
    private DeviceGroupMapper deviceGroupMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override // com.moredian.onpremise.api.device.DeviceGroupService
    public Long insertOne(DeviceGroupRequest request) throws BeansException {
        checkParams(request);
        DeviceGroup deviceGroup = new DeviceGroup();
        BeanUtils.copyProperties(request, deviceGroup);
        if (request.getSuperId().equals(0L)) {
            deviceGroup.setSuperId(1L);
        }
        this.deviceGroupMapper.insertOne(deviceGroup);
        return deviceGroup.getId();
    }

    private void checkParams(DeviceGroupRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getName(), OnpremiseErrorEnum.DEVICE_GROUP_NAME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getSuperId(), OnpremiseErrorEnum.DEVICE_GROUP_SUPER_MUST_NOT_NULL);
    }

    @Override // com.moredian.onpremise.api.device.DeviceGroupService
    @Transactional(rollbackFor = {Exception.class})
    public void softDeleteById(DeviceGroupDeleteRequest request) {
        AssertUtil.isNullOrEmpty(request.getId(), OnpremiseErrorEnum.DEVICE_GROUP_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        DeviceGroup deviceGroup = this.deviceGroupMapper.getOneById(request.getId(), request.getOrgId());
        AssertUtil.isTrue(Boolean.valueOf(!deviceGroup.getSuperId().equals(0L)), OnpremiseErrorEnum.DEVICE_GROUP_ROOT_CAN_NOT_DELETE);
        List<DeviceGroup> childDeviceGroups = this.deviceGroupMapper.getChildList(request.getId(), request.getOrgId());
        AssertUtil.isTrue(Boolean.valueOf(CollectionUtils.isEmpty(childDeviceGroups)), OnpremiseErrorEnum.DEVICE_GROUP_WITH_CHILD_CANT_NOT_DELETE);
        this.deviceGroupMapper.softDeleteById(request.getId(), request.getOrgId());
        List<Device> deviceList = this.deviceMapper.listDeviceByDeviceGroupId(request.getOrgId(), request.getId());
        if (!CollectionUtils.isEmpty(deviceList)) {
            List<String> deviceSns = new ArrayList<>();
            for (Device device : deviceList) {
                deviceSns.add(device.getDeviceSn());
            }
            this.deviceMapper.updateDeviceGroup(request.getOrgId(), deviceSns, 1L);
        }
    }

    @Override // com.moredian.onpremise.api.device.DeviceGroupService
    public void updateOneById(DeviceGroupRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getId(), OnpremiseErrorEnum.DEVICE_GROUP_ID_MUST_NOT_NULL);
        checkParams(request);
        DeviceGroup oldDeviceGroup = this.deviceGroupMapper.getOneById(request.getId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(oldDeviceGroup, OnpremiseErrorEnum.DEVICE_GROUP_NOT_FOUND);
        DeviceGroup deviceGroup = new DeviceGroup();
        BeanUtils.copyProperties(request, deviceGroup);
        if (request.getSuperId().equals(0L)) {
            deviceGroup.setSuperId(oldDeviceGroup.getSuperId());
        }
        this.deviceGroupMapper.updateOneById(deviceGroup);
    }

    @Override // com.moredian.onpremise.api.device.DeviceGroupService
    public List<DeviceGroupResponse> getList(Long orgId) throws BeansException {
        List<DeviceGroup> deviceGroups = this.deviceGroupMapper.getList(orgId);
        List<DeviceGroupResponse> responses = new ArrayList<>();
        if (CollectionUtils.isEmpty(deviceGroups)) {
            return responses;
        }
        for (DeviceGroup deviceGroup : deviceGroups) {
            DeviceGroupResponse deviceGroupResponse = new DeviceGroupResponse();
            BeanUtils.copyProperties(deviceGroup, deviceGroupResponse);
            responses.add(deviceGroupResponse);
        }
        return createTree(responses);
    }

    private List<DeviceGroupResponse> createTree(List<DeviceGroupResponse> responses) {
        Map<Long, List<DeviceGroupResponse>> groupByParentIdMap = new HashMap<>();
        for (DeviceGroupResponse item : responses) {
            List<DeviceGroupResponse> listDeptResponseList = new ArrayList<>();
            if (!groupByParentIdMap.containsKey(item.getSuperId())) {
                listDeptResponseList.add(item);
                groupByParentIdMap.put(item.getSuperId(), listDeptResponseList);
            } else {
                groupByParentIdMap.get(item.getSuperId()).add(item);
            }
        }
        Map<Long, DeviceGroupResponse> dataMap = new HashMap<>();
        for (DeviceGroupResponse item2 : responses) {
            dataMap.put(item2.getId(), item2);
        }
        ArrayList resp = new ArrayList();
        for (Long parentId : groupByParentIdMap.keySet()) {
            if (dataMap.containsKey(parentId)) {
                List<DeviceGroupResponse> child = dataMap.get(parentId).getChildDeviceGroups();
                if (CollectionUtils.isEmpty(child)) {
                    child = new ArrayList();
                }
                child.addAll(groupByParentIdMap.get(parentId));
                dataMap.get(parentId).setChildDeviceGroups(child);
            } else {
                resp.addAll(groupByParentIdMap.get(parentId));
            }
        }
        return resp;
    }
}
