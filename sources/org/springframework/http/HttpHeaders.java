package org.springframework.http;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/HttpHeaders.class */
public class HttpHeaders implements MultiValueMap<String, String>, Serializable {
    private static final long serialVersionUID = -8578554704772377436L;
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPECT = "Expect";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LINK = "Link";
    public static final String LOCATION = "Location";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
    public static final String TE = "TE";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final String[] DATE_FORMATS = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy"};
    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        if (readOnly) {
            Map<String, List<String>> map = new LinkedCaseInsensitiveMap<>(headers.size(), Locale.ENGLISH);
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                List<String> values = Collections.unmodifiableList(entry.getValue());
                map.put(entry.getKey(), values);
            }
            this.headers = Collections.unmodifiableMap(map);
            return;
        }
        this.headers = headers;
    }

    public void setAccept(List<MediaType> acceptableMediaTypes) {
        set("Accept", MediaType.toString(acceptableMediaTypes));
    }

    public List<MediaType> getAccept() {
        return MediaType.parseMediaTypes(get("Accept"));
    }

    public void setAccessControlAllowCredentials(boolean allowCredentials) {
        set("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
    }

    public boolean getAccessControlAllowCredentials() {
        return Boolean.parseBoolean(getFirst("Access-Control-Allow-Credentials"));
    }

    public void setAccessControlAllowHeaders(List<String> allowedHeaders) {
        set("Access-Control-Allow-Headers", toCommaDelimitedString(allowedHeaders));
    }

    public List<String> getAccessControlAllowHeaders() {
        return getValuesAsList("Access-Control-Allow-Headers");
    }

    public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods) {
        set("Access-Control-Allow-Methods", StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public List<HttpMethod> getAccessControlAllowMethods() {
        List<HttpMethod> result = new ArrayList<>();
        String value = getFirst("Access-Control-Allow-Methods");
        if (value != null) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            for (String token : tokens) {
                HttpMethod resolved = HttpMethod.resolve(token);
                if (resolved != null) {
                    result.add(resolved);
                }
            }
        }
        return result;
    }

    public void setAccessControlAllowOrigin(String allowedOrigin) {
        set("Access-Control-Allow-Origin", allowedOrigin);
    }

    public String getAccessControlAllowOrigin() {
        return getFieldValues("Access-Control-Allow-Origin");
    }

    public void setAccessControlExposeHeaders(List<String> exposedHeaders) {
        set("Access-Control-Expose-Headers", toCommaDelimitedString(exposedHeaders));
    }

    public List<String> getAccessControlExposeHeaders() {
        return getValuesAsList("Access-Control-Expose-Headers");
    }

    public void setAccessControlMaxAge(long maxAge) {
        set("Access-Control-Max-Age", Long.toString(maxAge));
    }

    public long getAccessControlMaxAge() {
        String value = getFirst("Access-Control-Max-Age");
        if (value != null) {
            return Long.parseLong(value);
        }
        return -1L;
    }

    public void setAccessControlRequestHeaders(List<String> requestHeaders) {
        set("Access-Control-Request-Headers", toCommaDelimitedString(requestHeaders));
    }

    public List<String> getAccessControlRequestHeaders() {
        return getValuesAsList("Access-Control-Request-Headers");
    }

    public void setAccessControlRequestMethod(HttpMethod requestMethod) {
        set("Access-Control-Request-Method", requestMethod.name());
    }

    public HttpMethod getAccessControlRequestMethod() {
        return HttpMethod.resolve(getFirst("Access-Control-Request-Method"));
    }

    public void setAcceptCharset(List<Charset> acceptableCharsets) {
        StringBuilder builder = new StringBuilder();
        Iterator<Charset> iterator = acceptableCharsets.iterator();
        while (iterator.hasNext()) {
            Charset charset = iterator.next();
            builder.append(charset.name().toLowerCase(Locale.ENGLISH));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        set("Accept-Charset", builder.toString());
    }

    public List<Charset> getAcceptCharset() {
        String charsetName;
        String value = getFirst("Accept-Charset");
        if (value != null) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            List<Charset> result = new ArrayList<>(tokens.length);
            for (String token : tokens) {
                int paramIdx = token.indexOf(59);
                if (paramIdx == -1) {
                    charsetName = token;
                } else {
                    charsetName = token.substring(0, paramIdx);
                }
                if (!charsetName.equals("*")) {
                    result.add(Charset.forName(charsetName));
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    public void setAllow(Set<HttpMethod> allowedMethods) {
        set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public Set<HttpMethod> getAllow() {
        String value = getFirst("Allow");
        if (!StringUtils.isEmpty(value)) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            List<HttpMethod> result = new ArrayList<>(tokens.length);
            for (String token : tokens) {
                HttpMethod resolved = HttpMethod.resolve(token);
                if (resolved != null) {
                    result.add(resolved);
                }
            }
            return EnumSet.copyOf((Collection) result);
        }
        return EnumSet.noneOf(HttpMethod.class);
    }

    public void setCacheControl(String cacheControl) {
        set("Cache-Control", cacheControl);
    }

    public String getCacheControl() {
        return getFieldValues("Cache-Control");
    }

    public void setConnection(String connection) {
        set("Connection", connection);
    }

    public void setConnection(List<String> connection) {
        set("Connection", toCommaDelimitedString(connection));
    }

    public List<String> getConnection() {
        return getValuesAsList("Connection");
    }

    public void setContentDispositionFormData(String name, String filename) {
        Assert.notNull(name, "'name' must not be null");
        StringBuilder builder = new StringBuilder("form-data; name=\"");
        builder.append(name).append('\"');
        if (filename != null) {
            builder.append("; filename=\"");
            builder.append(filename).append('\"');
        }
        set("Content-Disposition", builder.toString());
    }

    @Deprecated
    public void setContentDispositionFormData(String name, String filename, Charset charset) {
        Assert.notNull(name, "'name' must not be null");
        StringBuilder builder = new StringBuilder("form-data; name=\"");
        builder.append(name).append('\"');
        if (filename != null) {
            if (charset == null || charset.name().equals("US-ASCII")) {
                builder.append("; filename=\"");
                builder.append(filename).append('\"');
            } else {
                builder.append("; filename*=");
                builder.append(encodeHeaderFieldParam(filename, charset));
            }
        }
        set("Content-Disposition", builder.toString());
    }

    public void setContentLength(long contentLength) {
        set("Content-Length", Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = getFirst("Content-Length");
        if (value != null) {
            return Long.parseLong(value);
        }
        return -1L;
    }

    public void setContentType(MediaType mediaType) {
        Assert.isTrue(!mediaType.isWildcardType(), "Content-Type cannot contain wildcard type '*'");
        Assert.isTrue(!mediaType.isWildcardSubtype(), "Content-Type cannot contain wildcard subtype '*'");
        set("Content-Type", mediaType.toString());
    }

    public MediaType getContentType() {
        String value = getFirst("Content-Type");
        if (StringUtils.hasLength(value)) {
            return MediaType.parseMediaType(value);
        }
        return null;
    }

    public void setDate(long date) {
        setDate("Date", date);
    }

    public long getDate() {
        return getFirstDate("Date");
    }

    public void setETag(String etag) {
        if (etag != null) {
            Assert.isTrue(etag.startsWith(SymbolConstants.QUOTES_SYMBOL) || etag.startsWith("W/"), "Invalid ETag: does not start with W/ or \"");
            Assert.isTrue(etag.endsWith(SymbolConstants.QUOTES_SYMBOL), "Invalid ETag: does not end with \"");
        }
        set("ETag", etag);
    }

    public String getETag() {
        return getFirst("ETag");
    }

    public void setExpires(long expires) {
        setDate("Expires", expires);
    }

    public long getExpires() {
        return getFirstDate("Expires", false);
    }

    public void setIfMatch(String ifMatch) {
        set("If-Match", ifMatch);
    }

    public void setIfMatch(List<String> ifMatchList) {
        set("If-Match", toCommaDelimitedString(ifMatchList));
    }

    public List<String> getIfMatch() {
        return getETagValuesAsList("If-Match");
    }

    public void setIfModifiedSince(long ifModifiedSince) {
        setDate("If-Modified-Since", ifModifiedSince);
    }

    public long getIfModifiedSince() {
        return getFirstDate("If-Modified-Since", false);
    }

    public void setIfNoneMatch(String ifNoneMatch) {
        set("If-None-Match", ifNoneMatch);
    }

    public void setIfNoneMatch(List<String> ifNoneMatchList) {
        set("If-None-Match", toCommaDelimitedString(ifNoneMatchList));
    }

    public List<String> getIfNoneMatch() {
        return getETagValuesAsList("If-None-Match");
    }

    public void setIfUnmodifiedSince(long ifUnmodifiedSince) {
        setDate("If-Unmodified-Since", ifUnmodifiedSince);
    }

    public long getIfUnmodifiedSince() {
        return getFirstDate("If-Unmodified-Since", false);
    }

    public void setLastModified(long lastModified) {
        setDate("Last-Modified", lastModified);
    }

    public long getLastModified() {
        return getFirstDate("Last-Modified", false);
    }

    public void setLocation(URI location) {
        set("Location", location.toASCIIString());
    }

    public URI getLocation() {
        String value = getFirst("Location");
        if (value != null) {
            return URI.create(value);
        }
        return null;
    }

    public void setOrigin(String origin) {
        set("Origin", origin);
    }

    public String getOrigin() {
        return getFirst("Origin");
    }

    public void setPragma(String pragma) {
        set("Pragma", pragma);
    }

    public String getPragma() {
        return getFirst("Pragma");
    }

    public void setRange(List<HttpRange> ranges) {
        String value = HttpRange.toString(ranges);
        set("Range", value);
    }

    public List<HttpRange> getRange() {
        String value = getFirst("Range");
        return HttpRange.parseRanges(value);
    }

    public void setUpgrade(String upgrade) {
        set("Upgrade", upgrade);
    }

    public String getUpgrade() {
        return getFirst("Upgrade");
    }

    public void setVary(List<String> requestHeaders) {
        set("Vary", toCommaDelimitedString(requestHeaders));
    }

    public List<String> getVary() {
        return getValuesAsList("Vary");
    }

    public void setDate(String headerName, long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
        dateFormat.setTimeZone(GMT);
        set(headerName, dateFormat.format(new Date(date)));
    }

    public long getFirstDate(String headerName) {
        return getFirstDate(headerName, true);
    }

    private long getFirstDate(String headerName, boolean rejectInvalid) {
        String headerValue = getFirst(headerName);
        if (headerValue == null) {
            return -1L;
        }
        if (headerValue.length() >= 3) {
            for (String dateFormat : DATE_FORMATS) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                simpleDateFormat.setTimeZone(GMT);
                try {
                    return simpleDateFormat.parse(headerValue).getTime();
                } catch (ParseException e) {
                }
            }
        }
        if (rejectInvalid) {
            throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
        }
        return -1L;
    }

    public List<String> getValuesAsList(String headerName) {
        List<String> values = get((Object) headerName);
        if (values != null) {
            List<String> result = new ArrayList<>();
            for (String value : values) {
                if (value != null) {
                    String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
                    for (String token : tokens) {
                        result.add(token);
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    protected List<String> getETagValuesAsList(String headerName) {
        List<String> values = get((Object) headerName);
        if (values != null) {
            List<String> result = new ArrayList<>();
            for (String value : values) {
                if (value != null) {
                    Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);
                    while (matcher.find()) {
                        if ("*".equals(matcher.group())) {
                            result.add(matcher.group());
                        } else {
                            result.add(matcher.group(1));
                        }
                    }
                    if (result.isEmpty()) {
                        throw new IllegalArgumentException("Could not parse header '" + headerName + "' with value '" + value + "'");
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    protected String getFieldValues(String headerName) {
        List<String> headerValues = get((Object) headerName);
        if (headerValues != null) {
            return toCommaDelimitedString(headerValues);
        }
        return null;
    }

    protected String toCommaDelimitedString(List<String> headerValues) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = headerValues.iterator();
        while (it.hasNext()) {
            String val = it.next();
            builder.append(val);
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    @Override // org.springframework.util.MultiValueMap
    public String getFirst(String headerName) {
        List<String> headerValues = this.headers.get(headerName);
        if (headerValues != null) {
            return headerValues.get(0);
        }
        return null;
    }

    @Override // org.springframework.util.MultiValueMap
    public void add(String headerName, String headerValue) {
        List<String> headerValues = this.headers.get(headerName);
        if (headerValues == null) {
            headerValues = new LinkedList();
            this.headers.put(headerName, headerValues);
        }
        headerValues.add(headerValue);
    }

    @Override // org.springframework.util.MultiValueMap
    public void set(String headerName, String headerValue) {
        List<String> headerValues = new LinkedList<>();
        headerValues.add(headerValue);
        this.headers.put(headerName, headerValues);
    }

    @Override // org.springframework.util.MultiValueMap
    public void setAll(Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override // org.springframework.util.MultiValueMap
    public Map<String, String> toSingleValueMap() {
        LinkedHashMap<String, String> singleValueMap = new LinkedHashMap<>(this.headers.size());
        for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
            singleValueMap.put(entry.getKey(), entry.getValue().get(0));
        }
        return singleValueMap;
    }

    @Override // java.util.Map
    public int size() {
        return this.headers.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override // java.util.Map
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override // java.util.Map
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override // java.util.Map
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        this.headers.clear();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override // java.util.Map
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpHeaders)) {
            return false;
        }
        HttpHeaders otherHeaders = (HttpHeaders) other;
        return this.headers.equals(otherHeaders.headers);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.headers.hashCode();
    }

    public String toString() {
        return this.headers.toString();
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        Assert.notNull(headers, "HttpHeaders must not be null");
        return new HttpHeaders(headers, true);
    }

    static String encodeHeaderFieldParam(String input, Charset charset) {
        Assert.notNull(input, "Input String should not be null");
        Assert.notNull(charset, "Charset should not be null");
        if (charset.name().equals("US-ASCII")) {
            return input;
        }
        Assert.isTrue(charset.name().equals("UTF-8") || charset.name().equals("ISO-8859-1"), "Charset should be UTF-8 or ISO-8859-1");
        byte[] source = input.getBytes(charset);
        int len = source.length;
        StringBuilder sb = new StringBuilder(len << 1);
        sb.append(charset.name());
        sb.append("''");
        for (byte b : source) {
            if (isRFC5987AttrChar(b)) {
                sb.append((char) b);
            } else {
                sb.append('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 15, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
                sb.append(hex1);
                sb.append(hex2);
            }
        }
        return sb.toString();
    }

    private static boolean isRFC5987AttrChar(byte c) {
        return (c >= 48 && c <= 57) || (c >= 97 && c <= 122) || ((c >= 65 && c <= 90) || c == 33 || c == 35 || c == 36 || c == 38 || c == 43 || c == 45 || c == 46 || c == 94 || c == 95 || c == 96 || c == 124 || c == 126);
    }
}
