package org.hyperic.sigar.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestThreads.class */
public class TestThreads extends TestCase {
    private static Sigar gSigar = null;
    private static SigarProxy gProxy = null;
    private static Object lock = new Object();
    private static boolean verbose = true;

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestThreads$ProxyThread.class */
    class ProxyThread extends Thread {
        SigarException ex;
        boolean useGlobal = false;
        private final TestThreads this$0;

        ProxyThread(TestThreads testThreads) {
            this.this$0 = testThreads;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws Throwable {
            Sigar sigar;
            SigarProxy proxy;
            try {
                synchronized (TestThreads.lock) {
                    if (this.useGlobal) {
                        if (TestThreads.gSigar == null) {
                            Sigar unused = TestThreads.gSigar = new Sigar();
                            SigarProxy unused2 = TestThreads.gProxy = SigarProxyCache.newInstance(TestThreads.gSigar, 30000);
                        }
                        sigar = TestThreads.gSigar;
                        proxy = TestThreads.gProxy;
                    } else {
                        sigar = new Sigar();
                        proxy = SigarProxyCache.newInstance(sigar, 30000);
                    }
                }
                String[] args = {"leaktest", "50"};
                Proxy cmdProxy = new Proxy(sigar, proxy);
                PrintStream ps = new PrintStream(new ByteArrayOutputStream());
                if (TestThreads.verbose) {
                    cmdProxy.setVerbose(true);
                    cmdProxy.setLeakVerbose(true);
                    cmdProxy.run(args);
                } else {
                    cmdProxy.setOutputStream(ps);
                }
            } catch (SigarException e) {
                this.ex = e;
            }
        }
    }

    public TestThreads(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        ArrayList threads = new ArrayList();
        for (int i = 0; i < 4; i++) {
            ProxyThread pt = new ProxyThread(this);
            pt.useGlobal = true;
            threads.add(pt);
            pt.start();
        }
        for (int n = 0; n < threads.size(); n++) {
            ProxyThread pt2 = (ProxyThread) threads.get(n);
            pt2.join();
            if (pt2.ex != null) {
                pt2.ex.printStackTrace();
                fail(pt2.ex.getMessage());
            }
        }
    }
}
