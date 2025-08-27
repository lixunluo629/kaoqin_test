package lombok.javac.apt;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.processing.JavacFiler;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import lombok.Lombok;
import lombok.core.DiagnosticsReceiver;
import lombok.javac.JavacTransformer;
import org.springframework.beans.PropertyAccessor;

@SupportedAnnotationTypes({"*"})
/* loaded from: lombok-1.16.22.jar:lombok/javac/apt/LombokProcessor.SCL.lombok */
public class LombokProcessor extends AbstractProcessor {
    private ProcessingEnvironment processingEnv;
    private JavacProcessingEnvironment javacProcessingEnv;
    private JavacFiler javacFiler;
    private JavacTransformer transformer;
    private Trees trees;
    private static final String JPE = "com.sun.tools.javac.processing.JavacProcessingEnvironment";
    private static final Field javacProcessingEnvironment_discoveredProcs = getFieldAccessor(JPE, "discoveredProcs");
    private static final Field discoveredProcessors_procStateList = getFieldAccessor("com.sun.tools.javac.processing.JavacProcessingEnvironment$DiscoveredProcessors", "procStateList");
    private static final Field processorState_processor = getFieldAccessor("com.sun.tools.javac.processing.JavacProcessingEnvironment$processor", "processor");
    private long[] priorityLevels;
    private Set<Long> priorityLevelsRequiringResolutionReset;
    private boolean lombokDisabled = false;
    private final IdentityHashMap<JCTree.JCCompilationUnit, Long> roots = new IdentityHashMap<>();
    private int dummyCount = 0;

