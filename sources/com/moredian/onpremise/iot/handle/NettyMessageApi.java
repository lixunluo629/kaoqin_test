package com.moredian.onpremise.iot.handle;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.CacheUtils;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.RedisUtils;
import com.moredian.onpremise.core.utils.ThreadPoolUtils;
import com.moredian.onpremise.iot.IOTSession;
import com.moredian.onpremise.model.DeviceVoiceConfigRequest;
import com.moredian.onpremise.model.SyncAccountRequest;
import com.moredian.onpremise.model.SyncAttendanceGroupRequest;
import com.moredian.onpremise.model.SyncAttendanceHolidayRequest;
import com.moredian.onpremise.model.SyncCanteenRequest;
import com.moredian.onpremise.model.SyncCheckInTaskMemberRequest;
import com.moredian.onpremise.model.SyncCheckInTaskRequest;
import com.moredian.onpremise.model.SyncExternalContactRequest;
import com.moredian.onpremise.model.SyncGroupRequest;
import com.moredian.onpremise.model.SyncMemberRequest;
import com.moredian.onpremise.model.SyncSnapModeRequest;
import com.moredian.onpremise.model.SyncStrangerReminderInfoRequest;
import com.moredian.onpremise.model.SyncVisitConfigRequest;
import io.netty.channel.ChannelId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/handle/NettyMessageApi.class */
public class NettyMessageApi {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) NettyMessageApi.class);

    @Value("${onpremise.deploy.type}")
    private Integer deployType;

    public void sendMsg(final Object object, final Integer type, final String deviceSn) {
        try {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() { // from class: com.moredian.onpremise.iot.handle.NettyMessageApi.1
                @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
                public void afterCommit() {
                    NettyMessageApi.this.doSendMsg(object, type, deviceSn);
                }
            });
        } catch (Exception e) {
            doSendMsg(object, type, deviceSn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSendMsg(Object object, Integer type, String deviceSn) {
        try {
            if (this.deployType.intValue() == 0) {
                doConsumeMsg(object, type, deviceSn);
            } else {
                String ip = CacheAdapter.getDeviceServerMapInfo(deviceSn);
                if (ip != null && ip.trim().length() > 0) {
                    if (CacheUtils.getServerIpAddressInfo(Constants.SERVER_IP_ADDRESS_KEY).equals(ip)) {
                        doConsumeMsg(object, type, deviceSn);
                    } else {
                        String id = UUID.randomUUID().toString();
                        RedisUtils.cache(id, object, 60000L);
                        Map<String, Object> params = new HashMap<>();
                        params.put("id", id);
                        params.put("type", type);
                        params.put("deviceSn", deviceSn);
                        HttpUtils.sendURLGet("http://" + ip + ":8022/netty/netty/sendMsg", params, null);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
        }
    }

    public void sendMsg(Object object, Integer type) {
        try {
            if (this.deployType.intValue() == 0) {
                ConcurrentMap<String, ChannelId> map = IOTSession.getTargetChannelMap();
                logger.info("===============size :{} ", Integer.valueOf(map.size()));
                for (String key : map.keySet()) {
                    logger.info("===============sendMsg key :{} ", key);
                    if (!Constants.DEFAULT_TARGET.equals(key)) {
                        sendMsg(object, type, key);
                    }
                }
            } else {
                Map<String, CacheHeartBeatInfo> map2 = RedisUtils.getHeartBeatInfoAll();
                logger.info("===============size :{} ", Integer.valueOf(map2.size()));
                for (String key2 : map2.keySet()) {
                    logger.info("===============sendMsg key :{} ", key2);
                    String key3 = CacheKeyGenerateUtils.restoreHeartBeatCacheKey(key2);
                    if (!Constants.DEFAULT_TARGET.equals(key3)) {
                        sendMsg(object, type, key3);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
        }
    }

    public void sendMsg(final Object object, final Integer type, final List<String> deviceSnList) {
        ThreadPoolUtils.poolSend.execute(new Runnable() { // from class: com.moredian.onpremise.iot.handle.NettyMessageApi.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    NettyMessageApi.logger.info("===============size :{} ", Integer.valueOf(deviceSnList.size()));
                    for (String deviceSn : deviceSnList) {
                        if (!Constants.DEFAULT_TARGET.equals(deviceSn)) {
                            NettyMessageApi.this.sendMsg(object, type, deviceSn);
                        }
                    }
                } catch (Exception e) {
                    NettyMessageApi.logger.error("error:{}", (Throwable) e);
                }
            }
        });
    }

    public void consumeMsg(Object object, Integer type, String deviceSn) {
        doConsumeMsg(object, type, deviceSn);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0081 A[Catch: Exception -> 0x00b8, TRY_ENTER, TryCatch #0 {Exception -> 0x00b8, blocks: (B:2:0x0000, B:3:0x0014, B:4:0x0040, B:6:0x0049, B:8:0x0058, B:11:0x0081, B:13:0x00ad), top: B:18:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void doConsumeMsg(java.lang.Object r8, java.lang.Integer r9, java.lang.String r10) {
        /*
            r7 = this;
            r0 = r7
            r1 = r8
            r2 = r10
            r0.cacheLastModifyTime(r1, r2)     // Catch: java.lang.Exception -> Lb8
            int[] r0 = com.moredian.onpremise.iot.handle.NettyMessageApi.AnonymousClass4.$SwitchMap$com$moredian$onpremise$model$IOTModelType     // Catch: java.lang.Exception -> Lb8
            r1 = r9
            int r1 = r1.intValue()     // Catch: java.lang.Exception -> Lb8
            com.moredian.onpremise.model.IOTModelType r1 = com.moredian.onpremise.model.IOTModelType.from(r1)     // Catch: java.lang.Exception -> Lb8
            int r1 = r1.ordinal()     // Catch: java.lang.Exception -> Lb8
            r0 = r0[r1]     // Catch: java.lang.Exception -> Lb8
            switch(r0) {
                case 1: goto L40;
                case 2: goto L40;
                case 3: goto L57;
                case 4: goto L57;
                case 5: goto L57;
                case 6: goto L57;
                case 7: goto L57;
                default: goto L58;
            }     // Catch: java.lang.Exception -> Lb8
        L40:
            java.lang.String r0 = "device_syncmode_key"
            r1 = r10
            boolean r0 = com.moredian.onpremise.core.adapter.CacheAdapter.checkSetMemberExist(r0, r1)     // Catch: java.lang.Exception -> Lb8
            if (r0 == 0) goto L57
            org.slf4j.Logger r0 = com.moredian.onpremise.iot.handle.NettyMessageApi.logger     // Catch: java.lang.Exception -> Lb8
            java.lang.String r1 = "============= device {} via real mode"
            r2 = r10
            r0.info(r1, r2)     // Catch: java.lang.Exception -> Lb8
            goto L58
        L57:
            return
        L58:
            org.slf4j.Logger r0 = com.moredian.onpremise.iot.handle.NettyMessageApi.logger     // Catch: java.lang.Exception -> Lb8
            java.lang.String r1 = "===============consume msg type:{}==,msg :{}=====,deviceSn:{}"
            r2 = 3
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> Lb8
            r3 = r2
            r4 = 0
            r5 = r9
            r3[r4] = r5     // Catch: java.lang.Exception -> Lb8
            r3 = r2
            r4 = 1
            r5 = r8
            java.lang.String r5 = com.moredian.onpremise.core.utils.JsonUtils.toJson(r5)     // Catch: java.lang.Exception -> Lb8
            r3[r4] = r5     // Catch: java.lang.Exception -> Lb8
            r3 = r2
            r4 = 2
            r5 = r10
            r3[r4] = r5     // Catch: java.lang.Exception -> Lb8
            r0.info(r1, r2)     // Catch: java.lang.Exception -> Lb8
            r0 = r10
            io.netty.channel.Channel r0 = com.moredian.onpremise.iot.IOTSession.getChannelByTarget(r0)     // Catch: java.lang.Exception -> Lb8
            r11 = r0
            r0 = r11
            if (r0 != 0) goto L81
            return
        L81:
            r0 = r11
            com.moredian.onpremise.event.ModelTransferEvent r1 = new com.moredian.onpremise.event.ModelTransferEvent     // Catch: java.lang.Exception -> Lb8
            r2 = r1
            r3 = r9
            int r3 = r3.intValue()     // Catch: java.lang.Exception -> Lb8
            r4 = r8
            r2.<init>(r3, r4)     // Catch: java.lang.Exception -> Lb8
            io.netty.channel.ChannelFuture r0 = r0.writeAndFlush(r1)     // Catch: java.lang.Exception -> Lb8
            com.moredian.onpremise.iot.handle.NettyMessageApi$3 r1 = new com.moredian.onpremise.iot.handle.NettyMessageApi$3     // Catch: java.lang.Exception -> Lb8
            r2 = r1
            r3 = r7
            r2.<init>()     // Catch: java.lang.Exception -> Lb8
            io.netty.channel.ChannelFuture r0 = r0.addListener2(r1)     // Catch: java.lang.Exception -> Lb8
            io.netty.channel.ChannelFuture r0 = r0.sync2()     // Catch: java.lang.Exception -> Lb8
            r12 = r0
            r0 = r12
            if (r0 == 0) goto Lb5
            r0 = r12
            io.netty.channel.ChannelFuture r0 = r0.sync2()     // Catch: java.lang.Exception -> Lb8
        Lb5:
            goto Lc6
        Lb8:
            r11 = move-exception
            org.slf4j.Logger r0 = com.moredian.onpremise.iot.handle.NettyMessageApi.logger
            java.lang.String r1 = "error:{}"
            r2 = r11
            r0.error(r1, r2)
        Lc6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moredian.onpremise.iot.handle.NettyMessageApi.doConsumeMsg(java.lang.Object, java.lang.Integer, java.lang.String):void");
    }

    private void cacheLastModifyTime(Object object, String deviceSn) {
        if (object instanceof SyncMemberRequest) {
            SyncMemberRequest request = (SyncMemberRequest) object;
            String key = CacheKeyGenerateUtils.getMemberLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key, request.getLastModifyTime());
            return;
        }
        if (object instanceof SyncGroupRequest) {
            SyncGroupRequest request2 = (SyncGroupRequest) object;
            String key2 = CacheKeyGenerateUtils.getGroupLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key2, request2.getLastModifyTime());
            return;
        }
        if (object instanceof SyncAttendanceGroupRequest) {
            SyncAttendanceGroupRequest request3 = (SyncAttendanceGroupRequest) object;
            String key3 = CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key3, request3.getLastModifyTime());
            return;
        }
        if (object instanceof SyncCheckInTaskRequest) {
            SyncCheckInTaskRequest request4 = (SyncCheckInTaskRequest) object;
            String key4 = CacheKeyGenerateUtils.getCheckInLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key4, request4.getLastModifyTime());
            return;
        }
        if (object instanceof SyncCheckInTaskMemberRequest) {
            SyncCheckInTaskMemberRequest request5 = (SyncCheckInTaskMemberRequest) object;
            String key5 = CacheKeyGenerateUtils.getCheckInTaskMemberLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key5, request5.getLastModifyTime());
            return;
        }
        if (object instanceof SyncAttendanceHolidayRequest) {
            SyncAttendanceHolidayRequest request6 = (SyncAttendanceHolidayRequest) object;
            String key6 = CacheKeyGenerateUtils.getAttendanceHolidayLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key6, request6.getLastModifyTime());
            return;
        }
        if (object instanceof SyncCanteenRequest) {
            SyncCanteenRequest request7 = (SyncCanteenRequest) object;
            String key7 = CacheKeyGenerateUtils.getCanteenLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key7, request7.getLastModifyTime());
            return;
        }
        if (object instanceof SyncVisitConfigRequest) {
            SyncVisitConfigRequest request8 = (SyncVisitConfigRequest) object;
            String key8 = CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key8, request8.getLastModifyTime());
            return;
        }
        if (object instanceof SyncExternalContactRequest) {
            SyncExternalContactRequest request9 = (SyncExternalContactRequest) object;
            String key9 = CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key9, request9.getLastModifyTime());
            return;
        }
        if (object instanceof SyncAccountRequest) {
            SyncAccountRequest request10 = (SyncAccountRequest) object;
            String key10 = CacheKeyGenerateUtils.getAccountLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key10, request10.getLastModifyTime());
            return;
        }
        if (object instanceof DeviceVoiceConfigRequest) {
            DeviceVoiceConfigRequest request11 = (DeviceVoiceConfigRequest) object;
            String key11 = CacheKeyGenerateUtils.getDeviceConfigLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key11, request11.getLastModifyTime());
        } else if (object instanceof SyncSnapModeRequest) {
            SyncSnapModeRequest request12 = (SyncSnapModeRequest) object;
            String key12 = CacheKeyGenerateUtils.getDeviceConfigLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key12, request12.getLastModifyTime());
        } else if (object instanceof SyncStrangerReminderInfoRequest) {
            SyncStrangerReminderInfoRequest request13 = (SyncStrangerReminderInfoRequest) object;
            String key13 = CacheKeyGenerateUtils.getDeviceConfigLastModifyTimeKey(deviceSn);
            doCacheLastModifyTime(key13, request13.getLastModifyTime());
        }
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
}
