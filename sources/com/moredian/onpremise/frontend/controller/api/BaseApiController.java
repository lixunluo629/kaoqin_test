package com.moredian.onpremise.frontend.controller.api;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.OpenApiConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.JsonUtils;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/controller/api/BaseApiController.class */
public class BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BaseApiController.class);

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler
    public String handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String message;
        HashMap<String, Object> msg = new HashMap<>();
        try {
            String errorCode = OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode();
            if (exception instanceof BizException) {
                BizException bizException = (BizException) exception;
                errorCode = bizException.getErrorCode();
                message = bizException.getMessage();
                logger.error("failed to handle http request,,errCode:" + errorCode + ",errMessage:" + message, (Throwable) bizException);
            } else {
                logger.error("服务调用失败", (Throwable) exception);
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
        }
        return JsonUtils.toJson(msg);
    }

    protected UserLoginResponse getLogin() {
        UserLoginResponse userLoginResponse = CacheAdapter.getLoginInfo(OpenApiConstants.OPEN_API_LOGIN_ACCOUNT);
        return userLoginResponse;
    }
}
