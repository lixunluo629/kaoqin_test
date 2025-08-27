package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/CheckboxesTag.class */
public class CheckboxesTag extends AbstractMultiCheckedElementTag {
    @Override // org.springframework.web.servlet.tags.form.AbstractMultiCheckedElementTag, org.springframework.web.servlet.tags.form.AbstractCheckedElementTag, org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        super.writeTagContent(tagWriter);
        if (!isDisabled()) {
            tagWriter.startTag("input");
            tagWriter.writeAttribute("type", CellUtil.HIDDEN);
            String name = "_" + getName();
            tagWriter.writeAttribute("name", name);
            tagWriter.writeAttribute("value", processFieldValue(name, CustomBooleanEditor.VALUE_ON, CellUtil.HIDDEN));
            tagWriter.endTag();
            return 0;
        }
        return 0;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractCheckedElementTag
    protected String getInputType() {
        return "checkbox";
    }
}
