package org.apache.catalina.servlets;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.catalina.util.IOTools;
import org.apache.catalina.util.ServerInfo;
import org.apache.catalina.util.TomcatCSS;
import org.apache.catalina.util.URLEncoder;
import org.apache.catalina.webresources.CachedResource;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.tomcat.util.buf.B2CConverter;
import org.apache.tomcat.util.http.ResponseUtil;
import org.apache.tomcat.util.http.parser.ContentRange;
import org.apache.tomcat.util.http.parser.Ranges;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.security.Escape;
import org.apache.tomcat.util.security.PrivilegedGetTccl;
import org.apache.tomcat.util.security.PrivilegedSetTccl;
import org.bouncycastle.i18n.TextBundle;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet.class */
public class DefaultServlet extends HttpServlet {
    private static final long serialVersionUID = 1;
    private static final DocumentBuilderFactory factory;
    private static final SecureEntityResolver secureEntityResolver;
    protected static final String mimeSeparation = "CATALINA_MIME_BOUNDARY";

    @Deprecated
    protected static final String RESOURCES_JNDI_NAME = "java:/comp/Resources";
    protected static final int BUFFER_SIZE = 4096;
    protected CompressionFormat[] compressionFormats;
    protected transient SortManager sortManager;
    protected static final StringManager sm = StringManager.getManager(Constants.Package);
    protected static final ArrayList<Range> FULL = new ArrayList<>();
    private static final Range IGNORE = new Range();
    protected int debug = 0;
    protected int input = 2048;
    protected boolean listings = false;
    protected boolean readOnly = true;
    protected int output = 2048;
    protected String localXsltFile = null;
    protected String contextXsltFile = null;
    protected String globalXsltFile = null;
    protected String readmeFile = null;
    protected transient WebResourceRoot resources = null;
    protected String fileEncoding = null;
    private transient Charset fileEncodingCharset = null;
    private boolean useBomIfPresent = true;
    protected int sendfileSize = CpioConstants.C_ISSOCK;
    protected boolean useAcceptRanges = true;
    protected boolean showServerInfo = true;
    protected boolean sortListings = false;
    private boolean allowPartialPut = true;

    static {
        if (Globals.IS_SECURITY_ENABLED) {
            factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            secureEntityResolver = new SecureEntityResolver();
            return;
        }
        factory = null;
        secureEntityResolver = null;
    }

    @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
    public void destroy() {
    }

    @Override // javax.servlet.GenericServlet
    public void init() throws ServletException {
        boolean sortDirectoriesFirst;
        if (getServletConfig().getInitParameter("debug") != null) {
            this.debug = Integer.parseInt(getServletConfig().getInitParameter("debug"));
        }
        if (getServletConfig().getInitParameter("input") != null) {
            this.input = Integer.parseInt(getServletConfig().getInitParameter("input"));
        }
        if (getServletConfig().getInitParameter("output") != null) {
            this.output = Integer.parseInt(getServletConfig().getInitParameter("output"));
        }
        this.listings = Boolean.parseBoolean(getServletConfig().getInitParameter("listings"));
        if (getServletConfig().getInitParameter("readonly") != null) {
            this.readOnly = Boolean.parseBoolean(getServletConfig().getInitParameter("readonly"));
        }
        this.compressionFormats = parseCompressionFormats(getServletConfig().getInitParameter("precompressed"), getServletConfig().getInitParameter("gzip"));
        if (getServletConfig().getInitParameter("sendfileSize") != null) {
            this.sendfileSize = Integer.parseInt(getServletConfig().getInitParameter("sendfileSize")) * 1024;
        }
        this.fileEncoding = getServletConfig().getInitParameter("fileEncoding");
        if (this.fileEncoding == null) {
            this.fileEncodingCharset = Charset.defaultCharset();
            this.fileEncoding = this.fileEncodingCharset.name();
        } else {
            try {
                this.fileEncodingCharset = B2CConverter.getCharset(this.fileEncoding);
            } catch (UnsupportedEncodingException e) {
                throw new ServletException(e);
            }
        }
        if (getServletConfig().getInitParameter("useBomIfPresent") != null) {
            this.useBomIfPresent = Boolean.parseBoolean(getServletConfig().getInitParameter("useBomIfPresent"));
        }
        this.globalXsltFile = getServletConfig().getInitParameter("globalXsltFile");
        this.contextXsltFile = getServletConfig().getInitParameter("contextXsltFile");
        this.localXsltFile = getServletConfig().getInitParameter("localXsltFile");
        this.readmeFile = getServletConfig().getInitParameter("readmeFile");
        if (getServletConfig().getInitParameter("useAcceptRanges") != null) {
            this.useAcceptRanges = Boolean.parseBoolean(getServletConfig().getInitParameter("useAcceptRanges"));
        }
        if (this.input < 256) {
            this.input = 256;
        }
        if (this.output < 256) {
            this.output = 256;
        }
        if (this.debug > 0) {
            log("DefaultServlet.init:  input buffer size=" + this.input + ", output buffer size=" + this.output);
        }
        this.resources = (WebResourceRoot) getServletContext().getAttribute(Globals.RESOURCES_ATTR);
        if (this.resources == null) {
            throw new UnavailableException(sm.getString("defaultServlet.noResources"));
        }
        if (getServletConfig().getInitParameter("showServerInfo") != null) {
            this.showServerInfo = Boolean.parseBoolean(getServletConfig().getInitParameter("showServerInfo"));
        }
        if (getServletConfig().getInitParameter("sortListings") != null) {
            this.sortListings = Boolean.parseBoolean(getServletConfig().getInitParameter("sortListings"));
            if (this.sortListings) {
                if (getServletConfig().getInitParameter("sortDirectoriesFirst") != null) {
                    sortDirectoriesFirst = Boolean.parseBoolean(getServletConfig().getInitParameter("sortDirectoriesFirst"));
                } else {
                    sortDirectoriesFirst = false;
                }
                this.sortManager = new SortManager(sortDirectoriesFirst);
            }
        }
        if (getServletConfig().getInitParameter("allowPartialPut") != null) {
            this.allowPartialPut = Boolean.parseBoolean(getServletConfig().getInitParameter("allowPartialPut"));
        }
    }

    private CompressionFormat[] parseCompressionFormats(String precompressed, String gzip) {
        List<CompressionFormat> ret = new ArrayList<>();
        if (precompressed != null && precompressed.indexOf(61) > 0) {
            String[] arr$ = precompressed.split(",");
            for (String pair : arr$) {
                String[] setting = pair.split(SymbolConstants.EQUAL_SYMBOL);
                String encoding = setting[0];
                String extension = setting[1];
                ret.add(new CompressionFormat(extension, encoding));
            }
        } else if (precompressed != null) {
            if (Boolean.parseBoolean(precompressed)) {
                ret.add(new CompressionFormat(".br", CompressorStreamFactory.BROTLI));
                ret.add(new CompressionFormat(".gz", "gzip"));
            }
        } else if (Boolean.parseBoolean(gzip)) {
            ret.add(new CompressionFormat(".gz", "gzip"));
        }
        return (CompressionFormat[]) ret.toArray(new CompressionFormat[ret.size()]);
    }

    protected String getRelativePath(HttpServletRequest request) {
        return getRelativePath(request, false);
    }

