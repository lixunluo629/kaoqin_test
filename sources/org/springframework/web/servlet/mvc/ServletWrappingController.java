package org.springframework.web.servlet.mvc;

import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/ServletWrappingController.class */
public class ServletWrappingController extends AbstractController implements BeanNameAware, InitializingBean, DisposableBean {
    private Class<? extends Servlet> servletClass;
    private String servletName;
    private Properties initParameters;
    private String beanName;
    private Servlet servletInstance;

    public ServletWrappingController() {
        super(false);
        this.initParameters = new Properties();
    }

    public void setServletClass(Class<? extends Servlet> servletClass) {
        this.servletClass = servletClass;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public void setInitParameters(Properties initParameters) {
        this.initParameters = initParameters;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (this.servletClass == null) {
            throw new IllegalArgumentException("'servletClass' is required");
        }
        if (this.servletName == null) {
            this.servletName = this.beanName;
        }
        this.servletInstance = this.servletClass.newInstance();
        this.servletInstance.init(new DelegatingServletConfig());
    }

    @Override // org.springframework.web.servlet.mvc.AbstractController
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.servletInstance.service(request, response);
        return null;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.servletInstance.destroy();
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/ServletWrappingController$DelegatingServletConfig.class */
    private class DelegatingServletConfig implements ServletConfig {
        private DelegatingServletConfig() {
        }

        @Override // javax.servlet.ServletConfig
        public String getServletName() {
            return ServletWrappingController.this.servletName;
        }

        @Override // javax.servlet.ServletConfig
        public ServletContext getServletContext() {
            return ServletWrappingController.this.getServletContext();
        }

        @Override // javax.servlet.ServletConfig
        public String getInitParameter(String paramName) {
            return ServletWrappingController.this.initParameters.getProperty(paramName);
        }

        @Override // javax.servlet.ServletConfig
        public Enumeration<String> getInitParameterNames() {
            return ServletWrappingController.this.initParameters.keys();
        }
    }
}
