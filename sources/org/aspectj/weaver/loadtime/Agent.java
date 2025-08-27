package org.aspectj.weaver.loadtime;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/Agent.class */
public class Agent {
    private static Instrumentation s_instrumentation;
    private static ClassFileTransformer s_transformer = new ClassPreProcessorAgentAdapter();

    public static void premain(String options, Instrumentation instrumentation) {
        if (s_instrumentation != null) {
            return;
        }
        s_instrumentation = instrumentation;
        s_instrumentation.addTransformer(s_transformer);
    }

    public static void agentmain(String options, Instrumentation instrumentation) {
        premain(options, instrumentation);
    }

    public static Instrumentation getInstrumentation() {
        if (s_instrumentation == null) {
            throw new UnsupportedOperationException("AspectJ weaving agent was neither started via '-javaagent' (preMain) nor attached via 'VirtualMachine.loadAgent' (agentMain)");
        }
        return s_instrumentation;
    }
}
