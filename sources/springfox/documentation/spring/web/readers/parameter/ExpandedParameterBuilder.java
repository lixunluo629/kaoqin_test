package springfox.documentation.spring.web.readers.parameter;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ExpandedParameterBuilder.class */
public class ExpandedParameterBuilder implements ExpandedParameterBuilderPlugin {
    private final TypeResolver resolver;

    @Autowired
    public ExpandedParameterBuilder(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override // springfox.documentation.spi.service.ExpandedParameterBuilderPlugin
    public void apply(ParameterExpansionContext context) {
        AllowableValues allowable = allowableValues(context.getField());
        String name = Strings.isNullOrEmpty(context.getParentName()) ? context.getField().getName() : String.format("%s.%s", context.getParentName(), context.getField().getName());
        context.getParameterBuilder().name(name).description(null).defaultValue(null).required(Boolean.FALSE.booleanValue()).allowMultiple(Boolean.FALSE.booleanValue()).type(this.resolver.resolve(context.getField().getType(), new Type[0])).modelRef(new ModelRef(context.getDataTypeName())).allowableValues(allowable).parameterType("query").parameterAccess(null);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private AllowableValues allowableValues(Field field) {
        AllowableValues allowable = null;
        if (field.getType().isEnum()) {
            allowable = new AllowableListValues(getEnumValues(field.getType()), "LIST");
        }
        return allowable;
    }

    private List<String> getEnumValues(Class<?> subject) {
        return Lists.transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() { // from class: springfox.documentation.spring.web.readers.parameter.ExpandedParameterBuilder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Function
            public String apply(Object input) {
                return input.toString();
            }
        });
    }
}
