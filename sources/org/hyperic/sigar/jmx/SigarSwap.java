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

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarSwap.class */
public class SigarSwap extends AbstractMBean {
    private static final String MBEAN_TYPE = "Swap";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_FREE = new MBeanAttributeInfo("Free", XmlErrorCodes.LONG, "The amount of free swap memory, in [bytes]", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_TOTAL = new MBeanAttributeInfo("Total", XmlErrorCodes.LONG, "The total amount of swap memory, in [bytes]", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_USED = new MBeanAttributeInfo("Used", XmlErrorCodes.LONG, "The amount of swap memory in use, in [bytes]", true, false, false);
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    static Class class$org$hyperic$sigar$Sigar;
    static Class class$org$hyperic$sigar$jmx$SigarSwap;

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
        if (class$org$hyperic$sigar$jmx$SigarSwap == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarSwap");
            class$org$hyperic$sigar$jmx$SigarSwap = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarSwap;
        }
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(clsClass$2.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data.", new MBeanParameterInfo[]{MBEAN_PARAM_SIGAR});
        if (class$org$hyperic$sigar$jmx$SigarSwap == null) {
            clsClass$3 = class$("org.hyperic.sigar.jmx.SigarSwap");
            class$org$hyperic$sigar$jmx$SigarSwap = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$jmx$SigarSwap;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$3.getName(), "Sigar Swap MBean, provides raw data for the swap memory configured on the system. Uses an internal cache that invalidates within 5 seconds.", new MBeanAttributeInfo[]{MBEAN_ATTR_FREE, MBEAN_ATTR_TOTAL, MBEAN_ATTR_USED}, new MBeanConstructorInfo[]{MBEAN_CONSTR_SIGAR}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarSwap(Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short) 1);
        this.objectName = "sigar:type=Swap";
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public String getObjectName() {
        return this.objectName;
    }

    public long getFree() {
        try {
            return this.sigar.getSwap().getFree();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getTotal() {
        try {
            return this.sigar.getSwap().getTotal();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    public long getUsed() {
        try {
            return this.sigar.getSwap().getUsed();
        } catch (SigarException e) {
            throw unexpectedError(MBEAN_TYPE, e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws MBeanException, AttributeNotFoundException, ReflectionException {
        if (MBEAN_ATTR_FREE.getName().equals(attr)) {
            return new Long(getFree());
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
