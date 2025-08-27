package org.ehcache.sizeof.impl;

import java.lang.instrument.Instrumentation;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/sizeof-agent.jar:org/ehcache/sizeof/impl/SizeOfAgent.class */
public class SizeOfAgent {
    private static volatile Instrumentation instrumentation;
    private static final String NO_INSTRUMENTATION_SYSTEM_PROPERTY_NAME = "org.ehcache.sizeof.agent.instrumentationSystemProperty";

    public static void premain(String options, Instrumentation inst) {
        instrumentation = inst;
        registerSystemProperty();
    }

    public static void agentmain(String options, Instrumentation inst) {
        instrumentation = inst;
        registerSystemProperty();
    }

    private static void registerSystemProperty() {
        if (Boolean.getBoolean(NO_INSTRUMENTATION_SYSTEM_PROPERTY_NAME)) {
            System.getProperties().put("org.ehcache.sizeof.agent.instrumentation", instrumentation);
        }
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    private SizeOfAgent() {
    }
}
