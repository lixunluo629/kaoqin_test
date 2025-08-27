package org.springframework.web.servlet.view.freemarker;

import freemarker.core.ParseException;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hyperic.sigar.win32.EventLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractTemplateView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerView.class */
public class FreeMarkerView extends AbstractTemplateView {
    private String encoding;
    private Configuration configuration;
    private TaglibFactory taglibFactory;
    private ServletContextHashModel servletContextHashModel;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    protected String getEncoding() {
        return this.encoding;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    protected Configuration getConfiguration() {
        return this.configuration;
    }

    @Override // org.springframework.web.context.support.WebApplicationObjectSupport
    protected void initServletContext(ServletContext servletContext) throws BeansException {
        if (getConfiguration() != null) {
            this.taglibFactory = new TaglibFactory(servletContext);
        } else {
            FreeMarkerConfig config = autodetectConfiguration();
            setConfiguration(config.getConfiguration());
            this.taglibFactory = config.getTaglibFactory();
        }
        GenericServlet servlet = new GenericServletAdapter();
        try {
            servlet.init(new DelegatingServletConfig());
            this.servletContextHashModel = new ServletContextHashModel(servlet, getObjectWrapper());
        } catch (ServletException ex) {
            throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
        }
    }

    protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
        try {
            return (FreeMarkerConfig) BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), FreeMarkerConfig.class, true, false);
        } catch (NoSuchBeanDefinitionException ex) {
            throw new ApplicationContextException("Must define a single FreeMarkerConfig bean in this web application context (may be inherited): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.", ex);
        }
    }

    protected ObjectWrapper getObjectWrapper() {
        ObjectWrapper ow = getConfiguration().getObjectWrapper();
        return ow != null ? ow : new DefaultObjectWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
    }

    @Override // org.springframework.web.servlet.view.AbstractUrlBasedView
    public boolean checkResource(Locale locale) throws Exception {
        String url = getUrl();
        try {
            getTemplate(url, locale);
            return true;
        } catch (FileNotFoundException e) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No FreeMarker view found for URL: " + url);
                return false;
            }
            return false;
        } catch (IOException ex) {
            throw new ApplicationContextException("Could not load FreeMarker template for URL [" + url + "]", ex);
        } catch (ParseException ex2) {
            throw new ApplicationContextException("Failed to parse FreeMarker template for URL [" + url + "]", ex2);
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractTemplateView
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeHelpers(model, request);
        doRender(model, request, response);
    }

    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
    }

    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeModelAsRequestAttributes(model, request);
        SimpleHash fmModel = buildTemplateModel(model, request, response);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
        }
        Locale locale = RequestContextUtils.getLocale(request);
        processTemplate(getTemplate(locale), fmModel, response);
    }

    protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
        fmModel.put("JspTaglibs", this.taglibFactory);
        fmModel.put(EventLog.APPLICATION, this.servletContextHashModel);
        fmModel.put(RtspHeaders.Names.SESSION, buildSessionModel(request, response));
        fmModel.put("Request", new HttpRequestHashModel(request, response, getObjectWrapper()));
        fmModel.put("RequestParameters", new HttpRequestParametersHashModel(request));
        fmModel.putAll(model);
        return fmModel;
    }

    private HttpSessionHashModel buildSessionModel(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return new HttpSessionHashModel(session, getObjectWrapper());
        }
        return new HttpSessionHashModel((FreemarkerServlet) null, request, response, getObjectWrapper());
    }

    protected Template getTemplate(Locale locale) throws IOException {
        return getTemplate(getUrl(), locale);
    }

    protected Template getTemplate(String name, Locale locale) throws IOException {
        if (getEncoding() != null) {
            return getConfiguration().getTemplate(name, locale, getEncoding());
        }
        return getConfiguration().getTemplate(name, locale);
    }

    protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response) throws TemplateException, IOException {
        template.process(model, response.getWriter());
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerView$GenericServletAdapter.class */
    private static class GenericServletAdapter extends GenericServlet {
        private GenericServletAdapter() {
        }

        @Override // javax.servlet.GenericServlet, javax.servlet.Servlet
        public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerView$DelegatingServletConfig.class */
    private class DelegatingServletConfig implements ServletConfig {
        private DelegatingServletConfig() {
        }

        @Override // javax.servlet.ServletConfig
        public String getServletName() {
            return FreeMarkerView.this.getBeanName();
        }

        @Override // javax.servlet.ServletConfig
        public ServletContext getServletContext() {
            return FreeMarkerView.this.getServletContext();
        }

        @Override // javax.servlet.ServletConfig
        public String getInitParameter(String paramName) {
            return null;
        }

        @Override // javax.servlet.ServletConfig
        public Enumeration<String> getInitParameterNames() {
            return Collections.enumeration(Collections.emptySet());
        }
    }
}
