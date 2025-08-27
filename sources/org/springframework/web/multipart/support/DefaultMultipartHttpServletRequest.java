package org.springframework.web.multipart.support;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/support/DefaultMultipartHttpServletRequest.class */
public class DefaultMultipartHttpServletRequest extends AbstractMultipartHttpServletRequest {
    private static final String CONTENT_TYPE = "Content-Type";
    private Map<String, String[]> multipartParameters;
    private Map<String, String> multipartParameterContentTypes;

    public DefaultMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
        super(request);
        setMultipartFiles(mpFiles);
        setMultipartParameters(mpParams);
        setMultipartParameterContentTypes(mpParamContentTypes);
    }

    public DefaultMultipartHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public String getParameter(String name) {
        String[] values = getMultipartParameters().get(name);
        if (values != null) {
            if (values.length > 0) {
                return values[0];
            }
            return null;
        }
        return super.getParameter(name);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public String[] getParameterValues(String name) {
        String[] values = getMultipartParameters().get(name);
        if (values != null) {
            return values;
        }
        return super.getParameterValues(name);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Enumeration<String> getParameterNames() {
        Map<String, String[]> multipartParameters = getMultipartParameters();
        if (multipartParameters.isEmpty()) {
            return super.getParameterNames();
        }
        Set<String> paramNames = new LinkedHashSet<>();
        Enumeration<String> paramEnum = super.getParameterNames();
        while (paramEnum.hasMoreElements()) {
            paramNames.add(paramEnum.nextElement());
        }
        paramNames.addAll(multipartParameters.keySet());
        return Collections.enumeration(paramNames);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Map<String, String[]> getParameterMap() {
        Map<? extends String, ? extends String[]> multipartParameters = getMultipartParameters();
        if (multipartParameters.isEmpty()) {
            return super.getParameterMap();
        }
        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.putAll(super.getParameterMap());
        paramMap.putAll(multipartParameters);
        return paramMap;
    }

    @Override // org.springframework.web.multipart.MultipartRequest
    public String getMultipartContentType(String paramOrFileName) {
        MultipartFile file = getFile(paramOrFileName);
        if (file != null) {
            return file.getContentType();
        }
        return getMultipartParameterContentTypes().get(paramOrFileName);
    }

    @Override // org.springframework.web.multipart.MultipartHttpServletRequest
    public HttpHeaders getMultipartHeaders(String paramOrFileName) {
        String contentType = getMultipartContentType(paramOrFileName);
        if (contentType != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", contentType);
            return headers;
        }
        return null;
    }

    protected final void setMultipartParameters(Map<String, String[]> multipartParameters) {
        this.multipartParameters = multipartParameters;
    }

    protected Map<String, String[]> getMultipartParameters() {
        if (this.multipartParameters == null) {
            initializeMultipart();
        }
        return this.multipartParameters;
    }

    protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes) {
        this.multipartParameterContentTypes = multipartParameterContentTypes;
    }

    protected Map<String, String> getMultipartParameterContentTypes() {
        if (this.multipartParameterContentTypes == null) {
            initializeMultipart();
        }
        return this.multipartParameterContentTypes;
    }
}
