package org.springframework.web.servlet.view.velocity;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/velocity/VelocityLayoutViewResolver.class */
public class VelocityLayoutViewResolver extends VelocityViewResolver {
    private String layoutUrl;
    private String layoutKey;
    private String screenContentKey;

    @Override // org.springframework.web.servlet.view.velocity.VelocityViewResolver, org.springframework.web.servlet.view.AbstractTemplateViewResolver, org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return VelocityLayoutView.class;
    }

    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    public void setLayoutKey(String layoutKey) {
        this.layoutKey = layoutKey;
    }

    public void setScreenContentKey(String screenContentKey) {
        this.screenContentKey = screenContentKey;
    }

    @Override // org.springframework.web.servlet.view.velocity.VelocityViewResolver, org.springframework.web.servlet.view.AbstractTemplateViewResolver, org.springframework.web.servlet.view.UrlBasedViewResolver
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        VelocityLayoutView view = (VelocityLayoutView) super.buildView(viewName);
        if (this.layoutUrl != null) {
            view.setLayoutUrl(this.layoutUrl);
        }
        if (this.layoutKey != null) {
            view.setLayoutKey(this.layoutKey);
        }
        if (this.screenContentKey != null) {
            view.setScreenContentKey(this.screenContentKey);
        }
        return view;
    }
}
