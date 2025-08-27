package org.springframework.web.servlet.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/LabelTag.class */
public class LabelTag extends AbstractHtmlElementTag {
    private static final String LABEL_TAG = "label";
    private static final String FOR_ATTRIBUTE = "for";
    private TagWriter tagWriter;
    private String forId;

    public void setFor(String forId) {
        Assert.notNull(forId, "'forId' must not be null");
        this.forId = forId;
    }

    public String getFor() {
        return this.forId;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException, IOException {
        tagWriter.startTag("label");
        tagWriter.writeAttribute(FOR_ATTRIBUTE, resolveFor());
        writeDefaultAttributes(tagWriter);
        tagWriter.forceBlock();
        this.tagWriter = tagWriter;
        return 1;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected String getName() throws JspException {
        return null;
    }

    protected String resolveFor() throws JspException {
        if (StringUtils.hasText(this.forId)) {
            return getDisplayString(evaluate(FOR_ATTRIBUTE, this.forId));
        }
        return autogenerateFor();
    }

    protected String autogenerateFor() throws JspException {
        return StringUtils.deleteAny(getPropertyPath(), "[]");
    }

    public int doEndTag() throws JspException, IOException {
        this.tagWriter.endTag();
        return 6;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag, org.springframework.web.servlet.tags.RequestContextAwareTag
    public void doFinally() {
        super.doFinally();
        this.tagWriter = null;
    }
}
