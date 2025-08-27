package net.sf.cglib.beans;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanGenerator.class */
public class BeanGenerator extends AbstractClassGenerator {
    private static final AbstractClassGenerator.Source SOURCE;
    private static final BeanGeneratorKey KEY_FACTORY;
    private Class superclass;
    private Map props;
    private boolean classOnly;
    static Class class$net$sf$cglib$beans$BeanGenerator;
    static Class class$net$sf$cglib$beans$BeanGenerator$BeanGeneratorKey;
    static Class class$java$lang$Object;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/beans/BeanGenerator$BeanGeneratorKey.class */
    interface BeanGeneratorKey {
        Object newInstance(String str, Map map);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        Class clsClass$2;
        if (class$net$sf$cglib$beans$BeanGenerator == null) {
            clsClass$ = class$("net.sf.cglib.beans.BeanGenerator");
            class$net$sf$cglib$beans$BeanGenerator = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$beans$BeanGenerator;
        }
        SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        if (class$net$sf$cglib$beans$BeanGenerator$BeanGeneratorKey == null) {
            clsClass$2 = class$("net.sf.cglib.beans.BeanGenerator$BeanGeneratorKey");
            class$net$sf$cglib$beans$BeanGenerator$BeanGeneratorKey = clsClass$2;
        } else {
            clsClass$2 = class$net$sf$cglib$beans$BeanGenerator$BeanGeneratorKey;
        }
        KEY_FACTORY = (BeanGeneratorKey) KeyFactory.create(clsClass$2);
    }

    public BeanGenerator() {
        super(SOURCE);
        this.props = new HashMap();
    }

    public void setSuperclass(Class superclass) {
        Class clsClass$;
        if (superclass != null) {
            if (class$java$lang$Object == null) {
                clsClass$ = class$("java.lang.Object");
                class$java$lang$Object = clsClass$;
            } else {
                clsClass$ = class$java$lang$Object;
            }
            if (superclass.equals(clsClass$)) {
                superclass = null;
            }
        }
        this.superclass = superclass;
    }

    public void addProperty(String name, Class type) {
        if (this.props.containsKey(name)) {
            throw new IllegalArgumentException(new StringBuffer().append("Duplicate property name \"").append(name).append(SymbolConstants.QUOTES_SYMBOL).toString());
        }
        this.props.put(name, Type.getType((Class<?>) type));
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected ClassLoader getDefaultClassLoader() {
        if (this.superclass != null) {
            return this.superclass.getClassLoader();
        }
        return null;
    }

    public Object create() {
        this.classOnly = false;
        return createHelper();
    }

    public Object createClass() {
        this.classOnly = true;
        return createHelper();
    }

    private Object createHelper() {
        if (this.superclass != null) {
            setNamePrefix(this.superclass.getName());
        }
        String superName = this.superclass != null ? this.superclass.getName() : "java.lang.Object";
        Object key = KEY_FACTORY.newInstance(superName, this.props);
        return super.create(key);
    }

    @Override // net.sf.cglib.core.ClassGenerator
    public void generateClass(ClassVisitor v) throws Exception {
        int size = this.props.size();
        String[] names = (String[]) this.props.keySet().toArray(new String[size]);
        Type[] types = new Type[size];
        for (int i = 0; i < size; i++) {
            types[i] = (Type) this.props.get(names[i]);
        }
        ClassEmitter ce = new ClassEmitter(v);
        ce.begin_class(46, 1, getClassName(), this.superclass != null ? Type.getType((Class<?>) this.superclass) : Constants.TYPE_OBJECT, null, null);
        EmitUtils.null_constructor(ce);
        EmitUtils.add_properties(ce, names, types);
        ce.end_class();
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object firstInstance(Class type) {
        if (this.classOnly) {
            return type;
        }
        return ReflectUtils.newInstance(type);
    }

    @Override // net.sf.cglib.core.AbstractClassGenerator
    protected Object nextInstance(Object instance) {
        Class protoclass = instance instanceof Class ? (Class) instance : instance.getClass();
        if (this.classOnly) {
            return protoclass;
        }
        return ReflectUtils.newInstance(protoclass);
    }

    public static void addProperties(BeanGenerator gen, Map props) {
        for (String name : props.keySet()) {
            gen.addProperty(name, (Class) props.get(name));
        }
    }

    public static void addProperties(BeanGenerator gen, Class type) {
        addProperties(gen, ReflectUtils.getBeanProperties(type));
    }

    public static void addProperties(BeanGenerator gen, PropertyDescriptor[] descriptors) {
        for (int i = 0; i < descriptors.length; i++) {
            gen.addProperty(descriptors[i].getName(), descriptors[i].getPropertyType());
        }
    }
}
