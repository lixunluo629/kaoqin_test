package net.sf.cglib.core;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassNameReader.class */
public class ClassNameReader {
    private static final EarlyExitException EARLY_EXIT = new EarlyExitException(null);

    private ClassNameReader() {
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassNameReader$EarlyExitException.class */
    private static class EarlyExitException extends RuntimeException {
        private EarlyExitException() {
        }

        EarlyExitException(AnonymousClass1 x0) {
            this();
        }
    }

    public static String getClassName(ClassReader r) {
        return getClassInfo(r)[0];
    }

    public static String[] getClassInfo(ClassReader r) {
        List array = new ArrayList();
        try {
            r.accept(new ClassVisitor(262144, null, array) { // from class: net.sf.cglib.core.ClassNameReader.1
                private final List val$array;

                {
                    this.val$array = array;
                }

                @Override // org.objectweb.asm.ClassVisitor
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    this.val$array.add(name.replace('/', '.'));
                    if (superName != null) {
                        this.val$array.add(superName.replace('/', '.'));
                    }
                    for (String str : interfaces) {
                        this.val$array.add(str.replace('/', '.'));
                    }
                    throw ClassNameReader.EARLY_EXIT;
                }
            }, 6);
        } catch (EarlyExitException e) {
        }
        return (String[]) array.toArray(new String[0]);
    }
}
