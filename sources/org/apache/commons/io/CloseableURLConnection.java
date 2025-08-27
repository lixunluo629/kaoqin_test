package org.apache.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/CloseableURLConnection.class */
final class CloseableURLConnection extends URLConnection implements AutoCloseable {
    private final URLConnection urlConnection;

    static CloseableURLConnection open(URI uri) throws IOException {
        return open(((URI) Objects.requireNonNull(uri, "uri")).toURL());
    }

    static CloseableURLConnection open(URL url) throws IOException {
        return new CloseableURLConnection(url.openConnection());
    }

    CloseableURLConnection(URLConnection urlConnection) {
        super(((URLConnection) Objects.requireNonNull(urlConnection, "urlConnection")).getURL());
        this.urlConnection = urlConnection;
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String key, String value) {
        this.urlConnection.addRequestProperty(key, value);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        IOUtils.close(this.urlConnection);
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        this.urlConnection.connect();
    }

    public boolean equals(Object obj) {
        return this.urlConnection.equals(obj);
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.urlConnection.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        return this.urlConnection.getConnectTimeout();
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        return this.urlConnection.getContent();
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] classes) throws IOException {
        return this.urlConnection.getContent(classes);
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return this.urlConnection.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        return this.urlConnection.getContentLength();
    }

    @Override // java.net.URLConnection
    public long getContentLengthLong() {
        return this.urlConnection.getContentLengthLong();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        return this.urlConnection.getContentType();
    }

    @Override // java.net.URLConnection
    public long getDate() {
        return this.urlConnection.getDate();
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.urlConnection.getDefaultUseCaches();
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        return this.urlConnection.getDoInput();
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        return this.urlConnection.getDoOutput();
    }

    @Override // java.net.URLConnection
    public long getExpiration() {
        return this.urlConnection.getExpiration();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(int n) {
        return this.urlConnection.getHeaderField(n);
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String name) {
        return this.urlConnection.getHeaderField(name);
    }

    @Override // java.net.URLConnection
    public long getHeaderFieldDate(String name, long Default) {
        return this.urlConnection.getHeaderFieldDate(name, Default);
    }

    @Override // java.net.URLConnection
    public int getHeaderFieldInt(String name, int Default) {
        return this.urlConnection.getHeaderFieldInt(name, Default);
    }

    @Override // java.net.URLConnection
    public String getHeaderFieldKey(int n) {
        return this.urlConnection.getHeaderFieldKey(n);
    }

    @Override // java.net.URLConnection
    public long getHeaderFieldLong(String name, long Default) {
        return this.urlConnection.getHeaderFieldLong(name, Default);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        return this.urlConnection.getHeaderFields();
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        return this.urlConnection.getIfModifiedSince();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        return this.urlConnection.getInputStream();
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        return this.urlConnection.getLastModified();
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        return this.urlConnection.getOutputStream();
    }

    @Override // java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.urlConnection.getPermission();
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        return this.urlConnection.getReadTimeout();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.urlConnection.getRequestProperties();
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String key) {
        return this.urlConnection.getRequestProperty(key);
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        return this.urlConnection.getURL();
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.urlConnection.getUseCaches();
    }

    public int hashCode() {
        return this.urlConnection.hashCode();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean allowUserInteraction) {
        this.urlConnection.setAllowUserInteraction(allowUserInteraction);
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int timeout) {
        this.urlConnection.setConnectTimeout(timeout);
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean defaultUseCaches) {
        this.urlConnection.setDefaultUseCaches(defaultUseCaches);
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean doInput) {
        this.urlConnection.setDoInput(doInput);
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean doOutput) {
        this.urlConnection.setDoOutput(doOutput);
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long ifModifiedSince) {
        this.urlConnection.setIfModifiedSince(ifModifiedSince);
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int timeout) {
        this.urlConnection.setReadTimeout(timeout);
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String key, String value) {
        this.urlConnection.setRequestProperty(key, value);
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean useCaches) {
        this.urlConnection.setUseCaches(useCaches);
    }

    @Override // java.net.URLConnection
    public String toString() {
        return this.urlConnection.toString();
    }
}
