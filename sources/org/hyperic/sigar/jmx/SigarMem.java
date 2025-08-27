package org.hyperic.sigar.jmx;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import org.apache.xmlbeans.XmlErrorCodes;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarMem.class */
public class SigarMem extends AbstractMBean {
    private static final String MBEAN_TYPE = "Mem";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_ACTUAL_FREE = new MBeanAttributeInfo("ActualFree", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_ACTUAL_USED = new MBeanAttributeInfo("ActualUsed", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_FREE = new MBeanAttributeInfo("Free", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_RAM = new MBeanAttributeInfo("Ram", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_USED = new MBeanAttributeInfo("Used", XmlErrorCodes.LONG, "TODO add proper description here", true, false, false);
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    static Class class$org$hyperic$sigar$Sigar;
    static Class class$org$hyperic$sigar$jmx$SigarMem;

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (class$org$hyperic$sigar$Sigar == null) {
            clsClass$ = class$("org.hyperic.sigar.Sigar");
            class$org$hyperic$sigar$Sigar = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$Sigar;
        }
        MBEAN_PARAM_SIGAR = new MBeanParameterInfo(SigarInvokerJMX.DOMAIN_NAME, clsClass$.getName(), "The Sigar instance to use to fetch data from");
        if (class$org$hyperic$sigar$jmx$SigarMem == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarMem");
            class$org$hyperic$sigar$jmx$SigarMem = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarMem;
        }
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(clsClass$2.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data.", new MBeanParameterInfo[]{MBEAN_PARAM_SIGAR});
        if (class$org$hyperic$sigar$jmx$SigarMem == null) {
            clsClass$3 = class$("org.hyperic.sigar.jmx.SigarMem");
            class$org$hyperic$sigar$jmx$SigarMem = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$jmx$SigarMem;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$3.getName(), "Sigar Memory MBean, provides raw data for the physical memory installed on the system. Uses an internal cache that invalidates within 500ms, allowing for bulk request being satisfied with a single dataset fetch.", new MBeanAttributeInfo[]{MBEAN_ATTR_ACTUAL_FREE, MBEAN_ATTR_ACTUAL_USED, MBEAN_ATTR_FREE, MBEAN_ATTR_RAM, MBEAN_ATTR_TOTAL, MBEAN_ATTR_USED}, new MBeanConstructorInfo[]{MBEAN_CONSTR_SIGAR}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarMem(Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short) 2);
        this.objectName = "sigar:type=Memory";
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public String getObjectName() {
        return this.objectName;
    }

    public long getActualFree() {
        try {
            return this.sigar.getMem().getActualFree();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getActualUsed() {
        try {
            return this.sigar.getMem().getActualUsed();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getFree() {
        try {
            return this.sigar.getMem().getFree();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getRam() {
        try {
            return this.sigar.getMem().getRam();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getTotal() {
        try {
            return this.sigar.getMem().getTotal();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getUsed() {
        try {
            return this.sigar.getMem().getUsed();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws MBeanException, AttributeNotFoundException, ReflectionException {
        if (MBEAN_ATTR_ACTUAL_FREE.getName().equals(attr)) {
            return new Long(getActualFree());
        }
        if (MBEAN_ATTR_ACTUAL_USED.getName().equals(attr)) {
            return new Long(getActualUsed());
        }
        if (MBEAN_ATTR_FREE.getName().equals(attr)) {
            return new Long(getFree());
        }
        if (MBEAN_ATTR_RAM.getName().equals(attr)) {
            return new Long(getRam());
        }
        if (MBEAN_ATTR_TOTAL.getName().equals(attr)) {
            return new Long(getTotal());
        }
        if (MBEAN_ATTR_USED.getName().equals(attr)) {
            return new Long(getUsed());
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
