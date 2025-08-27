package org.springframework.web.servlet.view.jasperreports;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsMultiFormatView.class */
public class JasperReportsMultiFormatView extends AbstractJasperReportsView {
    public static final String DEFAULT_FORMAT_KEY = "format";
    private String formatKey = "format";
    private Map<String, Class<? extends AbstractJasperReportsView>> formatMappings = new HashMap(4);
    private Properties contentDispositionMappings;

    public JasperReportsMultiFormatView() {
        this.formatMappings.put("csv", JasperReportsCsvView.class);
        this.formatMappings.put("html", JasperReportsHtmlView.class);
        this.formatMappings.put("pdf", JasperReportsPdfView.class);
        this.formatMappings.put("xls", JasperReportsXlsView.class);
        this.formatMappings.put("xlsx", JasperReportsXlsxView.class);
    }

    public void setFormatKey(String formatKey) {
        this.formatKey = formatKey;
    }

    public void setFormatMappings(Map<String, Class<? extends AbstractJasperReportsView>> formatMappings) {
        if (CollectionUtils.isEmpty(formatMappings)) {
            throw new IllegalArgumentException("'formatMappings' must not be empty");
        }
        this.formatMappings = formatMappings;
    }

    public void setContentDispositionMappings(Properties mappings) {
        this.contentDispositionMappings = mappings;
    }

    public Properties getContentDispositionMappings() {
        if (this.contentDispositionMappings == null) {
            this.contentDispositionMappings = new Properties();
        }
        return this.contentDispositionMappings;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView
    protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response) throws Exception {
        String format = (String) model.get(this.formatKey);
        if (format == null) {
            throw new IllegalArgumentException("No format found in model");
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Rendering report using format mapping key [" + format + "]");
        }
        Class<? extends AbstractJasperReportsView> viewClass = this.formatMappings.get(format);
        if (viewClass == null) {
            throw new IllegalArgumentException("Format discriminator [" + format + "] is not a configured mapping");
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Rendering report using view class [" + viewClass.getName() + "]");
        }
        AbstractJasperReportsView view = (AbstractJasperReportsView) BeanUtils.instantiateClass(viewClass);
        view.setExporterParameters(getExporterParameters());
        view.setConvertedExporterParameters(getConvertedExporterParameters());
        populateContentDispositionIfNecessary(response, format);
        view.renderReport(populatedReport, model, response);
    }

    private void populateContentDispositionIfNecessary(HttpServletResponse response, String format) {
        String header;
        if (this.contentDispositionMappings != null && (header = this.contentDispositionMappings.getProperty(format)) != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Setting Content-Disposition header to: [" + header + "]");
            }
            response.setHeader("Content-Disposition", header);
        }
    }
}
