package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.util.TagUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/OptionsTag.class */
public class OptionsTag extends AbstractHtmlElementTag {
    private Object items;
    private String itemValue;
    private String itemLabel;
    private boolean disabled;

    public void setItems(Object items) {
        this.items = items;
    }

    protected Object getItems() {
        return this.items;
    }

    public void setItemValue(String itemValue) {
        Assert.hasText(itemValue, "'itemValue' must not be empty");
        this.itemValue = itemValue;
    }

    protected String getItemValue() {
        return this.itemValue;
    }

    public void setItemLabel(String itemLabel) {
        Assert.hasText(itemLabel, "'itemLabel' must not be empty");
        this.itemLabel = itemLabel;
    }

    protected String getItemLabel() {
        return this.itemLabel;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    protected boolean isDisabled() {
        return this.disabled;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        SelectTag selectTag = getSelectTag();
        Object items = getItems();
        Object itemsObject = null;
        if (items != null) {
            itemsObject = items instanceof String ? evaluate("items", items) : items;
        } else {
            Class<?> selectTagBoundType = selectTag.getBindStatus().getValueType();
            if (selectTagBoundType != null && selectTagBoundType.isEnum()) {
                itemsObject = selectTagBoundType.getEnumConstants();
            }
        }
        if (itemsObject != null) {
            String selectName = selectTag.getName();
            String itemValue = getItemValue();
            String itemLabel = getItemLabel();
            String valueProperty = itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
            String labelProperty = itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
            OptionsWriter optionWriter = new OptionsWriter(selectName, itemsObject, valueProperty, labelProperty);
            optionWriter.writeOptions(tagWriter);
            return 0;
        }
        return 0;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected String resolveId() throws JspException {
        Object id = evaluate("id", getId());
        if (id != null) {
            String idString = id.toString();
            if (StringUtils.hasText(idString)) {
                return TagIdGenerator.nextId(idString, this.pageContext);
            }
            return null;
        }
        return null;
    }

    private SelectTag getSelectTag() {
        TagUtils.assertHasAncestorOfType(this, SelectTag.class, "options", "select");
        return findAncestorWithClass(this, SelectTag.class);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
    protected BindStatus getBindStatus() {
        return (BindStatus) this.pageContext.getAttribute(SelectTag.LIST_VALUE_PAGE_ATTRIBUTE);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/tags/form/OptionsTag$OptionsWriter.class */
    private class OptionsWriter extends OptionWriter {
        private final String selectName;

        public OptionsWriter(String selectName, Object optionSource, String valueProperty, String labelProperty) {
            super(optionSource, OptionsTag.this.getBindStatus(), valueProperty, labelProperty, OptionsTag.this.isHtmlEscape());
            this.selectName = selectName;
        }

        @Override // org.springframework.web.servlet.tags.form.OptionWriter
        protected boolean isOptionDisabled() throws JspException {
            return OptionsTag.this.isDisabled();
        }

        @Override // org.springframework.web.servlet.tags.form.OptionWriter
        protected void writeCommonAttributes(TagWriter tagWriter) throws JspException {
            OptionsTag.this.writeOptionalAttribute(tagWriter, "id", OptionsTag.this.resolveId());
            OptionsTag.this.writeOptionalAttributes(tagWriter);
        }

        @Override // org.springframework.web.servlet.tags.form.OptionWriter
        protected String processOptionValue(String value) {
            return OptionsTag.this.processFieldValue(this.selectName, value, "option");
        }
    }
}
