package net.sf.cglib.beans;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanMapEmitter.class */
class BeanMapEmitter extends ClassEmitter {
    private static final Type BEAN_MAP = TypeUtils.parseType("net.sf.cglib.beans.BeanMap");
    private static final Type FIXED_KEY_SET = TypeUtils.parseType("net.sf.cglib.beans.FixedKeySet");
    private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
    private static final Signature CSTRUCT_STRING_ARRAY = TypeUtils.parseConstructor("String[]");
    private static final Signature BEAN_MAP_GET = TypeUtils.parseSignature("Object get(Object, Object)");
    private static final Signature BEAN_MAP_PUT = TypeUtils.parseSignature("Object put(Object, Object, Object)");
    private static final Signature KEY_SET = TypeUtils.parseSignature("java.util.Set keySet()");
    private static final Signature NEW_INSTANCE = new Signature("newInstance", BEAN_MAP, new Type[]{Constants.TYPE_OBJECT});
    private static final Signature GET_PROPERTY_TYPE = TypeUtils.parseSignature("Class getPropertyType(String)");

    public BeanMapEmitter(ClassVisitor v, String className, Class type, int require) {
        super(v);
        begin_class(46, 1, className, BEAN_MAP, null, "<generated>");
        EmitUtils.null_constructor(this);
        EmitUtils.factory_method(this, NEW_INSTANCE);
        generateConstructor();
        Map getters = makePropertyMap(ReflectUtils.getBeanGetters(type));
        Map setters = makePropertyMap(ReflectUtils.getBeanSetters(type));
        Map allProps = new HashMap();
        allProps.putAll(getters);
        allProps.putAll(setters);
        if (require != 0) {
            Iterator it = allProps.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                if (((require & 1) != 0 && !getters.containsKey(name)) || ((require & 2) != 0 && !setters.containsKey(name))) {
                    it.remove();
                    getters.remove(name);
                    setters.remove(name);
                }
            }
        }
        generateGet(type, getters);
        generatePut(type, setters);
        String[] allNames = getNames(allProps);
        generateKeySet(allNames);
        generateGetPropertyType(allProps, allNames);
        end_class();
    }

    private Map makePropertyMap(PropertyDescriptor[] props) {
        Map names = new HashMap();
        for (int i = 0; i < props.length; i++) {
            names.put(props[i].getName(), props[i]);
        }
        return names;
    }

    private String[] getNames(Map propertyMap) {
        return (String[]) propertyMap.keySet().toArray(new String[propertyMap.size()]);
    }

    private void generateConstructor() {
        CodeEmitter e = begin_method(1, CSTRUCT_OBJECT, null);
        e.load_this();
        e.load_arg(0);
        e.super_invoke_constructor(CSTRUCT_OBJECT);
        e.return_value();
        e.end_method();
    }

    private void generateGet(Class type, Map getters) {
        CodeEmitter e = begin_method(1, BEAN_MAP_GET, null);
        e.load_arg(0);
        e.checkcast(Type.getType((Class<?>) type));
        e.load_arg(1);
        e.checkcast(Constants.TYPE_STRING);
        EmitUtils.string_switch(e, getNames(getters), 1, new ObjectSwitchCallback(this, getters, e) { // from class: net.sf.cglib.beans.BeanMapEmitter.1
            private final Map val$getters;
            private final CodeEmitter val$e;
            private final BeanMapEmitter this$0;

            {
                this.this$0 = this;
                this.val$getters = getters;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) this.val$getters.get(key);
                MethodInfo method = ReflectUtils.getMethodInfo(pd.getReadMethod());
                this.val$e.invoke(method);
                this.val$e.box(method.getSignature().getReturnType());
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.aconst_null();
                this.val$e.return_value();
            }
        });
        e.end_method();
    }

    private void generatePut(Class type, Map setters) {
        CodeEmitter e = begin_method(1, BEAN_MAP_PUT, null);
        e.load_arg(0);
        e.checkcast(Type.getType((Class<?>) type));
        e.load_arg(1);
        e.checkcast(Constants.TYPE_STRING);
        EmitUtils.string_switch(e, getNames(setters), 1, new ObjectSwitchCallback(this, setters, e) { // from class: net.sf.cglib.beans.BeanMapEmitter.2
            private final Map val$setters;
            private final CodeEmitter val$e;
            private final BeanMapEmitter this$0;

            {
                this.this$0 = this;
                this.val$setters = setters;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) this.val$setters.get(key);
                if (pd.getReadMethod() == null) {
                    this.val$e.aconst_null();
                } else {
                    MethodInfo read = ReflectUtils.getMethodInfo(pd.getReadMethod());
                    this.val$e.dup();
                    this.val$e.invoke(read);
                    this.val$e.box(read.getSignature().getReturnType());
                }
                this.val$e.swap();
                this.val$e.load_arg(2);
                MethodInfo write = ReflectUtils.getMethodInfo(pd.getWriteMethod());
                this.val$e.unbox(write.getSignature().getArgumentTypes()[0]);
                this.val$e.invoke(write);
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
            }
        });
        e.aconst_null();
        e.return_value();
        e.end_method();
    }

    private void generateKeySet(String[] allNames) {
        declare_field(10, "keys", FIXED_KEY_SET, null);
        CodeEmitter e = begin_static();
        e.new_instance(FIXED_KEY_SET);
        e.dup();
        EmitUtils.push_array(e, allNames);
        e.invoke_constructor(FIXED_KEY_SET, CSTRUCT_STRING_ARRAY);
        e.putfield("keys");
        e.return_value();
        e.end_method();
        CodeEmitter e2 = begin_method(1, KEY_SET, null);
        e2.load_this();
        e2.getfield("keys");
        e2.return_value();
        e2.end_method();
    }

    private void generateGetPropertyType(Map allProps, String[] allNames) {
        CodeEmitter e = begin_method(1, GET_PROPERTY_TYPE, null);
        e.load_arg(0);
        EmitUtils.string_switch(e, allNames, 1, new ObjectSwitchCallback(this, allProps, e) { // from class: net.sf.cglib.beans.BeanMapEmitter.3
            private final Map val$allProps;
            private final CodeEmitter val$e;
            private final BeanMapEmitter this$0;

            {
                this.this$0 = this;
                this.val$allProps = allProps;
                this.val$e = e;
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processCase(Object key, Label end) {
                PropertyDescriptor pd = (PropertyDescriptor) this.val$allProps.get(key);
                EmitUtils.load_class(this.val$e, Type.getType((Class<?>) pd.getPropertyType()));
                this.val$e.return_value();
            }

            @Override // net.sf.cglib.core.ObjectSwitchCallback
            public void processDefault() {
                this.val$e.aconst_null();
                this.val$e.return_value();
            }
        });
        e.end_method();
    }
}
