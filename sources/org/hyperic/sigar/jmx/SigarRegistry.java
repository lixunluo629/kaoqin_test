package org.hyperic.sigar.jmx;

import java.util.ArrayList;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarRegistry.class */
public class SigarRegistry extends AbstractMBean {
    private static final String MBEAN_TYPE = "SigarRegistry";
    private static final MBeanInfo MBEAN_INFO;
    private static final MBeanConstructorInfo MBEAN_CONSTR_DEFAULT;
    private final String objectName;
    private final ArrayList managedBeans;
    static Class class$org$hyperic$sigar$jmx$SigarRegistry;

    static {
        Class clsClass$;
        Class clsClass$2;
        if (class$org$hyperic$sigar$jmx$SigarRegistry == null) {
            clsClass$ = class$("org.hyperic.sigar.jmx.SigarRegistry");
            class$org$hyperic$sigar$jmx$SigarRegistry = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$jmx$SigarRegistry;
        }
        MBEAN_CONSTR_DEFAULT = new MBeanConstructorInfo(clsClass$.getName(), "Creates a new instance of this class. Will create the Sigar instance this class uses when constructing other MBeans", new MBeanParameterInfo[0]);
        if (class$org$hyperic$sigar$jmx$SigarRegistry == null) {
            clsClass$2 = class$("org.hyperic.sigar.jmx.SigarRegistry");
            class$org$hyperic$sigar$jmx$SigarRegistry = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$jmx$SigarRegistry;
        }
        MBEAN_INFO = new MBeanInfo(clsClass$2.getName(), "Sigar MBean registry. Provides a central point for creation and destruction of Sigar MBeans. Any Sigar MBean created via this instance will automatically be cleaned up when this instance is deregistered from the MBean server.", (MBeanAttributeInfo[]) null, new MBeanConstructorInfo[]{MBEAN_CONSTR_DEFAULT}, (MBeanOperationInfo[]) null, (MBeanNotificationInfo[]) null);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarRegistry() {
        super(new Sigar(), (short) 3);
        this.objectName = "sigar:type=SigarRegistry";
        this.managedBeans = new ArrayList();
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public String getObjectName() {
        return this.objectName;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public void setAttribute(Attribute attr) throws AttributeNotFoundException {
        throw new AttributeNotFoundException(attr.getName());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.ReflectionException */
    public Object invoke(String action, Object[] params, String[] signatures) throws MBeanException, ReflectionException {
        throw new ReflectionException(new NoSuchMethodException(action), action);
    }

    public MBeanInfo getMBeanInfo() {
        return MBEAN_INFO;
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public void postRegister(Boolean success) {
        super.postRegister(success);
        if (!success.booleanValue()) {
            return;
        }
        registerCpuBeans();
        registerMemoryBeans();
        registerSystemBeans();
    }

    private void registerCpuBeans() {
        ObjectInstance nextRegistered = null;
        try {
            int cpuCount = this.sigar.getCpuInfoList().length;
            for (int i = 0; i < cpuCount; i++) {
                SigarCpu nextCpu = new SigarCpu(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpu.getObjectName()))) {
                        nextRegistered = this.mbeanServer.registerMBean(nextCpu, (ObjectName) null);
                    }
                } catch (Exception e) {
                }
                if (nextRegistered != null) {
                    this.managedBeans.add(nextRegistered.getObjectName());
                }
                ObjectInstance nextRegistered2 = null;
                SigarCpuPerc nextCpuPerc = new SigarCpuPerc(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpuPerc.getObjectName()))) {
                        nextRegistered2 = this.mbeanServer.registerMBean(nextCpuPerc, (ObjectName) null);
                    }
                } catch (Exception e2) {
                }
                if (nextRegistered2 != null) {
                    this.managedBeans.add(nextRegistered2.getObjectName());
                }
                ObjectInstance nextRegistered3 = null;
                SigarCpuInfo nextCpuInfo = new SigarCpuInfo(this.sigarImpl, i);
                try {
                    if (!this.mbeanServer.isRegistered(new ObjectName(nextCpuInfo.getObjectName()))) {
                        nextRegistered3 = this.mbeanServer.registerMBean(nextCpuInfo, (ObjectName) null);
                    }
                } catch (Exception e3) {
                }
                if (nextRegistered3 != null) {
                    this.managedBeans.add(nextRegistered3.getObjectName());
                }
                nextRegistered = null;
            }
        } catch (SigarException e4) {
            throw unexpectedError("CpuInfoList", e4);
        }
    }

    private void registerMemoryBeans() {
        ObjectInstance nextRegistered = null;
        SigarMem mem = new SigarMem(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(mem.getObjectName()))) {
                nextRegistered = this.mbeanServer.registerMBean(mem, (ObjectName) null);
            }
        } catch (Exception e) {
        }
        if (nextRegistered != null) {
            this.managedBeans.add(nextRegistered.getObjectName());
        }
        ObjectInstance nextRegistered2 = null;
        SigarSwap swap = new SigarSwap(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(swap.getObjectName()))) {
                nextRegistered2 = this.mbeanServer.registerMBean(swap, (ObjectName) null);
            }
        } catch (Exception e2) {
            nextRegistered2 = null;
        }
        if (nextRegistered2 != null) {
            this.managedBeans.add(nextRegistered2.getObjectName());
        }
    }

    private void registerSystemBeans() {
        ObjectInstance nextRegistered = null;
        SigarLoadAverage loadAvg = new SigarLoadAverage(this.sigarImpl);
        try {
            if (!this.mbeanServer.isRegistered(new ObjectName(loadAvg.getObjectName()))) {
                nextRegistered = this.mbeanServer.registerMBean(loadAvg, (ObjectName) null);
            }
        } catch (Exception e) {
        }
        if (nextRegistered != null) {
            this.managedBeans.add(nextRegistered.getObjectName());
        }
    }

    @Override // org.hyperic.sigar.jmx.AbstractMBean
    public void preDeregister() throws Exception {
        for (int i = this.managedBeans.size() - 1; i >= 0; i--) {
            ObjectName next = (ObjectName) this.managedBeans.remove(i);
            if (this.mbeanServer.isRegistered(next)) {
                try {
                    this.mbeanServer.unregisterMBean(next);
                } catch (Exception e) {
                }
            }
        }
        super.preDeregister();
    }
}
