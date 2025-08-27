package io.swagger.models.properties;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.models.Xml;
import java.util.ArrayList;
import java.util.List;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/StringProperty.class */
public class StringProperty extends AbstractProperty implements Property {
    public static final String TYPE = "string";
    protected List<String> _enum;
    protected Integer minLength;
    protected Integer maxLength;
    protected String pattern;
    protected String _default;

    /* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/StringProperty$Format.class */
    public enum Format {
        BYTE("byte"),
        URI("uri"),
        URL(RtspHeaders.Values.URL);

        private final String name;

        Format(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Format fromName(String name) {
            Format[] arr$ = values();
            for (Format item : arr$) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }
            return null;
        }
    }

    public StringProperty() {
        this((String) null);
    }

    public StringProperty(Format format) {
        this(format.getName());
    }

    public StringProperty(String format) {
        this.minLength = null;
        this.maxLength = null;
        this.pattern = null;
        this.type = TYPE;
        this.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && (format == null || Format.fromName(format) != null);
    }

    public StringProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public StringProperty example(String example) {
        setExample(example);
        return this;
    }

    public StringProperty minLength(Integer minLength) {
        setMinLength(minLength);
        return this;
    }

    public StringProperty maxLength(Integer maxLength) {
        setMaxLength(maxLength);
        return this;
    }

    public StringProperty pattern(String pattern) {
        setPattern(pattern);
        return this;
    }

    public StringProperty _enum(String value) {
        if (this._enum == null) {
            this._enum = new ArrayList();
        }
        if (!this._enum.contains(value)) {
            this._enum.add(value);
        }
        return this;
    }

    public StringProperty _enum(List<String> value) {
        this._enum = value;
        return this;
    }

    public StringProperty _default(String _default) {
        this._default = _default;
        return this;
    }

    public List<String> getEnum() {
        return this._enum;
    }

    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    public Integer getMinLength() {
        return this.minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefault() {
        return this._default;
    }

    @Override // io.swagger.models.properties.AbstractProperty, io.swagger.models.properties.Property
    public void setDefault(String _default) {
        this._default = _default;
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + (this._default == null ? 0 : this._default.hashCode()))) + (this._enum == null ? 0 : this._enum.hashCode()))) + (this.maxLength == null ? 0 : this.maxLength.hashCode()))) + (this.minLength == null ? 0 : this.minLength.hashCode()))) + (this.pattern == null ? 0 : this.pattern.hashCode());
    }

    @Override // io.swagger.models.properties.AbstractProperty
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof StringProperty)) {
            return false;
        }
        StringProperty other = (StringProperty) obj;
        if (this._default == null) {
            if (other._default != null) {
                return false;
            }
        } else if (!this._default.equals(other._default)) {
            return false;
        }
        if (this._enum == null) {
            if (other._enum != null) {
                return false;
            }
        } else if (!this._enum.equals(other._enum)) {
            return false;
        }
        if (this.maxLength == null) {
            if (other.maxLength != null) {
                return false;
            }
        } else if (!this.maxLength.equals(other.maxLength)) {
            return false;
        }
        if (this.minLength == null) {
            if (other.minLength != null) {
                return false;
            }
        } else if (!this.minLength.equals(other.minLength)) {
            return false;
        }
        if (this.pattern == null) {
            if (other.pattern != null) {
                return false;
            }
            return true;
        }
        if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        return true;
    }
}
