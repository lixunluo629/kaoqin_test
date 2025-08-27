package net.sf.cglib.core;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/EmitUtils.class */
public class EmitUtils {
    private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
    private static final Signature CSTRUCT_THROWABLE = TypeUtils.parseConstructor("Throwable");
    private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
    private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
    private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
    private static final Signature STRING_LENGTH = TypeUtils.parseSignature("int length()");
    private static final Signature STRING_CHAR_AT = TypeUtils.parseSignature("char charAt(int)");
    private static final Signature FOR_NAME = TypeUtils.parseSignature("Class forName(String)");
    private static final Signature DOUBLE_TO_LONG_BITS = TypeUtils.parseSignature("long doubleToLongBits(double)");
    private static final Signature FLOAT_TO_INT_BITS = TypeUtils.parseSignature("int floatToIntBits(float)");
    private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
    private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
    private static final Signature APPEND_INT = TypeUtils.parseSignature("StringBuffer append(int)");
    private static final Signature APPEND_DOUBLE = TypeUtils.parseSignature("StringBuffer append(double)");
    private static final Signature APPEND_FLOAT = TypeUtils.parseSignature("StringBuffer append(float)");
    private static final Signature APPEND_CHAR = TypeUtils.parseSignature("StringBuffer append(char)");
    private static final Signature APPEND_LONG = TypeUtils.parseSignature("StringBuffer append(long)");
    private static final Signature APPEND_BOOLEAN = TypeUtils.parseSignature("StringBuffer append(boolean)");
    private static final Signature LENGTH = TypeUtils.parseSignature("int length()");
    private static final Signature SET_LENGTH = TypeUtils.parseSignature("void setLength(int)");
    private static final Signature GET_DECLARED_METHOD = TypeUtils.parseSignature("java.lang.reflect.Method getDeclaredMethod(String, Class[])");
    public static final ArrayDelimiters DEFAULT_DELIMITERS = new ArrayDelimiters("{", ", ", "}");
    static Class class$org$objectweb$asm$Type;
    static Class class$java$lang$Class;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/EmitUtils$ParameterTyper.class */
    private interface ParameterTyper {
        Type[] getParameterTypes(MethodInfo methodInfo);
    }

    private EmitUtils() {
    }

    public static void factory_method(ClassEmitter ce, Signature sig) {
        CodeEmitter e = ce.begin_method(1, sig, null);
        e.new_instance_this();
        e.dup();
        e.load_args();
        e.invoke_constructor_this(TypeUtils.parseConstructor(sig.getArgumentTypes()));
        e.return_value();
        e.end_method();
    }

