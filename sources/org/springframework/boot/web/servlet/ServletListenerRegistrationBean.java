package org.springframework.boot.web.servlet;

import java.util.Collections;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletListenerRegistrationBean.class */
public class ServletListenerRegistrationBean<T extends EventListener> extends RegistrationBean {
    private static final Log logger = LogFactory.getLog(ServletListenerRegistrationBean.class);
    private static final Set<Class<?>> SUPPORTED_TYPES;
    private T listener;

    static {
        Set<Class<?>> types = new HashSet<>();
        types.add(ServletContextAttributeListener.class);
        types.add(ServletRequestListener.class);
        types.add(ServletRequestAttributeListener.class);
        types.add(HttpSessionAttributeListener.class);
        types.add(HttpSessionListener.class);
        types.add(ServletContextListener.class);
        SUPPORTED_TYPES = Collections.unmodifiableSet(types);
    }

    public ServletListenerRegistrationBean() {
    }

    public ServletListenerRegistrationBean(T listener) {
        Assert.notNull(listener, "Listener must not be null");
        Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
        this.listener = listener;
    }

    public void setListener(T listener) {
        Assert.notNull(listener, "Listener must not be null");
        Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
        this.listener = listener;
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public void setName(String name) {
        super.setName(name);
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public void setAsyncSupported(boolean asyncSupported) {
        super.setAsyncSupported(asyncSupported);
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public boolean isAsyncSupported() {
        return super.isAsyncSupported();
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public void setInitParameters(Map<String, String> initParameters) {
        super.setInitParameters(initParameters);
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public Map<String, String> getInitParameters() {
        return super.getInitParameters();
    }

    @Override // org.springframework.boot.web.servlet.RegistrationBean
    @Deprecated
    public void addInitParameter(String name, String value) {
        super.addInitParameter(name, value);
    }

    @Override // org.springframework.boot.web.servlet.ServletContextInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (!isEnabled()) {
            logger.info("Listener " + this.listener + " was not registered (disabled)");
            return;
        }
        try {
            servletContext.addListener((ServletContext) this.listener);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Failed to add listener '" + this.listener + "' to servlet context", ex);
        }
    }

    public T getListener() {
        return this.listener;
    }

    public static boolean isSupportedType(EventListener listener) {
        for (Class<?> type : SUPPORTED_TYPES) {
            if (ClassUtils.isAssignableValue(type, listener)) {
                return true;
            }
        }
        return false;
    }

    public static Set<Class<?>> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
