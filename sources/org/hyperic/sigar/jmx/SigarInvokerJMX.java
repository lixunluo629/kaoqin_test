package org.hyperic.sigar.jmx;

import java.util.Map;
import java.util.StringTokenizer;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarInvoker;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.util.ReferenceMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/jmx/SigarInvokerJMX.class */
public class SigarInvokerJMX extends SigarInvoker {
    public static final String DOMAIN_NAME = "sigar";
    public static final String PROP_TYPE = "Type";
    public static final String PROP_ARG = "Arg";
    private String arg = null;
    private static Map cache = ReferenceMap.synchronizedMap();

    public static SigarInvokerJMX getInstance(SigarProxy proxy, String name) {
        int ix = name.indexOf(":");
        if (ix > 0) {
            name = name.substring(ix + 1);
        }
        SigarInvokerJMX invoker = (SigarInvokerJMX) cache.get(name);
        if (invoker != null) {
            invoker.setProxy(proxy);
            return invoker;
        }
        SigarInvokerJMX invoker2 = new SigarInvokerJMX();
        invoker2.setProxy(proxy);
        StringTokenizer st = new StringTokenizer(name, ",");
        while (st.hasMoreTokens()) {
            String attr = st.nextToken();
            String key = attr.substring(0, attr.indexOf(61));
            String val = attr.substring(key.length() + 1);
            if (key.equals("Type")) {
                invoker2.setType(val);
            } else if (key.equals(PROP_ARG)) {
                invoker2.setArg(decode(val));
            }
        }
        cache.put(name, invoker2);
        return invoker2;
    }

    public static String decode(String val) {
        char c;
        StringBuffer buf = new StringBuffer(val.length());
        boolean changed = false;
        int len = val.length();
        int i = 0;
        while (i < len) {
            char c2 = val.charAt(i);
            if (c2 == '%') {
                if (i + 2 > len) {
                    break;
                }
                String s = val.substring(i + 1, i + 3);
                if (s.equals("3A")) {
                    c = ':';
                } else if (s.equals("3D")) {
                    c = '=';
                } else if (s.equals("2C")) {
                    c = ',';
                } else {
                    buf.append(c2);
                    i++;
                }
                char d = c;
                changed = true;
                buf.append(d);
                i += 3;
            } else {
                buf.append(c2);
                i++;
            }
        }
        return changed ? buf.toString() : val;
    }

    private void setArg(String val) {
        this.arg = val;
    }

    public String getArg() {
        return this.arg;
    }

    public static String getObjectName(String type, String arg) {
        String s = new StringBuffer().append("sigar:").append("Type=").append(type).toString();
        if (arg != null) {
            s = new StringBuffer().append(s).append(",Arg=").append(arg).toString();
        }
        return s;
    }

    public String getObjectName() {
        return getObjectName(getType(), getArg());
    }

    public String toString() {
        return getObjectName();
    }

    public Object invoke(String attr) throws SigarException {
        return super.invoke(getArg(), attr);
    }
}
