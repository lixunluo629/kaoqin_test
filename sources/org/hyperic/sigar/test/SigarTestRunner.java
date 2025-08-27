package org.hyperic.sigar.test;

import java.util.ArrayList;
import java.util.Collection;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.cmd.Shell;
import org.hyperic.sigar.cmd.SigarCommandBase;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/SigarTestRunner.class */
public class SigarTestRunner extends SigarCommandBase {
    private Collection completions;
    private static final Class[] TESTS;
    private static final Class[] ALL_TESTS;
    private static final Class[] WIN32_TESTS;
    static Class class$org$hyperic$sigar$test$TestLog;
    static Class class$org$hyperic$sigar$test$TestInvoker;
    static Class class$org$hyperic$sigar$test$TestPTQL;
    static Class class$org$hyperic$sigar$test$TestCpu;
    static Class class$org$hyperic$sigar$test$TestCpuInfo;
    static Class class$org$hyperic$sigar$test$TestFileInfo;
    static Class class$org$hyperic$sigar$test$TestFileSystem;
    static Class class$org$hyperic$sigar$test$TestFQDN;
    static Class class$org$hyperic$sigar$test$TestLoadAverage;
    static Class class$org$hyperic$sigar$test$TestMem;
    static Class class$org$hyperic$sigar$test$TestNetIf;
    static Class class$org$hyperic$sigar$test$TestNetInfo;
    static Class class$org$hyperic$sigar$test$TestNetRoute;
    static Class class$org$hyperic$sigar$test$TestNetStat;
    static Class class$org$hyperic$sigar$test$TestNetStatPort;
    static Class class$org$hyperic$sigar$test$TestTcpStat;
    static Class class$org$hyperic$sigar$test$TestNfsClientV2;
    static Class class$org$hyperic$sigar$test$TestNfsServerV2;
    static Class class$org$hyperic$sigar$test$TestNfsClientV3;
    static Class class$org$hyperic$sigar$test$TestNfsServerV3;
    static Class class$org$hyperic$sigar$test$TestProcArgs;
    static Class class$org$hyperic$sigar$test$TestProcEnv;
    static Class class$org$hyperic$sigar$test$TestProcExe;
    static Class class$org$hyperic$sigar$test$TestProcModules;
    static Class class$org$hyperic$sigar$test$TestProcFd;
    static Class class$org$hyperic$sigar$test$TestProcList;
    static Class class$org$hyperic$sigar$test$TestProcMem;
    static Class class$org$hyperic$sigar$test$TestProcState;
    static Class class$org$hyperic$sigar$test$TestProcStat;
    static Class class$org$hyperic$sigar$test$TestProcTime;
    static Class class$org$hyperic$sigar$test$TestResourceLimit;
    static Class class$org$hyperic$sigar$test$TestSignal;
    static Class class$org$hyperic$sigar$test$TestSwap;
    static Class class$org$hyperic$sigar$test$TestThreadCpu;
    static Class class$org$hyperic$sigar$test$TestUptime;
    static Class class$org$hyperic$sigar$test$TestVMware;
    static Class class$org$hyperic$sigar$test$TestWho;
    static Class class$org$hyperic$sigar$test$TestHumidor;
    static Class class$org$hyperic$sigar$win32$test$TestEventLog;
    static Class class$org$hyperic$sigar$win32$test$TestLocaleInfo;
    static Class class$org$hyperic$sigar$win32$test$TestPdh;
    static Class class$org$hyperic$sigar$win32$test$TestMetaBase;
    static Class class$org$hyperic$sigar$win32$test$TestRegistryKey;
    static Class class$org$hyperic$sigar$win32$test$TestService;
    static Class class$org$hyperic$sigar$win32$test$TestFileVersion;

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        Class clsClass$14;
        Class clsClass$15;
        Class clsClass$16;
        Class clsClass$17;
        Class clsClass$18;
        Class clsClass$19;
        Class clsClass$20;
        Class clsClass$21;
        Class clsClass$22;
        Class clsClass$23;
        Class clsClass$24;
        Class clsClass$25;
        Class clsClass$26;
        Class clsClass$27;
        Class clsClass$28;
        Class clsClass$29;
        Class clsClass$30;
        Class clsClass$31;
        Class clsClass$32;
        Class clsClass$33;
        Class clsClass$34;
        Class clsClass$35;
        Class clsClass$36;
        Class clsClass$37;
        Class clsClass$38;
        Class clsClass$39;
        Class clsClass$40;
        Class clsClass$41;
        Class clsClass$42;
        Class clsClass$43;
        Class clsClass$44;
        Class clsClass$45;
        Class[] clsArr = new Class[38];
        if (class$org$hyperic$sigar$test$TestLog == null) {
            clsClass$ = class$("org.hyperic.sigar.test.TestLog");
            class$org$hyperic$sigar$test$TestLog = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$test$TestLog;
        }
        clsArr[0] = clsClass$;
        if (class$org$hyperic$sigar$test$TestInvoker == null) {
            clsClass$2 = class$("org.hyperic.sigar.test.TestInvoker");
            class$org$hyperic$sigar$test$TestInvoker = clsClass$2;
        } else {
            clsClass$2 = class$org$hyperic$sigar$test$TestInvoker;
        }
        clsArr[1] = clsClass$2;
        if (class$org$hyperic$sigar$test$TestPTQL == null) {
            clsClass$3 = class$("org.hyperic.sigar.test.TestPTQL");
            class$org$hyperic$sigar$test$TestPTQL = clsClass$3;
        } else {
            clsClass$3 = class$org$hyperic$sigar$test$TestPTQL;
        }
        clsArr[2] = clsClass$3;
        if (class$org$hyperic$sigar$test$TestCpu == null) {
            clsClass$4 = class$("org.hyperic.sigar.test.TestCpu");
            class$org$hyperic$sigar$test$TestCpu = clsClass$4;
        } else {
            clsClass$4 = class$org$hyperic$sigar$test$TestCpu;
        }
        clsArr[3] = clsClass$4;
        if (class$org$hyperic$sigar$test$TestCpuInfo == null) {
            clsClass$5 = class$("org.hyperic.sigar.test.TestCpuInfo");
            class$org$hyperic$sigar$test$TestCpuInfo = clsClass$5;
        } else {
            clsClass$5 = class$org$hyperic$sigar$test$TestCpuInfo;
        }
        clsArr[4] = clsClass$5;
        if (class$org$hyperic$sigar$test$TestFileInfo == null) {
            clsClass$6 = class$("org.hyperic.sigar.test.TestFileInfo");
            class$org$hyperic$sigar$test$TestFileInfo = clsClass$6;
        } else {
            clsClass$6 = class$org$hyperic$sigar$test$TestFileInfo;
        }
        clsArr[5] = clsClass$6;
        if (class$org$hyperic$sigar$test$TestFileSystem == null) {
            clsClass$7 = class$("org.hyperic.sigar.test.TestFileSystem");
            class$org$hyperic$sigar$test$TestFileSystem = clsClass$7;
        } else {
            clsClass$7 = class$org$hyperic$sigar$test$TestFileSystem;
        }
        clsArr[6] = clsClass$7;
        if (class$org$hyperic$sigar$test$TestFQDN == null) {
            clsClass$8 = class$("org.hyperic.sigar.test.TestFQDN");
            class$org$hyperic$sigar$test$TestFQDN = clsClass$8;
        } else {
            clsClass$8 = class$org$hyperic$sigar$test$TestFQDN;
        }
        clsArr[7] = clsClass$8;
        if (class$org$hyperic$sigar$test$TestLoadAverage == null) {
            clsClass$9 = class$("org.hyperic.sigar.test.TestLoadAverage");
            class$org$hyperic$sigar$test$TestLoadAverage = clsClass$9;
        } else {
            clsClass$9 = class$org$hyperic$sigar$test$TestLoadAverage;
        }
        clsArr[8] = clsClass$9;
        if (class$org$hyperic$sigar$test$TestMem == null) {
            clsClass$10 = class$("org.hyperic.sigar.test.TestMem");
            class$org$hyperic$sigar$test$TestMem = clsClass$10;
        } else {
            clsClass$10 = class$org$hyperic$sigar$test$TestMem;
        }
        clsArr[9] = clsClass$10;
        if (class$org$hyperic$sigar$test$TestNetIf == null) {
            clsClass$11 = class$("org.hyperic.sigar.test.TestNetIf");
            class$org$hyperic$sigar$test$TestNetIf = clsClass$11;
        } else {
            clsClass$11 = class$org$hyperic$sigar$test$TestNetIf;
        }
        clsArr[10] = clsClass$11;
        if (class$org$hyperic$sigar$test$TestNetInfo == null) {
            clsClass$12 = class$("org.hyperic.sigar.test.TestNetInfo");
            class$org$hyperic$sigar$test$TestNetInfo = clsClass$12;
        } else {
            clsClass$12 = class$org$hyperic$sigar$test$TestNetInfo;
        }
        clsArr[11] = clsClass$12;
        if (class$org$hyperic$sigar$test$TestNetRoute == null) {
            clsClass$13 = class$("org.hyperic.sigar.test.TestNetRoute");
            class$org$hyperic$sigar$test$TestNetRoute = clsClass$13;
        } else {
            clsClass$13 = class$org$hyperic$sigar$test$TestNetRoute;
        }
        clsArr[12] = clsClass$13;
        if (class$org$hyperic$sigar$test$TestNetStat == null) {
            clsClass$14 = class$("org.hyperic.sigar.test.TestNetStat");
            class$org$hyperic$sigar$test$TestNetStat = clsClass$14;
        } else {
            clsClass$14 = class$org$hyperic$sigar$test$TestNetStat;
        }
        clsArr[13] = clsClass$14;
        if (class$org$hyperic$sigar$test$TestNetStatPort == null) {
            clsClass$15 = class$("org.hyperic.sigar.test.TestNetStatPort");
            class$org$hyperic$sigar$test$TestNetStatPort = clsClass$15;
        } else {
            clsClass$15 = class$org$hyperic$sigar$test$TestNetStatPort;
        }
        clsArr[14] = clsClass$15;
        if (class$org$hyperic$sigar$test$TestTcpStat == null) {
            clsClass$16 = class$("org.hyperic.sigar.test.TestTcpStat");
            class$org$hyperic$sigar$test$TestTcpStat = clsClass$16;
        } else {
            clsClass$16 = class$org$hyperic$sigar$test$TestTcpStat;
        }
        clsArr[15] = clsClass$16;
        if (class$org$hyperic$sigar$test$TestNfsClientV2 == null) {
            clsClass$17 = class$("org.hyperic.sigar.test.TestNfsClientV2");
            class$org$hyperic$sigar$test$TestNfsClientV2 = clsClass$17;
        } else {
            clsClass$17 = class$org$hyperic$sigar$test$TestNfsClientV2;
        }
        clsArr[16] = clsClass$17;
        if (class$org$hyperic$sigar$test$TestNfsServerV2 == null) {
            clsClass$18 = class$("org.hyperic.sigar.test.TestNfsServerV2");
            class$org$hyperic$sigar$test$TestNfsServerV2 = clsClass$18;
        } else {
            clsClass$18 = class$org$hyperic$sigar$test$TestNfsServerV2;
        }
        clsArr[17] = clsClass$18;
        if (class$org$hyperic$sigar$test$TestNfsClientV3 == null) {
            clsClass$19 = class$("org.hyperic.sigar.test.TestNfsClientV3");
            class$org$hyperic$sigar$test$TestNfsClientV3 = clsClass$19;
        } else {
            clsClass$19 = class$org$hyperic$sigar$test$TestNfsClientV3;
        }
        clsArr[18] = clsClass$19;
        if (class$org$hyperic$sigar$test$TestNfsServerV3 == null) {
            clsClass$20 = class$("org.hyperic.sigar.test.TestNfsServerV3");
            class$org$hyperic$sigar$test$TestNfsServerV3 = clsClass$20;
        } else {
            clsClass$20 = class$org$hyperic$sigar$test$TestNfsServerV3;
        }
        clsArr[19] = clsClass$20;
        if (class$org$hyperic$sigar$test$TestProcArgs == null) {
            clsClass$21 = class$("org.hyperic.sigar.test.TestProcArgs");
            class$org$hyperic$sigar$test$TestProcArgs = clsClass$21;
        } else {
            clsClass$21 = class$org$hyperic$sigar$test$TestProcArgs;
        }
        clsArr[20] = clsClass$21;
        if (class$org$hyperic$sigar$test$TestProcEnv == null) {
            clsClass$22 = class$("org.hyperic.sigar.test.TestProcEnv");
            class$org$hyperic$sigar$test$TestProcEnv = clsClass$22;
        } else {
            clsClass$22 = class$org$hyperic$sigar$test$TestProcEnv;
        }
        clsArr[21] = clsClass$22;
        if (class$org$hyperic$sigar$test$TestProcExe == null) {
            clsClass$23 = class$("org.hyperic.sigar.test.TestProcExe");
            class$org$hyperic$sigar$test$TestProcExe = clsClass$23;
        } else {
            clsClass$23 = class$org$hyperic$sigar$test$TestProcExe;
        }
        clsArr[22] = clsClass$23;
        if (class$org$hyperic$sigar$test$TestProcModules == null) {
            clsClass$24 = class$("org.hyperic.sigar.test.TestProcModules");
            class$org$hyperic$sigar$test$TestProcModules = clsClass$24;
        } else {
            clsClass$24 = class$org$hyperic$sigar$test$TestProcModules;
        }
        clsArr[23] = clsClass$24;
        if (class$org$hyperic$sigar$test$TestProcFd == null) {
            clsClass$25 = class$("org.hyperic.sigar.test.TestProcFd");
            class$org$hyperic$sigar$test$TestProcFd = clsClass$25;
        } else {
            clsClass$25 = class$org$hyperic$sigar$test$TestProcFd;
        }
        clsArr[24] = clsClass$25;
        if (class$org$hyperic$sigar$test$TestProcList == null) {
            clsClass$26 = class$("org.hyperic.sigar.test.TestProcList");
            class$org$hyperic$sigar$test$TestProcList = clsClass$26;
        } else {
            clsClass$26 = class$org$hyperic$sigar$test$TestProcList;
        }
        clsArr[25] = clsClass$26;
        if (class$org$hyperic$sigar$test$TestProcMem == null) {
            clsClass$27 = class$("org.hyperic.sigar.test.TestProcMem");
            class$org$hyperic$sigar$test$TestProcMem = clsClass$27;
        } else {
            clsClass$27 = class$org$hyperic$sigar$test$TestProcMem;
        }
        clsArr[26] = clsClass$27;
        if (class$org$hyperic$sigar$test$TestProcState == null) {
            clsClass$28 = class$("org.hyperic.sigar.test.TestProcState");
            class$org$hyperic$sigar$test$TestProcState = clsClass$28;
        } else {
            clsClass$28 = class$org$hyperic$sigar$test$TestProcState;
        }
        clsArr[27] = clsClass$28;
        if (class$org$hyperic$sigar$test$TestProcStat == null) {
            clsClass$29 = class$("org.hyperic.sigar.test.TestProcStat");
            class$org$hyperic$sigar$test$TestProcStat = clsClass$29;
        } else {
            clsClass$29 = class$org$hyperic$sigar$test$TestProcStat;
        }
        clsArr[28] = clsClass$29;
        if (class$org$hyperic$sigar$test$TestProcTime == null) {
            clsClass$30 = class$("org.hyperic.sigar.test.TestProcTime");
            class$org$hyperic$sigar$test$TestProcTime = clsClass$30;
        } else {
            clsClass$30 = class$org$hyperic$sigar$test$TestProcTime;
        }
        clsArr[29] = clsClass$30;
        if (class$org$hyperic$sigar$test$TestResourceLimit == null) {
            clsClass$31 = class$("org.hyperic.sigar.test.TestResourceLimit");
            class$org$hyperic$sigar$test$TestResourceLimit = clsClass$31;
        } else {
            clsClass$31 = class$org$hyperic$sigar$test$TestResourceLimit;
        }
        clsArr[30] = clsClass$31;
        if (class$org$hyperic$sigar$test$TestSignal == null) {
            clsClass$32 = class$("org.hyperic.sigar.test.TestSignal");
            class$org$hyperic$sigar$test$TestSignal = clsClass$32;
        } else {
            clsClass$32 = class$org$hyperic$sigar$test$TestSignal;
        }
        clsArr[31] = clsClass$32;
        if (class$org$hyperic$sigar$test$TestSwap == null) {
            clsClass$33 = class$("org.hyperic.sigar.test.TestSwap");
            class$org$hyperic$sigar$test$TestSwap = clsClass$33;
        } else {
            clsClass$33 = class$org$hyperic$sigar$test$TestSwap;
        }
        clsArr[32] = clsClass$33;
        if (class$org$hyperic$sigar$test$TestThreadCpu == null) {
            clsClass$34 = class$("org.hyperic.sigar.test.TestThreadCpu");
            class$org$hyperic$sigar$test$TestThreadCpu = clsClass$34;
        } else {
            clsClass$34 = class$org$hyperic$sigar$test$TestThreadCpu;
        }
        clsArr[33] = clsClass$34;
        if (class$org$hyperic$sigar$test$TestUptime == null) {
            clsClass$35 = class$("org.hyperic.sigar.test.TestUptime");
            class$org$hyperic$sigar$test$TestUptime = clsClass$35;
        } else {
            clsClass$35 = class$org$hyperic$sigar$test$TestUptime;
        }
        clsArr[34] = clsClass$35;
        if (class$org$hyperic$sigar$test$TestVMware == null) {
            clsClass$36 = class$("org.hyperic.sigar.test.TestVMware");
            class$org$hyperic$sigar$test$TestVMware = clsClass$36;
        } else {
            clsClass$36 = class$org$hyperic$sigar$test$TestVMware;
        }
        clsArr[35] = clsClass$36;
        if (class$org$hyperic$sigar$test$TestWho == null) {
            clsClass$37 = class$("org.hyperic.sigar.test.TestWho");
            class$org$hyperic$sigar$test$TestWho = clsClass$37;
        } else {
            clsClass$37 = class$org$hyperic$sigar$test$TestWho;
        }
        clsArr[36] = clsClass$37;
        if (class$org$hyperic$sigar$test$TestHumidor == null) {
            clsClass$38 = class$("org.hyperic.sigar.test.TestHumidor");
            class$org$hyperic$sigar$test$TestHumidor = clsClass$38;
        } else {
            clsClass$38 = class$org$hyperic$sigar$test$TestHumidor;
        }
        clsArr[37] = clsClass$38;
        ALL_TESTS = clsArr;
        Class[] clsArr2 = new Class[7];
        if (class$org$hyperic$sigar$win32$test$TestEventLog == null) {
            clsClass$39 = class$("org.hyperic.sigar.win32.test.TestEventLog");
            class$org$hyperic$sigar$win32$test$TestEventLog = clsClass$39;
        } else {
            clsClass$39 = class$org$hyperic$sigar$win32$test$TestEventLog;
        }
        clsArr2[0] = clsClass$39;
        if (class$org$hyperic$sigar$win32$test$TestLocaleInfo == null) {
            clsClass$40 = class$("org.hyperic.sigar.win32.test.TestLocaleInfo");
            class$org$hyperic$sigar$win32$test$TestLocaleInfo = clsClass$40;
        } else {
            clsClass$40 = class$org$hyperic$sigar$win32$test$TestLocaleInfo;
        }
        clsArr2[1] = clsClass$40;
        if (class$org$hyperic$sigar$win32$test$TestPdh == null) {
            clsClass$41 = class$("org.hyperic.sigar.win32.test.TestPdh");
            class$org$hyperic$sigar$win32$test$TestPdh = clsClass$41;
        } else {
            clsClass$41 = class$org$hyperic$sigar$win32$test$TestPdh;
        }
        clsArr2[2] = clsClass$41;
        if (class$org$hyperic$sigar$win32$test$TestMetaBase == null) {
            clsClass$42 = class$("org.hyperic.sigar.win32.test.TestMetaBase");
            class$org$hyperic$sigar$win32$test$TestMetaBase = clsClass$42;
        } else {
            clsClass$42 = class$org$hyperic$sigar$win32$test$TestMetaBase;
        }
        clsArr2[3] = clsClass$42;
        if (class$org$hyperic$sigar$win32$test$TestRegistryKey == null) {
            clsClass$43 = class$("org.hyperic.sigar.win32.test.TestRegistryKey");
            class$org$hyperic$sigar$win32$test$TestRegistryKey = clsClass$43;
        } else {
            clsClass$43 = class$org$hyperic$sigar$win32$test$TestRegistryKey;
        }
        clsArr2[4] = clsClass$43;
        if (class$org$hyperic$sigar$win32$test$TestService == null) {
            clsClass$44 = class$("org.hyperic.sigar.win32.test.TestService");
            class$org$hyperic$sigar$win32$test$TestService = clsClass$44;
        } else {
            clsClass$44 = class$org$hyperic$sigar$win32$test$TestService;
        }
        clsArr2[5] = clsClass$44;
        if (class$org$hyperic$sigar$win32$test$TestFileVersion == null) {
            clsClass$45 = class$("org.hyperic.sigar.win32.test.TestFileVersion");
            class$org$hyperic$sigar$win32$test$TestFileVersion = clsClass$45;
        } else {
            clsClass$45 = class$org$hyperic$sigar$win32$test$TestFileVersion;
        }
        clsArr2[6] = clsClass$45;
        WIN32_TESTS = clsArr2;
        if (SigarLoader.IS_WIN32) {
            TESTS = new Class[ALL_TESTS.length + WIN32_TESTS.length];
            System.arraycopy(ALL_TESTS, 0, TESTS, 0, ALL_TESTS.length);
            System.arraycopy(WIN32_TESTS, 0, TESTS, ALL_TESTS.length, WIN32_TESTS.length);
            return;
        }
        TESTS = ALL_TESTS;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public SigarTestRunner(Shell shell) {
        super(shell);
        this.completions = new ArrayList();
        for (int i = 0; i < TESTS.length; i++) {
            String name = TESTS[i].getName();
            int ix = name.lastIndexOf(".Test");
            this.completions.add(name.substring(ix + 5));
        }
    }

    public SigarTestRunner() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[testclass]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Run sigar tests";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public Collection getCompletions() {
        return this.completions;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws Throwable {
        SigarTestPrinter.runTests(TESTS, args);
    }

    public static void main(String[] args) throws Exception {
        new SigarTestRunner().processCommand(args);
    }
}
