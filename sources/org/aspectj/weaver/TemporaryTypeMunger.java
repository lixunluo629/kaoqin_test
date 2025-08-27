package org.aspectj.weaver;

import java.util.Map;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/TemporaryTypeMunger.class */
public class TemporaryTypeMunger extends ConcreteTypeMunger {
    public TemporaryTypeMunger(ResolvedTypeMunger munger, ResolvedType aspectType) {
        super(munger, aspectType);
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ConcreteTypeMunger parameterizeWith(Map parameterizationMap, World world) {
        throw new UnsupportedOperationException("Cannot be called on a TemporaryTypeMunger");
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ConcreteTypeMunger parameterizedFor(ResolvedType targetType) {
        throw new UnsupportedOperationException("Cannot be called on a TemporaryTypeMunger");
    }
}
