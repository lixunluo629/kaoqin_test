package org.apache.catalina.mbeans;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Array;
import java.util.Set;
import javax.management.JMRuntimeException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/mbeans/MBeanDumper.class */
public class MBeanDumper {
    private static final Log log = LogFactory.getLog((Class<?>) MBeanDumper.class);
    private static final String CRLF = "\r\n";

    public static String dumpBeans(MBeanServer mbeanServer, Set<ObjectName> names) {
        String valueString;
        StringBuilder buf = new StringBuilder();
        for (ObjectName oname : names) {
            buf.append("Name: ");
            buf.append(oname.toString());
            buf.append("\r\n");
            try {
                MBeanInfo minfo = mbeanServer.getMBeanInfo(oname);
                String code = minfo.getClassName();
                if ("org.apache.commons.modeler.BaseModelMBean".equals(code)) {
                    code = (String) mbeanServer.getAttribute(oname, "modelerType");
                }
                buf.append("modelerType: ");
                buf.append(code);
                buf.append("\r\n");
                MBeanAttributeInfo[] attrs = minfo.getAttributes();
                for (int i = 0; i < attrs.length; i++) {
                    if (attrs[i].isReadable()) {
                        String attName = attrs[i].getName();
                        if (!"modelerType".equals(attName) && attName.indexOf(61) < 0 && attName.indexOf(58) < 0 && attName.indexOf(32) < 0) {
                            try {
                                Object value = mbeanServer.getAttribute(oname, attName);
                                if (value != null) {
                                    try {
                                        Class<?> c = value.getClass();
                                        if (c.isArray()) {
                                            int len = Array.getLength(value);
                                            StringBuilder sb = new StringBuilder("Array[" + c.getComponentType().getName() + "] of length " + len);
                                            if (len > 0) {
                                                sb.append("\r\n");
                                            }
                                            for (int j = 0; j < len; j++) {
                                                sb.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                                                Object item = Array.get(value, j);
                                                if (item == null) {
                                                    sb.append("NULL VALUE");
                                                } else {
                                                    try {
                                                        sb.append(escape(item.toString()));
                                                    } catch (Throwable t) {
                                                        ExceptionUtils.handleThrowable(t);
                                                        sb.append("NON-STRINGABLE VALUE");
                                                    }
                                                }
                                                if (j < len - 1) {
                                                    sb.append("\r\n");
                                                }
                                            }
                                            valueString = sb.toString();
                                        } else {
                                            valueString = escape(value.toString());
                                        }
                                        buf.append(attName);
                                        buf.append(": ");
                                        buf.append(valueString);
                                        buf.append("\r\n");
                                    } catch (Throwable t2) {
                                        ExceptionUtils.handleThrowable(t2);
                                    }
                                }
                            } catch (JMRuntimeException e) {
                                Throwable cause = e.getCause();
                                if (cause instanceof UnsupportedOperationException) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Error getting attribute " + oname + SymbolConstants.SPACE_SYMBOL + attName, e);
                                    }
                                } else if (cause instanceof NullPointerException) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Error getting attribute " + oname + SymbolConstants.SPACE_SYMBOL + attName, e);
                                    }
                                } else {
                                    log.error("Error getting attribute " + oname + SymbolConstants.SPACE_SYMBOL + attName, e);
                                }
                            } catch (Throwable t3) {
                                ExceptionUtils.handleThrowable(t3);
                                log.error("Error getting attribute " + oname + SymbolConstants.SPACE_SYMBOL + attName, t3);
                            }
                        }
                    }
                }
            } catch (Throwable t4) {
                ExceptionUtils.handleThrowable(t4);
            }
            buf.append("\r\n");
        }
        return buf.toString();
    }

    public static String escape(String value) {
        int idx = value.indexOf(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (idx < 0) {
            return value;
        }
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        while (idx >= 0) {
            appendHead(sb, value, prev, idx);
            sb.append("\\n\n ");
            prev = idx + 1;
            if (idx == value.length() - 1) {
                break;
            }
            idx = value.indexOf(10, idx + 1);
        }
        if (prev < value.length()) {
            appendHead(sb, value, prev, value.length());
        }
        return sb.toString();
    }

    private static void appendHead(StringBuilder sb, String value, int start, int end) {
        if (end < 1) {
            return;
        }
        int i = start;
        while (true) {
            int pos = i;
            if (end - pos > 78) {
                sb.append(value.substring(pos, pos + 78));
                sb.append("\n ");
                i = pos + 78;
            } else {
                sb.append(value.substring(pos, end));
                return;
            }
        }
    }
}
