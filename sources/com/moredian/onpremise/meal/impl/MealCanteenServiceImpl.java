package com.moredian.onpremise.meal.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.api.member.DeptService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.CanteenTimeTypeEnum;
import com.moredian.onpremise.core.common.enums.CommonStatusEnum;
import com.moredian.onpremise.core.common.enums.DeleteOrNotEnum;
import com.moredian.onpremise.core.common.enums.MealCanteenMemberTypeEnum;
import com.moredian.onpremise.core.common.enums.MealCanteenNotifyTypeEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.DeptMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.MealCanteenDeviceMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMapper;
import com.moredian.onpremise.core.mapper.MealCanteenMemberMapper;
import com.moredian.onpremise.core.mapper.MealCanteenTimeMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.MemberMealCanteenSyncMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.Dept;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.MealCanteen;
import com.moredian.onpremise.core.model.domain.MealCanteenDevice;
import com.moredian.onpremise.core.model.domain.MealCanteenMember;
import com.moredian.onpremise.core.model.domain.MealCanteenTime;
import com.moredian.onpremise.core.model.domain.Member;
import com.moredian.onpremise.core.model.domain.MemberMealCanteenSync;
import com.moredian.onpremise.core.model.dto.CanteenTimeDto;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.dto.MemberDto;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.info.UpdateGroupDeviceInfo;
import com.moredian.onpremise.core.model.request.DeleteMealCanteenRequest;
import com.moredian.onpremise.core.model.request.ListCanteenRequest;
import com.moredian.onpremise.core.model.request.QueryCanteenDetailsRequest;
import com.moredian.onpremise.core.model.request.SaveMealCanteenRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCanteenRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.DeptMemberListResponse;
import com.moredian.onpremise.core.model.response.ListCanteenResponse;
import com.moredian.onpremise.core.model.response.ListDeptResponse;
import com.moredian.onpremise.core.model.response.QueryCanteenDetailsResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberMealCanteenResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncCanteenMemberRequest;
import com.moredian.onpremise.model.SyncCanteenRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
/* loaded from: onpremise-meal-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/meal/impl/MealCanteenServiceImpl.class */
public class MealCanteenServiceImpl implements MealCanteenService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MealCanteenServiceImpl.class);

    @Autowired
    private MealCanteenMapper canteenMapper;

    @Autowired
    private MealCanteenMemberMapper canteenMemberMapper;

    @Autowired
    private MealCanteenTimeMapper canteenTimeMapper;

    @Autowired
    private MealCanteenDeviceMapper canteenDeviceMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private MemberMealCanteenSyncMapper memberMealCanteenSyncMapper;

    @Autowired
    private MealCanteenDeviceMapper mealCanteenDeviceMapper;

    @Autowired
    private MealCanteenMemberMapper mealCanteenMemberMapper;

    @Autowired
    private DeptService deptService;

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long insert(SaveMealCanteenRequest request) throws BeansException {
        checkSaveMealCanteenRequest(request);
        MealCanteen canteen = requestTransToCanteen(request);
        AssertUtil.isTrue(Boolean.valueOf(this.canteenMapper.insert(canteen) > 0), OnpremiseErrorEnum.INSERT_CANTEEN_FAIL);
        List<GroupMemberDto> members = request.getCanteenMembers();
        ArrayList arrayList = new ArrayList();
        for (GroupMemberDto item : members) {
            item.setConfirmFlag(1);
            if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
            }
        }
        HashSet hashSet = new HashSet();
        List<Long> noRepeatChildDeptIds = new ArrayList<>();
        hashSet.addAll(arrayList);
        noRepeatChildDeptIds.addAll(hashSet);
        for (Long deptId : noRepeatChildDeptIds) {
            GroupMemberDto memberDto = new GroupMemberDto();
            memberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            memberDto.setDeptId(deptId);
            memberDto.setMemberId(0L);
            memberDto.setConfirmFlag(0);
            if (!members.contains(memberDto)) {
                members.add(memberDto);
            }
        }
        doInsertCanteenMemberByDto(members, request.getOrgId(), canteen.getMealCanteenId());
        Set memberIdSet = new HashSet();
        Set deptIdSet = new HashSet();
        for (GroupMemberDto memberDto2 : members) {
            if (memberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(memberDto2.getDeptId());
            } else {
                memberIdSet.add(memberDto2.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
        }
        doInsertCanteenDeviceByDto(request.getCanteenDevices(), request.getOrgId(), canteen.getMealCanteenId(), DeviceDto.distinctDeviceDto(request.getCanteenDevices()));
        doInsertCanteenTimeByDto(request.getCanteenTimes(), request.getOrgId(), canteen.getMealCanteenId());
        doSendMemberNetty(request.getOrgId());
        doSendNettyMessage(request.getOrgId(), DeviceDto.distinctDeviceDto(request.getCanteenDevices()));
        return canteen.getMealCanteenId();
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    @Transactional(rollbackFor = {RuntimeException.class})
    public Long update(SaveMealCanteenRequest request) throws BeansException {
        checkSaveMealCanteenRequest(request);
        AssertUtil.checkId(request.getMealCanteenId(), OnpremiseErrorEnum.CANTEEN_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(this.canteenMapper.getOneById(request.getMealCanteenId(), request.getOrgId()), OnpremiseErrorEnum.CANTEEN_NOT_FIND);
        MealCanteen canteen = requestTransToCanteen(request);
        AssertUtil.isTrue(Boolean.valueOf(this.canteenMapper.update(canteen) > 0), OnpremiseErrorEnum.UPDATE_CANTEEN_FAIL);
        UpdateGroupDeviceInfo updateCanteenDevice = updateCanteenDevice(request);
        updateCanteenMember(request, updateCanteenDevice);
        updateCanteenTime(request);
        doSendMemberNetty(request.getOrgId());
        doSendNettyMessage(request.getOrgId(), DeviceDto.distinctDeviceDto(request.getCanteenDevices()));
        return canteen.getMealCanteenId();
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteById(DeleteMealCanteenRequest request) {
        checkIdParam(request.getMealCanteenId(), request.getOrgId(), request.getLoginAccountId());
        List<String> deviceSnList = new ArrayList<>();
        List<MealCanteenDevice> mealCanteenDeviceList = this.mealCanteenDeviceMapper.getByMealCanteenId(request.getOrgId(), request.getMealCanteenId());
        if (!CollectionUtils.isEmpty(mealCanteenDeviceList)) {
            for (MealCanteenDevice mealCanteenDevice : mealCanteenDeviceList) {
                if (!deviceSnList.contains(mealCanteenDevice.getDeviceSn())) {
                    deviceSnList.add(mealCanteenDevice.getDeviceSn());
                }
            }
        }
        doDeleteCanteenInfo(request.getMealCanteenId(), request.getOrgId());
        doSendMemberNetty(request.getOrgId());
        doSendNettyMessage(request.getOrgId(), deviceSnList);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteByName(DeleteMealCanteenRequest request) {
        MealCanteen mealCanteen = checkNameParam(request.getCanteenName(), request.getOrgId(), request.getLoginAccountId());
        List<String> deviceSnList = new ArrayList<>();
        List<MealCanteenDevice> mealCanteenDeviceList = this.mealCanteenDeviceMapper.getByMealCanteenId(request.getOrgId(), mealCanteen.getMealCanteenId());
        if (!CollectionUtils.isEmpty(mealCanteenDeviceList)) {
            for (MealCanteenDevice mealCanteenDevice : mealCanteenDeviceList) {
                if (!deviceSnList.contains(mealCanteenDevice.getDeviceSn())) {
                    deviceSnList.add(mealCanteenDevice.getDeviceSn());
                }
            }
        }
        doDeleteCanteenInfo(mealCanteen.getMealCanteenId(), request.getOrgId());
        doSendMemberNetty(request.getOrgId());
        doSendNettyMessage(request.getOrgId(), deviceSnList);
        return true;
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public QueryCanteenDetailsResponse getDetailsById(QueryCanteenDetailsRequest request) {
        MealCanteen canteen = checkIdParam(request.getMealCanteenId(), request.getOrgId(), request.getLoginAccountId());
        return packageCanteenDetailsResponse(canteen);
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public QueryCanteenDetailsResponse getDetailsByName(QueryCanteenDetailsRequest request) {
        MealCanteen canteen = checkNameParam(request.getCanteenName(), request.getOrgId(), request.getLoginAccountId());
        return packageCanteenDetailsResponse(canteen);
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public PageList<ListCanteenResponse> listCanteen(ListCanteenRequest request) {
        Paginator paginator = request.getPaginator();
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        if (account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || account.getModuleManager().intValue() == 1) {
            request.setLoginAccountId(null);
            request.setManageAccountId(null);
        } else {
            request.setLoginAccountId(account.getAccountId());
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<ListCanteenResponse> responses = this.canteenMapper.listCanteen(request);
            if (request.getDisableCount() == null || request.getDisableCount().intValue() != 1) {
                packagingCanteenList(responses, request.getOrgId());
            }
            return new PageList<>(responses);
        }
        List<ListCanteenResponse> responses2 = this.canteenMapper.listCanteen(request);
        if (request.getDisableCount() == null || request.getDisableCount().intValue() != 1) {
            packagingCanteenList(responses2, request.getOrgId());
        }
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public TerminalSyncCanteenResponse syncCanteen(TerminalSyncCanteenRequest request) {
        AssertUtil.isNullOrEmpty(request.getLastSyncTime(), OnpremiseErrorEnum.CHECK_IN_TASK_LAST_SYNC_TIME_LACK);
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.CHECK_IN_TASK_ORGID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.CHECK_IN_TASK_DEVICE_NO_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        logger.info("==============syncCanteen 设备信息：{}。", JsonUtils.toJson(device));
        if (device == null) {
            logger.info("==============syncCanteen 设备：{}不存在。", request.getDeviceSn());
            TerminalSyncCanteenResponse result = new TerminalSyncCanteenResponse();
            result.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCanteenLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
            return result;
        }
        return dealSyncResponse(request, device);
    }

    private TerminalSyncCanteenResponse dealSyncResponse(TerminalSyncCanteenRequest request, Device device) {
        TerminalSyncCanteenResponse result = new TerminalSyncCanteenResponse();
        MealCanteenDevice mealCanteenDevice = this.canteenDeviceMapper.getOneByDeviceSn(device.getOrgId(), device.getDeviceSn());
        logger.info("==============syncCanteen 设备关联餐厅信息：{}。", JsonUtils.toJson(mealCanteenDevice));
        if (mealCanteenDevice == null) {
            logger.info("==============syncCanteen 设备：{}没有关联的餐厅。", device.getDeviceSn());
            DeviceLastModifyTimeInfo tempCache = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCanteenLastModifyTimeKey(request.getDeviceSn()));
            result.setSyncTime(Long.valueOf(tempCache != null ? tempCache.getLastModifyTime().longValue() : 0L));
            return result;
        }
        MealCanteen mealCanteen = this.canteenMapper.getOneById(mealCanteenDevice.getMealCanteenId(), request.getOrgId());
        logger.info("==============syncCanteen 餐厅信息：{}。", JsonUtils.toJson(mealCanteen));
        result.setMealCanteenId(mealCanteen.getMealCanteenId());
        result.setCanteenName(mealCanteen.getCanteenName());
        result.setCanteenScope(mealCanteen.getCanteenScope());
        List<TerminalSyncCanteenResponse.CanteenTime> canteenTimeList = new ArrayList<>();
        List<CanteenTimeDto> canteenTimeDtoList = this.canteenTimeMapper.listTimeByCanteenId(mealCanteen.getMealCanteenId(), request.getOrgId());
        for (CanteenTimeDto canteenTimeDto : canteenTimeDtoList) {
            TerminalSyncCanteenResponse.CanteenTime canteenTime = new TerminalSyncCanteenResponse.CanteenTime();
            canteenTime.setStartTime(canteenTimeDto.getStartTime());
            canteenTime.setEndTime(canteenTimeDto.getEndTime());
            canteenTime.setTimeType(canteenTimeDto.getTimeType());
            Date startTime = MyDateUtils.parseDate(canteenTimeDto.getStartTime(), "HH:mm");
            Date endTime = MyDateUtils.parseDate(canteenTimeDto.getEndTime(), "HH:mm");
            if (startTime.after(endTime)) {
                canteenTime.setEndTime(Constants.DEFAULT_END_TIME);
                TerminalSyncCanteenResponse.CanteenTime canteenTimeTomorrow = new TerminalSyncCanteenResponse.CanteenTime();
                canteenTimeTomorrow.setStartTime(Constants.DEFAULT_START_TIME);
                canteenTimeTomorrow.setEndTime(canteenTimeDto.getEndTime());
                canteenTimeTomorrow.setTimeType(canteenTimeDto.getTimeType());
                canteenTimeList.add(canteenTimeTomorrow);
            }
            canteenTimeList.add(canteenTime);
        }
        result.setTimes(canteenTimeList);
        result.setSyncTime(Long.valueOf(mealCanteen.getGmtModify().getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getCanteenLastModifyTimeKey(request.getDeviceSn()), result.getSyncTime());
        return result;
    }

    private void doCacheLastModifyTime(String key, Long lastModifyTime) {
        DeviceLastModifyTimeInfo info = CacheAdapter.getLastModifyTime(key);
        if (info == null) {
            info = new DeviceLastModifyTimeInfo();
        }
        info.setLastModifyTime(lastModifyTime);
        info.setExpireTime(Long.valueOf(MyDateUtils.addYears(new Date(), 30).getTime()));
        CacheAdapter.cacheLastModifyTime(key, info);
    }

    private boolean checkSaveMealCanteenRequest(SaveMealCanteenRequest request) {
        AssertUtil.isNullOrEmpty(request.getCanteenName(), OnpremiseErrorEnum.CANTEEN_NAME_MUST_NOT_NULL);
        if (MyListUtils.listIsEmpty(request.getCanteenDevices())) {
            for (DeviceDto deviceDto : request.getCanteenDevices()) {
                AssertUtil.isNullOrEmpty(deviceDto.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
            }
        }
        if (MyListUtils.listIsEmpty(request.getCanteenMembers())) {
            for (GroupMemberDto memberDto : request.getCanteenMembers()) {
                memberDto.checkParam();
            }
        }
        if (MyListUtils.listIsEmpty(request.getCanteenTimes())) {
            for (CanteenTimeDto timeDto : request.getCanteenTimes()) {
                AssertUtil.isNullOrEmpty(timeDto.getTimeType(), OnpremiseErrorEnum.CANTEEN_TIME_TYPE_ERROR);
                CanteenTimeTypeEnum canteenTimeTypeEnum = CanteenTimeTypeEnum.getByValue(timeDto.getTimeType().intValue());
                AssertUtil.isNullOrEmpty(canteenTimeTypeEnum, OnpremiseErrorEnum.CANTEEN_TIME_TYPE_ERROR);
                switch (canteenTimeTypeEnum) {
                    case BREAKFAST_TIME:
                        checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                        break;
                    case LUNCH_TIME:
                        checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                        break;
                    case DINNER_TIME:
                        checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                        break;
                    case MIDNIGHT_SNACK_TIME:
                        checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                        break;
                }
            }
        }
        MealCanteen canteen = this.canteenMapper.getOneByCanteenName(request.getCanteenName(), request.getOrgId());
        if (request.getMealCanteenId() == null || request.getMealCanteenId().longValue() <= 0) {
            AssertUtil.isTrue(Boolean.valueOf(canteen == null), OnpremiseErrorEnum.CANTEEN_NAME_ALREADY_EXIST);
            return true;
        }
        AssertUtil.isTrue(Boolean.valueOf(canteen == null || request.getMealCanteenId().longValue() == canteen.getMealCanteenId().longValue()), OnpremiseErrorEnum.CANTEEN_NAME_ALREADY_EXIST);
        return true;
    }

    private void packagingCanteenList(List<ListCanteenResponse> responses, Long orgId) {
        if (responses != null && responses.size() > 0) {
            for (ListCanteenResponse response : responses) {
                doPackagingCanteenList(response, orgId);
            }
        }
    }

    private void doPackagingCanteenList(ListCanteenResponse response, Long orgId) {
        Integer memberNum = 0;
        List<Long> deptIds = new ArrayList<>();
        List<MealCanteenMember> groupDepts = this.canteenMemberMapper.listByCanteenIdAndType(response.getMealCanteenId(), orgId, MemberTypeEnum.DEPT.getValue());
        for (MealCanteenMember dept : groupDepts) {
            deptIds.add(dept.getDeptId());
        }
        List<Long> memberIds = new ArrayList<>();
        List<MealCanteenMember> groupMembers = this.canteenMemberMapper.listByCanteenIdAndType(response.getMealCanteenId(), orgId, MemberTypeEnum.MEMBER.getValue());
        for (MealCanteenMember member : groupMembers) {
            memberIds.add(member.getMemberId());
        }
        if (deptIds.size() > 0) {
            memberNum = Integer.valueOf(memberNum.intValue() + this.memberMapper.countMemberByDepts(deptIds, memberIds, orgId).intValue());
        }
        response.setMemberNum(Integer.valueOf(memberNum.intValue() + memberIds.size()));
        response.setDeviceNum(this.canteenDeviceMapper.countDeviceByCanteenId(response.getMealCanteenId(), orgId));
        Account account = this.accountMapper.getAccountInfoIncludDelete(response.getAccountId(), orgId);
        response.setAccountName(account == null ? "" : account.getAccountName());
    }

    private MealCanteen requestTransToCanteen(SaveMealCanteenRequest request) throws BeansException {
        MealCanteen canteen = new MealCanteen();
        BeanUtils.copyProperties(request, canteen);
        canteen.setAccountId(request.getLoginAccountId());
        canteen.setOrgId(request.getOrgId());
        return canteen;
    }

    private UpdateGroupDeviceInfo updateCanteenDevice(SaveMealCanteenRequest request) throws BeansException {
        List<DeviceDto> newDevices = request.getCanteenDevices();
        List<DeviceDto> oldDevices = this.canteenDeviceMapper.listDeviceByCanteenIdNotDelete(request.getMealCanteenId(), request.getOrgId());
        MyListUtils<DeviceDto> deviceUtils = new MyListUtils<>();
        List<DeviceDto> insertDevices = deviceUtils.difference(newDevices, oldDevices);
        List<DeviceDto> deleteDevices = deviceUtils.difference(oldDevices, newDevices);
        List<String> deviceSns = new ArrayList<>();
        if (MyListUtils.listIsEmpty(deleteDevices)) {
            for (DeviceDto dto : deleteDevices) {
                AssertUtil.isTrue(Boolean.valueOf(this.canteenDeviceMapper.deleteCanteenDevice(request.getMealCanteenId(), dto.getDeviceSn(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_CANTEEN_DEVICE_FAIL);
            }
        }
        if (MyListUtils.listIsEmpty(insertDevices)) {
            for (DeviceDto dto2 : insertDevices) {
                deviceSns.add(dto2.getDeviceSn());
            }
        }
        doInsertCanteenDeviceByDto(insertDevices, request.getOrgId(), request.getMealCanteenId(), deviceSns);
        return new UpdateGroupDeviceInfo(newDevices, oldDevices);
    }

    private boolean doInsertCanteenDeviceByDto(List<DeviceDto> insertDevices, Long orgId, Long mealCanteenId, List<String> deviceSns) throws BeansException {
        if (insertDevices != null && insertDevices.size() > 0) {
            this.canteenDeviceMapper.softDeleteByDeviceSns(new Date(), orgId, deviceSns);
            MealCanteenDevice device = new MealCanteenDevice();
            for (DeviceDto deviceDto : insertDevices) {
                BeanUtils.copyProperties(deviceDto, device);
                device.setOrgId(orgId);
                device.setMealCanteenId(mealCanteenId);
                AssertUtil.isTrue(Boolean.valueOf(this.canteenDeviceMapper.insertCanteenDevice(device) > 0), OnpremiseErrorEnum.INSERT_CANTEEN_DEVICE_FAIL);
                if (!deviceSns.contains(deviceDto.getDeviceSn())) {
                    deviceSns.add(deviceDto.getDeviceSn());
                }
            }
            return true;
        }
        return true;
    }

    private boolean updateCanteenTime(SaveMealCanteenRequest request) throws BeansException {
        List<CanteenTimeDto> newTime = request.getCanteenTimes();
        List<CanteenTimeDto> oldTime = this.canteenTimeMapper.listTimeByCanteenId(request.getMealCanteenId(), request.getOrgId());
        MyListUtils<CanteenTimeDto> timeUtils = new MyListUtils<>();
        List<CanteenTimeDto> insertTime = timeUtils.difference(newTime, oldTime);
        List<CanteenTimeDto> deleteTime = timeUtils.difference(oldTime, newTime);
        if (MyListUtils.listIsEmpty(deleteTime)) {
            for (CanteenTimeDto dto : deleteTime) {
                AssertUtil.isTrue(Boolean.valueOf(this.canteenTimeMapper.deleteCanteenTime(request.getMealCanteenId(), request.getOrgId(), dto.getMealCanteenTimeId()) > 0), OnpremiseErrorEnum.DELETE_GROUP_AUTH_FAIL);
            }
        }
        doInsertCanteenTimeByDto(insertTime, request.getOrgId(), request.getMealCanteenId());
        return true;
    }

    private boolean doInsertCanteenTimeByDto(List<CanteenTimeDto> time, Long orgId, Long mealCanteenId) throws BeansException {
        if (time != null && time.size() > 0) {
            MealCanteenTime canteenTime = new MealCanteenTime();
            for (CanteenTimeDto timeDto : time) {
                checkCanteenTimeDto(timeDto);
                BeanUtils.copyProperties(timeDto, canteenTime);
                canteenTime.setOrgId(orgId);
                canteenTime.setMealCanteenId(mealCanteenId);
                AssertUtil.isTrue(Boolean.valueOf(this.canteenTimeMapper.insertCanteenTime(canteenTime) > 0), OnpremiseErrorEnum.INSERT_GROUP_AUTH_FAIL);
            }
            return true;
        }
        return true;
    }

    private boolean checkCanteenTimeDto(CanteenTimeDto timeDto) {
        AssertUtil.isNullOrEmpty(timeDto.getTimeType(), OnpremiseErrorEnum.CANTEEN_TIME_TYPE_ERROR);
        CanteenTimeTypeEnum canteenTimeTypeEnum = CanteenTimeTypeEnum.getByValue(timeDto.getTimeType().intValue());
        AssertUtil.isNullOrEmpty(canteenTimeTypeEnum, OnpremiseErrorEnum.CANTEEN_TIME_TYPE_ERROR);
        switch (canteenTimeTypeEnum) {
            case BREAKFAST_TIME:
                checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                break;
            case LUNCH_TIME:
                checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                break;
            case DINNER_TIME:
                checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                break;
            case MIDNIGHT_SNACK_TIME:
                checkCanteenTime(timeDto.getStartTime(), timeDto.getEndTime());
                break;
        }
        return true;
    }

    private boolean checkCanteenTime(String startTime, String endTime) {
        AssertUtil.isNullOrEmpty(startTime, OnpremiseErrorEnum.START_TIME_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(MyDateUtils.checkHHMMTime(startTime)), OnpremiseErrorEnum.START_TIME_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(endTime, OnpremiseErrorEnum.END_TIME_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(MyDateUtils.checkHHMMTime(endTime)), OnpremiseErrorEnum.END_TIME_MUST_NOT_NULL);
        return true;
    }

    private boolean updateCanteenMember(SaveMealCanteenRequest request, UpdateGroupDeviceInfo updateCanteenDevice) throws BeansException {
        Set<Long> memberIdSet = new HashSet<>();
        Set<Long> deptIdSet = new HashSet<>();
        List<GroupMemberDto> newMembers = request.getCanteenMembers();
        ArrayList arrayList = new ArrayList();
        for (GroupMemberDto item : newMembers) {
            item.setConfirmFlag(1);
            if (item.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                this.deptService.packagingChildDept(item.getDeptId(), request.getOrgId(), arrayList);
            }
        }
        HashSet hashSet = new HashSet();
        List<Long> noRepeatChildDeptIds = new ArrayList<>();
        hashSet.addAll(arrayList);
        noRepeatChildDeptIds.addAll(hashSet);
        for (Long deptId : noRepeatChildDeptIds) {
            GroupMemberDto memberDto = new GroupMemberDto();
            memberDto.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
            memberDto.setDeptId(deptId);
            memberDto.setMemberId(0L);
            memberDto.setConfirmFlag(0);
            if (!newMembers.contains(memberDto)) {
                newMembers.add(memberDto);
            }
        }
        List<GroupMemberDto> oldMembers = this.canteenMemberMapper.listMemberByCanteenId(request.getMealCanteenId(), request.getOrgId());
        logger.info("newMembers :{}", JsonUtils.toJson(newMembers));
        logger.info("oldMembers :{}", JsonUtils.toJson(oldMembers));
        MyListUtils<GroupMemberDto> memberUtils = new MyListUtils<>();
        List<GroupMemberDto> insertMembers = memberUtils.difference(newMembers, oldMembers);
        List<GroupMemberDto> deleteMembers = memberUtils.difference(oldMembers, newMembers);
        logger.info("insertMembers size :{} , deleteMembers size :{}", Integer.valueOf(insertMembers.size()), Integer.valueOf(deleteMembers.size()));
        if (MyListUtils.listIsEmpty(deleteMembers)) {
            logger.info("================update delete member auth info");
            for (GroupMemberDto dto : deleteMembers) {
                if (MemberTypeEnum.MEMBER.getValue() == dto.getType().intValue()) {
                    this.canteenMemberMapper.deleteCanteenByMember(dto.getMemberId(), request.getOrgId(), request.getMealCanteenId());
                    memberIdSet.add(dto.getMemberId());
                } else {
                    this.canteenMemberMapper.deleteCanteenByDept(dto.getDeptId(), request.getOrgId(), request.getMealCanteenId());
                    deptIdSet.add(dto.getDeptId());
                }
            }
        }
        doInsertCanteenMemberByDto(insertMembers, request.getOrgId(), request.getMealCanteenId());
        for (GroupMemberDto memberDto2 : insertMembers) {
            if (memberDto2.getType().equals(Integer.valueOf(MemberTypeEnum.DEPT.getValue()))) {
                deptIdSet.add(memberDto2.getDeptId());
            } else {
                memberIdSet.add(memberDto2.getMemberId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdSet) || !CollectionUtils.isEmpty(deptIdSet)) {
            this.memberMapper.updateModifyTime(request.getOrgId(), new ArrayList(memberIdSet), new ArrayList(deptIdSet));
            return true;
        }
        return true;
    }

    private void deleteChildDeptCanteen(Long deptId, Long orgId, Long mealCanteenId) {
        List<ListDeptResponse> childDepts = this.deptMapper.listChildDept(deptId, orgId);
        if (MyListUtils.listIsEmpty(childDepts)) {
            for (ListDeptResponse response : childDepts) {
                this.canteenMemberMapper.deleteCanteenByDept(response.getDeptId(), orgId, mealCanteenId);
                deleteChildDeptCanteen(response.getDeptId(), orgId, mealCanteenId);
            }
        }
    }

    private boolean doInsertCanteenMemberByDto(List<GroupMemberDto> members, Long orgId, Long mealCanteenId) throws BeansException {
        if (members != null && members.size() > 0) {
            MealCanteenMember canteenMember = new MealCanteenMember();
            for (GroupMemberDto memberDto : members) {
                BeanUtils.copyProperties(memberDto, canteenMember);
                canteenMember.setOrgId(orgId);
                canteenMember.setMealCanteenId(mealCanteenId);
                canteenMember.setConfirmFlag(memberDto.getConfirmFlag());
                AssertUtil.isTrue(Boolean.valueOf(this.canteenMemberMapper.insertCanteenMember(canteenMember) > 0), OnpremiseErrorEnum.INSERT_CANTEEN_MEMBER_FAIL);
            }
            return true;
        }
        return true;
    }

    private void insertChildDeptCanteen(Long orgId, MealCanteenMember canteenMember) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(canteenMember.getDeptId(), orgId);
        logger.info("==============child dept is :{}", JsonUtils.toJson(childDepts));
        if (childDepts != null && childDepts.size() > 0) {
            for (Dept childDept : childDepts) {
                canteenMember.setDeptId(childDept.getDeptId());
                canteenMember.setConfirmFlag(0);
                logger.info("===============insert child dept ");
                this.canteenMemberMapper.insertCanteenMember(canteenMember);
                insertChildDeptCanteen(orgId, canteenMember);
            }
        }
    }

    private void passDept(List<MemberDto> memberDtos, Long orgId) {
        List<MemberDto> temp = new ArrayList<>();
        temp.addAll(memberDtos);
        for (MemberDto dto : temp) {
            doPassDept(memberDtos, orgId, dto);
        }
    }

    private void doPassDept(List<MemberDto> memberDtos, Long orgId, MemberDto dto) {
        List<Dept> childDepts = this.deptMapper.listChildDeptToReturnDept(Long.valueOf(Long.parseLong(dto.getDeptId())), orgId);
        if (childDepts != null && childDepts.size() > 0) {
            MemberDto var = new MemberDto();
            for (Dept childDept : childDepts) {
                var.setDeptId(childDept.getDeptId().toString());
                var.setDeptName(childDept.getDeptName());
                var.setType(Integer.valueOf(MemberTypeEnum.DEPT.getValue()));
                if (memberDtos.contains(var)) {
                    memberDtos.remove(var);
                }
                doPassDept(memberDtos, orgId, var);
            }
        }
    }

    private boolean doDeleteCanteenInfo(Long canteenId, Long orgId) {
        AssertUtil.isTrue(Boolean.valueOf(this.canteenMapper.deleteById(canteenId, orgId) > 0), OnpremiseErrorEnum.DELETE_CANTEEN_FAIL);
        List<GroupMemberDto> memberDtoList = this.canteenMemberMapper.listMemberByCanteenId(canteenId, orgId);
        List<Long> memberIdList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        for (GroupMemberDto memberDto : memberDtoList) {
            if (memberDto.getType().equals(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()))) {
                memberIdList.add(memberDto.getMemberId());
            } else {
                deptIdList.add(memberDto.getDeptId());
            }
        }
        if (!CollectionUtils.isEmpty(memberIdList) || !CollectionUtils.isEmpty(deptIdList)) {
            this.memberMapper.updateModifyTime(orgId, memberIdList, deptIdList);
        }
        this.canteenTimeMapper.deleteCanteenTime(canteenId, orgId, null);
        this.canteenMemberMapper.deleteCanteenMember(orgId, canteenId);
        List<DeviceDto> deviceDtos = this.canteenDeviceMapper.listDeviceByCanteenIdNotDelete(canteenId, orgId);
        if (MyListUtils.listIsEmpty(deviceDtos)) {
            this.canteenDeviceMapper.deleteCanteenDevice(canteenId, null, orgId);
            return true;
        }
        return true;
    }

    private void doSendNettyMessage(Long orgId, List<String> deviceSns) {
        MealCanteen canteen = this.canteenMapper.getLastModify(orgId);
        if (MyListUtils.listIsEmpty(deviceSns)) {
            for (String deviceSn : deviceSns) {
                logger.info("====================send deviceSn:{}", deviceSn);
                this.nettyMessageApi.sendMsg(new SyncCanteenRequest(Long.valueOf(canteen == null ? 0L : canteen.getGmtModify().getTime()), canteen.getOrgId()), Integer.valueOf(SyncCanteenRequest.MODEL_TYPE.type()), deviceSn);
            }
        }
    }

    private void doSendMemberNetty(Long orgId) {
        Member member = this.memberMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncMemberRequest(Long.valueOf(member == null ? 0L : member.getGmtModify().getTime()), orgId), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private MealCanteen checkNameParam(String canteenName, Long orgId, Long accountId) {
        AssertUtil.isNullOrEmpty(canteenName, OnpremiseErrorEnum.CANTEEN_NAME_MUST_NOT_NULL);
        MealCanteen canteen = this.canteenMapper.getOneByCanteenName(canteenName, orgId);
        AssertUtil.isNullOrEmpty(canteen, OnpremiseErrorEnum.GROUP_NOT_FIND);
        return canteen;
    }

    private MealCanteen checkIdParam(Long canteenId, Long orgId, Long accountId) {
        AssertUtil.checkId(canteenId, OnpremiseErrorEnum.CANTEEN_ID_MUST_NOT_NULL);
        MealCanteen canteen = this.canteenMapper.getOneById(canteenId, orgId);
        AssertUtil.isNullOrEmpty(canteen, OnpremiseErrorEnum.GROUP_NOT_FIND);
        return canteen;
    }

    private void checkAccount(Long accountId, Long orgId, Long loginAccountId) {
        Account account = this.accountMapper.getAccountInfo(loginAccountId, orgId);
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(account.getAccountGrade().intValue() == AccountGradeEnum.SUPER_ACCOUNT.getValue() || account.getAccountGrade().intValue() == AccountGradeEnum.MAIN_ACCOUNT.getValue() || loginAccountId.longValue() == accountId.longValue()), OnpremiseErrorEnum.NOT_ALLOW_OPERATE_OTHERS_CANTEEN);
    }

    private QueryCanteenDetailsResponse packageCanteenDetailsResponse(MealCanteen canteen) throws BeansException {
        QueryCanteenDetailsResponse response = new QueryCanteenDetailsResponse();
        BeanUtils.copyProperties(canteen, response);
        response.setCanteenDevices(this.canteenDeviceMapper.listDeviceByCanteenIdNotDelete(canteen.getMealCanteenId(), canteen.getOrgId()));
        response.setCanteenMembers(this.canteenMemberMapper.listConfirmMemberByCanteenId(canteen.getMealCanteenId(), canteen.getOrgId()));
        response.setCanteenTimes(this.canteenTimeMapper.listTimeByCanteenId(canteen.getMealCanteenId(), canteen.getOrgId()));
        return response;
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void generateMealCanteenBaseLibraryByMember(Long memberId, Long orgId, boolean flag) {
        if (flag) {
            this.memberMealCanteenSyncMapper.softDeleteByMember(memberId, orgId);
            return;
        }
        Member member = this.memberMapper.getMemberInfoByMemberId(memberId, orgId);
        if (member == null) {
            return;
        }
        MemberMealCanteenSync memberMealCanteenSync = this.memberMealCanteenSyncMapper.findByMember(memberId, orgId);
        if (memberMealCanteenSync == null) {
            MemberMealCanteenSync memberMealCanteenSync2 = new MemberMealCanteenSync();
            memberMealCanteenSync2.setOrgId(orgId);
            memberMealCanteenSync2.setMemberId(memberId);
            if (dealMemberMealCanteenSync(member, memberMealCanteenSync2)) {
                this.memberMealCanteenSyncMapper.insertMemberMealCanteenSync(memberMealCanteenSync2);
                return;
            }
            return;
        }
        if (dealMemberMealCanteenSync(member, memberMealCanteenSync)) {
            this.memberMealCanteenSyncMapper.updateByMember(memberMealCanteenSync);
        } else {
            this.memberMealCanteenSyncMapper.softDeleteByMember(memberId, orgId);
        }
    }

    private boolean dealMemberMealCanteenSync(Member member, MemberMealCanteenSync memberMealCanteenSync) {
        memberMealCanteenSync.setDeptId(member.getDeptId());
        List<Long> mealCanteenIdList = new ArrayList<>();
        Map memberMap = new HashMap(3);
        memberMap.put("orgId", member.getOrgId());
        memberMap.put("memberId", member.getMemberId());
        List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.findCond(memberMap);
        if (!CollectionUtils.isEmpty(mealCanteenMemberList)) {
            for (MealCanteenMember mealCanteenMember : mealCanteenMemberList) {
                if (!mealCanteenIdList.contains(mealCanteenMember.getMealCanteenId())) {
                    mealCanteenIdList.add(mealCanteenMember.getMealCanteenId());
                }
            }
        }
        List<Long> deptIdList = new ArrayList<>();
        String[] deptIds = member.getDeptId().split(",");
        for (String deptId : deptIds) {
            if (!StringUtils.isEmpty(deptId)) {
                dealSuperDept(Long.valueOf(Long.parseLong(deptId)), deptIdList);
            }
        }
        for (Long deptId2 : deptIdList) {
            Map deptMap = new HashMap(3);
            deptMap.put("orgId", member.getOrgId());
            deptMap.put("deptId", deptId2);
            List<MealCanteenMember> mealCanteenDeptList = this.mealCanteenMemberMapper.findCond(deptMap);
            if (!CollectionUtils.isEmpty(mealCanteenDeptList)) {
                for (MealCanteenMember mealCanteenMember2 : mealCanteenDeptList) {
                    if (!mealCanteenIdList.contains(mealCanteenMember2.getMealCanteenId())) {
                        mealCanteenIdList.add(mealCanteenMember2.getMealCanteenId());
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(mealCanteenIdList)) {
            return false;
        }
        String checkInTaskIdStrTemp = Arrays.toString(mealCanteenIdList.toArray());
        String checkInTaskIdStrTemp2 = checkInTaskIdStrTemp.substring(1, checkInTaskIdStrTemp.length());
        String checkInTaskIdStr = checkInTaskIdStrTemp2.substring(0, checkInTaskIdStrTemp2.length() - 1);
        memberMealCanteenSync.setMealCanteenIds(checkInTaskIdStr.replaceAll(SymbolConstants.SPACE_SYMBOL, ""));
        List<String> deviceSnList = new ArrayList<>();
        for (Long mealCanteenId : mealCanteenIdList) {
            List<MealCanteenDevice> mealCanteenDeviceList = this.mealCanteenDeviceMapper.getByMealCanteenId(member.getOrgId(), mealCanteenId);
            if (!CollectionUtils.isEmpty(mealCanteenDeviceList)) {
                for (MealCanteenDevice mealCanteenDevice : mealCanteenDeviceList) {
                    if (!deviceSnList.contains(mealCanteenDevice.getDeviceSn())) {
                        deviceSnList.add(mealCanteenDevice.getDeviceSn());
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return false;
        }
        String deviceSnStrTemp = Arrays.toString(deviceSnList.toArray());
        String deviceSnStrTemp2 = deviceSnStrTemp.substring(1, deviceSnStrTemp.length());
        String deviceSnStr = deviceSnStrTemp2.substring(0, deviceSnStrTemp2.length() - 1);
        memberMealCanteenSync.setNewestDeviceSns(deviceSnStr.replaceAll(SymbolConstants.SPACE_SYMBOL, ""));
        return true;
    }

    private void dealSuperDept(Long deptId, List<Long> deptIdList) {
        deptIdList.add(deptId);
        Dept dept = this.deptMapper.getDeptById(deptId);
        Dept superDept = this.deptMapper.getDeptById(dept.getSuperDeptId());
        if (superDept != null) {
            dealSuperDept(superDept.getDeptId(), deptIdList);
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void generateMealCanteenBaseLibraryByDept(Long deptId, Long orgId) {
        List<Long> memberIdList = new ArrayList<>();
        dealDeptMember(memberIdList, deptId, orgId);
        if (CollectionUtils.isEmpty(memberIdList)) {
            return;
        }
        for (Long memberId : memberIdList) {
            generateMealCanteenBaseLibraryByMember(memberId, orgId, false);
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void generateMealCanteenBaseLibraryByMealCanteen(Long mealCanteenId, Long orgId, boolean flag) {
        if (flag) {
            for (Long memberId : this.memberMealCanteenSyncMapper.getMemberListByMealCanteen(mealCanteenId, orgId)) {
                generateMealCanteenBaseLibraryByMember(memberId, orgId, false);
            }
            return;
        }
        List<Long> memberIdListOld = this.memberMealCanteenSyncMapper.getMemberListByMealCanteen(mealCanteenId, orgId);
        ArrayList arrayList = new ArrayList();
        List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.getByMealCanteenId(orgId, mealCanteenId);
        if (!CollectionUtils.isEmpty(mealCanteenMemberList)) {
            for (MealCanteenMember mealCanteenMember : mealCanteenMemberList) {
                if (mealCanteenMember.getType().equals(Integer.valueOf(MealCanteenMemberTypeEnum.DEPT.getValue()))) {
                    dealDeptMember(arrayList, mealCanteenMember.getDeptId(), mealCanteenMember.getOrgId());
                } else {
                    arrayList.add(mealCanteenMember.getMemberId());
                }
            }
        }
        List<Long> memberIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(arrayList)) {
            memberIdList.addAll(memberIdListOld);
        } else if (CollectionUtils.isEmpty(memberIdListOld)) {
            memberIdList.addAll(arrayList);
        } else {
            arrayList.removeAll(memberIdListOld);
            arrayList.addAll(memberIdListOld);
            memberIdList.addAll(arrayList);
        }
        for (Long memberId2 : memberIdList) {
            generateMealCanteenBaseLibraryByMember(memberId2, orgId, false);
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void generateMealCanteenBaseLibraryByDevice(String deviceSn, Long orgId) {
        this.memberMealCanteenSyncMapper.softDeleteByDevice(deviceSn, orgId);
        this.memberMealCanteenSyncMapper.updateNewestDeviceSnsByDevice(deviceSn, orgId);
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public TerminalSyncResponse<TerminalSyncMemberMealCanteenResponse> syncMealCanteenMember(TerminalSyncRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getLastSyncTime() != null), OnpremiseErrorEnum.LAST_SYNC_TIME_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            return new TerminalSyncResponse<>();
        }
        String lastSyncTime = MyDateUtils.getDateString(request.getLastSyncTime().longValue(), "yyyy-MM-dd HH:mm:ss.SSS");
        List<TerminalSyncMemberMealCanteenResponse> terminalSyncMemberMealCanteenResponseList = this.memberMealCanteenSyncMapper.getListForSync(request.getOrgId(), lastSyncTime, request.getDeviceSn());
        List<TerminalSyncMemberMealCanteenResponse> syncInsertResult = new ArrayList<>();
        List<TerminalSyncMemberMealCanteenResponse> syncModifyResult = new ArrayList<>();
        List<TerminalSyncMemberMealCanteenResponse> syncDeleteResult = new ArrayList<>();
        for (TerminalSyncMemberMealCanteenResponse response : terminalSyncMemberMealCanteenResponseList) {
            if (response.getDeleteOrNot().intValue() == DeleteOrNotEnum.DELETE_YES.getValue()) {
                if (request.getLastSyncTime().longValue() > 0) {
                    syncDeleteResult.add(response);
                }
            } else if (request.getLastSyncTime().longValue() == 0) {
                syncInsertResult.add(response);
            } else if (response.getLastSyncDeviceSns().contains(request.getDeviceSn())) {
                if (response.getNewestDeviceSns().contains(request.getDeviceSn())) {
                    syncModifyResult.add(response);
                } else {
                    syncDeleteResult.add(response);
                }
            } else {
                syncInsertResult.add(response);
            }
        }
        TerminalSyncResponse terminalSyncResponse = new TerminalSyncResponse();
        terminalSyncResponse.setSyncInsertResult(syncInsertResult);
        terminalSyncResponse.setSyncDeleteResult(syncDeleteResult);
        terminalSyncResponse.setSyncModifyResult(syncModifyResult);
        Date date = this.memberMealCanteenSyncMapper.getLastModify(request.getOrgId());
        terminalSyncResponse.setSyncTime(Long.valueOf(date == null ? 0L : date.getTime()));
        doCacheLastModifyTime(CacheKeyGenerateUtils.getMealCanteenMemberLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        this.memberMealCanteenSyncMapper.updateLastDeviceSnsByDevice(request.getDeviceSn(), request.getOrgId(), lastSyncTime);
        return terminalSyncResponse;
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void doSendNettyMessageForSyncMember(Long orgId) {
        Date date = this.memberMealCanteenSyncMapper.getLastModify(orgId);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncCanteenMemberRequest(Long.valueOf(date == null ? 0L : date.getTime()), orgId), Integer.valueOf(SyncCanteenMemberRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void dealDeptMember(List<Long> memberIdList, Long deptId, Long orgId) {
        List<DeptMemberListResponse> deptMemberListResponseList = this.memberMapper.listMemberByDeptId(deptId, orgId);
        if (!CollectionUtils.isEmpty(deptMemberListResponseList)) {
            for (DeptMemberListResponse itemDeptMember : deptMemberListResponseList) {
                memberIdList.add(itemDeptMember.getMemberId());
            }
        }
        List<Dept> childDeptList = this.deptMapper.listChildDeptToReturnDept(deptId, orgId);
        if (!CollectionUtils.isEmpty(childDeptList)) {
            for (Dept dept : childDeptList) {
                dealDeptMember(memberIdList, dept.getDeptId(), orgId);
            }
        }
    }

    @Override // com.moredian.onpremise.api.meal.MealCanteenService
    public void notify(Long orgId, Long id, Integer type) {
        Device device;
        MealCanteenDevice mealCanteenDevice;
        Date now = new Date();
        Map findMap = new HashMap(3);
        findMap.put("orgId", orgId);
        if (type.equals(Integer.valueOf(MealCanteenNotifyTypeEnum.DEPT.getValue()))) {
            findMap.put("deptId", id);
            findMap.put("deleteFlag", Integer.valueOf(CommonStatusEnum.NO.getValue()));
            List<MealCanteenMember> mealCanteenMemberList = this.mealCanteenMemberMapper.findCond(findMap);
            if (CollectionUtils.isEmpty(mealCanteenMemberList)) {
                return;
            }
            for (MealCanteenMember item : mealCanteenMemberList) {
                this.canteenMapper.updateModify(now, orgId, item.getMealCanteenId());
                this.mealCanteenMemberMapper.softDeleteById(now, orgId, item.getMealCanteenMemberId());
            }
            return;
        }
        if (type.equals(Integer.valueOf(MealCanteenNotifyTypeEnum.MEMBER.getValue()))) {
            findMap.put("memberId", id);
            findMap.put("deleteFlag", Integer.valueOf(CommonStatusEnum.NO.getValue()));
            List<MealCanteenMember> mealCanteenMemberList2 = this.mealCanteenMemberMapper.findCond(findMap);
            if (CollectionUtils.isEmpty(mealCanteenMemberList2)) {
                return;
            }
            for (MealCanteenMember item2 : mealCanteenMemberList2) {
                this.canteenMapper.updateModify(now, orgId, item2.getMealCanteenId());
                this.mealCanteenMemberMapper.softDeleteById(now, orgId, item2.getMealCanteenMemberId());
            }
            return;
        }
        if (type.equals(Integer.valueOf(MealCanteenNotifyTypeEnum.DEVICE.getValue())) && (device = this.deviceMapper.getDeviceInfoByDeviceId(id, orgId)) != null && (mealCanteenDevice = this.mealCanteenDeviceMapper.getOneByDeviceSn(orgId, device.getDeviceSn())) != null) {
            this.canteenMapper.updateModify(now, orgId, mealCanteenDevice.getMealCanteenId());
            this.mealCanteenDeviceMapper.softDeleteById(now, orgId, mealCanteenDevice.getMealCanteenDeviceId());
        }
    }
}
