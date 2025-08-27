package org.springframework.web.servlet.tags.form;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/RadioButtonsTag.class */
public class RadioButtonsTag extends AbstractMultiCheckedElementTag {
    @Override // org.springframework.web.servlet.tags.form.AbstractCheckedElementTag
    protected String getInputType() {
        return "radio";
    }
}