    protected String getRelativePath(HttpServletRequest request, boolean allowEmptyPath) {
        String pathInfo;
        String servletPath;
        if (request.getAttribute("javax.servlet.include.request_uri") != null) {
            pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
            servletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");
        } else {
            pathInfo = request.getPathInfo();
            servletPath = request.getServletPath();
        }
        StringBuilder result = new StringBuilder();
        if (servletPath.length() > 0) {
            result.append(servletPath);
        }
        if (pathInfo != null) {
            result.append(pathInfo);
        }
        if (result.length() == 0 && !allowEmptyPath) {
            result.append('/');
        }
        return result.toString();
    }

    protected String getPathPrefix(HttpServletRequest request) {
        return request.getContextPath();
    }

    @Override // javax.servlet.http.HttpServlet
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, NoSuchMethodException, IOException, NumberFormatException, SecurityException, ClassNotFoundException {
        if (req.getDispatcherType() == DispatcherType.ERROR) {
            doGet(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
        serveResource(request, response, true, this.fileEncoding);
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
        boolean serveContent = DispatcherType.INCLUDE.equals(request.getDispatcherType());
        serveResource(request, response, serveContent, this.fileEncoding);
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Allow", determineMethodsAllowed(req));
    }

    protected String determineMethodsAllowed(HttpServletRequest req) {
        StringBuilder allow = new StringBuilder();
        allow.append("OPTIONS, GET, HEAD, POST");
        if (!this.readOnly) {
            allow.append(", PUT, DELETE");
        }
        if ((req instanceof RequestFacade) && ((RequestFacade) req).getAllowTrace()) {
            allow.append(", TRACE");
        }
        return allow.toString();
    }

    protected void sendNotAllowed(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Allow", determineMethodsAllowed(req));
        resp.sendError(405);
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
        doGet(request, response);
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream resourceInputStream;
        if (this.readOnly) {
            sendNotAllowed(req, resp);
            return;
        }
        String path = getRelativePath(req);
        WebResource resource = this.resources.getResource(path);
        Range range = parseContentRange(req, resp);
        if (range == null) {
            return;
        }
        InputStream resourceInputStream2 = null;
        try {
            if (range == IGNORE) {
                resourceInputStream = req.getInputStream();
            } else {
                File contentFile = executePartialPut(req, range, path);
                resourceInputStream = new FileInputStream(contentFile);
            }
            if (!this.resources.write(path, resourceInputStream, true)) {
                resp.sendError(409);
            } else if (resource.exists()) {
                resp.setStatus(204);
            } else {
                resp.setStatus(201);
            }
            if (resourceInputStream != null) {
                try {
                    resourceInputStream.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    resourceInputStream2.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    protected File executePartialPut(HttpServletRequest req, Range range, String path) throws IOException {
        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        String convertedResourcePath = path.replace('/', '.');
        File contentFile = new File(tempDir, convertedResourcePath);
        if (contentFile.createNewFile()) {
            contentFile.deleteOnExit();
        }
        RandomAccessFile randAccessContentFile = new RandomAccessFile(contentFile, "rw");
        Throwable th = null;
        try {
            WebResource oldResource = this.resources.getResource(path);
            if (oldResource.isFile()) {
                BufferedInputStream bufOldRevStream = new BufferedInputStream(oldResource.getInputStream(), 4096);
                Throwable th2 = null;
                try {
                    byte[] copyBuffer = new byte[4096];
                    while (true) {
                        int numBytesRead = bufOldRevStream.read(copyBuffer);
                        if (numBytesRead == -1) {
                            break;
                        }
                        randAccessContentFile.write(copyBuffer, 0, numBytesRead);
                    }
                    if (bufOldRevStream != null) {
                        if (0 != 0) {
                            try {
                                bufOldRevStream.close();
                            } catch (Throwable x2) {
                                th2.addSuppressed(x2);
                            }
                        } else {
                            bufOldRevStream.close();
                        }
                    }
                } catch (Throwable th3) {
                    if (bufOldRevStream != null) {
                        if (0 != 0) {
                            try {
                                bufOldRevStream.close();
                            } catch (Throwable x22) {
                                th2.addSuppressed(x22);
                            }
                        } else {
                            bufOldRevStream.close();
                        }
                    }
                    throw th3;
                }
            }
            randAccessContentFile.setLength(range.length);
            randAccessContentFile.seek(range.start);
            byte[] transferBuffer = new byte[4096];
            BufferedInputStream requestBufInStream = new BufferedInputStream(req.getInputStream(), 4096);
            Throwable th4 = null;
            while (true) {
                try {
                    try {
                        int numBytesRead2 = requestBufInStream.read(transferBuffer);
                        if (numBytesRead2 == -1) {
                            break;
                        }
                        randAccessContentFile.write(transferBuffer, 0, numBytesRead2);
                    } finally {
                    }
                } catch (Throwable th5) {
                    if (requestBufInStream != null) {
                        if (th4 != null) {
                            try {
                                requestBufInStream.close();
                            } catch (Throwable x23) {
                                th4.addSuppressed(x23);
                            }
                        } else {
                            requestBufInStream.close();
                        }
                    }
                    throw th5;
                }
            }
            if (requestBufInStream != null) {
                if (0 != 0) {
                    try {
                        requestBufInStream.close();
                    } catch (Throwable x24) {
                        th4.addSuppressed(x24);
                    }
                } else {
                    requestBufInStream.close();
                }
            }
            return contentFile;
        } finally {
            if (randAccessContentFile != null) {
                if (0 != 0) {
                    try {
                        randAccessContentFile.close();
                    } catch (Throwable x25) {
                        th.addSuppressed(x25);
                    }
                } else {
                    randAccessContentFile.close();
                }
            }
        }
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (this.readOnly) {
            sendNotAllowed(req, resp);
            return;
        }
        String path = getRelativePath(req);
        WebResource resource = this.resources.getResource(path);
        if (resource.exists()) {
            if (resource.delete()) {
                resp.setStatus(204);
                return;
            } else {
                resp.sendError(405);
                return;
            }
        }
        resp.sendError(404);
    }

    protected boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response, WebResource resource) throws IOException {
        return checkIfMatch(request, response, resource) && checkIfModifiedSince(request, response, resource) && checkIfNoneMatch(request, response, resource) && checkIfUnmodifiedSince(request, response, resource);
    }

    protected String rewriteUrl(String path) {
        return URLEncoder.DEFAULT.encode(path, StandardCharsets.UTF_8);
    }

    protected void serveResource(HttpServletRequest request, HttpServletResponse response, boolean content, String inputEncoding) throws ServletException, IOException, NumberFormatException {
        boolean conversionRequired;
        InputStream renderResult;
        boolean serveContent = content;
        String path = getRelativePath(request, true);
        if (this.debug > 0) {
            if (serveContent) {
                log("DefaultServlet.serveResource:  Serving resource '" + path + "' headers and data");
            } else {
                log("DefaultServlet.serveResource:  Serving resource '" + path + "' headers only");
            }
        }
        if (path.length() == 0) {
            doDirectoryRedirect(request, response);
            return;
        }
        WebResource resource = this.resources.getResource(path);
        boolean isError = DispatcherType.ERROR == request.getDispatcherType();
        if (!resource.exists()) {
            String requestUri = (String) request.getAttribute("javax.servlet.include.request_uri");
            if (requestUri == null) {
                String requestUri2 = request.getRequestURI();
                if (isError) {
                    response.sendError(((Integer) request.getAttribute("javax.servlet.error.status_code")).intValue());
                    return;
                } else {
                    response.sendError(404, requestUri2);
                    return;
                }
            }
            throw new FileNotFoundException(sm.getString("defaultServlet.missingResource", requestUri));
        }
        if (!resource.canRead()) {
            String requestUri3 = (String) request.getAttribute("javax.servlet.include.request_uri");
            if (requestUri3 == null) {
                String requestUri4 = request.getRequestURI();
                if (isError) {
                    response.sendError(((Integer) request.getAttribute("javax.servlet.error.status_code")).intValue());
                    return;
                } else {
                    response.sendError(403, requestUri4);
                    return;
                }
            }
            throw new FileNotFoundException(sm.getString("defaultServlet.missingResource", requestUri3));
        }
        boolean included = false;
        if (resource.isFile()) {
            included = request.getAttribute("javax.servlet.include.context_path") != null;
            if (!included && !isError && !checkIfHeaders(request, response, resource)) {
                return;
            }
        }
        String contentType = resource.getMimeType();
        if (contentType == null) {
            contentType = getServletContext().getMimeType(resource.getName());
            resource.setMimeType(contentType);
        }
        String eTag = null;
        String lastModifiedHttp = null;
        if (resource.isFile() && !isError) {
            eTag = resource.getETag();
            lastModifiedHttp = resource.getLastModifiedHttp();
        }
        boolean usingPrecompressedVersion = false;
        if (this.compressionFormats.length > 0 && !included && resource.isFile() && !pathEndsWithCompressedExtension(path)) {
            List<PrecompressedResource> precompressedResources = getAvailablePrecompressedResources(path);
            if (!precompressedResources.isEmpty()) {
                ResponseUtil.addVaryFieldName(response, "accept-encoding");
                PrecompressedResource bestResource = getBestPrecompressedResource(request, precompressedResources);
                if (bestResource != null) {
                    response.addHeader("Content-Encoding", bestResource.format.encoding);
                    resource = bestResource.resource;
                    usingPrecompressedVersion = true;
                }
            }
        }
        ArrayList<Range> ranges = FULL;
        long contentLength = -1;
        if (resource.isDirectory()) {
            if (!path.endsWith("/")) {
                doDirectoryRedirect(request, response);
                return;
            } else {
                if (!this.listings) {
                    response.sendError(404, request.getRequestURI());
                    return;
                }
                contentType = "text/html;charset=UTF-8";
            }
        } else {
            if (!isError) {
                if (this.useAcceptRanges) {
                    response.setHeader("Accept-Ranges", "bytes");
                }
                ranges = parseRange(request, response, resource);
                if (ranges == null) {
                    return;
                }
                response.setHeader("ETag", eTag);
                response.setHeader("Last-Modified", lastModifiedHttp);
            }
            contentLength = resource.getContentLength();
            if (contentLength == 0) {
                serveContent = false;
            }
        }
        ServletOutputStream ostream = null;
        PrintWriter writer = null;
        if (serveContent) {
            try {
                ostream = response.getOutputStream();
            } catch (IllegalStateException e) {
                if (!usingPrecompressedVersion && isText(contentType)) {
                    writer = response.getWriter();
                    ranges = FULL;
                } else {
                    throw e;
                }
            }
        }
        ServletResponse r = response;
        long contentWritten = 0;
        while (r instanceof ServletResponseWrapper) {
            r = ((ServletResponseWrapper) r).getResponse();
        }
        if (r instanceof ResponseFacade) {
            contentWritten = ((ResponseFacade) r).getContentWritten();
        }
        if (contentWritten > 0) {
            ranges = FULL;
        }
        String outputEncoding = response.getCharacterEncoding();
        Charset charset = B2CConverter.getCharset(outputEncoding);
        boolean outputEncodingSpecified = (outputEncoding == org.apache.coyote.Constants.DEFAULT_BODY_CHARSET.name() || outputEncoding == this.resources.getContext().getResponseCharacterEncoding()) ? false : true;
        if (!usingPrecompressedVersion && isText(contentType) && outputEncodingSpecified && !charset.equals(this.fileEncodingCharset)) {
            conversionRequired = true;
            ranges = FULL;
        } else {
            conversionRequired = false;
        }
        if (resource.isDirectory() || isError || ranges == FULL) {
            if (contentType != null) {
                if (this.debug > 0) {
                    log("DefaultServlet.serveFile:  contentType='" + contentType + "'");
                }
                if (response.getContentType() == null) {
                    response.setContentType(contentType);
                }
            }
            if (resource.isFile() && contentLength >= 0 && (!serveContent || ostream != null)) {
                if (this.debug > 0) {
                    log("DefaultServlet.serveFile:  contentLength=" + contentLength);
                }
                if (contentWritten == 0 && !conversionRequired) {
                    response.setContentLengthLong(contentLength);
                }
            }
            if (serveContent) {
                try {
                    response.setBufferSize(this.output);
                } catch (IllegalStateException e2) {
                }
                InputStream renderResult2 = null;
                if (ostream == null) {
                    if (resource.isDirectory()) {
                        renderResult = render(request, getPathPrefix(request), resource, inputEncoding);
                    } else {
                        renderResult = resource.getInputStream();
                        if (included) {
                            if (!renderResult.markSupported()) {
                                renderResult = new BufferedInputStream(renderResult);
                            }
                            Charset bomCharset = processBom(renderResult);
                            if (bomCharset != null && this.useBomIfPresent) {
                                inputEncoding = bomCharset.name();
                            }
                        }
                    }
                    copy(renderResult, writer, inputEncoding);
                    return;
                }
                if (resource.isDirectory()) {
                    renderResult2 = render(request, getPathPrefix(request), resource, inputEncoding);
                } else if (conversionRequired || included) {
                    InputStream source = resource.getInputStream();
                    if (!source.markSupported()) {
                        source = new BufferedInputStream(source);
                    }
                    Charset bomCharset2 = processBom(source);
                    if (bomCharset2 != null && this.useBomIfPresent) {
                        inputEncoding = bomCharset2.name();
                    }
                    if (outputEncodingSpecified) {
                        OutputStreamWriter osw = new OutputStreamWriter(ostream, charset);
                        PrintWriter pw = new PrintWriter(osw);
                        copy(source, pw, inputEncoding);
                        pw.flush();
                    } else {
                        renderResult2 = source;
                    }
                } else if (!checkSendfile(request, response, resource, contentLength, null)) {
                    byte[] resourceBody = null;
                    if (resource instanceof CachedResource) {
                        resourceBody = resource.getContent();
                    }
                    if (resourceBody == null) {
                        renderResult2 = resource.getInputStream();
                    } else {
                        ostream.write(resourceBody);
                    }
                }
                if (renderResult2 != null) {
                    copy(renderResult2, ostream);
                    return;
                }
                return;
            }
            return;
        }
        if (ranges == null || ranges.isEmpty()) {
            return;
        }
        response.setStatus(206);
        if (ranges.size() == 1) {
            Range range = ranges.get(0);
            response.addHeader("Content-Range", "bytes " + range.start + "-" + range.end + "/" + range.length);
            long length = (range.end - range.start) + serialVersionUID;
            response.setContentLengthLong(length);
            if (contentType != null) {
                if (this.debug > 0) {
                    log("DefaultServlet.serveFile:  contentType='" + contentType + "'");
                }
                response.setContentType(contentType);
            }
            if (serveContent) {
                try {
                    response.setBufferSize(this.output);
                } catch (IllegalStateException e3) {
                }
                if (ostream != null) {
                    if (!checkSendfile(request, response, resource, (range.end - range.start) + serialVersionUID, range)) {
                        copy(resource, ostream, range);
                        return;
                    }
                    return;
                }
                throw new IllegalStateException();
            }
            return;
        }
        response.setContentType("multipart/byteranges; boundary=CATALINA_MIME_BOUNDARY");
        if (serveContent) {
            try {
                response.setBufferSize(this.output);
            } catch (IllegalStateException e4) {
            }
            if (ostream != null) {
                copy(resource, ostream, ranges.iterator(), contentType);
                return;
            }
            throw new IllegalStateException();
        }
    }

    private static Charset processBom(InputStream is) throws IOException {
        byte[] bom = new byte[4];
        is.mark(bom.length);
        int count = is.read(bom);
        if (count < 2) {
            skip(is, 0);
            return null;
        }
        int b0 = bom[0] & 255;
        int b1 = bom[1] & 255;
        if (b0 == 254 && b1 == 255) {
            skip(is, 2);
            return StandardCharsets.UTF_16BE;
        }
        if (count == 2 && b0 == 255 && b1 == 254) {
            skip(is, 2);
            return StandardCharsets.UTF_16LE;
        }
        if (count < 3) {
            skip(is, 0);
            return null;
        }
        int b2 = bom[2] & 255;
        if (b0 == 239 && b1 == 187 && b2 == 191) {
            skip(is, 3);
            return StandardCharsets.UTF_8;
        }
        if (count < 4) {
            skip(is, 0);
            return null;
        }
        int b3 = bom[3] & 255;
        if (b0 == 0 && b1 == 0 && b2 == 254 && b3 == 255) {
            return Charset.forName("UTF-32BE");
        }
        if (b0 == 255 && b1 == 254 && b2 == 0 && b3 == 0) {
            return Charset.forName("UTF-32LE");
        }
        if (b0 == 255 && b1 == 254) {
            skip(is, 2);
            return StandardCharsets.UTF_16LE;
        }
        skip(is, 0);
        return null;
    }

    private static void skip(InputStream is, int skip) throws IOException {
        is.reset();
        while (true) {
            int i = skip;
            skip--;
            if (i > 0) {
                is.read();
            } else {
                return;
            }
        }
    }

    private static boolean isText(String contentType) {
        return contentType == null || contentType.startsWith(TextBundle.TEXT_ENTRY) || contentType.endsWith("xml") || contentType.contains("/javascript");
    }

    private boolean pathEndsWithCompressedExtension(String path) {
        CompressionFormat[] arr$ = this.compressionFormats;
        for (CompressionFormat format : arr$) {
            if (path.endsWith(format.extension)) {
                return true;
            }
        }
        return false;
    }

    private List<PrecompressedResource> getAvailablePrecompressedResources(String path) {
        List<PrecompressedResource> ret = new ArrayList<>(this.compressionFormats.length);
        CompressionFormat[] arr$ = this.compressionFormats;
        for (CompressionFormat format : arr$) {
            WebResource precompressedResource = this.resources.getResource(path + format.extension);
            if (precompressedResource.exists() && precompressedResource.isFile()) {
                ret.add(new PrecompressedResource(precompressedResource, format));
            }
        }
        return ret;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x012c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.catalina.servlets.DefaultServlet.PrecompressedResource getBestPrecompressedResource(javax.servlet.http.HttpServletRequest r6, java.util.List<org.apache.catalina.servlets.DefaultServlet.PrecompressedResource> r7) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 312
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.catalina.servlets.DefaultServlet.getBestPrecompressedResource(javax.servlet.http.HttpServletRequest, java.util.List):org.apache.catalina.servlets.DefaultServlet$PrecompressedResource");
    }

    private void doDirectoryRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder location = new StringBuilder(request.getRequestURI());
        location.append('/');
        if (request.getQueryString() != null) {
            location.append('?');
            location.append(request.getQueryString());
        }
        while (location.length() > 1 && location.charAt(1) == '/') {
            location.deleteCharAt(0);
        }
        response.sendRedirect(response.encodeRedirectURL(location.toString()));
    }

    protected Range parseContentRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentRangeHeader = request.getHeader("Content-Range");
        if (contentRangeHeader == null) {
            return IGNORE;
        }
        if (!this.allowPartialPut) {
            response.sendError(400);
            return null;
        }
        ContentRange contentRange = ContentRange.parse(new StringReader(contentRangeHeader));
        if (contentRange == null) {
            response.sendError(400);
            return null;
        }
        if (!contentRange.getUnits().equals("bytes")) {
            response.sendError(400);
            return null;
        }
        Range range = new Range();
        range.start = contentRange.getStart();
        range.end = contentRange.getEnd();
        range.length = contentRange.getLength();
        if (!range.validate()) {
            response.sendError(400);
            return null;
        }
        return range;
    }

    protected ArrayList<Range> parseRange(HttpServletRequest request, HttpServletResponse response, WebResource resource) throws IOException {
        String headerValue = request.getHeader("If-Range");
        if (headerValue != null) {
            long headerValueTime = -1;
            try {
                headerValueTime = request.getDateHeader("If-Range");
            } catch (IllegalArgumentException e) {
            }
            String eTag = resource.getETag();
            long lastModified = resource.getLastModified();
            if (headerValueTime == -1) {
                if (!eTag.equals(headerValue.trim())) {
                    return FULL;
                }
            } else if (Math.abs(lastModified - headerValueTime) > 1000) {
                return FULL;
            }
        }
        long fileLength = resource.getContentLength();
        if (fileLength == 0) {
            return FULL;
        }
        String rangeHeader = request.getHeader("Range");
        if (rangeHeader == null) {
            return FULL;
        }
        Ranges ranges = Ranges.parse(new StringReader(rangeHeader));
        if (ranges == null) {
            response.addHeader("Content-Range", "bytes */" + fileLength);
            response.sendError(416);
            return null;
        }
        if (!ranges.getUnits().equals("bytes")) {
            return FULL;
        }
        ArrayList<Range> result = new ArrayList<>();
        for (Ranges.Entry entry : ranges.getEntries()) {
            Range currentRange = new Range();
            if (entry.getStart() == -1) {
                currentRange.start = fileLength - entry.getEnd();
                if (currentRange.start < 0) {
                    currentRange.start = 0L;
                }
                currentRange.end = fileLength - serialVersionUID;
            } else if (entry.getEnd() == -1) {
                currentRange.start = entry.getStart();
                currentRange.end = fileLength - serialVersionUID;
            } else {
                currentRange.start = entry.getStart();
                currentRange.end = entry.getEnd();
            }
            currentRange.length = fileLength;
            if (!currentRange.validate()) {
                response.addHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(416);
                return null;
            }
            result.add(currentRange);
        }
        return result;
    }

    @Deprecated
    protected InputStream render(String contextPath, WebResource resource) throws ServletException, IOException {
        return render(contextPath, resource, null);
    }

    @Deprecated
    protected InputStream render(String contextPath, WebResource resource, String encoding) throws ServletException, IOException {
        return render(null, contextPath, resource, encoding);
    }

    protected InputStream render(HttpServletRequest request, String contextPath, WebResource resource, String encoding) throws ServletException, IOException {
        Source xsltSource = findXsltSource(resource);
        if (xsltSource == null) {
            return renderHtml(request, contextPath, resource, encoding);
        }
        return renderXml(request, contextPath, resource, xsltSource, encoding);
    }

    @Deprecated
    protected InputStream renderXml(String contextPath, WebResource resource, Source xsltSource) throws ServletException, IOException {
        return renderXml(contextPath, resource, xsltSource, null);
    }

    @Deprecated
    protected InputStream renderXml(String contextPath, WebResource resource, Source xsltSource, String encoding) throws ServletException, IOException {
        return renderXml(null, contextPath, resource, xsltSource, encoding);
    }

    protected InputStream renderXml(HttpServletRequest request, String contextPath, WebResource resource, Source xsltSource, String encoding) throws ServletException, IOException {
        ClassLoader original;
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("<listing ");
        sb.append(" contextPath='");
        sb.append(contextPath);
        sb.append("'");
        sb.append(" directory='");
        sb.append(resource.getName());
        sb.append("' ");
        sb.append(" hasParent='").append(!resource.getName().equals("/"));
        sb.append("'>");
        sb.append("<entries>");
        String[] entries = this.resources.list(resource.getWebappPath());
        String rewrittenContextPath = rewriteUrl(contextPath);
        String directoryWebappPath = resource.getWebappPath();
        for (String entry : entries) {
            if (!entry.equalsIgnoreCase("WEB-INF") && !entry.equalsIgnoreCase("META-INF") && !entry.equalsIgnoreCase(this.localXsltFile) && !(directoryWebappPath + entry).equals(this.contextXsltFile)) {
                WebResource childResource = this.resources.getResource(directoryWebappPath + entry);
                if (childResource.exists()) {
                    sb.append("<entry");
                    sb.append(" type='").append(childResource.isDirectory() ? AbstractHtmlElementTag.DIR_ATTRIBUTE : "file").append("'");
                    sb.append(" urlPath='").append(rewrittenContextPath).append(rewriteUrl(directoryWebappPath + entry)).append(childResource.isDirectory() ? "/" : "").append("'");
                    if (childResource.isFile()) {
                        sb.append(" size='").append(renderSize(childResource.getContentLength())).append("'");
                    }
                    sb.append(" date='").append(childResource.getLastModifiedHttp()).append("'");
                    sb.append(">");
                    sb.append(Escape.htmlElementContent(entry));
                    if (childResource.isDirectory()) {
                        sb.append("/");
                    }
                    sb.append("</entry>");
                }
            }
        }
        sb.append("</entries>");
        String readme = getReadme(resource, encoding);
        if (readme != null) {
            sb.append("<readme><![CDATA[");
            sb.append(readme);
            sb.append("]]></readme>");
        }
        sb.append("</listing>");
        if (Globals.IS_SECURITY_ENABLED) {
            PrivilegedGetTccl pa = new PrivilegedGetTccl();
            original = (ClassLoader) AccessController.doPrivileged(pa);
        } else {
            original = Thread.currentThread().getContextClassLoader();
        }
        try {
            try {
                if (Globals.IS_SECURITY_ENABLED) {
                    PrivilegedSetTccl pa2 = new PrivilegedSetTccl(DefaultServlet.class.getClassLoader());
                    AccessController.doPrivileged(pa2);
                } else {
                    Thread.currentThread().setContextClassLoader(DefaultServlet.class.getClassLoader());
                }
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Source xmlSource = new StreamSource(new StringReader(sb.toString()));
                Transformer transformer = tFactory.newTransformer(xsltSource);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
                StreamResult out = new StreamResult(osWriter);
                transformer.transform(xmlSource, out);
                osWriter.flush();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stream.toByteArray());
                if (Globals.IS_SECURITY_ENABLED) {
                    PrivilegedSetTccl pa3 = new PrivilegedSetTccl(original);
                    AccessController.doPrivileged(pa3);
                } else {
                    Thread.currentThread().setContextClassLoader(original);
                }
                return byteArrayInputStream;
            } catch (TransformerException e) {
                throw new ServletException(sm.getString("defaultServlet.xslError"), e);
            }
        } catch (Throwable th) {
            if (Globals.IS_SECURITY_ENABLED) {
                PrivilegedSetTccl pa4 = new PrivilegedSetTccl(original);
                AccessController.doPrivileged(pa4);
            } else {
                Thread.currentThread().setContextClassLoader(original);
            }
            throw th;
        }
    }

    @Deprecated
    protected InputStream renderHtml(String contextPath, WebResource resource) throws IOException {
        return renderHtml(contextPath, resource, null);
    }

    @Deprecated
    protected InputStream renderHtml(String contextPath, WebResource resource, String encoding) throws IOException {
        return renderHtml(null, contextPath, resource, encoding);
    }

    protected InputStream renderHtml(HttpServletRequest request, String contextPath, WebResource resource, String encoding) throws IOException {
        SortManager.Order order;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter osWriter = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        PrintWriter writer = new PrintWriter(osWriter);
        StringBuilder sb = new StringBuilder();
        String directoryWebappPath = resource.getWebappPath();
        WebResource[] entries = this.resources.listResources(directoryWebappPath);
        String rewrittenContextPath = rewriteUrl(contextPath);
        sb.append("<html>\r\n");
        sb.append("<head>\r\n");
        sb.append("<title>");
        sb.append(sm.getString("directory.title", directoryWebappPath));
        sb.append("</title>\r\n");
        sb.append("<STYLE><!--");
        sb.append(TomcatCSS.TOMCAT_CSS);
        sb.append("--></STYLE> ");
        sb.append("</head>\r\n");
        sb.append("<body>");
        sb.append("<h1>");
        sb.append(sm.getString("directory.title", directoryWebappPath));
        String parentDirectory = directoryWebappPath;
        if (parentDirectory.endsWith("/")) {
            parentDirectory = parentDirectory.substring(0, parentDirectory.length() - 1);
        }
        int slash = parentDirectory.lastIndexOf(47);
        if (slash >= 0) {
            String parent = directoryWebappPath.substring(0, slash);
            sb.append(" - <a href=\"");
            sb.append(rewrittenContextPath);
            if (parent.equals("")) {
                parent = "/";
            }
            sb.append(rewriteUrl(parent));
            if (!parent.endsWith("/")) {
                sb.append("/");
            }
            sb.append("\">");
            sb.append("<b>");
            sb.append(sm.getString("directory.parent", parent));
            sb.append("</b>");
            sb.append("</a>");
        }
        sb.append("</h1>");
        sb.append("<HR size=\"1\" noshade=\"noshade\">");
        sb.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\">\r\n");
        if (this.sortListings && null != request) {
            order = this.sortManager.getOrder(request.getQueryString());
        } else {
            order = null;
        }
        sb.append("<tr>\r\n");
        sb.append("<td align=\"left\"><font size=\"+1\"><strong>");
        if (this.sortListings && null != request) {
            sb.append("<a href=\"?C=N;O=");
            sb.append(getOrderChar(order, 'N'));
            sb.append("\">");
            sb.append(sm.getString("directory.filename"));
            sb.append("</a>");
        } else {
            sb.append(sm.getString("directory.filename"));
        }
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"center\"><font size=\"+1\"><strong>");
        if (this.sortListings && null != request) {
            sb.append("<a href=\"?C=S;O=");
            sb.append(getOrderChar(order, 'S'));
            sb.append("\">");
            sb.append(sm.getString("directory.size"));
            sb.append("</a>");
        } else {
            sb.append(sm.getString("directory.size"));
        }
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"right\"><font size=\"+1\"><strong>");
        if (this.sortListings && null != request) {
            sb.append("<a href=\"?C=M;O=");
            sb.append(getOrderChar(order, 'M'));
            sb.append("\">");
            sb.append(sm.getString("directory.lastModified"));
            sb.append("</a>");
        } else {
            sb.append(sm.getString("directory.lastModified"));
        }
        sb.append("</strong></font></td>\r\n");
        sb.append("</tr>");
        if (null != this.sortManager && null != request) {
            this.sortManager.sort(entries, request.getQueryString());
        }
        boolean shade = false;
        for (WebResource childResource : entries) {
            String filename = childResource.getName();
            if (!filename.equalsIgnoreCase("WEB-INF") && !filename.equalsIgnoreCase("META-INF") && childResource.exists()) {
                sb.append("<tr");
                if (shade) {
                    sb.append(" bgcolor=\"#eeeeee\"");
                }
                sb.append(">\r\n");
                shade = !shade;
                sb.append("<td align=\"left\">&nbsp;&nbsp;\r\n");
                sb.append("<a href=\"");
                sb.append(rewrittenContextPath);
                sb.append(rewriteUrl(childResource.getWebappPath()));
                if (childResource.isDirectory()) {
                    sb.append("/");
                }
                sb.append("\"><tt>");
                sb.append(Escape.htmlElementContent(filename));
                if (childResource.isDirectory()) {
                    sb.append("/");
                }
                sb.append("</tt></a></td>\r\n");
                sb.append("<td align=\"right\"><tt>");
                if (childResource.isDirectory()) {
                    sb.append("&nbsp;");
                } else {
                    sb.append(renderSize(childResource.getContentLength()));
                }
                sb.append("</tt></td>\r\n");
                sb.append("<td align=\"right\"><tt>");
                sb.append(childResource.getLastModifiedHttp());
                sb.append("</tt></td>\r\n");
                sb.append("</tr>\r\n");
            }
        }
        sb.append("</table>\r\n");
        sb.append("<HR size=\"1\" noshade=\"noshade\">");
        String readme = getReadme(resource, encoding);
        if (readme != null) {
            sb.append(readme);
            sb.append("<HR size=\"1\" noshade=\"noshade\">");
        }
        if (this.showServerInfo) {
            sb.append("<h3>").append(ServerInfo.getServerInfo()).append("</h3>");
        }
        sb.append("</body>\r\n");
        sb.append("</html>\r\n");
        writer.write(sb.toString());
        writer.flush();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    protected String renderSize(long size) {
        long leftSide = size / 1024;
        long rightSide = (size % 1024) / 103;
        if (leftSide == 0 && rightSide == 0 && size > 0) {
            rightSide = 1;
        }
        return "" + leftSide + "." + rightSide + " kb";
    }

