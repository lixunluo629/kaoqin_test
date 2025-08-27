package net.sf.cglib.transform.impl;

import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/InterceptFieldTransformer.class */
public class InterceptFieldTransformer extends ClassEmitterTransformer {
    private static final String CALLBACK_FIELD = "$CGLIB_READ_WRITE_CALLBACK";
    private static final Type CALLBACK = TypeUtils.parseType("net.sf.cglib.transform.impl.InterceptFieldCallback");
    private static final Type ENABLED = TypeUtils.parseType("net.sf.cglib.transform.impl.InterceptFieldEnabled");
    private static final Signature ENABLED_SET = new Signature("setInterceptFieldCallback", Type.VOID_TYPE, new Type[]{CALLBACK});
    private static final Signature ENABLED_GET = new Signature("getInterceptFieldCallback", CALLBACK, new Type[0]);
    private InterceptFieldFilter filter;

    public InterceptFieldTransformer(InterceptFieldFilter filter) {
        this.filter = filter;
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
        if (!TypeUtils.isInterface(access)) {
            super.begin_class(version, access, className, superType, TypeUtils.add(interfaces, ENABLED), sourceFile);
            super.declare_field(130, CALLBACK_FIELD, CALLBACK, null);
            CodeEmitter e = super.begin_method(1, ENABLED_GET, null);
            e.load_this();
            e.getfield(CALLBACK_FIELD);
            e.return_value();
            e.end_method();
            CodeEmitter e2 = super.begin_method(1, ENABLED_SET, null);
            e2.load_this();
            e2.load_arg(0);
            e2.putfield(CALLBACK_FIELD);
            e2.return_value();
            e2.end_method();
            return;
        }
        super.begin_class(version, access, className, superType, interfaces, sourceFile);
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void declare_field(int access, String name, Type type, Object value) {
        super.declare_field(access, name, type, value);
        if (!TypeUtils.isStatic(access)) {
            if (this.filter.acceptRead(getClassType(), name)) {
                addReadMethod(name, type);
            }
            if (this.filter.acceptWrite(getClassType(), name)) {
                addWriteMethod(name, type);
            }
        }
    }

    private void addReadMethod(String name, Type type) {
        CodeEmitter e = super.begin_method(1, readMethodSig(name, type.getDescriptor()), null);
        e.load_this();
        e.getfield(name);
        e.load_this();
        e.invoke_interface(ENABLED, ENABLED_GET);
        Label intercept = e.make_label();
        e.ifnonnull(intercept);
        e.return_value();
        e.mark(intercept);
        Local result = e.make_local(type);
        e.store_local(result);
        e.load_this();
        e.invoke_interface(ENABLED, ENABLED_GET);
        e.load_this();
        e.push(name);
        e.load_local(result);
        e.invoke_interface(CALLBACK, readCallbackSig(type));
        if (!TypeUtils.isPrimitive(type)) {
            e.checkcast(type);
        }
        e.return_value();
        e.end_method();
    }

    private void addWriteMethod(String name, Type type) {
        CodeEmitter e = super.begin_method(1, writeMethodSig(name, type.getDescriptor()), null);
        e.load_this();
        e.dup();
        e.invoke_interface(ENABLED, ENABLED_GET);
        Label skip = e.make_label();
        e.ifnull(skip);
        e.load_this();
        e.invoke_interface(ENABLED, ENABLED_GET);
        e.load_this();
        e.push(name);
        e.load_this();
        e.getfield(name);
        e.load_arg(0);
        e.invoke_interface(CALLBACK, writeCallbackSig(type));
        if (!TypeUtils.isPrimitive(type)) {
            e.checkcast(type);
        }
        Label go = e.make_label();
        e.goTo(go);
        e.mark(skip);
        e.load_arg(0);
        e.mark(go);
        e.putfield(name);
        e.return_value();
        e.end_method();
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        return new CodeEmitter(this, super.begin_method(access, sig, exceptions)) { // from class: net.sf.cglib.transform.impl.InterceptFieldTransformer.1
            private final InterceptFieldTransformer this$0;

            {
                this.this$0 = this;
            }

            @Override // org.objectweb.asm.MethodVisitor
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                Type towner = TypeUtils.fromInternalName(owner);
                switch (opcode) {
                    case 180:
                        if (this.this$0.filter.acceptRead(towner, name)) {
                            helper(towner, InterceptFieldTransformer.readMethodSig(name, desc));
                            return;
                        }
                        break;
                    case 181:
                        if (this.this$0.filter.acceptWrite(towner, name)) {
                            helper(towner, InterceptFieldTransformer.writeMethodSig(name, desc));
                            return;
                        }
                        break;
                }
                super.visitFieldInsn(opcode, owner, name, desc);
            }

            private void helper(Type owner, Signature sig2) {
                invoke_virtual(owner, sig2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Signature readMethodSig(String name, String desc) {
        return new Signature(new StringBuffer().append("$cglib_read_").append(name).toString(), new StringBuffer().append("()").append(desc).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Signature writeMethodSig(String name, String desc) {
        return new Signature(new StringBuffer().append("$cglib_write_").append(name).toString(), new StringBuffer().append("(").append(desc).append(")V").toString());
    }

    private static Signature readCallbackSig(Type type) {
        Type remap = remap(type);
        return new Signature(new StringBuffer().append("read").append(callbackName(remap)).toString(), remap, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap});
    }

    private static Signature writeCallbackSig(Type type) {
        Type remap = remap(type);
        return new Signature(new StringBuffer().append("write").append(callbackName(remap)).toString(), remap, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap, remap});
    }

    private static Type remap(Type type) {
        switch (type.getSort()) {
            case 9:
            case 10:
                return Constants.TYPE_OBJECT;
            default:
                return type;
        }
    }

    private static String callbackName(Type type) {
        return type == Constants.TYPE_OBJECT ? "Object" : TypeUtils.upperFirst(TypeUtils.getClassName(type));
    }
}
