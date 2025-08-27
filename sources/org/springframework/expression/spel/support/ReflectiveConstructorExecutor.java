package org.springframework.expression.spel.support;

import java.lang.reflect.Constructor;
import org.springframework.expression.AccessException;
import org.springframework.expression.ConstructorExecutor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/ReflectiveConstructorExecutor.class */
public class ReflectiveConstructorExecutor implements ConstructorExecutor {
    private final Constructor<?> ctor;
    private final Integer varargsPosition;

    public ReflectiveConstructorExecutor(Constructor<?> ctor) {
        this.ctor = ctor;
        if (ctor.isVarArgs()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();
            this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
        } else {
            this.varargsPosition = null;
        }
    }

    @Override // org.springframework.expression.ConstructorExecutor
    public TypedValue execute(EvaluationContext context, Object... arguments) throws AccessException {
        if (arguments != null) {
            try {
                ReflectionHelper.convertArguments(context.getTypeConverter(), arguments, this.ctor, this.varargsPosition);
            } catch (Exception ex) {
                throw new AccessException("Problem invoking constructor: " + this.ctor, ex);
            }
        }
        if (this.ctor.isVarArgs()) {
            arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor.getParameterTypes(), arguments);
        }
        ReflectionUtils.makeAccessible(this.ctor);
        return new TypedValue(this.ctor.newInstance(arguments));
    }

    public Constructor<?> getConstructor() {
        return this.ctor;
    }
}
