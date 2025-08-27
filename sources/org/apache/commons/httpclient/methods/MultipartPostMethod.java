package org.apache.commons.httpclient.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/MultipartPostMethod.class */
public class MultipartPostMethod extends ExpectContinueMethod {
    public static final String MULTIPART_FORM_CONTENT_TYPE = "multipart/form-data";
    private static final Log LOG;
    private final List parameters;
    static Class class$org$apache$commons$httpclient$methods$MultipartPostMethod;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$methods$MultipartPostMethod == null) {
            clsClass$ = class$("org.apache.commons.httpclient.methods.MultipartPostMethod");
            class$org$apache$commons$httpclient$methods$MultipartPostMethod = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$methods$MultipartPostMethod;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public MultipartPostMethod() {
        this.parameters = new ArrayList();
    }

    public MultipartPostMethod(String uri) {
        super(uri);
        this.parameters = new ArrayList();
    }

    @Override // org.apache.commons.httpclient.methods.ExpectContinueMethod
    protected boolean hasRequestContent() {
        return true;
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public String getName() {
        return WebContentGenerator.METHOD_POST;
    }

    public void addParameter(String parameterName, String parameterValue) {
        LOG.trace("enter addParameter(String parameterName, String parameterValue)");
        this.parameters.add(new StringPart(parameterName, parameterValue));
    }

    public void addParameter(String parameterName, File parameterFile) throws FileNotFoundException {
        LOG.trace("enter MultipartPostMethod.addParameter(String parameterName, File parameterFile)");
        this.parameters.add(new FilePart(parameterName, parameterFile));
    }

    public void addParameter(String parameterName, String fileName, File parameterFile) throws FileNotFoundException {
        LOG.trace("enter MultipartPostMethod.addParameter(String parameterName, String fileName, File parameterFile)");
        this.parameters.add(new FilePart(parameterName, fileName, parameterFile));
    }

    public void addPart(Part part) {
        LOG.trace("enter addPart(Part part)");
        this.parameters.add(part);
    }

    public Part[] getParts() {
        return (Part[]) this.parameters.toArray(new Part[this.parameters.size()]);
    }

    protected void addContentLengthRequestHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter EntityEnclosingMethod.addContentLengthRequestHeader(HttpState, HttpConnection)");
        if (getRequestHeader("Content-Length") == null) {
            long len = getRequestContentLength();
            addRequestHeader("Content-Length", String.valueOf(len));
        }
        removeRequestHeader("Transfer-Encoding");
    }

    protected void addContentTypeRequestHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter EntityEnclosingMethod.addContentTypeRequestHeader(HttpState, HttpConnection)");
        if (!this.parameters.isEmpty()) {
            StringBuffer buffer = new StringBuffer("multipart/form-data");
            if (Part.getBoundary() != null) {
                buffer.append("; boundary=");
                buffer.append(Part.getBoundary());
            }
            setRequestHeader("Content-Type", buffer.toString());
        }
    }

    @Override // org.apache.commons.httpclient.methods.ExpectContinueMethod, org.apache.commons.httpclient.HttpMethodBase
    protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, IllegalArgumentException {
        LOG.trace("enter MultipartPostMethod.addRequestHeaders(HttpState state, HttpConnection conn)");
        super.addRequestHeaders(state, conn);
        addContentLengthRequestHeader(state, conn);
        addContentTypeRequestHeader(state, conn);
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase
    protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter MultipartPostMethod.writeRequestBody(HttpState state, HttpConnection conn)");
        OutputStream out = conn.getRequestOutputStream();
        Part.sendParts(out, getParts());
        return true;
    }

    protected long getRequestContentLength() throws IOException {
        LOG.trace("enter MultipartPostMethod.getRequestContentLength()");
        return Part.getLengthOfParts(getParts());
    }

    @Override // org.apache.commons.httpclient.HttpMethodBase, org.apache.commons.httpclient.HttpMethod
    public void recycle() {
        LOG.trace("enter MultipartPostMethod.recycle()");
        super.recycle();
        this.parameters.clear();
    }
}
