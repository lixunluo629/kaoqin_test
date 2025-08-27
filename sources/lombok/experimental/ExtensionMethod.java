package lombok.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: lombok-1.16.22.jar:lombok/experimental/ExtensionMethod.class */
public @interface ExtensionMethod {
    Class<?>[] value();

    boolean suppressBaseMethods() default true;
}
