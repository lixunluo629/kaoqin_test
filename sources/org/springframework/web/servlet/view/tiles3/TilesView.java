package org.springframework.web.servlet.view.tiles3;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.renderer.DefinitionRenderer;
import org.apache.tiles.request.AbstractRequest;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles3/TilesView.class */
public class TilesView extends AbstractUrlBasedView {
    private Renderer renderer;
    private boolean exposeJstlAttributes = true;
    private boolean alwaysInclude = false;
    private ApplicationContext applicationContext;

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    protected void setExposeJstlAttributes(boolean exposeJstlAttributes) {
        this.exposeJstlAttributes = exposeJstlAttributes;
    }

    public void setAlwaysInclude(boolean alwaysInclude) {
        this.alwaysInclude = alwaysInclude;
    }

    @Override // org.springframework.web.servlet.view.AbstractUrlBasedView, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.applicationContext = ServletUtil.getApplicationContext(getServletContext());
        if (this.renderer == null) {
            TilesContainer container = TilesAccess.getContainer(this.applicationContext);
            this.renderer = new DefinitionRenderer(container);
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractUrlBasedView
    public boolean checkResource(final Locale locale) throws Exception {
        HttpServletRequest servletRequest = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return this.renderer.isRenderable(getUrl(), new ServletRequest(this.applicationContext, servletRequest, null) { // from class: org.springframework.web.servlet.view.tiles3.TilesView.1
            public Locale getRequestLocale() {
                return locale;
            }
        });
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeModelAsRequestAttributes(model, request);
        if (this.exposeJstlAttributes) {
            JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
        }
        if (this.alwaysInclude) {
            request.setAttribute(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME, true);
        }
        Request tilesRequest = createTilesRequest(request, response);
        this.renderer.render(getUrl(), tilesRequest);
    }

    protected Request createTilesRequest(final HttpServletRequest request, HttpServletResponse response) {
        return new ServletRequest(this.applicationContext, request, response) { // from class: org.springframework.web.servlet.view.tiles3.TilesView.2
            public Locale getRequestLocale() {
                return RequestContextUtils.getLocale(request);
            }
        };
    }
}
