package org.springframework.web.servlet.view.tiles2;

import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/TilesView.class */
public class TilesView extends AbstractUrlBasedView {
    private boolean alwaysInclude = false;

    public void setAlwaysInclude(boolean alwaysInclude) {
        this.alwaysInclude = alwaysInclude;
    }

    @Override // org.springframework.web.servlet.view.AbstractUrlBasedView
    public boolean checkResource(final Locale locale) throws Exception {
        BasicTilesContainer container = ServletUtil.getContainer(getServletContext());
        if (!(container instanceof BasicTilesContainer)) {
            return true;
        }
        BasicTilesContainer basicContainer = container;
        return basicContainer.getDefinitionsFactory().getDefinition(getUrl(), new ServletTilesRequestContext(new ServletTilesApplicationContext(getServletContext()), null, null) { // from class: org.springframework.web.servlet.view.tiles2.TilesView.1
            public Locale getRequestLocale() {
                return locale;
            }
        }) != null;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletContext servletContext = getServletContext();
        TilesContainer container = ServletUtil.getContainer(servletContext);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. Have you added a TilesConfigurer to your web application context?");
        }
        exposeModelAsRequestAttributes(model, request);
        JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));
        if (this.alwaysInclude) {
            ServletUtil.setForceInclude(request, true);
        }
        container.render(getUrl(), new Object[]{request, response});
    }
}
