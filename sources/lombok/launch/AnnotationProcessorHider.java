package lombok.launch;

import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.mapstruct.ap.spi.AstModifyingAnnotationProcessor;
import sun.misc.Unsafe;

/* compiled from: AnnotationProcessor.java */
/* loaded from: lombok-1.16.22.jar:lombok/launch/AnnotationProcessorHider.class */
class AnnotationProcessorHider {
    AnnotationProcessorHider() {
    }

    /* compiled from: AnnotationProcessor.java */
    /* loaded from: lombok-1.16.22.jar:lombok/launch/AnnotationProcessorHider$AstModificationNotifier.class */
    public static class AstModificationNotifier implements AstModifyingAnnotationProcessor {
        public boolean isTypeComplete(TypeMirror type) {
            if (System.getProperty("lombok.disable") != null) {
                return true;
            }
            return AstModificationNotifierData.lombokInvoked;
        }
    }

    /* compiled from: AnnotationProcessor.java */
    /* loaded from: lombok-1.16.22.jar:lombok/launch/AnnotationProcessorHider$AstModificationNotifierData.class */
    static class AstModificationNotifierData {
        static volatile boolean lombokInvoked = false;

        AstModificationNotifierData() {
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/launch/AnnotationProcessorHider$AnnotationProcessor.class */
    public static class AnnotationProcessor extends AbstractProcessor {
        private final AbstractProcessor instance = createWrappedInstance();

        public Set<String> getSupportedOptions() {
            return this.instance.getSupportedOptions();
        }

        public Set<String> getSupportedAnnotationTypes() {
            return this.instance.getSupportedAnnotationTypes();
        }

        public SourceVersion getSupportedSourceVersion() {
            return this.instance.getSupportedSourceVersion();
        }

        public void init(ProcessingEnvironment processingEnv) {
            disableJava9SillyWarning();
            AstModificationNotifierData.lombokInvoked = true;
            this.instance.init(processingEnv);
            super.init(processingEnv);
        }

        private void disableJava9SillyWarning() {
            try {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Unsafe u = (Unsafe) theUnsafe.get(null);
                Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
                Field logger = cls.getDeclaredField("logger");
                u.putObjectVolatile(cls, u.staticFieldOffset(logger), (Object) null);
            } catch (Throwable unused) {
            }
        }

        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            return this.instance.process(annotations, roundEnv);
        }

        public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
            return this.instance.getCompletions(element, annotation, member, userText);
        }

        private static AbstractProcessor createWrappedInstance() {
            ClassLoader cl = Main.createShadowClassLoader();
            try {
                Class<?> mc = cl.loadClass("lombok.core.AnnotationProcessor");
                return (AbstractProcessor) mc.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Throwable t) {
                if (t instanceof Error) {
                    throw ((Error) t);
                }
                if (t instanceof RuntimeException) {
                    throw ((RuntimeException) t);
                }
                throw new RuntimeException(t);
            }
        }
    }

    /* compiled from: AnnotationProcessor.java */
    @SupportedAnnotationTypes({"lombok.*"})
    /* loaded from: lombok-1.16.22.jar:lombok/launch/AnnotationProcessorHider$ClaimingProcessor.class */
    public static class ClaimingProcessor extends AbstractProcessor {
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            return true;
        }

        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latest();
        }
    }
}
