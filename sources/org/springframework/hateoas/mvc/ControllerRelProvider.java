package org.springframework.hateoas.mvc;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.RelProvider;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerRelProvider.class */
public class ControllerRelProvider implements RelProvider {
    private final Class<?> controllerType;
    private final Class<?> entityType;
    private final PluginRegistry<RelProvider, Class<?>> providers;

    public ControllerRelProvider(Class<?> controller, PluginRegistry<RelProvider, Class<?>> providers) {
        Assert.notNull(controller);
        ExposesResourceFor annotation = (ExposesResourceFor) AnnotationUtils.findAnnotation(controller, ExposesResourceFor.class);
        Assert.notNull(annotation);
        this.controllerType = controller;
        this.entityType = annotation.value();
        this.providers = providers;
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getCollectionResourceRelFor(Class<?> resource) {
        return ((RelProvider) this.providers.getPluginFor(this.entityType)).getCollectionResourceRelFor(resource);
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getItemResourceRelFor(Class<?> resource) {
        return ((RelProvider) this.providers.getPluginFor(this.entityType)).getItemResourceRelFor(resource);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return this.controllerType.equals(delimiter);
    }
}
