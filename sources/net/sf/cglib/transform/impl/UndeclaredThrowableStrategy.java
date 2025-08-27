package net.sf.cglib.transform.impl;

import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassTransformer;
import net.sf.cglib.transform.MethodFilter;
import net.sf.cglib.transform.MethodFilterTransformer;
import net.sf.cglib.transform.TransformingClassGenerator;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/UndeclaredThrowableStrategy.class */
public class UndeclaredThrowableStrategy extends DefaultGeneratorStrategy {
    private Class wrapper;
    private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() { // from class: net.sf.cglib.transform.impl.UndeclaredThrowableStrategy.1
        @Override // net.sf.cglib.transform.MethodFilter
        public boolean accept(int access, String name, String desc, String signature, String[] exceptions) {
            return !TypeUtils.isPrivate(access) && name.indexOf(36) < 0;
        }
    };

    public UndeclaredThrowableStrategy(Class wrapper) {
        this.wrapper = wrapper;
    }

    @Override // net.sf.cglib.core.DefaultGeneratorStrategy
    protected ClassGenerator transform(ClassGenerator cg) throws Exception {
        ClassTransformer tr = new UndeclaredThrowableTransformer(this.wrapper);
        return new TransformingClassGenerator(cg, new MethodFilterTransformer(TRANSFORM_FILTER, tr));
    }
}
