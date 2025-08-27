package org.aspectj.weaver.bcel.asm;

import aj.org.objectweb.asm.ClassReader;
import aj.org.objectweb.asm.ClassVisitor;
import aj.org.objectweb.asm.ClassWriter;
import aj.org.objectweb.asm.MethodVisitor;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/asm/StackMapAdder.class */
public class StackMapAdder {
    public static byte[] addStackMaps(World world, byte[] data) {
        try {
            ClassReader cr = new ClassReader(data);
            ClassWriter cw = new AspectJConnectClassWriter(cr, world);
            ClassVisitor cv = new AspectJClassVisitor(cw);
            cr.accept(cv, 0);
            return cw.toByteArray();
        } catch (Throwable t) {
            System.err.println("AspectJ Internal Error: unable to add stackmap attributes. " + t.getMessage());
            AsmDetector.isAsmAround = false;
            return data;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/asm/StackMapAdder$AspectJClassVisitor.class */
    private static class AspectJClassVisitor extends ClassVisitor {
        public AspectJClassVisitor(ClassVisitor classwriter) {
            super(327680, classwriter);
        }

        @Override // aj.org.objectweb.asm.ClassVisitor
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            return new AJMethodVisitor(mv);
        }

        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/asm/StackMapAdder$AspectJClassVisitor$AJMethodVisitor.class */
        static class AJMethodVisitor extends MethodVisitor {
            public AJMethodVisitor(MethodVisitor mv) {
                super(327680, mv);
            }
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/asm/StackMapAdder$AspectJConnectClassWriter.class */
    private static class AspectJConnectClassWriter extends ClassWriter {
        private final World world;

        public AspectJConnectClassWriter(ClassReader cr, World w) {
            super(cr, 2);
            this.world = w;
        }

        @Override // aj.org.objectweb.asm.ClassWriter
        protected String getCommonSuperClass(String type1, String type2) {
            ResolvedType resolvedType1 = this.world.resolve(UnresolvedType.forName(type1.replace('/', '.')));
            ResolvedType resolvedType2 = this.world.resolve(UnresolvedType.forName(type2.replace('/', '.')));
            if (resolvedType1.isAssignableFrom(resolvedType2)) {
                return type1;
            }
            if (resolvedType2.isAssignableFrom(resolvedType1)) {
                return type2;
            }
            if (resolvedType1.isInterface() || resolvedType2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                resolvedType1 = resolvedType1.getSuperclass();
                if (resolvedType1.isParameterizedOrGenericType()) {
                    resolvedType1 = resolvedType1.getRawType();
                }
            } while (!resolvedType1.isAssignableFrom(resolvedType2));
            return resolvedType1.getRawName().replace('.', '/');
        }
    }
}
