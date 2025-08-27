package com.moredian.onpremise.frontend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.mapper.OpenApiMapper;
import com.moredian.onpremise.core.model.domain.OpenApi;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.JwtUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/web/LoginFilter.class */
public class LoginFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) LoginFilter.class);

    @Autowired
    private OpenApiMapper openApiMapper;

    @Override // javax.servlet.Filter
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        OpenApi openApi;
        Map<String, Object> params;
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String sessionId = request.getSession().getId();
        String path = request.getRequestURI();
        logger.info("request ip:{} method:{} uri:{}", HttpUtils.getCilentIp(request), request.getMethod(), request.getRequestURI());
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, resp);
            return;
        }
        String[] needLoginPaths = {"/app", "/dept", "/member", "/group", "/device", "/record", "/server", "/checkIn", "/account/account/getAccountOrgInfo", "/account/account/updateOrgInfo", "/account/account/modifyPassword", "/account/account/updateLanguage", "/account/member/listAccount", "/account/member/saveAccount", "/account/member/deleteAccount", "/account/member/generatorOpenApiAppKey", "/account/member/getOpenApiAppKey", "/account/authCode/listAuthCode", "/account/authCode/saveAuthCode", "/account/auth/getOrgCode", "/account/config", "/account/memory/queryMemoryInfo", "/account/viewConfig", "/account/operLog", "/auth/module/saveModule", "/auth/module/listModule", "/auth/module/listAccountManageModule", "/attendance", "/meal", "/temperature", "/visit"};
        boolean needLogin = isMatch(path, needLoginPaths);
        if (!needLogin) {
            chain.doFilter(req, resp);
            return;
        }
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(sessionId);
        if (loginInfo != null && loginInfo.getExpireTime().longValue() > System.currentTimeMillis()) {
            loginInfo.setExpireTime(Long.valueOf(MyDateUtils.addMinutes(new Date(), 30).getTime()));
            CacheAdapter.cacheLoginInfo(sessionId, loginInfo);
            chain.doFilter(req, resp);
            return;
        }
        String token = request.getHeader("Auth-token");
        if (token == null) {
            token = request.getHeader("Auth-Token");
        }
        logger.info("=====> api token:{}", token);
        if (token != null && token.trim().length() > 0 && (openApi = this.openApiMapper.getLastOne(1L)) != null && (params = JwtUtils.verifyParams(token, openApi.getAppKey())) != null && params.size() > 0) {
            chain.doFilter(req, resp);
            return;
        }
        logger.info("request sessionId:{};request url:{};method:{};response:{}", sessionId, request.getRequestURL(), request.getMethod(), 401);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setStatus(401);
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("code", "401");
        msg.put(ConstraintHelper.MESSAGE, OnpremiseErrorEnum.USER_SHOULD_BE_LOGIN.getMessage());
        msg.put("errorMessage", OnpremiseErrorEnum.USER_SHOULD_BE_LOGIN.getMessage());
        msg.put("errorCode", OnpremiseErrorEnum.USER_SHOULD_BE_LOGIN.getErrorCode());
        String json = mapper.writeValueAsString(msg);
        out.print(json);
        out.flush();
        out.close();
    }

    @Override // javax.servlet.Filter
    public void destroy() {
    }

    private boolean isMatch(String path, String[] paths) {
        boolean isMatch = false;
        int length = paths.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String p = paths[i];
            if (!path.startsWith(p)) {
                i++;
            } else {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }
}