    public static void null_constructor(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, CSTRUCT_NULL, null);
        e.load_this();
        e.super_invoke_constructor();
        e.return_value();
        e.end_method();
    }

    public static void process_array(CodeEmitter e, Type type, ProcessArrayCallback callback) {
        Type componentType = TypeUtils.getComponentType(type);
        Local array = e.make_local();
        Local loopvar = e.make_local(Type.INT_TYPE);
        Label loopbody = e.make_label();
        Label checkloop = e.make_label();
        e.store_local(array);
        e.push(0);
        e.store_local(loopvar);
        e.goTo(checkloop);
        e.mark(loopbody);
        e.load_local(array);
        e.load_local(loopvar);
        e.array_load(componentType);
        callback.processElement(componentType);
        e.iinc(loopvar, 1);
        e.mark(checkloop);
        e.load_local(loopvar);
        e.load_local(array);
        e.arraylength();
        e.if_icmp(155, loopbody);
    }

    public static void process_arrays(CodeEmitter e, Type type, ProcessArrayCallback callback) {
        Type componentType = TypeUtils.getComponentType(type);
        Local array1 = e.make_local();
        Local array2 = e.make_local();
        Local loopvar = e.make_local(Type.INT_TYPE);
        Label loopbody = e.make_label();
        Label checkloop = e.make_label();
        e.store_local(array1);
        e.store_local(array2);
        e.push(0);
        e.store_local(loopvar);
        e.goTo(checkloop);
        e.mark(loopbody);
        e.load_local(array1);
        e.load_local(loopvar);
        e.array_load(componentType);
        e.load_local(array2);
        e.load_local(loopvar);
        e.array_load(componentType);
        callback.processElement(componentType);
        e.iinc(loopvar, 1);
        e.mark(checkloop);
        e.load_local(loopvar);
        e.load_local(array1);
        e.arraylength();
        e.if_icmp(155, loopbody);
    }

    public static void string_switch(CodeEmitter e, String[] strings, int switchStyle, ObjectSwitchCallback callback) {
        try {
            switch (switchStyle) {
                case 0:
                    string_switch_trie(e, strings, callback);
                    break;
                case 1:
                    string_switch_hash(e, strings, callback, false);
                    break;
                case 2:
                    string_switch_hash(e, strings, callback, true);
                    break;
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("unknown switch style ").append(switchStyle).toString());
            }
        } catch (Error ex) {
            throw ex;
        } catch (RuntimeException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            throw new CodeGenerationException(ex3);
        }
    }

    private static void string_switch_trie(CodeEmitter e, String[] strings, ObjectSwitchCallback callback) throws Exception {
        Label def = e.make_label();
        Label end = e.make_label();
        Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new Transformer() { // from class: net.sf.cglib.core.EmitUtils.1
            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                return new Integer(((String) value).length());
            }
        });
        e.dup();
        e.invoke_virtual(Constants.TYPE_STRING, STRING_LENGTH);
        e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback(buckets, e, callback, def, end) { // from class: net.sf.cglib.core.EmitUtils.2
            private final Map val$buckets;
            private final CodeEmitter val$e;
            private final ObjectSwitchCallback val$callback;
            private final Label val$def;
            private final Label val$end;

            {
                this.val$buckets = buckets;
                this.val$e = e;
                this.val$callback = callback;
                this.val$def = def;
                this.val$end = end;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label ignore_end) throws Exception {
                List bucket = (List) this.val$buckets.get(new Integer(key));
                EmitUtils.stringSwitchHelper(this.val$e, bucket, this.val$callback, this.val$def, this.val$end, 0);
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() {
                this.val$e.goTo(this.val$def);
            }
        });
        e.mark(def);
        e.pop();
        callback.processDefault();
        e.mark(end);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stringSwitchHelper(CodeEmitter e, List strings, ObjectSwitchCallback callback, Label def, Label end, int index) throws Exception {
        int len = ((String) strings.get(0)).length();
        Map buckets = CollectionUtils.bucket(strings, new Transformer(index) { // from class: net.sf.cglib.core.EmitUtils.3
            private final int val$index;

            {
                this.val$index = index;
            }

            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                return new Integer(((String) value).charAt(this.val$index));
            }
        });
        e.dup();
        e.push(index);
        e.invoke_virtual(Constants.TYPE_STRING, STRING_CHAR_AT);
        e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback(buckets, index, len, e, callback, end, def) { // from class: net.sf.cglib.core.EmitUtils.4
            private final Map val$buckets;
            private final int val$index;
            private final int val$len;
            private final CodeEmitter val$e;
            private final ObjectSwitchCallback val$callback;
            private final Label val$end;
            private final Label val$def;

            {
                this.val$buckets = buckets;
                this.val$index = index;
                this.val$len = len;
                this.val$e = e;
                this.val$callback = callback;
                this.val$end = end;
                this.val$def = def;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label ignore_end) throws Exception {
                List bucket = (List) this.val$buckets.get(new Integer(key));
                if (this.val$index + 1 != this.val$len) {
                    EmitUtils.stringSwitchHelper(this.val$e, bucket, this.val$callback, this.val$def, this.val$end, this.val$index + 1);
                } else {
                    this.val$e.pop();
                    this.val$callback.processCase(bucket.get(0), this.val$end);
                }
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() {
                this.val$e.goTo(this.val$def);
            }
        });
    }

    static int[] getSwitchKeys(Map buckets) {
        int[] keys = new int[buckets.size()];
        int index = 0;
        Iterator it = buckets.keySet().iterator();
        while (it.hasNext()) {
            int i = index;
            index++;
            keys[i] = ((Integer) it.next()).intValue();
        }
        Arrays.sort(keys);
        return keys;
    }

    private static void string_switch_hash(CodeEmitter e, String[] strings, ObjectSwitchCallback callback, boolean skipEquals) throws Exception {
        Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new Transformer() { // from class: net.sf.cglib.core.EmitUtils.5
            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                return new Integer(value.hashCode());
            }
        });
        Label def = e.make_label();
        Label end = e.make_label();
        e.dup();
        e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
        e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback(buckets, skipEquals, e, callback, end, def) { // from class: net.sf.cglib.core.EmitUtils.6
            private final Map val$buckets;
            private final boolean val$skipEquals;
            private final CodeEmitter val$e;
            private final ObjectSwitchCallback val$callback;
            private final Label val$end;
            private final Label val$def;

            {
                this.val$buckets = buckets;
                this.val$skipEquals = skipEquals;
                this.val$e = e;
                this.val$callback = callback;
                this.val$end = end;
                this.val$def = def;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label ignore_end) throws Exception {
                List bucket = (List) this.val$buckets.get(new Integer(key));
                Label next = null;
                if (this.val$skipEquals && bucket.size() == 1) {
                    if (this.val$skipEquals) {
                        this.val$e.pop();
                    }
                    this.val$callback.processCase((String) bucket.get(0), this.val$end);
                    return;
                }
                Iterator it = bucket.iterator();
                while (it.hasNext()) {
                    String string = (String) it.next();
                    if (next != null) {
                        this.val$e.mark(next);
                    }
                    if (it.hasNext()) {
                        this.val$e.dup();
                    }
                    this.val$e.push(string);
                    this.val$e.invoke_virtual(Constants.TYPE_OBJECT, EmitUtils.EQUALS);
                    if (it.hasNext()) {
                        CodeEmitter codeEmitter = this.val$e;
                        CodeEmitter codeEmitter2 = this.val$e;
                        Label labelMake_label = this.val$e.make_label();
                        next = labelMake_label;
                        codeEmitter.if_jump(153, labelMake_label);
                        this.val$e.pop();
                    } else {
                        CodeEmitter codeEmitter3 = this.val$e;
                        CodeEmitter codeEmitter4 = this.val$e;
                        codeEmitter3.if_jump(153, this.val$def);
                    }
                    this.val$callback.processCase(string, this.val$end);
                }
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() {
                this.val$e.pop();
            }
        });
        e.mark(def);
        callback.processDefault();
        e.mark(end);
    }

    public static void load_class_this(CodeEmitter e) {
        load_class_helper(e, e.getClassEmitter().getClassType());
    }

    public static void load_class(CodeEmitter e, Type type) {
        if (TypeUtils.isPrimitive(type)) {
            if (type == Type.VOID_TYPE) {
                throw new IllegalArgumentException("cannot load void type");
            }
            e.getstatic(TypeUtils.getBoxedType(type), "TYPE", Constants.TYPE_CLASS);
            return;
        }
        load_class_helper(e, type);
    }

    private static void load_class_helper(CodeEmitter e, Type type) {
        if (e.isStaticHook()) {
            e.push(TypeUtils.emulateClassGetName(type));
            e.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
            return;
        }
        ClassEmitter ce = e.getClassEmitter();
        String typeName = TypeUtils.emulateClassGetName(type);
        String fieldName = new StringBuffer().append("CGLIB$load_class$").append(TypeUtils.escapeType(typeName)).toString();
        if (!ce.isFieldDeclared(fieldName)) {
            ce.declare_field(26, fieldName, Constants.TYPE_CLASS, null);
            CodeEmitter hook = ce.getStaticHook();
            hook.push(typeName);
            hook.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
            hook.putstatic(ce.getClassType(), fieldName, Constants.TYPE_CLASS);
        }
        e.getfield(fieldName);
    }

    public static void push_array(CodeEmitter e, Object[] array) {
        e.push(array.length);
        e.newarray(Type.getType((Class<?>) remapComponentType(array.getClass().getComponentType())));
        for (int i = 0; i < array.length; i++) {
            e.dup();
            e.push(i);
            push_object(e, array[i]);
            e.aastore();
        }
    }

    private static Class remapComponentType(Class componentType) {
        Class clsClass$;
        if (class$org$objectweb$asm$Type == null) {
            clsClass$ = class$("org.objectweb.asm.Type");
            class$org$objectweb$asm$Type = clsClass$;
        } else {
            clsClass$ = class$org$objectweb$asm$Type;
        }
        if (componentType.equals(clsClass$)) {
            if (class$java$lang$Class != null) {
                return class$java$lang$Class;
            }
            Class clsClass$2 = class$("java.lang.Class");
            class$java$lang$Class = clsClass$2;
            return clsClass$2;
        }
        return componentType;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static void push_object(CodeEmitter e, Object obj) {
        if (obj == null) {
            e.aconst_null();
            return;
        }
        Class type = obj.getClass();
        if (type.isArray()) {
            push_array(e, (Object[]) obj);
            return;
        }
        if (obj instanceof String) {
            e.push((String) obj);
            return;
        }
        if (obj instanceof Type) {
            load_class(e, (Type) obj);
            return;
        }
        if (obj instanceof Class) {
            load_class(e, Type.getType((Class<?>) obj));
            return;
        }
        if (obj instanceof BigInteger) {
            e.new_instance(Constants.TYPE_BIG_INTEGER);
            e.dup();
            e.push(obj.toString());
            e.invoke_constructor(Constants.TYPE_BIG_INTEGER);
            return;
        }
        if (obj instanceof BigDecimal) {
            e.new_instance(Constants.TYPE_BIG_DECIMAL);
            e.dup();
            e.push(obj.toString());
            e.invoke_constructor(Constants.TYPE_BIG_DECIMAL);
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("unknown type: ").append(obj.getClass()).toString());
    }

    public static void hash_code(CodeEmitter e, Type type, int multiplier, Customizer customizer) {
        if (TypeUtils.isArray(type)) {
            hash_array(e, type, multiplier, customizer);
            return;
        }
        e.swap(Type.INT_TYPE, type);
        e.push(multiplier);
        e.math(104, Type.INT_TYPE);
        e.swap(type, Type.INT_TYPE);
        if (TypeUtils.isPrimitive(type)) {
            hash_primitive(e, type);
        } else {
            hash_object(e, type, customizer);
        }
        e.math(96, Type.INT_TYPE);
    }

    private static void hash_array(CodeEmitter e, Type type, int multiplier, Customizer customizer) {
        Label skip = e.make_label();
        Label end = e.make_label();
        e.dup();
        e.ifnull(skip);
        process_array(e, type, new ProcessArrayCallback(e, multiplier, customizer) { // from class: net.sf.cglib.core.EmitUtils.7
            private final CodeEmitter val$e;
            private final int val$multiplier;
            private final Customizer val$customizer;

            {
                this.val$e = e;
                this.val$multiplier = multiplier;
                this.val$customizer = customizer;
            }

            @Override // net.sf.cglib.core.ProcessArrayCallback
            public void processElement(Type type2) {
                EmitUtils.hash_code(this.val$e, type2, this.val$multiplier, this.val$customizer);
            }
        });
        e.goTo(end);
        e.mark(skip);
        e.pop();
        e.mark(end);
    }

    private static void hash_object(CodeEmitter e, Type type, Customizer customizer) {
        Label skip = e.make_label();
        Label end = e.make_label();
        e.dup();
        e.ifnull(skip);
        if (customizer != null) {
            customizer.customize(e, type);
        }
        e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
        e.goTo(end);
        e.mark(skip);
        e.pop();
        e.push(0);
        e.mark(end);
    }

    private static void hash_primitive(CodeEmitter e, Type type) {
        switch (type.getSort()) {
            case 1:
                e.push(1);
                e.math(130, Type.INT_TYPE);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                return;
            case 6:
                e.invoke_static(Constants.TYPE_FLOAT, FLOAT_TO_INT_BITS);
                return;
            case 7:
                break;
            case 8:
                e.invoke_static(Constants.TYPE_DOUBLE, DOUBLE_TO_LONG_BITS);
                break;
        }
        hash_long(e);
    }

    private static void hash_long(CodeEmitter e) {
        e.dup2();
        e.push(32);
        e.math(124, Type.LONG_TYPE);
        e.math(130, Type.LONG_TYPE);
        e.cast_numeric(Type.LONG_TYPE, Type.INT_TYPE);
    }

    public static void not_equals(CodeEmitter e, Type type, Label notEquals, Customizer customizer) {
        new ProcessArrayCallback(e, notEquals, customizer) { // from class: net.sf.cglib.core.EmitUtils.8
            private final CodeEmitter val$e;
            private final Label val$notEquals;
            private final Customizer val$customizer;

            {
                this.val$e = e;
                this.val$notEquals = notEquals;
                this.val$customizer = customizer;
            }

            @Override // net.sf.cglib.core.ProcessArrayCallback
            public void processElement(Type type2) {
                EmitUtils.not_equals_helper(this.val$e, type2, this.val$notEquals, this.val$customizer, this);
            }
        }.processElement(type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void not_equals_helper(CodeEmitter e, Type type, Label notEquals, Customizer customizer, ProcessArrayCallback callback) {
        if (TypeUtils.isPrimitive(type)) {
            e.if_cmp(type, 154, notEquals);
            return;
        }
        Label end = e.make_label();
        nullcmp(e, notEquals, end);
        if (TypeUtils.isArray(type)) {
            Label checkContents = e.make_label();
            e.dup2();
            e.arraylength();
            e.swap();
            e.arraylength();
            e.if_icmp(153, checkContents);
            e.pop2();
            e.goTo(notEquals);
            e.mark(checkContents);
            process_arrays(e, type, callback);
        } else {
            if (customizer != null) {
                customizer.customize(e, type);
                e.swap();
                customizer.customize(e, type);
            }
            e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
            e.if_jump(153, notEquals);
        }
        e.mark(end);
    }

    private static void nullcmp(CodeEmitter e, Label oneNull, Label bothNull) {
        e.dup2();
        Label nonNull = e.make_label();
        Label oneNullHelper = e.make_label();
        Label end = e.make_label();
        e.ifnonnull(nonNull);
        e.ifnonnull(oneNullHelper);
        e.pop2();
        e.goTo(bothNull);
        e.mark(nonNull);
        e.ifnull(oneNullHelper);
        e.goTo(end);
        e.mark(oneNullHelper);
        e.pop2();
        e.goTo(oneNull);
        e.mark(end);
    }

    public static void append_string(CodeEmitter e, Type type, ArrayDelimiters delims, Customizer customizer) {
        ArrayDelimiters d = delims != null ? delims : DEFAULT_DELIMITERS;
        ProcessArrayCallback callback = new ProcessArrayCallback(e, d, customizer) { // from class: net.sf.cglib.core.EmitUtils.9
            private final CodeEmitter val$e;
            private final ArrayDelimiters val$d;
            private final Customizer val$customizer;

            {
                this.val$e = e;
                this.val$d = d;
                this.val$customizer = customizer;
            }

            @Override // net.sf.cglib.core.ProcessArrayCallback
            public void processElement(Type type2) {
                EmitUtils.append_string_helper(this.val$e, type2, this.val$d, this.val$customizer, this);
                this.val$e.push(this.val$d.inside);
                this.val$e.invoke_virtual(Constants.TYPE_STRING_BUFFER, EmitUtils.APPEND_STRING);
            }
        };
        append_string_helper(e, type, d, customizer, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void append_string_helper(CodeEmitter e, Type type, ArrayDelimiters delims, Customizer customizer, ProcessArrayCallback callback) {
        Label skip = e.make_label();
        Label end = e.make_label();
        if (TypeUtils.isPrimitive(type)) {
            switch (type.getSort()) {
                case 1:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_BOOLEAN);
                    break;
                case 2:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_CHAR);
                    break;
                case 3:
                case 4:
                case 5:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_INT);
                    break;
                case 6:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_FLOAT);
                    break;
                case 7:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_LONG);
                    break;
                case 8:
                    e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_DOUBLE);
                    break;
            }
        } else if (TypeUtils.isArray(type)) {
            e.dup();
            e.ifnull(skip);
            e.swap();
            if (delims != null && delims.before != null && !"".equals(delims.before)) {
                e.push(delims.before);
                e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
                e.swap();
            }
            process_array(e, type, callback);
            shrinkStringBuffer(e, 2);
            if (delims != null && delims.after != null && !"".equals(delims.after)) {
                e.push(delims.after);
                e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
            }
        } else {
            e.dup();
            e.ifnull(skip);
            if (customizer != null) {
                customizer.customize(e, type);
            }
            e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
            e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
        }
        e.goTo(end);
        e.mark(skip);
        e.pop();
        e.push("null");
        e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
        e.mark(end);
    }

    private static void shrinkStringBuffer(CodeEmitter e, int amt) {
        e.dup();
        e.dup();
        e.invoke_virtual(Constants.TYPE_STRING_BUFFER, LENGTH);
        e.push(amt);
        e.math(100, Type.INT_TYPE);
        e.invoke_virtual(Constants.TYPE_STRING_BUFFER, SET_LENGTH);
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/EmitUtils$ArrayDelimiters.class */
    public static class ArrayDelimiters {
        private String before;
        private String inside;
        private String after;

        public ArrayDelimiters(String before, String inside, String after) {
            this.before = before;
            this.inside = inside;
            this.after = after;
        }
    }

    public static void load_method(CodeEmitter e, MethodInfo method) {
        load_class(e, method.getClassInfo().getType());
        e.push(method.getSignature().getName());
        push_object(e, method.getSignature().getArgumentTypes());
        e.invoke_virtual(Constants.TYPE_CLASS, GET_DECLARED_METHOD);
    }

    public static void method_switch(CodeEmitter e, List methods, ObjectSwitchCallback callback) {
        member_switch_helper(e, methods, callback, true);
    }

    public static void constructor_switch(CodeEmitter e, List constructors, ObjectSwitchCallback callback) {
        member_switch_helper(e, constructors, callback, false);
    }

    private static void member_switch_helper(CodeEmitter e, List members, ObjectSwitchCallback callback, boolean useName) {
        try {
            Map cache = new HashMap();
            ParameterTyper cached = new ParameterTyper(cache) { // from class: net.sf.cglib.core.EmitUtils.10
                private final Map val$cache;

                {
                    this.val$cache = cache;
                }

                @Override // net.sf.cglib.core.EmitUtils.ParameterTyper
                public Type[] getParameterTypes(MethodInfo member) {
                    Type[] types = (Type[]) this.val$cache.get(member);
                    if (types == null) {
                        Map map = this.val$cache;
                        Type[] argumentTypes = member.getSignature().getArgumentTypes();
                        types = argumentTypes;
                        map.put(member, argumentTypes);
                    }
                    return types;
                }
            };
            Label def = e.make_label();
            Label end = e.make_label();
            if (useName) {
                e.swap();
                Map buckets = CollectionUtils.bucket(members, new Transformer() { // from class: net.sf.cglib.core.EmitUtils.11
                    @Override // net.sf.cglib.core.Transformer
                    public Object transform(Object value) {
                        return ((MethodInfo) value).getSignature().getName();
                    }
                });
                String[] names = (String[]) buckets.keySet().toArray(new String[buckets.size()]);
                string_switch(e, names, 1, new ObjectSwitchCallback(e, buckets, callback, cached, def, end) { // from class: net.sf.cglib.core.EmitUtils.12
                    private final CodeEmitter val$e;
                    private final Map val$buckets;
                    private final ObjectSwitchCallback val$callback;
                    private final ParameterTyper val$cached;
                    private final Label val$def;
                    private final Label val$end;

                    {
                        this.val$e = e;
                        this.val$buckets = buckets;
                        this.val$callback = callback;
                        this.val$cached = cached;
                        this.val$def = def;
                        this.val$end = end;
                    }

                    @Override // net.sf.cglib.core.ObjectSwitchCallback
                    public void processCase(Object key, Label dontUseEnd) throws Exception {
                        EmitUtils.member_helper_size(this.val$e, (List) this.val$buckets.get(key), this.val$callback, this.val$cached, this.val$def, this.val$end);
                    }

                    @Override // net.sf.cglib.core.ObjectSwitchCallback
                    public void processDefault() throws Exception {
                        this.val$e.goTo(this.val$def);
                    }
                });
            } else {
                member_helper_size(e, members, callback, cached, def, end);
            }
            e.mark(def);
            e.pop();
            callback.processDefault();
            e.mark(end);
        } catch (Error ex) {
            throw ex;
        } catch (RuntimeException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            throw new CodeGenerationException(ex3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void member_helper_size(CodeEmitter e, List members, ObjectSwitchCallback callback, ParameterTyper typer, Label def, Label end) throws Exception {
        Map buckets = CollectionUtils.bucket(members, new Transformer(typer) { // from class: net.sf.cglib.core.EmitUtils.13
            private final ParameterTyper val$typer;

            {
                this.val$typer = typer;
            }

            @Override // net.sf.cglib.core.Transformer
            public Object transform(Object value) {
                return new Integer(this.val$typer.getParameterTypes((MethodInfo) value).length);
            }
        });
        e.dup();
        e.arraylength();
        e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback(buckets, e, callback, typer, def, end) { // from class: net.sf.cglib.core.EmitUtils.14
            private final Map val$buckets;
            private final CodeEmitter val$e;
            private final ObjectSwitchCallback val$callback;
            private final ParameterTyper val$typer;
            private final Label val$def;
            private final Label val$end;

            {
                this.val$buckets = buckets;
                this.val$e = e;
                this.val$callback = callback;
                this.val$typer = typer;
                this.val$def = def;
                this.val$end = end;
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processCase(int key, Label dontUseEnd) throws Exception {
                List bucket = (List) this.val$buckets.get(new Integer(key));
                EmitUtils.member_helper_type(this.val$e, bucket, this.val$callback, this.val$typer, this.val$def, this.val$end, new BitSet());
            }

            @Override // net.sf.cglib.core.ProcessSwitchCallback
            public void processDefault() throws Exception {
                this.val$e.goTo(this.val$def);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void member_helper_type(CodeEmitter e, List members, ObjectSwitchCallback callback, ParameterTyper typer, Label def, Label end, BitSet checked) throws Exception {
        if (members.size() == 1) {
            MethodInfo member = (MethodInfo) members.get(0);
            Type[] types = typer.getParameterTypes(member);
            for (int i = 0; i < types.length; i++) {
                if (checked == null || !checked.get(i)) {
                    e.dup();
                    e.aaload(i);
                    e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
                    e.push(TypeUtils.emulateClassGetName(types[i]));
                    e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
                    e.if_jump(153, def);
                }
            }
            e.pop();
            callback.processCase(member, end);
            return;
        }
        Type[] example = typer.getParameterTypes((MethodInfo) members.get(0));
        Map buckets = null;
        int index = -1;
        for (int i2 = 0; i2 < example.length; i2++) {
            int j = i2;
            Map test = CollectionUtils.bucket(members, new Transformer(typer, j) { // from class: net.sf.cglib.core.EmitUtils.15
                private final ParameterTyper val$typer;
                private final int val$j;

                {
                    this.val$typer = typer;
                    this.val$j = j;
                }

                @Override // net.sf.cglib.core.Transformer
                public Object transform(Object value) {
                    return TypeUtils.emulateClassGetName(this.val$typer.getParameterTypes((MethodInfo) value)[this.val$j]);
                }
            });
            if (buckets == null || test.size() > buckets.size()) {
                buckets = test;
                index = i2;
            }
        }
        if (buckets == null || buckets.size() == 1) {
            e.goTo(def);
            return;
        }
        checked.set(index);
        e.dup();
        e.aaload(index);
        e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
        Map fbuckets = buckets;
        String[] names = (String[]) buckets.keySet().toArray(new String[buckets.size()]);
        string_switch(e, names, 1, new ObjectSwitchCallback(e, fbuckets, callback, typer, def, end, checked) { // from class: net.sf.cglib.core.EmitUtils.16
            private final CodeEmitter val$e;
            private final Map val$fbuckets;
            private final ObjectSwitchCallback val$callback;
            private final ParameterTyper val$typer;
            private final Label val$def;
            private final Label val$end;
            private final BitSet val$checked;

            {
                this.val$e = e;
                this.val$fbuckets = fbuckets;
                this.val$callback = callback;
                this.val$typer = typer;
                this.val$def = def;
                this.val$end = end;
                this.val$checked = checked;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label dontUseEnd) throws Exception {
                EmitUtils.member_helper_type(this.val$e, (List) this.val$fbuckets.get(key), this.val$callback, this.val$typer, this.val$def, this.val$end, this.val$checked);
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() throws Exception {
                this.val$e.goTo(this.val$def);
            }
        });
    }

    public static void wrap_throwable(Block block, Type wrapper) {
        CodeEmitter e = block.getCodeEmitter();
        e.catch_exception(block, Constants.TYPE_THROWABLE);
        e.new_instance(wrapper);
        e.dup_x1();
        e.swap();
        e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
        e.athrow();
    }

    public static void add_properties(ClassEmitter ce, String[] names, Type[] types) {
        for (int i = 0; i < names.length; i++) {
            String fieldName = new StringBuffer().append("$cglib_prop_").append(names[i]).toString();
            ce.declare_field(2, fieldName, types[i], null);
            add_property(ce, names[i], types[i], fieldName);
        }
    }

    public static void add_property(ClassEmitter ce, String name, Type type, String fieldName) {
        String property = TypeUtils.upperFirst(name);
        CodeEmitter e = ce.begin_method(1, new Signature(new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(property).toString(), type, Constants.TYPES_EMPTY), null);
        e.load_this();
        e.getfield(fieldName);
        e.return_value();
        e.end_method();
        CodeEmitter e2 = ce.begin_method(1, new Signature(new StringBuffer().append("set").append(property).toString(), Type.VOID_TYPE, new Type[]{type}), null);
        e2.load_this();
        e2.load_arg(0);
        e2.putfield(fieldName);
        e2.return_value();
        e2.end_method();
    }

    public static void wrap_undeclared_throwable(CodeEmitter e, Block handler, Type[] exceptions, Type wrapper) {
        Set set = exceptions == null ? Collections.EMPTY_SET : new HashSet(Arrays.asList(exceptions));
        if (set.contains(Constants.TYPE_THROWABLE)) {
            return;
        }
        boolean needThrow = exceptions != null;
        if (!set.contains(Constants.TYPE_RUNTIME_EXCEPTION)) {
            e.catch_exception(handler, Constants.TYPE_RUNTIME_EXCEPTION);
            needThrow = true;
        }
        if (!set.contains(Constants.TYPE_ERROR)) {
            e.catch_exception(handler, Constants.TYPE_ERROR);
            needThrow = true;
        }
        if (exceptions != null) {
            for (Type type : exceptions) {
                e.catch_exception(handler, type);
            }
        }
        if (needThrow) {
            e.athrow();
        }
        e.catch_exception(handler, Constants.TYPE_THROWABLE);
        e.new_instance(wrapper);
        e.dup_x1();
        e.swap();
        e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
        e.athrow();
    }

    public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method) {
        return begin_method(e, method, method.getModifiers());
    }

    public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method, int access) {
        return e.begin_method(access, method.getSignature(), method.getExceptionTypes());
    }
}
