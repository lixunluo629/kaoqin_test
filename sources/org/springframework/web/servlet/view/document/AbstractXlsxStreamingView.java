package org.springframework.web.servlet.view.document;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/document/AbstractXlsxStreamingView.class */
public abstract class AbstractXlsxStreamingView extends AbstractXlsxView {
    @Override // org.springframework.web.servlet.view.document.AbstractXlsxView, org.springframework.web.servlet.view.document.AbstractXlsView
    protected /* bridge */ /* synthetic */ Workbook createWorkbook(Map map, HttpServletRequest httpServletRequest) {
        return createWorkbook((Map<String, Object>) map, httpServletRequest);
    }

    @Override // org.springframework.web.servlet.view.document.AbstractXlsxView, org.springframework.web.servlet.view.document.AbstractXlsView
    protected SXSSFWorkbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
        return new SXSSFWorkbook();
    }

    @Override // org.springframework.web.servlet.view.document.AbstractXlsView
    protected void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
        super.renderWorkbook(workbook, response);
        ((SXSSFWorkbook) workbook).dispose();
    }
}
