package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/RadioButtonTag.class */
public class RadioButtonTag extends AbstractSingleCheckedElementTag {
    @Override // org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag
    protected void writeTagDetails(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.writeAttribute("type", getInputType());
        Object resolvedValue = evaluate("value", getValue());
        renderFromValue(resolvedValue, tagWriter);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractCheckedElementTag
    protected String getInputType() {
        return "radio";
    }
}
