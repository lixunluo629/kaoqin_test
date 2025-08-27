package org.springframework.web.accept;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/PathExtensionContentNegotiationStrategy.class */
public class PathExtensionContentNegotiationStrategy extends AbstractMappingContentNegotiationStrategy {
    private static final boolean JAF_PRESENT = ClassUtils.isPresent("javax.activation.FileTypeMap", PathExtensionContentNegotiationStrategy.class.getClassLoader());
    private static final Log logger = LogFactory.getLog(PathExtensionContentNegotiationStrategy.class);
    private UrlPathHelper urlPathHelper;
    private boolean useJaf;
    private boolean ignoreUnknownExtensions;

    public PathExtensionContentNegotiationStrategy() {
        this(null);
    }

    public PathExtensionContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
        this.urlPathHelper = new UrlPathHelper();
        this.useJaf = true;
        this.ignoreUnknownExtensions = true;
        this.urlPathHelper.setUrlDecode(false);
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public void setUseJaf(boolean useJaf) {
        this.useJaf = useJaf;
    }

    public void setIgnoreUnknownExtensions(boolean ignoreUnknownExtensions) {
        this.ignoreUnknownExtensions = ignoreUnknownExtensions;
    }

    @Override // org.springframework.web.accept.AbstractMappingContentNegotiationStrategy
    protected String getMediaTypeKey(NativeWebRequest webRequest) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            logger.warn("An HttpServletRequest is required to determine the media type key");
            return null;
        }
        String path = this.urlPathHelper.getLookupPathForRequest(request);
        String extension = UriUtils.extractFileExtension(path);
        if (StringUtils.hasText(extension)) {
            return extension.toLowerCase(Locale.ENGLISH);
        }
        return null;
    }

    @Override // org.springframework.web.accept.AbstractMappingContentNegotiationStrategy
    protected MediaType handleNoMatch(NativeWebRequest webRequest, String extension) throws HttpMediaTypeNotAcceptableException {
        MediaType mediaType;
        if (this.useJaf && JAF_PRESENT && (mediaType = ActivationMediaTypeFactory.getMediaType("file." + extension)) != null && !MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
            return mediaType;
        }
        if (this.ignoreUnknownExtensions) {
            return null;
        }
        throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
    }

    public MediaType getMediaTypeForResource(Resource resource) {
        Assert.notNull(resource, "Resource must not be null");
        MediaType mediaType = null;
        String filename = resource.getFilename();
        String extension = StringUtils.getFilenameExtension(filename);
        if (extension != null) {
            mediaType = lookupMediaType(extension);
        }
        if (mediaType == null && JAF_PRESENT) {
            mediaType = ActivationMediaTypeFactory.getMediaType(filename);
        }
        if (MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
            mediaType = null;
        }
        return mediaType;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/accept/PathExtensionContentNegotiationStrategy$ActivationMediaTypeFactory.class */
    private static class ActivationMediaTypeFactory {
        private static final FileTypeMap fileTypeMap = initFileTypeMap();

        private ActivationMediaTypeFactory() {
        }

        private static FileTypeMap initFileTypeMap() throws IOException {
            Resource resource = new ClassPathResource("org/springframework/mail/javamail/mime.types");
            if (resource.exists()) {
                if (PathExtensionContentNegotiationStrategy.logger.isTraceEnabled()) {
                    PathExtensionContentNegotiationStrategy.logger.trace("Loading JAF FileTypeMap from " + resource);
                }
                InputStream inputStream = null;
                try {
                    inputStream = resource.getInputStream();
                    MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap(inputStream);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                        }
                    }
                    return mimetypesFileTypeMap;
                } catch (IOException e2) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            }
            if (PathExtensionContentNegotiationStrategy.logger.isTraceEnabled()) {
                PathExtensionContentNegotiationStrategy.logger.trace("Loading default Java Activation Framework FileTypeMap");
            }
            return FileTypeMap.getDefaultFileTypeMap();
        }

        public static MediaType getMediaType(String filename) {
            String mediaType = fileTypeMap.getContentType(filename);
            if (StringUtils.hasText(mediaType)) {
                return MediaType.parseMediaType(mediaType);
            }
            return null;
        }
    }
}
