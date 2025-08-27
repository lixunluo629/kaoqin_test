package lombok.eclipse.agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Lombok;

/* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchDelegatePortal.SCL.lombok */
public class PatchDelegatePortal {
    static final String CLASS_SCOPE = "org.eclipse.jdt.internal.compiler.lookup.ClassScope";

    public static boolean handleDelegateForType(Object classScope) {
        try {
            return ((Boolean) Reflection.handleDelegateForType.invoke(null, classScope)).booleanValue();
        } catch (IllegalAccessException e) {
            throw Lombok.sneakyThrow(e);
        } catch (NoClassDefFoundError unused) {
            return false;
        } catch (NullPointerException e2) {
            if (!"false".equals(System.getProperty("lombok.debug.reflection", "false"))) {
                e2.initCause(Reflection.problem);
                throw e2;
            }
            return false;
        } catch (InvocationTargetException e3) {
            throw Lombok.sneakyThrow(e3.getCause());
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/eclipse/agent/PatchDelegatePortal$Reflection.SCL.lombok */
    private static final class Reflection {
        public static final Method handleDelegateForType;
        public static final Throwable problem;

        private Reflection() {
        }

        static {
            Method m = null;
            Throwable problem_ = null;
            try {
                m = PatchDelegate.class.getMethod("handleDelegateForType", Class.forName(PatchDelegatePortal.CLASS_SCOPE));
            } catch (Throwable t) {
                problem_ = t;
            }
            handleDelegateForType = m;
            problem = problem_;
        }
    }
}
