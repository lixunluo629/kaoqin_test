package org.springframework.hateoas.alps;

import java.beans.ConstructorProperties;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Doc.class */
public final class Doc {
    private final String href;
    private final String value;
    private final Format format;

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Doc$DocBuilder.class */
    public static class DocBuilder {
        private String href;
        private String value;
        private Format format;

        DocBuilder() {
        }

        public DocBuilder href(String href) {
            this.href = href;
            return this;
        }

        public DocBuilder value(String value) {
            this.value = value;
            return this;
        }

        public DocBuilder format(Format format) {
            this.format = format;
            return this;
        }

        public Doc build() {
            return new Doc(this.href, this.value, this.format);
        }

        public String toString() {
            return "Doc.DocBuilder(href=" + this.href + ", value=" + this.value + ", format=" + this.format + ")";
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Doc)) {
            return false;
        }
        Doc other = (Doc) o;
        Object this$href = getHref();
        Object other$href = other.getHref();
        if (this$href == null) {
            if (other$href != null) {
                return false;
            }
        } else if (!this$href.equals(other$href)) {
            return false;
        }
        Object this$value = getValue();
        Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value != null) {
                return false;
            }
        } else if (!this$value.equals(other$value)) {
            return false;
        }
        Object this$format = getFormat();
        Object other$format = other.getFormat();
        return this$format == null ? other$format == null : this$format.equals(other$format);
    }

    public int hashCode() {
        Object $href = getHref();
        int result = (1 * 59) + ($href == null ? 43 : $href.hashCode());
        Object $value = getValue();
        int result2 = (result * 59) + ($value == null ? 43 : $value.hashCode());
        Object $format = getFormat();
        return (result2 * 59) + ($format == null ? 43 : $format.hashCode());
    }

    public String toString() {
        return "Doc(href=" + getHref() + ", value=" + getValue() + ", format=" + getFormat() + ")";
    }

    public static DocBuilder builder() {
        return new DocBuilder();
    }

    @ConstructorProperties({"href", "value", "format"})
    public Doc(String href, String value, Format format) {
        this.href = href;
        this.value = value;
        this.format = format;
    }

    public String getHref() {
        return this.href;
    }

    public String getValue() {
        return this.value;
    }

    public Format getFormat() {
        return this.format;
    }

    public Doc(String value, Format format) {
        Assert.hasText(value, "Value must not be null or empty!");
        Assert.notNull(format, "Format must not be null!");
        this.href = null;
        this.value = value;
        this.format = format;
    }
}
