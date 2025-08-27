package org.springframework.web.servlet.view.jasperreports;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.core.io.Resource;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/jasperreports/AbstractJasperReportsView.class */
public abstract class AbstractJasperReportsView extends AbstractUrlBasedView {
    protected static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    protected static final String CONTENT_DISPOSITION_INLINE = "inline";
    private String reportDataKey;
    private Properties subReportUrls;
    private String[] subReportDataKeys;
    private Properties headers;
    private Map<?, ?> exporterParameters = new HashMap();
    private Map<JRExporterParameter, Object> convertedExporterParameters;
    private DataSource jdbcDataSource;
    private JasperReport report;
    private Map<String, JasperReport> subReports;

    protected abstract void renderReport(JasperPrint jasperPrint, Map<String, Object> map, HttpServletResponse httpServletResponse) throws Exception;

    public void setReportDataKey(String reportDataKey) {
        this.reportDataKey = reportDataKey;
    }

    public void setSubReportUrls(Properties subReports) {
        this.subReportUrls = subReports;
    }

    public void setSubReportDataKeys(String... subReportDataKeys) {
        this.subReportDataKeys = subReportDataKeys;
    }

    public void setHeaders(Properties headers) {
        this.headers = headers;
    }

    public void setExporterParameters(Map<?, ?> parameters) {
        this.exporterParameters = parameters;
    }

    public Map<?, ?> getExporterParameters() {
        return this.exporterParameters;
    }

    protected void setConvertedExporterParameters(Map<JRExporterParameter, Object> parameters) {
        this.convertedExporterParameters = parameters;
    }

    protected Map<JRExporterParameter, Object> getConvertedExporterParameters() {
        return this.convertedExporterParameters;
    }

    public void setJdbcDataSource(DataSource jdbcDataSource) {
        this.jdbcDataSource = jdbcDataSource;
    }

    protected DataSource getJdbcDataSource() {
        return this.jdbcDataSource;
    }

    @Override // org.springframework.web.servlet.view.AbstractUrlBasedView
    protected boolean isUrlRequired() {
        return false;
    }

    @Override // org.springframework.context.support.ApplicationObjectSupport
    protected final void initApplicationContext() throws ApplicationContextException {
        this.report = loadReport();
        if (this.subReportUrls != null) {
            if (this.subReportDataKeys != null && this.subReportDataKeys.length > 0 && this.reportDataKey == null) {
                throw new ApplicationContextException("'reportDataKey' for main report is required when specifying a value for 'subReportDataKeys'");
            }
            this.subReports = new HashMap(this.subReportUrls.size());
            Enumeration<?> urls = this.subReportUrls.propertyNames();
            while (urls.hasMoreElements()) {
                String key = (String) urls.nextElement();
                String path = this.subReportUrls.getProperty(key);
                Resource resource = getApplicationContext().getResource(path);
                this.subReports.put(key, loadReport(resource));
            }
        }
        convertExporterParameters();
        if (this.headers == null) {
            this.headers = new Properties();
        }
        if (!this.headers.containsKey("Content-Disposition")) {
            this.headers.setProperty("Content-Disposition", CONTENT_DISPOSITION_INLINE);
        }
        onInit();
    }

    protected void onInit() {
    }

    protected final void convertExporterParameters() {
        if (!CollectionUtils.isEmpty(this.exporterParameters)) {
            this.convertedExporterParameters = new HashMap(this.exporterParameters.size());
            for (Map.Entry<?, ?> entry : this.exporterParameters.entrySet()) {
                JRExporterParameter exporterParameter = getExporterParameter(entry.getKey());
                this.convertedExporterParameters.put(exporterParameter, convertParameterValue(exporterParameter, entry.getValue()));
            }
        }
    }

    protected Object convertParameterValue(JRExporterParameter parameter, Object value) {
        if (value instanceof String) {
            String str = (String) value;
            if ("true".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equals(str)) {
                return Boolean.FALSE;
            }
            if (str.length() > 0 && Character.isDigit(str.charAt(0))) {
                try {
                    return Integer.valueOf(str);
                } catch (NumberFormatException e) {
                    return str;
                }
            }
        }
        return value;
    }

