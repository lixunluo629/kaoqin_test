package org.apache.ibatis.javassist;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.Hashtable;
import java.util.Vector;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/Loader.class */
public class Loader extends ClassLoader {
    private Hashtable notDefinedHere;
    private Vector notDefinedPackages;
    private ClassPool source;
    private Translator translator;
    private ProtectionDomain domain;
    public boolean doDelegation;

    public Loader() {
        this(null);
    }

    public Loader(ClassPool cp) {
        this.doDelegation = true;
        init(cp);
    }

    public Loader(ClassLoader parent, ClassPool cp) {
        super(parent);
        this.doDelegation = true;
        init(cp);
    }

    private void init(ClassPool cp) {
        this.notDefinedHere = new Hashtable();
        this.notDefinedPackages = new Vector();
        this.source = cp;
        this.translator = null;
        this.domain = null;
        delegateLoadingOf("org.apache.ibatis.javassist.Loader");
    }

    public void delegateLoadingOf(String classname) {
        if (classname.endsWith(".")) {
            this.notDefinedPackages.addElement(classname);
        } else {
            this.notDefinedHere.put(classname, this);
        }
    }

    public void setDomain(ProtectionDomain d) {
        this.domain = d;
    }

    public void setClassPool(ClassPool cp) {
        this.source = cp;
    }

    public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
        this.source = cp;
        this.translator = t;
        t.start(cp);
    }

    public static void main(String[] args) throws Throwable {
        Loader cl = new Loader();
        cl.run(args);
    }

    public void run(String[] args) throws Throwable {
        int n = args.length - 1;
        if (n >= 0) {
            String[] args2 = new String[n];
            for (int i = 0; i < n; i++) {
                args2[i] = args[i + 1];
            }
            run(args[0], args2);
        }
    }

    public void run(String classname, String[] args) throws Throwable {
        Class c = loadClass(classname);
        try {
            c.getDeclaredMethod("main", String[].class).invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @Override // java.lang.ClassLoader
    protected Class loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
        Class cls;
        String name2 = name.intern();
        synchronized (name2) {
            Class c = findLoadedClass(name2);
            if (c == null) {
                c = loadClassByDelegation(name2);
            }
            if (c == null) {
                c = findClass(name2);
            }
            if (c == null) {
                c = delegateToParent(name2);
            }
            if (resolve) {
                resolveClass(c);
            }
            cls = c;
        }
        return cls;
    }

    @Override // java.lang.ClassLoader
    protected Class findClass(String name) throws ClassNotFoundException {
        byte[] classfile;
        try {
            if (this.source != null) {
                if (this.translator != null) {
                    this.translator.onLoad(this.source, name);
                }
                try {
                    classfile = this.source.get(name).toBytecode();
                } catch (NotFoundException e) {
                    return null;
                }
            } else {
                String jarname = "/" + name.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
                InputStream in = getClass().getResourceAsStream(jarname);
                if (in == null) {
                    return null;
                }
                classfile = ClassPoolTail.readStream(in);
            }
            int i = name.lastIndexOf(46);
            if (i != -1) {
                String pname = name.substring(0, i);
                if (getPackage(pname) == null) {
                    try {
                        definePackage(pname, null, null, null, null, null, null, null);
                    } catch (IllegalArgumentException e2) {
                    }
                }
            }
            if (this.domain == null) {
                return defineClass(name, classfile, 0, classfile.length);
            }
            return defineClass(name, classfile, 0, classfile.length, this.domain);
        } catch (Exception e3) {
            throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, e3);
        }
    }

    protected Class loadClassByDelegation(String name) throws ClassNotFoundException {
        Class c = null;
        if (this.doDelegation && (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("sun.") || name.startsWith("com.sun.") || name.startsWith("org.w3c.") || name.startsWith("org.xml.") || notDelegated(name))) {
            c = delegateToParent(name);
        }
        return c;
    }

    private boolean notDelegated(String name) {
        if (this.notDefinedHere.get(name) != null) {
            return true;
        }
        int n = this.notDefinedPackages.size();
        for (int i = 0; i < n; i++) {
            if (name.startsWith((String) this.notDefinedPackages.elementAt(i))) {
                return true;
            }
        }
        return false;
    }

    protected Class delegateToParent(String classname) throws ClassNotFoundException {
        ClassLoader cl = getParent();
        if (cl != null) {
            return cl.loadClass(classname);
        }
        return findSystemClass(classname);
    }
}
