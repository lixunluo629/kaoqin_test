package org.springframework.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/ResourceServlet.class */
public class ResourceServlet extends HttpServletBean {
    public static final String RESOURCE_URL_DELIMITERS = ",; \t\n";
    public static final String RESOURCE_PARAM_NAME = "resource";
    private String defaultUrl;
    private String allowedResources;
    private String contentType;
    private boolean applyLastModified = false;
    private PathMatcher pathMatcher;
    private long startupTime;

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public void setAllowedResources(String allowedResources) {
        this.allowedResources = allowedResources;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setApplyLastModified(boolean applyLastModified) {
        this.applyLastModified = applyLastModified;
    }

    @Override // org.springframework.web.servlet.HttpServletBean
    protected void initServletBean() {
        this.pathMatcher = getPathMatcher();
        this.startupTime = System.currentTimeMillis();
    }

    protected PathMatcher getPathMatcher() {
        return new AntPathMatcher();
    }

    @Override // javax.servlet.http.HttpServlet
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resourceUrl = determineResourceUrl(request);
        if (resourceUrl != null) {
            try {
                doInclude(request, response, resourceUrl);
                return;
            } catch (IOException ex) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex);
                }
                if (!includeDefaultUrl(request, response)) {
                    throw ex;
                }
                return;
            } catch (ServletException ex2) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex2);
                }
                if (!includeDefaultUrl(request, response)) {
                    throw ex2;
                }
                return;
            }
        }
        if (!includeDefaultUrl(request, response)) {
            throw new ServletException("No target resource URL found for request");
        }
    }

    protected String determineResourceUrl(HttpServletRequest request) {
        return request.getParameter("resource");
    }

    private boolean includeDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.defaultUrl == null) {
            return false;
        }
        doInclude(request, response, this.defaultUrl);
        return true;
    }

    private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl) throws ServletException, IOException {
        if (this.contentType != null) {
            response.setContentType(this.contentType);
        }
        String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
        for (String url : resourceUrls) {
            String path = StringUtils.cleanPath(url);
            if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, path)) {
                throw new ServletException("Resource [" + path + "] does not match allowed pattern [" + this.allowedResources + "]");
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Including resource [" + path + "]");
            }
            RequestDispatcher rd = request.getRequestDispatcher(path);
            rd.include(request, response);
        }
    }

    @Override // javax.servlet.http.HttpServlet
    protected final long getLastModified(HttpServletRequest request) {
        if (this.applyLastModified) {
            String resourceUrl = determineResourceUrl(request);
            if (resourceUrl == null) {
                resourceUrl = this.defaultUrl;
            }
            if (resourceUrl != null) {
                String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
                long latestTimestamp = -1;
                for (String url : resourceUrls) {
                    long timestamp = getFileTimestamp(url);
                    if (timestamp > latestTimestamp) {
                        latestTimestamp = timestamp;
                    }
                }
                return latestTimestamp > this.startupTime ? latestTimestamp : this.startupTime;
            }
            return -1L;
        }
        return -1L;
    }

    protected long getFileTimestamp(String resourceUrl) {
        ServletContextResource resource = new ServletContextResource(getServletContext(), resourceUrl);
        try {
            long lastModifiedTime = resource.lastModified();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Last-modified timestamp of " + resource + " is " + lastModifiedTime);
            }
            return lastModifiedTime;
        } catch (IOException e) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Couldn't retrieve last-modified timestamp of " + resource + " - using ResourceServlet startup time");
                return -1L;
            }
            return -1L;
        }
    }
}
