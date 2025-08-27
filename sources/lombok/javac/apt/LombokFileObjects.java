package lombok.javac.apt;

import com.sun.tools.javac.file.BaseFileManager;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import lombok.core.DiagnosticsReceiver;

/* loaded from: lombok-1.16.22.jar:lombok/javac/apt/LombokFileObjects.SCL.lombok */
final class LombokFileObjects {
    private static final List<String> KNOWN_JAVA9_FILE_MANAGERS = Arrays.asList("com.google.errorprone.MaskedClassLoader$MaskedFileManager", "com.google.devtools.build.buildjar.javac.BlazeJavacMain$ClassloaderMaskingFileManager", "com.google.devtools.build.java.turbine.javac.JavacTurbineCompiler$ClassloaderMaskingFileManager", "org.netbeans.modules.java.source.parsing.ProxyFileManager", "com.sun.tools.javac.api.ClientCodeWrapper$WrappedStandardJavaFileManager", "com.sun.tools.javac.main.DelegatingJavaFileManager$DelegatingSJFM");

    /* loaded from: lombok-1.16.22.jar:lombok/javac/apt/LombokFileObjects$Compiler.SCL.lombok */
    interface Compiler {
        public static final Compiler JAVAC6 = new Compiler() { // from class: lombok.javac.apt.LombokFileObjects.Compiler.1
            private Method decoderMethod = null;
            private final AtomicBoolean decoderIsSet = new AtomicBoolean();

            @Override // lombok.javac.apt.LombokFileObjects.Compiler
            public JavaFileObject wrap(LombokFileObject fileObject) {
                return new Javac6BaseFileObjectWrapper(fileObject);
            }

            @Override // lombok.javac.apt.LombokFileObjects.Compiler
            public Method getDecoderMethod() {
                synchronized (this.decoderIsSet) {
                    if (this.decoderIsSet.get()) {
                        return this.decoderMethod;
                    }
                    this.decoderMethod = LombokFileObjects.getDecoderMethod("com.sun.tools.javac.util.BaseFileObject");
                    this.decoderIsSet.set(true);
                    return this.decoderMethod;
                }
            }
        };
        public static final Compiler JAVAC7 = new Compiler() { // from class: lombok.javac.apt.LombokFileObjects.Compiler.2
            private Method decoderMethod = null;
            private final AtomicBoolean decoderIsSet = new AtomicBoolean();

            @Override // lombok.javac.apt.LombokFileObjects.Compiler
            public JavaFileObject wrap(LombokFileObject fileObject) {
                return new Javac7BaseFileObjectWrapper(fileObject);
            }

            @Override // lombok.javac.apt.LombokFileObjects.Compiler
            public Method getDecoderMethod() {
                synchronized (this.decoderIsSet) {
                    if (this.decoderIsSet.get()) {
                        return this.decoderMethod;
                    }
                    this.decoderMethod = LombokFileObjects.getDecoderMethod("com.sun.tools.javac.file.BaseFileObject");
                    this.decoderIsSet.set(true);
                    return this.decoderMethod;
                }
            }
        };

        JavaFileObject wrap(LombokFileObject lombokFileObject);

        Method getDecoderMethod();
    }

    static Method getDecoderMethod(String className) throws NoSuchMethodException, SecurityException {
        Method m = null;
        try {
            m = Class.forName(className).getDeclaredMethod("getDecoder", Boolean.TYPE);
            m.setAccessible(true);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        }
        return m;
    }

    private LombokFileObjects() {
    }

