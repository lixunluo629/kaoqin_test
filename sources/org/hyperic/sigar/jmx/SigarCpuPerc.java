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

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarCpuPerc.class */
public class SigarCpuPerc extends AbstractMBean {
    private static final String MBEAN_TYPE = "CpuPercList";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX;
    private static final MBeanConstructorInfo MBEAN_CONSTR_CPUINDEX_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private int cpuIndex;
    private String objectName;
    static Class class$org$hyperic$sigar$Sigar;
    static Class class$org$hyperic$sigar$jmx$SigarCpuPerc;
    private static final MBeanAttributeInfo MBEAN_ATTR_CPUINDEX = new MBeanAttributeInfo("CpuIndex", XmlErrorCodes.INT, "The index of the CPU, typically starting at 0", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_COMBINED = new MBeanAttributeInfo("Combined", XmlErrorCodes.DOUBLE, "The total time of the CPU, as a fraction of 1", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_IDLE = new MBeanAttributeInfo("Idle", XmlErrorCodes.DOUBLE, "The idle time of the CPU, as a fraction of 1", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_NICE = new MBeanAttributeInfo("Nice", XmlErrorCodes.DOUBLE, "The time of the CPU spent on nice priority, as a fraction of 1", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_SYS = new MBeanAttributeInfo("Sys", XmlErrorCodes.DOUBLE, "The time of the CPU used by the system, as a fraction of 1", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_USER = new MBeanAttributeInfo("User", XmlErrorCodes.DOUBLE, "The time of the CPU used by user processes, as a fraction of 1", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_WAIT = new MBeanAttributeInfo("Wait", XmlErrorCodes.DOUBLE, "The time the CPU had to wait for data to be loaded, as a fraction of 1", true, false, false);
    private static MBeanParameterInfo MBEAN_PARAM_CPUINDEX = new MBeanParameterInfo("cpuIndex", XmlErrorCodes.INT, "The index of the CPU to read data for. Must be >= 0 and not exceed the CPU count of the system");

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
        if (class$org$hyperic$sigar$jmx$SigarCpuPerc == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarCpuPerc");
            class$org$hyperic$sigar$jmx$SigarCpuPerc = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarCpuPerc;
        }
        MBEAN_CONSTR_CPUINDEX = new MBeanConstructorInfo(clsClass$2.getName(), "Creates a new instance for the CPU index specified, using a new Sigar instance to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[]{MBEAN_PARAM_CPUINDEX});
        if (class$org$hyperic$sigar$jmx$SigarCpuPerc == null) {
            clsClass$3 = class$("org.hyperic.sigar.jmx.SigarCpuPerc");
            class$org$hyperic$sigar$jmx$SigarCpuPerc = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$jmx$SigarCpuPerc;
        }
        MBEAN_CONSTR_CPUINDEX_SIGAR = new MBeanConstructorInfo(clsClass$3.getName(), "Creates a new instance for the CPU index specified, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[]{MBEAN_PARAM_SIGAR, MBEAN_PARAM_CPUINDEX});
        if (class$org$hyperic$sigar$jmx$SigarCpuPerc == null) {
            clsClass$4 = class$("org.hyperic.sigar.jmx.SigarCpuPerc");
            class$org$hyperic$sigar$jmx$SigarCpuPerc = clsClass$4;
        } else {
            clsClass$4 = class$org$hyperic$sigar$jmx$SigarCpuPerc;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$4.getName(), "Sigar CPU MBean. Provides percentage data for a single CPU, averaged over the timeframe between the last and the current measurement point. Two measurement points can be as close as 5 seconds, meaning subsequent requests for data within 5 seconds after the last executed call will be satisfied from cached data.", new MBeanAttributeInfo[]{MBEAN_ATTR_CPUINDEX, MBEAN_ATTR_COMBINED, MBEAN_ATTR_IDLE, MBEAN_ATTR_NICE, MBEAN_ATTR_SYS, MBEAN_ATTR_USER, MBEAN_ATTR_WAIT}, new MBeanConstructorInfo[]{MBEAN_CONSTR_CPUINDEX, MBEAN_CONSTR_CPUINDEX_SIGAR}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarCpuPerc(int index) {
        this(new Sigar(), index);
    }

    public SigarCpuPerc(Sigar sigar, int index) {
        super(sigar, (short) 1);
        if (index < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("CPU index has to be non-negative: ").append(index).toString());
        }
        try {
            int cpuCount = sigar.getCpuPercList().length;
            if (cpuCount < index) {
                throw new IllegalArgumentException(new StringBuffer().append("CPU index out of range (found ").append(cpuCount).append(" CPU(s)): ").append(index).toString());
            }
            this.cpuIndex = index;
            this.objectName = new StringBuffer().append("sigar:type=CpuPerc,").append(MBEAN_ATTR_CPUINDEX.getName().substring(0, 1).toLowerCase()).append(MBEAN_ATTR_CPUINDEX.getName().substring(1)).append(SymbolConstants.EQUAL_SYMBOL).append(this.cpuIndex).toString();
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

    public double getCombined() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getCombined();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public double getIdle() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getIdle();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public double getNice() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getNice();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public double getSys() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getSys();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public double getUser() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getUser();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public double getWait() {
        try {
            return this.sigar.getCpuPercList()[this.cpuIndex].getWait();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws AttributeNotFoundException {
        if (MBEAN_ATTR_COMBINED.getName().equals(attr)) {
            return new Double(getCombined());
        }
        if (MBEAN_ATTR_CPUINDEX.getName().equals(attr)) {
            return new Integer(getCpuIndex());
        }
        if (MBEAN_ATTR_IDLE.getName().equals(attr)) {
            return new Double(getIdle());
        }
        if (MBEAN_ATTR_NICE.getName().equals(attr)) {
            return new Double(getNice());
        }
        if (MBEAN_ATTR_SYS.getName().equals(attr)) {
            return new Double(getSys());
        }
        if (MBEAN_ATTR_USER.getName().equals(attr)) {
            return new Double(getUser());
        }
        if (MBEAN_ATTR_WAIT.getName().equals(attr)) {
            return new Double(getWait());
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
