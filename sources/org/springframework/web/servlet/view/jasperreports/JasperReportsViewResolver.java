package org.springframework.web.servlet.view.jasperreports;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/JasperReportsViewResolver.class */
public class JasperReportsViewResolver extends UrlBasedViewResolver {
    private String reportDataKey;
    private Properties subReportUrls;
    private String[] subReportDataKeys;
    private Properties headers;
    private Map<String, Object> exporterParameters = new HashMap();
    private DataSource jdbcDataSource;

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return AbstractJasperReportsView.class;
    }

    public void setReportDataKey(String reportDataKey) {
        this.reportDataKey = reportDataKey;
    }

    public void setSubReportUrls(Properties subReportUrls) {
        this.subReportUrls = subReportUrls;
    }

    public void setSubReportDataKeys(String... subReportDataKeys) {
        this.subReportDataKeys = subReportDataKeys;
    }

    public void setHeaders(Properties headers) {
        this.headers = headers;
    }

    public void setExporterParameters(Map<String, Object> exporterParameters) {
        this.exporterParameters = exporterParameters;
    }

    public void setJdbcDataSource(DataSource jdbcDataSource) {
        this.jdbcDataSource = jdbcDataSource;
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractJasperReportsView view = (AbstractJasperReportsView) super.buildView(viewName);
        view.setReportDataKey(this.reportDataKey);
        view.setSubReportUrls(this.subReportUrls);
        view.setSubReportDataKeys(this.subReportDataKeys);
        view.setHeaders(this.headers);
        view.setExporterParameters(this.exporterParameters);
        view.setJdbcDataSource(this.jdbcDataSource);
        return view;
    }
}
