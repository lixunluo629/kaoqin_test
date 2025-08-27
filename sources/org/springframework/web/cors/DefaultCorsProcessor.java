package org.springframework.web.cors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/cors/DefaultCorsProcessor.class */
public class DefaultCorsProcessor implements CorsProcessor {
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final Log logger = LogFactory.getLog(DefaultCorsProcessor.class);

    @Override // org.springframework.web.cors.CorsProcessor
    public boolean processRequest(CorsConfiguration config, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!CorsUtils.isCorsRequest(request)) {
            return true;
        }
        ServletServerHttpResponse serverResponse = new ServletServerHttpResponse(response);
        if (responseHasCors(serverResponse)) {
            logger.debug("Skip CORS processing: response already contains \"Access-Control-Allow-Origin\" header");
            return true;
        }
        ServletServerHttpRequest serverRequest = new ServletServerHttpRequest(request);
        if (WebUtils.isSameOrigin(serverRequest)) {
            logger.debug("Skip CORS processing: request is from same origin");
            return true;
        }
        boolean preFlightRequest = CorsUtils.isPreFlightRequest(request);
        if (config == null) {
            if (preFlightRequest) {
                rejectRequest(serverResponse);
                return false;
            }
            return true;
        }
        return handleInternal(serverRequest, serverResponse, config, preFlightRequest);
    }

    private boolean responseHasCors(ServerHttpResponse response) {
        try {
            return response.getHeaders().getAccessControlAllowOrigin() != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    protected void rejectRequest(ServerHttpResponse response) throws IOException {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getBody().write("Invalid CORS request".getBytes(UTF8_CHARSET));
    }

    protected boolean handleInternal(ServerHttpRequest request, ServerHttpResponse response, CorsConfiguration config, boolean preFlightRequest) throws IOException {
        String requestOrigin = request.getHeaders().getOrigin();
        String allowOrigin = checkOrigin(config, requestOrigin);
        HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
        List<HttpMethod> allowMethods = checkMethods(config, requestMethod);
        List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
        List<String> allowHeaders = checkHeaders(config, requestHeaders);
        if (allowOrigin == null || allowMethods == null || (preFlightRequest && allowHeaders == null)) {
            rejectRequest(response);
            return false;
        }
        HttpHeaders responseHeaders = response.getHeaders();
        responseHeaders.setAccessControlAllowOrigin(allowOrigin);
        responseHeaders.add("Vary", "Origin");
        if (preFlightRequest) {
            responseHeaders.setAccessControlAllowMethods(allowMethods);
        }
        if (preFlightRequest && !allowHeaders.isEmpty()) {
            responseHeaders.setAccessControlAllowHeaders(allowHeaders);
        }
        if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
            responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
        }
        if (Boolean.TRUE.equals(config.getAllowCredentials())) {
            responseHeaders.setAccessControlAllowCredentials(true);
        }
        if (preFlightRequest && config.getMaxAge() != null) {
            responseHeaders.setAccessControlMaxAge(config.getMaxAge().longValue());
        }
        response.flush();
        return true;
    }

    protected String checkOrigin(CorsConfiguration config, String requestOrigin) {
        return config.checkOrigin(requestOrigin);
    }

    protected List<HttpMethod> checkMethods(CorsConfiguration config, HttpMethod requestMethod) {
        return config.checkHttpMethod(requestMethod);
    }

    private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
        return isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod();
    }

    protected List<String> checkHeaders(CorsConfiguration config, List<String> requestHeaders) {
        return config.checkHeaders(requestHeaders);
    }

    private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
        HttpHeaders headers = request.getHeaders();
        return isPreFlight ? headers.getAccessControlRequestHeaders() : new ArrayList(headers.keySet());
    }
}
