package org.springframework.web.method.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/support/ModelAndViewContainer.class */
public class ModelAndViewContainer {
    private Object view;
    private ModelMap redirectModel;
    private HttpStatus status;
    private boolean ignoreDefaultModelOnRedirect = false;
    private final ModelMap defaultModel = new BindingAwareModelMap();
    private boolean redirectModelScenario = false;
    private final Set<String> noBinding = new HashSet(4);
    private final Set<String> bindingDisabled = new HashSet(4);
    private final SessionStatus sessionStatus = new SimpleSessionStatus();
    private boolean requestHandled = false;

    public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
        this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
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

    public void setView(Object view) {
        this.view = view;
    }

    public Object getView() {
        return this.view;
    }

    public boolean isViewReference() {
        return this.view instanceof String;
    }

    public ModelMap getModel() {
        if (useDefaultModel()) {
            return this.defaultModel;
        }
        if (this.redirectModel == null) {
            this.redirectModel = new ModelMap();
        }
        return this.redirectModel;
    }

    private boolean useDefaultModel() {
        return !this.redirectModelScenario || (this.redirectModel == null && !this.ignoreDefaultModelOnRedirect);
    }

    public ModelMap getDefaultModel() {
        return this.defaultModel;
    }

    public void setRedirectModel(ModelMap redirectModel) {
        this.redirectModel = redirectModel;
    }

    public void setRedirectModelScenario(boolean redirectModelScenario) {
        this.redirectModelScenario = redirectModelScenario;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public void setBindingDisabled(String attributeName) {
        this.bindingDisabled.add(attributeName);
    }

    public boolean isBindingDisabled(String name) {
        return this.bindingDisabled.contains(name) || this.noBinding.contains(name);
    }

    public void setBinding(String attributeName, boolean enabled) {
        if (!enabled) {
            this.noBinding.add(attributeName);
        } else {
            this.noBinding.remove(attributeName);
        }
    }

    public SessionStatus getSessionStatus() {
        return this.sessionStatus;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }

    public boolean isRequestHandled() {
        return this.requestHandled;
    }

    public ModelAndViewContainer addAttribute(String name, Object value) {
        getModel().addAttribute(name, value);
        return this;
    }

    public ModelAndViewContainer addAttribute(Object value) {
        getModel().addAttribute(value);
        return this;
    }

    public ModelAndViewContainer addAllAttributes(Map<String, ?> attributes) {
        getModel().addAllAttributes(attributes);
        return this;
    }

    public ModelAndViewContainer mergeAttributes(Map<String, ?> attributes) {
        getModel().mergeAttributes(attributes);
        return this;
    }

    public ModelAndViewContainer removeAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (String key : attributes.keySet()) {
                getModel().remove(key);
            }
        }
        return this;
    }

    public boolean containsAttribute(String name) {
        return getModel().containsAttribute(name);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ModelAndViewContainer: ");
        if (!isRequestHandled()) {
            if (isViewReference()) {
                sb.append("reference to view with name '").append(this.view).append("'");
            } else {
                sb.append("View is [").append(this.view).append(']');
            }
            if (useDefaultModel()) {
                sb.append("; default model ");
            } else {
                sb.append("; redirect model ");
            }
            sb.append(getModel());
        } else {
            sb.append("Request handled directly");
        }
        return sb.toString();
    }
}
