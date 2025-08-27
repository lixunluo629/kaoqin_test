package org.springframework.web.servlet.tags.form;

import com.mysql.jdbc.NonRegisteringDriver;
import java.io.IOException;
import javax.servlet.jsp.JspException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/PasswordInputTag.class */
public class PasswordInputTag extends InputTag {
    private boolean showPassword = false;

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public boolean isShowPassword() {
        return this.showPassword;
    }

    @Override // org.springframework.web.servlet.tags.form.InputTag, org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return !"type".equals(localName);
    }

    @Override // org.springframework.web.servlet.tags.form.InputTag
    protected String getType() {
        return NonRegisteringDriver.PASSWORD_PROPERTY_KEY;
    }

    @Override // org.springframework.web.servlet.tags.form.InputTag
    protected void writeValue(TagWriter tagWriter) throws JspException, IOException {
        if (this.showPassword) {
            super.writeValue(tagWriter);
        } else {
            tagWriter.writeAttribute("value", processFieldValue(getName(), "", getType()));
        }
    }
}
