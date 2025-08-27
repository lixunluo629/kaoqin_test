package com.moredian.onpremise.core.aop.token;

import com.moredian.onpremise.core.aop.token.annotation.AppTokenAnnotation;
import com.moredian.onpremise.core.mapper.AccessKeyMapper;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/token/AppTokenAop.class */
public class AppTokenAop {
    private static final Logger logger = LoggerFactory.getLogger("BIZ");

    @Autowired
    private AccessKeyMapper accessKeyMapper;

    @Pointcut("@annotation(com.moredian.onpremise.core.aop.token.annotation.AppTokenAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(appTokenAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, AppTokenAnnotation appTokenAnnotation) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.getHeader("Authorization");
        String deviceSn = request.getHeader("DeviceSn");
        logger.info("======device sn :{} ==========", deviceSn);
        return pjp.proceed();
    }
}
