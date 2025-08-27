package net.sf.cglib.transform.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/AddDelegateTransformer.class */
public class AddDelegateTransformer extends ClassEmitterTransformer {
    private static final String DELEGATE = "$CGLIB_DELEGATE";
    private static final Signature CSTRUCT_OBJECT = TypeUtils.parseSignature("void <init>(Object)");
    private Class[] delegateIf;
    private Class delegateImpl;
    private Type delegateType;
    static Class class$java$lang$Object;

    public AddDelegateTransformer(Class[] delegateIf, Class delegateImpl) throws NoSuchMethodException, SecurityException {
        Class<?> clsClass$;
        try {
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Object == null) {
                clsClass$ = class$("java.lang.Object");
                class$java$lang$Object = clsClass$;
            } else {
                clsClass$ = class$java$lang$Object;
            }
            clsArr[0] = clsClass$;
            delegateImpl.getConstructor(clsArr);
            this.delegateIf = delegateIf;
            this.delegateImpl = delegateImpl;
            this.delegateType = Type.getType((Class<?>) delegateImpl);
        } catch (NoSuchMethodException e) {
            throw new CodeGenerationException(e);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) throws NoSuchMethodException, SecurityException {
        if (!TypeUtils.isInterface(access)) {
            Type[] all = TypeUtils.add(interfaces, TypeUtils.getTypes(this.delegateIf));
            super.begin_class(version, access, className, superType, all, sourceFile);
            declare_field(130, DELEGATE, this.delegateType, null);
            for (int i = 0; i < this.delegateIf.length; i++) {
                Method[] methods = this.delegateIf[i].getMethods();
                for (int j = 0; j < methods.length; j++) {
                    if (Modifier.isAbstract(methods[j].getModifiers())) {
                        addDelegate(methods[j]);
                    }
                }
            }
            return;
        }
        super.begin_class(version, access, className, superType, interfaces, sourceFile);
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        CodeEmitter e = super.begin_method(access, sig, exceptions);
        if (sig.getName().equals("<init>")) {
            return new CodeEmitter(this, e) { // from class: net.sf.cglib.transform.impl.AddDelegateTransformer.1
                private boolean transformInit = true;
                private final AddDelegateTransformer this$0;

                {
                    this.this$0 = this;
                }

                @Override // org.objectweb.asm.MethodVisitor
                public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                    super.visitMethodInsn(opcode, owner, name, desc);
                    if (this.transformInit && opcode == 183) {
                        load_this();
                        new_instance(this.this$0.delegateType);
                        dup();
                        load_this();
                        invoke_constructor(this.this$0.delegateType, AddDelegateTransformer.CSTRUCT_OBJECT);
                        putfield(AddDelegateTransformer.DELEGATE);
                        this.transformInit = false;
                    }
                }
            };
        }
        return e;
    }

    private void addDelegate(Method m) throws NoSuchMethodException, SecurityException {
        try {
            Method delegate = this.delegateImpl.getMethod(m.getName(), m.getParameterTypes());
            if (!delegate.getReturnType().getName().equals(m.getReturnType().getName())) {
                throw new IllegalArgumentException(new StringBuffer().append("Invalid delegate signature ").append(delegate).toString());
            }
            Signature sig = ReflectUtils.getSignature(m);
            Type[] exceptions = TypeUtils.getTypes(m.getExceptionTypes());
            CodeEmitter e = super.begin_method(1, sig, exceptions);
            e.load_this();
            e.getfield(DELEGATE);
            e.load_args();
            e.invoke_virtual(this.delegateType, sig);
            e.return_value();
            e.end_method();
        } catch (NoSuchMethodException e2) {
            throw new CodeGenerationException(e2);
        }
    }
}
