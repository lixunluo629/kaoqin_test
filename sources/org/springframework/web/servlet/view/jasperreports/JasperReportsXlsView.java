package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsXlsView.class */
public class JasperReportsXlsView extends AbstractJasperReportsSingleFormatView {
    public JasperReportsXlsView() {
        setContentType("application/vnd.ms-excel");
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return new JRXlsExporter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return false;
    }
}
