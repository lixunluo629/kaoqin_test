package org.springframework.web.servlet.tags;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/ThemeTag.class */
public class ThemeTag extends MessageTag {
    @Override // org.springframework.web.servlet.tags.MessageTag
    protected MessageSource getMessageSource() {
        return getRequestContext().getTheme().getMessageSource();
    }

    @Override // org.springframework.web.servlet.tags.MessageTag
    protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
        return "Theme '" + getRequestContext().getTheme().getName() + "': " + ex.getMessage();
    }
}
