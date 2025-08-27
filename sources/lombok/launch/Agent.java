package lombok.launch;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: lombok-1.16.22.jar:lombok/launch/Agent.class */
final class Agent {
    Agent() {
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        runLauncher(agentArgs, instrumentation, true);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        runLauncher(agentArgs, instrumentation, false);
    }

    private static void runLauncher(String agentArgs, Instrumentation instrumentation, boolean injected) throws Throwable {
        ClassLoader cl = Main.createShadowClassLoader();
        try {
            Class<?> c = cl.loadClass("lombok.core.AgentLauncher");
            Method m = c.getDeclaredMethod("runAgents", String.class, Instrumentation.class, Boolean.TYPE, Class.class);
            m.invoke(null, agentArgs, instrumentation, Boolean.valueOf(injected), Agent.class);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
