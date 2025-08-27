package org.hyperic.sigar.test;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Properties;
import junit.framework.TestCase;
import org.apache.xmlbeans.XmlOptions;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessQueryFactory;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/SigarTestCase.class */
public abstract class SigarTestCase extends TestCase {
    private Properties props;
    protected static final boolean JDK_14_COMPAT;
    private static PrintStream out;
    private static Sigar sigar = null;
    private static boolean verbose = "true".equals(System.getProperty("sigar.testVerbose"));

    static {
        JDK_14_COMPAT = System.getProperty("java.specification.version").compareTo(XmlOptions.GENERATE_JAVA_14) >= 0;
        out = System.out;
    }

    public SigarTestCase(String name) throws IOException {
        super(name);
        this.props = new Properties();
        File f = new File(System.getProperty("user.home"), ".sigar.properties");
        if (f.exists()) {
            FileInputStream is = null;
            try {
                try {
                    is = new FileInputStream(f);
                    this.props.load(is);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e3) {
                        }
                    }
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        }
    }

    public Sigar getSigar() {
        if (sigar == null) {
            sigar = new Sigar();
            if (getVerbose()) {
                sigar.enableLogging(true);
            }
        }
        return sigar;
    }

    public static void closeSigar() {
        if (sigar != null) {
            sigar.close();
            sigar = null;
        }
        ProcessQueryFactory.getInstance().clear();
        Humidor.getInstance().close();
    }

    public Properties getProperties() {
        return this.props;
    }

    public String getProperty(String key, String val) {
        return getProperties().getProperty(key, val);
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public static void setVerbose(boolean value) {
        verbose = value;
    }

    public static boolean getVerbose() {
        return verbose;
    }

    public static void setWriter(PrintStream value) {
        out = value;
    }

    public static PrintStream getWriter() {
        return out;
    }

    public long getInvalidPid() {
        return 666666L;
    }

    public void traceln(String msg) {
        if (getVerbose()) {
            getWriter().println(msg);
        }
    }

    public void trace(String msg) {
        if (getVerbose()) {
            getWriter().print(msg);
        }
    }

    public void assertTrueTrace(String msg, String value) {
        traceln(new StringBuffer().append(msg).append(SymbolConstants.EQUAL_SYMBOL).append(value).toString());
        assertTrue(msg, value != null);
    }

    public void assertLengthTrace(String msg, String value) {
        assertTrueTrace(msg, value);
        assertTrue(msg, value.length() > 0);
    }

    public void assertIndexOfTrace(String msg, String value, String substr) {
        assertTrueTrace(msg, value);
        assertTrue(msg, value.indexOf(substr) != -1);
    }

    public void assertGtZeroTrace(String msg, long value) {
        traceln(new StringBuffer().append(msg).append(SymbolConstants.EQUAL_SYMBOL).append(value).toString());
        assertTrue(msg, value > 0);
    }

    public void assertGtEqZeroTrace(String msg, long value) {
        traceln(new StringBuffer().append(msg).append(SymbolConstants.EQUAL_SYMBOL).append(value).toString());
        assertTrue(msg, value >= 0);
    }

    public void assertValidFieldTrace(String msg, long value) {
        if (value != -1) {
            assertGtEqZeroTrace(msg, value);
        }
    }

    public void assertEqualsTrace(String msg, long expected, long actual) {
        traceln(new StringBuffer().append(msg).append(SymbolConstants.EQUAL_SYMBOL).append(actual).append("/").append(expected).toString());
        assertEquals(msg, expected, actual);
    }

    public void traceMethods(Object obj) throws Exception {
        Class cls = obj.getClass();
        Method[] methods = cls.getDeclaredMethods();
        traceln("");
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if (name.startsWith(BeanUtil.PREFIX_GETTER_GET)) {
                Object val = methods[i].invoke(obj, new Object[0]);
                if ((val instanceof Long) && ((Long) val).longValue() == -1) {
                    val = "NOTIMPL";
                }
                traceln(new StringBuffer().append(name).append(SymbolConstants.EQUAL_SYMBOL).append(val).toString());
            }
        }
    }
}
