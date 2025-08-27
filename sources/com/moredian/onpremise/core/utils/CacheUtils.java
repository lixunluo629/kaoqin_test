package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.enums.ExtractFeatureStatusEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleTypeEnum;
import com.moredian.onpremise.core.model.domain.Config;
import com.moredian.onpremise.core.model.dto.FreezeAccountDto;
import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.CacheUpgradeStatusInfo;
import com.moredian.onpremise.core.model.info.CurrentVersionInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.info.FireWarnStatusInfo;
import com.moredian.onpremise.core.model.response.FaceReloadProgressResponse;
import com.moredian.onpremise.core.model.response.UploadUpgradePackageResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/CacheUtils.class */
public class CacheUtils {
    public static Map<String, UserLoginResponse> LOGIN_CACHE = new ConcurrentHashMap();
    public static Map<String, CacheHeartBeatInfo> HEART_BEAT_CACHE = new ConcurrentHashMap();
    private static Map<String, CacheUpgradeStatusInfo> DEVICE_UPGRADE_STATUS_CACHE = new ConcurrentHashMap();
    public static Map<String, CacheExtractFeatureStatusInfo> EXTRACT_FEATURE_STATUS_CACHE = new ConcurrentHashMap();
    public static Map<String, Map<String, CacheExtractFeatureStatusInfo>> BATCH_EXTRACT_FEATURE_STATUS_CACHE = new ConcurrentHashMap();
    public static Map<String, FireWarnStatusInfo> FIRE_WARN_STATUS_CACHE = new ConcurrentHashMap();
    public static Map<String, String> SERVER_IP_ADDRESS = new ConcurrentHashMap();
    public static Map<String, DeviceLastModifyTimeInfo> LAST_MODIFY_TIME = new ConcurrentHashMap();
    public static Map<String, String> SERVER_ORG_CODE = new ConcurrentHashMap(1);
    public static Map<String, UploadUpgradePackageResponse> UPGRADE_PACKAGE_COMMENTS = new ConcurrentHashMap();
    public static Map<String, CurrentVersionInfo> CURRENT_VERSION = new ConcurrentHashMap(1);
    public static Map<String, String> SERVER_TIME_ZONE = new ConcurrentHashMap(1);
    public static Map<String, Integer> SYNC_COUNT = new ConcurrentHashMap();
    public static Map<String, FaceReloadProgressResponse> FACE_RELOAD_PROGRESS = new ConcurrentHashMap();
    public static Map<String, FreezeAccountDto> FREEZE_ACCOUNT = new ConcurrentHashMap();
    public static Map<String, Config> COMMON_CACHE = new ConcurrentHashMap();
    public static Map<String, Set> COMMON_SET = new ConcurrentHashMap();

    public static void cacheServerIpAddressInfo(String key, String value) {
        SERVER_IP_ADDRESS.put(key, value);
    }

    public static String getServerIpAddressInfo(String key) {
        return SERVER_IP_ADDRESS.get(key);
    }

    public static void cacheFireWarnStatusInfo(String key, FireWarnStatusInfo value) {
        FIRE_WARN_STATUS_CACHE.put(key, value);
    }

    public static void removeFireWarnStatusInfo(String key) {
        FIRE_WARN_STATUS_CACHE.remove(key);
    }

