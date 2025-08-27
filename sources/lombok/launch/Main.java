package lombok.launch;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/* loaded from: lombok-1.16.22.jar:lombok/launch/Main.class */
class Main {
    Main() {
    }

    static ClassLoader createShadowClassLoader() {
        return new ShadowClassLoader(Main.class.getClassLoader(), "lombok", null, Arrays.asList(new String[0]), Arrays.asList("lombok.patcher.Symbols"));
    }

    public static void main(String[] args) throws Throwable {
        ClassLoader cl = createShadowClassLoader();
        Class<?> mc = cl.loadClass("lombok.core.Main");
        try {
            mc.getMethod("main", String[].class).invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
