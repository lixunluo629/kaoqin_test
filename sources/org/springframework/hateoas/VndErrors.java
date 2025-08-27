package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.BindErrorsTag;

@XmlRootElement(name = BindErrorsTag.ERRORS_VARIABLE_NAME)
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/VndErrors.class */
public class VndErrors implements Iterable<VndError> {

    @XmlElement(name = AsmRelationshipUtils.DECLARE_ERROR)
    private final List<VndError> vndErrors;

    public VndErrors(String logref, String message, Link... links) {
        this(new VndError(logref, message, links), new VndError[0]);
    }

    public VndErrors(VndError error, VndError... errors) {
        Assert.notNull(error, "Error must not be null");
        this.vndErrors = new ArrayList(errors.length + 1);
        this.vndErrors.add(error);
        this.vndErrors.addAll(Arrays.asList(errors));
    }

    @JsonCreator
    public VndErrors(List<VndError> errors) {
        Assert.notNull(errors, "Errors must not be null!");
        Assert.isTrue(!errors.isEmpty(), "Errors must not be empty!");
        this.vndErrors = errors;
    }

    protected VndErrors() {
        this.vndErrors = new ArrayList();
    }

    public VndErrors add(VndError error) {
        this.vndErrors.add(error);
        return this;
    }

    @JsonValue
    private List<VndError> getErrors() {
        return this.vndErrors;
    }

    @Override // java.lang.Iterable
    public Iterator<VndError> iterator() {
        return this.vndErrors.iterator();
    }

    public String toString() {
        return String.format("VndErrors[%s]", StringUtils.collectionToCommaDelimitedString(this.vndErrors));
    }

    public int hashCode() {
        return this.vndErrors.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VndErrors)) {
            return false;
        }
        VndErrors that = (VndErrors) obj;
        return this.vndErrors.equals(that.vndErrors);
    }

    @XmlType
    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/VndErrors$VndError.class */
    public static class VndError extends ResourceSupport {

        @JsonProperty
        @XmlAttribute
        private final String logref;

        @JsonProperty
        @XmlElement
        private final String message;

        public VndError(String logref, String message, Link... links) {
            Assert.hasText(logref, "Logref must not be null or empty!");
            Assert.hasText(message, "Message must not be null or empty!");
            this.logref = logref;
            this.message = message;
            add(Arrays.asList(links));
        }

        protected VndError() {
            this.logref = null;
            this.message = null;
        }

        public String getLogref() {
            return this.logref;
        }

        public String getMessage() {
            return this.message;
        }

        @Override // org.springframework.hateoas.ResourceSupport
        public String toString() {
            return String.format("VndError[logref: %s, message: %s, links: [%s]]", this.logref, this.message, StringUtils.collectionToCommaDelimitedString(getLinks()));
        }

        @Override // org.springframework.hateoas.ResourceSupport
        public int hashCode() {
            int result = 17 + (31 * this.logref.hashCode());
            return result + (31 * this.message.hashCode());
        }

        @Override // org.springframework.hateoas.ResourceSupport
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof VndError)) {
                return false;
            }
            VndError that = (VndError) obj;
            return this.logref.equals(that.logref) && this.message.equals(that.message);
        }
    }
}
