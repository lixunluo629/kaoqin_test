package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import org.springframework.web.util.HtmlUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/HtmlEscapingAwareTag.class */
public abstract class HtmlEscapingAwareTag extends RequestContextAwareTag {
    private Boolean htmlEscape;

    public void setHtmlEscape(boolean htmlEscape) throws JspException {
        this.htmlEscape = Boolean.valueOf(htmlEscape);
    }

    protected boolean isHtmlEscape() {
        if (this.htmlEscape != null) {
            return this.htmlEscape.booleanValue();
        }
        return isDefaultHtmlEscape();
    }

    protected boolean isDefaultHtmlEscape() {
        return getRequestContext().isDefaultHtmlEscape();
    }

    protected boolean isResponseEncodedHtmlEscape() {
        return getRequestContext().isResponseEncodedHtmlEscape();
    }

    protected String htmlEscape(String content) {
        String out = content;
        if (isHtmlEscape()) {
            if (isResponseEncodedHtmlEscape()) {
                out = HtmlUtils.htmlEscape(content, this.pageContext.getResponse().getCharacterEncoding());
            } else {
                out = HtmlUtils.htmlEscape(content);
            }
        }
        return out;
    }
}
