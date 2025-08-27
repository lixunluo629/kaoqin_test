package com.moredian.onpremise.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.moredian.onpremise.core.common.enums.ExtractFeatureStatusEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleTypeEnum;
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
import io.netty.channel.ChannelId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/RedisUtils.class */
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static RedisUtils redisUtils;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) RedisUtils.class);
    public static String LOGIN_CACHE_KEY = "login_cache_";
    public static String HEART_BEAT_CACHE_KEY = "heart_beat_cache_";
    public static Map<String, CacheHeartBeatInfo> HEART_BEAT_CACHE = new ConcurrentHashMap();
    private static String DEVICE_UPGRADE_STATUS_CACHE_KEY = "device_upgrade_status_cache_";
    public static String EXTRACT_FEATURE_STATUS_CACHE_KEY = "extract_feature_status_cache_";
    public static String BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY = "batch_extract_feature_status_cache_";
    public static String FIRE_WARN_STATUS_CACHE_KEY = "fire_warn_status_cache_";
    public static String SERVER_IP_ADDRESS_KEY = "server_ip_address_";
    public static String LAST_MODIFY_TIME_KEY = "last_modify_time_";
    public static String SERVER_ORG_CODE_KEY = "server_org_code_";
    public static String UPGRADE_PACKAGE_COMMENTS_KEY = "upgrade_package_comments_";
    public static String CURRENT_VERSION_KEY = "current_version_";
    public static String SERVER_TIME_ZONE_KEY = "server_time_zone_";
    public static String TARGET_CHANNEL_MAP_KEY = "target_channel_map_";
    public static String DEVICE_SERVER_MAP_KEY = "device_server_map_";
    public static String SYNC_COUNT_KEY = "sync_count_map_";
    public static String FACE_RELOAD_PROGRESS_KEY = "face_reload_progress_map_";
    public static String FREE_ACCOUNT_KEY = "freeze_account_map_";
    public static String SYNC_MEMBER_KEY = "sync_member_map_";

    @PostConstruct
    public void init() {
        redisUtils = this;
        this.redisTemplate = this.redisTemplate;
    }

    public static void cacheServerIpAddressInfo(String key, String value) {
        cache(SERVER_IP_ADDRESS_KEY + key, value);
    }

    public static String getServerIpAddressInfo(String key) {
        return (String) query(SERVER_IP_ADDRESS_KEY + key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.1
        });
    }

    public static void cacheTargetChannelMapInfo(String key, ChannelId value) {
        logger.info("cache channel :{}", value.toString());
        cache(TARGET_CHANNEL_MAP_KEY + key, value.asShortText());
    }

    public static String getTargetChannelMapInfo(String key) {
        return (String) query(TARGET_CHANNEL_MAP_KEY + key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.2
        });
    }

    public static Map<String, String> getTargetChannelMapAllInfo() {
        HashMap map = new HashMap();
        Set<String> keys = keys(TARGET_CHANNEL_MAP_KEY + "*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                String tempKey = key.replace(HEART_BEAT_CACHE_KEY, "");
                map.put(tempKey, query(key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.3
                }));
            }
        }
        return map;
    }

    public static void removeTargetChannelMapInfo(String key) {
        expire(TARGET_CHANNEL_MAP_KEY + key);
    }

    public static void cacheDeviceServerMapInfo(String key, String value) {
        cache(DEVICE_SERVER_MAP_KEY + key, value);
    }

    public static String getDeviceServerMapInfo(String key) {
        return (String) query(DEVICE_SERVER_MAP_KEY + key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.4
        });
    }

    public static void removeDeviceServerMapInfo(String key) {
        expire(DEVICE_SERVER_MAP_KEY + key);
    }

    public static void cacheFireWarnStatusInfo(String key, FireWarnStatusInfo value) {
        cache(FIRE_WARN_STATUS_CACHE_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeFireWarnStatusInfo(String key) {
        expire(FIRE_WARN_STATUS_CACHE_KEY + key);
    }

    public static FireWarnStatusInfo getFireWarnStatusInfo(String key) {
        return (FireWarnStatusInfo) query(FIRE_WARN_STATUS_CACHE_KEY + key, new TypeReference<FireWarnStatusInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.5
        });
    }

    public static void cacheHeartBeatInfo(String key, CacheHeartBeatInfo value) {
        cache(HEART_BEAT_CACHE_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeHeartBeatInfo(String key) {
        expire(HEART_BEAT_CACHE_KEY + key);
    }

    public static CacheHeartBeatInfo getHeartBeatInfo(String key) {
        return (CacheHeartBeatInfo) query(HEART_BEAT_CACHE_KEY + key, new TypeReference<CacheHeartBeatInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.6
        });
    }

    public static CacheHeartBeatInfo getLastHeartBeatInfo(String key) {
        return (CacheHeartBeatInfo) query(HEART_BEAT_CACHE_KEY + key, new TypeReference<CacheHeartBeatInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.7
        });
    }

    public static Map<String, CacheHeartBeatInfo> getHeartBeatInfoAll() {
        HashMap map = new HashMap();
        Set<String> keys = keys(HEART_BEAT_CACHE_KEY + "*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                String tempKey = key.replace(HEART_BEAT_CACHE_KEY, "");
                map.put(tempKey, query(key, new TypeReference<CacheHeartBeatInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.8
                }));
            }
        }
        return map;
    }

    public static void cacheUpgradeStatusInfo(String key, CacheUpgradeStatusInfo value) {
        cache(DEVICE_UPGRADE_STATUS_CACHE_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeUpgradeStatusInfo(String key) {
        expire(DEVICE_UPGRADE_STATUS_CACHE_KEY + key);
    }

    public static CacheUpgradeStatusInfo getUpgradeStatusInfo(String key) {
        return (CacheUpgradeStatusInfo) query(DEVICE_UPGRADE_STATUS_CACHE_KEY + key, new TypeReference<CacheUpgradeStatusInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.9
        });
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
        HashMap map = new HashMap(16);
        Set<String> keys = keys(DEVICE_UPGRADE_STATUS_CACHE_KEY);
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                map.put(key.replace(DEVICE_UPGRADE_STATUS_CACHE_KEY, ""), query(key, new TypeReference<CacheUpgradeStatusInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.10
                }));
            }
        }
        return map;
    }

    public static void cacheLoginInfo(String key, UserLoginResponse value) {
        cache(LOGIN_CACHE_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeLoginInfo(String key) {
        expire(LOGIN_CACHE_KEY + key);
    }

    public static UserLoginResponse getLoginInfo(String key) {
        return (UserLoginResponse) query(LOGIN_CACHE_KEY + key, new TypeReference<UserLoginResponse>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.11
        });
    }

    public static void cacheExtractFeatureStatus(String key, CacheExtractFeatureStatusInfo value) {
        cache(EXTRACT_FEATURE_STATUS_CACHE_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeExtractFeatureStatus(String key) {
        expire(EXTRACT_FEATURE_STATUS_CACHE_KEY + key);
    }

    public static CacheExtractFeatureStatusInfo getExtractFeatureStatus(String key) {
        return (CacheExtractFeatureStatusInfo) query(EXTRACT_FEATURE_STATUS_CACHE_KEY + key, new TypeReference<CacheExtractFeatureStatusInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.12
        });
    }

    public static void cacheBatchExtractFeatureStatus(String key, Map<String, CacheExtractFeatureStatusInfo> value) {
        Map<String, CacheExtractFeatureStatusInfo> values = (Map) query(BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY + key, new TypeReference<Map<String, CacheExtractFeatureStatusInfo>>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.13
        });
        if (values == null) {
            cache(BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY + key, value, 86400000L);
        } else {
            values.putAll(value);
            cache(BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY + key, values, 86400000L);
        }
    }

    public static void removeBatchExtractFeatureStatus(String key) {
        expire(BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY + key);
    }

    public static Map<String, CacheExtractFeatureStatusInfo> getBatchExtractFeatureStatus(String key) {
        Map<String, CacheExtractFeatureStatusInfo> temp = (Map) query(BATCH_EXTRACT_FEATURE_STATUS_CACHE_KEY + key, new TypeReference<Map<String, CacheExtractFeatureStatusInfo>>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.14
        });
        if (temp == null) {
            return null;
        }
        for (String mapKey : temp.keySet()) {
            CacheExtractFeatureStatusInfo info = temp.get(mapKey);
            if (info != null && info.getExpireTime().longValue() < System.currentTimeMillis() && info.getStatus().intValue() == ExtractFeatureStatusEnum.EXTRACTING_STATUS.getValue()) {
                info.setStatus(0);
            }
            temp.put(mapKey, info);
        }
        return temp;
    }

    public static void cacheLastModifyTime(String key, DeviceLastModifyTimeInfo value) {
        cache(LAST_MODIFY_TIME_KEY + key, value, Long.valueOf(value.getExpireTime().longValue() - System.currentTimeMillis()));
    }

    public static void removeLastModifyTime(String key) {
        expire(LAST_MODIFY_TIME_KEY + key);
    }

    public static DeviceLastModifyTimeInfo getLastModifyTime(String key) {
        return (DeviceLastModifyTimeInfo) query(LAST_MODIFY_TIME_KEY + key, new TypeReference<DeviceLastModifyTimeInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.15
        });
    }

    public static void cacheServerTimeZone(String key, String value) {
        cache(SERVER_TIME_ZONE_KEY + key, value);
    }

    public static String getServerTimeZone(String key) {
        return (String) query(SERVER_TIME_ZONE_KEY + key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.16
        });
    }

    public static void cacheServerOrgCode(String key, String value) {
        cache(SERVER_ORG_CODE_KEY + key, value);
    }

    public static String getServerOrgCode(String key) {
        return (String) query(SERVER_ORG_CODE_KEY + key, new TypeReference<String>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.17
        });
    }

    public static void cacheUpgradePackageComments(String key, UploadUpgradePackageResponse value) {
        cache(UPGRADE_PACKAGE_COMMENTS_KEY + key, value, 3600000L);
    }

    public static void removeUpgradePackageComments(String key) {
        expire(UPGRADE_PACKAGE_COMMENTS_KEY + key);
    }

    public static UploadUpgradePackageResponse getUpgradePackageComments(String key) {
        return (UploadUpgradePackageResponse) query(UPGRADE_PACKAGE_COMMENTS_KEY + key, new TypeReference<UploadUpgradePackageResponse>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.18
        });
    }

    public static void cacheCurrentVersionInfo(String key, CurrentVersionInfo value) {
        cache(CURRENT_VERSION_KEY + key, value);
    }

    public static void removeCurrentVersionInfo(String key) {
        expire(CURRENT_VERSION_KEY + key);
    }

    public static CurrentVersionInfo getCurrentVersionInfo(String key) {
        return (CurrentVersionInfo) query(CURRENT_VERSION_KEY + key, new TypeReference<CurrentVersionInfo>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.19
        });
    }

    public static void cacheSyncCount(String key, Integer value) {
        cache(SYNC_COUNT_KEY + key, value);
    }

    public static Integer getSyncCount(String key) {
        return (Integer) query(SYNC_COUNT_KEY + key, new TypeReference<Integer>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.20
        });
    }

    public static void cacheFaceReloadProgress(String key, FaceReloadProgressResponse value) {
        cache(FACE_RELOAD_PROGRESS_KEY + key, value);
    }

    public static FaceReloadProgressResponse getFaceReloadProgress(String key) {
        return (FaceReloadProgressResponse) query(FACE_RELOAD_PROGRESS_KEY + key, new TypeReference<FaceReloadProgressResponse>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.21
        });
    }

    public static void cacheFreezeAccount(String key, FreezeAccountDto value) {
        cache(FREE_ACCOUNT_KEY + key, value);
    }

    public static FreezeAccountDto getFreezeAccount(String key) {
        FreezeAccountDto freezeAccountDto = (FreezeAccountDto) query(FREE_ACCOUNT_KEY + key, new TypeReference<FreezeAccountDto>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.22
        });
        if (freezeAccountDto != null && freezeAccountDto.getExpireTime() != null && freezeAccountDto.getExpireTime().longValue() > System.currentTimeMillis()) {
            return freezeAccountDto;
        }
        return null;
    }

    public static void cacheSyncMember(String key, TerminalSyncResponse<TerminalSyncMemberResponse> value) {
        cache(SYNC_MEMBER_KEY + key, value, Long.valueOf(BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
    }

    public static TerminalSyncResponse<TerminalSyncMemberResponse> getSyncMember(String key) {
        TerminalSyncResponse<TerminalSyncMemberResponse> terminalSyncResponse = (TerminalSyncResponse) query(SYNC_MEMBER_KEY + key, new TypeReference<TerminalSyncResponse<TerminalSyncMemberResponse>>() { // from class: com.moredian.onpremise.core.utils.RedisUtils.23
        });
        return terminalSyncResponse;
    }

    public static void cache(String key, Object value, Long time) {
        redisUtils.doCache(key, value, time);
    }

    public static void cache(String key, Object value) {
        redisUtils.doCache(key, value);
    }

    public static void expire(String key) {
        redisUtils.doExpire(key);
    }

    public static <T> T query(String str, TypeReference<T> typeReference) {
        return (T) redisUtils.doQuery(str, typeReference);
    }

    public static <T> T query(String str, Class<T> cls) {
        return (T) redisUtils.doQuery(str, cls);
    }

    private static Set<String> keys(String parttern) {
        return redisUtils.getKeys(parttern);
    }

    private void doCache(String key, Object value, Long time) {
        this.redisTemplate.opsForValue().set(key, JsonUtils.toJson(value), time.longValue(), TimeUnit.MILLISECONDS);
    }

    private void doCache(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, JsonUtils.toJson(value));
    }

    private void doExpire(String key) {
        this.redisTemplate.expire(key, 1L, TimeUnit.MILLISECONDS);
    }

    private <T> T doQuery(String str, TypeReference<T> typeReference) {
        return (T) JsonUtils.json2Object(this.redisTemplate.opsForValue().get(str), typeReference);
    }

    private <T> T doQuery(String str, Class<T> cls) {
        return (T) JsonUtils.json2Object(this.redisTemplate.opsForValue().get(str), cls);
    }

    private Set<String> getKeys(String parttern) {
        return this.redisTemplate.keys(parttern);
    }
}
