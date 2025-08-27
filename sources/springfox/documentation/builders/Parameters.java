package springfox.documentation.builders;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import springfox.documentation.service.Parameter;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/Parameters.class */
public class Parameters {
    private Parameters() {
        throw new UnsupportedOperationException();
    }

    public static Predicate<Parameter> withName(final String name) {
        return new Predicate<Parameter>() { // from class: springfox.documentation.builders.Parameters.1
            @Override // com.google.common.base.Predicate
            public boolean apply(Parameter input) {
                return name.equals(input.getName());
            }
        };
    }

    public static Function<Parameter, String> toParameterName() {
        return new Function<Parameter, String>() { // from class: springfox.documentation.builders.Parameters.2
            @Override // com.google.common.base.Function
            public String apply(Parameter input) {
                return input.getName();
            }
        };
    }
}
