package org.hyperic.sigar.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.jmx.SigarInvokerJMX;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/TestInvoker.class */
public class TestInvoker extends SigarTestCase {
    private static final String[][] OK_QUERIES = {new String[]{"sigar:Type=Mem", "Free"}, new String[]{"sigar:Type=Mem", "Total"}, new String[]{"sigar:Type=Cpu", "User"}, new String[]{"sigar:Type=Cpu", "Sys"}, new String[]{"sigar:Type=CpuPerc", "User"}, new String[]{"sigar:Type=CpuPerc", "Sys"}, new String[]{"sigar:Type=Swap", "Free"}, new String[]{"sigar:Type=Swap", "Used"}, new String[]{"sigar:Type=Uptime", "Uptime"}, new String[]{"sigar:Type=LoadAverage", "0"}, new String[]{"sigar:Type=LoadAverage", "1"}, new String[]{"sigar:Type=LoadAverage", "2"}, new String[]{"sigar:Type=ProcMem,Arg=$$", "Size"}, new String[]{"sigar:Type=ProcMem,Arg=$$", "Resident"}, new String[]{"sigar:Type=ProcTime,Arg=$$", "Sys"}, new String[]{"sigar:Type=ProcTime,Arg=$$", "User"}, new String[]{"sigar:Type=ProcTime,Arg=$$", "Total"}, new String[]{"sigar:Type=MultiProcCpu,Arg=CredName.User.eq%3Ddougm", "Sys"}, new String[]{"sigar:Type=MultiProcMem,Arg=CredName.User.eq%3Ddougm", "Size"}, new String[]{"sigar:Type=ProcTime,Arg=$$", "Stime"}, new String[]{"sigar:Type=ProcTime,Arg=$$", "Utime"}, new String[]{"sigar:Type=CpuPercList,Arg=0", "Idle"}, new String[]{"sigar:Type=NetStat", "TcpOutboundTotal"}, new String[]{"sigar:Type=NetStat", "TcpListen"}};
    private static final String[][] BROKEN_QUERIES = {new String[]{"sigar:Type=BREAK", "Free"}, new String[]{"sigar:Type=Mem", "BREAK"}, new String[]{"sigar:Type=ProcTime,Arg=BREAK", "Sys"}, new String[]{"sigar:Type=CpuPercList,Arg=1000", "Idle"}, new String[]{"sigar:Type=CpuPercList,Arg=BREAK", "Idle"}};

    public TestInvoker(String name) {
        super(name);
    }

    public void testCreate() throws Exception {
        Sigar sigar = new Sigar();
        SigarProxy proxy = SigarProxyCache.newInstance(sigar);
        testOK(proxy);
        sigar.close();
    }

    private void testOK(SigarProxy proxy) throws Exception {
        for (int i = 0; i < OK_QUERIES.length; i++) {
            String[] query = OK_QUERIES[i];
            SigarInvokerJMX invoker = SigarInvokerJMX.getInstance(proxy, query[0]);
            try {
                Object o = invoker.invoke(query[1]);
                traceln(new StringBuffer().append(query[0]).append(":").append(query[1]).append(SymbolConstants.EQUAL_SYMBOL).append(o).toString());
                assertTrue(true);
            } catch (SigarNotImplementedException e) {
                traceln(new StringBuffer().append(query[0]).append(" NotImplemented").toString());
            } catch (SigarPermissionDeniedException e2) {
                traceln(new StringBuffer().append(query[0]).append(" PermissionDenied").toString());
            } catch (SigarException e3) {
                traceln(new StringBuffer().append(query[0]).append(":").append(query[1]).append(SymbolConstants.EQUAL_SYMBOL).append(e3).toString());
                assertTrue(false);
            }
        }
        for (int i2 = 0; i2 < BROKEN_QUERIES.length; i2++) {
            String[] query2 = BROKEN_QUERIES[i2];
            SigarInvokerJMX invoker2 = SigarInvokerJMX.getInstance(proxy, query2[0]);
            try {
                invoker2.invoke(query2[1]);
                assertTrue(false);
            } catch (SigarException e4) {
                traceln(new StringBuffer().append(query2[0]).append(":").append(query2[1]).append(SymbolConstants.EQUAL_SYMBOL).append(e4.getMessage()).toString());
                assertTrue(true);
            }
        }
    }
}
