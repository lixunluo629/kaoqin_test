package org.springframework.web.servlet.resource;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/VersionResourceResolver.class */
public class VersionResourceResolver extends AbstractResourceResolver {
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Map<String, VersionStrategy> versionStrategyMap = new LinkedHashMap();

    public void setStrategyMap(Map<String, VersionStrategy> map) {
        this.versionStrategyMap.clear();
        this.versionStrategyMap.putAll(map);
    }

    public Map<String, VersionStrategy> getStrategyMap() {
        return this.versionStrategyMap;
    }

    public VersionResourceResolver addContentVersionStrategy(String... pathPatterns) {
        addVersionStrategy(new ContentVersionStrategy(), pathPatterns);
        return this;
    }

    public VersionResourceResolver addFixedVersionStrategy(String version, String... pathPatterns) {
        List<String> patternsList = Arrays.asList(pathPatterns);
        List<String> prefixedPatterns = new ArrayList<>(pathPatterns.length);
        String versionPrefix = "/" + version;
        for (String pattern : patternsList) {
            prefixedPatterns.add(pattern);
            if (!pattern.startsWith(versionPrefix) && !patternsList.contains(versionPrefix + pattern)) {
                prefixedPatterns.add(versionPrefix + pattern);
            }
        }
        return addVersionStrategy(new FixedVersionStrategy(version), StringUtils.toStringArray(prefixedPatterns));
    }

    public VersionResourceResolver addVersionStrategy(VersionStrategy strategy, String... pathPatterns) {
        for (String pattern : pathPatterns) {
            getStrategyMap().put(pattern, strategy);
        }
        return this;
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        Resource resolved = chain.resolveResource(request, requestPath, locations);
        if (resolved != null) {
            return resolved;
        }
        VersionStrategy versionStrategy = getStrategyForPath(requestPath);
        if (versionStrategy == null) {
            return null;
        }
        String candidateVersion = versionStrategy.extractVersion(requestPath);
        if (StringUtils.isEmpty(candidateVersion)) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No version found in path \"" + requestPath + SymbolConstants.QUOTES_SYMBOL);
                return null;
            }
            return null;
        }
        String simplePath = versionStrategy.removeVersion(requestPath, candidateVersion);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Extracted version from path, re-resolving without version: \"" + simplePath + SymbolConstants.QUOTES_SYMBOL);
        }
        Resource baseResource = chain.resolveResource(request, simplePath, locations);
        if (baseResource == null) {
            return null;
        }
        String actualVersion = versionStrategy.getResourceVersion(baseResource);
        if (candidateVersion.equals(actualVersion)) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Resource matches extracted version [" + candidateVersion + "]");
            }
            return new FileNameVersionedResource(baseResource, candidateVersion);
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Potential resource found for \"" + requestPath + "\", but version [" + candidateVersion + "] does not match");
            return null;
        }
        return null;
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        String baseUrl = chain.resolveUrlPath(resourceUrlPath, locations);
        if (StringUtils.hasText(baseUrl)) {
            VersionStrategy versionStrategy = getStrategyForPath(resourceUrlPath);
            if (versionStrategy == null) {
                return baseUrl;
            }
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Getting the original resource to determine version for path \"" + resourceUrlPath + SymbolConstants.QUOTES_SYMBOL);
            }
            Resource resource = chain.resolveResource(null, baseUrl, locations);
            String version = versionStrategy.getResourceVersion(resource);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Determined version [" + version + "] for " + resource);
            }
            return versionStrategy.addVersion(baseUrl, version);
        }
        return baseUrl;
    }

    protected VersionStrategy getStrategyForPath(String requestPath) {
        String path = "/".concat(requestPath);
        List<String> matchingPatterns = new ArrayList<>();
        for (String pattern : this.versionStrategyMap.keySet()) {
            if (this.pathMatcher.match(pattern, path)) {
                matchingPatterns.add(pattern);
            }
        }
        if (!matchingPatterns.isEmpty()) {
            Comparator<String> comparator = this.pathMatcher.getPatternComparator(path);
            Collections.sort(matchingPatterns, comparator);
            return this.versionStrategyMap.get(matchingPatterns.get(0));
        }
        return null;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/VersionResourceResolver$FileNameVersionedResource.class */
    private class FileNameVersionedResource extends AbstractResource implements VersionedResource {
        private final Resource original;
        private final String version;

        public FileNameVersionedResource(Resource original, String version) {
            this.original = original;
            this.version = version;
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean exists() {
            return this.original.exists();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean isReadable() {
            return this.original.isReadable();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean isOpen() {
            return this.original.isOpen();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public URL getURL() throws IOException {
            return this.original.getURL();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public URI getURI() throws IOException {
            return this.original.getURI();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public File getFile() throws IOException {
            return this.original.getFile();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public String getFilename() {
            return this.original.getFilename();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public long contentLength() throws IOException {
            return this.original.contentLength();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public long lastModified() throws IOException {
            return this.original.lastModified();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public Resource createRelative(String relativePath) throws IOException {
            return this.original.createRelative(relativePath);
        }

        @Override // org.springframework.core.io.Resource
        public String getDescription() {
            return this.original.getDescription();
        }

        @Override // org.springframework.core.io.InputStreamSource
        public InputStream getInputStream() throws IOException {
            return this.original.getInputStream();
        }

        @Override // org.springframework.web.servlet.resource.VersionedResource
        public String getVersion() {
            return this.version;
        }
    }
}
