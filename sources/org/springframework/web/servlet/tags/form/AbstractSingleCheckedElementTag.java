package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/AbstractSingleCheckedElementTag.class */
public abstract class AbstractSingleCheckedElementTag extends AbstractCheckedElementTag {
    private Object value;
    private Object label;

    protected abstract void writeTagDetails(TagWriter tagWriter) throws JspException;

    public void setValue(Object value) {
        this.value = value;
    }

    protected Object getValue() {
        return this.value;
    }

    public void setLabel(Object label) {
        this.label = label;
    }

    protected Object getLabel() {
        return this.label;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractCheckedElementTag, org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("input");
        String id = resolveId();
        writeOptionalAttribute(tagWriter, "id", id);
        writeOptionalAttribute(tagWriter, "name", getName());
        writeOptionalAttributes(tagWriter);
        writeTagDetails(tagWriter);
        tagWriter.endTag();
        Object resolvedLabel = evaluate(AnnotatedPrivateKey.LABEL, getLabel());
        if (resolvedLabel != null) {
            tagWriter.startTag(AnnotatedPrivateKey.LABEL);
            tagWriter.writeAttribute("for", id);
            tagWriter.appendValue(convertToDisplayString(resolvedLabel));
            tagWriter.endTag();
            return 0;
        }
        return 0;
    }
}
