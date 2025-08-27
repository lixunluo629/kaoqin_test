package org.springframework.hateoas.jaxrs;

import java.util.Map;
import javax.ws.rs.Path;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.LinkBuilderSupport;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/jaxrs/JaxRsLinkBuilder.class */
public class JaxRsLinkBuilder extends LinkBuilderSupport<JaxRsLinkBuilder> {
    private static final MappingDiscoverer DISCOVERER = new AnnotationMappingDiscoverer(Path.class);
    private static final CustomUriTemplateHandler HANDLER = new CustomUriTemplateHandler();

    private JaxRsLinkBuilder(UriComponentsBuilder builder) {
        super(builder);
    }

    public static JaxRsLinkBuilder linkTo(Class<?> service) {
        return linkTo(service, new Object[0]);
    }

    public static JaxRsLinkBuilder linkTo(Class<?> resourceType, Object... parameters) {
        Assert.notNull(resourceType, "Controller type must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(DISCOVERER.getMapping(resourceType));
        UriComponents expandedComponents = HANDLER.expandAndEncode(builder, parameters);
        return new JaxRsLinkBuilder(ServletUriComponentsBuilder.fromCurrentServletMapping()).slash(expandedComponents, true);
    }

    public static JaxRsLinkBuilder linkTo(Class<?> resourceType, Map<String, ?> parameters) {
        Assert.notNull(resourceType, "Controller type must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(DISCOVERER.getMapping(resourceType));
        UriComponents expandedComponents = HANDLER.expandAndEncode(builder, parameters);
        return new JaxRsLinkBuilder(ServletUriComponentsBuilder.fromCurrentServletMapping()).slash(expandedComponents, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public JaxRsLinkBuilder getThis() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public JaxRsLinkBuilder createNewInstance(UriComponentsBuilder builder) {
        return new JaxRsLinkBuilder(builder);
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/jaxrs/JaxRsLinkBuilder$CustomUriTemplateHandler.class */
    private static class CustomUriTemplateHandler extends DefaultUriTemplateHandler {
        public CustomUriTemplateHandler() {
            setStrictEncoding(true);
        }

        @Override // org.springframework.web.util.DefaultUriTemplateHandler
        public UriComponents expandAndEncode(UriComponentsBuilder builder, Map<String, ?> uriVariables) {
            return super.expandAndEncode(builder, uriVariables);
        }

        @Override // org.springframework.web.util.DefaultUriTemplateHandler
        public UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
            return super.expandAndEncode(builder, uriVariables);
        }
    }
}