    protected JRExporterParameter getExporterParameter(Object parameter) {
        if (parameter instanceof JRExporterParameter) {
            return (JRExporterParameter) parameter;
        }
        if (parameter instanceof String) {
            return convertToExporterParameter((String) parameter);
        }
        throw new IllegalArgumentException("Parameter [" + parameter + "] is invalid type. Should be either String or JRExporterParameter.");
    }

    protected JRExporterParameter convertToExporterParameter(String fqFieldName) throws LinkageError, NoSuchFieldException {
        int index = fqFieldName.lastIndexOf(46);
        if (index == -1 || index == fqFieldName.length()) {
            throw new IllegalArgumentException("Parameter name [" + fqFieldName + "] is not a valid static field. The parameter name must map to a static field such as [net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI]");
        }
        String className = fqFieldName.substring(0, index);
        String fieldName = fqFieldName.substring(index + 1);
        try {
            Class<?> cls = ClassUtils.forName(className, getApplicationContext().getClassLoader());
            Field field = cls.getField(fieldName);
            if (JRExporterParameter.class.isAssignableFrom(field.getType())) {
                try {
                    return (JRExporterParameter) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Unable to access field [" + fieldName + "] of class [" + className + "]. Check that it is static and accessible.");
                }
            }
            throw new IllegalArgumentException("Field [" + fieldName + "] on class [" + className + "] is not assignable from JRExporterParameter - check the type of this field.");
        } catch (ClassNotFoundException e2) {
            throw new IllegalArgumentException("Class [" + className + "] in key [" + fqFieldName + "] could not be found.");
        } catch (NoSuchFieldException e3) {
            throw new IllegalArgumentException("Field [" + fieldName + "] in key [" + fqFieldName + "] could not be found on class [" + className + "].");
        }
    }

    protected JasperReport loadReport() {
        String url = getUrl();
        if (url == null) {
            return null;
        }
        Resource mainReport = getApplicationContext().getResource(url);
        return loadReport(mainReport);
    }

