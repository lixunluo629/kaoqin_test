package org.hyperic.sigar.win32.test;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import org.hyperic.sigar.test.SigarTestCase;
import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.win32.Pdh;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/win32/test/TestPdh.class */
public class TestPdh extends SigarTestCase {
    public TestPdh(String name) {
        super(name);
    }

    private static boolean isCounter(long type) {
        return (type & 1024) == 1024;
    }

    private void getValue(String key) throws Exception {
        Pdh pdh = new Pdh();
        traceln(new StringBuffer().append(key).append(": ").append(pdh.getDescription(key)).toString());
        traceln(new StringBuffer().append("counter=").append(isCounter(pdh.getCounterType(key))).toString());
        assertGtEqZeroTrace("raw", (long) pdh.getRawValue(key));
        assertGtEqZeroTrace("fmt", (long) pdh.getFormattedValue(key));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void testGetValue() throws Exception {
        Pdh.enableTranslation();
        String[] strArr = {new String[]{EventLog.SYSTEM, "System Up Time"}, new String[]{"Memory", "Available Bytes"}, new String[]{"Memory", "Pages/sec"}, new String[]{"Processor(_Total)", "% User Time"}};
        for (int i = 0; i < strArr.length; i++) {
            String path = new StringBuffer().append("\\").append(strArr[i][0]).append("\\").append(strArr[i][1]).toString();
            String trans = Pdh.translate(path);
            if (!trans.equals(path)) {
                traceln(new StringBuffer().append(path).append("-->").append(trans).toString());
            }
            traceln(new StringBuffer().append(path).append(" validate: ").append(Pdh.validate(path)).toString());
            getValue(path);
        }
    }

    public void testCounterMap() throws Exception {
        Map counters = Pdh.getEnglishPerflibCounterMap();
        assertGtZeroTrace("counters", counters.size());
        int dups = 0;
        for (Map.Entry entry : counters.entrySet()) {
            if (((int[]) entry.getValue()).length > 1) {
                dups++;
            }
        }
        traceln(new StringBuffer().append(dups).append(" names have dups").toString());
        String[] keys = {EventLog.SYSTEM, "System Up Time"};
        int last = -1;
        for (String name : keys) {
            int[] ix = (int[]) counters.get(name.toLowerCase());
            assertFalse(ix[0] == last);
            traceln(new StringBuffer().append(name).append(SymbolConstants.EQUAL_SYMBOL).append(ix[0]).toString());
            last = ix[0];
            String lookupName = Pdh.getCounterName(ix[0]);
            traceln(new StringBuffer().append(name).append(SymbolConstants.EQUAL_SYMBOL).append(lookupName).toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void testValidate() {
        Object[] objArr = {new Object[]{"\\Does Not\\Exist", new Integer(Pdh.NO_OBJECT), new Integer(Pdh.BAD_COUNTERNAME)}, new Object[]{"Does Not Exist", new Integer(Pdh.BAD_COUNTERNAME)}, new Object[]{"\\System\\DoesNotExist", new Integer(Pdh.NO_COUNTER)}, new Object[]{"\\Processor(666)\\% User Time", new Integer(Pdh.NO_INSTANCE)}, new Object[]{"\\System\\Threads", new Integer(0), new Integer(Pdh.BAD_COUNTERNAME)}};
        for (int i = 0; i < objArr.length; i++) {
            String path = (String) objArr[i][0];
            int expect = ((Integer) objArr[i][1]).intValue();
            int status = Pdh.validate(path);
            boolean expectedResult = status == expect;
            if (!expectedResult && objArr[i].length == 3) {
                expect = ((Integer) objArr[i][2]).intValue();
                expectedResult = status == expect;
            }
            if (!expectedResult) {
                traceln(new StringBuffer().append("[validate] ").append(path).append("-->").append(Integer.toHexString(status).toUpperCase()).append(" != ").append(Integer.toHexString(expect).toUpperCase()).toString());
            }
            assertTrue(expectedResult);
        }
    }

    public void testPdh() throws Exception {
        String[] iface = Pdh.getKeys("Thread");
        assertTrue(iface.length > 0);
    }
}
