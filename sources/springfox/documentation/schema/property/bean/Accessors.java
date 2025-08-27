package springfox.documentation.schema.property.bean;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.annotation.AnnotationUtils;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/bean/Accessors.class */
public class Accessors {
    private static Pattern getter = Pattern.compile("^get([a-zA-Z_0-9].*)");
    private static Pattern isGetter = Pattern.compile("^is([a-zA-Z_0_9].*)");
    private static Pattern setter = Pattern.compile("^set([a-zA-Z_0-9].*)");

    private Accessors() {
        throw new UnsupportedOperationException();
    }

    public static boolean maybeAGetter(Method method) {
        if (method.getParameterTypes().length == 0) {
            return notAVoidMethod(method);
        }
        return false;
    }

    private static boolean notAVoidMethod(Method method) {
        return !method.getReturnType().equals(Void.TYPE);
    }

    public static boolean isSetter(Method method) {
        return isSetterMethod(method) || setterAnnotation(method).isPresent();
    }

    public static String toCamelCase(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static String propertyName(Method method) {
        Optional<JsonGetter> jsonGetterAnnotation = getterAnnotation(method);
        if (jsonGetterAnnotation.isPresent() && !Strings.isNullOrEmpty(jsonGetterAnnotation.get().value())) {
            return jsonGetterAnnotation.get().value();
        }
        Optional<JsonSetter> jsonSetterAnnotation = setterAnnotation(method);
        if (jsonSetterAnnotation.isPresent() && !Strings.isNullOrEmpty(jsonSetterAnnotation.get().value())) {
            return jsonSetterAnnotation.get().value();
        }
        Matcher matcher = getter.matcher(method.getName());
        if (matcher.find()) {
            return toCamelCase(matcher.group(1));
        }
        Matcher matcher2 = isGetter.matcher(method.getName());
        if (matcher2.find()) {
            return toCamelCase(matcher2.group(1));
        }
        Matcher matcher3 = setter.matcher(method.getName());
        if (matcher3.find()) {
            return toCamelCase(matcher3.group(1));
        }
        return "";
    }

    private static Optional<JsonGetter> getterAnnotation(Method method) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(method, JsonGetter.class));
    }

    private static Optional<JsonSetter> setterAnnotation(Method method) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(method, JsonSetter.class));
    }

    private static boolean isSetterMethod(Method method) {
        return maybeASetter(method) && setter.matcher(method.getName()).find();
    }

    public static boolean maybeASetter(Method method) {
        return method.getParameterTypes().length == 1;
    }
}