    public static FireWarnStatusInfo getFireWarnStatusInfo(String key) {
        Iterator<Map.Entry<String, FireWarnStatusInfo>> it = FIRE_WARN_STATUS_CACHE.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, FireWarnStatusInfo> entry = it.next();
            FireWarnStatusInfo temp = entry.getValue();
            if (temp.getExpireTime().longValue() < System.currentTimeMillis()) {
                it.remove();
            }
        }
        FireWarnStatusInfo temp2 = FIRE_WARN_STATUS_CACHE.get(key);
        return temp2;
    }

    public static void cacheHeartBeatInfo(String key, CacheHeartBeatInfo value) {
        HEART_BEAT_CACHE.put(key, value);
    }

    public static void removeHeartBeatInfo(String key) {
        HEART_BEAT_CACHE.remove(key);
    }

    public static List<CacheHeartBeatInfo> removeExpireHeartBeatInfo() throws BeansException {
        List<CacheHeartBeatInfo> removeCacheHeartBeatInfoList = new ArrayList<>();
        Iterator<Map.Entry<String, CacheHeartBeatInfo>> it = HEART_BEAT_CACHE.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, CacheHeartBeatInfo> entry = it.next();
            CacheHeartBeatInfo temp = entry.getValue();
            if (temp.getExpireTime().longValue() < System.currentTimeMillis()) {
                CacheHeartBeatInfo remove = new CacheHeartBeatInfo();
                BeanUtils.copyProperties(temp, remove);
                removeCacheHeartBeatInfoList.add(remove);
                it.remove();
            }
        }
        return removeCacheHeartBeatInfoList;
    }

    public static CacheHeartBeatInfo getHeartBeatInfo(String key) {
        CacheHeartBeatInfo temp = HEART_BEAT_CACHE.get(key);
        if (temp == null || temp.getExpireTime().longValue() < System.currentTimeMillis()) {
            return null;
        }
        return temp;
    }

    public static CacheHeartBeatInfo getLastHeartBeatInfo(String key) {
        CacheHeartBeatInfo temp = HEART_BEAT_CACHE.get(key);
        if (temp == null) {
            return null;
        }
        return temp;
    }

    public static Map<String, CacheHeartBeatInfo> getHeartBeatInfoAll() {
        Map<String, CacheHeartBeatInfo> result = HEART_BEAT_CACHE;
        Iterator<Map.Entry<String, CacheHeartBeatInfo>> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            getHeartBeatInfo(iterator.next().getKey());
        }
        return HEART_BEAT_CACHE;
    }

    public static void cacheUpgradeStatusInfo(String key, CacheUpgradeStatusInfo value) {
        DEVICE_UPGRADE_STATUS_CACHE.put(key, value);
    }

    public static void removeUpgradeStatusInfo(String key) {
        DEVICE_UPGRADE_STATUS_CACHE.remove(key);
    }

    public static CacheUpgradeStatusInfo getUpgradeStatusInfo(String key) {
        CacheUpgradeStatusInfo temp = DEVICE_UPGRADE_STATUS_CACHE.get(key);
        if (temp == null) {
            return null;
        }
        return temp;
    }

    public static void updateUpgradeCacheStatus(String deviceSn, Long upgradeScheduleId, Integer status) {
        Integer upgradeType;
        CacheUpgradeStatusInfo upgradeStatusInfo = getUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(deviceSn));
        if (upgradeStatusInfo == null) {
            upgradeType = Integer.valueOf(UpgradeScheduleTypeEnum.ROM.getValue());
        } else {
            upgradeType = upgradeStatusInfo.getUpgradeType();
        }
        updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, status, upgradeType);
    }

    public static void updateUpgradeCacheStatus(String deviceSn, Long upgradeScheduleId, Integer status, Integer upgradeType) {
        CacheUpgradeStatusInfo upgradeStatusInfo = new CacheUpgradeStatusInfo();
        upgradeStatusInfo.setDeviceSn(deviceSn);
        upgradeStatusInfo.setUpgradeScheduleId(upgradeScheduleId);
        upgradeStatusInfo.setUpgradeStatus(status);
        upgradeStatusInfo.setUpgradeType(upgradeType);
        if (upgradeType.intValue() == UpgradeScheduleTypeEnum.ROM.getValue()) {
            upgradeStatusInfo.setExpireTime(Long.valueOf(MyDateUtils.addMinutes(new Date(), 90).getTime()));
        } else {
            upgradeStatusInfo.setExpireTime(Long.valueOf(MyDateUtils.addMinutes(new Date(), 30).getTime()));
        }
        cacheUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(deviceSn), upgradeStatusInfo);
    }

    public static Map<String, CacheUpgradeStatusInfo> getUpgradeStatusAll() {
        return DEVICE_UPGRADE_STATUS_CACHE;
    }

    public static void cacheLoginInfo(String key, UserLoginResponse value) {
        LOGIN_CACHE.put(key, value);
    }

    public static void removeLoginInfo(String key) {
        LOGIN_CACHE.remove(key);
    }

    public static UserLoginResponse getLoginInfo(String key) {
        Iterator<Map.Entry<String, UserLoginResponse>> it = LOGIN_CACHE.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, UserLoginResponse> entry = it.next();
            UserLoginResponse temp = entry.getValue();
            if (temp.getExpireTime().longValue() < System.currentTimeMillis()) {
                it.remove();
            }
        }
        UserLoginResponse temp2 = LOGIN_CACHE.get(key);
        return temp2;
    }

    public static void cacheExtractFeatureStatus(String key, CacheExtractFeatureStatusInfo value) {
        EXTRACT_FEATURE_STATUS_CACHE.put(key, value);
    }

    public static void removeExtractFeatureStatus(String key) {
        EXTRACT_FEATURE_STATUS_CACHE.remove(key);
    }

    public static CacheExtractFeatureStatusInfo getExtractFeatureStatus(String key) {
        CacheExtractFeatureStatusInfo temp = EXTRACT_FEATURE_STATUS_CACHE.get(key);
        if (temp == null) {
            return null;
        }
        if (temp.getExpireTime().longValue() < System.currentTimeMillis()) {
            removeExtractFeatureStatus(key);
            return null;
        }
        return temp;
    }

    public static void cacheBatchExtractFeatureStatus(String key, Map<String, CacheExtractFeatureStatusInfo> value) {
        if (BATCH_EXTRACT_FEATURE_STATUS_CACHE.get(key) != null) {
            Map<String, CacheExtractFeatureStatusInfo> values = BATCH_EXTRACT_FEATURE_STATUS_CACHE.get(key);
            values.putAll(value);
            BATCH_EXTRACT_FEATURE_STATUS_CACHE.put(key, values);
            return;
        }
        BATCH_EXTRACT_FEATURE_STATUS_CACHE.put(key, value);
    }

    public static void removeBatchExtractFeatureStatus(String key) {
        BATCH_EXTRACT_FEATURE_STATUS_CACHE.remove(key);
    }

    public static Map<String, CacheExtractFeatureStatusInfo> getBatchExtractFeatureStatus(String key) {
        Map<String, CacheExtractFeatureStatusInfo> temp = BATCH_EXTRACT_FEATURE_STATUS_CACHE.get(key);
        if (temp == null) {
            return null;
        }
        for (String mapKey : temp.keySet()) {
            CacheExtractFeatureStatusInfo info = temp.get(mapKey);
            if (info != null && info.getExpireTime().longValue() < System.currentTimeMillis() && info.getStatus().intValue() == ExtractFeatureStatusEnum.EXTRACTING_STATUS.getValue()) {
                info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.FAIL_STATUS.getValue()));
            }
            temp.put(mapKey, info);
        }
        return temp;
    }

    public static void cacheLastModifyTime(String key, DeviceLastModifyTimeInfo value) {
        LAST_MODIFY_TIME.put(key, value);
    }

    public static void removeLastModifyTime(String key) {
        LAST_MODIFY_TIME.remove(key);
    }

    public static DeviceLastModifyTimeInfo getLastModifyTime(String key) {
        DeviceLastModifyTimeInfo temp = LAST_MODIFY_TIME.get(key);
        if (temp == null) {
            return null;
        }
        if (temp.getExpireTime().longValue() < System.currentTimeMillis() || temp.getLastModifyTime() == null) {
            removeLastModifyTime(key);
            return null;
        }
        return temp;
    }

    public static void cacheServerTimeZone(String key, String value) {
        SERVER_TIME_ZONE.put(key, value);
    }

    public static String getServerTimeZone(String key) {
        return SERVER_TIME_ZONE.get(key);
    }

    public static void cacheServerOrgCode(String key, String value) {
        SERVER_ORG_CODE.put(key, value);
    }

    public static String getServerOrgCode(String key) {
        return SERVER_ORG_CODE.get(key);
    }

    public static void cacheUpgradePackageComments(String key, UploadUpgradePackageResponse value) {
        UPGRADE_PACKAGE_COMMENTS.put(key, value);
    }

    public static void removeUpgradePackageComments(String key) {
        UPGRADE_PACKAGE_COMMENTS.remove(key);
    }

    public static UploadUpgradePackageResponse getUpgradePackageComments(String key) {
        UploadUpgradePackageResponse temp = UPGRADE_PACKAGE_COMMENTS.get(key);
        if (temp == null) {
            return null;
        }
        return temp;
    }

    public static void cacheCurrentVersionInfo(String key, CurrentVersionInfo value) {
        CURRENT_VERSION.put(key, value);
    }

    public static void removeCurrentVersionInfo(String key) {
        CURRENT_VERSION.remove(key);
    }

    public static CurrentVersionInfo getCurrentVersionInfo(String key) {
        CurrentVersionInfo temp = CURRENT_VERSION.get(key);
        if (temp == null) {
            return null;
        }
        return temp;
    }

    public static void cacheSyncCount(String key, Integer value) {
        SYNC_COUNT.put(key, value);
    }

    public static Integer getSyncCount(String key) {
        return SYNC_COUNT.get(key);
    }

    public static void cacheFaceReloadProgress(String key, FaceReloadProgressResponse value) {
        FACE_RELOAD_PROGRESS.put(key, value);
    }

    public static FaceReloadProgressResponse getFaceReloadProgress(String key) {
        return FACE_RELOAD_PROGRESS.get(key);
    }

    public static void cacheFreezeAccount(String key, FreezeAccountDto value) {
        FREEZE_ACCOUNT.put(key, value);
    }

    public static FreezeAccountDto getFreezeAccount(String key) {
        FreezeAccountDto freezeAccountDto = FREEZE_ACCOUNT.get(key);
        if (freezeAccountDto != null && freezeAccountDto.getExpireTime() != null && freezeAccountDto.getExpireTime().longValue() > System.currentTimeMillis()) {
            return freezeAccountDto;
        }
        return null;
    }

    public static void cacheConfig(String key, Config value) {
        COMMON_CACHE.put(key, value);
    }

    public static Config queryConfig(String key) {
        return COMMON_CACHE.get(key);
    }

    public static void cacheSet(String key, Set value) {
        COMMON_SET.put(key, value);
    }

    public static Set querySet(String key) {
        return COMMON_SET.get(key);
    }
}
