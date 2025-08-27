package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/AbstractCheckedElementTag.class */
public abstract class AbstractCheckedElementTag extends AbstractHtmlInputElementTag {
    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected abstract int writeTagContent(TagWriter tagWriter) throws JspException;

    protected abstract String getInputType();

    protected void renderFromValue(Object value, TagWriter tagWriter) throws JspException, IOException {
        renderFromValue(value, value, tagWriter);
    }

    protected void renderFromValue(Object item, Object value, TagWriter tagWriter) throws JspException, IOException {
        String displayValue = convertToDisplayString(value);
        tagWriter.writeAttribute("value", processFieldValue(getName(), displayValue, getInputType()));
        if (isOptionSelected(value) || (value != item && isOptionSelected(item))) {
            tagWriter.writeAttribute("checked", "checked");
        }
    }

    private boolean isOptionSelected(Object value) throws JspException {
        return SelectedValueComparator.isSelected(getBindStatus(), value);
    }

    protected void renderFromBoolean(Boolean boundValue, TagWriter tagWriter) throws JspException, IOException {
        tagWriter.writeAttribute("value", processFieldValue(getName(), "true", getInputType()));
        if (boundValue.booleanValue()) {
            tagWriter.writeAttribute("checked", "checked");
        }
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected String autogenerateId() throws JspException {
        return TagIdGenerator.nextId(super.autogenerateId(), this.pageContext);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return !"type".equals(localName);
    }
}
