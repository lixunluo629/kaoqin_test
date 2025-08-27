package net.sf.cglib.transform;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.DebuggingClassWriter;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.springframework.util.ClassUtils;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/AbstractClassLoader.class */
public abstract class AbstractClassLoader extends ClassLoader {
    private ClassFilter filter;
    private ClassLoader classPath;
    private static ProtectionDomain DOMAIN = (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction() { // from class: net.sf.cglib.transform.AbstractClassLoader.1
        @Override // java.security.PrivilegedAction
        public Object run() {
            Class clsClass$;
            if (AbstractClassLoader.class$net$sf$cglib$transform$AbstractClassLoader == null) {
                clsClass$ = AbstractClassLoader.class$("net.sf.cglib.transform.AbstractClassLoader");
                AbstractClassLoader.class$net$sf$cglib$transform$AbstractClassLoader = clsClass$;
            } else {
                clsClass$ = AbstractClassLoader.class$net$sf$cglib$transform$AbstractClassLoader;
            }
            return clsClass$.getProtectionDomain();
        }
    });
    static Class class$net$sf$cglib$transform$AbstractClassLoader;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    protected AbstractClassLoader(ClassLoader parent, ClassLoader classPath, ClassFilter filter) {
        super(parent);
        this.filter = filter;
        this.classPath = classPath;
    }

    @Override // java.lang.ClassLoader
    public Class loadClass(String name) throws ClassNotFoundException, IOException, ClassFormatError {
        Class loaded = findLoadedClass(name);
        if (loaded != null && loaded.getClassLoader() == this) {
            return loaded;
        }
        if (!this.filter.accept(name)) {
            return super.loadClass(name);
        }
        try {
            InputStream is = this.classPath.getResourceAsStream(new StringBuffer().append(name.replace('.', '/')).append(ClassUtils.CLASS_FILE_SUFFIX).toString());
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                ClassReader r = new ClassReader(is);
                is.close();
                try {
                    DebuggingClassWriter w = new DebuggingClassWriter(1);
                    getGenerator(r).generateClass(w);
                    byte[] b = w.toByteArray();
                    Class c = super.defineClass(name, b, 0, b.length, DOMAIN);
                    postProcess(c);
                    return c;
                } catch (Error e) {
                    throw e;
                } catch (RuntimeException e2) {
                    throw e2;
                } catch (Exception e3) {
                    throw new CodeGenerationException(e3);
                }
            } catch (Throwable th) {
                is.close();
                throw th;
            }
        } catch (IOException e4) {
            throw new ClassNotFoundException(new StringBuffer().append(name).append(":").append(e4.getMessage()).toString());
        }
    }

    protected ClassGenerator getGenerator(ClassReader r) {
        return new ClassReaderGenerator(r, attributes(), getFlags());
    }

    protected int getFlags() {
        return 0;
    }

    protected Attribute[] attributes() {
        return null;
    }

    protected void postProcess(Class c) {
    }
}
