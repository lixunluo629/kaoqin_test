package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.bouncycastle.i18n.TextBundle;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/InputTag.class */
public class InputTag extends AbstractHtmlInputElementTag {
    public static final String SIZE_ATTRIBUTE = "size";
    public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
    public static final String ALT_ATTRIBUTE = "alt";
    public static final String ONSELECT_ATTRIBUTE = "onselect";
    public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";

    @Deprecated
    public static final String READONLY_ATTRIBUTE = "readonly";
    private String size;
    private String maxlength;
    private String alt;
    private String onselect;
    private String autocomplete;

    public void setSize(String size) {
        this.size = size;
    }

    protected String getSize() {
        return this.size;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    protected String getMaxlength() {
        return this.maxlength;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    protected String getAlt() {
        return this.alt;
    }

    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }

    protected String getOnselect() {
        return this.onselect;
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    protected String getAutocomplete() {
        return this.autocomplete;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("input");
        writeDefaultAttributes(tagWriter);
        if (!hasDynamicTypeAttribute()) {
            tagWriter.writeAttribute("type", getType());
        }
        writeValue(tagWriter);
        writeOptionalAttribute(tagWriter, SIZE_ATTRIBUTE, getSize());
        writeOptionalAttribute(tagWriter, MAXLENGTH_ATTRIBUTE, getMaxlength());
        writeOptionalAttribute(tagWriter, ALT_ATTRIBUTE, getAlt());
        writeOptionalAttribute(tagWriter, "onselect", getOnselect());
        writeOptionalAttribute(tagWriter, AUTOCOMPLETE_ATTRIBUTE, getAutocomplete());
        tagWriter.endTag();
        return 0;
    }

    protected void writeValue(TagWriter tagWriter) throws JspException, IOException {
        String value = getDisplayString(getBoundValue(), getPropertyEditor());
        String type = hasDynamicTypeAttribute() ? (String) getDynamicAttributes().get("type") : getType();
        tagWriter.writeAttribute("value", processFieldValue(getName(), value, type));
    }

    private boolean hasDynamicTypeAttribute() {
        return getDynamicAttributes() != null && getDynamicAttributes().containsKey("type");
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return ("type".equals(localName) && ("checkbox".equals(value) || "radio".equals(value))) ? false : true;
    }

    protected String getType() {
        return TextBundle.TEXT_ENTRY;
    }
}
