package org.springframework.data.keyvalue.core;

import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/IdentifierGenerator.class */
public interface IdentifierGenerator {
    <T> T generateIdentifierOfType(TypeInformation<T> typeInformation);
}
