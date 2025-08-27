package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsHtmlView.class */
public class JasperReportsHtmlView extends AbstractJasperReportsSingleFormatView {
    public JasperReportsHtmlView() {
        setContentType("text/html");
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return new JRHtmlExporter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return true;
    }
}
