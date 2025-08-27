package org.springframework.web.servlet.tags.form;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/AbstractHtmlElementTag.class */
public abstract class AbstractHtmlElementTag extends AbstractDataBoundFormElementTag implements DynamicAttributes {
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String STYLE_ATTRIBUTE = "style";
    public static final String LANG_ATTRIBUTE = "lang";
    public static final String TITLE_ATTRIBUTE = "title";
    public static final String DIR_ATTRIBUTE = "dir";
    public static final String TABINDEX_ATTRIBUTE = "tabindex";
    public static final String ONCLICK_ATTRIBUTE = "onclick";
    public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
    public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
    public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
    public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
    public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
    public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
    public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
    public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
    public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";
    private String cssClass;
    private String cssErrorClass;
    private String cssStyle;
    private String lang;
    private String title;
    private String dir;
    private String tabindex;
    private String onclick;
    private String ondblclick;
    private String onmousedown;
    private String onmouseup;
    private String onmouseover;
    private String onmousemove;
    private String onmouseout;
    private String onkeypress;
    private String onkeyup;
    private String onkeydown;
    private Map<String, Object> dynamicAttributes;

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    protected String getCssClass() {
        return this.cssClass;
    }

    public void setCssErrorClass(String cssErrorClass) {
        this.cssErrorClass = cssErrorClass;
    }

    protected String getCssErrorClass() {
        return this.cssErrorClass;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    protected String getCssStyle() {
        return this.cssStyle;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    protected String getLang() {
        return this.lang;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected String getTitle() {
        return this.title;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    protected String getDir() {
        return this.dir;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    protected String getTabindex() {
        return this.tabindex;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    protected String getOnclick() {
        return this.onclick;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    protected String getOndblclick() {
        return this.ondblclick;
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    protected String getOnmousedown() {
        return this.onmousedown;
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    protected String getOnmouseup() {
        return this.onmouseup;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    protected String getOnmouseover() {
        return this.onmouseover;
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    protected String getOnmousemove() {
        return this.onmousemove;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    protected String getOnmouseout() {
        return this.onmouseout;
    }

    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    protected String getOnkeypress() {
        return this.onkeypress;
    }

    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    protected String getOnkeyup() {
        return this.onkeyup;
    }

    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    protected String getOnkeydown() {
        return this.onkeydown;
    }

    protected Map<String, Object> getDynamicAttributes() {
        return this.dynamicAttributes;
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        if (this.dynamicAttributes == null) {
            this.dynamicAttributes = new HashMap();
        }
        if (!isValidDynamicAttribute(localName, value)) {
            throw new IllegalArgumentException("Attribute " + localName + "=\"" + value + "\" is not allowed");
        }
        this.dynamicAttributes.put(localName, value);
    }

    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return true;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
        super.writeDefaultAttributes(tagWriter);
        writeOptionalAttributes(tagWriter);
    }

    protected void writeOptionalAttributes(TagWriter tagWriter) throws JspException {
        tagWriter.writeOptionalAttributeValue("class", resolveCssClass());
        tagWriter.writeOptionalAttributeValue(STYLE_ATTRIBUTE, ObjectUtils.getDisplayString(evaluate("cssStyle", getCssStyle())));
        writeOptionalAttribute(tagWriter, LANG_ATTRIBUTE, getLang());
        writeOptionalAttribute(tagWriter, "title", getTitle());
        writeOptionalAttribute(tagWriter, DIR_ATTRIBUTE, getDir());
        writeOptionalAttribute(tagWriter, TABINDEX_ATTRIBUTE, getTabindex());
        writeOptionalAttribute(tagWriter, ONCLICK_ATTRIBUTE, getOnclick());
        writeOptionalAttribute(tagWriter, ONDBLCLICK_ATTRIBUTE, getOndblclick());
        writeOptionalAttribute(tagWriter, ONMOUSEDOWN_ATTRIBUTE, getOnmousedown());
        writeOptionalAttribute(tagWriter, ONMOUSEUP_ATTRIBUTE, getOnmouseup());
        writeOptionalAttribute(tagWriter, ONMOUSEOVER_ATTRIBUTE, getOnmouseover());
        writeOptionalAttribute(tagWriter, ONMOUSEMOVE_ATTRIBUTE, getOnmousemove());
        writeOptionalAttribute(tagWriter, ONMOUSEOUT_ATTRIBUTE, getOnmouseout());
        writeOptionalAttribute(tagWriter, ONKEYPRESS_ATTRIBUTE, getOnkeypress());
        writeOptionalAttribute(tagWriter, ONKEYUP_ATTRIBUTE, getOnkeyup());
        writeOptionalAttribute(tagWriter, ONKEYDOWN_ATTRIBUTE, getOnkeydown());
        if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
            for (String attr : this.dynamicAttributes.keySet()) {
                tagWriter.writeOptionalAttributeValue(attr, getDisplayString(this.dynamicAttributes.get(attr)));
            }
        }
    }

    protected String resolveCssClass() throws JspException {
        if (getBindStatus().isError() && StringUtils.hasText(getCssErrorClass())) {
            return ObjectUtils.getDisplayString(evaluate("cssErrorClass", getCssErrorClass()));
        }
        return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
    }
}
