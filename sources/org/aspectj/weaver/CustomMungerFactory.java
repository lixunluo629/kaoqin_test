package org.aspectj.weaver;

import java.util.Collection;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/CustomMungerFactory.class */
public interface CustomMungerFactory {
    Collection<ShadowMunger> createCustomShadowMungers(ResolvedType resolvedType);

    Collection<ConcreteTypeMunger> createCustomTypeMungers(ResolvedType resolvedType);

    Collection<ShadowMunger> getAllCreatedCustomShadowMungers();

    Collection<ConcreteTypeMunger> getAllCreatedCustomTypeMungers();
}
