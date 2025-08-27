package org.aspectj.weaver.patterns;

import java.util.List;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/NameBindingPointcut.class */
public abstract class NameBindingPointcut extends Pointcut {
    public abstract List<BindingTypePattern> getBindingTypePatterns();

    public abstract List<BindingPattern> getBindingAnnotationTypePatterns();

    protected Test exposeStateForVar(Var var, TypePattern type, ExposedState state, World world) {
        if (type instanceof BindingTypePattern) {
            BindingTypePattern b = (BindingTypePattern) type;
            state.set(b.getFormalIndex(), var);
        }
        ResolvedType myType = type.getExactType().resolve(world);
        if (myType.isParameterizedType()) {
            myType = myType.getRawType();
        }
        return Test.makeInstanceof(var, myType.resolve(world));
    }
}
