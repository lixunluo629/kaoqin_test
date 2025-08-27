package ch.qos.logback.classic.selector.servlet;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextJNDISelector;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.LoggerFactory;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/selector/servlet/LoggerContextFilter.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/selector/servlet/LoggerContextFilter.class */
public class LoggerContextFilter implements Filter {
    @Override // javax.servlet.Filter
    public void destroy() {
    }

    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
        ContextJNDISelector sel = null;
        if (selector instanceof ContextJNDISelector) {
            sel = (ContextJNDISelector) selector;
            sel.setLocalContext(lc);
        }
        try {
            chain.doFilter(request, response);
            if (sel != null) {
                sel.removeLocalContext();
            }
        } catch (Throwable th) {
            if (sel != null) {
                sel.removeLocalContext();
            }
            throw th;
        }
    }

    @Override // javax.servlet.Filter
    public void init(FilterConfig arg0) throws ServletException {
    }
}
