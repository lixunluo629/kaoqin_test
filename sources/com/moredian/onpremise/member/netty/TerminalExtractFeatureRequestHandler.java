package com.moredian.onpremise.member.netty;

import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import com.moredian.onpremise.core.model.request.SaveMemberFaceRequest;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncMemberRequest;
import com.moredian.onpremise.model.TerminalExtractFeatureRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-member-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/member/netty/TerminalExtractFeatureRequestHandler.class */
public class TerminalExtractFeatureRequestHandler extends AbstractModelEventHandler<TerminalExtractFeatureRequest> {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TerminalExtractFeatureRequestHandler.class);
    private static volatile Map<String, Integer> counter = new ConcurrentHashMap();

    @Autowired
    private MemberService memberService;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(TerminalExtractFeatureRequest model, IOTContext context) {
        CacheExtractFeatureStatusInfo info;
        logger.debug("设备抽取特征值结果：{}", JsonUtils.toJson(model));
        Map<String, CacheExtractFeatureStatusInfo> infos = CacheAdapter.getBatchExtractFeatureStatus(model.getJobNum());
        if (infos != null && (info = infos.get(model.getQueryKey())) != null && info.getStatus().intValue() == 2) {
            info.setEigenvalue(model.getEigenvalue());
            info.setStatus(0);
            if (model.getEigenvalue().trim().length() > 0) {
                if (Constants.FAIL.equals(model.getEigenvalue())) {
                    info.setStatus(3);
                } else {
                    info.setStatus(1);
                    if (info.getMemberJobNum() != null && info.getMemberJobNum().trim().length() > 0) {
                        SaveMemberFaceRequest request = new SaveMemberFaceRequest();
                        request.setEigenvalueValue(model.getEigenvalue());
                        request.setMemberJobNum(info.getMemberJobNum());
                        request.setVerifyFaceUrl(info.getVerifyFaceUrl());
                        request.setOrgId(info.getOrgId());
                        request.setNeedSendMsg(false);
                        this.memberService.saveMemberFace(request);
                    }
                }
            }
            Integer count = counter.get(model.getJobNum());
            if (count == null) {
                count = 0;
            }
            if (count.intValue() + 1 == info.getCount().intValue()) {
                this.nettyMessageApi.sendMsg(new SyncMemberRequest(), Integer.valueOf(SyncMemberRequest.MODEL_TYPE.type()));
                counter.remove(model.getJobNum());
            } else {
                counter.put(model.getJobNum(), Integer.valueOf(count.intValue() + 1));
            }
            infos.put(model.getQueryKey(), info);
            CacheAdapter.cacheBatchExtractFeatureStatus(model.getJobNum(), infos);
        }
    }
}
