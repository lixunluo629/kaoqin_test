package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsXlsxView.class */
public class JasperReportsXlsxView extends AbstractJasperReportsSingleFormatView {
    public JasperReportsXlsxView() {
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return new JRXlsxExporter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return false;
    }
}
