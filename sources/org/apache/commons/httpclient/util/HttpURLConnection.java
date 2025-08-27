package org.apache.commons.httpclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/HttpURLConnection.class */
public class HttpURLConnection extends java.net.HttpURLConnection {
    private static final Log LOG;
    private HttpMethod method;
    private URL url;
    static Class class$org$apache$commons$httpclient$util$HttpURLConnection;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$util$HttpURLConnection == null) {
            clsClass$ = class$("org.apache.commons.httpclient.util.HttpURLConnection");
            class$org$apache$commons$httpclient$util$HttpURLConnection = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$util$HttpURLConnection;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public HttpURLConnection(HttpMethod method, URL url) {
        super(url);
        this.method = method;
        this.url = url;
    }

    protected HttpURLConnection(URL url) {
        super(url);
        throw new RuntimeException("An HTTP URL connection can only be constructed from a HttpMethod class");
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        LOG.trace("enter HttpURLConnection.getInputStream()");
        return this.method.getResponseBodyAsStream();
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        LOG.trace("enter HttpURLConnection.getErrorStream()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        LOG.trace("enter HttpURLConnection.disconnect()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        LOG.trace("enter HttpURLConnection.connect()");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        LOG.trace("enter HttpURLConnection.usingProxy()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.HttpURLConnection
    public String getRequestMethod() {
        LOG.trace("enter HttpURLConnection.getRequestMethod()");
        return this.method.getName();
    }

    @Override // java.net.HttpURLConnection
    public int getResponseCode() throws IOException {
        LOG.trace("enter HttpURLConnection.getResponseCode()");
        return this.method.getStatusCode();
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        LOG.trace("enter HttpURLConnection.getResponseMessage()");
        return this.method.getStatusText();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String name) {
        LOG.trace("enter HttpURLConnection.getHeaderField(String)");
        Header[] headers = this.method.getResponseHeaders();
        for (int i = headers.length - 1; i >= 0; i--) {
            if (headers[i].getName().equalsIgnoreCase(name)) {
                return headers[i].getValue();
            }
        }
        return null;
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int keyPosition) {
        LOG.trace("enter HttpURLConnection.getHeaderFieldKey(int)");
        if (keyPosition == 0) {
            return null;
        }
        Header[] headers = this.method.getResponseHeaders();
        if (keyPosition < 0 || keyPosition > headers.length) {
            return null;
        }
        return headers[keyPosition - 1].getName();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int position) {
        LOG.trace("enter HttpURLConnection.getHeaderField(int)");
        if (position == 0) {
            return this.method.getStatusLine().toString();
        }
        Header[] headers = this.method.getResponseHeaders();
        if (position < 0 || position > headers.length) {
            return null;
        }
        return headers[position - 1].getValue();
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        LOG.trace("enter HttpURLConnection.getURL()");
        return this.url;
    }

    @Override // java.net.HttpURLConnection
    public void setInstanceFollowRedirects(boolean isFollowingRedirects) {
        LOG.trace("enter HttpURLConnection.setInstanceFollowRedirects(boolean)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.HttpURLConnection
    public boolean getInstanceFollowRedirects() {
        LOG.trace("enter HttpURLConnection.getInstanceFollowRedirects()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.HttpURLConnection
    public void setRequestMethod(String method) throws ProtocolException {
        LOG.trace("enter HttpURLConnection.setRequestMethod(String)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public Permission getPermission() throws IOException {
        LOG.trace("enter HttpURLConnection.getPermission()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        LOG.trace("enter HttpURLConnection.getContent()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] classes) throws IOException {
        LOG.trace("enter HttpURLConnection.getContent(Class[])");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        LOG.trace("enter HttpURLConnection.getOutputStream()");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean isInput) {
        LOG.trace("enter HttpURLConnection.setDoInput()");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        LOG.trace("enter HttpURLConnection.getDoInput()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean isOutput) {
        LOG.trace("enter HttpURLConnection.setDoOutput()");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        LOG.trace("enter HttpURLConnection.getDoOutput()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean isAllowInteraction) {
        LOG.trace("enter HttpURLConnection.setAllowUserInteraction(boolean)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        LOG.trace("enter HttpURLConnection.getAllowUserInteraction()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean isUsingCaches) {
        LOG.trace("enter HttpURLConnection.setUseCaches(boolean)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        LOG.trace("enter HttpURLConnection.getUseCaches()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long modificationDate) {
        LOG.trace("enter HttpURLConnection.setIfModifiedSince(long)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        LOG.trace("enter HttpURLConnection.getIfmodifiedSince()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        LOG.trace("enter HttpURLConnection.getDefaultUseCaches()");
        throw new RuntimeException("Not implemented yet");
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean isUsingCaches) {
        LOG.trace("enter HttpURLConnection.setDefaultUseCaches(boolean)");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String key, String value) {
        LOG.trace("enter HttpURLConnection.setRequestProperty()");
        throw new RuntimeException("This class can only be used with alreadyretrieved data");
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String key) {
        LOG.trace("enter HttpURLConnection.getRequestProperty()");
        throw new RuntimeException("Not implemented yet");
    }
}
