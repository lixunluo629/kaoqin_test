package org.apache.poi.xwpf.usermodel;

import ch.qos.logback.classic.net.SyslogAppender;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFSDTContentCell.class */
public class XWPFSDTContentCell implements ISDTContent {
    private String text;

    public XWPFSDTContentCell(CTSdtContentCell sdtContentCell, XWPFTableRow xwpfTableRow, IBody part) {
        this.text = "";
        if (sdtContentCell == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        XmlCursor cursor = sdtContentCell.newCursor();
        int tcCnt = 0;
        int iBodyCnt = 0;
        int depth = 1;
        while (cursor.hasNextToken() && depth > 0) {
            XmlCursor.TokenType t = cursor.toNextToken();
            if (t.isText()) {
                sb.append(cursor.getTextValue());
            } else if (isStartToken(cursor, "tr")) {
                tcCnt = 0;
                iBodyCnt = 0;
            } else if (isStartToken(cursor, "tc")) {
                int i = tcCnt;
                tcCnt++;
                if (i > 0) {
                    sb.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                }
                iBodyCnt = 0;
            } else if (isStartToken(cursor, "p") || isStartToken(cursor, "tbl") || isStartToken(cursor, "sdt")) {
                if (iBodyCnt > 0) {
                    sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                iBodyCnt++;
            }
            if (cursor.isStart()) {
                depth++;
            } else if (cursor.isEnd()) {
                depth--;
            }
        }
        this.text = sb.toString();
        cursor.dispose();
    }

    private boolean isStartToken(XmlCursor cursor, String string) {
        QName qName;
        if (cursor.isStart() && (qName = cursor.getName()) != null && qName.getLocalPart() != null && qName.getLocalPart().equals(string)) {
            return true;
        }
        return false;
    }

    @Override // org.apache.poi.xwpf.usermodel.ISDTContent
    public String getText() {
        return this.text;
    }

    @Override // org.apache.poi.xwpf.usermodel.ISDTContent
    public String toString() {
        return getText();
    }
}
