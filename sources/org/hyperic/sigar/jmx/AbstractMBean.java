package org.hyperic.sigar.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/AbstractMBean.class */
public abstract class AbstractMBean implements DynamicMBean, MBeanRegistration {
    protected static final String MBEAN_ATTR_TYPE = "type";
    protected static final short CACHED_30SEC = 0;
    protected static final short CACHED_5SEC = 1;
    protected static final short CACHED_500MS = 2;
    protected static final short CACHELESS = 3;
    protected static final short DEFAULT = 0;
    protected final Sigar sigarImpl;
    protected final SigarProxy sigar;
    protected MBeanServer mbeanServer;

    public abstract String getObjectName();

    protected AbstractMBean(Sigar sigar, short cacheMode) {
        this.sigarImpl = sigar;
        if (cacheMode == 3) {
            this.sigar = this.sigarImpl;
            return;
        }
        if (cacheMode == 2) {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 500);
        } else if (cacheMode == 1) {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 5000);
        } else {
            this.sigar = SigarProxyCache.newInstance(this.sigarImpl, 30000);
        }
    }

    protected RuntimeException unexpectedError(String type, SigarException e) {
        String msg = new StringBuffer().append("Unexected error in Sigar.get").append(type).append(": ").append(e.getMessage()).toString();
        return new IllegalArgumentException(msg);
    }

    public AttributeList getAttributes(String[] attrs) {
        AttributeList result = new AttributeList();
        for (int i = 0; i < attrs.length; i++) {
            try {
                result.add(new Attribute(attrs[i], getAttribute(attrs[i])));
            } catch (AttributeNotFoundException e) {
            } catch (ReflectionException e2) {
            } catch (MBeanException e3) {
            }
        }
        return result;
    }

    public AttributeList setAttributes(AttributeList attrs) {
        AttributeList result = new AttributeList();
        for (int i = 0; i < attrs.size(); i++) {
            try {
                Attribute next = (Attribute) attrs.get(i);
                setAttribute(next);
                result.add(next);
            } catch (InvalidAttributeValueException e) {
            } catch (ReflectionException e2) {
            } catch (MBeanException e3) {
            } catch (AttributeNotFoundException e4) {
            }
        }
        return result;
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.mbeanServer = server;
        return new ObjectName(getObjectName());
    }

    public void postRegister(Boolean success) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
        this.mbeanServer = null;
    }
}
