package org.springframework.web.accept;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/ParameterContentNegotiationStrategy.class */
public class ParameterContentNegotiationStrategy extends AbstractMappingContentNegotiationStrategy {
    private static final Log logger = LogFactory.getLog(ParameterContentNegotiationStrategy.class);
    private String parameterName;

    public ParameterContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
        this.parameterName = "format";
    }

    public void setParameterName(String parameterName) {
        Assert.notNull(parameterName, "'parameterName' is required");
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    @Override // org.springframework.web.accept.AbstractMappingContentNegotiationStrategy
    protected String getMediaTypeKey(NativeWebRequest request) {
        return request.getParameter(getParameterName());
    }

    @Override // org.springframework.web.accept.AbstractMappingContentNegotiationStrategy
    protected void handleMatch(String mediaTypeKey, MediaType mediaType) {
        if (logger.isDebugEnabled()) {
            logger.debug("Requested media type: '" + mediaType + "' based on '" + getParameterName() + "'='" + mediaTypeKey + "'");
        }
    }

    @Override // org.springframework.web.accept.AbstractMappingContentNegotiationStrategy
    protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
        throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
    }
}
