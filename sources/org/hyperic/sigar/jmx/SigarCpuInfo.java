package org.hyperic.sigar.jmx;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import org.apache.xmlbeans.XmlErrorCodes;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarCpuInfo.class */
public class SigarCpuInfo extends AbstractMBean {
    private static final String MBEAN_TYPE = "CpuInfoList";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX_SIGAR;
    private static final MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private int cpuIndex;
    private String objectName;
    static Class class$org$hyperic$sigar$Sigar;
    static Class class$org$hyperic$sigar$jmx$SigarCpuInfo;
    private static final MBeanAttributeInfo MBEAN_ATTR_CPUINDEX = new MBeanAttributeInfo("CpuIndex", XmlErrorCodes.INT, "The index of the CPU, typically starting at 0", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_CACHESIZE = new MBeanAttributeInfo("CacheSize", XmlErrorCodes.LONG, "The cache size of the CPU, in [byte]", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_MHZ = new MBeanAttributeInfo("Mhz", XmlErrorCodes.INT, "The clock speed of the CPU, in [MHz]", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_MODEL = new MBeanAttributeInfo("Model", "java.lang.String", "The CPU model reported", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_VENDOR = new MBeanAttributeInfo("Vendor", "java.lang.String", "The CPU vendor reported", true, false, false);
    private static final MBeanParameterInfo MBEAN_PARAM_CPUINDEX = new MBeanParameterInfo("cpuIndex", XmlErrorCodes.INT, "The index of the CPU to read data for. Must be >= 0 and not exceed the CPU count of the system");

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (class$org$hyperic$sigar$Sigar == null) {
            clsClass$ = class$("org.hyperic.sigar.Sigar");
            class$org$hyperic$sigar$Sigar = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$Sigar;
        }
        MBEAN_PARAM_SIGAR = new MBeanParameterInfo(SigarInvokerJMX.DOMAIN_NAME, clsClass$.getName(), "The Sigar instance to use to fetch data from");
        if (class$org$hyperic$sigar$jmx$SigarCpuInfo == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarCpuInfo");
            class$org$hyperic$sigar$jmx$SigarCpuInfo = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarCpuInfo;
        }
        MBEAN_CONSTR_CPUINDEX = new MBeanConstructorInfo(clsClass$2.getName(), "Creates a new instance for the CPU index specified, using a new Sigar instance to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[]{MBEAN_PARAM_CPUINDEX});
        if (class$org$hyperic$sigar$jmx$SigarCpuInfo == null) {
            clsClass$3 = class$("org.hyperic.sigar.jmx.SigarCpuInfo");
            class$org$hyperic$sigar$jmx$SigarCpuInfo = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$jmx$SigarCpuInfo;
        }
        MBEAN_CONSTR_CPUINDEX_SIGAR = new MBeanConstructorInfo(clsClass$3.getName(), "Creates a new instance for the CPU index specified, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[]{MBEAN_PARAM_SIGAR, MBEAN_PARAM_CPUINDEX});
        if (class$org$hyperic$sigar$jmx$SigarCpuInfo == null) {
            clsClass$4 = class$("org.hyperic.sigar.jmx.SigarCpuInfo");
            class$org$hyperic$sigar$jmx$SigarCpuInfo = clsClass$4;
        } else {
            clsClass$4 = class$org$hyperic$sigar$jmx$SigarCpuInfo;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$4.getName(), "Sigar CPU Info MBean, provides overall information for a single CPU. This information only changes if, for example, a CPU is reducing its clock frequency or shutting down part of its cache. Subsequent requests are satisfied from within a cache that invalidates after 30 seconds.", new MBeanAttributeInfo[]{MBEAN_ATTR_CPUINDEX, MBEAN_ATTR_CACHESIZE, MBEAN_ATTR_MHZ, MBEAN_ATTR_MODEL, MBEAN_ATTR_VENDOR}, new MBeanConstructorInfo[]{MBEAN_CONSTR_CPUINDEX, MBEAN_CONSTR_CPUINDEX_SIGAR}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarCpuInfo(int index) throws IllegalArgumentException {
        this(new Sigar(), index);
    }

    public SigarCpuInfo(Sigar sigar, int index) {
        super(sigar, (short) 0);
        if (index < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("CPU index has to be non-negative: ").append(index).toString());
        }
        try {
            int cpuCount = sigar.getCpuInfoList().length;
            if (cpuCount < index) {
                throw new IllegalArgumentException(new StringBuffer().append("CPU index out of range (found ").append(cpuCount).append(" CPU(s)): ").append(index).toString());
            }
            this.cpuIndex = index;
            this.objectName = new StringBuffer().append("sigar:type=CpuInfo,").append(MBEAN_ATTR_CPUINDEX.getName().substring(0, 1).toLowerCase()).append(MBEAN_ATTR_CPUINDEX.getName().substring(1)).append(SymbolConstants.EQUAL_SYMBOL).append(this.cpuIndex).toString();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public String getObjectName() {
        return this.objectName;
    }

    public int getCpuIndex() {
        return this.cpuIndex;
    }

    public long getCacheSize() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getCacheSize();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public int getMhz() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getMhz();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public String getModel() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getModel();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public String getVendor() {
        try {
            return this.sigar.getCpuInfoList()[this.cpuIndex].getVendor();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws AttributeNotFoundException {
        if (MBEAN_ATTR_CACHESIZE.getName().equals(attr)) {
            return new Long(getCacheSize());
        }
        if (MBEAN_ATTR_CPUINDEX.getName().equals(attr)) {
            return new Integer(getCpuIndex());
        }
        if (MBEAN_ATTR_MHZ.getName().equals(attr)) {
            return new Integer(getMhz());
        }
        if (MBEAN_ATTR_MODEL.getName().equals(attr)) {
            return getModel();
        }
        if (MBEAN_ATTR_VENDOR.getName().equals(attr)) {
            return getVendor();
        }
        throw new AttributeNotFoundException(attr);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public void setAttribute(Attribute attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr.getName());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.ReflectionException */
    public Object invoke(String actionName, Object[] params, String[] signature) throws ReflectionException {
        throw new ReflectionException(new NoSuchMethodException(actionName), actionName);
    }

    public MBeanInfo getMBeanInfo() {
        return MBEAN_INFO;
    }
}
