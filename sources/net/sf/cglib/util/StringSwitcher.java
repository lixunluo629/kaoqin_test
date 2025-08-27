package net.sf.cglib.util;

import java.util.Arrays;
import java.util.List;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/util/StringSwitcher.class */
public abstract class StringSwitcher {
    private static final Type STRING_SWITCHER = TypeUtils.parseType("net.sf.cglib.util.StringSwitcher");
    private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue(String)");
    private static final StringSwitcherKey KEY_FACTORY;
    static Class class$net$sf$cglib$util$StringSwitcher$StringSwitcherKey;
    static Class class$net$sf$cglib$util$StringSwitcher;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/StringSwitcher$StringSwitcherKey.class */
    interface StringSwitcherKey {
        Object newInstance(String[] strArr, int[] iArr, boolean z);
    }

    public abstract int intValue(String str);

    static {
        Class clsClass$;
        if (class$net$sf$cglib$util$StringSwitcher$StringSwitcherKey == null) {
            clsClass$ = class$("net.sf.cglib.util.StringSwitcher$StringSwitcherKey");
            class$net$sf$cglib$util$StringSwitcher$StringSwitcherKey = clsClass$;
        } else {
            clsClass$ = class$net$sf$cglib$util$StringSwitcher$StringSwitcherKey;
        }
        KEY_FACTORY = (StringSwitcherKey) KeyFactory.create(clsClass$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static StringSwitcher create(String[] strings, int[] ints, boolean fixedInput) {
        Generator gen = new Generator();
        gen.setStrings(strings);
        gen.setInts(ints);
        gen.setFixedInput(fixedInput);
        return gen.create();
    }

    protected StringSwitcher() {
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/StringSwitcher$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private String[] strings;
        private int[] ints;
        private boolean fixedInput;

        static {
            Class clsClass$;
            if (StringSwitcher.class$net$sf$cglib$util$StringSwitcher == null) {
                clsClass$ = StringSwitcher.class$("net.sf.cglib.util.StringSwitcher");
                StringSwitcher.class$net$sf$cglib$util$StringSwitcher = clsClass$;
            } else {
                clsClass$ = StringSwitcher.class$net$sf$cglib$util$StringSwitcher;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        public void setStrings(String[] strings) {
            this.strings = strings;
        }

        public void setInts(int[] ints) {
            this.ints = ints;
        }

        public void setFixedInput(boolean fixedInput) {
            this.fixedInput = fixedInput;
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return getClass().getClassLoader();
        }

        public StringSwitcher create() {
            Class clsClass$;
            if (StringSwitcher.class$net$sf$cglib$util$StringSwitcher == null) {
                clsClass$ = StringSwitcher.class$("net.sf.cglib.util.StringSwitcher");
                StringSwitcher.class$net$sf$cglib$util$StringSwitcher = clsClass$;
            } else {
                clsClass$ = StringSwitcher.class$net$sf$cglib$util$StringSwitcher;
            }
            setNamePrefix(clsClass$.getName());
            Object key = StringSwitcher.KEY_FACTORY.newInstance(this.strings, this.ints, this.fixedInput);
            return (StringSwitcher) super.create(key);
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws Exception {
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, getClassName(), StringSwitcher.STRING_SWITCHER, null, "<generated>");
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(1, StringSwitcher.INT_VALUE, null);
            e.load_arg(0);
            List stringList = Arrays.asList(this.strings);
            int style = this.fixedInput ? 2 : 1;
            EmitUtils.string_switch(e, this.strings, style, new ObjectSwitchCallback(this, e, stringList) { // from class: net.sf.cglib.util.StringSwitcher.Generator.1
                private final CodeEmitter val$e;
                private final List val$stringList;
                private final Generator this$0;

                {
                    this.this$0 = this;
                    this.val$e = e;
                    this.val$stringList = stringList;
                }

                @Override // net.sf.cglib.core.ObjectSwitchCallback
                public void processCase(Object key, Label end) {
                    this.val$e.push(this.this$0.ints[this.val$stringList.indexOf(key)]);
                    this.val$e.return_value();
                }

                @Override // net.sf.cglib.core.ObjectSwitchCallback
                public void processDefault() {
                    this.val$e.push(-1);
                    this.val$e.return_value();
                }
            });
            e.end_method();
            ce.end_class();
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return (StringSwitcher) ReflectUtils.newInstance(type);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return instance;
        }
    }
}
