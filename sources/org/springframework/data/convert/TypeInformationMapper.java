package org.springframework.data.convert;

import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/TypeInformationMapper.class */
public interface TypeInformationMapper {
    TypeInformation<?> resolveTypeFrom(Object obj);

    Object createAliasFor(TypeInformation<?> typeInformation);
}
