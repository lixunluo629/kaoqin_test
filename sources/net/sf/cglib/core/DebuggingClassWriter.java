package net.sf.cglib.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.springframework.util.ClassUtils;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/DebuggingClassWriter.class */
public class DebuggingClassWriter extends ClassVisitor {
    public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
    private static String debugLocation = System.getProperty("cglib.debugLocation");
    private static Constructor traceCtor;
    private String className;
    private String superName;
    static Class class$org$objectweb$asm$ClassVisitor;
    static Class class$java$io$PrintWriter;

    static {
        Class<?> clsClass$;
        Class<?> clsClass$2;
        if (debugLocation != null) {
            System.err.println(new StringBuffer().append("CGLIB debugging enabled, writing to '").append(debugLocation).append("'").toString());
            try {
                Class clazz = Class.forName("org.objectweb.asm.util.TraceClassVisitor");
                Class<?>[] clsArr = new Class[2];
                if (class$org$objectweb$asm$ClassVisitor == null) {
                    clsClass$ = class$("org.objectweb.asm.ClassVisitor");
                    class$org$objectweb$asm$ClassVisitor = clsClass$;
                } else {
                    clsClass$ = class$org$objectweb$asm$ClassVisitor;
                }
                clsArr[0] = clsClass$;
                if (class$java$io$PrintWriter == null) {
                    clsClass$2 = class$("java.io.PrintWriter");
                    class$java$io$PrintWriter = clsClass$2;
                } else {
                    clsClass$2 = class$java$io$PrintWriter;
                }
                clsArr[1] = clsClass$2;
                traceCtor = clazz.getConstructor(clsArr);
            } catch (Throwable th) {
            }
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public DebuggingClassWriter(int flags) {
        super(262144, new ClassWriter(flags));
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.');
        this.superName = superName.replace('/', '.');
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public String getClassName() {
        return this.className;
    }

    public String getSuperName() {
        return this.superName;
    }

    public byte[] toByteArray() {
        return (byte[]) AccessController.doPrivileged(new PrivilegedAction(this) { // from class: net.sf.cglib.core.DebuggingClassWriter.1
            private final DebuggingClassWriter this$0;

            {
                this.this$0 = this;
            }

            @Override // java.security.PrivilegedAction
            public Object run() throws IOException {
                byte[] b = ((ClassWriter) ((ClassVisitor) this.this$0).cv).toByteArray();
                if (DebuggingClassWriter.debugLocation != null) {
                    String dirs = this.this$0.className.replace('.', File.separatorChar);
                    try {
                        new File(new StringBuffer().append(DebuggingClassWriter.debugLocation).append(File.separatorChar).append(dirs).toString()).getParentFile().mkdirs();
                        File file = new File(new File(DebuggingClassWriter.debugLocation), new StringBuffer().append(dirs).append(ClassUtils.CLASS_FILE_SUFFIX).toString());
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                        try {
                            out.write(b);
                            out.close();
                            if (DebuggingClassWriter.traceCtor != null) {
                                File file2 = new File(new File(DebuggingClassWriter.debugLocation), new StringBuffer().append(dirs).append(".asm").toString());
                                out = new BufferedOutputStream(new FileOutputStream(file2));
                                try {
                                    ClassReader cr = new ClassReader(b);
                                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                                    ClassVisitor tcv = (ClassVisitor) DebuggingClassWriter.traceCtor.newInstance(null, pw);
                                    cr.accept(tcv, 0);
                                    pw.flush();
                                    out.close();
                                } finally {
                                }
                            }
                        } finally {
                        }
                    } catch (Exception e) {
                        throw new CodeGenerationException(e);
                    }
                }
                return b;
            }
        });
    }
}
