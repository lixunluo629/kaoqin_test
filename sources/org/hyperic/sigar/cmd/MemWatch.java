package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/MemWatch.class */
public class MemWatch {
    static final int SLEEP_TIME = 10000;

    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        if (args.length != 1) {
            throw new Exception("Usage: MemWatch pid");
        }
        long pid = Long.parseLong(args[0]);
        long lastTime = System.currentTimeMillis();
        ProcMem last = sigar.getProcMem(pid);
        while (true) {
            ProcMem cur = sigar.getProcMem(pid);
            StringBuffer diff = diff(last, cur);
            if (diff.length() == 0) {
                System.out.println(new StringBuffer().append("no change (size=").append(Sigar.formatSize(cur.getSize())).append(")").toString());
            } else {
                long curTime = System.currentTimeMillis();
                long timeDiff = curTime - lastTime;
                lastTime = curTime;
                diff.append(new StringBuffer().append(" after ").append(timeDiff).append("ms").toString());
                System.out.println(diff);
            }
            last = cur;
            Thread.sleep(10000L);
        }
    }

    private static StringBuffer diff(ProcMem last, ProcMem cur) {
        StringBuffer buf = new StringBuffer();
        long diff = cur.getSize() - last.getSize();
        if (diff != 0) {
            buf.append(new StringBuffer().append("size=").append(diff).toString());
        }
        long diff2 = cur.getResident() - last.getResident();
        if (diff2 != 0) {
            buf.append(new StringBuffer().append(", resident=").append(diff2).toString());
        }
        long diff3 = cur.getShare() - last.getShare();
        if (diff3 != 0) {
            buf.append(new StringBuffer().append(", share=").append(diff3).toString());
        }
        return buf;
    }
}
