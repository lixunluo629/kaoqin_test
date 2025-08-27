package org.springframework.web.servlet.view;

import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/AbstractUrlBasedView.class */
public abstract class AbstractUrlBasedView extends AbstractView implements InitializingBean {
    private String url;

    protected AbstractUrlBasedView() {
    }

    protected AbstractUrlBasedView(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (isUrlRequired() && getUrl() == null) {
            throw new IllegalArgumentException("Property 'url' is required");
        }
    }

    protected boolean isUrlRequired() {
        return true;
    }

    public boolean checkResource(Locale locale) throws Exception {
        return true;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    public String toString() {
        return super.toString() + "; URL [" + getUrl() + "]";
    }
}
