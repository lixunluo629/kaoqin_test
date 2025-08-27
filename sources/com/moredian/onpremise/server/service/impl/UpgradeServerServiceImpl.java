package com.moredian.onpremise.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.api.server.UpgradeServerService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.CacheMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.mapper.UpgradeServerScheduleMapper;
import com.moredian.onpremise.core.model.domain.Cache;
import com.moredian.onpremise.core.model.domain.UpgradeServerSchedule;
import com.moredian.onpremise.core.model.info.CurrentVersionInfo;
import com.moredian.onpremise.core.model.request.DeleteServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ExecuteUpgradeServerScheduleRequest;
import com.moredian.onpremise.core.model.request.ListServerScheduleRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.response.ListServerScheduleResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.core.utils.ShellUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
/* loaded from: onpremise-server-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/server/service/impl/UpgradeServerServiceImpl.class */
public class UpgradeServerServiceImpl implements UpgradeServerService, ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) UpgradeServerServiceImpl.class);

    @Autowired
    private UpgradeServerScheduleMapper serverScheduleMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private CacheMapper cacheMapper;

    @Value("${onpremise.deploy.type}")
    private Integer deployType;

    @Value("${onpremise.save.server.path}")
    private String saveServerPath;

    @Value("${onpremise.reload.feature}")
    private Integer reloadFeatureFlag;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Override // org.springframework.boot.ApplicationRunner
    public void run(ApplicationArguments applicationArguments) throws Exception {
        readCacheFromDb();
    }

    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public boolean delete(DeleteServerScheduleRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        checkScheduleId(request.getUpgradeServerScheduleId());
        UpgradeServerSchedule serverSchedule = this.serverScheduleMapper.getOneById(request.getUpgradeServerScheduleId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(serverSchedule, OnpremiseErrorEnum.SERVER_SCHEDULE_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.serverScheduleMapper.delete(request.getUpgradeServerScheduleId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_SERVER_SCHEDULE_FAIL);
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [com.moredian.onpremise.server.service.impl.UpgradeServerServiceImpl$1] */
    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public boolean executeSchedule(ExecuteUpgradeServerScheduleRequest request) {
        AssertUtil.checkOrgId(request.getOrgId());
        checkScheduleId(request.getUpgradeServerScheduleId());
        UpgradeServerSchedule serverSchedule = this.serverScheduleMapper.getOneById(request.getUpgradeServerScheduleId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(serverSchedule, OnpremiseErrorEnum.SERVER_SCHEDULE_NOT_FIND);
        this.cacheMapper.deleteAll();
        CurrentVersionInfo info = new CurrentVersionInfo();
        info.setCurrentVersion(serverSchedule.getUpgradeVersion());
        CacheAdapter.cacheCurrentVersionInfo(Constants.SERVER_CURRENT_VERSION_KEY, info);
        saveCache();
        final String name = serverSchedule.getUpgradePackageName();
        new Thread() { // from class: com.moredian.onpremise.server.service.impl.UpgradeServerServiceImpl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws InterruptedException {
                try {
                    Thread.sleep(1000L);
                    ShellUtils.upgradeServer(name);
                } catch (InterruptedException e) {
                    UpgradeServerServiceImpl.logger.error("error:{}", (Throwable) e);
                }
            }
        }.start();
        return true;
    }

    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public boolean readCacheFromDb() throws IllegalAccessException, IllegalArgumentException {
        if (this.deployType.intValue() != 0) {
            return true;
        }
        logger.info("========= start readCacheFromDb");
        CacheUtils cacheUtils = new CacheUtils();
        Field[] fields = cacheUtils.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String cacheType = field.getName();
                Map<String, Object> map = new HashMap<>(3);
                logger.info("========= cache type :{}", cacheType);
                List<Cache> caches = this.cacheMapper.getByCacheType(cacheType);
                if (MyListUtils.listIsEmpty(caches)) {
                    logger.info("========= cache type size:{}", Integer.valueOf(caches.size()));
                    for (Cache cache : caches) {
                        if (cache != null) {
                            map.put(cache.getCacheKey(), JsonUtils.json2Object(cache.getCacheValue(), Class.forName(cache.getValueType())));
                        }
                    }
                    logger.info("========= cache content :{}", JsonUtils.toJson(map));
                    field.set(CacheUtils.class, map);
                }
                field.setAccessible(false);
            } catch (Exception e) {
                logger.error("error:{}", (Throwable) e);
            }
        }
        logger.info("========current version :{}", JsonUtils.toJson(CacheAdapter.getCurrentVersionInfo(Constants.SERVER_CURRENT_VERSION_KEY)));
        return true;
    }

    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public boolean uploadPackage(UploadUpgradePackageRequest request) throws IOException {
        AssertUtil.checkOrgId(request.getOrgId());
        this.organizationMapper.getOneById(request.getOrgId());
        String fileName = MyDateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".tar.gz";
        String path = MyFileUtils.upload(request.getFile(), this.saveServerPath, fileName);
        AssertUtil.isNullOrEmpty(path, OnpremiseErrorEnum.FILE_UPLOAD_FAIL);
        Map<String, String> parseResult = MyFileUtils.parseTargz(path);
        UpgradeServerSchedule serverSchedule = new UpgradeServerSchedule();
        serverSchedule.setUpgradeReleaseTime(parseResult.get(UpgradeConstants.UPGRADE_PARAM_RELEASE_TIME_KEY));
        serverSchedule.setUpgradeVersion(parseResult.get(UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY));
        serverSchedule.setUpgradeTime(MyDateUtils.addSeconds(new Date(), 30));
        serverSchedule.setOrgId(request.getOrgId());
        serverSchedule.setUpgradePackageName(fileName);
        AssertUtil.isTrue(Boolean.valueOf(this.serverScheduleMapper.insert(serverSchedule) > 0), OnpremiseErrorEnum.SAVE_SERVER_SCHEDULE_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public PageList<ListServerScheduleResponse> listServerSchedule(ListServerScheduleRequest request) {
        PageList<ListServerScheduleResponse> result;
        AssertUtil.checkOrgId(request.getOrgId());
        if (Paginator.checkPaginator(request.getPaginator())) {
            PageHelper.startPage(request.getPaginator().getPageNum(), request.getPaginator().getPageSize());
            result = new PageList<>(this.serverScheduleMapper.listServerSchedule(request));
        } else {
            List<ListServerScheduleResponse> responses = this.serverScheduleMapper.listServerSchedule(request);
            result = new PageList<>(Paginator.initPaginator(responses), responses);
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.server.UpgradeServerService
    public boolean reloadMemberFeature() {
        if (this.reloadFeatureFlag.intValue() == 1) {
            logger.info("========= reload member feature");
            return true;
        }
        return true;
    }

    private void checkScheduleId(Long scheduleId) {
        AssertUtil.isTrue(Boolean.valueOf(scheduleId != null && scheduleId.longValue() > 0), OnpremiseErrorEnum.SERVER_SCHEDULE_ID_MUST_NOT_NULL);
    }

    private void saveCache() {
        if (this.deployType.intValue() != 0) {
            return;
        }
        CacheUtils cacheUtils = new CacheUtils();
        Field[] fields = cacheUtils.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                logger.info("=====cache type :{}", field.getName());
                if (!"BATCH_EXTRACT_FEATURE_STATUS_CACHE".equals(field.getName()) && !"UPGRADE_PACKAGE_COMMENTS".equals(field.getName()) && !"LOGIN_CACHE".equals(field.getName()) && !"FACE_RELOAD_PROGRESS".equals(field.getName())) {
                    Map<String, Object> map = (Map) field.get(CacheUtils.class);
                    logger.info("=====map size :{}", Integer.valueOf(map.size()));
                    for (String key : map.keySet()) {
                        Object entry = map.get(key);
                        this.cacheMapper.insert(new Cache(field.getName(), key, JsonUtils.toJson(entry), entry.getClass().getName()));
                    }
                    field.setAccessible(false);
                }
            } catch (Exception e) {
                logger.error("error:{}", (Throwable) e);
            }
        }
    }
}
