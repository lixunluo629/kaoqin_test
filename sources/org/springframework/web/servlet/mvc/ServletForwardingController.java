package org.springframework.web.servlet.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/ServletForwardingController.class */
public class ServletForwardingController extends AbstractController implements BeanNameAware {
    private String servletName;
    private String beanName;

    public ServletForwardingController() {
        super(false);
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        this.beanName = name;
        if (this.servletName == null) {
            this.servletName = name;
        }
    }

    @Override // org.springframework.web.servlet.mvc.AbstractController
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher rd = getServletContext().getNamedDispatcher(this.servletName);
        if (rd == null) {
            throw new ServletException("No servlet with name '" + this.servletName + "' defined in web.xml");
        }
        if (useInclude(request, response)) {
            rd.include(request, response);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Included servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
                return null;
            }
            return null;
        }
        rd.forward(request, response);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Forwarded to servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
            return null;
        }
        return null;
    }

    protected boolean useInclude(HttpServletRequest request, HttpServletResponse response) {
        return WebUtils.isIncludeRequest(request) || response.isCommitted();
    }
}
