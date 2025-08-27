package org.springframework.web.servlet.view.json;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/json/MappingJackson2JsonView.class */
public class MappingJackson2JsonView extends AbstractJackson2View {
    public static final String DEFAULT_CONTENT_TYPE = "application/json";

    @Deprecated
    public static final String DEFAULT_JSONP_CONTENT_TYPE = "application/javascript";
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
    private String jsonPrefix;
    private Set<String> modelKeys;
    private boolean extractValueFromSingleKeyModel;
    private Set<String> jsonpParameterNames;

    public MappingJackson2JsonView() {
        super(Jackson2ObjectMapperBuilder.json().build(), "application/json");
        this.extractValueFromSingleKeyModel = false;
        this.jsonpParameterNames = new LinkedHashSet();
    }

    public MappingJackson2JsonView(ObjectMapper objectMapper) {
        super(objectMapper, "application/json");
        this.extractValueFromSingleKeyModel = false;
        this.jsonpParameterNames = new LinkedHashSet();
    }

    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = prefixJson ? ")]}', " : null;
    }

    @Override // org.springframework.web.servlet.view.json.AbstractJackson2View
    public void setModelKey(String modelKey) {
        this.modelKeys = Collections.singleton(modelKey);
    }

    public void setModelKeys(Set<String> modelKeys) {
        this.modelKeys = modelKeys;
    }

    public final Set<String> getModelKeys() {
        return this.modelKeys;
    }

    public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
        this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
    }

    @Deprecated
    public void setJsonpParameterNames(Set<String> jsonpParameterNames) {
        this.jsonpParameterNames = jsonpParameterNames;
    }

    private String getJsonpParameterValue(HttpServletRequest request) {
        if (this.jsonpParameterNames != null) {
            for (String name : this.jsonpParameterNames) {
                String value = request.getParameter(name);
                if (!StringUtils.isEmpty(value)) {
                    if (!isValidJsonpQueryParam(value)) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Ignoring invalid jsonp parameter value: " + value);
                        }
                    } else {
                        return value;
                    }
                }
            }
            return null;
        }
        return null;
    }

    @Deprecated
    protected boolean isValidJsonpQueryParam(String value) {
        return CALLBACK_PARAM_PATTERN.matcher(value).matches();
    }

    @Override // org.springframework.web.servlet.view.json.AbstractJackson2View
    protected Object filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<>(model.size());
        Set<String> modelKeys = !CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && modelKeys.contains(entry.getKey()) && !entry.getKey().equals(JsonView.class.getName()) && !entry.getKey().equals(FilterProvider.class.getName())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return (this.extractValueFromSingleKeyModel && result.size() == 1) ? result.values().iterator().next() : result;
    }

    @Override // org.springframework.web.servlet.view.json.AbstractJackson2View
    protected Object filterAndWrapModel(Map<String, Object> model, HttpServletRequest request) {
        Object value = super.filterAndWrapModel(model, request);
        String jsonpParameterValue = getJsonpParameterValue(request);
        if (jsonpParameterValue != null) {
            if (value instanceof MappingJacksonValue) {
                ((MappingJacksonValue) value).setJsonpFunction(jsonpParameterValue);
            } else {
                MappingJacksonValue container = new MappingJacksonValue(value);
                container.setJsonpFunction(jsonpParameterValue);
                value = container;
            }
        }
        return value;
    }

    @Override // org.springframework.web.servlet.view.json.AbstractJackson2View
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
        String jsonpFunction = null;
        if (object instanceof MappingJacksonValue) {
            jsonpFunction = ((MappingJacksonValue) object).getJsonpFunction();
        }
        if (jsonpFunction != null) {
            generator.writeRaw("/**/");
            generator.writeRaw(jsonpFunction + "(");
        }
    }

    @Override // org.springframework.web.servlet.view.json.AbstractJackson2View
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction = null;
        if (object instanceof MappingJacksonValue) {
            jsonpFunction = ((MappingJacksonValue) object).getJsonpFunction();
        }
        if (jsonpFunction != null) {
            generator.writeRaw(");");
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
        if (getJsonpParameterValue(request) != null) {
            response.setContentType(DEFAULT_JSONP_CONTENT_TYPE);
        } else {
            super.setResponseContentType(request, response);
        }
    }
}
