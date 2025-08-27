package com.moredian.onpremise.core.aop.token;

import com.moredian.onpremise.core.aop.token.annotation.OpenApiTokenAnnotation;
import com.moredian.onpremise.core.mapper.OpenApiMapper;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/token/OpenApiTokenAop.class */
public class OpenApiTokenAop {

    @Autowired
    private OpenApiMapper openApiMapper;

    @Pointcut("@annotation(com.moredian.onpremise.core.aop.token.annotation.OpenApiTokenAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(appTokenAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, OpenApiTokenAnnotation appTokenAnnotation) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.getHeader("Auth-token");
        String orgId = request.getHeader("Auth-orgId");
        this.openApiMapper.getLastOne(Long.valueOf(orgId));
        return pjp.proceed();
    }
}
