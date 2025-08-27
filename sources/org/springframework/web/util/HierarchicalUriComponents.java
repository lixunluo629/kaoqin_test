package org.springframework.web.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents.class */
final class HierarchicalUriComponents extends UriComponents {
    private static final char PATH_DELIMITER = '/';
    private static final String PATH_DELIMITER_STRING = "/";
    private final String userInfo;
    private final String host;
    private final String port;
    private final PathComponent path;
    private final MultiValueMap<String, String> queryParams;
    private final boolean encoded;
    static final PathComponent NULL_PATH_COMPONENT = new PathComponent() { // from class: org.springframework.web.util.HierarchicalUriComponents.1
        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public String getPath() {
            return null;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public List<String> getPathSegments() {
            return Collections.emptyList();
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            return this;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void verify() {
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
            return this;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        }

        public boolean equals(Object obj) {
            return this == obj;
        }

        public int hashCode() {
            return getClass().hashCode();
        }
    };

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$PathComponent.class */
    interface PathComponent extends Serializable {
        String getPath();

        List<String> getPathSegments();

        PathComponent encode(String str) throws UnsupportedEncodingException;

        void verify();

        PathComponent expand(UriComponents.UriTemplateVariables uriTemplateVariables);

        void copyToUriComponentsBuilder(UriComponentsBuilder uriComponentsBuilder);
    }

    HierarchicalUriComponents(String scheme, String userInfo, String host, String port, PathComponent path, MultiValueMap<String, String> queryParams, String fragment, boolean encoded, boolean verify) {
        super(scheme, fragment);
        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        this.path = path != null ? path : NULL_PATH_COMPONENT;
        this.queryParams = CollectionUtils.unmodifiableMultiValueMap(queryParams != null ? queryParams : new LinkedMultiValueMap<>(0));
        this.encoded = encoded;
        if (verify) {
            verify();
        }
    }

    @Override // org.springframework.web.util.UriComponents
    public String getSchemeSpecificPart() {
        return null;
    }

    @Override // org.springframework.web.util.UriComponents
    public String getUserInfo() {
        return this.userInfo;
    }

    @Override // org.springframework.web.util.UriComponents
    public String getHost() {
        return this.host;
    }

    @Override // org.springframework.web.util.UriComponents
    public int getPort() {
        if (this.port == null) {
            return -1;
        }
        if (this.port.contains("{")) {
            throw new IllegalStateException("The port contains a URI variable but has not been expanded yet: " + this.port);
        }
        return Integer.parseInt(this.port);
    }

    @Override // org.springframework.web.util.UriComponents
    public String getPath() {
        return this.path.getPath();
    }

    @Override // org.springframework.web.util.UriComponents
    public List<String> getPathSegments() {
        return this.path.getPathSegments();
    }

    @Override // org.springframework.web.util.UriComponents
    public String getQuery() {
        if (!this.queryParams.isEmpty()) {
            StringBuilder queryBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
                String name = entry.getKey();
                List<String> values = (List) entry.getValue();
                if (CollectionUtils.isEmpty(values)) {
                    if (queryBuilder.length() != 0) {
                        queryBuilder.append('&');
                    }
                    queryBuilder.append(name);
                } else {
                    for (Object value : values) {
                        if (queryBuilder.length() != 0) {
                            queryBuilder.append('&');
                        }
                        queryBuilder.append(name);
                        if (value != null) {
                            queryBuilder.append('=').append(value.toString());
                        }
                    }
                }
            }
            return queryBuilder.toString();
        }
        return null;
    }

    @Override // org.springframework.web.util.UriComponents
    public MultiValueMap<String, String> getQueryParams() {
        return this.queryParams;
    }

    @Override // org.springframework.web.util.UriComponents
    public HierarchicalUriComponents encode(String encoding) throws UnsupportedEncodingException {
        if (this.encoded) {
            return this;
        }
        Assert.hasLength(encoding, "Encoding must not be empty");
        String schemeTo = encodeUriComponent(getScheme(), encoding, Type.SCHEME);
        String userInfoTo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
        String hostTo = encodeUriComponent(this.host, encoding, getHostType());
        PathComponent pathTo = this.path.encode(encoding);
        MultiValueMap<String, String> paramsTo = encodeQueryParams(encoding);
        String fragmentTo = encodeUriComponent(getFragment(), encoding, Type.FRAGMENT);
        return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, this.port, pathTo, paramsTo, fragmentTo, true, false);
    }