    static Compiler getCompiler(JavaFileManager jfm) {
        String jfmClassName = jfm != null ? jfm.getClass().getName() : "null";
        if (!jfmClassName.equals("com.sun.tools.javac.util.DefaultFileManager") && !jfmClassName.equals("com.sun.tools.javac.util.JavacFileManager")) {
            if (jfmClassName.equals("com.sun.tools.javac.file.JavacFileManager")) {
                try {
                    Class<?> superType = Class.forName("com.sun.tools.javac.file.BaseFileManager");
                    if (superType.isInstance(jfm)) {
                        return new Java9Compiler(jfm);
                    }
                } catch (Throwable th) {
                }
                return Compiler.JAVAC7;
            }
            if (KNOWN_JAVA9_FILE_MANAGERS.contains(jfmClassName)) {
                try {
                    return new Java9Compiler(jfm);
                } catch (Throwable th2) {
                }
            }
            try {
                if (Class.forName("com.sun.tools.javac.file.PathFileObject") == null) {
                    throw new NullPointerException();
                }
                return new Java9Compiler(jfm);
            } catch (Throwable th3) {
                try {
                    if (Class.forName("com.sun.tools.javac.file.BaseFileObject") == null) {
                        throw new NullPointerException();
                    }
                    return Compiler.JAVAC7;
                } catch (Throwable th4) {
                    try {
                        if (Class.forName("com.sun.tools.javac.util.BaseFileObject") == null) {
                            throw new NullPointerException();
                        }
                        return Compiler.JAVAC6;
                    } catch (Throwable th5) {
                        StringBuilder sb = new StringBuilder(jfmClassName);
                        if (jfm != null) {
                            sb.append(" extends ").append(jfm.getClass().getSuperclass().getName());
                            for (Class<?> cls : jfm.getClass().getInterfaces()) {
                                sb.append(" implements ").append(cls.getName());
                            }
                        }
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
        }
        return Compiler.JAVAC6;
    }

    static JavaFileObject createEmpty(Compiler compiler, String name, JavaFileObject.Kind kind) {
        return compiler.wrap(new EmptyLombokFileObject(name, kind));
    }

    static JavaFileObject createIntercepting(Compiler compiler, JavaFileObject delegate, String fileName, DiagnosticsReceiver diagnostics) {
        return compiler.wrap(new InterceptingJavaFileObject(delegate, fileName, diagnostics, compiler.getDecoderMethod()));
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/apt/LombokFileObjects$Java9Compiler.SCL.lombok */
    static class Java9Compiler implements Compiler {
        private final BaseFileManager fileManager;

        public Java9Compiler(JavaFileManager jfm) {
            this.fileManager = asBaseFileManager(jfm);
        }

        @Override // lombok.javac.apt.LombokFileObjects.Compiler
        public JavaFileObject wrap(LombokFileObject fileObject) {
            return new Javac9BaseFileObjectWrapper(this.fileManager, toPath(fileObject), fileObject);
        }

        @Override // lombok.javac.apt.LombokFileObjects.Compiler
        public Method getDecoderMethod() {
            return null;
        }

        private static Path toPath(LombokFileObject fileObject) {
            URI uri = fileObject.toUri();
            if (uri.getScheme() == null) {
                uri = URI.create("file:///" + uri);
            }
            try {
                return Paths.get(uri);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Problems in URI '" + uri + "' (" + fileObject.toUri() + ")", e);
            }
        }

        private static BaseFileManager asBaseFileManager(JavaFileManager jfm) {
            if (jfm instanceof BaseFileManager) {
                return (BaseFileManager) jfm;
            }
            return new FileManagerWrapper(jfm);
        }

        /* loaded from: lombok-1.16.22.jar:lombok/javac/apt/LombokFileObjects$Java9Compiler$FileManagerWrapper.SCL.lombok */
        static class FileManagerWrapper extends BaseFileManager {
            JavaFileManager manager;

            public FileManagerWrapper(JavaFileManager manager) {
                super((Charset) null);
                this.manager = manager;
            }

            public int isSupportedOption(String option) {
                return this.manager.isSupportedOption(option);
            }

            public ClassLoader getClassLoader(JavaFileManager.Location location) {
                return this.manager.getClassLoader(location);
            }

            public Iterable<JavaFileObject> list(JavaFileManager.Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
                return this.manager.list(location, packageName, kinds, recurse);
            }

            public String inferBinaryName(JavaFileManager.Location location, JavaFileObject file) {
                return this.manager.inferBinaryName(location, file);
            }

            public boolean isSameFile(FileObject a, FileObject b) {
                return this.manager.isSameFile(a, b);
            }

            public boolean handleOption(String current, Iterator<String> remaining) {
                return this.manager.handleOption(current, remaining);
            }

            public boolean hasLocation(JavaFileManager.Location location) {
                return this.manager.hasLocation(location);
            }

            public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
                return this.manager.getJavaFileForInput(location, className, kind);
            }

            public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                return this.manager.getJavaFileForOutput(location, className, kind, sibling);
            }

            public FileObject getFileForInput(JavaFileManager.Location location, String packageName, String relativeName) throws IOException {
                return this.manager.getFileForInput(location, packageName, relativeName);
            }

            public FileObject getFileForOutput(JavaFileManager.Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
                return this.manager.getFileForOutput(location, packageName, relativeName, sibling);
            }

            public void flush() throws IOException {
                this.manager.flush();
            }

            public void close() throws IOException {
                this.manager.close();
            }
        }
    }
}
