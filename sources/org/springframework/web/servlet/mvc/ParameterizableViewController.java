package org.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/ParameterizableViewController.class */
public class ParameterizableViewController extends AbstractController {
    private Object view;
    private HttpStatus statusCode;
    private boolean statusOnly;

    public ParameterizableViewController() {
        super(false);
        setSupportedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name());
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public String getViewName() {
        if (this.view instanceof String) {
            return (String) this.view;
        }
        return null;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        if (this.view instanceof View) {
            return (View) this.view;
        }
        return null;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    public void setStatusOnly(boolean statusOnly) {
        this.statusOnly = statusOnly;
    }

    public boolean isStatusOnly() {
        return this.statusOnly;
    }

    @Override // org.springframework.web.servlet.mvc.AbstractController
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = getViewName();
        if (getStatusCode() != null) {
            if (getStatusCode().is3xxRedirection()) {
                request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, getStatusCode());
                viewName = (viewName == null || viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) ? viewName : UrlBasedViewResolver.REDIRECT_URL_PREFIX + viewName;
            } else {
                response.setStatus(getStatusCode().value());
                if (isStatusOnly()) {
                    return null;
                }
                if (getStatusCode().equals(HttpStatus.NO_CONTENT) && getViewName() == null) {
                    return null;
                }
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAllObjects(RequestContextUtils.getInputFlashMap(request));
        if (getViewName() != null) {
            modelAndView.setViewName(viewName);
        } else {
            modelAndView.setView(getView());
        }
        if (isStatusOnly()) {
            return null;
        }
        return modelAndView;
    }
}
