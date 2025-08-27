package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/ButtonTag.class */
public class ButtonTag extends AbstractHtmlElementTag {
    public static final String DISABLED_ATTRIBUTE = "disabled";
    private TagWriter tagWriter;
    private String name;
    private String value;
    private boolean disabled;

    public void setName(String name) {
        this.name = name;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    public String getName() {
        return this.name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("button");
        writeDefaultAttributes(tagWriter);
        tagWriter.writeAttribute("type", getType());
        writeValue(tagWriter);
        if (isDisabled()) {
            tagWriter.writeAttribute("disabled", "disabled");
        }
        tagWriter.forceBlock();
        this.tagWriter = tagWriter;
        return 1;
    }

    protected void writeValue(TagWriter tagWriter) throws JspException, IOException {
        String valueToUse = getValue() != null ? getValue() : getDefaultValue();
        tagWriter.writeAttribute("value", processFieldValue(getName(), valueToUse, getType()));
    }

    protected String getDefaultValue() {
        return "Submit";
    }

    protected String getType() {
        return "submit";
    }

    public int doEndTag() throws JspException, IOException {
        this.tagWriter.endTag();
        return 6;
    }
}
