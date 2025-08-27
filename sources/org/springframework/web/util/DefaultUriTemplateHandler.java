package org.springframework.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/DefaultUriTemplateHandler.class */
public class DefaultUriTemplateHandler extends AbstractUriTemplateHandler {
    private boolean parsePath;
    private boolean strictEncoding;

    public void setParsePath(boolean parsePath) {
        this.parsePath = parsePath;
    }

    public boolean shouldParsePath() {
        return this.parsePath;
    }

    public void setStrictEncoding(boolean strictEncoding) {
        this.strictEncoding = strictEncoding;
    }

    public boolean isStrictEncoding() {
        return this.strictEncoding;
    }

    @Override // org.springframework.web.util.AbstractUriTemplateHandler
    protected URI expandInternal(String uriTemplate, Map<String, ?> uriVariables) throws IllegalArgumentException {
        UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
        UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
        return createUri(uriComponents);
    }

    @Override // org.springframework.web.util.AbstractUriTemplateHandler
    protected URI expandInternal(String uriTemplate, Object... uriVariables) throws IllegalArgumentException {
        UriComponentsBuilder uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
        UriComponents uriComponents = expandAndEncode(uriComponentsBuilder, uriVariables);
        return createUri(uriComponents);
    }

    protected UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) throws IllegalArgumentException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
        if (shouldParsePath() && !isStrictEncoding()) {
            List<String> pathSegments = builder.build().getPathSegments();
            builder.replacePath(null);
            for (String pathSegment : pathSegments) {
                builder.pathSegment(pathSegment);
            }
        }
        return builder;
    }

    protected UriComponents expandAndEncode(UriComponentsBuilder builder, Map<String, ?> uriVariables) {
        if (!isStrictEncoding()) {
            return builder.buildAndExpand(uriVariables).encode();
        }
        Map<String, Object> encodedUriVars = new HashMap<>(uriVariables.size());
        for (Map.Entry<String, ?> entry : uriVariables.entrySet()) {
            encodedUriVars.put(entry.getKey(), applyStrictEncoding(entry.getValue()));
        }
        return builder.buildAndExpand((Map<String, ?>) encodedUriVars);
    }

    protected UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
        if (!isStrictEncoding()) {
            return builder.buildAndExpand(uriVariables).encode();
        }
        Object[] encodedUriVars = new Object[uriVariables.length];
        for (int i = 0; i < uriVariables.length; i++) {
            encodedUriVars[i] = applyStrictEncoding(uriVariables[i]);
        }
        return builder.buildAndExpand(encodedUriVars);
    }

    private String applyStrictEncoding(Object value) {
        String stringValue = value != null ? value.toString() : "";
        try {
            return UriUtils.encode(stringValue, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Failed to encode URI variable", ex);
        }
    }

    private URI createUri(UriComponents uriComponents) {
        try {
            return new URI(uriComponents.toUriString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }
}
