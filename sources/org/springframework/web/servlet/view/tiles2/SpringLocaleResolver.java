package org.springframework.web.servlet.view.tiles2;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.jsp.context.JspTilesRequestContext;
import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/SpringLocaleResolver.class */
public class SpringLocaleResolver extends DefaultLocaleResolver {
    public Locale resolveLocale(TilesRequestContext context) {
        HttpServletRequest request;
        if (context instanceof JspTilesRequestContext) {
            PageContext pc = ((JspTilesRequestContext) context).getPageContext();
            return RequestContextUtils.getLocale((HttpServletRequest) pc.getRequest());
        }
        if ((context instanceof ServletTilesRequestContext) && (request = ((ServletTilesRequestContext) context).getRequest()) != null) {
            return RequestContextUtils.getLocale(request);
        }
        return super.resolveLocale(context);
    }
}
