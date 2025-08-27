package org.springframework.web.servlet.resource;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.accept.ServletPathExtensionContentNegotiationStrategy;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/ResourceHttpRequestHandler.class */
public class ResourceHttpRequestHandler extends WebContentGenerator implements HttpRequestHandler, EmbeddedValueResolverAware, InitializingBean, CorsConfigurationSource {
    private static final boolean contentLengthLongAvailable = ClassUtils.hasMethod(ServletResponse.class, "setContentLengthLong", Long.TYPE);
    private static final Log logger = LogFactory.getLog(ResourceHttpRequestHandler.class);
    private static final String URL_RESOURCE_CHARSET_PREFIX = "[charset=";
    private final List<String> locationValues;
    private final List<Resource> locations;
    private final Map<Resource, Charset> locationCharsets;
    private final List<ResourceResolver> resourceResolvers;
    private final List<ResourceTransformer> resourceTransformers;
    private ResourceHttpMessageConverter resourceHttpMessageConverter;
    private ResourceRegionHttpMessageConverter resourceRegionHttpMessageConverter;
    private ContentNegotiationManager contentNegotiationManager;
    private PathExtensionContentNegotiationStrategy contentNegotiationStrategy;
    private CorsConfiguration corsConfiguration;
    private UrlPathHelper urlPathHelper;
    private StringValueResolver embeddedValueResolver;

    public ResourceHttpRequestHandler() {
        super(HttpMethod.GET.name(), HttpMethod.HEAD.name());
        this.locationValues = new ArrayList(4);
        this.locations = new ArrayList(4);
        this.locationCharsets = new HashMap(4);
        this.resourceResolvers = new ArrayList(4);
        this.resourceTransformers = new ArrayList(4);
    }

    public void setLocationValues(List<String> locationValues) {
        Assert.notNull(locationValues, "Location values list must not be null");
        this.locationValues.clear();
        this.locationValues.addAll(locationValues);
    }

    public void setLocations(List<Resource> locations) {
        Assert.notNull(locations, "Locations list must not be null");
        this.locations.clear();
        this.locations.addAll(locations);
    }

    public List<Resource> getLocations() {
        return this.locations;
    }

    public void setResourceResolvers(List<ResourceResolver> resourceResolvers) {
        this.resourceResolvers.clear();
        if (resourceResolvers != null) {
            this.resourceResolvers.addAll(resourceResolvers);
        }
    }

    public List<ResourceResolver> getResourceResolvers() {
        return this.resourceResolvers;
    }

    public void setResourceTransformers(List<ResourceTransformer> resourceTransformers) {
        this.resourceTransformers.clear();
        if (resourceTransformers != null) {
            this.resourceTransformers.addAll(resourceTransformers);
        }
    }

    public List<ResourceTransformer> getResourceTransformers() {
        return this.resourceTransformers;
    }

    public void setResourceHttpMessageConverter(ResourceHttpMessageConverter messageConverter) {
        this.resourceHttpMessageConverter = messageConverter;
    }

    public ResourceHttpMessageConverter getResourceHttpMessageConverter() {
        return this.resourceHttpMessageConverter;
    }

    public void setResourceRegionHttpMessageConverter(ResourceRegionHttpMessageConverter messageConverter) {
        this.resourceRegionHttpMessageConverter = messageConverter;
    }

