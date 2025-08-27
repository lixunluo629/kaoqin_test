package com.moredian.onpremise.frontend.controller.base;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/base/BaseController.class */
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger("BIZ");

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler
    @ResponseBody
    public String handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String message;
        HashMap<String, Object> msg = new HashMap<>();
        try {
            String errorCode = OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode();
            if (exception instanceof BizException) {
                BizException bizException = (BizException) exception;
                errorCode = bizException.getErrorCode();
                message = bizException.getMessage();
                logger.error("failed to handle http request:{} errCode:{} errMessage:{} {}", request.getRequestURI(), errorCode, message, bizException);
            } else {
                logger.error("服务调用失败:{}", (Throwable) exception);
                message = "服务调用失败";
            }
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setStatus(200);
            msg.put("code", "500");
            msg.put("errorMessage", message);
            msg.put(ConstraintHelper.MESSAGE, message);
            msg.put("errorCode", errorCode);
        } catch (Exception e) {
            msg.put("code", "500");
            msg.put("errorMessage", "服务调用失败");
            logger.error("服务调用失败::{}", (Throwable) e);
        }
        return JsonUtils.toJson(msg);
    }

    protected Long getOrgId() {
        Map map = getParameterMaps();
        return Long.valueOf(String.valueOf(map.get("orgId")));
    }

    protected Long getAccountId() {
        Map map = getParameterMaps();
        return Long.valueOf(String.valueOf(map.get("accountId")));
    }

    protected BaseRequest getBaseRequest(BaseRequest baseRequest) {
        Map<String, String> map = getParameterMaps();
        if (map == null || map.size() == 0) {
            return baseRequest;
        }
        if (baseRequest.getOrgId() == null || baseRequest.getOrgId().longValue() == 0) {
            baseRequest.setOrgId(Long.valueOf(map.get("orgId")));
        }
        if (baseRequest.getLoginAccountId() == null || baseRequest.getLoginAccountId().longValue() == 0) {
            baseRequest.setLoginAccountId(Long.valueOf(map.get("accountId")));
        }
        baseRequest.setSessionId(map.get("sessionId"));
        return baseRequest;
    }

    private Map<String, String> getParameterMaps() {
        String sessionId = this.request.getSession().getId();
        UserLoginResponse response = CacheAdapter.getLoginInfo(sessionId);
        if (response == null) {
            response = CacheAdapter.getLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT);
            sessionId = OpenApiConstants.OPEN_API_LOGIN_ACCOUNT;
        }
        AssertUtil.isNullOrEmpty(response, OnpremiseErrorEnum.LOGIN_OR_TOKEN_EXPIRE);
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("accountId", String.valueOf(response.getAccountId()));
        map.put("orgId", String.valueOf(response.getOrgId()));
        return map;
    }
}
