package org.hyperic.sigar.test;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Properties;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;
import org.apache.log4j.PropertyConfigurator;
import org.hyperic.sigar.cmd.Version;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/SigarTestPrinter.class */
public class SigarTestPrinter extends ResultPrinter {
    private HashMap failures;
    private int maxNameLen;
    private static boolean printedVersion;
    private static final String PREFIX = "org.hyperic.sigar.test.";
    private static final String[][] LOG_PROPS = {new String[]{"log4j.rootLogger", "DEBUG, R"}, new String[]{"log4j.appender.R", "org.apache.log4j.ConsoleAppender"}, new String[]{"log4j.appender.R.layout", "org.apache.log4j.PatternLayout"}, new String[]{"log4j.appender.R.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n"}};

    public SigarTestPrinter(PrintStream writer) {
        super(writer);
        this.failures = new HashMap();
        this.maxNameLen = 0;
    }

    @Override // junit.textui.ResultPrinter, junit.framework.TestListener
    public void startTest(Test test) {
        PrintStream writer = getWriter();
        String cls = test.getClass().getName();
        String cls2 = cls.substring(PREFIX.length());
        String method = ((TestCase) test).getName();
        String name = new StringBuffer().append(cls2).append(".").append(method).toString();
        writer.print(name);
        int n = (this.maxNameLen + 3) - name.length();
        for (int i = 0; i < n; i++) {
            writer.print('.');
        }
    }

    @Override // junit.textui.ResultPrinter, junit.framework.TestListener
    public void addFailure(Test test, AssertionFailedError t) {
        this.failures.put(test, Boolean.TRUE);
        getWriter().println("FAILED");
    }

    @Override // junit.textui.ResultPrinter, junit.framework.TestListener
    public void addError(Test test, Throwable t) {
        this.failures.put(test, Boolean.TRUE);
        getWriter().println("ERROR");
    }

    @Override // junit.textui.ResultPrinter, junit.framework.TestListener
    public void endTest(Test test) {
        if (this.failures.get(test) != Boolean.TRUE) {
            getWriter().println("ok");
        }
    }

    @Override // junit.textui.ResultPrinter
    protected void printDefectHeader(TestFailure failure, int count) {
        getWriter().println(new StringBuffer().append(count).append(") ").append(failure.failedTest().getClass().getName()).append(":").toString());
    }

    public void printVersionInfo() throws Throwable {
        if (printedVersion) {
            return;
        }
        printedVersion = true;
        PrintStream writer = getWriter();
        Version.printInfo(writer);
        writer.println("");
    }

    public static void addTest(SigarTestPrinter printer, TestSuite suite, Class test) {
        int len = test.getName().length();
        if (len > printer.maxNameLen) {
            printer.maxNameLen = len;
        }
        suite.addTestSuite(test);
    }

    private static Class findTest(Class[] tests, String name) {
        String tname = new StringBuffer().append("Test").append(name).toString();
        for (int i = 0; i < tests.length; i++) {
            if (tests[i].getName().endsWith(tname)) {
                return tests[i];
            }
        }
        return null;
    }

    public static void runTests(Class[] tests, String[] args) throws Throwable {
        TestSuite suite = new TestSuite("Sigar tests");
        SigarTestPrinter printer = new SigarTestPrinter(System.out);
        printer.printVersionInfo();
        if (args.length > 0) {
            Properties props = new Properties();
            for (int i = 0; i < LOG_PROPS.length; i++) {
                props.setProperty(LOG_PROPS[i][0], LOG_PROPS[i][1]);
            }
            PropertyConfigurator.configure(props);
            SigarTestCase.setVerbose(true);
            SigarTestCase.setWriter(printer.getWriter());
            for (int i2 = 0; i2 < args.length; i2++) {
                Class test = findTest(tests, args[i2]);
                if (test == null) {
                    String msg = new StringBuffer().append("Invalid test: ").append(args[i2]).toString();
                    throw new IllegalArgumentException(msg);
                }
                addTest(printer, suite, test);
            }
        } else {
            for (Class cls : tests) {
                addTest(printer, suite, cls);
            }
        }
        TestRunner runner = new TestRunner(printer);
        runner.doRun(suite);
    }
}
