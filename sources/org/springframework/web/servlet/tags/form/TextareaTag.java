package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/TextareaTag.class */
public class TextareaTag extends AbstractHtmlInputElementTag {
    public static final String ROWS_ATTRIBUTE = "rows";
    public static final String COLS_ATTRIBUTE = "cols";
    public static final String ONSELECT_ATTRIBUTE = "onselect";

    @Deprecated
    public static final String READONLY_ATTRIBUTE = "readonly";
    private String rows;
    private String cols;
    private String onselect;

    public void setRows(String rows) {
        this.rows = rows;
    }

    protected String getRows() {
        return this.rows;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    protected String getCols() {
        return this.cols;
    }

    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }

    protected String getOnselect() {
        return this.onselect;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("textarea");
        writeDefaultAttributes(tagWriter);
        writeOptionalAttribute(tagWriter, ROWS_ATTRIBUTE, getRows());
        writeOptionalAttribute(tagWriter, COLS_ATTRIBUTE, getCols());
        writeOptionalAttribute(tagWriter, "onselect", getOnselect());
        String value = getDisplayString(getBoundValue(), getPropertyEditor());
        tagWriter.appendValue("\r\n" + processFieldValue(getName(), value, "textarea"));
        tagWriter.endTag();
        return 0;
    }
}
