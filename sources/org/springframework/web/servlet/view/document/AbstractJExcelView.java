package org.springframework.web.servlet.view.document;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/document/AbstractJExcelView.class */
public abstract class AbstractJExcelView extends AbstractView {
    private static final String CONTENT_TYPE = "application/vnd.ms-excel";
    private static final String EXTENSION = ".xls";
    private String url;

    protected abstract void buildExcelDocument(Map<String, Object> map, WritableWorkbook writableWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public AbstractJExcelView() {
        setContentType(CONTENT_TYPE);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WritableWorkbook workbook;
        response.setContentType(getContentType());
        OutputStream out = response.getOutputStream();
        if (this.url != null) {
            Workbook template = getTemplateSource(this.url, request);
            workbook = Workbook.createWorkbook(out, template);
        } else {
            this.logger.debug("Creating Excel Workbook from scratch");
            workbook = Workbook.createWorkbook(out);
        }
        buildExcelDocument(model, workbook, request, response);
        workbook.write();
        out.flush();
        workbook.close();
    }

    protected Workbook getTemplateSource(String url, HttpServletRequest request) throws Exception {
        LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
        Locale userLocale = RequestContextUtils.getLocale(request);
        Resource inputFile = helper.findLocalizedResource(url, EXTENSION, userLocale);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loading Excel workbook from " + inputFile);
        }
        return Workbook.getWorkbook(inputFile.getInputStream());
    }
}
