package com.moredian.onpremise.core.adapter;

import com.moredian.onpremise.core.model.domain.Config;
import com.moredian.onpremise.core.model.dto.FreezeAccountDto;
import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.CacheUpgradeStatusInfo;
import com.moredian.onpremise.core.model.info.CurrentVersionInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.info.FireWarnStatusInfo;
import com.moredian.onpremise.core.model.response.FaceReloadProgressResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.UploadUpgradePackageResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.CacheUtils;
import com.moredian.onpremise.core.utils.RedisUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/adapter/CacheAdapter.class */
public class CacheAdapter {
    private static Integer deployType;

    @Value("${onpremise.deploy.type}")
    public void setDeployType(Integer deployType2) {
        deployType = deployType2;
    }

    public static void cacheServerIpAddressInfo(String key, String value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheServerIpAddressInfo(key, value);
        } else {
            RedisUtils.cacheServerIpAddressInfo(key, value);
        }
    }

    public static String getServerIpAddressInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getServerIpAddressInfo(key);
        }
        return RedisUtils.getServerIpAddressInfo(key);
    }

    public static void cacheFireWarnStatusInfo(String key, FireWarnStatusInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheFireWarnStatusInfo(key, value);
        } else {
            RedisUtils.cacheFireWarnStatusInfo(key, value);
        }
    }

    public static void removeFireWarnStatusInfo(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeFireWarnStatusInfo(key);
        } else {
            RedisUtils.removeFireWarnStatusInfo(key);
        }
    }

    public static FireWarnStatusInfo getFireWarnStatusInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getFireWarnStatusInfo(key);
        }
        return RedisUtils.getFireWarnStatusInfo(key);
    }

    public static void cacheHeartBeatInfo(String key, CacheHeartBeatInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheHeartBeatInfo(key, value);
        } else {
            RedisUtils.cacheHeartBeatInfo(key, value);
        }
    }

    public static void removeHeartBeatInfo(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeHeartBeatInfo(key);
        } else {
            RedisUtils.removeHeartBeatInfo(key);
        }
    }

    public static List<CacheHeartBeatInfo> removeExpireHeartBeatInfo() {
        if (deployType.intValue() == 0) {
            return CacheUtils.removeExpireHeartBeatInfo();
        }
        return new ArrayList();
    }

    public static CacheHeartBeatInfo getHeartBeatInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getHeartBeatInfo(key);
        }
        return RedisUtils.getHeartBeatInfo(key);
    }

    public static CacheHeartBeatInfo getLastHeartBeatInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getLastHeartBeatInfo(key);
        }
        return RedisUtils.getLastHeartBeatInfo(key);
    }

    public static Map<String, CacheHeartBeatInfo> getHeartBeatInfoAll() {
        if (deployType.intValue() == 0) {
            return CacheUtils.getHeartBeatInfoAll();
        }
        return RedisUtils.getHeartBeatInfoAll();
    }

    public static void cacheUpgradeStatusInfo(String key, CacheUpgradeStatusInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheUpgradeStatusInfo(key, value);
        } else {
            RedisUtils.cacheUpgradeStatusInfo(key, value);
        }
    }

    public static void removeUpgradeStatusInfo(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeUpgradeStatusInfo(key);
        } else {
            RedisUtils.removeUpgradeStatusInfo(key);
        }
    }

    public static CacheUpgradeStatusInfo getUpgradeStatusInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getUpgradeStatusInfo(key);
        }
        return RedisUtils.getUpgradeStatusInfo(key);
    }

    public static void updateUpgradeCacheStatus(String deviceSn, Long upgradeScheduleId, Integer status) {
        if (deployType.intValue() == 0) {
            CacheUtils.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, status);
        } else {
            RedisUtils.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, status);
        }
    }

    public static void updateUpgradeCacheStatus(String deviceSn, Long upgradeScheduleId, Integer status, Integer upgradeType) {
        if (deployType.intValue() == 0) {
            CacheUtils.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, status, upgradeType);
        } else {
            RedisUtils.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, status, upgradeType);
        }
    }

    public static Map<String, CacheUpgradeStatusInfo> getUpgradeStatusAll() {
        if (deployType.intValue() == 0) {
            return CacheUtils.getUpgradeStatusAll();
        }
        return RedisUtils.getUpgradeStatusAll();
    }

    public static void cacheLoginInfo(String key, UserLoginResponse value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheLoginInfo(key, value);
        } else {
            RedisUtils.cacheLoginInfo(key, value);
        }
    }

    public static void removeLoginInfo(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeLoginInfo(key);
        } else {
            RedisUtils.removeLoginInfo(key);
        }
    }

    public static UserLoginResponse getLoginInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getLoginInfo(key);
        }
        return RedisUtils.getLoginInfo(key);
    }

    public static void cacheExtractFeatureStatus(String key, CacheExtractFeatureStatusInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheExtractFeatureStatus(key, value);
        } else {
            RedisUtils.cacheExtractFeatureStatus(key, value);
        }
    }

    public static void removeExtractFeatureStatus(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeExtractFeatureStatus(key);
        } else {
            RedisUtils.removeExtractFeatureStatus(key);
        }
    }

    public static CacheExtractFeatureStatusInfo getExtractFeatureStatus(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getExtractFeatureStatus(key);
        }
        return RedisUtils.getExtractFeatureStatus(key);
    }

    public static void cacheBatchExtractFeatureStatus(String key, Map<String, CacheExtractFeatureStatusInfo> value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheBatchExtractFeatureStatus(key, value);
        } else {
            RedisUtils.cacheBatchExtractFeatureStatus(key, value);
        }
    }

    public static void removeBatchExtractFeatureStatus(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeBatchExtractFeatureStatus(key);
        } else {
            RedisUtils.removeBatchExtractFeatureStatus(key);
        }
    }

    public static Map<String, CacheExtractFeatureStatusInfo> getBatchExtractFeatureStatus(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getBatchExtractFeatureStatus(key);
        }
        return RedisUtils.getBatchExtractFeatureStatus(key);
    }

    public static void cacheLastModifyTime(String key, DeviceLastModifyTimeInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheLastModifyTime(key, value);
        } else {
            RedisUtils.cacheLastModifyTime(key, value);
        }
    }

    public static void removeLastModifyTime(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeLastModifyTime(key);
        } else {
            RedisUtils.removeLastModifyTime(key);
        }
    }

    public static DeviceLastModifyTimeInfo getLastModifyTime(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getLastModifyTime(key);
        }
        return RedisUtils.getLastModifyTime(key);
    }

    public static void cacheServerTimeZone(String key, String value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheServerTimeZone(key, value);
        } else {
            RedisUtils.cacheServerTimeZone(key, value);
        }
    }

    public static String getServerTimeZone(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getServerTimeZone(key);
        }
        return RedisUtils.getServerTimeZone(key);
    }

    public static void cacheServerOrgCode(String key, String value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheServerOrgCode(key, value);
        } else {
            RedisUtils.cacheServerOrgCode(key, value);
        }
    }

    public static String getServerOrgCode(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getServerOrgCode(key);
        }
        return RedisUtils.getServerOrgCode(key);
    }

    public static void cacheUpgradePackageComments(String key, UploadUpgradePackageResponse value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheUpgradePackageComments(key, value);
        } else {
            RedisUtils.cacheUpgradePackageComments(key, value);
        }
    }

    public static void removeUpgradePackageComments(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeUpgradePackageComments(key);
        } else {
            RedisUtils.removeUpgradePackageComments(key);
        }
    }

    public static UploadUpgradePackageResponse getUpgradePackageComments(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getUpgradePackageComments(key);
        }
        return RedisUtils.getUpgradePackageComments(key);
    }

    public static void cacheCurrentVersionInfo(String key, CurrentVersionInfo value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheCurrentVersionInfo(key, value);
        } else {
            RedisUtils.cacheCurrentVersionInfo(key, value);
        }
    }

    public static void removeCurrentVersionInfo(String key) {
        if (deployType.intValue() == 0) {
            CacheUtils.removeCurrentVersionInfo(key);
        } else {
            RedisUtils.removeCurrentVersionInfo(key);
        }
    }

    public static CurrentVersionInfo getCurrentVersionInfo(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getCurrentVersionInfo(key);
        }
        return RedisUtils.getCurrentVersionInfo(key);
    }

    public static void cacheDeviceServerMapInfo(String key, String value) {
        if (deployType.intValue() == 1) {
            RedisUtils.cacheDeviceServerMapInfo(key, value);
        }
    }

    public static String getDeviceServerMapInfo(String key) {
        if (deployType.intValue() == 1) {
            return RedisUtils.getDeviceServerMapInfo(key);
        }
        return "";
    }

    public static void removeDeviceServerMapInfo(String key) {
        if (deployType.intValue() == 1) {
            RedisUtils.removeDeviceServerMapInfo(key);
        }
    }

    public static void cacheSyncCount(String key, Integer value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheSyncCount(key, value);
        } else {
            RedisUtils.cacheSyncCount(key, value);
        }
    }

    public static Integer getSyncCount(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getSyncCount(key);
        }
        return RedisUtils.getSyncCount(key);
    }

    public static void cacheFaceReloadProgress(String key, FaceReloadProgressResponse value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheFaceReloadProgress(key, value);
        } else {
            RedisUtils.cacheFaceReloadProgress(key, value);
        }
    }

    public static FaceReloadProgressResponse getFaceReloadProgress(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getFaceReloadProgress(key);
        }
        return RedisUtils.getFaceReloadProgress(key);
    }

    public static void cacheFreezeAccount(String key, FreezeAccountDto value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheFreezeAccount(key, value);
        } else {
            RedisUtils.cacheFreezeAccount(key, value);
        }
    }

    public static FreezeAccountDto getFreezeAccount(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.getFreezeAccount(key);
        }
        return RedisUtils.getFreezeAccount(key);
    }

    public static synchronized void cacheConfig(String key, Config value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheConfig(key, value);
        } else {
            RedisUtils.cache(key, value);
        }
    }

    public static Config getConfig(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.queryConfig(key);
        }
        return (Config) RedisUtils.query(key, Config.class);
    }

    public static synchronized void cacheSet(String key, Set value) {
        if (deployType.intValue() == 0) {
            CacheUtils.cacheSet(key, value);
        }
    }

    public static Set getSet(String key) {
        if (deployType.intValue() == 0) {
            return CacheUtils.querySet(key);
        }
        return null;
    }

    public static synchronized void cacheSetMemberAdd(String key, Object value) {
        Set targetSet = getSet(key);
        if (targetSet == null) {
            targetSet = new HashSet();
        }
        targetSet.add(value);
        cacheSet(key, targetSet);
    }

    public static synchronized void cacheSetMemberRemove(String key, Object value) {
        Set targetSet = getSet(key);
        if (targetSet != null) {
            targetSet.remove(value);
        }
    }

    public static boolean checkSetMemberExist(String key, Object member) {
        Set targetSet = getSet(key);
        if (targetSet != null) {
            return targetSet.contains(member);
        }
        return false;
    }

    public static void cacheSyncMember(String key, TerminalSyncResponse<TerminalSyncMemberResponse> value) {
        RedisUtils.cacheSyncMember(key, value);
    }

    public static TerminalSyncResponse<TerminalSyncMemberResponse> getSyncMember(String key) {
        return RedisUtils.getSyncMember(key);
    }
}
