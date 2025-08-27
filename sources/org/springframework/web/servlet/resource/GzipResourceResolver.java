package org.springframework.web.servlet.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/GzipResourceResolver.class */
public class GzipResourceResolver extends AbstractResourceResolver {
    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        Resource resource = chain.resolveResource(request, requestPath, locations);
        if (resource == null || (request != null && !isGzipAccepted(request))) {
            return resource;
        }
        try {
            Resource gzipped = new GzippedResource(resource);
            if (gzipped.exists()) {
                return gzipped;
            }
        } catch (IOException ex) {
            this.logger.trace("No gzip resource for [" + resource.getFilename() + "]", ex);
        }
        return resource;
    }

    private boolean isGzipAccepted(HttpServletRequest request) {
        String value = request.getHeader("Accept-Encoding");
        return value != null && value.toLowerCase().contains("gzip");
    }

    @Override // org.springframework.web.servlet.resource.AbstractResourceResolver
    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return chain.resolveUrlPath(resourceUrlPath, locations);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/GzipResourceResolver$GzippedResource.class */
    private static final class GzippedResource extends AbstractResource implements EncodedResource {
        private final Resource original;
        private final Resource gzipped;

        public GzippedResource(Resource original) throws IOException {
            this.original = original;
            this.gzipped = original.createRelative(original.getFilename() + ".gz");
        }

        @Override // org.springframework.core.io.InputStreamSource
        public InputStream getInputStream() throws IOException {
            return this.gzipped.getInputStream();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean exists() {
            return this.gzipped.exists();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean isReadable() {
            return this.gzipped.isReadable();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public boolean isOpen() {
            return this.gzipped.isOpen();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public URL getURL() throws IOException {
            return this.gzipped.getURL();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public URI getURI() throws IOException {
            return this.gzipped.getURI();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public File getFile() throws IOException {
            return this.gzipped.getFile();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public long contentLength() throws IOException {
            return this.gzipped.contentLength();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public long lastModified() throws IOException {
            return this.gzipped.lastModified();
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public Resource createRelative(String relativePath) throws IOException {
            return this.gzipped.createRelative(relativePath);
        }

        @Override // org.springframework.core.io.AbstractResource, org.springframework.core.io.Resource
        public String getFilename() {
            return this.original.getFilename();
        }

        @Override // org.springframework.core.io.Resource
        public String getDescription() {
            return this.gzipped.getDescription();
        }

        @Override // org.springframework.web.servlet.resource.EncodedResource
        public String getContentEncoding() {
            return "gzip";
        }
    }
}
