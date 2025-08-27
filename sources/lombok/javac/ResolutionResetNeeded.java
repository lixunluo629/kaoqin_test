package lombok.javac;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: lombok-1.16.22.jar:lombok/javac/ResolutionResetNeeded.SCL.lombok */
public @interface ResolutionResetNeeded {
}
