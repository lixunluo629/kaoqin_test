package org.springframework.web.servlet.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AppCacheManifestTransformer.class */
public class AppCacheManifestTransformer extends ResourceTransformerSupport {
    private static final String MANIFEST_HEADER = "CACHE MANIFEST";
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Log logger = LogFactory.getLog(AppCacheManifestTransformer.class);
    private final Map<String, SectionTransformer> sectionTransformers;
    private final String fileExtension;

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AppCacheManifestTransformer$SectionTransformer.class */
    private interface SectionTransformer {
        String transform(String str, HashBuilder hashBuilder, Resource resource, ResourceTransformerChain resourceTransformerChain, HttpServletRequest httpServletRequest) throws IOException;
    }

    public AppCacheManifestTransformer() {
        this("manifest");
    }

    public AppCacheManifestTransformer(String fileExtension) {
        this.sectionTransformers = new HashMap();
        this.fileExtension = fileExtension;
        SectionTransformer noOpSection = new NoOpSection();
        this.sectionTransformers.put(MANIFEST_HEADER, noOpSection);
        this.sectionTransformers.put("NETWORK:", noOpSection);
        this.sectionTransformers.put("FALLBACK:", noOpSection);
        this.sectionTransformers.put("CACHE:", new CacheSection());
    }

    @Override // org.springframework.web.servlet.resource.ResourceTransformer
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        Resource resource2 = transformerChain.transform(request, resource);
        if (!this.fileExtension.equals(StringUtils.getFilenameExtension(resource2.getFilename()))) {
            return resource2;
        }
        byte[] bytes = FileCopyUtils.copyToByteArray(resource2.getInputStream());
        String content = new String(bytes, DEFAULT_CHARSET);
        if (!content.startsWith(MANIFEST_HEADER)) {
            if (logger.isTraceEnabled()) {
                logger.trace("AppCache manifest does not start with 'CACHE MANIFEST', skipping: " + resource2);
            }
            return resource2;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Transforming resource: " + resource2);
        }
        StringWriter contentWriter = new StringWriter();
        HashBuilder hashBuilder = new HashBuilder(content.length());
        Scanner scanner = new Scanner(content);
        SectionTransformer currentTransformer = this.sectionTransformers.get(MANIFEST_HEADER);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (this.sectionTransformers.containsKey(line.trim())) {
                currentTransformer = this.sectionTransformers.get(line.trim());
                contentWriter.write(line + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                hashBuilder.appendString(line);
            } else {
                contentWriter.write(currentTransformer.transform(line, hashBuilder, resource2, transformerChain, request) + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        String hash = hashBuilder.build();
        contentWriter.write("\n# Hash: " + hash);
        if (logger.isTraceEnabled()) {
            logger.trace("AppCache file: [" + resource2.getFilename() + "] hash: [" + hash + "]");
        }
        return new TransformedResource(resource2, contentWriter.toString().getBytes(DEFAULT_CHARSET));
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AppCacheManifestTransformer$NoOpSection.class */
    private static class NoOpSection implements SectionTransformer {
        private NoOpSection() {
        }

        @Override // org.springframework.web.servlet.resource.AppCacheManifestTransformer.SectionTransformer
        public String transform(String line, HashBuilder builder, Resource resource, ResourceTransformerChain transformerChain, HttpServletRequest request) throws IOException {
            builder.appendString(line);
            return line;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AppCacheManifestTransformer$CacheSection.class */
    private class CacheSection implements SectionTransformer {
        private static final String COMMENT_DIRECTIVE = "#";

        private CacheSection() {
        }

        @Override // org.springframework.web.servlet.resource.AppCacheManifestTransformer.SectionTransformer
        public String transform(String line, HashBuilder builder, Resource resource, ResourceTransformerChain transformerChain, HttpServletRequest request) throws IOException {
            if (isLink(line) && !hasScheme(line)) {
                ResourceResolverChain resolverChain = transformerChain.getResolverChain();
                Resource appCacheResource = resolverChain.resolveResource(null, line, Collections.singletonList(resource));
                String path = AppCacheManifestTransformer.this.resolveUrlPath(line, request, resource, transformerChain);
                builder.appendResource(appCacheResource);
                if (AppCacheManifestTransformer.logger.isTraceEnabled()) {
                    AppCacheManifestTransformer.logger.trace("Link modified: " + path + " (original: " + line + ")");
                }
                return path;
            }
            builder.appendString(line);
            return line;
        }

        private boolean hasScheme(String link) {
            int schemeIndex = link.indexOf(58);
            return link.startsWith("//") || (schemeIndex > 0 && !link.substring(0, schemeIndex).contains("/"));
        }

        private boolean isLink(String line) {
            return StringUtils.hasText(line) && !line.startsWith("#");
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/AppCacheManifestTransformer$HashBuilder.class */
    private static class HashBuilder {
        private final ByteArrayOutputStream baos;

        public HashBuilder(int initialSize) {
            this.baos = new ByteArrayOutputStream(initialSize);
        }

        public void appendResource(Resource resource) throws IOException {
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            this.baos.write(DigestUtils.md5Digest(content));
        }

        public void appendString(String content) throws IOException {
            this.baos.write(content.getBytes(AppCacheManifestTransformer.DEFAULT_CHARSET));
        }

        public String build() {
            return DigestUtils.md5DigestAsHex(this.baos.toByteArray());
        }
    }
}
