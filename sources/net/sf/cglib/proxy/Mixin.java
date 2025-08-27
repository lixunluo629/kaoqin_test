package net.sf.cglib.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassesKey;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Mixin.class */
public abstract class Mixin {
    private static final MixinKey KEY_FACTORY;
    private static final Map ROUTE_CACHE;
    public static final int STYLE_INTERFACES = 0;
    public static final int STYLE_BEANS = 1;
    public static final int STYLE_EVERYTHING = 2;
    static Class class$net$sf$cglib$proxy$Mixin$MixinKey;
    static Class class$net$sf$cglib$proxy$Mixin;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Mixin$MixinKey.class */
    interface MixinKey {
        Object newInstance(int i, String[] strArr, int[] iArr);
    }

    public abstract Mixin newInstance(Object[] objArr);

    static {
        Class clsClass$;
        if (class$net$sf$cglib$proxy$Mixin$MixinKey == null) {
            clsClass$ = class$("net.sf.cglib.proxy.Mixin$MixinKey");
            class$net$sf$cglib$proxy$Mixin$MixinKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$proxy$Mixin$MixinKey;
        }
        KEY_FACTORY = (MixinKey) KeyFactory.create(clsClass$, KeyFactory.CLASS_BY_NAME);
        ROUTE_CACHE = Collections.synchronizedMap(new HashMap());
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static Mixin create(Object[] delegates) {
        Generator gen = new Generator();
        gen.setDelegates(delegates);
        return gen.create();
    }

    public static Mixin create(Class[] interfaces, Object[] delegates) {
        Generator gen = new Generator();
        gen.setClasses(interfaces);
        gen.setDelegates(delegates);
        return gen.create();
    }

    public static Mixin createBean(Object[] beans) {
        return createBean(null, beans);
    }

    public static Mixin createBean(ClassLoader loader, Object[] beans) {
        Generator gen = new Generator();
        gen.setStyle(1);
        gen.setDelegates(beans);
        gen.setClassLoader(loader);
        return gen.create();
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Mixin$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Class[] classes;
        private Object[] delegates;
        private int style;
        private int[] route;

        static {
            Class clsClass$;
            if (Mixin.class$net$sf$cglib$proxy$Mixin == null) {
                clsClass$ = Mixin.class$("net.sf.cglib.proxy.Mixin");
                Mixin.class$net$sf$cglib$proxy$Mixin = clsClass$;
            } else {
                clsClass$ = Mixin.class$net$sf$cglib$proxy$Mixin;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
            this.style = 0;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return this.classes[0].getClassLoader();
        }

        public void setStyle(int style) {
            switch (style) {
                case 0:
                case 1:
                case 2:
                    this.style = style;
                    return;
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Unknown mixin style: ").append(style).toString());
            }
        }

        public void setClasses(Class[] classes) {
            this.classes = classes;
        }

        public void setDelegates(Object[] delegates) {
            this.delegates = delegates;
        }

        public Mixin create() {
            if (this.classes == null && this.delegates == null) {
                throw new IllegalStateException("Either classes or delegates must be set");
            }
            switch (this.style) {
                case 0:
                    if (this.classes == null) {
                        Route r = Mixin.route(this.delegates);
                        this.classes = r.classes;
                        this.route = r.route;
                        break;
                    }
                    break;
                case 1:
                case 2:
                    if (this.classes == null) {
                        this.classes = ReflectUtils.getClasses(this.delegates);
                        break;
                    } else if (this.delegates != null) {
                        Class[] temp = ReflectUtils.getClasses(this.delegates);
                        if (this.classes.length != temp.length) {
                            throw new IllegalStateException("Specified classes are incompatible with delegates");
                        }
                        for (int i = 0; i < this.classes.length; i++) {
                            if (!this.classes[i].isAssignableFrom(temp[i])) {
                                throw new IllegalStateException(new StringBuffer().append("Specified class ").append(this.classes[i]).append(" is incompatible with delegate class ").append(temp[i]).append(" (index ").append(i).append(")").toString());
                            }
                        }
                        break;
                    }
                    break;
            }
            setNamePrefix(this.classes[ReflectUtils.findPackageProtected(this.classes)].getName());
            return (Mixin) super.create(Mixin.KEY_FACTORY.newInstance(this.style, ReflectUtils.getNames(this.classes), this.route));
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) {
            switch (this.style) {
                case 0:
                    new MixinEmitter(v, getClassName(), this.classes, this.route);
                    break;
                case 1:
                    new MixinBeanEmitter(v, getClassName(), this.classes);
                    break;
                case 2:
                    new MixinEverythingEmitter(v, getClassName(), this.classes);
                    break;
            }
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ((Mixin) ReflectUtils.newInstance(type)).newInstance(this.delegates);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return ((Mixin) instance).newInstance(this.delegates);
        }
    }

    public static Class[] getClasses(Object[] delegates) {
        return (Class[]) route(delegates).classes.clone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Route route(Object[] delegates) {
        Object key = ClassesKey.create(delegates);
        Route route = (Route) ROUTE_CACHE.get(key);
        if (route == null) {
            Map map = ROUTE_CACHE;
            Route route2 = new Route(delegates);
            route = route2;
            map.put(key, route2);
        }
        return route;
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/Mixin$Route.class */
    private static class Route {
        private Class[] classes;
        private int[] route;

        Route(Object[] delegates) {
            Map map = new HashMap();
            ArrayList collect = new ArrayList();
            for (int i = 0; i < delegates.length; i++) {
                Class delegate = delegates[i].getClass();
                collect.clear();
                ReflectUtils.addAllInterfaces(delegate, collect);
                Iterator it = collect.iterator();
                while (it.hasNext()) {
                    Class iface = (Class) it.next();
                    if (!map.containsKey(iface)) {
                        map.put(iface, new Integer(i));
                    }
                }
            }
            this.classes = new Class[map.size()];
            this.route = new int[map.size()];
            int index = 0;
            for (Class key : map.keySet()) {
                this.classes[index] = key;
                this.route[index] = ((Integer) map.get(key)).intValue();
                index++;
            }
        }
    }
}
