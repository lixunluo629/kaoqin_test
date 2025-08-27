package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsCsvView.class */
public class JasperReportsCsvView extends AbstractJasperReportsSingleFormatView {
    public JasperReportsCsvView() {
        setContentType("text/csv");
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return new JRCsvExporter();
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return true;
    }
}
