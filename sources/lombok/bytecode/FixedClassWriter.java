package lombok.bytecode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/* loaded from: lombok-1.16.22.jar:lombok/bytecode/FixedClassWriter.SCL.lombok */
class FixedClassWriter extends ClassWriter {
    FixedClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override // org.objectweb.asm.ClassWriter
    protected String getCommonSuperClass(String type1, String type2) {
        try {
            return super.getCommonSuperClass(type1, type2);
        } catch (OutOfMemoryError e) {
            throw e;
        } catch (Throwable unused) {
            return "java/lang/Object";
        }
    }
}
