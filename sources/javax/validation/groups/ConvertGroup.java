package javax.validation.groups;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/groups/ConvertGroup.class */
public @interface ConvertGroup {

    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/groups/ConvertGroup$List.class */
    public @interface List {
        ConvertGroup[] value();
    }

    Class<?> from();

    Class<?> to();
}
