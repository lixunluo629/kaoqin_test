package org.springframework.hateoas.core;

import org.springframework.core.annotation.Order;
import org.springframework.hateoas.RelProvider;
import org.springframework.util.StringUtils;

@Order(Integer.MAX_VALUE)
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DefaultRelProvider.class */
public class DefaultRelProvider implements RelProvider {
    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return true;
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getCollectionResourceRelFor(Class<?> type) {
        return StringUtils.uncapitalize(type.getSimpleName()) + "List";
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getItemResourceRelFor(Class<?> type) {
        return StringUtils.uncapitalize(type.getSimpleName());
    }
}
