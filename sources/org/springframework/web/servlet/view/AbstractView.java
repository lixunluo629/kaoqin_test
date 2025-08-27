package org.springframework.web.servlet.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/AbstractView.class */
public abstract class AbstractView extends WebApplicationObjectSupport implements View, BeanNameAware {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
    private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;
    private String requestContextAttribute;
    private Set<String> exposedContextBeanNames;
    private String beanName;
    private String contentType = DEFAULT_CONTENT_TYPE;
    private final Map<String, Object> staticAttributes = new LinkedHashMap();
    private boolean exposePathVariables = true;
    private boolean exposeContextBeansAsAttributes = false;

    protected abstract void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override // org.springframework.web.servlet.View
    public String getContentType() {
        return this.contentType;
    }

    public void setRequestContextAttribute(String requestContextAttribute) {
        this.requestContextAttribute = requestContextAttribute;
    }

    public String getRequestContextAttribute() {
        return this.requestContextAttribute;
    }

    public void setAttributesCSV(String propString) throws IllegalArgumentException {
        if (propString != null) {
            StringTokenizer st = new StringTokenizer(propString, ",");
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                int eqIdx = tok.indexOf(61);
                if (eqIdx == -1) {
                    throw new IllegalArgumentException("Expected '=' in attributes CSV string '" + propString + "'");
                }
                if (eqIdx >= tok.length() - 2) {
                    throw new IllegalArgumentException("At least 2 characters ([]) required in attributes CSV string '" + propString + "'");
                }
                String name = tok.substring(0, eqIdx);
                String value = tok.substring(eqIdx + 1).substring(1);
                addStaticAttribute(name, value.substring(0, value.length() - 1));
            }
        }
    }

    public void setAttributes(Properties attributes) {
        CollectionUtils.mergePropertiesIntoMap(attributes, this.staticAttributes);
    }

    public void setAttributesMap(Map<String, ?> attributes) {
        if (attributes != null) {
            for (Map.Entry<String, ?> entry : attributes.entrySet()) {
                addStaticAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, Object> getAttributesMap() {
        return this.staticAttributes;
    }

    public void addStaticAttribute(String name, Object value) {
        this.staticAttributes.put(name, value);
    }

    public Map<String, Object> getStaticAttributes() {
        return Collections.unmodifiableMap(this.staticAttributes);
    }

    public void setExposePathVariables(boolean exposePathVariables) {
        this.exposePathVariables = exposePathVariables;
    }

    public boolean isExposePathVariables() {
        return this.exposePathVariables;
    }

    public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes) {
        this.exposeContextBeansAsAttributes = exposeContextBeansAsAttributes;
    }

    public void setExposedContextBeanNames(String... exposedContextBeanNames) {
        this.exposedContextBeanNames = new HashSet(Arrays.asList(exposedContextBeanNames));
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    @Override // org.springframework.web.servlet.View
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Rendering view with name '" + this.beanName + "' with model " + model + " and static attributes " + this.staticAttributes);
        }
        Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
        prepareResponse(request, response);
        renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
    }

    protected Map<String, Object> createMergedOutputModel(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        Map<? extends String, ? extends Object> pathVars = this.exposePathVariables ? (Map) request.getAttribute(View.PATH_VARIABLES) : null;
        int size = this.staticAttributes.size();
        Map<String, Object> mergedModel = new LinkedHashMap<>(size + (model != null ? model.size() : 0) + (pathVars != null ? pathVars.size() : 0));
        mergedModel.putAll(this.staticAttributes);
        if (pathVars != null) {
            mergedModel.putAll(pathVars);
        }
        if (model != null) {
            mergedModel.putAll(model);
        }
        if (this.requestContextAttribute != null) {
            mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
        }
        return mergedModel;
    }

    protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        return new RequestContext(request, response, getServletContext(), model);
    }

    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        if (generatesDownloadContent()) {
            response.setHeader("Pragma", "private");
            response.setHeader("Cache-Control", "private, must-revalidate");
        }
    }

    protected boolean generatesDownloadContent() {
        return false;
    }

    protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest) {
        if (this.exposeContextBeansAsAttributes || this.exposedContextBeanNames != null) {
            return new ContextExposingHttpServletRequest(originalRequest, getWebApplicationContext(), this.exposedContextBeanNames);
        }
        return originalRequest;
    }

    protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) throws Exception {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            String modelName = entry.getKey();
            Object modelValue = entry.getValue();
            if (modelValue != null) {
                request.setAttribute(modelName, modelValue);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added model object '" + modelName + "' of type [" + modelValue.getClass().getName() + "] to request in view with name '" + getBeanName() + "'");
                }
            } else {
                request.removeAttribute(modelName);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Removed model object '" + modelName + "' from request in view with name '" + getBeanName() + "'");
                }
            }
        }
    }

    protected ByteArrayOutputStream createTemporaryOutputStream() {
        return new ByteArrayOutputStream(4096);
    }

    protected void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos) throws IOException {
        response.setContentType(getContentType());
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }

    protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
        MediaType mediaType = (MediaType) request.getAttribute(View.SELECTED_CONTENT_TYPE);
        if (mediaType != null && mediaType.isConcrete()) {
            response.setContentType(mediaType.toString());
        } else {
            response.setContentType(getContentType());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        if (getBeanName() != null) {
            sb.append(": name '").append(getBeanName()).append("'");
        } else {
            sb.append(": unnamed");
        }
        return sb.toString();
    }
}
