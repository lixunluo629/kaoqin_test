package org.springframework.hateoas.core;

import org.atteo.evo.inflector.English;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EvoInflectorRelProvider.class */
public class EvoInflectorRelProvider extends DefaultRelProvider {
    @Override // org.springframework.hateoas.core.DefaultRelProvider, org.springframework.hateoas.RelProvider
    public String getCollectionResourceRelFor(Class<?> type) {
        return English.plural(getItemResourceRelFor(type));
    }
}
