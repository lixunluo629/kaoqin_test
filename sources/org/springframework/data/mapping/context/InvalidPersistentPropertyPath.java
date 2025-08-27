package org.springframework.data.mapping.context;

import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/InvalidPersistentPropertyPath.class */
public class InvalidPersistentPropertyPath extends MappingException {
    private static final long serialVersionUID = 2805815643641094488L;
    private final String source;
    private final String unresolvableSegment;
    private final String resolvedPath;
    private final TypeInformation<?> type;

    InvalidPersistentPropertyPath(String source, TypeInformation<?> type, String unresolvableSegment, String resolvedPath, String message) {
        super(message);
        Assert.notNull(source, "Source property path must not be null!");
        Assert.notNull(type, "Type must not be null!");
        Assert.notNull(unresolvableSegment, "Unresolvable segment must not be null!");
        this.source = source;
        this.type = type;
        this.unresolvableSegment = unresolvableSegment;
        this.resolvedPath = resolvedPath == null ? "" : resolvedPath;
    }

    public String getSource() {
        return this.source;
    }

    public TypeInformation<?> getType() {
        return this.type;
    }

    public String getUnresolvableSegment() {
        return this.unresolvableSegment;
    }

    public String getResolvedPath() {
        return this.resolvedPath;
    }
}
