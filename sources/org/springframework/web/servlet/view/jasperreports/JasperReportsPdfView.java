package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.http.MediaType;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsPdfView.class */
public class JasperReportsPdfView extends AbstractJasperReportsSingleFormatView {
    public JasperReportsPdfView() {
        setContentType(MediaType.APPLICATION_PDF_VALUE);
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return new JRPdfExporter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return false;
    }
}
