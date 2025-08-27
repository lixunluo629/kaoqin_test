package org.ehcache.sizeof.impl;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/JvmInformation.class */
public enum JvmInformation {
    UNKNOWN_32_BIT(null) { // from class: org.ehcache.sizeof.impl.JvmInformation.1
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "Unrecognized 32-Bit JVM";
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getPointerSize() {
            return 4;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getJavaPointerSize() {
            return 4;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectAlignment() {
            return 8;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getFieldOffsetAdjustment() {
            return 0;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getAgentSizeOfAdjustment() {
            return 0;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsAgentSizeOf() {
            return true;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsUnsafeSizeOf() {
            return true;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsReflectionSizeOf() {
            return true;
        }
    },
    UNKNOWN_64_BIT(UNKNOWN_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.2
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getPointerSize() {
            return 8;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getJavaPointerSize() {
            return 8;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "Unrecognized 64-Bit JVM";
        }
    },
    HOTSPOT_32_BIT(UNKNOWN_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.3
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "32-Bit HotSpot JVM";
        }
    },
    HOTSPOT_32_BIT_WITH_CONCURRENT_MARK_AND_SWEEP(UNKNOWN_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.4
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "32-Bit HotSpot JVM with Concurrent Mark-and-Sweep GC";
        }
    },
    HOTSPOT_64_BIT(UNKNOWN_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.5
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit HotSpot JVM";
        }
    },
    HOTSPOT_64_BIT_WITH_CONCURRENT_MARK_AND_SWEEP(HOTSPOT_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.6
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit HotSpot JVM with Concurrent Mark-and-Sweep GC";
        }
    },
    HOTSPOT_64_BIT_WITH_COMPRESSED_OOPS(HOTSPOT_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.7
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getJavaPointerSize() {
            return 4;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit HotSpot JVM with Compressed OOPs";
        }
    },
    HOTSPOT_64_BIT_WITH_COMPRESSED_OOPS_AND_CONCURRENT_MARK_AND_SWEEP(HOTSPOT_64_BIT_WITH_COMPRESSED_OOPS) { // from class: org.ehcache.sizeof.impl.JvmInformation.8
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit HotSpot JVM with Compressed OOPs and Concurrent Mark-and-Sweep GC";
        }
    },
    OPENJDK_32_BIT(HOTSPOT_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.9
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "32-Bit OpenJDK JVM";
        }
    },
    OPENJDK_32_BIT_WITH_CONCURRENT_MARK_AND_SWEEP(HOTSPOT_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.10
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "32-Bit OpenJDK JVM with Concurrent Mark-and-Sweep GC";
        }
    },
    OPENJDK_64_BIT(HOTSPOT_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.11
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit OpenJDK JVM";
        }
    },
    OPENJDK_64_BIT_WITH_CONCURRENT_MARK_AND_SWEEP(OPENJDK_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.12
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit OpenJDK JVM with Concurrent Mark-and-Sweep GC";
        }
    },
    OPENJDK_64_BIT_WITH_COMPRESSED_OOPS(OPENJDK_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.13
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getJavaPointerSize() {
            return 4;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit OpenJDK JVM with Compressed OOPs";
        }
    },
    OPENJDK_64_BIT_WITH_COMPRESSED_OOPS_AND_CONCURRENT_MARK_AND_SWEEP(OPENJDK_64_BIT_WITH_COMPRESSED_OOPS) { // from class: org.ehcache.sizeof.impl.JvmInformation.14
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getMinimumObjectSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit OpenJDK JVM with Compressed OOPs and Concurrent Mark-and-Sweep GC";
        }
    },
    JROCKIT_32_BIT(UNKNOWN_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.15
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getAgentSizeOfAdjustment() {
            return 8;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getFieldOffsetAdjustment() {
            return 8;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "32-Bit JRockit JVM";
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsReflectionSizeOf() {
            return false;
        }
    },
    JROCKIT_64_BIT(JROCKIT_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.16
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit JRockit JVM (with no reference compression)";
        }
    },
    JROCKIT_64_BIT_WITH_64GB_COMPRESSED_REFS(JROCKIT_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.17
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectAlignment() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getAgentSizeOfAdjustment() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getFieldOffsetAdjustment() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "64-Bit JRockit JVM with 64GB Compressed References";
        }
    },
    IBM_32_BIT(UNKNOWN_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.18
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "IBM 32-Bit JVM";
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsReflectionSizeOf() {
            return false;
        }
    },
    IBM_64_BIT(UNKNOWN_64_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.19
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 24;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public boolean supportsReflectionSizeOf() {
            return false;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "IBM 64-Bit JVM (with no reference compression)";
        }
    },
    IBM_64_BIT_WITH_COMPRESSED_REFS(IBM_32_BIT) { // from class: org.ehcache.sizeof.impl.JvmInformation.20
        @Override // org.ehcache.sizeof.impl.JvmInformation
        public int getObjectHeaderSize() {
            return 16;
        }

        @Override // org.ehcache.sizeof.impl.JvmInformation
        public String getJvmDescription() {
            return "IBM 64-Bit JVM with Compressed References";
        }
    };

    private static final long TWENTY_FIVE_GB = 26843545600L;
    private static final long FIFTY_SEVEN_GB = 61203283968L;
    private JvmInformation parent;
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) JvmInformation.class);
    public static final JvmInformation CURRENT_JVM_INFORMATION = getJvmInformation();

    public abstract String getJvmDescription();

    static {
        LOGGER.info("Detected JVM data model settings of: " + CURRENT_JVM_INFORMATION.getJvmDescription());
    }

    JvmInformation(JvmInformation parent) {
        this.parent = parent;
    }

    public int getPointerSize() {
        return this.parent.getPointerSize();
    }

    public int getJavaPointerSize() {
        return this.parent.getJavaPointerSize();
    }

    public int getMinimumObjectSize() {
        return getObjectAlignment();
    }

    public int getObjectAlignment() {
        return this.parent.getObjectAlignment();
    }

    public int getObjectHeaderSize() {
        return getPointerSize() + getJavaPointerSize();
    }

    public int getFieldOffsetAdjustment() {
        return this.parent.getFieldOffsetAdjustment();
    }

    public int getAgentSizeOfAdjustment() {
        return this.parent.getAgentSizeOfAdjustment();
    }

    public boolean supportsAgentSizeOf() {
        return this.parent.supportsAgentSizeOf();
    }

    public boolean supportsUnsafeSizeOf() {
        return this.parent.supportsUnsafeSizeOf();
    }

    public boolean supportsReflectionSizeOf() {
        return this.parent.supportsReflectionSizeOf();
    }

    private static JvmInformation getJvmInformation() {
        JvmInformation jif = detectHotSpot();
        if (jif == null) {
            jif = detectOpenJDK();
        }
        if (jif == null) {
            jif = detectJRockit();
        }
        if (jif == null) {
            jif = detectIBM();
        }
        if (jif == null && is64Bit()) {
            jif = UNKNOWN_64_BIT;
        } else if (jif == null) {
            jif = UNKNOWN_32_BIT;
        }
        return jif;
    }

    private static JvmInformation detectHotSpot() {
        JvmInformation jif = null;
        if (isHotspot()) {
            if (is64Bit()) {
                if (isHotspotCompressedOops() && isHotspotConcurrentMarkSweepGC()) {
                    jif = HOTSPOT_64_BIT_WITH_COMPRESSED_OOPS_AND_CONCURRENT_MARK_AND_SWEEP;
                } else if (isHotspotCompressedOops()) {
                    jif = HOTSPOT_64_BIT_WITH_COMPRESSED_OOPS;
                } else if (isHotspotConcurrentMarkSweepGC()) {
                    jif = HOTSPOT_64_BIT_WITH_CONCURRENT_MARK_AND_SWEEP;
                } else {
                    jif = HOTSPOT_64_BIT;
                }
            } else if (isHotspotConcurrentMarkSweepGC()) {
                jif = HOTSPOT_32_BIT_WITH_CONCURRENT_MARK_AND_SWEEP;
            } else {
                jif = HOTSPOT_32_BIT;
            }
        }
        return jif;
    }

    private static JvmInformation detectOpenJDK() {
        JvmInformation jif = null;
        if (isOpenJDK()) {
            if (is64Bit()) {
                if (isHotspotCompressedOops() && isHotspotConcurrentMarkSweepGC()) {
                    jif = OPENJDK_64_BIT_WITH_COMPRESSED_OOPS_AND_CONCURRENT_MARK_AND_SWEEP;
                } else if (isHotspotCompressedOops()) {
                    jif = OPENJDK_64_BIT_WITH_COMPRESSED_OOPS;
                } else if (isHotspotConcurrentMarkSweepGC()) {
                    jif = OPENJDK_64_BIT_WITH_CONCURRENT_MARK_AND_SWEEP;
                } else {
                    jif = OPENJDK_64_BIT;
                }
            } else if (isHotspotConcurrentMarkSweepGC()) {
                jif = OPENJDK_32_BIT_WITH_CONCURRENT_MARK_AND_SWEEP;
            } else {
                jif = OPENJDK_32_BIT;
            }
        }
        return jif;
    }

    private static JvmInformation detectJRockit() {
        JvmInformation jif = null;
        if (isJRockit()) {
            if (is64Bit()) {
                if (isJRockit64GBCompression()) {
                    jif = JROCKIT_64_BIT_WITH_64GB_COMPRESSED_REFS;
                } else {
                    jif = JROCKIT_64_BIT;
                }
            } else {
                jif = JROCKIT_32_BIT;
            }
        }
        return jif;
    }

    private static JvmInformation detectIBM() {
        JvmInformation jif = null;
        if (isIBM()) {
            if (is64Bit()) {
                if (isIBMCompressedRefs()) {
                    jif = IBM_64_BIT_WITH_COMPRESSED_REFS;
                } else {
                    jif = IBM_64_BIT;
                }
            } else {
                jif = IBM_32_BIT;
            }
        }
        return jif;
    }

    private static boolean isJRockit64GBCompression() {
        if (getJRockitVmArgs().contains("-XXcompressedRefs:enable=false") || getJRockitVmArgs().contains("-XXcompressedRefs:size=4GB") || getJRockitVmArgs().contains("-XXcompressedRefs:size=32GB")) {
            return false;
        }
        if (getJRockitVmArgs().contains("-XXcompressedRefs:size=64GB")) {
            return true;
        }
        if (Runtime.getRuntime().maxMemory() > TWENTY_FIVE_GB && Runtime.getRuntime().maxMemory() <= FIFTY_SEVEN_GB && getJRockitVmArgs().contains("-XXcompressedRefs:enable=true")) {
            return true;
        }
        return false;
    }

    public static boolean isJRockit() {
        return System.getProperty("jrockit.version") != null || System.getProperty("java.vm.name", "").toLowerCase().contains("jrockit");
    }

    public static boolean isOSX() {
        String vendor = System.getProperty("java.vm.vendor");
        return vendor != null && vendor.startsWith("Apple");
    }

    public static boolean isHotspot() {
        return System.getProperty("java.vm.name", "").toLowerCase().contains("hotspot");
    }

    public static boolean isOpenJDK() {
        return System.getProperty("java.vm.name", "").toLowerCase().contains("openjdk");
    }

    public static boolean isIBM() {
        return System.getProperty("java.vm.name", "").contains("IBM") && System.getProperty("java.vm.vendor").contains("IBM");
    }

    private static boolean isIBMCompressedRefs() {
        return System.getProperty("com.ibm.oti.vm.bootstrap.library.path", "").contains("compressedrefs");
    }

    private static boolean isHotspotCompressedOops() {
        String value = getHotSpotVmOptionValue("UseCompressedOops");
        if (value == null) {
            return false;
        }
        return Boolean.valueOf(value).booleanValue();
    }

    private static String getHotSpotVmOptionValue(String name) {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName beanName = ObjectName.getInstance("com.sun.management:type=HotSpotDiagnostic");
            Object vmOption = server.invoke(beanName, "getVMOption", new Object[]{name}, new String[]{"java.lang.String"});
            return (String) ((CompositeData) vmOption).get("value");
        } catch (Throwable th) {
            return null;
        }
    }

    private static String getPlatformMBeanAttribute(String beanName, String attrName) {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance(beanName);
            Object attr = server.getAttribute(name, attrName).toString();
            if (attr != null) {
                return attr.toString();
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private static String getJRockitVmArgs() {
        return getPlatformMBeanAttribute("oracle.jrockit.management:type=PerfCounters", "java.rt.vmArgs");
    }

    private static boolean isHotspotConcurrentMarkSweepGC() {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if ("ConcurrentMarkSweep".equals(bean.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean is64Bit() {
        String systemProp = System.getProperty("com.ibm.vm.bitmode");
        if (systemProp != null) {
            return systemProp.equals("64");
        }
        String systemProp2 = System.getProperty("sun.arch.data.model");
        if (systemProp2 != null) {
            return systemProp2.equals("64");
        }
        String systemProp3 = System.getProperty("java.vm.version");
        if (systemProp3 != null) {
            return systemProp3.contains("_64");
        }
        return false;
    }
}
