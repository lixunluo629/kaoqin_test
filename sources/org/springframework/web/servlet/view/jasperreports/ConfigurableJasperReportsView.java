package org.springframework.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/ConfigurableJasperReportsView.class */
public class ConfigurableJasperReportsView extends AbstractJasperReportsSingleFormatView {
    private Class<? extends JRExporter> exporterClass;
    private boolean useWriter = true;

    public void setExporterClass(Class<? extends JRExporter> exporterClass) {
        Assert.isAssignable(JRExporter.class, exporterClass);
        this.exporterClass = exporterClass;
    }

    public void setUseWriter(boolean useWriter) {
        this.useWriter = useWriter;
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView
    protected void onInit() {
        if (this.exporterClass == null) {
            throw new IllegalArgumentException("exporterClass is required");
        }
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected JRExporter createExporter() {
        return (JRExporter) BeanUtils.instantiateClass(this.exporterClass);
    }

    @Override // org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
    protected boolean useWriter() {
        return this.useWriter;
    }
}
