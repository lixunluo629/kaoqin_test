package lombok.core;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: lombok-1.16.22.jar:lombok/core/AgentLauncher.SCL.lombok */
public class AgentLauncher {
    private static final List<AgentInfo> AGENTS = Collections.unmodifiableList(Arrays.asList(new EclipsePatcherInfo(null)));

    /* loaded from: lombok-1.16.22.jar:lombok/core/AgentLauncher$AgentLaunchable.SCL.lombok */
    public interface AgentLaunchable {
        void runAgent(String str, Instrumentation instrumentation, boolean z, Class<?> cls) throws Exception;
    }

    public static void runAgents(String agentArgs, Instrumentation instrumentation, boolean injected, Class<?> launchingContext) throws Throwable {
        for (AgentInfo info : AGENTS) {
            try {
                Class<?> agentClass = Class.forName(info.className());
                AgentLaunchable agent = (AgentLaunchable) agentClass.newInstance();
                agent.runAgent(agentArgs, instrumentation, injected, launchingContext);
            } catch (Throwable t) {
                info.problem(t, instrumentation);
            }
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/core/AgentLauncher$AgentInfo.SCL.lombok */
    private static abstract class AgentInfo {
        abstract String className();

        private AgentInfo() {
        }

        /* synthetic */ AgentInfo(AgentInfo agentInfo) {
            this();
        }

        void problem(Throwable t, Instrumentation instrumentation) throws Throwable {
            if (t instanceof ClassNotFoundException) {
                return;
            }
            if (t instanceof ClassCastException) {
                throw new InternalError("Lombok bug. Class: " + className() + " is not an implementation of lombok.core.Agent");
            }
            if (t instanceof IllegalAccessError) {
                throw new InternalError("Lombok bug. Class: " + className() + " is not public");
            }
            if (t instanceof InstantiationException) {
                throw new InternalError("Lombok bug. Class: " + className() + " is not concrete or has no public no-args constructor");
            }
            throw t;
        }
    }

    /* loaded from: lombok-1.16.22.jar:lombok/core/AgentLauncher$EclipsePatcherInfo.SCL.lombok */
    private static class EclipsePatcherInfo extends AgentInfo {
        private EclipsePatcherInfo() {
            super(null);
        }

        /* synthetic */ EclipsePatcherInfo(EclipsePatcherInfo eclipsePatcherInfo) {
            this();
        }

        @Override // lombok.core.AgentLauncher.AgentInfo
        String className() {
            return "lombok.eclipse.agent.EclipsePatcher";
        }
    }
}
