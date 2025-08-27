package org.springframework.web.accept;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/HeaderContentNegotiationStrategy.class */
public class HeaderContentNegotiationStrategy implements ContentNegotiationStrategy {
    @Override // org.springframework.web.accept.ContentNegotiationStrategy
    public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
        String[] headerValueArray = request.getHeaderValues("Accept");
        if (headerValueArray == null) {
            return Collections.emptyList();
        }
        List<String> headerValues = Arrays.asList(headerValueArray);
        try {
            List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerValues);
            MediaType.sortBySpecificityAndQuality(mediaTypes);
            return mediaTypes;
        } catch (InvalidMediaTypeException ex) {
            throw new HttpMediaTypeNotAcceptableException("Could not parse 'Accept' header " + headerValues + ": " + ex.getMessage());
        }
    }
}
