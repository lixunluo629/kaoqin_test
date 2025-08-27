package net.sf.cglib.transform.impl;

import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.ProcessSwitchCallback;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/impl/FieldProviderTransformer.class */
public class FieldProviderTransformer extends ClassEmitterTransformer {
    private static final String FIELD_NAMES = "CGLIB$FIELD_NAMES";
    private static final String FIELD_TYPES = "CGLIB$FIELD_TYPES";
    private static final Type FIELD_PROVIDER = TypeUtils.parseType("net.sf.cglib.transform.impl.FieldProvider");
    private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
    private static final Signature PROVIDER_GET = TypeUtils.parseSignature("Object getField(String)");
    private static final Signature PROVIDER_SET = TypeUtils.parseSignature("void setField(String, Object)");
    private static final Signature PROVIDER_SET_BY_INDEX = TypeUtils.parseSignature("void setField(int, Object)");
    private static final Signature PROVIDER_GET_BY_INDEX = TypeUtils.parseSignature("Object getField(int)");
    private static final Signature PROVIDER_GET_TYPES = TypeUtils.parseSignature("Class[] getFieldTypes()");
    private static final Signature PROVIDER_GET_NAMES = TypeUtils.parseSignature("String[] getFieldNames()");
    private int access;
    private Map fields;

    @Override // net.sf.cglib.core.ClassEmitter
    public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
        if (!TypeUtils.isAbstract(access)) {
            interfaces = TypeUtils.add(interfaces, FIELD_PROVIDER);
        }
        this.access = access;
        this.fields = new HashMap();
        super.begin_class(version, access, className, superType, interfaces, sourceFile);
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void declare_field(int access, String name, Type type, Object value) {
        super.declare_field(access, name, type, value);
        if (!TypeUtils.isStatic(access)) {
            this.fields.put(name, type);
        }
    }

    @Override // net.sf.cglib.core.ClassEmitter
    public void end_class() {
        if (!TypeUtils.isInterface(this.access)) {
            try {
                generate();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new CodeGenerationException(e2);
            }
        }
        super.end_class();
    }

    private void generate() throws Exception {
        String[] names = (String[]) this.fields.keySet().toArray(new String[this.fields.size()]);
        int[] indexes = new int[names.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        super.declare_field(26, FIELD_NAMES, Constants.TYPE_STRING_ARRAY, null);
        super.declare_field(26, FIELD_TYPES, Constants.TYPE_CLASS_ARRAY, null);
        initFieldProvider(names);
        getNames();
        getTypes();
        getField(names);
        setField(names);
        setByIndex(names, indexes);
        getByIndex(names, indexes);
    }

    private void initFieldProvider(String[] names) {
        CodeEmitter e = getStaticHook();
        EmitUtils.push_object(e, names);
        e.putstatic(getClassType(), FIELD_NAMES, Constants.TYPE_STRING_ARRAY);
        e.push(names.length);
        e.newarray(Constants.TYPE_CLASS);
        e.dup();
        for (int i = 0; i < names.length; i++) {
            e.dup();
            e.push(i);
            Type type = (Type) this.fields.get(names[i]);
            EmitUtils.load_class(e, type);
            e.aastore();
        }
        e.putstatic(getClassType(), FIELD_TYPES, Constants.TYPE_CLASS_ARRAY);
    }

    private void getNames() {
        CodeEmitter e = super.begin_method(1, PROVIDER_GET_NAMES, null);
        e.getstatic(getClassType(), FIELD_NAMES, Constants.TYPE_STRING_ARRAY);
        e.return_value();
        e.end_method();
    }

    private void getTypes() {
        CodeEmitter e = super.begin_method(1, PROVIDER_GET_TYPES, null);
        e.getstatic(getClassType(), FIELD_TYPES, Constants.TYPE_CLASS_ARRAY);
        e.return_value();
        e.end_method();
    }

    private void setByIndex(String[] names, int[] indexes) throws Exception {
        CodeEmitter e = super.begin_method(1, PROVIDER_SET_BY_INDEX, null);
        e.load_this();
        e.load_arg(1);
        e.load_arg(0);
        e.process_switch(indexes, new ProcessSwitchCallback(this, names, e) { // from class: net.sf.cglib.transform.impl.FieldProviderTransformer.1
            private final String[] val$names;
            private final CodeEmitter val$e;
            private final FieldProviderTransformer this$0;

            {
                this.this$0 = this;
                this.val$names = names;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) throws Exception {
                Type type = (Type) this.this$0.fields.get(this.val$names[key]);
                this.val$e.unbox(type);
                this.val$e.putfield(this.val$names[key]);
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() throws Exception {
                this.val$e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
            }
        });
        e.end_method();
    }

    private void getByIndex(String[] names, int[] indexes) throws Exception {
        CodeEmitter e = super.begin_method(1, PROVIDER_GET_BY_INDEX, null);
        e.load_this();
        e.load_arg(0);
        e.process_switch(indexes, new ProcessSwitchCallback(this, names, e) { // from class: net.sf.cglib.transform.impl.FieldProviderTransformer.2
            private final String[] val$names;
            private final CodeEmitter val$e;
            private final FieldProviderTransformer this$0;

            {
                this.this$0 = this;
                this.val$names = names;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label end) throws Exception {
                Type type = (Type) this.this$0.fields.get(this.val$names[key]);
                this.val$e.getfield(this.val$names[key]);
                this.val$e.box(type);
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() throws Exception {
                this.val$e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
            }
        });
        e.end_method();
    }

    private void getField(String[] names) throws Exception {
        CodeEmitter e = begin_method(1, PROVIDER_GET, null);
        e.load_this();
        e.load_arg(0);
        EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback(this, e) { // from class: net.sf.cglib.transform.impl.FieldProviderTransformer.3
            private final CodeEmitter val$e;
            private final FieldProviderTransformer this$0;

            {
                this.this$0 = this;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                Type type = (Type) this.this$0.fields.get(key);
                this.val$e.getfield((String) key);
                this.val$e.box(type);
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
            }
        });
        e.end_method();
    }

    private void setField(String[] names) throws Exception {
        CodeEmitter e = begin_method(1, PROVIDER_SET, null);
        e.load_this();
        e.load_arg(1);
        e.load_arg(0);
        EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback(this, e) { // from class: net.sf.cglib.transform.impl.FieldProviderTransformer.4
            private final CodeEmitter val$e;
            private final FieldProviderTransformer this$0;

            {
                this.this$0 = this;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                Type type = (Type) this.this$0.fields.get(key);
                this.val$e.unbox(type);
                this.val$e.putfield((String) key);
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
            }
        });
        e.end_method();
    }
}
