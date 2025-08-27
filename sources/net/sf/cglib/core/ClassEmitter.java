package net.sf.cglib.core;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.transform.ClassTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassEmitter.class */
public class ClassEmitter extends ClassTransformer {
    private ClassInfo classInfo;
    private Map fieldInfo;
    private static int hookCounter;
    private MethodVisitor rawStaticInit;
    private CodeEmitter staticInit;
    private CodeEmitter staticHook;
    private Signature staticHookSig;

    public ClassEmitter(ClassVisitor cv) {
        setTarget(cv);
    }

    public ClassEmitter() {
        super(262144);
    }

    @Override // net.sf.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor cv) {
        this.cv = cv;
        this.fieldInfo = new HashMap();
        this.staticHook = null;
        this.staticInit = null;
        this.staticHookSig = null;
    }

    private static synchronized int getNextHook() {
        int i = hookCounter + 1;
        hookCounter = i;
        return i;
    }

    public ClassInfo getClassInfo() {
        return this.classInfo;
    }

    public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String source) {
        Type classType = Type.getType(new StringBuffer().append(StandardRoles.L).append(className.replace('.', '/')).append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR).toString());
        this.classInfo = new ClassInfo(this, classType, superType, interfaces, access) { // from class: net.sf.cglib.core.ClassEmitter.1
            private final Type val$classType;
            private final Type val$superType;
            private final Type[] val$interfaces;
            private final int val$access;
            private final ClassEmitter this$0;

            {
                this.this$0 = this;
                this.val$classType = classType;
                this.val$superType = superType;
                this.val$interfaces = interfaces;
                this.val$access = access;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type getType() {
                return this.val$classType;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type getSuperType() {
                return this.val$superType != null ? this.val$superType : Constants.TYPE_OBJECT;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public Type[] getInterfaces() {
                return this.val$interfaces;
            }

            @Override // net.sf.cglib.core.ClassInfo
            public int getModifiers() {
                return this.val$access;
            }
        };
        this.cv.visit(version, access, this.classInfo.getType().getInternalName(), null, this.classInfo.getSuperType().getInternalName(), TypeUtils.toInternalNames(interfaces));
        if (source != null) {
            this.cv.visitSource(source, null);
        }
        init();
    }

    public CodeEmitter getStaticHook() {
        if (TypeUtils.isInterface(getAccess())) {
            throw new IllegalStateException("static hook is invalid for this class");
        }
        if (this.staticHook == null) {
            this.staticHookSig = new Signature(new StringBuffer().append("CGLIB$STATICHOOK").append(getNextHook()).toString(), "()V");
            this.staticHook = begin_method(8, this.staticHookSig, null);
            if (this.staticInit != null) {
                this.staticInit.invoke_static_this(this.staticHookSig);
            }
        }
        return this.staticHook;
    }

    protected void init() {
    }

    public int getAccess() {
        return this.classInfo.getModifiers();
    }

    public Type getClassType() {
        return this.classInfo.getType();
    }

    public Type getSuperType() {
        return this.classInfo.getSuperType();
    }

    public void end_class() {
        if (this.staticHook != null && this.staticInit == null) {
            begin_static();
        }
        if (this.staticInit != null) {
            this.staticHook.return_value();
            this.staticHook.end_method();
            this.rawStaticInit.visitInsn(177);
            this.rawStaticInit.visitMaxs(0, 0);
            this.staticHook = null;
            this.staticInit = null;
            this.staticHookSig = null;
        }
        this.cv.visitEnd();
    }

    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        if (this.classInfo == null) {
            throw new IllegalStateException(new StringBuffer().append("classInfo is null! ").append(this).toString());
        }
        MethodVisitor v = this.cv.visitMethod(access, sig.getName(), sig.getDescriptor(), null, TypeUtils.toInternalNames(exceptions));
        if (sig.equals(Constants.SIG_STATIC) && !TypeUtils.isInterface(getAccess())) {
            this.rawStaticInit = v;
            MethodVisitor wrapped = new MethodVisitor(this, 262144, v) { // from class: net.sf.cglib.core.ClassEmitter.2
                private final ClassEmitter this$0;

                {
                    this.this$0 = this;
                }

                @Override // org.objectweb.asm.MethodVisitor
                public void visitMaxs(int maxStack, int maxLocals) {
                }

                @Override // org.objectweb.asm.MethodVisitor
                public void visitInsn(int insn) {
                    if (insn != 177) {
                        super.visitInsn(insn);
                    }
                }
            };
            this.staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
            if (this.staticHook == null) {
                getStaticHook();
            } else {
                this.staticInit.invoke_static_this(this.staticHookSig);
            }
            return this.staticInit;
        }
        if (sig.equals(this.staticHookSig)) {
            return new CodeEmitter(this, this, v, access, sig, exceptions) { // from class: net.sf.cglib.core.ClassEmitter.3
                private final ClassEmitter this$0;

                {
                    this.this$0 = this;
                }

                @Override // net.sf.cglib.core.CodeEmitter
                public boolean isStaticHook() {
                    return true;
                }
            };
        }
        return new CodeEmitter(this, v, access, sig, exceptions);
    }

    public CodeEmitter begin_static() {
        return begin_method(8, Constants.SIG_STATIC, null);
    }

    public void declare_field(int access, String name, Type type, Object value) {
        FieldInfo existing = (FieldInfo) this.fieldInfo.get(name);
        FieldInfo info = new FieldInfo(access, name, type, value);
        if (existing != null) {
            if (!info.equals(existing)) {
                throw new IllegalArgumentException(new StringBuffer().append("Field \"").append(name).append("\" has been declared differently").toString());
            }
        } else {
            this.fieldInfo.put(name, info);
            this.cv.visitField(access, name, type.getDescriptor(), null, value);
        }
    }

    boolean isFieldDeclared(String name) {
        return this.fieldInfo.get(name) != null;
    }

    FieldInfo getFieldInfo(String name) {
        FieldInfo field = (FieldInfo) this.fieldInfo.get(name);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Field ").append(name).append(" is not declared in ").append(getClassType().getClassName()).toString());
        }
        return field;
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassEmitter$FieldInfo.class */
    static class FieldInfo {
        int access;
        String name;
        Type type;
        Object value;

        public FieldInfo(int access, String name, Type type, Object value) {
            this.access = access;
            this.name = name;
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof FieldInfo)) {
                return false;
            }
            FieldInfo other = (FieldInfo) o;
            if (this.access != other.access || !this.name.equals(other.name) || !this.type.equals(other.type)) {
                return false;
            }
            if ((this.value == null) ^ (other.value == null)) {
                return false;
            }
            if (this.value != null && !this.value.equals(other.value)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return ((this.access ^ this.name.hashCode()) ^ this.type.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        begin_class(version, access, name.replace('/', '.'), TypeUtils.fromInternalName(superName), TypeUtils.fromInternalNames(interfaces), null);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        end_class();
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        declare_field(access, name, Type.getType(desc), value);
        return null;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return begin_method(access, new Signature(name, desc), TypeUtils.fromInternalNames(exceptions));
    }
}
