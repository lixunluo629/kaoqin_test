package springfox.documentation.swagger.readers.operation;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ObjectVendorExtension;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/VendorExtensionsReader.class */
public class VendorExtensionsReader implements OperationBuilderPlugin {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) VendorExtensionsReader.class);

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        Optional<ApiOperation> apiOperation = Annotations.findApiOperationAnnotation(handlerMethod.getMethod());
        if (apiOperation.isPresent()) {
            Extension[] extensionsAnnotations = apiOperation.get().extensions();
            List<VendorExtension> extensions = readExtensions(extensionsAnnotations);
            LOG.debug("Extension count {} for method {}", Integer.valueOf(extensions.size()), handlerMethod.getMethod().getName());
            context.operationBuilder().extensions(extensions);
        }
    }

    private List<VendorExtension> readExtensions(Extension[] vendorAnnotations) {
        return FluentIterable.from(Lists.newArrayList(vendorAnnotations)).transform(toVendorExtension()).toList();
    }

    private Function<Extension, VendorExtension> toVendorExtension() {
        return new Function<Extension, VendorExtension>() { // from class: springfox.documentation.swagger.readers.operation.VendorExtensionsReader.1
            @Override // com.google.common.base.Function
            public VendorExtension apply(Extension input) {
                return (VendorExtension) Optional.fromNullable(Strings.emptyToNull(input.name())).transform(VendorExtensionsReader.this.propertyExtension(input)).or((Optional) VendorExtensionsReader.this.objectExtension(input));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public VendorExtension objectExtension(Extension each) {
        ObjectVendorExtension extension = new ObjectVendorExtension(ensurePrefixed(Strings.nullToEmpty(each.name())));
        for (ExtensionProperty property : each.properties()) {
            if (!Strings.isNullOrEmpty(property.name()) && !Strings.isNullOrEmpty(property.value())) {
                extension.addProperty(new StringVendorExtension(property.name(), property.value()));
            }
        }
        return extension;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<String, VendorExtension> propertyExtension(final Extension annotation) {
        return new Function<String, VendorExtension>() { // from class: springfox.documentation.swagger.readers.operation.VendorExtensionsReader.2
            @Override // com.google.common.base.Function
            public VendorExtension apply(String input) {
                ObjectVendorExtension extension = new ObjectVendorExtension(VendorExtensionsReader.this.ensurePrefixed(input));
                for (ExtensionProperty each : annotation.properties()) {
                    extension.addProperty(new StringVendorExtension(each.name(), each.value()));
                }
                return extension;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String ensurePrefixed(String name) {
        if (!Strings.isNullOrEmpty(name) && !name.startsWith("x-")) {
            name = "x-" + name;
        }
        return name;
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
