package org.springframework.web.servlet.view.jasperreports;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/AbstractJasperReportsSingleFormatView.class */
public abstract class AbstractJasperReportsSingleFormatView extends AbstractJasperReportsView {
    protected abstract JRExporter createExporter();

    protected abstract boolean useWriter();

    @Override // org.springframework.web.servlet.view.AbstractView
    protected boolean generatesDownloadContent() {
        return !useWriter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView
    protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response) throws Exception {
        JRExporter exporter = createExporter();
        Map<JRExporterParameter, Object> mergedExporterParameters = getConvertedExporterParameters();
        if (!CollectionUtils.isEmpty((Map<?, ?>) mergedExporterParameters)) {
            exporter.setParameters(mergedExporterParameters);
        }
        if (useWriter()) {
            renderReportUsingWriter(exporter, populatedReport, response);
        } else {
            renderReportUsingOutputStream(exporter, populatedReport, response);
        }
    }

    protected void renderReportUsingWriter(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response) throws Exception {
        String contentType = getContentType();
        String encoding = (String) exporter.getParameter(JRExporterParameter.CHARACTER_ENCODING);
        if (encoding != null && contentType != null && !contentType.toLowerCase().contains(WebUtils.CONTENT_TYPE_CHARSET_PREFIX)) {
            contentType = contentType + WebUtils.CONTENT_TYPE_CHARSET_PREFIX + encoding;
        }
        response.setContentType(contentType);
        JasperReportsUtils.render(exporter, populatedReport, response.getWriter());
    }

    protected void renderReportUsingOutputStream(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        JasperReportsUtils.render(exporter, populatedReport, baos);
        writeToResponse(response, baos);
    }
}