    private MultiValueMap<String, String> encodeQueryParams(String encoding) throws UnsupportedEncodingException {
        int size = this.queryParams.size();
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>(size);
        for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
            String name = encodeUriComponent(entry.getKey(), encoding, Type.QUERY_PARAM);
            ArrayList arrayList = new ArrayList(((List) entry.getValue()).size());
            for (String value : (List) entry.getValue()) {
                arrayList.add(encodeUriComponent(value, encoding, Type.QUERY_PARAM));
            }
            result.put(name, arrayList);
        }
        return result;
    }

    static String encodeUriComponent(String source, String encoding, Type type) throws UnsupportedEncodingException {
        if (source == null) {
            return null;
        }
        Assert.hasLength(encoding, "Encoding must not be empty");
        byte[] bytes = encodeBytes(source.getBytes(encoding), type);
        return new String(bytes, "US-ASCII");
    }

    private static byte[] encodeBytes(byte[] source, Type type) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(type, "Type must not be null");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
        int length = source.length;
        for (int i = 0; i < length; i++) {
            byte b = source[i];
            if (b < 0) {
                b = (byte) (b + 256);
            }
            if (type.isAllowed(b)) {
                bos.write(b);
            } else {
                bos.write(37);
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 15, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
                bos.write(hex1);
                bos.write(hex2);
            }
        }
        return bos.toByteArray();
    }

    private Type getHostType() {
        return (this.host == null || !this.host.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) ? Type.HOST_IPV4 : Type.HOST_IPV6;
    }

    private void verify() {
        if (!this.encoded) {
            return;
        }
        verifyUriComponent(getScheme(), Type.SCHEME);
        verifyUriComponent(this.userInfo, Type.USER_INFO);
        verifyUriComponent(this.host, getHostType());
        this.path.verify();
        for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
            verifyUriComponent(entry.getKey(), Type.QUERY_PARAM);
            for (String value : (List) entry.getValue()) {
                verifyUriComponent(value, Type.QUERY_PARAM);
            }
        }
        verifyUriComponent(getFragment(), Type.FRAGMENT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void verifyUriComponent(String source, Type type) {
        if (source == null) {
            return;
        }
        int length = source.length();
        int i = 0;
        while (i < length) {
            char ch2 = source.charAt(i);
            if (ch2 == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + SymbolConstants.QUOTES_SYMBOL);
                    }
                    i += 2;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + SymbolConstants.QUOTES_SYMBOL);
                }
            } else if (!type.isAllowed(ch2)) {
                throw new IllegalArgumentException("Invalid character '" + ch2 + "' for " + type.name() + " in \"" + source + SymbolConstants.QUOTES_SYMBOL);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.util.UriComponents
    public HierarchicalUriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables) {
        Assert.state(!this.encoded, "Cannot expand an already encoded UriComponents object");
        String schemeTo = expandUriComponent(getScheme(), uriVariables);
        String userInfoTo = expandUriComponent(this.userInfo, uriVariables);
        String hostTo = expandUriComponent(this.host, uriVariables);
        String portTo = expandUriComponent(this.port, uriVariables);
        PathComponent pathTo = this.path.expand(uriVariables);
        MultiValueMap<String, String> paramsTo = expandQueryParams(uriVariables);
        String fragmentTo = expandUriComponent(getFragment(), uriVariables);
        return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, portTo, pathTo, paramsTo, fragmentTo, false, false);
    }

    private MultiValueMap<String, String> expandQueryParams(UriComponents.UriTemplateVariables variables) {
        int size = this.queryParams.size();
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>(size);
        UriComponents.UriTemplateVariables variables2 = new QueryUriTemplateVariables(variables);
        for (Map.Entry<String, String> entry : this.queryParams.entrySet()) {
            String name = expandUriComponent(entry.getKey(), variables2);
            ArrayList arrayList = new ArrayList(((List) entry.getValue()).size());
            for (String value : (List) entry.getValue()) {
                arrayList.add(expandUriComponent(value, variables2));
            }
            result.put(name, arrayList);
        }
        return result;
    }

    @Override // org.springframework.web.util.UriComponents
    public UriComponents normalize() {
        String normalizedPath = StringUtils.cleanPath(getPath());
        return new HierarchicalUriComponents(getScheme(), this.userInfo, this.host, this.port, new FullPathComponent(normalizedPath), this.queryParams, getFragment(), this.encoded, false);
    }

    @Override // org.springframework.web.util.UriComponents
    public String toUriString() {
        StringBuilder uriBuilder = new StringBuilder();
        if (getScheme() != null) {
            uriBuilder.append(getScheme());
            uriBuilder.append(':');
        }
        if (this.userInfo != null || this.host != null) {
            uriBuilder.append("//");
            if (this.userInfo != null) {
                uriBuilder.append(this.userInfo);
                uriBuilder.append('@');
            }
            if (this.host != null) {
                uriBuilder.append(this.host);
            }
            if (getPort() != -1) {
                uriBuilder.append(':');
                uriBuilder.append(this.port);
            }
        }
        String path = getPath();
        if (StringUtils.hasLength(path)) {
            if (uriBuilder.length() != 0 && path.charAt(0) != '/') {
                uriBuilder.append('/');
            }
            uriBuilder.append(path);
        }
        String query = getQuery();
        if (query != null) {
            uriBuilder.append('?');
            uriBuilder.append(query);
        }
        if (getFragment() != null) {
            uriBuilder.append('#');
            uriBuilder.append(getFragment());
        }
        return uriBuilder.toString();
    }

    @Override // org.springframework.web.util.UriComponents
    public URI toUri() {
        try {
            if (this.encoded) {
                return new URI(toString());
            }
            String path = getPath();
            if (StringUtils.hasLength(path) && path.charAt(0) != '/' && (getScheme() != null || getUserInfo() != null || getHost() != null || getPort() != -1)) {
                path = '/' + path;
            }
            return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), getFragment());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }

    @Override // org.springframework.web.util.UriComponents
    protected void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
        builder.scheme(getScheme());
        builder.userInfo(getUserInfo());
        builder.host(getHost());
        builder.port(getPort());
        builder.replacePath("");
        this.path.copyToUriComponentsBuilder(builder);
        builder.replaceQueryParams(getQueryParams());
        builder.fragment(getFragment());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HierarchicalUriComponents)) {
            return false;
        }
        HierarchicalUriComponents other = (HierarchicalUriComponents) obj;
        return ObjectUtils.nullSafeEquals(getScheme(), other.getScheme()) && ObjectUtils.nullSafeEquals(getUserInfo(), other.getUserInfo()) && ObjectUtils.nullSafeEquals(getHost(), other.getHost()) && getPort() == other.getPort() && this.path.equals(other.path) && this.queryParams.equals(other.queryParams) && ObjectUtils.nullSafeEquals(getFragment(), other.getFragment());
    }

    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(getScheme());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + ObjectUtils.nullSafeHashCode(this.userInfo))) + ObjectUtils.nullSafeHashCode(this.host))) + ObjectUtils.nullSafeHashCode(this.port))) + this.path.hashCode())) + this.queryParams.hashCode())) + ObjectUtils.nullSafeHashCode(getFragment());
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$Type.class */
    enum Type {
        SCHEME { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.1
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isAlpha(c) || isDigit(c) || 43 == c || 45 == c || 46 == c;
            }
        },
        AUTHORITY { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.2
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c;
            }
        },
        USER_INFO { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.3
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 58 == c;
            }
        },
        HOST_IPV4 { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.4
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c);
            }
        },
        HOST_IPV6 { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.5
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 91 == c || 93 == c || 58 == c;
            }
        },
        PORT { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.6
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isDigit(c);
            }
        },
        PATH { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.7
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c;
            }
        },
        PATH_SEGMENT { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.8
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isPchar(c);
            }
        },
        QUERY { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.9
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c || 63 == c;
            }
        },
        QUERY_PARAM { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.10
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                if (61 == c || 43 == c || 38 == c) {
                    return false;
                }
                return isPchar(c) || 47 == c || 63 == c;
            }
        },
        FRAGMENT { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.11
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c || 63 == c;
            }
        },
        URI { // from class: org.springframework.web.util.HierarchicalUriComponents.Type.12
            @Override // org.springframework.web.util.HierarchicalUriComponents.Type
            public boolean isAllowed(int c) {
                return isUnreserved(c);
            }
        };

        public abstract boolean isAllowed(int i);

        protected boolean isAlpha(int c) {
            return (c >= 97 && c <= 122) || (c >= 65 && c <= 90);
        }

        protected boolean isDigit(int c) {
            return c >= 48 && c <= 57;
        }

        protected boolean isGenericDelimiter(int c) {
            return 58 == c || 47 == c || 63 == c || 35 == c || 91 == c || 93 == c || 64 == c;
        }

        protected boolean isSubDelimiter(int c) {
            return 33 == c || 36 == c || 38 == c || 39 == c || 40 == c || 41 == c || 42 == c || 43 == c || 44 == c || 59 == c || 61 == c;
        }

        protected boolean isReserved(int c) {
            return isGenericDelimiter(c) || isSubDelimiter(c);
        }

        protected boolean isUnreserved(int c) {
            return isAlpha(c) || isDigit(c) || 45 == c || 46 == c || 95 == c || 126 == c;
        }

        protected boolean isPchar(int c) {
            return isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c;
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$FullPathComponent.class */
    static final class FullPathComponent implements PathComponent {
        private final String path;

        public FullPathComponent(String path) {
            this.path = path;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public String getPath() {
            return this.path;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public List<String> getPathSegments() {
            String[] segments = StringUtils.tokenizeToStringArray(getPath(), "/");
            if (segments == null) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(Arrays.asList(segments));
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            String encodedPath = HierarchicalUriComponents.encodeUriComponent(getPath(), encoding, Type.PATH);
            return new FullPathComponent(encodedPath);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void verify() {
            HierarchicalUriComponents.verifyUriComponent(getPath(), Type.PATH);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
            String expandedPath = UriComponents.expandUriComponent(getPath(), uriVariables);
            return new FullPathComponent(expandedPath);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
            builder.path(getPath());
        }

        public boolean equals(Object obj) {
            return this == obj || ((obj instanceof FullPathComponent) && ObjectUtils.nullSafeEquals(getPath(), ((FullPathComponent) obj).getPath()));
        }

        public int hashCode() {
            return ObjectUtils.nullSafeHashCode(getPath());
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$PathSegmentComponent.class */
    static final class PathSegmentComponent implements PathComponent {
        private final List<String> pathSegments;

        public PathSegmentComponent(List<String> pathSegments) {
            Assert.notNull(pathSegments, "List must not be null");
            this.pathSegments = Collections.unmodifiableList(new ArrayList(pathSegments));
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public String getPath() {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append('/');
            Iterator<String> iterator = this.pathSegments.iterator();
            while (iterator.hasNext()) {
                String pathSegment = iterator.next();
                pathBuilder.append(pathSegment);
                if (iterator.hasNext()) {
                    pathBuilder.append('/');
                }
            }
            return pathBuilder.toString();
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public List<String> getPathSegments() {
            return this.pathSegments;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            List<String> pathSegments = getPathSegments();
            List<String> encodedPathSegments = new ArrayList<>(pathSegments.size());
            for (String pathSegment : pathSegments) {
                String encodedPathSegment = HierarchicalUriComponents.encodeUriComponent(pathSegment, encoding, Type.PATH_SEGMENT);
                encodedPathSegments.add(encodedPathSegment);
            }
            return new PathSegmentComponent(encodedPathSegments);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void verify() {
            for (String pathSegment : getPathSegments()) {
                HierarchicalUriComponents.verifyUriComponent(pathSegment, Type.PATH_SEGMENT);
            }
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
            List<String> pathSegments = getPathSegments();
            List<String> expandedPathSegments = new ArrayList<>(pathSegments.size());
            for (String pathSegment : pathSegments) {
                String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables);
                expandedPathSegments.add(expandedPathSegment);
            }
            return new PathSegmentComponent(expandedPathSegments);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void copyToUriComponentsBuilder(UriComponentsBuilder builder) throws IllegalArgumentException {
            builder.pathSegment(StringUtils.toStringArray(getPathSegments()));
        }

        public boolean equals(Object obj) {
            return this == obj || ((obj instanceof PathSegmentComponent) && getPathSegments().equals(((PathSegmentComponent) obj).getPathSegments()));
        }

        public int hashCode() {
            return getPathSegments().hashCode();
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$PathComponentComposite.class */
    static final class PathComponentComposite implements PathComponent {
        private final List<PathComponent> pathComponents;

        public PathComponentComposite(List<PathComponent> pathComponents) {
            Assert.notNull(pathComponents, "PathComponent List must not be null");
            this.pathComponents = pathComponents;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public String getPath() {
            StringBuilder pathBuilder = new StringBuilder();
            for (PathComponent pathComponent : this.pathComponents) {
                pathBuilder.append(pathComponent.getPath());
            }
            return pathBuilder.toString();
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public List<String> getPathSegments() {
            List<String> result = new ArrayList<>();
            for (PathComponent pathComponent : this.pathComponents) {
                result.addAll(pathComponent.getPathSegments());
            }
            return result;
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            List<PathComponent> encodedComponents = new ArrayList<>(this.pathComponents.size());
            for (PathComponent pathComponent : this.pathComponents) {
                encodedComponents.add(pathComponent.encode(encoding));
            }
            return new PathComponentComposite(encodedComponents);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void verify() {
            for (PathComponent pathComponent : this.pathComponents) {
                pathComponent.verify();
            }
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public PathComponent expand(UriComponents.UriTemplateVariables uriVariables) {
            List<PathComponent> expandedComponents = new ArrayList<>(this.pathComponents.size());
            for (PathComponent pathComponent : this.pathComponents) {
                expandedComponents.add(pathComponent.expand(uriVariables));
            }
            return new PathComponentComposite(expandedComponents);
        }

        @Override // org.springframework.web.util.HierarchicalUriComponents.PathComponent
        public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {
            for (PathComponent pathComponent : this.pathComponents) {
                pathComponent.copyToUriComponentsBuilder(builder);
            }
        }
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/HierarchicalUriComponents$QueryUriTemplateVariables.class */
    private static class QueryUriTemplateVariables implements UriComponents.UriTemplateVariables {
        private final UriComponents.UriTemplateVariables delegate;

        public QueryUriTemplateVariables(UriComponents.UriTemplateVariables delegate) {
            this.delegate = delegate;
        }

        @Override // org.springframework.web.util.UriComponents.UriTemplateVariables
        public Object getValue(String name) {
            Object value = this.delegate.getValue(name);
            if (ObjectUtils.isArray(value)) {
                value = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(value));
            }
            return value;
        }
    }
}
