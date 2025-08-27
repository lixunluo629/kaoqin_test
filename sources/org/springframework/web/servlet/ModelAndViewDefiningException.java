package org.springframework.web.servlet;

import javax.servlet.ServletException;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/ModelAndViewDefiningException.class */
public class ModelAndViewDefiningException extends ServletException {
    private ModelAndView modelAndView;

    public ModelAndViewDefiningException(ModelAndView modelAndView) {
        Assert.notNull(modelAndView, "ModelAndView must not be null in ModelAndViewDefiningException");
        this.modelAndView = modelAndView;
    }

    public ModelAndView getModelAndView() {
        return this.modelAndView;
    }
}
