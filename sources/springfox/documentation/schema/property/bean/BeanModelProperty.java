package springfox.documentation.schema.property.bean;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMember;
import com.fasterxml.classmate.members.ResolvedMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.schema.property.BaseModelProperty;
import springfox.documentation.spi.schema.AlternateTypeProvider;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/bean/BeanModelProperty.class */
public class BeanModelProperty extends BaseModelProperty {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) BeanModelProperty.class);
    private final ResolvedMethod method;
    private final boolean isGetter;
    private TypeResolver typeResolver;

    public BeanModelProperty(String propertyName, ResolvedMethod method, boolean isGetter, TypeResolver typeResolver, AlternateTypeProvider alternateTypeProvider) {
        super(propertyName, alternateTypeProvider);
        this.method = method;
        this.isGetter = isGetter;
        this.typeResolver = typeResolver;
    }

    public static boolean accessorMemberIs(ResolvedMember method, String methodName) {
        return method.getRawMember().getName().equals(methodName);
    }

    @Override // springfox.documentation.schema.property.BaseModelProperty
    protected ResolvedType realType() {
        return paramOrReturnType(this.typeResolver, this.method);
    }

    private static ResolvedType adjustedToClassmateBug(TypeResolver typeResolver, ResolvedType resolvedType) {
        if (resolvedType.getErasedType().getTypeParameters().length > 0) {
            return resolvedType;
        }
        return typeResolver.resolve(resolvedType.getErasedType(), new Type[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ResolvedType paramOrReturnType(TypeResolver typeResolver, ResolvedMethod input) {
        if (Accessors.maybeAGetter((Method) input.getRawMember())) {
            LOG.debug("Evaluating unwrapped getter for member {}", ((Method) input.getRawMember()).getName());
            return adjustedToClassmateBug(typeResolver, input.getReturnType());
        }
        LOG.debug("Evaluating unwrapped setter for member {}", ((Method) input.getRawMember()).getName());
        return adjustedToClassmateBug(typeResolver, input.getArgumentType(0));
    }
}
