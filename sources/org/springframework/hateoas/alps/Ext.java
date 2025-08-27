package org.springframework.hateoas.alps;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Ext.class */
public final class Ext {
    private final String id;
    private final String href;
    private final String value;

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Ext$ExtBuilder.class */
    public static class ExtBuilder {
        private String id;
        private String href;
        private String value;

        ExtBuilder() {
        }

        public ExtBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ExtBuilder href(String href) {
            this.href = href;
            return this;
        }

        public ExtBuilder value(String value) {
            this.value = value;
            return this;
        }

        public Ext build() {
            return new Ext(this.id, this.href, this.value);
        }

        public String toString() {
            return "Ext.ExtBuilder(id=" + this.id + ", href=" + this.href + ", value=" + this.value + ")";
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Ext)) {
            return false;
        }
        Ext other = (Ext) o;
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
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
        return this$value == null ? other$value == null : this$value.equals(other$value);
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $href = getHref();
        int result2 = (result * 59) + ($href == null ? 43 : $href.hashCode());
        Object $value = getValue();
        return (result2 * 59) + ($value == null ? 43 : $value.hashCode());
    }

    public String toString() {
        return "Ext(id=" + getId() + ", href=" + getHref() + ", value=" + getValue() + ")";
    }

    Ext(String id, String href, String value) {
        this.id = id;
        this.href = href;
        this.value = value;
    }

    public static ExtBuilder builder() {
        return new ExtBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getHref() {
        return this.href;
    }

    public String getValue() {
        return this.value;
    }
}
