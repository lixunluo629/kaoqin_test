package org.apache.catalina.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/RestCsrfPreventionFilter.class */
public class RestCsrfPreventionFilter extends CsrfPreventionFilterBase {
    private static final Pattern NON_MODIFYING_METHODS_PATTERN = Pattern.compile("GET|HEAD|OPTIONS");
    private Set<String> pathsAcceptingParams = new HashSet();
    private String pathsDelimiter = ",";

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/RestCsrfPreventionFilter$MethodType.class */
    private enum MethodType {
        NON_MODIFYING_METHOD,
        MODIFYING_METHOD
    }

    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        RestCsrfPreventionStrategy strategy;
        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            MethodType mType = MethodType.MODIFYING_METHOD;
            String method = ((HttpServletRequest) request).getMethod();
            if (method != null && NON_MODIFYING_METHODS_PATTERN.matcher(method).matches()) {
                mType = MethodType.NON_MODIFYING_METHOD;
            }
            switch (mType) {
                case NON_MODIFYING_METHOD:
                    strategy = new FetchRequest();
                    break;
                default:
                    strategy = new StateChangingRequest();
                    break;
            }
            if (!strategy.apply((HttpServletRequest) request, (HttpServletResponse) response)) {
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/RestCsrfPreventionFilter$RestCsrfPreventionStrategy.class */
    private static abstract class RestCsrfPreventionStrategy {
        abstract boolean apply(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;

        private RestCsrfPreventionStrategy() {
        }

        protected String extractNonceFromRequestHeader(HttpServletRequest request, String key) {
            return request.getHeader(key);
        }

        protected String[] extractNonceFromRequestParams(HttpServletRequest request, String key) {
            return request.getParameterValues(key);
        }

        protected void storeNonceToResponse(HttpServletResponse response, String key, String value) {
            response.setHeader(key, value);
        }

        protected String extractNonceFromSession(HttpSession session, String key) {
            if (session == null) {
                return null;
            }
            return (String) session.getAttribute(key);
        }

        protected void storeNonceToSession(HttpSession session, String key, Object value) {
            session.setAttribute(key, value);
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/RestCsrfPreventionFilter$StateChangingRequest.class */
    private class StateChangingRequest extends RestCsrfPreventionStrategy {
        private StateChangingRequest() {
            super();
        }

        @Override // org.apache.catalina.filters.RestCsrfPreventionFilter.RestCsrfPreventionStrategy
        public boolean apply(HttpServletRequest request, HttpServletResponse response) throws IOException {
            if (isValidStateChangingRequest(extractNonceFromRequest(request), extractNonceFromSession(request.getSession(false), Constants.CSRF_REST_NONCE_SESSION_ATTR_NAME))) {
                return true;
            }
            storeNonceToResponse(response, Constants.CSRF_REST_NONCE_HEADER_NAME, Constants.CSRF_REST_NONCE_HEADER_REQUIRED_VALUE);
            response.sendError(RestCsrfPreventionFilter.this.getDenyStatus(), FilterBase.sm.getString("restCsrfPreventionFilter.invalidNonce"));
            return false;
        }

        private boolean isValidStateChangingRequest(String reqNonce, String sessionNonce) {
            return (reqNonce == null || sessionNonce == null || !Objects.equals(reqNonce, sessionNonce)) ? false : true;
        }

        private String extractNonceFromRequest(HttpServletRequest request) {
            String nonceFromRequest = extractNonceFromRequestHeader(request, Constants.CSRF_REST_NONCE_HEADER_NAME);
            if ((nonceFromRequest == null || Objects.equals("", nonceFromRequest)) && !RestCsrfPreventionFilter.this.getPathsAcceptingParams().isEmpty() && RestCsrfPreventionFilter.this.getPathsAcceptingParams().contains(RestCsrfPreventionFilter.this.getRequestedPath(request))) {
                nonceFromRequest = extractNonceFromRequestParams(request);
            }
            return nonceFromRequest;
        }

        private String extractNonceFromRequestParams(HttpServletRequest request) {
            String[] params = extractNonceFromRequestParams(request, Constants.CSRF_REST_NONCE_HEADER_NAME);
            if (params != null && params.length > 0) {
                String nonce = params[0];
                for (String param : params) {
                    if (!Objects.equals(param, nonce)) {
                        return null;
                    }
                }
                return nonce;
            }
            return null;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/RestCsrfPreventionFilter$FetchRequest.class */
    private class FetchRequest extends RestCsrfPreventionStrategy {
        private FetchRequest() {
            super();
        }

        @Override // org.apache.catalina.filters.RestCsrfPreventionFilter.RestCsrfPreventionStrategy
        public boolean apply(HttpServletRequest request, HttpServletResponse response) {
            if (Constants.CSRF_REST_NONCE_HEADER_FETCH_VALUE.equalsIgnoreCase(extractNonceFromRequestHeader(request, Constants.CSRF_REST_NONCE_HEADER_NAME))) {
                String nonceFromSessionStr = extractNonceFromSession(request.getSession(false), Constants.CSRF_REST_NONCE_SESSION_ATTR_NAME);
                if (nonceFromSessionStr == null) {
                    nonceFromSessionStr = RestCsrfPreventionFilter.this.generateNonce();
                    storeNonceToSession((HttpSession) Objects.requireNonNull(request.getSession(true)), Constants.CSRF_REST_NONCE_SESSION_ATTR_NAME, nonceFromSessionStr);
                }
                storeNonceToResponse(response, Constants.CSRF_REST_NONCE_HEADER_NAME, nonceFromSessionStr);
                return true;
            }
            return true;
        }
    }

    public void setPathsAcceptingParams(String pathsList) {
        if (pathsList != null) {
            String[] arr$ = pathsList.split(this.pathsDelimiter);
            for (String element : arr$) {
                this.pathsAcceptingParams.add(element.trim());
            }
        }
    }

    public Set<String> getPathsAcceptingParams() {
        return this.pathsAcceptingParams;
    }
}
