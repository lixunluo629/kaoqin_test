package org.springframework.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/filter/ShallowEtagHeaderFilter.class */
public class ShallowEtagHeaderFilter extends OncePerRequestFilter {
    private static final String HEADER_ETAG = "ETag";
    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String DIRECTIVE_NO_STORE = "no-store";
    private static final String STREAMING_ATTRIBUTE = ShallowEtagHeaderFilter.class.getName() + ".STREAMING";
    private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", String.class);
    private boolean writeWeakETag = false;

    public void setWriteWeakETag(boolean writeWeakETag) {
        this.writeWeakETag = writeWeakETag;
    }

    public boolean isWriteWeakETag() {
        return this.writeWeakETag;
    }

    @Override // org.springframework.web.filter.OncePerRequestFilter
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override // org.springframework.web.filter.OncePerRequestFilter
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse responseToUse = response;
        if (!isAsyncDispatch(request) && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new HttpStreamingAwareContentCachingResponseWrapper(response, request);
        }
        filterChain.doFilter(request, responseToUse);
        if (!isAsyncStarted(request) && !isContentCachingDisabled(request)) {
            updateResponse(request, responseToUse);
        }
    }

    private void updateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
        HttpServletResponse rawResponse = (HttpServletResponse) responseWrapper.getResponse();
        int statusCode = responseWrapper.getStatusCode();
        if (rawResponse.isCommitted()) {
            responseWrapper.copyBodyToResponse();
            return;
        }
        if (isEligibleForEtag(request, responseWrapper, statusCode, responseWrapper.getContentInputStream())) {
            String responseETag = generateETagHeaderValue(responseWrapper.getContentInputStream(), this.writeWeakETag);
            rawResponse.setHeader("ETag", responseETag);
            String requestETag = request.getHeader("If-None-Match");
            if (requestETag != null && ("*".equals(requestETag) || responseETag.equals(requestETag) || responseETag.replaceFirst("^W/", "").equals(requestETag.replaceFirst("^W/", "")))) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
                }
                rawResponse.setStatus(304);
                return;
            } else {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag + "], sending normal response");
                }
                responseWrapper.copyBodyToResponse();
                return;
            }
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
        }
        responseWrapper.copyBodyToResponse();
    }

    protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response, int responseStatusCode, InputStream inputStream) {
        String method = request.getMethod();
        if (responseStatusCode >= 200 && responseStatusCode < 300 && HttpMethod.GET.matches(method)) {
            String cacheControl = null;
            if (servlet3Present) {
                cacheControl = response.getHeader("Cache-Control");
            }
            if (cacheControl == null || !cacheControl.contains("no-store")) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak) throws IOException {
        StringBuilder builder = new StringBuilder(37);
        if (isWeak) {
            builder.append("W/");
        }
        builder.append("\"0");
        DigestUtils.appendMd5DigestAsHex(inputStream, builder);
        builder.append('\"');
        return builder.toString();
    }

    public static void disableContentCaching(ServletRequest request) {
        Assert.notNull(request, "ServletRequest must not be null");
        request.setAttribute(STREAMING_ATTRIBUTE, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isContentCachingDisabled(HttpServletRequest request) {
        return request.getAttribute(STREAMING_ATTRIBUTE) != null;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/filter/ShallowEtagHeaderFilter$HttpStreamingAwareContentCachingResponseWrapper.class */
    private static class HttpStreamingAwareContentCachingResponseWrapper extends ContentCachingResponseWrapper {
        private final HttpServletRequest request;

        public HttpStreamingAwareContentCachingResponseWrapper(HttpServletResponse response, HttpServletRequest request) {
            super(response);
            this.request = request;
        }

        @Override // org.springframework.web.util.ContentCachingResponseWrapper, javax.servlet.ServletResponseWrapper, javax.servlet.ServletResponse
        public ServletOutputStream getOutputStream() throws IOException {
            return useRawResponse() ? getResponse().getOutputStream() : super.getOutputStream();
        }

        @Override // org.springframework.web.util.ContentCachingResponseWrapper, javax.servlet.ServletResponseWrapper, javax.servlet.ServletResponse
        public PrintWriter getWriter() throws IOException {
            return useRawResponse() ? getResponse().getWriter() : super.getWriter();
        }

        private boolean useRawResponse() {
            return ShallowEtagHeaderFilter.isContentCachingDisabled(this.request);
        }
    }
}
