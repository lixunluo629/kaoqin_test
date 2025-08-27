package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.apache.poi.ss.util.CellUtil;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/HiddenInputTag.class */
public class HiddenInputTag extends AbstractHtmlElementTag {
    public static final String DISABLED_ATTRIBUTE = "disabled";
    private boolean disabled;

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return !"type".equals(localName);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("input");
        writeDefaultAttributes(tagWriter);
        tagWriter.writeAttribute("type", CellUtil.HIDDEN);
        if (isDisabled()) {
            tagWriter.writeAttribute("disabled", "disabled");
        }
        String value = getDisplayString(getBoundValue(), getPropertyEditor());
        tagWriter.writeAttribute("value", processFieldValue(getName(), value, CellUtil.HIDDEN));
        tagWriter.endTag();
        return 0;
    }
}
