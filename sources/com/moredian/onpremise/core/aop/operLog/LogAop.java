package com.moredian.onpremise.core.aop.operLog;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.mapper.OperLogMapper;
import com.moredian.onpremise.core.model.domain.OperLog;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/operLog/LogAop.class */
public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperLogMapper operLogMapper;

    @Pointcut("@annotation(com.moredian.onpremise.core.aop.operLog.annotation.LogAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(logAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, LogAnnotation logAnnotation) throws Throwable {
        String module = logAnnotation.module();
        String desc = logAnnotation.description();
        OperLog operLog = new OperLog();
        operLog.setOperType(module);
        operLog.setOperDescription(desc);
        operLog.setOperArgs(JsonUtils.toJson(pjp.getArgs()));
        UserLoginResponse response = CacheAdapter.getLoginInfo(this.request.getSession().getId());
        if (response == null) {
            response = CacheAdapter.getLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT);
        }
        if (response == null) {
            return pjp.proceed();
        }
        operLog.setAccountId(response.getAccountId());
        operLog.setAccountName(response.getAccountName());
        operLog.setOrgId(response.getOrgId());
        this.operLogMapper.insert(operLog);
        return pjp.proceed();
    }
}
