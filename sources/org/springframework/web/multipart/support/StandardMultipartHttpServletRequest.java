package org.springframework.web.multipart.support;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/support/StandardMultipartHttpServletRequest.class */
public class StandardMultipartHttpServletRequest extends AbstractMultipartHttpServletRequest {
    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String FILENAME_KEY = "filename=";
    private static final String FILENAME_WITH_CHARSET_KEY = "filename*=";
    private static final Charset US_ASCII = Charset.forName("us-ascii");
    private Set<String> multipartParameterNames;

    public StandardMultipartHttpServletRequest(HttpServletRequest request) throws MultipartException {
        this(request, false);
    }

    public StandardMultipartHttpServletRequest(HttpServletRequest request, boolean lazyParsing) throws MultipartException {
        super(request);
        if (!lazyParsing) {
            parseRequest(request);
        }
    }

    private void parseRequest(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            this.multipartParameterNames = new LinkedHashSet(parts.size());
            MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<>(parts.size());
            for (Part part : parts) {
                String disposition = part.getHeader(CONTENT_DISPOSITION);
                String filename = extractFilename(disposition);
                if (filename == null) {
                    filename = extractFilenameWithCharset(disposition);
                }
                if (filename != null) {
                    files.add(part.getName(), new StandardMultipartFile(part, filename));
                } else {
                    this.multipartParameterNames.add(part.getName());
                }
            }
            setMultipartFiles(files);
        } catch (Throwable ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

    private String extractFilename(String contentDisposition, String key) {
        int startIndex;
        if (contentDisposition == null || (startIndex = contentDisposition.indexOf(key)) == -1) {
            return null;
        }
        String filename = contentDisposition.substring(startIndex + key.length());
        if (filename.startsWith(SymbolConstants.QUOTES_SYMBOL)) {
            int endIndex = filename.indexOf(SymbolConstants.QUOTES_SYMBOL, 1);
            if (endIndex != -1) {
                return filename.substring(1, endIndex);
            }
        } else {
            int endIndex2 = filename.indexOf(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            if (endIndex2 != -1) {
                return filename.substring(0, endIndex2);
            }
        }
        return filename;
    }

    private String extractFilename(String contentDisposition) {
        return extractFilename(contentDisposition, FILENAME_KEY);
    }

    private String extractFilenameWithCharset(String contentDisposition) {
        String filename = extractFilename(contentDisposition, FILENAME_WITH_CHARSET_KEY);
        if (filename == null) {
            return null;
        }
        int index = filename.indexOf("'");
        if (index != -1) {
            Charset charset = null;
            try {
                charset = Charset.forName(filename.substring(0, index));
            } catch (IllegalArgumentException e) {
            }
            filename = filename.substring(index + 1);
            int index2 = filename.indexOf("'");
            if (index2 != -1) {
                filename = filename.substring(index2 + 1);
            }
            if (charset != null) {
                filename = new String(filename.getBytes(US_ASCII), charset);
            }
        }
        return filename;
    }

    @Override // org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest
    protected void initializeMultipart() {
        parseRequest(getRequest());
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Enumeration<String> getParameterNames() {
        if (this.multipartParameterNames == null) {
            initializeMultipart();
        }
        if (this.multipartParameterNames.isEmpty()) {
            return super.getParameterNames();
        }
        Set<String> paramNames = new LinkedHashSet<>();
        Enumeration<String> paramEnum = super.getParameterNames();
        while (paramEnum.hasMoreElements()) {
            paramNames.add(paramEnum.nextElement());
        }
        paramNames.addAll(this.multipartParameterNames);
        return Collections.enumeration(paramNames);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Map<String, String[]> getParameterMap() {
        if (this.multipartParameterNames == null) {
            initializeMultipart();
        }
        if (this.multipartParameterNames.isEmpty()) {
            return super.getParameterMap();
        }
        Map<String, String[]> paramMap = new LinkedHashMap<>();
        paramMap.putAll(super.getParameterMap());
        for (String paramName : this.multipartParameterNames) {
            if (!paramMap.containsKey(paramName)) {
                paramMap.put(paramName, getParameterValues(paramName));
            }
        }
        return paramMap;
    }

    @Override // org.springframework.web.multipart.MultipartRequest
    public String getMultipartContentType(String paramOrFileName) {
        try {
            Part part = getPart(paramOrFileName);
            if (part != null) {
                return part.getContentType();
            }
            return null;
        } catch (Throwable ex) {
            throw new MultipartException("Could not access multipart servlet request", ex);
        }
    }

    @Override // org.springframework.web.multipart.MultipartHttpServletRequest
    public HttpHeaders getMultipartHeaders(String paramOrFileName) {
        try {
            Part part = getPart(paramOrFileName);
            if (part != null) {
                HttpHeaders headers = new HttpHeaders();
                for (String headerName : part.getHeaderNames()) {
                    headers.put(headerName, (List<String>) new ArrayList(part.getHeaders(headerName)));
                }
                return headers;
            }
            return null;
        } catch (Throwable ex) {
            throw new MultipartException("Could not access multipart servlet request", ex);
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/support/StandardMultipartHttpServletRequest$StandardMultipartFile.class */
    private static class StandardMultipartFile implements MultipartFile, Serializable {
        private final Part part;
        private final String filename;

        public StandardMultipartFile(Part part, String filename) {
            this.part = part;
            this.filename = filename;
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public String getName() {
            return this.part.getName();
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public String getOriginalFilename() {
            return this.filename;
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public String getContentType() {
            return this.part.getContentType();
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public boolean isEmpty() {
            return this.part.getSize() == 0;
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public long getSize() {
            return this.part.getSize();
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public byte[] getBytes() throws IOException {
            return FileCopyUtils.copyToByteArray(this.part.getInputStream());
        }

        @Override // org.springframework.web.multipart.MultipartFile, org.springframework.core.io.InputStreamSource
        public InputStream getInputStream() throws IOException {
            return this.part.getInputStream();
        }

        @Override // org.springframework.web.multipart.MultipartFile
        public void transferTo(File dest) throws IllegalStateException, IOException {
            this.part.write(dest.getPath());
            if (dest.isAbsolute() && !dest.exists()) {
                FileCopyUtils.copy(this.part.getInputStream(), new FileOutputStream(dest));
            }
        }
    }
}