    public void init(ProcessingEnvironment procEnv) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super.init(procEnv);
        if (System.getProperty("lombok.disable") != null) {
            this.lombokDisabled = true;
            return;
        }
        this.processingEnv = procEnv;
        this.javacProcessingEnv = getJavacProcessingEnvironment(procEnv);
        this.javacFiler = getJavacFiler(procEnv.getFiler());
        placePostCompileAndDontMakeForceRoundDummiesHook();
        this.trees = Trees.instance(this.javacProcessingEnv);
        this.transformer = new JavacTransformer(procEnv.getMessager(), this.trees);
        SortedSet<Long> p = this.transformer.getPriorities();
        if (p.isEmpty()) {
            this.priorityLevels = new long[]{0};
            this.priorityLevelsRequiringResolutionReset = new HashSet();
            return;
        }
        this.priorityLevels = new long[p.size()];
        int i = 0;
        for (Long prio : p) {
            int i2 = i;
            i++;
            this.priorityLevels[i2] = prio.longValue();
        }
        this.priorityLevelsRequiringResolutionReset = this.transformer.getPrioritiesRequiringResolutionReset();
    }

    private static final Field getFieldAccessor(String typeName, String fieldName) throws NoSuchFieldException, ClassNotFoundException {
        try {
            Class<?> c = Class.forName(typeName);
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }

    private String listAnnotationProcessorsBeforeOurs() throws IllegalAccessException, IllegalArgumentException {
        try {
            Object discoveredProcessors = javacProcessingEnvironment_discoveredProcs.get(this.javacProcessingEnv);
            ArrayList<?> states = (ArrayList) discoveredProcessors_procStateList.get(discoveredProcessors);
            if (states == null || states.isEmpty()) {
                return null;
            }
            if (states.size() == 1) {
                return processorState_processor.get(states.get(0)).getClass().getName();
            }
            int idx = 0;
            StringBuilder out = new StringBuilder();
            Iterator<?> it = states.iterator();
            while (it.hasNext()) {
                Object processState = it.next();
                idx++;
                String name = processorState_processor.get(processState).getClass().getName();
                if (out.length() > 0) {
                    out.append(", ");
                }
                out.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(idx).append("] ").append(name);
            }
            return out.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void placePostCompileAndDontMakeForceRoundDummiesHook() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        stopJavacProcessingEnvironmentFromClosingOurClassloader();
        forceMultipleRoundsInNetBeansEditor();
        Context context = this.javacProcessingEnv.getContext();
        disablePartialReparseInNetBeansEditor(context);
        try {
            Method keyMethod = Context.class.getDeclaredMethod("key", Class.class);
            keyMethod.setAccessible(true);
            Object key = keyMethod.invoke(context, JavaFileManager.class);
            Field htField = Context.class.getDeclaredField("ht");
            htField.setAccessible(true);
            Map<Object, Object> ht = (Map) htField.get(context);
            JavaFileManager originalFiler = (JavaFileManager) ht.get(key);
            if (!(originalFiler instanceof InterceptingJavaFileManager)) {
                Messager messager = this.processingEnv.getMessager();
                DiagnosticsReceiver receiver = new MessagerDiagnosticsReceiver(messager);
                InterceptingJavaFileManager interceptingJavaFileManager = new InterceptingJavaFileManager(originalFiler, receiver);
                ht.put(key, interceptingJavaFileManager);
                Field filerFileManagerField = JavacFiler.class.getDeclaredField("fileManager");
                filerFileManagerField.setAccessible(true);
                filerFileManagerField.set(this.javacFiler, interceptingJavaFileManager);
                replaceFileManagerJdk9(context, interceptingJavaFileManager);
            }
        } catch (Exception e) {
            throw Lombok.sneakyThrow(e);
        }
    }

    private void replaceFileManagerJdk9(Context context, JavaFileManager newFiler) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            JavaCompiler compiler = (JavaCompiler) JavaCompiler.class.getDeclaredMethod("instance", Context.class).invoke(null, context);
            try {
                Field fileManagerField = JavaCompiler.class.getDeclaredField("fileManager");
                fileManagerField.setAccessible(true);
                fileManagerField.set(compiler, newFiler);
            } catch (Exception e) {
            }
            try {
                Field writerField = JavaCompiler.class.getDeclaredField("writer");
                writerField.setAccessible(true);
                ClassWriter writer = (ClassWriter) writerField.get(compiler);
                Field fileManagerField2 = ClassWriter.class.getDeclaredField("fileManager");
                fileManagerField2.setAccessible(true);
                fileManagerField2.set(writer, newFiler);
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
        }
    }

    private void forceMultipleRoundsInNetBeansEditor() {
        try {
            Field f = JavacProcessingEnvironment.class.getDeclaredField("isBackgroundCompilation");
            f.setAccessible(true);
            f.set(this.javacProcessingEnv, true);
        } catch (NoSuchFieldException e) {
        } catch (Throwable t) {
            throw Lombok.sneakyThrow(t);
        }
    }

    private void disablePartialReparseInNetBeansEditor(Context context) {
        try {
            Class<?> cancelServiceClass = Class.forName("com.sun.tools.javac.util.CancelService");
            Method cancelServiceInstace = cancelServiceClass.getDeclaredMethod("instance", Context.class);
            Object cancelService = cancelServiceInstace.invoke(null, context);
            if (cancelService == null) {
                return;
            }
            Field parserField = cancelService.getClass().getDeclaredField("parser");
            parserField.setAccessible(true);
            Object parser = parserField.get(cancelService);
            Field supportsReparseField = parser.getClass().getDeclaredField("supportsReparse");
            supportsReparseField.setAccessible(true);
            supportsReparseField.set(parser, false);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchFieldException e2) {
        } catch (Throwable t) {
            throw Lombok.sneakyThrow(t);
        }
    }

    private static ClassLoader wrapClassLoader(final ClassLoader parent) {
        return new ClassLoader() { // from class: lombok.javac.apt.LombokProcessor.1
            @Override // java.lang.ClassLoader
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return parent.loadClass(name);
            }

            public String toString() {
                return parent.toString();
            }

            @Override // java.lang.ClassLoader
            public URL getResource(String name) {
                return parent.getResource(name);
            }

            @Override // java.lang.ClassLoader
            public Enumeration<URL> getResources(String name) throws IOException {
                return parent.getResources(name);
            }

            @Override // java.lang.ClassLoader
            public InputStream getResourceAsStream(String name) {
                return parent.getResourceAsStream(name);
            }

            @Override // java.lang.ClassLoader
            public void setDefaultAssertionStatus(boolean enabled) {
                parent.setDefaultAssertionStatus(enabled);
            }

            @Override // java.lang.ClassLoader
            public void setPackageAssertionStatus(String packageName, boolean enabled) {
                parent.setPackageAssertionStatus(packageName, enabled);
            }

            @Override // java.lang.ClassLoader
            public void setClassAssertionStatus(String className, boolean enabled) {
                parent.setClassAssertionStatus(className, enabled);
            }

            @Override // java.lang.ClassLoader
            public void clearAssertionStatus() {
                parent.clearAssertionStatus();
            }
        };
    }

    private void stopJavacProcessingEnvironmentFromClosingOurClassloader() {
        try {
            Field f = JavacProcessingEnvironment.class.getDeclaredField("processorClassLoader");
            f.setAccessible(true);
            ClassLoader unwrapped = (ClassLoader) f.get(this.javacProcessingEnv);
            if (unwrapped == null) {
                return;
            }
            ClassLoader wrapped = wrapClassLoader(unwrapped);
            f.set(this.javacProcessingEnv, wrapped);
        } catch (NoSuchFieldException e) {
        } catch (Throwable t) {
            throw Lombok.sneakyThrow(t);
        }
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws IOException {
        Set<Long> newLevels;
        if (this.lombokDisabled || roundEnv.processingOver()) {
            return false;
        }
        String randomModuleName = null;
        for (Element element : roundEnv.getRootElements()) {
            if (randomModuleName == null) {
                randomModuleName = getModuleNameFor(element);
            }
            JCTree.JCCompilationUnit unit = toUnit(element);
            if (unit != null && !this.roots.containsKey(unit)) {
                this.roots.put(unit, Long.valueOf(this.priorityLevels[0]));
            }
        }
        do {
            for (long prio : this.priorityLevels) {
                List<JCTree.JCCompilationUnit> cusForThisRound = new ArrayList<>();
                for (Map.Entry<JCTree.JCCompilationUnit, Long> entry : this.roots.entrySet()) {
                    Long prioOfCu = entry.getValue();
                    if (prioOfCu != null && prioOfCu.longValue() == prio) {
                        cusForThisRound.add(entry.getKey());
                    }
                }
                this.transformer.transform(prio, this.javacProcessingEnv.getContext(), cusForThisRound);
            }
            newLevels = new HashSet<>();
            int i = this.priorityLevels.length - 1;
            while (i >= 0) {
                Long curLevel = Long.valueOf(this.priorityLevels[i]);
                Long nextLevel = i == this.priorityLevels.length - 1 ? null : Long.valueOf(this.priorityLevels[i + 1]);
                List<JCTree.JCCompilationUnit> cusToAdvance = new ArrayList<>();
                for (Map.Entry<JCTree.JCCompilationUnit, Long> entry2 : this.roots.entrySet()) {
                    if (curLevel.equals(entry2.getValue())) {
                        cusToAdvance.add(entry2.getKey());
                        newLevels.add(nextLevel);
                    }
                }
                Iterator<JCTree.JCCompilationUnit> it = cusToAdvance.iterator();
                while (it.hasNext()) {
                    this.roots.put(it.next(), nextLevel);
                }
                i--;
            }
            newLevels.remove(null);
            if (newLevels.isEmpty()) {
                return false;
            }
            newLevels.retainAll(this.priorityLevelsRequiringResolutionReset);
        } while (newLevels.isEmpty());
        forceNewRound(randomModuleName, this.javacFiler);
        return false;
    }

    private void forceNewRound(String randomModuleName, JavacFiler filer) throws IOException {
        if (!filer.newFiles()) {
            try {
                StringBuilder sbAppend = new StringBuilder().append("lombok.dummy.ForceNewRound");
                int i = this.dummyCount;
                this.dummyCount = i + 1;
                String name = sbAppend.append(i).toString();
                if (randomModuleName != null) {
                    name = randomModuleName + "/" + name;
                }
                JavaFileObject dummy = filer.createSourceFile(name, new Element[0]);
                Writer w = dummy.openWriter();
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Can't force a new processing round. Lombok won't work.");
            }
        }
    }

    private String getModuleNameFor(Element element) {
        while (element != null) {
            if (element.getKind().name().equals("MODULE")) {
                String n = element.getSimpleName().toString().trim();
                if (n.isEmpty()) {
                    return null;
                }
                return n;
            }
            Element n2 = element.getEnclosingElement();
            if (n2 == element) {
                return null;
            }
            element = n2;
        }
        return null;
    }

    private JCTree.JCCompilationUnit toUnit(Element element) {
        TreePath path = this.trees == null ? null : this.trees.getPath(element);
        if (path == null) {
            return null;
        }
        return path.getCompilationUnit();
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    public JavacProcessingEnvironment getJavacProcessingEnvironment(ProcessingEnvironment procEnv) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Class<?> procEnvClass = procEnv.getClass();
        if (procEnv.getClass().getName().equals("org.gradle.api.internal.tasks.compile.processing.IncrementalProcessingEnvironment")) {
            try {
                Field field = procEnvClass.getDeclaredField("delegate");
                field.setAccessible(true);
                Object delegate = field.get(procEnv);
                return (JavacProcessingEnvironment) delegate;
            } catch (Exception e) {
                e.printStackTrace();
                procEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Can't get the delegate of the gradle IncrementalProcessingEnvironment. Lombok won't work.");
            }
        }
        return (JavacProcessingEnvironment) procEnv;
    }

    public JavacFiler getJavacFiler(Filer filer) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Class<?> filerSuperClass = filer.getClass().getSuperclass();
        if (filerSuperClass.getName().equals("org.gradle.api.internal.tasks.compile.processing.IncrementalFiler")) {
            try {
                Field field = filerSuperClass.getDeclaredField("delegate");
                field.setAccessible(true);
                Object delegate = field.get(filer);
                return (JavacFiler) delegate;
            } catch (Exception e) {
                e.printStackTrace();
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Can't get the delegate of the gradle IncrementalFiler. Lombok won't work.");
            }
        }
        return (JavacFiler) filer;
    }
}
