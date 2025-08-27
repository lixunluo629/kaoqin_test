package org.springframework.web.servlet.resource;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer.class */
public class CssLinkResourceTransformer extends ResourceTransformerSupport {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Log logger = LogFactory.getLog(CssLinkResourceTransformer.class);
    private final List<CssLinkParser> linkParsers = new ArrayList(2);

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer$CssLinkParser.class */
    protected interface CssLinkParser {
        void parseLink(String str, Set<CssLinkInfo> set);
    }

    public CssLinkResourceTransformer() {
        this.linkParsers.add(new ImportStatementCssLinkParser());
        this.linkParsers.add(new UrlFunctionCssLinkParser());
    }

    @Override // org.springframework.web.servlet.resource.ResourceTransformer
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        Resource resource2 = transformerChain.transform(request, resource);
        String filename = resource2.getFilename();
        if (!"css".equals(StringUtils.getFilenameExtension(filename))) {
            return resource2;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Transforming resource: " + resource2);
        }
        byte[] bytes = FileCopyUtils.copyToByteArray(resource2.getInputStream());
        String content = new String(bytes, DEFAULT_CHARSET);
        Set<CssLinkInfo> infos = new HashSet<>(8);
        for (CssLinkParser parser : this.linkParsers) {
            parser.parseLink(content, infos);
        }
        if (infos.isEmpty()) {
            if (logger.isTraceEnabled()) {
                logger.trace("No links found.");
            }
            return resource2;
        }
        List<CssLinkInfo> sortedInfos = new ArrayList<>(infos);
        Collections.sort(sortedInfos);
        int index = 0;
        StringWriter writer = new StringWriter();
        for (CssLinkInfo info : sortedInfos) {
            writer.write(content.substring(index, info.getStart()));
            String link = content.substring(info.getStart(), info.getEnd());
            String newLink = null;
            if (!hasScheme(link)) {
                newLink = resolveUrlPath(link, request, resource2, transformerChain);
            }
            if (logger.isTraceEnabled()) {
                if (newLink != null && !link.equals(newLink)) {
                    logger.trace("Link modified: " + newLink + " (original: " + link + ")");
                } else {
                    logger.trace("Link not modified: " + link);
                }
            }
            writer.write(newLink != null ? newLink : link);
            index = info.getEnd();
        }
        writer.write(content.substring(index));
        return new TransformedResource(resource2, writer.toString().getBytes(DEFAULT_CHARSET));
    }

    private boolean hasScheme(String link) {
        int schemeIndex = link.indexOf(58);
        return (schemeIndex > 0 && !link.substring(0, schemeIndex).contains("/")) || link.indexOf("//") == 0;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer$AbstractCssLinkParser.class */
    protected static abstract class AbstractCssLinkParser implements CssLinkParser {
        protected abstract String getKeyword();

        protected abstract int extractLink(int i, String str, Set<CssLinkInfo> set);

        protected AbstractCssLinkParser() {
        }

        @Override // org.springframework.web.servlet.resource.CssLinkResourceTransformer.CssLinkParser
        public void parseLink(String content, Set<CssLinkInfo> linkInfos) {
            int index = 0;
            while (true) {
                int index2 = content.indexOf(getKeyword(), index);
                if (index2 != -1) {
                    int index3 = skipWhitespace(content, index2 + getKeyword().length());
                    if (content.charAt(index3) == '\'') {
                        index = addLink(index3, "'", content, linkInfos);
                    } else if (content.charAt(index3) == '\"') {
                        index = addLink(index3, SymbolConstants.QUOTES_SYMBOL, content, linkInfos);
                    } else {
                        index = extractLink(index3, content, linkInfos);
                    }
                } else {
                    return;
                }
            }
        }

        private int skipWhitespace(String content, int index) {
            while (Character.isWhitespace(content.charAt(index))) {
                index++;
            }
            return index;
        }

        protected int addLink(int index, String endKey, String content, Set<CssLinkInfo> linkInfos) {
            int start = index + 1;
            int end = content.indexOf(endKey, start);
            linkInfos.add(new CssLinkInfo(start, end));
            return end + endKey.length();
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer$ImportStatementCssLinkParser.class */
    private static class ImportStatementCssLinkParser extends AbstractCssLinkParser {
        private ImportStatementCssLinkParser() {
        }

        @Override // org.springframework.web.servlet.resource.CssLinkResourceTransformer.AbstractCssLinkParser
        protected String getKeyword() {
            return "@import";
        }

        @Override // org.springframework.web.servlet.resource.CssLinkResourceTransformer.AbstractCssLinkParser
        protected int extractLink(int index, String content, Set<CssLinkInfo> linkInfos) {
            if (!content.substring(index, index + 4).equals("url(") && CssLinkResourceTransformer.logger.isErrorEnabled()) {
                CssLinkResourceTransformer.logger.error("Unexpected syntax for @import link at index " + index);
            }
            return index;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer$UrlFunctionCssLinkParser.class */
    private static class UrlFunctionCssLinkParser extends AbstractCssLinkParser {
        private UrlFunctionCssLinkParser() {
        }

        @Override // org.springframework.web.servlet.resource.CssLinkResourceTransformer.AbstractCssLinkParser
        protected String getKeyword() {
            return "url(";
        }

        @Override // org.springframework.web.servlet.resource.CssLinkResourceTransformer.AbstractCssLinkParser
        protected int extractLink(int index, String content, Set<CssLinkInfo> linkInfos) {
            return addLink(index - 1, ")", content, linkInfos);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/resource/CssLinkResourceTransformer$CssLinkInfo.class */
    private static class CssLinkInfo implements Comparable<CssLinkInfo> {
        private final int start;
        private final int end;

        public CssLinkInfo(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        @Override // java.lang.Comparable
        public int compareTo(CssLinkInfo other) {
            if (this.start < other.start) {
                return -1;
            }
            return this.start == other.start ? 0 : 1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && (obj instanceof CssLinkInfo)) {
                CssLinkInfo other = (CssLinkInfo) obj;
                return this.start == other.start && this.end == other.end;
            }
            return false;
        }

        public int hashCode() {
            return (this.start * 31) + this.end;
        }
    }
}
