package org.springframework.web.servlet.view.document;

import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/document/AbstractExcelView.class */
public abstract class AbstractExcelView extends AbstractView {
    private static final String CONTENT_TYPE = "application/vnd.ms-excel";
    private static final String EXTENSION = ".xls";
    private String url;

    protected abstract void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hSSFWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public AbstractExcelView() {
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
        HSSFWorkbook workbook;
        if (this.url != null) {
            workbook = getTemplateSource(this.url, request);
        } else {
            workbook = new HSSFWorkbook();
            this.logger.debug("Created Excel Workbook from scratch");
        }
        buildExcelDocument(model, workbook, request, response);
        response.setContentType(getContentType());
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    protected HSSFWorkbook getTemplateSource(String url, HttpServletRequest request) throws Exception {
        LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
        Locale userLocale = RequestContextUtils.getLocale(request);
        Resource inputFile = helper.findLocalizedResource(url, EXTENSION, userLocale);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loading Excel workbook from " + inputFile);
        }
        return new HSSFWorkbook(inputFile.getInputStream());
    }

    protected HSSFCell getCell(HSSFSheet sheet, int row, int col) {
        HSSFRow sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        HSSFCell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        return cell;
    }

    protected void setText(HSSFCell cell, String text) {
        cell.setCellType(1);
        cell.setCellValue(text);
    }
}