    protected final JasperReport loadReport(Resource resource) throws IOException {
        InputStream is;
        try {
            String filename = resource.getFilename();
            if (filename != null) {
                if (filename.endsWith(".jasper")) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Loading pre-compiled Jasper Report from " + resource);
                    }
                    is = resource.getInputStream();
                    try {
                        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
                        is.close();
                        return jasperReport;
                    } finally {
                    }
                }
                if (filename.endsWith(".jrxml")) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Compiling Jasper Report loaded from " + resource);
                    }
                    is = resource.getInputStream();
                    try {
                        JasperDesign design = JRXmlLoader.load(is);
                        JasperReport jasperReportCompileReport = JasperCompileManager.compileReport(design);
                        is.close();
                        return jasperReportCompileReport;
                    } finally {
                    }
                }
            }
            throw new IllegalArgumentException("Report filename [" + filename + "] must end in either .jasper or .jrxml");
        } catch (JRException ex) {
            throw new ApplicationContextException("Could not parse JasperReports report from " + resource, ex);
        } catch (IOException ex2) {
            throw new ApplicationContextException("Could not load JasperReports report from " + resource, ex2);
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.subReports != null) {
            model.putAll(this.subReports);
            if (this.subReportDataKeys != null) {
                for (String key : this.subReportDataKeys) {
                    model.put(key, convertReportData(model.get(key)));
                }
            }
        }
        exposeLocalizationContext(model, request);
        JasperPrint filledReport = fillReport(model);
        postProcessReport(filledReport, model);
        populateHeaders(response);
        renderReport(filledReport, model, response);
    }

    protected void exposeLocalizationContext(Map<String, Object> model, HttpServletRequest request) {
        RequestContext rc = new RequestContext(request, getServletContext());
        Locale locale = rc.getLocale();
        if (!model.containsKey("REPORT_LOCALE")) {
            model.put("REPORT_LOCALE", locale);
        }
        TimeZone timeZone = rc.getTimeZone();
        if (timeZone != null && !model.containsKey("REPORT_TIME_ZONE")) {
            model.put("REPORT_TIME_ZONE", timeZone);
        }
        JasperReport report = getReport();
        if ((report == null || report.getResourceBundle() == null) && !model.containsKey("REPORT_RESOURCE_BUNDLE")) {
            model.put("REPORT_RESOURCE_BUNDLE", new MessageSourceResourceBundle(rc.getMessageSource(), locale));
        }
    }

    protected JasperPrint fillReport(Map<String, Object> model) throws Exception {
        JasperReport report = getReport();
        if (report == null) {
            throw new IllegalStateException("No main report defined for 'fillReport' - specify a 'url' on this view or override 'getReport()' or 'fillReport(Map)'");
        }
        JRDataSource jrDataSource = null;
        DataSource jdbcDataSourceToUse = null;
        if (this.reportDataKey != null) {
            Object reportDataValue = model.get(this.reportDataKey);
            if (reportDataValue instanceof DataSource) {
                jdbcDataSourceToUse = (DataSource) reportDataValue;
            } else {
                jrDataSource = convertReportData(reportDataValue);
            }
        } else {
            Collection<?> values = model.values();
            jrDataSource = (JRDataSource) CollectionUtils.findValueOfType(values, JRDataSource.class);
            if (jrDataSource == null) {
                JRDataSourceProvider provider = (JRDataSourceProvider) CollectionUtils.findValueOfType(values, JRDataSourceProvider.class);
                if (provider != null) {
                    jrDataSource = createReport(provider);
                } else {
                    jdbcDataSourceToUse = (DataSource) CollectionUtils.findValueOfType(values, DataSource.class);
                    if (jdbcDataSourceToUse == null) {
                        jdbcDataSourceToUse = this.jdbcDataSource;
                    }
                }
            }
        }
        if (jdbcDataSourceToUse != null) {
            return doFillReport(report, model, jdbcDataSourceToUse);
        }
        if (jrDataSource == null) {
            jrDataSource = getReportData(model);
        }
        if (jrDataSource != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Filling report with JRDataSource [" + jrDataSource + "]");
            }
            return JasperFillManager.fillReport(report, model, jrDataSource);
        }
        this.logger.debug("Filling report with plain model");
        return JasperFillManager.fillReport(report, model);
    }

    private JasperPrint doFillReport(JasperReport report, Map<String, Object> model, DataSource ds) throws Exception {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Filling report using JDBC DataSource [" + ds + "]");
        }
        Connection con = ds.getConnection();
        try {
            return JasperFillManager.fillReport(report, model, con);
        } finally {
            try {
                con.close();
            } catch (Throwable ex) {
                this.logger.debug("Could not close JDBC Connection", ex);
            }
        }
    }

    private void populateHeaders(HttpServletResponse response) {
        Enumeration<?> en = this.headers.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            response.addHeader(key, this.headers.getProperty(key));
        }
    }

    protected JasperReport getReport() {
        return this.report;
    }

    protected JRDataSource getReportData(Map<String, Object> model) {
        Object value = CollectionUtils.findValueOfType((Collection<?>) model.values(), getReportDataTypes());
        if (value != null) {
            return convertReportData(value);
        }
        return null;
    }

    protected JRDataSource convertReportData(Object value) throws IllegalArgumentException {
        if (value instanceof JRDataSourceProvider) {
            return createReport((JRDataSourceProvider) value);
        }
        return JasperReportsUtils.convertReportData(value);
    }

    protected JRDataSource createReport(JRDataSourceProvider provider) {
        try {
            JasperReport report = getReport();
            if (report == null) {
                throw new IllegalStateException("No main report defined for JRDataSourceProvider - specify a 'url' on this view or override 'getReport()'");
            }
            return provider.create(report);
        } catch (JRException ex) {
            throw new IllegalArgumentException("Supplied JRDataSourceProvider is invalid", ex);
        }
    }

    protected Class<?>[] getReportDataTypes() {
        return new Class[]{Collection.class, Object[].class};
    }

    protected void postProcessReport(JasperPrint populatedReport, Map<String, Object> model) throws Exception {
    }
}
