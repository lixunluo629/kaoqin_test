package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestHumidor.class */
public class TestHumidor extends SigarTestCase {

    /* renamed from: org.hyperic.sigar.test.TestHumidor$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestHumidor$1.class */
    static class AnonymousClass1 {
    }

    public TestHumidor(String name) {
        super(name);
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestHumidor$HumidorThread.class */
    private static class HumidorThread extends Thread {
        private SigarProxy sigar;

        HumidorThread(SigarProxy x0, AnonymousClass1 x1) {
            this(x0);
        }

        private HumidorThread(SigarProxy sigar) {
            this.sigar = sigar;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                TestHumidor.getProcCpu(this.sigar);
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getProcCpu(SigarProxy sigar) throws Exception {
        long[] pids = sigar.getProcList();
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < pids.length; i++) {
                try {
                    double cpu = sigar.getProcCpu(pids[i]).getPercent();
                    if (SigarTestCase.getVerbose()) {
                        System.out.println(new StringBuffer().append(Thread.currentThread().getName()).append(SymbolConstants.SPACE_SYMBOL).append(pids[i]).append(SymbolConstants.EQUAL_SYMBOL).append(CpuPerc.format(cpu)).toString());
                    }
                } catch (SigarException e) {
                }
            }
        }
    }

    private void runTests(SigarProxy sigar) throws Exception {
        ArrayList threads = new ArrayList();
        for (int i = 0; i < 3; i++) {
            HumidorThread humidorThread = new HumidorThread(sigar, null);
            threads.add(humidorThread);
            humidorThread.start();
        }
        for (int i2 = 0; i2 < threads.size(); i2++) {
            Thread t = (Thread) threads.get(i2);
            t.join();
        }
    }

    public void testGlobalInstance() throws Exception {
        runTests(Humidor.getInstance().getSigar());
    }

    public void testInstance() throws Exception {
        Sigar sigar = new Sigar();
        runTests(new Humidor(sigar).getSigar());
        sigar.close();
    }
}
