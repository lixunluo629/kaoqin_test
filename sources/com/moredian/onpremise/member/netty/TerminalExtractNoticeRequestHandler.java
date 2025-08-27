package com.moredian.onpremise.member.netty;

import com.moredian.onpremise.api.member.MemberService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.ExtractFeatureStatusEnum;
import com.moredian.onpremise.core.model.info.CacheExtractFeatureStatusInfo;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.TerminalExtractNoticeRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-member-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/member/netty/TerminalExtractNoticeRequestHandler.class */
public class TerminalExtractNoticeRequestHandler extends AbstractModelEventHandler<TerminalExtractNoticeRequest> {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TerminalExtractNoticeRequestHandler.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(TerminalExtractNoticeRequest model, IOTContext context) {
        CacheExtractFeatureStatusInfo info;
        logger.debug("设备录脸结果：{}", JsonUtils.toJson(model));
        Map<String, CacheExtractFeatureStatusInfo> infos = CacheAdapter.getBatchExtractFeatureStatus(model.getJobNum());
        if (infos != null && (info = infos.get(model.getQueryKey())) != null && info.getStatus().intValue() == ExtractFeatureStatusEnum.EXTRACTING_STATUS.getValue()) {
            info.setEigenvalue(model.getEigenvalue());
            if (model.getEigenvalue().trim().length() > 0) {
                info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.SUCCESS_STATUS.getValue()));
            } else {
                info.setStatus(Integer.valueOf(ExtractFeatureStatusEnum.FAIL_STATUS.getValue()));
            }
            info.setVerifyFaceUrl(model.getVerifyFaceUrl());
            infos.put(model.getQueryKey(), info);
            CacheAdapter.cacheBatchExtractFeatureStatus(model.getJobNum(), infos);
        }
    }
}
