package org.springframework.http.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/server/ServletServerHttpRequest.class */
public class ServletServerHttpRequest implements ServerHttpRequest {
    protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    protected static final String FORM_CHARSET = "UTF-8";
    private final HttpServletRequest servletRequest;
    private URI uri;
    private HttpHeaders headers;
    private ServerHttpAsyncRequestControl asyncRequestControl;

    public ServletServerHttpRequest(HttpServletRequest servletRequest) {
        Assert.notNull(servletRequest, "HttpServletRequest must not be null");
        this.servletRequest = servletRequest;
    }

    public HttpServletRequest getServletRequest() {
        return this.servletRequest;
    }

    @Override // org.springframework.http.HttpRequest
    public HttpMethod getMethod() {
        return HttpMethod.resolve(this.servletRequest.getMethod());
    }

    @Override // org.springframework.http.HttpRequest
    public URI getURI() {
        if (this.uri == null) {
            String urlString = null;
            boolean hasQuery = false;
            try {
                StringBuffer url = this.servletRequest.getRequestURL();
                String query = this.servletRequest.getQueryString();
                hasQuery = StringUtils.hasText(query);
                if (hasQuery) {
                    url.append('?').append(query);
                }
                urlString = url.toString();
                this.uri = new URI(urlString);
            } catch (URISyntaxException ex) {
                if (!hasQuery) {
                    throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex);
                }
                try {
                    urlString = this.servletRequest.getRequestURL().toString();
                    this.uri = new URI(urlString);
                } catch (URISyntaxException ex2) {
                    throw new IllegalStateException("Could not resolve HttpServletRequest as URI: " + urlString, ex2);
                }
            }
        }
        return this.uri;
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        int requestContentLength;
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            Enumeration<?> names = this.servletRequest.getHeaderNames();
            while (names.hasMoreElements()) {
                String headerName = names.nextElement();
                Enumeration<?> headerValues = this.servletRequest.getHeaders(headerName);
                while (headerValues.hasMoreElements()) {
                    String headerValue = headerValues.nextElement();
                    this.headers.add(headerName, headerValue);
                }
            }
            try {
                MediaType contentType = this.headers.getContentType();
                if (contentType == null) {
                    String requestContentType = this.servletRequest.getContentType();
                    if (StringUtils.hasLength(requestContentType)) {
                        contentType = MediaType.parseMediaType(requestContentType);
                        this.headers.setContentType(contentType);
                    }
                }
                if (contentType != null && contentType.getCharset() == null) {
                    String requestEncoding = this.servletRequest.getCharacterEncoding();
                    if (StringUtils.hasLength(requestEncoding)) {
                        Charset charSet = Charset.forName(requestEncoding);
                        Map<String, String> params = new LinkedCaseInsensitiveMap<>();
                        params.putAll(contentType.getParameters());
                        params.put("charset", charSet.toString());
                        MediaType mediaType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
                        this.headers.setContentType(mediaType);
                    }
                }
            } catch (InvalidMediaTypeException e) {
            }
            if (this.headers.getContentLength() < 0 && (requestContentLength = this.servletRequest.getContentLength()) != -1) {
                this.headers.setContentLength(requestContentLength);
            }
        }
        return this.headers;
    }

    @Override // org.springframework.http.server.ServerHttpRequest
    public Principal getPrincipal() {
        return this.servletRequest.getUserPrincipal();
    }

    @Override // org.springframework.http.server.ServerHttpRequest
    public InetSocketAddress getLocalAddress() {
        return new InetSocketAddress(this.servletRequest.getLocalName(), this.servletRequest.getLocalPort());
    }

    @Override // org.springframework.http.server.ServerHttpRequest
    public InetSocketAddress getRemoteAddress() {
        return new InetSocketAddress(this.servletRequest.getRemoteHost(), this.servletRequest.getRemotePort());
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        if (isFormPost(this.servletRequest)) {
            return getBodyFromServletRequestParameters(this.servletRequest);
        }
        return this.servletRequest.getInputStream();
    }

    @Override // org.springframework.http.server.ServerHttpRequest
    public ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse response) {
        if (this.asyncRequestControl == null) {
            if (!ServletServerHttpResponse.class.isInstance(response)) {
                throw new IllegalArgumentException("Response must be a ServletServerHttpResponse: " + response.getClass());
            }
            ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse) response;
            this.asyncRequestControl = new ServletServerHttpAsyncRequestControl(this, servletServerResponse);
        }
        return this.asyncRequestControl;
    }

    private static boolean isFormPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/x-www-form-urlencoded") && HttpMethod.POST.matches(request.getMethod());
    }

    private static InputStream getBodyFromServletRequestParameters(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        Writer writer = new OutputStreamWriter(bos, "UTF-8");
        Map<String, String[]> form = request.getParameterMap();
        Iterator<String> nameIterator = form.keySet().iterator();
        while (nameIterator.hasNext()) {
            String name = nameIterator.next();
            List<String> values = Arrays.asList(form.get(name));
            Iterator<String> valueIterator = values.iterator();
            while (valueIterator.hasNext()) {
                String value = valueIterator.next();
                writer.write(URLEncoder.encode(name, "UTF-8"));
                if (value != null) {
                    writer.write(61);
                    writer.write(URLEncoder.encode(value, "UTF-8"));
                    if (valueIterator.hasNext()) {
                        writer.write(38);
                    }
                }
            }
            if (nameIterator.hasNext()) {
                writer.append('&');
            }
        }
        writer.flush();
        return new ByteArrayInputStream(bos.toByteArray());
    }
}
