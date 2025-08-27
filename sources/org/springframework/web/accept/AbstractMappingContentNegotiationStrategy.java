package org.springframework.web.accept;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/AbstractMappingContentNegotiationStrategy.class */
public abstract class AbstractMappingContentNegotiationStrategy extends MappingMediaTypeFileExtensionResolver implements ContentNegotiationStrategy {
    protected abstract String getMediaTypeKey(NativeWebRequest nativeWebRequest);

    public AbstractMappingContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
    }

    @Override // org.springframework.web.accept.ContentNegotiationStrategy
    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
        return resolveMediaTypeKey(webRequest, getMediaTypeKey(webRequest));
    }

    public List<MediaType> resolveMediaTypeKey(NativeWebRequest webRequest, String key) throws HttpMediaTypeNotAcceptableException {
        if (StringUtils.hasText(key)) {
            MediaType mediaType = lookupMediaType(key);
            if (mediaType != null) {
                handleMatch(key, mediaType);
                return Collections.singletonList(mediaType);
            }
            MediaType mediaType2 = handleNoMatch(webRequest, key);
            if (mediaType2 != null) {
                addMapping(key, mediaType2);
                return Collections.singletonList(mediaType2);
            }
        }
        return Collections.emptyList();
    }

    protected void handleMatch(String key, MediaType mediaType) {
    }

    protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
        return null;
    }
}