    @Deprecated
    protected String getReadme(WebResource directory) {
        return getReadme(directory, null);
    }

    protected String getReadme(WebResource directory, String encoding) throws IOException {
        InputStream is;
        if (this.readmeFile == null) {
            return null;
        }
        WebResource resource = this.resources.getResource(directory.getWebappPath() + this.readmeFile);
        if (!resource.isFile()) {
            if (this.debug <= 10) {
                return null;
            }
            log("readme '" + this.readmeFile + "' not found");
            return null;
        }
        StringWriter buffer = new StringWriter();
        InputStreamReader reader = null;
        try {
            try {
                try {
                    is = resource.getInputStream();
                    Throwable th = null;
                    InputStreamReader reader2 = encoding != null ? new InputStreamReader(is, encoding) : new InputStreamReader(is);
                    copyRange(reader2, new PrintWriter(buffer));
                    if (is != null) {
                        if (0 != 0) {
                            try {
                                is.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            is.close();
                        }
                    }
                    if (reader2 != null) {
                        try {
                            reader2.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    log(sm.getString("defaultServlet.readerCloseFailed"), e2);
                    if (0 != 0) {
                        try {
                            reader.close();
                        } catch (IOException e3) {
                        }
                    }
                }
                return buffer.toString();
            } catch (Throwable th2) {
                if (is != null) {
                    if (th != null) {
                        try {
                            is.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th2;
            }
        } catch (Throwable th3) {
            if (0 != 0) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
            throw th3;
        }
    }

    protected Source findXsltSource(WebResource directory) throws IOException {
        File f;
        InputStream is;
        if (this.localXsltFile != null) {
            WebResource resource = this.resources.getResource(directory.getWebappPath() + this.localXsltFile);
            if (resource.isFile() && (is = resource.getInputStream()) != null) {
                if (Globals.IS_SECURITY_ENABLED) {
                    return secureXslt(is);
                }
                return new StreamSource(is);
            }
            if (this.debug > 10) {
                log("localXsltFile '" + this.localXsltFile + "' not found");
            }
        }
        if (this.contextXsltFile != null) {
            InputStream is2 = getServletContext().getResourceAsStream(this.contextXsltFile);
            if (is2 != null) {
                if (Globals.IS_SECURITY_ENABLED) {
                    return secureXslt(is2);
                }
                return new StreamSource(is2);
            }
            if (this.debug > 10) {
                log("contextXsltFile '" + this.contextXsltFile + "' not found");
            }
        }
        if (this.globalXsltFile != null && (f = validateGlobalXsltFile()) != null) {
            long globalXsltFileSize = f.length();
            if (globalXsltFileSize > 2147483647L) {
                log("globalXsltFile [" + f.getAbsolutePath() + "] is too big to buffer");
                return null;
            }
            FileInputStream fis = new FileInputStream(f);
            Throwable th = null;
            try {
                try {
                    byte[] b = new byte[(int) f.length()];
                    IOTools.readFully(fis, b);
                    StreamSource streamSource = new StreamSource(new ByteArrayInputStream(b));
                    if (fis != null) {
                        if (0 != 0) {
                            try {
                                fis.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            fis.close();
                        }
                    }
                    return streamSource;
                } catch (Throwable th2) {
                    th = th2;
                    throw th2;
                }
            } catch (Throwable th3) {
                if (fis != null) {
                    if (th != null) {
                        try {
                            fis.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    } else {
                        fis.close();
                    }
                }
                throw th3;
            }
        }
        return null;
    }

    private File validateGlobalXsltFile() {
        Context context = this.resources.getContext();
        File baseConf = new File(context.getCatalinaBase(), "conf");
        File result = validateGlobalXsltFile(baseConf);
        if (result == null) {
            File homeConf = new File(context.getCatalinaHome(), "conf");
            if (!baseConf.equals(homeConf)) {
                result = validateGlobalXsltFile(homeConf);
            }
        }
        return result;
    }

    private File validateGlobalXsltFile(File base) {
        File candidate = new File(this.globalXsltFile);
        if (!candidate.isAbsolute()) {
            candidate = new File(base, this.globalXsltFile);
        }
        if (!candidate.isFile()) {
            return null;
        }
        try {
            if (!candidate.getCanonicalPath().startsWith(base.getCanonicalPath())) {
                return null;
            }
            String nameLower = candidate.getName().toLowerCase(Locale.ENGLISH);
            if (!nameLower.endsWith(".xslt") && !nameLower.endsWith(".xsl")) {
                return null;
            }
            return candidate;
        } catch (IOException e) {
            return null;
        }
    }

    private Source secureXslt(InputStream is) throws IOException {
        Source result = null;
        try {
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setEntityResolver(secureEntityResolver);
                Document document = builder.parse(is);
                result = new DOMSource(document);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e2) {
                if (this.debug > 0) {
                    log(e2.getMessage(), e2);
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
            }
            return result;
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    protected boolean checkSendfile(HttpServletRequest request, HttpServletResponse response, WebResource resource, long length, Range range) {
        String canonicalPath;
        if (this.sendfileSize > 0 && length > this.sendfileSize && Boolean.TRUE.equals(request.getAttribute("org.apache.tomcat.sendfile.support")) && request.getClass().getName().equals("org.apache.catalina.connector.RequestFacade") && response.getClass().getName().equals("org.apache.catalina.connector.ResponseFacade") && resource.isFile() && (canonicalPath = resource.getCanonicalPath()) != null) {
            request.setAttribute("org.apache.tomcat.sendfile.filename", canonicalPath);
            if (range == null) {
                request.setAttribute("org.apache.tomcat.sendfile.start", 0L);
                request.setAttribute("org.apache.tomcat.sendfile.end", Long.valueOf(length));
                return true;
            }
            request.setAttribute("org.apache.tomcat.sendfile.start", Long.valueOf(range.start));
            request.setAttribute("org.apache.tomcat.sendfile.end", Long.valueOf(range.end + serialVersionUID));
            return true;
        }
        return false;
    }

    protected boolean checkIfMatch(HttpServletRequest request, HttpServletResponse response, WebResource resource) throws IOException {
        String eTag = resource.getETag();
        String headerValue = request.getHeader("If-Match");
        if (headerValue != null && headerValue.indexOf(42) == -1) {
            StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
            boolean conditionSatisfied = false;
            while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                String currentToken = commaTokenizer.nextToken();
                if (currentToken.trim().equals(eTag)) {
                    conditionSatisfied = true;
                }
            }
            if (!conditionSatisfied) {
                response.sendError(412);
                return false;
            }
            return true;
        }
        return true;
    }

    protected boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, WebResource resource) {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
            long lastModified = resource.getLastModified();
            if (headerValue != -1 && request.getHeader("If-None-Match") == null && lastModified < headerValue + 1000) {
                response.setStatus(304);
                response.setHeader("ETag", resource.getETag());
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    protected boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response, WebResource resource) throws IOException {
        String eTag = resource.getETag();
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!headerValue.equals("*")) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }
            if (conditionSatisfied) {
                if ("GET".equals(request.getMethod()) || WebContentGenerator.METHOD_HEAD.equals(request.getMethod())) {
                    response.setStatus(304);
                    response.setHeader("ETag", eTag);
                    return false;
                }
                response.sendError(412);
                return false;
            }
            return true;
        }
        return true;
    }

    protected boolean checkIfUnmodifiedSince(HttpServletRequest request, HttpServletResponse response, WebResource resource) throws IOException {
        try {
            long lastModified = resource.getLastModified();
            long headerValue = request.getDateHeader("If-Unmodified-Since");
            if (headerValue != -1 && lastModified >= headerValue + 1000) {
                response.sendError(412);
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Deprecated
    protected void copy(WebResource resource, InputStream is, ServletOutputStream ostream) throws IOException {
        copy(is, ostream);
    }

    protected void copy(InputStream is, ServletOutputStream ostream) throws IOException {
        InputStream istream = new BufferedInputStream(is, this.input);
        IOException exception = copyRange(istream, ostream);
        istream.close();
        if (exception != null) {
            throw exception;
        }
    }

    @Deprecated
    protected void copy(WebResource resource, InputStream is, PrintWriter writer, String encoding) throws IOException {
        copy(is, writer, encoding);
    }

    protected void copy(InputStream is, PrintWriter writer, String encoding) throws IOException {
        Reader reader;
        if (encoding == null) {
            reader = new InputStreamReader(is);
        } else {
            reader = new InputStreamReader(is, encoding);
        }
        IOException exception = copyRange(reader, writer);
        reader.close();
        if (exception != null) {
            throw exception;
        }
    }

    protected void copy(WebResource resource, ServletOutputStream ostream, Range range) throws IOException {
        InputStream resourceInputStream = resource.getInputStream();
        InputStream istream = new BufferedInputStream(resourceInputStream, this.input);
        IOException exception = copyRange(istream, ostream, range.start, range.end);
        istream.close();
        if (exception != null) {
            throw exception;
        }
    }

    protected void copy(WebResource resource, ServletOutputStream ostream, Iterator<Range> ranges, String contentType) throws IOException {
        IOException exception = null;
        while (exception == null && ranges.hasNext()) {
            InputStream resourceInputStream = resource.getInputStream();
            InputStream istream = new BufferedInputStream(resourceInputStream, this.input);
            Throwable th = null;
            try {
                try {
                    Range currentRange = ranges.next();
                    ostream.println();
                    ostream.println("--CATALINA_MIME_BOUNDARY");
                    if (contentType != null) {
                        ostream.println("Content-Type: " + contentType);
                    }
                    ostream.println("Content-Range: bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.length);
                    ostream.println();
                    exception = copyRange(istream, ostream, currentRange.start, currentRange.end);
                    if (istream != null) {
                        if (0 != 0) {
                            try {
                                istream.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            istream.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th2) {
                if (istream != null) {
                    if (th != null) {
                        try {
                            istream.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    } else {
                        istream.close();
                    }
                }
                throw th2;
            }
        }
        ostream.println();
        ostream.print("--CATALINA_MIME_BOUNDARY--");
        if (exception != null) {
            throw exception;
        }
    }

    protected IOException copyRange(InputStream istream, ServletOutputStream ostream) throws IOException {
        IOException exception = null;
        byte[] buffer = new byte[this.input];
        int length = buffer.length;
        while (true) {
            try {
                int len = istream.read(buffer);
                if (len == -1) {
                    break;
                }
                ostream.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
            }
        }
        return exception;
    }

    protected IOException copyRange(Reader reader, PrintWriter writer) throws IOException {
        IOException exception = null;
        char[] buffer = new char[this.input];
        int length = buffer.length;
        while (true) {
            try {
                int len = reader.read(buffer);
                if (len == -1) {
                    break;
                }
                writer.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
            }
        }
        return exception;
    }

    protected IOException copyRange(InputStream istream, ServletOutputStream ostream, long start, long end) throws IOException {
        if (this.debug > 10) {
            log("Serving bytes:" + start + "-" + end);
        }
        try {
            long skipped = istream.skip(start);
            if (skipped < start) {
                return new IOException(sm.getString("defaultServlet.skipfail", Long.valueOf(skipped), Long.valueOf(start)));
            }
            IOException exception = null;
            long bytesToRead = (end - start) + serialVersionUID;
            byte[] buffer = new byte[this.input];
            int len = buffer.length;
            while (bytesToRead > 0 && len >= buffer.length) {
                try {
                    len = istream.read(buffer);
                    if (bytesToRead >= len) {
                        ostream.write(buffer, 0, len);
                        bytesToRead -= len;
                    } else {
                        ostream.write(buffer, 0, (int) bytesToRead);
                        bytesToRead = 0;
                    }
                } catch (IOException e) {
                    exception = e;
                    len = -1;
                }
                if (len < buffer.length) {
                    break;
                }
            }
            return exception;
        } catch (IOException e2) {
            return e2;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$Range.class */
    protected static class Range {
        public long start;
        public long end;
        public long length;

        protected Range() {
        }

        public boolean validate() {
            if (this.end >= this.length) {
                this.end = this.length - DefaultServlet.serialVersionUID;
            }
            return this.start >= 0 && this.end >= 0 && this.start <= this.end && this.length > 0;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$CompressionFormat.class */
    protected static class CompressionFormat implements Serializable {
        private static final long serialVersionUID = 1;
        public final String extension;
        public final String encoding;

        public CompressionFormat(String extension, String encoding) {
            this.extension = extension;
            this.encoding = encoding;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$PrecompressedResource.class */
    private static class PrecompressedResource {
        public final WebResource resource;
        public final CompressionFormat format;

        private PrecompressedResource(WebResource resource, CompressionFormat format) {
            this.resource = resource;
            this.format = format;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$SecureEntityResolver.class */
    private static class SecureEntityResolver implements EntityResolver2 {
        private SecureEntityResolver() {
        }

        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalEntity", publicId, systemId));
        }

        @Override // org.xml.sax.ext.EntityResolver2
        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalSubset", name, baseURI));
        }

        @Override // org.xml.sax.ext.EntityResolver2
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
            throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalEntity2", name, publicId, baseURI, systemId));
        }
    }

    private char getOrderChar(SortManager.Order order, char column) {
        if (column != order.column || order.ascending) {
            return 'D';
        }
        return 'A';
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$SortManager.class */
    private static class SortManager {
        protected Comparator<WebResource> defaultResourceComparator;
        protected Comparator<WebResource> resourceNameComparator;
        protected Comparator<WebResource> resourceNameComparatorAsc;
        protected Comparator<WebResource> resourceSizeComparator;
        protected Comparator<WebResource> resourceSizeComparatorAsc;
        protected Comparator<WebResource> resourceLastModifiedComparator;
        protected Comparator<WebResource> resourceLastModifiedComparatorAsc;

        public SortManager(boolean directoriesFirst) {
            this.resourceNameComparator = new ResourceNameComparator();
            this.resourceNameComparatorAsc = Collections.reverseOrder(this.resourceNameComparator);
            this.resourceSizeComparator = new ResourceSizeComparator(this.resourceNameComparator);
            this.resourceSizeComparatorAsc = Collections.reverseOrder(this.resourceSizeComparator);
            this.resourceLastModifiedComparator = new ResourceLastModifiedDateComparator(this.resourceNameComparator);
            this.resourceLastModifiedComparatorAsc = Collections.reverseOrder(this.resourceLastModifiedComparator);
            if (directoriesFirst) {
                this.resourceNameComparator = new DirsFirstComparator(this.resourceNameComparator);
                this.resourceNameComparatorAsc = new DirsFirstComparator(this.resourceNameComparatorAsc);
                this.resourceSizeComparator = new DirsFirstComparator(this.resourceSizeComparator);
                this.resourceSizeComparatorAsc = new DirsFirstComparator(this.resourceSizeComparatorAsc);
                this.resourceLastModifiedComparator = new DirsFirstComparator(this.resourceLastModifiedComparator);
                this.resourceLastModifiedComparatorAsc = new DirsFirstComparator(this.resourceLastModifiedComparatorAsc);
            }
            this.defaultResourceComparator = this.resourceNameComparator;
        }

        public void sort(WebResource[] resources, String order) {
            Comparator<WebResource> comparator = getComparator(order);
            if (null != comparator) {
                Arrays.sort(resources, comparator);
            }
        }

        public Comparator<WebResource> getComparator(String order) {
            return getComparator(getOrder(order));
        }

        public Comparator<WebResource> getComparator(Order order) {
            if (null == order) {
                return this.defaultResourceComparator;
            }
            if ('N' == order.column) {
                if (order.ascending) {
                    return this.resourceNameComparatorAsc;
                }
                return this.resourceNameComparator;
            }
            if ('S' == order.column) {
                if (order.ascending) {
                    return this.resourceSizeComparatorAsc;
                }
                return this.resourceSizeComparator;
            }
            if ('M' == order.column) {
                if (order.ascending) {
                    return this.resourceLastModifiedComparatorAsc;
                }
                return this.resourceLastModifiedComparator;
            }
            return this.defaultResourceComparator;
        }

        public Order getOrder(String order) {
            if (null == order || 0 == order.trim().length()) {
                return Order.DEFAULT;
            }
            String[] options = order.split(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            if (0 == options.length) {
                return Order.DEFAULT;
            }
            char column = 0;
            boolean ascending = false;
            for (String str : options) {
                String option = str.trim();
                if (2 < option.length()) {
                    char opt = option.charAt(0);
                    if ('C' == opt) {
                        column = option.charAt(2);
                    } else if ('O' == opt) {
                        ascending = 'A' == option.charAt(2);
                    }
                }
            }
            if ('N' == column) {
                if (ascending) {
                    return Order.NAME_ASC;
                }
                return Order.NAME;
            }
            if ('S' == column) {
                if (ascending) {
                    return Order.SIZE_ASC;
                }
                return Order.SIZE;
            }
            if ('M' == column) {
                if (ascending) {
                    return Order.LAST_MODIFIED_ASC;
                }
                return Order.LAST_MODIFIED;
            }
            return Order.DEFAULT;
        }

        /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$SortManager$Order.class */
        public static class Order {
            final char column;
            final boolean ascending;
            public static final Order NAME = new Order('N', false);
            public static final Order NAME_ASC = new Order('N', true);
            public static final Order SIZE = new Order('S', false);
            public static final Order SIZE_ASC = new Order('S', true);
            public static final Order LAST_MODIFIED = new Order('M', false);
            public static final Order LAST_MODIFIED_ASC = new Order('M', true);
            public static final Order DEFAULT = NAME;

            public Order(char column, boolean ascending) {
                this.column = column;
                this.ascending = ascending;
            }
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$DirsFirstComparator.class */
    private static class DirsFirstComparator implements Comparator<WebResource> {
        private final Comparator<WebResource> base;

        public DirsFirstComparator(Comparator<WebResource> core) {
            this.base = core;
        }

        @Override // java.util.Comparator
        public int compare(WebResource r1, WebResource r2) {
            if (r1.isDirectory()) {
                if (r2.isDirectory()) {
                    return this.base.compare(r1, r2);
                }
                return -1;
            }
            if (r2.isDirectory()) {
                return 1;
            }
            return this.base.compare(r1, r2);
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$ResourceNameComparator.class */
    private static class ResourceNameComparator implements Comparator<WebResource> {
        private ResourceNameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(WebResource r1, WebResource r2) {
            return r1.getName().compareTo(r2.getName());
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$ResourceSizeComparator.class */
    private static class ResourceSizeComparator implements Comparator<WebResource> {
        private Comparator<WebResource> base;

        public ResourceSizeComparator(Comparator<WebResource> base) {
            this.base = base;
        }

        @Override // java.util.Comparator
        public int compare(WebResource r1, WebResource r2) {
            int c = Long.compare(r1.getContentLength(), r2.getContentLength());
            if (0 == c) {
                return this.base.compare(r1, r2);
            }
            return c;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlets/DefaultServlet$ResourceLastModifiedDateComparator.class */
    private static class ResourceLastModifiedDateComparator implements Comparator<WebResource> {
        private Comparator<WebResource> base;

        public ResourceLastModifiedDateComparator(Comparator<WebResource> base) {
            this.base = base;
        }

        @Override // java.util.Comparator
        public int compare(WebResource r1, WebResource r2) {
            int c = Long.compare(r1.getLastModified(), r2.getLastModified());
            if (0 == c) {
                return this.base.compare(r1, r2);
            }
            return c;
        }
    }
}
