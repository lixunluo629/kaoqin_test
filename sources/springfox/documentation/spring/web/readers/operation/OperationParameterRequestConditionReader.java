package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.swagger.models.properties.StringProperty;
import java.lang.reflect.Type;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.Parameters;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Order(ConfigFileApplicationListener.DEFAULT_ORDER)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/OperationParameterRequestConditionReader.class */
public class OperationParameterRequestConditionReader implements OperationBuilderPlugin {
    private final TypeResolver resolver;

    @Autowired
    public OperationParameterRequestConditionReader(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        ParamsRequestCondition paramsCondition = context.getRequestMappingInfo().getParamsCondition();
        List<Parameter> parameters = Lists.newArrayList();
        for (NameValueExpression<String> expression : paramsCondition.getExpressions()) {
            if (!skipParameter(parameters, expression)) {
                String paramValue = expression.getValue();
                AllowableListValues allowableValues = null;
                if (!Strings.isNullOrEmpty(paramValue)) {
                    allowableValues = new AllowableListValues(Lists.newArrayList(paramValue), StringProperty.TYPE);
                }
                Parameter parameter = new ParameterBuilder().name(expression.getName()).description(null).defaultValue(paramValue).required(true).allowMultiple(false).type(this.resolver.resolve(String.class, new Type[0])).modelRef(new ModelRef(StringProperty.TYPE)).allowableValues(allowableValues).parameterType("query").build();
                parameters.add(parameter);
            }
        }
        context.operationBuilder().parameters(parameters);
    }

    private boolean skipParameter(List<Parameter> parameters, NameValueExpression<String> expression) {
        return expression.isNegated() || parameterHandled(parameters, expression);
    }

    private boolean parameterHandled(List<Parameter> parameters, NameValueExpression<String> expression) {
        return Iterables.any(parameters, Parameters.withName(expression.getName()));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
