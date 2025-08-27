package org.hyperic.sigar.jmx;

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
import org.hyperic.sigar.SigarNotImplementedException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarLoadAverage.class */
public class SigarLoadAverage extends AbstractMBean {
    private static final String MBEAN_TYPE = "LoadAverage";
    private static final double NOT_IMPLEMENTED_LOAD_VALUE = -1.0d;
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST1MIN = new MBeanAttributeInfo("LastMinute", XmlErrorCodes.DOUBLE, "The load average in the last minute, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST5MIN = new MBeanAttributeInfo("LastFiveMinutes", XmlErrorCodes.DOUBLE, "The load average over the last five minutes, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
    private static final MBeanAttributeInfo MBEAN_ATTR_LAST15MIN = new MBeanAttributeInfo("Last15Minutes", XmlErrorCodes.DOUBLE, "The load average over the last 15 minutes, as a fraction of 1, or -1.0 if the load cannot be determined on this platform", true, false, false);
    private static final MBeanConstructorInfo MBEAN_CONSTR_SIGAR;
    private static MBeanParameterInfo MBEAN_PARAM_SIGAR;
    private final String objectName;
    private boolean notImplemented;
    static Class class$org$hyperic$sigar$Sigar;
    static Class class$org$hyperic$sigar$jmx$SigarLoadAverage;

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
        if (class$org$hyperic$sigar$jmx$SigarLoadAverage == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarLoadAverage");
            class$org$hyperic$sigar$jmx$SigarLoadAverage = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarLoadAverage;
        }
        MBEAN_CONSTR_SIGAR = new MBeanConstructorInfo(clsClass$2.getName(), "Creates a new instance, using the Sigar instance specified to fetch the data. Fails if the CPU index is out of range.", new MBeanParameterInfo[]{MBEAN_PARAM_SIGAR});
        if (class$org$hyperic$sigar$jmx$SigarLoadAverage == null) {
            clsClass$3 = class$("org.hyperic.sigar.jmx.SigarLoadAverage");
            class$org$hyperic$sigar$jmx$SigarLoadAverage = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$jmx$SigarLoadAverage;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$3.getName(), "Sigar load average MBean. Provides load averages of the system over the last one, five and 15 minutes. Due to the long term character of that information, the fetch is done using a Sigar proxy cache with a timeout of 30 seconds.", new MBeanAttributeInfo[]{MBEAN_ATTR_LAST1MIN, MBEAN_ATTR_LAST5MIN, MBEAN_ATTR_LAST15MIN}, new MBeanConstructorInfo[]{MBEAN_CONSTR_SIGAR}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarLoadAverage() throws IllegalArgumentException {
        this(new Sigar());
    }

    public SigarLoadAverage(Sigar sigar) throws IllegalArgumentException {
        super(sigar, (short) 0);
        this.objectName = "sigar:type=LoadAverage";
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public String getObjectName() {
        return this.objectName;
    }

    public double getLastMinute() {
        try {
            return this.sigarImpl.getLoadAverage()[0];
        } catch (SigarNotImplementedException e) {
            return NOT_IMPLEMENTED_LOAD_VALUE;
        } catch (SigarException e2) {
            throw unexpectedError(MBEAN_TYPE, e2);
        }
    }

    public double getLastFiveMinutes() {
        try {
            return this.sigarImpl.getLoadAverage()[1];
        } catch (SigarNotImplementedException e) {
            return NOT_IMPLEMENTED_LOAD_VALUE;
        } catch (SigarException e2) {
            throw unexpectedError(MBEAN_TYPE, e2);
        }
    }

    public double getLast15Minutes() {
        try {
            return this.sigarImpl.getLoadAverage()[2];
        } catch (SigarNotImplementedException e) {
            return NOT_IMPLEMENTED_LOAD_VALUE;
        } catch (SigarException e2) {
            throw unexpectedError(MBEAN_TYPE, e2);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws AttributeNotFoundException {
        if (MBEAN_ATTR_LAST1MIN.getName().equals(attr)) {
            return new Double(getLastMinute());
        }
        if (MBEAN_ATTR_LAST5MIN.getName().equals(attr)) {
            return new Double(getLastFiveMinutes());
        }
        if (MBEAN_ATTR_LAST15MIN.getName().equals(attr)) {
            return new Double(getLast15Minutes());
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
