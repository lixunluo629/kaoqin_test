package org.springframework.web.servlet.view.tiles2;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesViewResolver.class */
public class TilesViewResolver extends UrlBasedViewResolver {
    private Boolean alwaysInclude;

    public TilesViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return TilesView.class;
    }

    public void setAlwaysInclude(Boolean alwaysInclude) {
        this.alwaysInclude = alwaysInclude;
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        TilesView view = (TilesView) super.buildView(viewName);
        if (this.alwaysInclude != null) {
            view.setAlwaysInclude(this.alwaysInclude.booleanValue());
        }
        return view;
    }
}
