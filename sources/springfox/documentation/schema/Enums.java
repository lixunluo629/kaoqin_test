package springfox.documentation.schema;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.annotation.AnnotationUtils;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/Enums.class */
public class Enums {
    private Enums() {
        throw new UnsupportedOperationException();
    }

    public static AllowableValues allowableValues(Class<?> type) {
        if (type.isEnum()) {
            List<String> enumValues = getEnumValues(type);
            return new AllowableListValues(enumValues, "LIST");
        }
        return null;
    }

    static List<String> getEnumValues(Class<?> subject) {
        return Lists.transform(Arrays.asList(subject.getEnumConstants()), new Function<Object, String>() { // from class: springfox.documentation.schema.Enums.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Function
            public String apply(Object input) {
                Optional<String> jsonValue = Enums.findJsonValueAnnotatedMethod(input).transform(Enums.evaluateJsonValue(input));
                if (jsonValue.isPresent() && !Strings.isNullOrEmpty(jsonValue.get())) {
                    return jsonValue.get();
                }
                return input.toString();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Function<Method, String> evaluateJsonValue(final Object enumConstant) {
        return new Function<Method, String>() { // from class: springfox.documentation.schema.Enums.2
            @Override // com.google.common.base.Function
            public String apply(Method input) {
                try {
                    return input.invoke(enumConstant, new Object[0]).toString();
                } catch (Exception e) {
                    return "";
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Optional<Method> findJsonValueAnnotatedMethod(Object enumConstant) throws SecurityException {
        for (Method each : enumConstant.getClass().getMethods()) {
            JsonValue jsonValue = (JsonValue) AnnotationUtils.findAnnotation(each, JsonValue.class);
            if (jsonValue != null && jsonValue.value()) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    public static AllowableValues emptyListValuesToNull(AllowableListValues values) {
        if (!values.getValues().isEmpty()) {
            return values;
        }
        return null;
    }
}
