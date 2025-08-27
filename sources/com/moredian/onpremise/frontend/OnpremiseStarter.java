package com.moredian.onpremise.frontend;

import com.moredian.FaceDet;
import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.ScheduleCronConstants;
import com.moredian.onpremise.core.common.enums.DeviceSyncModeEnum;
import com.moredian.onpremise.core.mapper.CacheMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.model.domain.Cache;
import com.moredian.onpremise.core.model.info.CurrentVersionInfo;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.utils.AuthCodeUtils;
import com.moredian.onpremise.core.utils.CacheUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.task.impl.AppValidSchedule;
import com.moredian.onpremise.task.impl.BackupsDataSchedule;
import com.moredian.onpremise.task.impl.DeleteCacheFileSchedule;
import com.moredian.onpremise.task.impl.DeleteExpireDataSchedule;
import com.moredian.onpremise.task.impl.ExternalContactExpiredSchedule;
import com.moredian.onpremise.task.impl.PullDeviceLogSchedule;
import java.util.List;
import org.apache.poi.ddf.EscherProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

@EnableScheduling
@SpringBootApplication
@MapperScan({"com.moredian.onpremise.core.mapper"})
@ComponentScan({"com.moredian.onpremise"})
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/OnpremiseStarter.class */
public class OnpremiseStarter implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) OnpremiseStarter.class);

    @Autowired
    private DeleteCacheFileSchedule deleteCacheFileSchedule;

    @Autowired
    private BackupsDataSchedule backupsDataSchedule;

    @Autowired
    private DeleteExpireDataSchedule deleteExpireDataSchedule;

    @Autowired
    private PullDeviceLogSchedule pullDeviceLogSchedule;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CacheMapper cacheMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private AppValidSchedule appValidSchedule;

    @Autowired
    private ExternalContactExpiredSchedule externalContactExpiredSchedule;

    @Value("${onpremise.deploy.type}")
    private Integer deployType;

    @Value("${onpremise.current.version}")
    private String currentVersion;

    @Value("${onpremise.netty.server.ip}")
    private String serverIp;

    public static void main(String[] args) {
        boolean init = FaceDet.Init(1080, EscherProperties.THREEDSTYLE__SKEWANGLE);
        logger.info("init faceDet:{}", Boolean.valueOf(init));
        FaceDet.SetPoseThreshold(30, 30, 30);
        SpringApplication.run(OnpremiseStarter.class, args);
    }

    @Override // org.springframework.boot.ApplicationRunner
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (this.deployType.intValue() == 1) {
            logger.info("=======================server ip :{}", this.serverIp);
            CacheUtils.cacheServerIpAddressInfo(Constants.SERVER_IP_ADDRESS_KEY, this.serverIp);
            CurrentVersionInfo info = new CurrentVersionInfo();
            info.setCurrentVersion(this.currentVersion);
            CacheAdapter.cacheCurrentVersionInfo(Constants.SERVER_CURRENT_VERSION_KEY, info);
        }
        Cache cache = this.cacheMapper.getByCacheKey(Constants.SERVER_ORG_CODE_KEY);
        if (cache != null) {
            String orgCode = String.valueOf(JsonUtils.json2Object(cache.getCacheValue(), Class.forName(cache.getValueType())));
            CacheAdapter.cacheServerOrgCode(Constants.SERVER_ORG_CODE_KEY, orgCode);
            logger.info("orgCode:{}", orgCode);
        } else {
            String orgCode2 = AuthCodeUtils.initServerDeviceId();
            logger.info("init orgCode:{}", orgCode2);
            this.cacheMapper.insert(new Cache(Constants.SERVER_ORG_CODE_KEY.toUpperCase(), Constants.SERVER_ORG_CODE_KEY, JsonUtils.toJson(orgCode2), "java.lang.String"));
        }
        List<DeviceResponse> devices = this.deviceMapper.listDevice(1L, null, null, null, null, null, null, null);
        if (!CollectionUtils.isEmpty(devices)) {
            for (DeviceResponse device : devices) {
                if (device.getSyncMode() != null && device.getSyncMode().equals(Integer.valueOf(DeviceSyncModeEnum.REALTIME.getValue()))) {
                    CacheAdapter.cacheSetMemberAdd(ConfigConstants.DEVICE_SYNCMODE_KEY, device.getDeviceSn());
                }
            }
        }
        this.deleteCacheFileSchedule.saveScheduled(ScheduleCronConstants.DELETE_CACHE_SCHEDULE_CRON);
        this.backupsDataSchedule.saveScheduled(ScheduleCronConstants.BACKUPS_DATA_SCHEDULE_CRON);
        this.deleteExpireDataSchedule.saveScheduled(ScheduleCronConstants.DELETE_EXPIRE_DATA_SCHEDULE_CRON);
        this.pullDeviceLogSchedule.saveScheduled(ScheduleCronConstants.PULL_DEVICE_LOG_SCHEDULE_CRON);
        this.appValidSchedule.saveScheduled("30 0 0 * * ?");
        this.externalContactExpiredSchedule.saveScheduled("30 0 0 * * ?");
    }
}
