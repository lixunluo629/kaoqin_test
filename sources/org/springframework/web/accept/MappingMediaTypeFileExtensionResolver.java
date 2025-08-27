package org.springframework.web.accept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/MappingMediaTypeFileExtensionResolver.class */
public class MappingMediaTypeFileExtensionResolver implements MediaTypeFileExtensionResolver {
    private final ConcurrentMap<String, MediaType> mediaTypes = new ConcurrentHashMap(64);
    private final MultiValueMap<MediaType, String> fileExtensions = new LinkedMultiValueMap();
    private final List<String> allFileExtensions = new LinkedList();

    public MappingMediaTypeFileExtensionResolver(Map<String, MediaType> mediaTypes) {
        if (mediaTypes != null) {
            for (Map.Entry<String, MediaType> entries : mediaTypes.entrySet()) {
                String extension = entries.getKey().toLowerCase(Locale.ENGLISH);
                MediaType mediaType = entries.getValue();
                this.mediaTypes.put(extension, mediaType);
                this.fileExtensions.add(mediaType, extension);
                this.allFileExtensions.add(extension);
            }
        }
    }

    public Map<String, MediaType> getMediaTypes() {
        return this.mediaTypes;
    }

    protected List<MediaType> getAllMediaTypes() {
        return new ArrayList(this.mediaTypes.values());
    }

    protected void addMapping(String extension, MediaType mediaType) {
        MediaType previous = this.mediaTypes.putIfAbsent(extension, mediaType);
        if (previous == null) {
            this.fileExtensions.add(mediaType, extension);
            this.allFileExtensions.add(extension);
        }
    }

    @Override // org.springframework.web.accept.MediaTypeFileExtensionResolver
    public List<String> resolveFileExtensions(MediaType mediaType) {
        List<String> fileExtensions = (List) this.fileExtensions.get(mediaType);
        return fileExtensions != null ? fileExtensions : Collections.emptyList();
    }

    @Override // org.springframework.web.accept.MediaTypeFileExtensionResolver
    public List<String> getAllFileExtensions() {
        return Collections.unmodifiableList(this.allFileExtensions);
    }

    protected MediaType lookupMediaType(String extension) {
        return this.mediaTypes.get(extension.toLowerCase(Locale.ENGLISH));
    }
}
