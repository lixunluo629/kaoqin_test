package net.sf.cglib.transform;

import net.sf.cglib.core.ClassGenerator;
import org.objectweb.asm.ClassReader;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/TransformingClassLoader.class */
public class TransformingClassLoader extends AbstractClassLoader {
    private ClassTransformerFactory t;

    public TransformingClassLoader(ClassLoader parent, ClassFilter filter, ClassTransformerFactory t) {
        super(parent, parent, filter);
        this.t = t;
    }

    @Override // net.sf.cglib.transform.AbstractClassLoader
    protected ClassGenerator getGenerator(ClassReader r) {
        ClassTransformer t2 = this.t.newInstance();
        return new TransformingClassGenerator(super.getGenerator(r), t2);
    }
}
