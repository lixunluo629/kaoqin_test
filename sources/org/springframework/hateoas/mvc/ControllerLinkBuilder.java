package org.springframework.hateoas.mvc;

import com.google.common.net.HttpHeaders;
import java.beans.ConstructorProperties;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.LinkBuilderSupport;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerLinkBuilder.class */
public class ControllerLinkBuilder extends LinkBuilderSupport<ControllerLinkBuilder> {
    private static final String REQUEST_ATTRIBUTES_MISSING = "Could not find current request via RequestContextHolder. Is this being called from a Spring MVC handler?";
    private static final CachingAnnotationMappingDiscoverer DISCOVERER = new CachingAnnotationMappingDiscoverer(new AnnotationMappingDiscoverer(RequestMapping.class));
    private static final ControllerLinkBuilderFactory FACTORY = new ControllerLinkBuilderFactory();
    private static final CustomUriTemplateHandler HANDLER = new CustomUriTemplateHandler();
    private final TemplateVariables variables;

    ControllerLinkBuilder(UriComponentsBuilder builder) {
        super(builder);
        this.variables = TemplateVariables.NONE;
    }

    ControllerLinkBuilder(UriComponents uriComponents) {
        this(uriComponents, TemplateVariables.NONE);
    }

    ControllerLinkBuilder(UriComponents uriComponents, TemplateVariables variables) {
        super(uriComponents);
        this.variables = variables;
    }

    public static ControllerLinkBuilder linkTo(Class<?> controller) {
        return linkTo(controller, new Object[0]);
    }

    public static ControllerLinkBuilder linkTo(Class<?> controller, Object... parameters) {
        Assert.notNull(controller, "Controller must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        String mapping = DISCOVERER.getMapping(controller);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(mapping == null ? "/" : mapping);
        UriComponents uriComponents = HANDLER.expandAndEncode(builder, parameters);
        return new ControllerLinkBuilder(getBuilder()).slash(uriComponents, true);
    }

    public static ControllerLinkBuilder linkTo(Class<?> controller, Map<String, ?> parameters) {
        Assert.notNull(controller, "Controller must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        String mapping = DISCOVERER.getMapping(controller);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(mapping == null ? "/" : mapping);
        UriComponents uriComponents = HANDLER.expandAndEncode(builder, parameters);
        return new ControllerLinkBuilder(getBuilder()).slash(uriComponents, true);
    }

    public static ControllerLinkBuilder linkTo(Method method, Object... parameters) {
        return linkTo(method.getDeclaringClass(), method, parameters);
    }

    public static ControllerLinkBuilder linkTo(Class<?> controller, Method method, Object... parameters) {
        Assert.notNull(controller, "Controller type must not be null!");
        Assert.notNull(method, "Method must not be null!");
        UriTemplate template = DISCOVERER.getMappingAsUriTemplate(controller, method);
        URI uri = template.expand(parameters);
        return new ControllerLinkBuilder(getBuilder()).slash(uri);
    }

    public static ControllerLinkBuilder linkTo(Object invocationValue) {
        return FACTORY.linkTo(invocationValue);
    }

    public static <T> T methodOn(Class<T> cls, Object... objArr) {
        return (T) DummyInvocationUtils.methodOn(cls, objArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public ControllerLinkBuilder getThis() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public ControllerLinkBuilder createNewInstance(UriComponentsBuilder builder) {
        return new ControllerLinkBuilder(builder);
    }

    public UriComponentsBuilder toUriComponentsBuilder() {
        return UriComponentsBuilder.fromUri(toUri());
    }

    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public String toString() {
        String result = super.toString();
        if (this.variables == TemplateVariables.NONE) {
            return result;
        }
        if (!result.contains("#")) {
            return result.concat(this.variables.toString());
        }
        String[] parts = result.split("#");
        return parts[0].concat(this.variables.toString()).concat("#").concat(parts[0]);
    }

    static UriComponentsBuilder getBuilder() {
        HttpServletRequest request = getCurrentRequest();
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(request);
        String forwardedSsl = request.getHeader("X-Forwarded-Ssl");
        ForwardedHeader forwarded = ForwardedHeader.of(request.getHeader(ForwardedHeader.NAME));
        String proto = StringUtils.hasText(forwarded.getProto()) ? forwarded.getProto() : request.getHeader(HttpHeaders.X_FORWARDED_PROTO);
        if (!StringUtils.hasText(proto) && StringUtils.hasText(forwardedSsl) && forwardedSsl.equalsIgnoreCase(CustomBooleanEditor.VALUE_ON)) {
            builder.scheme("https");
        }
        return builder;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, REQUEST_ATTRIBUTES_MISSING);
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerLinkBuilder$CachingAnnotationMappingDiscoverer.class */
    private static class CachingAnnotationMappingDiscoverer implements MappingDiscoverer {
        private final AnnotationMappingDiscoverer delegate;
        private final Map<String, UriTemplate> templates = new ConcurrentReferenceHashMap();

        @ConstructorProperties({"delegate"})
        public CachingAnnotationMappingDiscoverer(AnnotationMappingDiscoverer delegate) {
            this.delegate = delegate;
        }

        @Override // org.springframework.hateoas.core.MappingDiscoverer
        public String getMapping(Class<?> type) {
            return this.delegate.getMapping(type);
        }

        @Override // org.springframework.hateoas.core.MappingDiscoverer
        public String getMapping(Method method) {
            return this.delegate.getMapping(method);
        }

        @Override // org.springframework.hateoas.core.MappingDiscoverer
        public String getMapping(Class<?> type, Method method) {
            return this.delegate.getMapping(type, method);
        }

        public UriTemplate getMappingAsUriTemplate(Class<?> type, Method method) {
            String mapping = this.delegate.getMapping(type, method);
            UriTemplate template = this.templates.get(mapping);
            if (template == null) {
                template = new UriTemplate(mapping);
                this.templates.put(mapping, template);
            }
            return template;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerLinkBuilder$CustomUriTemplateHandler.class */
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
