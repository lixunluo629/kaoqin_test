package org.ehcache.sizeof.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.TempFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/AgentLoader.class */
final class AgentLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) AgentLoader.class);
    private static final String SIZEOF_AGENT_CLASSNAME = "org.ehcache.sizeof.impl.SizeOfAgent";
    private static final String VIRTUAL_MACHINE_CLASSNAME = "com.sun.tools.attach.VirtualMachine";
    private static final Method VIRTUAL_MACHINE_ATTACH;
    private static final Method VIRTUAL_MACHINE_DETACH;
    private static final Method VIRTUAL_MACHINE_LOAD_AGENT;
    private static volatile Instrumentation instrumentation;
    static final String INSTRUMENTATION_INSTANCE_SYSTEM_PROPERTY_NAME = "org.ehcache.sizeof.agent.instrumentation";

    AgentLoader() {
    }

    static {
        Method attach = null;
        Method detach = null;
        Method loadAgent = null;
        try {
            Class<?> virtualMachineClass = getVirtualMachineClass();
            attach = virtualMachineClass.getMethod("attach", String.class);
            detach = virtualMachineClass.getMethod("detach", new Class[0]);
            loadAgent = virtualMachineClass.getMethod("loadAgent", String.class);
        } catch (Throwable e) {
            LOGGER.info("Unavailable or unrecognised attach API : {}", e.toString());
        }
        VIRTUAL_MACHINE_ATTACH = attach;
        VIRTUAL_MACHINE_DETACH = detach;
        VIRTUAL_MACHINE_LOAD_AGENT = loadAgent;
    }

    private static Class<?> getVirtualMachineClass() throws ClassNotFoundException {
        try {
            return (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() { // from class: org.ehcache.sizeof.impl.AgentLoader.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Class<?> run() throws Exception {
                    try {
                        return ClassLoader.getSystemClassLoader().loadClass(AgentLoader.VIRTUAL_MACHINE_CLASSNAME);
                    } catch (ClassNotFoundException e) {
                        for (File jar : AgentLoader.getPossibleToolsJars()) {
                            try {
                                Class<?> vmClass = new URLClassLoader(new URL[]{jar.toURL()}).loadClass(AgentLoader.VIRTUAL_MACHINE_CLASSNAME);
                                AgentLoader.LOGGER.info("Located valid 'tools.jar' at '{}'", jar);
                                return vmClass;
                            } catch (Throwable t) {
                                AgentLoader.LOGGER.info("Exception while loading tools.jar from '{}': {}", jar, t);
                            }
                        }
                        throw new ClassNotFoundException(AgentLoader.VIRTUAL_MACHINE_CLASSNAME);
                    }
                }
            });
        } catch (PrivilegedActionException pae) {
            Throwable actual = pae.getCause();
            if (actual instanceof ClassNotFoundException) {
                throw ((ClassNotFoundException) actual);
            }
            throw new AssertionError("Unexpected checked exception : " + actual);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<File> getPossibleToolsJars() {
        List<File> jars = new ArrayList<>();
        File javaHome = new File(System.getProperty("java.home"));
        File jreSourced = new File(javaHome, "lib/tools.jar");
        if (jreSourced.exists()) {
            jars.add(jreSourced);
        }
        if ("jre".equals(javaHome.getName())) {
            File jdkHome = new File(javaHome, "../");
            File jdkSourced = new File(jdkHome, "lib/tools.jar");
            if (jdkSourced.exists()) {
                jars.add(jdkSourced);
            }
        }
        return jars;
    }

    static boolean loadAgent() {
        boolean b;
        synchronized (AgentLoader.class.getName().intern()) {
            if (!agentIsAvailable() && VIRTUAL_MACHINE_LOAD_AGENT != null) {
                try {
                    warnIfOSX();
                    String name = ManagementFactory.getRuntimeMXBean().getName();
                    Object vm = VIRTUAL_MACHINE_ATTACH.invoke(null, name.substring(0, name.indexOf(64)));
                    try {
                        File agent = getAgentFile();
                        LOGGER.info("Trying to load agent @ {}", agent);
                        if (agent != null) {
                            VIRTUAL_MACHINE_LOAD_AGENT.invoke(vm, agent.getAbsolutePath());
                        }
                        VIRTUAL_MACHINE_DETACH.invoke(vm, new Object[0]);
                    } catch (Throwable th) {
                        VIRTUAL_MACHINE_DETACH.invoke(vm, new Object[0]);
                        throw th;
                    }
                } catch (InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    LOGGER.info("Failed to attach to VM and load the agent: {}: {}", cause.getClass(), cause.getMessage());
                } catch (Throwable t) {
                    LOGGER.info("Failed to attach to VM and load the agent: {}: {}", t.getClass(), t.getMessage());
                }
            }
            b = agentIsAvailable();
            if (b) {
                LOGGER.info("Agent successfully loaded and available!");
            }
        }
        return b;
    }

    private static void warnIfOSX() {
        if (JvmInformation.isOSX() && System.getProperty(TempFile.JAVA_IO_TMPDIR) != null) {
            LOGGER.warn("Loading the SizeOfAgent will probably fail, as you are running on Apple OS X and have a value set for java.io.tmpdir\nThey both result in a bug, not yet fixed by Apple, that won't let us attach to the VM and load the agent.\nMost probably, you'll also get a full thread-dump after this because of the failure... Nothing to worry about!\nYou can bypass trying to load the Agent entirely by setting the System property 'org.ehcache.sizeof.AgentSizeOf.bypass'  to true");
        }
    }

    /* JADX WARN: Finally extract failed */
    private static File getAgentFile() throws IOException {
        URL agent = AgentLoader.class.getResource("sizeof-agent.jar");
        if (agent == null) {
            return null;
        }
        if (agent.getProtocol().equals("file")) {
            return new File(agent.getFile());
        }
        File temp = File.createTempFile("ehcache-sizeof-agent", ".jar");
        try {
            FileOutputStream fout = new FileOutputStream(temp);
            try {
                InputStream in = agent.openStream();
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int read = in.read(buffer);
                        if (read >= 0) {
                            fout.write(buffer, 0, read);
                        } else {
                            in.close();
                            fout.close();
                            LOGGER.info("Extracted agent jar to temporary file {}", temp);
                            return temp;
                        }
                    }
                } catch (Throwable th) {
                    in.close();
                    throw th;
                }
            } catch (Throwable th2) {
                fout.close();
                throw th2;
            }
        } finally {
            temp.deleteOnExit();
        }
    }

    static boolean agentIsAvailable() {
        try {
            if (instrumentation == null) {
                instrumentation = (Instrumentation) System.getProperties().get(INSTRUMENTATION_INSTANCE_SYSTEM_PROPERTY_NAME);
            }
            if (instrumentation == null) {
                Class sizeOfAgentClass = ClassLoader.getSystemClassLoader().loadClass(SIZEOF_AGENT_CLASSNAME);
                Method getInstrumentationMethod = sizeOfAgentClass.getMethod("getInstrumentation", new Class[0]);
                instrumentation = (Instrumentation) getInstrumentationMethod.invoke(sizeOfAgentClass, new Object[0]);
            }
            return instrumentation != null;
        } catch (SecurityException e) {
            LOGGER.warn("Couldn't access the system classloader because of the security policies applied by the security manager. You either want to loosen these, so ClassLoader.getSystemClassLoader() and reflection API calls are permitted or the sizing will be done using some other mechanism.\nAlternatively, set the system property org.ehcache.sizeof.agent.instrumentationSystemProperty to true to have the agent put the required instances in the System Properties for the loader to access.");
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    static long agentSizeOf(Object obj) {
        if (instrumentation == null) {
            throw new UnsupportedOperationException("Sizeof agent is not available");
        }
        return instrumentation.getObjectSize(obj);
    }
}
