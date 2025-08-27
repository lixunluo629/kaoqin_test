package org.springframework.hateoas.alps;

import java.util.List;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Descriptor.class */
public final class Descriptor {
    private final String id;
    private final String href;
    private final String name;
    private final Doc doc;
    private final Type type;
    private final Ext ext;
    private final String rt;
    private final List<Descriptor> descriptors;

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Descriptor$DescriptorBuilder.class */
    public static class DescriptorBuilder {
        private String id;
        private String href;
        private String name;
        private Doc doc;
        private Type type;
        private Ext ext;
        private String rt;
        private List<Descriptor> descriptors;

        DescriptorBuilder() {
        }

        public DescriptorBuilder id(String id) {
            this.id = id;
            return this;
        }

        public DescriptorBuilder href(String href) {
            this.href = href;
            return this;
        }

        public DescriptorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DescriptorBuilder doc(Doc doc) {
            this.doc = doc;
            return this;
        }

        public DescriptorBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public DescriptorBuilder ext(Ext ext) {
            this.ext = ext;
            return this;
        }

        public DescriptorBuilder rt(String rt) {
            this.rt = rt;
            return this;
        }

        public DescriptorBuilder descriptors(List<Descriptor> descriptors) {
            this.descriptors = descriptors;
            return this;
        }

        public Descriptor build() {
            return new Descriptor(this.id, this.href, this.name, this.doc, this.type, this.ext, this.rt, this.descriptors);
        }

        public String toString() {
            return "Descriptor.DescriptorBuilder(id=" + this.id + ", href=" + this.href + ", name=" + this.name + ", doc=" + this.doc + ", type=" + this.type + ", ext=" + this.ext + ", rt=" + this.rt + ", descriptors=" + this.descriptors + ")";
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Descriptor)) {
            return false;
        }
        Descriptor other = (Descriptor) o;
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$doc = getDoc();
        Object other$doc = other.getDoc();
        if (this$doc == null) {
            if (other$doc != null) {
                return false;
            }
        } else if (!this$doc.equals(other$doc)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$ext = getExt();
        Object other$ext = other.getExt();
        if (this$ext == null) {
            if (other$ext != null) {
                return false;
            }
        } else if (!this$ext.equals(other$ext)) {
            return false;
        }
        Object this$rt = getRt();
        Object other$rt = other.getRt();
        if (this$rt == null) {
            if (other$rt != null) {
                return false;
            }
        } else if (!this$rt.equals(other$rt)) {
            return false;
        }
        Object this$descriptors = getDescriptors();
        Object other$descriptors = other.getDescriptors();
        return this$descriptors == null ? other$descriptors == null : this$descriptors.equals(other$descriptors);
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $href = getHref();
        int result2 = (result * 59) + ($href == null ? 43 : $href.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $doc = getDoc();
        int result4 = (result3 * 59) + ($doc == null ? 43 : $doc.hashCode());
        Object $type = getType();
        int result5 = (result4 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $ext = getExt();
        int result6 = (result5 * 59) + ($ext == null ? 43 : $ext.hashCode());
        Object $rt = getRt();
        int result7 = (result6 * 59) + ($rt == null ? 43 : $rt.hashCode());
        Object $descriptors = getDescriptors();
        return (result7 * 59) + ($descriptors == null ? 43 : $descriptors.hashCode());
    }

    public String toString() {
        return "Descriptor(id=" + getId() + ", href=" + getHref() + ", name=" + getName() + ", doc=" + getDoc() + ", type=" + getType() + ", ext=" + getExt() + ", rt=" + getRt() + ", descriptors=" + getDescriptors() + ")";
    }

    Descriptor(String id, String href, String name, Doc doc, Type type, Ext ext, String rt, List<Descriptor> descriptors) {
        this.id = id;
        this.href = href;
        this.name = name;
        this.doc = doc;
        this.type = type;
        this.ext = ext;
        this.rt = rt;
        this.descriptors = descriptors;
    }

    public static DescriptorBuilder builder() {
        return new DescriptorBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getHref() {
        return this.href;
    }

    public String getName() {
        return this.name;
    }

    public Doc getDoc() {
        return this.doc;
    }

    public Type getType() {
        return this.type;
    }

    public Ext getExt() {
        return this.ext;
    }

    public String getRt() {
        return this.rt;
    }

    public List<Descriptor> getDescriptors() {
        return this.descriptors;
    }
}
