package org.springframework.hateoas.alps;

import java.util.List;
import org.springframework.hateoas.alps.Descriptor;
import org.springframework.hateoas.alps.Doc;
import org.springframework.hateoas.alps.Ext;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Alps.class */
public final class Alps {
    private final String version = "1.0";
    private final Doc doc;
    private final List<Descriptor> descriptors;

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Alps$AlpsBuilder.class */
    public static class AlpsBuilder {
        private Doc doc;
        private List<Descriptor> descriptors;

        AlpsBuilder() {
        }

        public AlpsBuilder doc(Doc doc) {
            this.doc = doc;
            return this;
        }

        public AlpsBuilder descriptors(List<Descriptor> descriptors) {
            this.descriptors = descriptors;
            return this;
        }

        public Alps build() {
            return new Alps(this.doc, this.descriptors);
        }

        public String toString() {
            return "Alps.AlpsBuilder(doc=" + this.doc + ", descriptors=" + this.descriptors + ")";
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Alps)) {
            return false;
        }
        Alps other = (Alps) o;
        Object this$version = getVersion();
        Object other$version = other.getVersion();
        if (this$version == null) {
            if (other$version != null) {
                return false;
            }
        } else if (!this$version.equals(other$version)) {
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
        Object this$descriptors = getDescriptors();
        Object other$descriptors = other.getDescriptors();
        return this$descriptors == null ? other$descriptors == null : this$descriptors.equals(other$descriptors);
    }

    public int hashCode() {
        Object $version = getVersion();
        int result = (1 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $doc = getDoc();
        int result2 = (result * 59) + ($doc == null ? 43 : $doc.hashCode());
        Object $descriptors = getDescriptors();
        return (result2 * 59) + ($descriptors == null ? 43 : $descriptors.hashCode());
    }

    public String toString() {
        return "Alps(version=" + getVersion() + ", doc=" + getDoc() + ", descriptors=" + getDescriptors() + ")";
    }

    Alps(Doc doc, List<Descriptor> descriptors) {
        this.doc = doc;
        this.descriptors = descriptors;
    }

    public static AlpsBuilder alps() {
        return new AlpsBuilder();
    }

    public String getVersion() {
        getClass();
        return "1.0";
    }

    public Doc getDoc() {
        return this.doc;
    }

    public List<Descriptor> getDescriptors() {
        return this.descriptors;
    }

    public static Descriptor.DescriptorBuilder descriptor() {
        return Descriptor.builder();
    }

    public static Doc.DocBuilder doc() {
        return Doc.builder();
    }

    public static Ext.ExtBuilder ext() {
        return Ext.builder();
    }
}
