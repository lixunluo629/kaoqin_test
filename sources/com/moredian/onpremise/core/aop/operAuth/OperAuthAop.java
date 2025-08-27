package com.moredian.onpremise.core.aop.operAuth;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.aop.operAuth.annotation.OperAuthAnnotation;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/operAuth/OperAuthAop.class */
public class OperAuthAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountMapper accountMapper;

    @Pointcut("@annotation(com.moredian.onpremise.core.aop.operAuth.annotation.OperAuthAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()&&@annotation(operAuthAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint pjp, OperAuthAnnotation operAuthAnnotation) throws Throwable {
        String sessionId = this.request.getSession().getId();
        UserLoginResponse loginResponse = CacheAdapter.getLoginInfo(sessionId);
        if (loginResponse == null) {
            loginResponse = CacheAdapter.getLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT);
        }
        AssertUtil.isNullOrEmpty(loginResponse, OnpremiseErrorEnum.ACCOUNT_NOT_LOGIN);
        Account account = this.accountMapper.getAccountInfo(loginResponse.getAccountId(), loginResponse.getOrgId());
        AssertUtil.isNullOrEmpty(account, OnpremiseErrorEnum.ACCOUNT_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(account.getAccountGrade().intValue() == 2 || account.getAccountGrade().intValue() == 1), OnpremiseErrorEnum.ACCOUNT_OPER_AUTH_NOT_ALLOW);
        return pjp.proceed();
    }
}
