package org.springframework.web.context.request;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/FacesRequestAttributes.class */
public class FacesRequestAttributes implements RequestAttributes {
    private static final boolean portletApiPresent = ClassUtils.isPresent("javax.portlet.PortletSession", FacesRequestAttributes.class.getClassLoader());
    private static final Log logger = LogFactory.getLog(FacesRequestAttributes.class);
    private final FacesContext facesContext;

    public FacesRequestAttributes(FacesContext facesContext) {
        Assert.notNull(facesContext, "FacesContext must not be null");
        this.facesContext = facesContext;
    }

    protected final FacesContext getFacesContext() {
        return this.facesContext;
    }

    protected final ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    protected Map<String, Object> getAttributeMap(int scope) {
        if (scope == 0) {
            return getExternalContext().getRequestMap();
        }
        return getExternalContext().getSessionMap();
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public Object getAttribute(String name, int scope) {
        if (scope == 2 && portletApiPresent) {
            return PortletSessionAccessor.getAttribute(name, getExternalContext());
        }
        return getAttributeMap(scope).get(name);
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public void setAttribute(String name, Object value, int scope) {
        if (scope == 2 && portletApiPresent) {
            PortletSessionAccessor.setAttribute(name, value, getExternalContext());
        } else {
            getAttributeMap(scope).put(name, value);
        }
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public void removeAttribute(String name, int scope) {
        if (scope == 2 && portletApiPresent) {
            PortletSessionAccessor.removeAttribute(name, getExternalContext());
        } else {
            getAttributeMap(scope).remove(name);
        }
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public String[] getAttributeNames(int scope) {
        if (scope == 2 && portletApiPresent) {
            return PortletSessionAccessor.getAttributeNames(getExternalContext());
        }
        return StringUtils.toStringArray(getAttributeMap(scope).keySet());
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public void registerDestructionCallback(String name, Runnable callback, int scope) {
        if (logger.isWarnEnabled()) {
            logger.warn("Could not register destruction callback [" + callback + "] for attribute '" + name + "' because FacesRequestAttributes does not support such callbacks");
        }
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public Object resolveReference(String key) {
        if ("request".equals(key)) {
            return getExternalContext().getRequest();
        }
        if ("session".equals(key)) {
            return getExternalContext().getSession(true);
        }
        if ("application".equals(key)) {
            return getExternalContext().getContext();
        }
        if ("requestScope".equals(key)) {
            return getExternalContext().getRequestMap();
        }
        if ("sessionScope".equals(key)) {
            return getExternalContext().getSessionMap();
        }
        if ("applicationScope".equals(key)) {
            return getExternalContext().getApplicationMap();
        }
        if ("facesContext".equals(key)) {
            return getFacesContext();
        }
        if ("cookie".equals(key)) {
            return getExternalContext().getRequestCookieMap();
        }
        if ("header".equals(key)) {
            return getExternalContext().getRequestHeaderMap();
        }
        if ("headerValues".equals(key)) {
            return getExternalContext().getRequestHeaderValuesMap();
        }
        if ("param".equals(key)) {
            return getExternalContext().getRequestParameterMap();
        }
        if ("paramValues".equals(key)) {
            return getExternalContext().getRequestParameterValuesMap();
        }
        if ("initParam".equals(key)) {
            return getExternalContext().getInitParameterMap();
        }
        if ("view".equals(key)) {
            return getFacesContext().getViewRoot();
        }
        if ("viewScope".equals(key)) {
            return getFacesContext().getViewRoot().getViewMap();
        }
        if ("flash".equals(key)) {
            return getExternalContext().getFlash();
        }
        if ("resource".equals(key)) {
            return getFacesContext().getApplication().getResourceHandler();
        }
        return null;
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public String getSessionId() throws NoSuchMethodException, SecurityException {
        Object session = getExternalContext().getSession(true);
        try {
            Method getIdMethod = session.getClass().getMethod("getId", new Class[0]);
            return ReflectionUtils.invokeMethod(getIdMethod, session).toString();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Session object [" + session + "] does not have a getId() method");
        }
    }

    @Override // org.springframework.web.context.request.RequestAttributes
    public Object getSessionMutex() {
        ExternalContext externalContext = getExternalContext();
        Object session = externalContext.getSession(true);
        Object mutex = externalContext.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
        if (mutex == null) {
            mutex = session != null ? session : externalContext;
        }
        return mutex;
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/FacesRequestAttributes$PortletSessionAccessor.class */
    private static class PortletSessionAccessor {
        private PortletSessionAccessor() {
        }

        public static Object getAttribute(String name, ExternalContext externalContext) {
            Object session = externalContext.getSession(false);
            if (session instanceof PortletSession) {
                return ((PortletSession) session).getAttribute(name, 1);
            }
            if (session != null) {
                return externalContext.getSessionMap().get(name);
            }
            return null;
        }

        public static void setAttribute(String name, Object value, ExternalContext externalContext) {
            Object session = externalContext.getSession(true);
            if (session instanceof PortletSession) {
                ((PortletSession) session).setAttribute(name, value, 1);
            } else {
                externalContext.getSessionMap().put(name, value);
            }
        }

        public static void removeAttribute(String name, ExternalContext externalContext) {
            Object session = externalContext.getSession(false);
            if (session instanceof PortletSession) {
                ((PortletSession) session).removeAttribute(name, 1);
            } else if (session != null) {
                externalContext.getSessionMap().remove(name);
            }
        }

        public static String[] getAttributeNames(ExternalContext externalContext) {
            Object session = externalContext.getSession(false);
            if (session instanceof PortletSession) {
                return StringUtils.toStringArray((Enumeration<String>) ((PortletSession) session).getAttributeNames(1));
            }
            if (session != null) {
                return StringUtils.toStringArray(externalContext.getSessionMap().keySet());
            }
            return new String[0];
        }
    }
}
