package io.swagger.models.refs;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/refs/GenericRef.class */
public class GenericRef {
    private final RefFormat format;
    private final RefType type;
    private final String ref;
    private final String simpleRef;

    public GenericRef(RefType type, String ref) {
        this.format = computeRefFormat(ref);
        this.type = type;
        validateFormatAndType(this.format, type);
        if (this.format == RefFormat.INTERNAL && !ref.startsWith("#/")) {
            this.ref = getPrefixForType(type) + ref;
        } else {
            this.ref = ref;
        }
        this.simpleRef = computeSimpleRef(this.ref, this.format, type);
    }

    private void validateFormatAndType(RefFormat format, RefType type) {
        if ((type == RefType.PATH || type == RefType.RESPONSE) && format == RefFormat.INTERNAL) {
            throw new RuntimeException(type + " refs can not be internal references");
        }
    }

    public RefFormat getFormat() {
        return this.format;
    }

    public RefType getType() {
        return this.type;
    }

    public String getRef() {
        return this.ref;
    }

    public String getSimpleRef() {
        return this.simpleRef;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenericRef that = (GenericRef) o;
        if (this.format != that.format || this.type != that.type) {
            return false;
        }
        if (this.ref != null) {
            if (!this.ref.equals(that.ref)) {
                return false;
            }
        } else if (that.ref != null) {
            return false;
        }
        return this.simpleRef == null ? that.simpleRef == null : this.simpleRef.equals(that.simpleRef);
    }

    public int hashCode() {
        int result = this.format != null ? this.format.hashCode() : 0;
        return (31 * ((31 * ((31 * result) + (this.type != null ? this.type.hashCode() : 0))) + (this.ref != null ? this.ref.hashCode() : 0))) + (this.simpleRef != null ? this.simpleRef.hashCode() : 0);
    }

    private static String computeSimpleRef(String ref, RefFormat format, RefType type) {
        String result = ref;
        if (format == RefFormat.INTERNAL) {
            String prefix = getPrefixForType(type);
            result = ref.substring(prefix.length());
        }
        return result;
    }

    private static String getPrefixForType(RefType refType) {
        String result;
        switch (refType) {
            case DEFINITION:
                result = RefConstants.INTERNAL_DEFINITION_PREFIX;
                break;
            case PARAMETER:
                result = RefConstants.INTERNAL_PARAMETER_PREFIX;
                break;
            default:
                throw new RuntimeException("No logic implemented for RefType of " + refType);
        }
        return result;
    }

    private static RefFormat computeRefFormat(String ref) {
        RefFormat result = RefFormat.INTERNAL;
        if (ref.startsWith("http")) {
            result = RefFormat.URL;
        } else if (ref.startsWith("#/")) {
            result = RefFormat.INTERNAL;
        } else if (ref.startsWith(".")) {
            result = RefFormat.RELATIVE;
        }
        return result;
    }
}
