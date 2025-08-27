package springfox.documentation.spring.web.paths;

import com.google.common.base.Strings;
import javax.servlet.ServletContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/RelativePathProvider.class */
public class RelativePathProvider extends AbstractPathProvider {
    public static final String ROOT = "/";
    private final ServletContext servletContext;

    public RelativePathProvider(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // springfox.documentation.spring.web.paths.AbstractPathProvider
    protected String applicationPath() {
        return Strings.isNullOrEmpty(this.servletContext.getContextPath()) ? "/" : this.servletContext.getContextPath();
    }

    @Override // springfox.documentation.spring.web.paths.AbstractPathProvider
    protected String getDocumentationPath() {
        return "/";
    }
}
