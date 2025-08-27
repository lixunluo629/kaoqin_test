package com.moredian.onpremise.core.aop.limit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moredian.onpremise.core.aop.limit.annotation.AccessLimitAnnotation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Aspect
@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/limit/AccessLimitAop.class */
public class AccessLimitAop {

    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    private HttpServletResponse servletResponse;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.moredian.onpremise.core.aop.limit.annotation.AccessLimitAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(accessLimitAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, AccessLimitAnnotation accessLimitAnnotation) throws Throwable {
        int limit = accessLimitAnnotation.limit();
        int sec = accessLimitAnnotation.sec();
        String key = this.servletRequest.getRequestURI();
        String maxLimit = this.redisTemplate.opsForValue().get(key);
        if (maxLimit == null) {
            this.redisTemplate.opsForValue().set(key, "1", sec, TimeUnit.SECONDS);
        } else if (Integer.valueOf(maxLimit).intValue() < limit) {
            this.redisTemplate.opsForValue().set(key, (Integer.valueOf(maxLimit).intValue() + 1) + "", sec, TimeUnit.SECONDS);
        } else {
            output(this.servletResponse, "接口请求过多，请稍后再试");
            return this.servletResponse;
        }
        return pjp.proceed();
    }

    public void output(HttpServletResponse response, String str) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setStatus(503);
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("code", "503");
        msg.put(ConstraintHelper.MESSAGE, str);
        String json = mapper.writeValueAsString(msg);
        out.print(json);
        out.flush();
        out.close();
    }
}
