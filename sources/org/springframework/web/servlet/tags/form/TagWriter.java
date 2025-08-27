package org.springframework.web.servlet.tags.form;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/TagWriter.class */
public class TagWriter {
    private final SafeWriter writer;
    private final Stack<TagStateEntry> tagState = new Stack<>();

    public TagWriter(PageContext pageContext) {
        Assert.notNull(pageContext, "PageContext must not be null");
        this.writer = new SafeWriter(pageContext);
    }

    public TagWriter(Writer writer) {
        Assert.notNull(writer, "Writer must not be null");
        this.writer = new SafeWriter(writer);
    }

    public void startTag(String tagName) throws JspException, IOException {
        if (inTag()) {
            closeTagAndMarkAsBlock();
        }
        push(tagName);
        this.writer.append("<").append(tagName);
    }

    public void writeAttribute(String attributeName, String attributeValue) throws JspException, IOException {
        if (currentState().isBlockTag()) {
            throw new IllegalStateException("Cannot write attributes after opening tag is closed.");
        }
        this.writer.append(SymbolConstants.SPACE_SYMBOL).append(attributeName).append("=\"").append(attributeValue).append(SymbolConstants.QUOTES_SYMBOL);
    }

    public void writeOptionalAttributeValue(String attributeName, String attributeValue) throws JspException, IOException {
        if (StringUtils.hasText(attributeValue)) {
            writeAttribute(attributeName, attributeValue);
        }
    }

    public void appendValue(String value) throws JspException, IOException {
        if (!inTag()) {
            throw new IllegalStateException("Cannot write tag value. No open tag available.");
        }
        closeTagAndMarkAsBlock();
        this.writer.append(value);
    }

    public void forceBlock() throws JspException, IOException {
        if (currentState().isBlockTag()) {
            return;
        }
        closeTagAndMarkAsBlock();
    }

    public void endTag() throws JspException, IOException {
        endTag(false);
    }

    public void endTag(boolean enforceClosingTag) throws JspException, IOException {
        if (!inTag()) {
            throw new IllegalStateException("Cannot write end of tag. No open tag available.");
        }
        boolean renderClosingTag = true;
        if (!currentState().isBlockTag()) {
            if (enforceClosingTag) {
                this.writer.append(">");
            } else {
                this.writer.append("/>");
                renderClosingTag = false;
            }
        }
        if (renderClosingTag) {
            this.writer.append("</").append(currentState().getTagName()).append(">");
        }
        this.tagState.pop();
    }

    private void push(String tagName) {
        this.tagState.push(new TagStateEntry(tagName));
    }

    private void closeTagAndMarkAsBlock() throws JspException, IOException {
        if (!currentState().isBlockTag()) {
            currentState().markAsBlockTag();
            this.writer.append(">");
        }
    }

    private boolean inTag() {
        return !this.tagState.isEmpty();
    }

    private TagStateEntry currentState() {
        return this.tagState.peek();
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/TagWriter$TagStateEntry.class */
    private static class TagStateEntry {
        private final String tagName;
        private boolean blockTag;

        public TagStateEntry(String tagName) {
            this.tagName = tagName;
        }

        public String getTagName() {
            return this.tagName;
        }

        public void markAsBlockTag() {
            this.blockTag = true;
        }

        public boolean isBlockTag() {
            return this.blockTag;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/TagWriter$SafeWriter.class */
    private static final class SafeWriter {
        private PageContext pageContext;
        private Writer writer;

        public SafeWriter(PageContext pageContext) {
            this.pageContext = pageContext;
        }

        public SafeWriter(Writer writer) {
            this.writer = writer;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.servlet.jsp.JspException */
        public SafeWriter append(String value) throws JspException, IOException {
            try {
                getWriterToUse().write(String.valueOf(value));
                return this;
            } catch (IOException ex) {
                throw new JspException("Unable to write to JspWriter", ex);
            }
        }

        private Writer getWriterToUse() {
            return this.pageContext != null ? this.pageContext.getOut() : this.writer;
        }
    }
}
