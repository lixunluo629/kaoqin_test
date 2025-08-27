package org.apache.catalina.ssi;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.tomcat.util.ExceptionUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/ResponseIncludeWrapper.class */
public class ResponseIncludeWrapper extends HttpServletResponseWrapper {
    private static final String LAST_MODIFIED = "last-modified";
    protected long lastModified;
    protected final ServletOutputStream captureServletOutputStream;
    protected ServletOutputStream servletOutputStream;
    protected PrintWriter printWriter;
    private static final String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final DateFormat RFC1123_FORMAT = new SimpleDateFormat(RFC1123_PATTERN, Locale.US);

    static {
        RFC1123_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public ResponseIncludeWrapper(HttpServletResponse response, ServletOutputStream captureServletOutputStream) {
        super(response);
        this.lastModified = -1L;
        this.captureServletOutputStream = captureServletOutputStream;
    }

    public void flushOutputStreamOrWriter() throws IOException {
        if (this.servletOutputStream != null) {
            this.servletOutputStream.flush();
        }
        if (this.printWriter != null) {
            this.printWriter.flush();
        }
    }

    @Override // javax.servlet.ServletResponseWrapper, javax.servlet.ServletResponse
    public PrintWriter getWriter() throws IOException {
        if (this.servletOutputStream == null) {
            if (this.printWriter == null) {
                setCharacterEncoding(getCharacterEncoding());
                this.printWriter = new PrintWriter(new OutputStreamWriter(this.captureServletOutputStream, getCharacterEncoding()));
            }
            return this.printWriter;
        }
        throw new IllegalStateException();
    }

    @Override // javax.servlet.ServletResponseWrapper, javax.servlet.ServletResponse
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.printWriter == null) {
            if (this.servletOutputStream == null) {
                this.servletOutputStream = this.captureServletOutputStream;
            }
            return this.servletOutputStream;
        }
        throw new IllegalStateException();
    }

    public long getLastModified() {
        return this.lastModified;
    }

    @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
    public void addDateHeader(String name, long value) {
        super.addDateHeader(name, value);
        String lname = name.toLowerCase(Locale.ENGLISH);
        if (lname.equals(LAST_MODIFIED)) {
            this.lastModified = value;
        }
    }

    @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
    public void addHeader(String name, String value) {
        super.addHeader(name, value);
        String lname = name.toLowerCase(Locale.ENGLISH);
        if (lname.equals(LAST_MODIFIED)) {
            try {
                synchronized (RFC1123_FORMAT) {
                    this.lastModified = RFC1123_FORMAT.parse(value).getTime();
                }
            } catch (Throwable ignore) {
                ExceptionUtils.handleThrowable(ignore);
            }
        }
    }

    @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
    public void setDateHeader(String name, long value) {
        super.setDateHeader(name, value);
        String lname = name.toLowerCase(Locale.ENGLISH);
        if (lname.equals(LAST_MODIFIED)) {
            this.lastModified = value;
        }
    }

    @Override // javax.servlet.http.HttpServletResponseWrapper, javax.servlet.http.HttpServletResponse
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
        String lname = name.toLowerCase(Locale.ENGLISH);
        if (lname.equals(LAST_MODIFIED)) {
            try {
                synchronized (RFC1123_FORMAT) {
                    this.lastModified = RFC1123_FORMAT.parse(value).getTime();
                }
            } catch (Throwable ignore) {
                ExceptionUtils.handleThrowable(ignore);
            }
        }
    }
}