    public ResourceRegionHttpMessageConverter getResourceRegionHttpMessageConverter() {
        return this.resourceRegionHttpMessageConverter;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    public void setCorsConfiguration(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    @Override // org.springframework.web.cors.CorsConfigurationSource
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return this.corsConfiguration;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public UrlPathHelper getUrlPathHelper() {
        return this.urlPathHelper;
    }

    @Override // org.springframework.context.EmbeddedValueResolverAware
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        resolveResourceLocations();
        if (logger.isWarnEnabled() && CollectionUtils.isEmpty(this.locations)) {
            logger.warn("Locations list is empty. No resources will be served unless a custom ResourceResolver is configured as an alternative to PathResourceResolver.");
        }
        if (this.resourceResolvers.isEmpty()) {
            this.resourceResolvers.add(new PathResourceResolver());
        }
        initAllowedLocations();
        if (this.resourceHttpMessageConverter == null) {
            this.resourceHttpMessageConverter = new ResourceHttpMessageConverter();
        }
        if (this.resourceRegionHttpMessageConverter == null) {
            this.resourceRegionHttpMessageConverter = new ResourceRegionHttpMessageConverter();
        }
        this.contentNegotiationStrategy = initContentNegotiationStrategy();
    }

    private void resolveResourceLocations() {
        if (CollectionUtils.isEmpty(this.locationValues)) {
            return;
        }
        if (!CollectionUtils.isEmpty(this.locations)) {
            throw new IllegalArgumentException("Please set either Resource-based \"locations\" or String-based \"locationValues\", but not both.");
        }
        ApplicationContext applicationContext = getApplicationContext();
        for (String location : this.locationValues) {
            if (this.embeddedValueResolver != null) {
                String resolvedLocation = this.embeddedValueResolver.resolveStringValue(location);
                if (resolvedLocation == null) {
                    throw new IllegalArgumentException("Location resolved to null: " + location);
                }
                location = resolvedLocation;
            }
            Charset charset = null;
            String location2 = location.trim();
            if (location2.startsWith(URL_RESOURCE_CHARSET_PREFIX)) {
                int endIndex = location2.indexOf(93, URL_RESOURCE_CHARSET_PREFIX.length());
                if (endIndex == -1) {
                    throw new IllegalArgumentException("Invalid charset syntax in location: " + location2);
                }
                String value = location2.substring(URL_RESOURCE_CHARSET_PREFIX.length(), endIndex);
                charset = Charset.forName(value);
                location2 = location2.substring(endIndex + 1);
            }
            Resource resource = applicationContext.getResource(location2);
            this.locations.add(resource);
            if (charset != null) {
                if (!(resource instanceof UrlResource)) {
                    throw new IllegalArgumentException("Unexpected charset for non-UrlResource: " + resource);
                }
                this.locationCharsets.put(resource, charset);
            }
        }
    }

    protected void initAllowedLocations() {
        if (CollectionUtils.isEmpty(this.locations)) {
            return;
        }
        for (int i = getResourceResolvers().size() - 1; i >= 0; i--) {
            if (getResourceResolvers().get(i) instanceof PathResourceResolver) {
                PathResourceResolver pathResolver = (PathResourceResolver) getResourceResolvers().get(i);
                if (ObjectUtils.isEmpty((Object[]) pathResolver.getAllowedLocations())) {
                    pathResolver.setAllowedLocations((Resource[]) getLocations().toArray(new Resource[getLocations().size()]));
                }
                if (this.urlPathHelper != null) {
                    pathResolver.setLocationCharsets(this.locationCharsets);
                    pathResolver.setUrlPathHelper(this.urlPathHelper);
                    return;
                }
                return;
            }
        }
    }

    protected PathExtensionContentNegotiationStrategy initContentNegotiationStrategy() {
        PathExtensionContentNegotiationStrategy strategy;
        Map<String, MediaType> mediaTypes = null;
        if (getContentNegotiationManager() != null && (strategy = (PathExtensionContentNegotiationStrategy) getContentNegotiationManager().getStrategy(PathExtensionContentNegotiationStrategy.class)) != null) {
            mediaTypes = new HashMap<>(strategy.getMediaTypes());
        }
        return getServletContext() != null ? new ServletPathExtensionContentNegotiationStrategy(getServletContext(), mediaTypes) : new PathExtensionContentNegotiationStrategy(mediaTypes);
    }

    @Override // org.springframework.web.HttpRequestHandler
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Resource resource = getResource(request);
        if (resource == null) {
            logger.trace("No matching resource found - returning 404");
            response.sendError(404);
            return;
        }
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setHeader("Allow", getAllowHeader());
            return;
        }
        checkRequest(request);
        if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified())) {
            logger.trace("Resource not modified - returning 304");
            return;
        }
        prepareResponse(response);
        MediaType mediaType = getMediaType(request, resource);
        if (mediaType != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Determined media type '" + mediaType + "' for " + resource);
            }
        } else if (logger.isTraceEnabled()) {
            logger.trace("No media type found for " + resource + " - not sending a content-type header");
        }
        if (WebContentGenerator.METHOD_HEAD.equals(request.getMethod())) {
            setHeaders(response, resource, mediaType);
            logger.trace("HEAD request - skipping content");
            return;
        }
        ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
        if (request.getHeader("Range") == null) {
            setHeaders(response, resource, mediaType);
            this.resourceHttpMessageConverter.write(resource, mediaType, outputMessage);
            return;
        }
        response.setHeader("Accept-Ranges", "bytes");
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        try {
            List<HttpRange> httpRanges = inputMessage.getHeaders().getRange();
            response.setStatus(206);
            if (httpRanges.size() == 1) {
                ResourceRegion resourceRegion = httpRanges.get(0).toResourceRegion(resource);
                this.resourceRegionHttpMessageConverter.write(resourceRegion, mediaType, outputMessage);
            } else {
                this.resourceRegionHttpMessageConverter.write(HttpRange.toResourceRegions(httpRanges, resource), mediaType, outputMessage);
            }
        } catch (IllegalArgumentException e) {
            response.setHeader("Content-Range", "bytes */" + resource.contentLength());
            response.sendError(416);
        }
    }

    protected Resource getResource(HttpServletRequest request) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (path == null) {
            throw new IllegalStateException("Required request attribute '" + HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
        }
        String path2 = processPath(path);
        if (!StringUtils.hasText(path2) || isInvalidPath(path2)) {
            if (logger.isTraceEnabled()) {
                logger.trace("Ignoring invalid resource path [" + path2 + "]");
                return null;
            }
            return null;
        }
        if (isInvalidEncodedPath(path2)) {
            if (logger.isTraceEnabled()) {
                logger.trace("Ignoring invalid resource path with escape sequences [" + path2 + "]");
                return null;
            }
            return null;
        }
        ResourceResolverChain resolveChain = new DefaultResourceResolverChain(getResourceResolvers());
        Resource resource = resolveChain.resolveResource(request, path2, getLocations());
        if (resource == null || getResourceTransformers().isEmpty()) {
            return resource;
        }
        ResourceTransformerChain transformChain = new DefaultResourceTransformerChain(resolveChain, getResourceTransformers());
        return transformChain.transform(request, resource);
    }

    protected String processPath(String path) {
        return cleanLeadingSlash(cleanDuplicateSlashes(StringUtils.replace(path, "\\", "/")));
    }

    private String cleanDuplicateSlashes(String path) {
        char c;
        StringBuilder sb = null;
        char prev = 0;
        for (int i = 0; i < path.length(); i++) {
            char curr = path.charAt(i);
            if (curr == '/' && prev == '/') {
                if (sb == null) {
                    sb = new StringBuilder(path.substring(0, i));
                }
                c = curr;
            } else {
                if (sb != null) {
                    sb.append(path.charAt(i));
                }
                c = curr;
            }
            prev = c;
        }
        return sb != null ? sb.toString() : path;
    }

    private String cleanLeadingSlash(String path) {
        boolean slash = false;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                slash = true;
            } else if (path.charAt(i) > ' ' && path.charAt(i) != 127) {
                if (i == 0 || (i == 1 && slash)) {
                    return path;
                }
                String path2 = slash ? "/" + path.substring(i) : path.substring(i);
                if (logger.isTraceEnabled()) {
                    logger.trace("Path after trimming leading '/' and control characters: [" + path2 + "]");
                }
                return path2;
            }
        }
        return slash ? "/" : "";
    }

    private boolean isInvalidEncodedPath(String path) throws UnsupportedEncodingException {
        if (path.contains(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            try {
                String decodedPath = URLDecoder.decode(path, "UTF-8");
                if (isInvalidPath(decodedPath)) {
                    return true;
                }
                if (isInvalidPath(processPath(decodedPath))) {
                    return true;
                }
                return false;
            } catch (UnsupportedEncodingException e) {
                return false;
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }
        return false;
    }

    protected boolean isInvalidPath(String path) {
        if (path.contains("WEB-INF") || path.contains("META-INF")) {
            if (logger.isTraceEnabled()) {
                logger.trace("Path with \"WEB-INF\" or \"META-INF\": [" + path + "]");
                return true;
            }
            return true;
        }
        if (path.contains(":/")) {
            String relativePath = path.charAt(0) == '/' ? path.substring(1) : path;
            if (ResourceUtils.isUrl(relativePath) || relativePath.startsWith("url:")) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Path represents URL or has \"url:\" prefix: [" + path + "]");
                    return true;
                }
                return true;
            }
        }
        if (path.contains("..") && StringUtils.cleanPath(path).contains("../")) {
            if (logger.isTraceEnabled()) {
                logger.trace("Path contains \"../\" after call to StringUtils#cleanPath: [" + path + "]");
                return true;
            }
            return true;
        }
        return false;
    }

    protected MediaType getMediaType(HttpServletRequest request, Resource resource) {
        MediaType mediaType = getMediaType(resource);
        if (mediaType != null) {
            return mediaType;
        }
        return this.contentNegotiationStrategy.getMediaTypeForResource(resource);
    }

    @Deprecated
    protected MediaType getMediaType(Resource resource) {
        return null;
    }

    protected void setHeaders(HttpServletResponse response, Resource resource, MediaType mediaType) throws IOException {
        long length = resource.contentLength();
        if (length > 2147483647L) {
            if (contentLengthLongAvailable) {
                response.setContentLengthLong(length);
            } else {
                response.setHeader("Content-Length", Long.toString(length));
            }
        } else {
            response.setContentLength((int) length);
        }
        if (mediaType != null) {
            response.setContentType(mediaType.toString());
        }
        if (resource instanceof EncodedResource) {
            response.setHeader("Content-Encoding", ((EncodedResource) resource).getContentEncoding());
        }
        if (resource instanceof VersionedResource) {
            response.setHeader("ETag", SymbolConstants.QUOTES_SYMBOL + ((VersionedResource) resource).getVersion() + SymbolConstants.QUOTES_SYMBOL);
        }
        response.setHeader("Accept-Ranges", "bytes");
    }

    public String toString() {
        return "ResourceHttpRequestHandler [locations=" + getLocations() + ", resolvers=" + getResourceResolvers() + "]";
    }
}
