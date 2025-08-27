package org.hyperic.sigar.shell;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.hyperic.sigar.util.GetlineCompleter;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ProcessQueryCompleter.class */
public class ProcessQueryCompleter implements GetlineCompleter {
    private static final String SIGAR_PACKAGE = "org.hyperic.sigar.";
    private static final Map METHODS = new HashMap();
    private static final Collection NOPS = Arrays.asList("eq", "ne", "gt", "ge", "lt", "le");
    private static final Collection SOPS = Arrays.asList("eq", "ne", "re", "ct", "ew", "sw");
    private static final Class[] NOPARAM = new Class[0];
    private static final String PROC_PREFIX = "getProc";
    private ShellBase shell;
    private GetlineCompleter m_completer;
    private Map methods = getMethods();
    static Class class$org$hyperic$sigar$SigarProxy;

    static {
        Class clsClass$;
        if (class$org$hyperic$sigar$SigarProxy == null) {
            clsClass$ = class$("org.hyperic.sigar.SigarProxy");
            class$org$hyperic$sigar$SigarProxy = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$SigarProxy;
        }
        Method[] methods = clsClass$.getMethods();
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if (name.startsWith(PROC_PREFIX)) {
                Class[] params = methods[i].getParameterTypes();
                if (params.length == 1 && params[0] == Long.TYPE) {
                    METHODS.put(name.substring(PROC_PREFIX.length()), methods[i]);
                }
            }
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public ProcessQueryCompleter(ShellBase shell) {
        this.shell = shell;
        this.m_completer = new CollectionCompleter(shell, this.methods.keySet());
    }

    public static Map getMethods() {
        return METHODS;
    }

    public static Collection getMethodOpNames(Method method) {
        if (method == null) {
            return SOPS;
        }
        Class rtype = method.getReturnType();
        if (rtype == Character.TYPE || rtype == Double.TYPE || rtype == Integer.TYPE || rtype == Long.TYPE) {
            return NOPS;
        }
        return SOPS;
    }

    public static boolean isSigarClass(Class type) {
        return type.getName().startsWith(SIGAR_PACKAGE);
    }

    @Override // org.hyperic.sigar.util.GetlineCompleter
    public String complete(String line) throws NoSuchMethodException, SecurityException {
        int ix = line.indexOf(".");
        if (ix == -1) {
            String line2 = this.m_completer.complete(line);
            if (!line2.endsWith(".") && this.methods.get(line2) != null) {
                return new StringBuffer().append(line2).append(".").toString();
            }
            return line2;
        }
        String attrClass = line.substring(0, ix);
        String attr = line.substring(ix + 1, line.length());
        Method method = (Method) this.methods.get(attrClass);
        if (method == null) {
            return line;
        }
        Class subtype = method.getReturnType();
        boolean isSigarClass = isSigarClass(subtype);
        int ix2 = attr.indexOf(".");
        if (ix2 != -1) {
            Method method2 = null;
            String op = attr.substring(ix2 + 1, attr.length());
            String attr2 = attr.substring(0, ix2);
            if (isSigarClass) {
                try {
                    method2 = subtype.getMethod(new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(attr2).toString(), NOPARAM);
                } catch (NoSuchMethodException e) {
                }
            }
            GetlineCompleter completer = new CollectionCompleter(this.shell, getMethodOpNames(method2));
            String partial = completer.complete(op);
            String result = new StringBuffer().append(attrClass).append(".").append(attr2).append(".").append(partial).toString();
            if (partial.length() == 2) {
                result = new StringBuffer().append(result).append(SymbolConstants.EQUAL_SYMBOL).toString();
            }
            return result;
        }
        if (isSigarClass) {
            ArrayList possible = new ArrayList();
            Method[] submethods = subtype.getDeclaredMethods();
            for (Method m : submethods) {
                if (m.getName().startsWith(BeanUtil.PREFIX_GETTER_GET)) {
                    possible.add(m.getName().substring(3));
                }
            }
            GetlineCompleter completer2 = new CollectionCompleter(this.shell, possible);
            String partial2 = completer2.complete(attr);
            String result2 = new StringBuffer().append(attrClass).append(".").append(partial2).toString();
            if (possible.contains(partial2)) {
                result2 = new StringBuffer().append(result2).append(".").toString();
            }
            return result2;
        }
        return line;
    }
}
