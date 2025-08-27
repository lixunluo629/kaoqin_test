package org.springframework.cglib.transform;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/ClassFilterTransformer.class */
public class ClassFilterTransformer extends AbstractClassFilterTransformer {
    private ClassFilter filter;

    public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass) {
        super(pass);
        this.filter = filter;
    }

    @Override // org.springframework.cglib.transform.AbstractClassFilterTransformer
    protected boolean accept(int version, int access, String name, String signature, String superName, String[] interfaces) {
        return this.filter.accept(name.replace('/', '.'));
    }
}
