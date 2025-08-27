package lombok.core.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ExampleValueString.SCL.lombok */
public @interface ExampleValueString {
    String value();
}
