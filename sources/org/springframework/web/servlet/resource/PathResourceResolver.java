package org.springframework.web.servlet.resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/PathResourceResolver.class */
public class PathResourceResolver extends AbstractResourceResolver {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Resource[] allowedLocations;
    private final Map<Resource, Charset> locationCharsets = new HashMap(4);
    private UrlPathHelper urlPathHelper;

    public void setAllowedLocations(Resource... locations) {
        this.allowedLocations = locations;
    }

    public Resource[] getAllowedLocations() {
        return this.allowedLocations;
    }

    public void setLocationCharsets(Map<Resource, Charset> locationCharsets) {
        this.locationCharsets.clear();
        this.locationCharsets.putAll(locationCharsets);
    }

    public Map<Resource, Charset> getLocationCharsets() {
        return Collections.unmodifiableMap(this.locationCharsets);
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public UrlPathHelper getUrlPathHelper() {
        return this.urlPathHelper;
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return getResource(requestPath, request, locations);
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected String resolveUrlPathInternal(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        if (!StringUtils.hasText(resourcePath) || getResource(resourcePath, null, locations) == null) {
            return null;
        }
        return resourcePath;
    }

    private Resource getResource(String resourcePath, HttpServletRequest request, List<? extends Resource> locations) {
        Resource resource;
        for (Resource location : locations) {
            try {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Checking location: " + location);
                }
                String pathToUse = encodeIfNecessary(resourcePath, request, location);
                resource = getResource(pathToUse, location);
            } catch (IOException ex) {
                this.logger.trace("Failure checking for relative resource - trying next location", ex);
            }
            if (resource != null) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Found match: " + resource);
                }
                return resource;
            }
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No match for location: " + location);
            }
        }
        return null;
    }

    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        Resource resource = location.createRelative(resourcePath);
        if (resource.exists() && resource.isReadable()) {
            if (checkResource(resource, location)) {
                return resource;
            }
            if (this.logger.isTraceEnabled()) {
                Resource[] allowedLocations = getAllowedLocations();
                this.logger.trace("Resource path \"" + resourcePath + "\" was successfully resolved but resource \"" + resource.getURL() + "\" is neither under the current location \"" + location.getURL() + "\" nor under any of the allowed locations " + (allowedLocations != null ? Arrays.asList(allowedLocations) : "[]"));
                return null;
            }
            return null;
        }
        return null;
    }

    protected boolean checkResource(Resource resource, Resource location) throws IOException {
        if (isResourceUnderLocation(resource, location)) {
            return true;
        }
        if (getAllowedLocations() != null) {
            for (Resource current : getAllowedLocations()) {
                if (isResourceUnderLocation(resource, current)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean isResourceUnderLocation(Resource resource, Resource location) throws IOException {
        String resourcePath;
        String locationPath;
        if (resource.getClass() != location.getClass()) {
            return false;
        }
        if (resource instanceof UrlResource) {
            resourcePath = resource.getURL().toExternalForm();
            locationPath = StringUtils.cleanPath(location.getURL().toString());
        } else if (resource instanceof ClassPathResource) {
            resourcePath = ((ClassPathResource) resource).getPath();
            locationPath = StringUtils.cleanPath(((ClassPathResource) location).getPath());
        } else if (resource instanceof ServletContextResource) {
            resourcePath = ((ServletContextResource) resource).getPath();
            locationPath = StringUtils.cleanPath(((ServletContextResource) location).getPath());
        } else {
            resourcePath = resource.getURL().getPath();
            locationPath = StringUtils.cleanPath(location.getURL().getPath());
        }
        if (locationPath.equals(resourcePath)) {
            return true;
        }
        return resourcePath.startsWith((locationPath.endsWith("/") || locationPath.isEmpty()) ? locationPath : new StringBuilder().append(locationPath).append("/").toString()) && !isInvalidEncodedPath(resourcePath);
    }

    private String encodeIfNecessary(String path, HttpServletRequest request, Resource location) {
        if (shouldEncodeRelativePath(location) && request != null) {
            Charset charset = this.locationCharsets.get(location);
            Charset charset2 = charset != null ? charset : DEFAULT_CHARSET;
            StringBuilder sb = new StringBuilder();
            StringTokenizer tokenizer = new StringTokenizer(path, "/");
            while (tokenizer.hasMoreTokens()) {
                try {
                    String value = UriUtils.encode(tokenizer.nextToken(), charset2.name());
                    sb.append(value);
                    sb.append("/");
                } catch (UnsupportedEncodingException ex) {
                    throw new IllegalStateException("Unexpected error", ex);
                }
            }
            if (!path.endsWith("/")) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        }
        return path;
    }

    private boolean shouldEncodeRelativePath(Resource location) {
        return (location instanceof UrlResource) && this.urlPathHelper != null && this.urlPathHelper.isUrlDecode();
    }

    private boolean isInvalidEncodedPath(String resourcePath) throws UnsupportedEncodingException {
        if (resourcePath.contains(QuickTargetSourceCreator.PREFIX_THREAD_LOCAL)) {
            try {
                String decodedPath = URLDecoder.decode(resourcePath, "UTF-8");
                if (decodedPath.contains("../") || decodedPath.contains("..\\")) {
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Resolved resource path contains encoded \"../\" or \"..\\\": " + resourcePath);
                        return true;
                    }
                    return true;
                }
                return false;
            } catch (UnsupportedEncodingException e) {
                return false;
            }
        }
        return false;
    }
}
