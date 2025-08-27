package org.terracotta.offheapstore.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/PhysicalMemory.class */
public class PhysicalMemory {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) PhysicalMemory.class);
    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getOperatingSystemMXBean();

    public static Long totalPhysicalMemory() {
        return (Long) getAttribute("getTotalPhysicalMemorySize");
    }

    public static Long freePhysicalMemory() {
        return (Long) getAttribute("getFreePhysicalMemorySize");
    }

    public static Long totalSwapSpace() {
        return (Long) getAttribute("getTotalSwapSpaceSize");
    }

    public static Long freeSwapSpace() {
        return (Long) getAttribute("getFreeSwapSpaceSize");
    }

    public static Long ourCommittedVirtualMemory() {
        return (Long) getAttribute("getCommittedVirtualMemorySize");
    }

    private static <T> T getAttribute(String str) {
        LOGGER.trace("Bean lookup for {}", str);
        Class<?> superclass = OS_BEAN.getClass();
        while (true) {
            Class<?> cls = superclass;
            if (cls == null) {
                for (Class<?> cls2 : OS_BEAN.getClass().getInterfaces()) {
                    try {
                        T t = (T) cls2.getMethod(str, new Class[0]).invoke(OS_BEAN, new Object[0]);
                        LOGGER.trace("Bean lookup successful using {}, got {}", cls2, t);
                        return t;
                    } catch (IllegalAccessException e) {
                        LOGGER.trace("Bean lookup failed on {}", cls2, e);
                    } catch (IllegalArgumentException e2) {
                        LOGGER.trace("Bean lookup failed on {}", cls2, e2);
                    } catch (NoSuchMethodException e3) {
                        LOGGER.trace("Bean lookup failed on {}", cls2, e3);
                    } catch (SecurityException e4) {
                        LOGGER.trace("Bean lookup failed on {}", cls2, e4);
                    } catch (InvocationTargetException e5) {
                        LOGGER.trace("Bean lookup failed on {}", cls2, e5);
                    }
                }
                LOGGER.trace("Returning null for {}", str);
                return null;
            }
            try {
                T t2 = (T) cls.getMethod(str, new Class[0]).invoke(OS_BEAN, new Object[0]);
                LOGGER.trace("Bean lookup successful using {}, got {}", cls, t2);
                return t2;
            } catch (IllegalAccessException e6) {
                LOGGER.trace("Bean lookup failed on {}", cls, e6);
            } catch (IllegalArgumentException e7) {
                LOGGER.trace("Bean lookup failed on {}", cls, e7);
            } catch (NoSuchMethodException e8) {
                LOGGER.trace("Bean lookup failed on {}", cls, e8);
            } catch (SecurityException e9) {
                LOGGER.trace("Bean lookup failed on {}", cls, e9);
            } catch (InvocationTargetException e10) {
                LOGGER.trace("Bean lookup failed on {}", cls, e10);
            }
            superclass = cls.getSuperclass();
        }
    }

    public static void main(String[] args) {
        System.out.println("Total Physical Memory: " + DebuggingUtils.toBase2SuffixedString(totalPhysicalMemory().longValue()) + "B");
        System.out.println("Free Physical Memory: " + DebuggingUtils.toBase2SuffixedString(freePhysicalMemory().longValue()) + "B");
        System.out.println("Total Swap Space: " + DebuggingUtils.toBase2SuffixedString(totalSwapSpace().longValue()) + "B");
        System.out.println("Free Swap Space: " + DebuggingUtils.toBase2SuffixedString(freeSwapSpace().longValue()) + "B");
        System.out.println("Committed Virtual Memory: " + DebuggingUtils.toBase2SuffixedString(ourCommittedVirtualMemory().longValue()) + "B");
    }
}
