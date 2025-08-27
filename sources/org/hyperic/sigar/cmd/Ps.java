package org.hyperic.sigar.cmd;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Ps.class */
public class Ps extends SigarCommandBase {
    public Ps(Shell shell) {
        super(shell);
    }

    public Ps() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[pid|query]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Show process status";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        long[] pids;
        if (args.length == 0) {
            pids = this.proxy.getProcList();
        } else {
            pids = this.shell.findPids(args);
        }
        for (long pid : pids) {
            try {
                output(pid);
            } catch (SigarException e) {
                this.err.println(new StringBuffer().append("Exception getting process info for ").append(pid).append(": ").append(e.getMessage()).toString());
            }
        }
    }

    public static String join(List info) {
        StringBuffer buf = new StringBuffer();
        Iterator i = info.iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            buf.append((String) i.next());
            hasNext = i.hasNext();
            if (hasNext) {
                buf.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
            }
        }
        return buf.toString();
    }

    public static List getInfo(SigarProxy sigar, long pid) throws SigarException {
        ProcState state = sigar.getProcState(pid);
        ProcTime time = null;
        List info = new ArrayList();
        info.add(String.valueOf(pid));
        try {
            ProcCredName cred = sigar.getProcCredName(pid);
            info.add(cred.getUser());
        } catch (SigarException e) {
            info.add("???");
        }
        try {
            time = sigar.getProcTime(pid);
            info.add(getStartTime(time.getStartTime()));
        } catch (SigarException e2) {
            info.add("???");
        }
        try {
            ProcMem mem = sigar.getProcMem(pid);
            info.add(Sigar.formatSize(mem.getSize()));
            info.add(Sigar.formatSize(mem.getRss()));
            info.add(Sigar.formatSize(mem.getShare()));
        } catch (SigarException e3) {
            info.add("???");
        }
        info.add(String.valueOf(state.getState()));
        if (time != null) {
            info.add(getCpuTime(time));
        } else {
            info.add("???");
        }
        String name = ProcUtil.getDescription(sigar, pid);
        info.add(name);
        return info;
    }

    public void output(long pid) throws SigarException {
        println(join(getInfo(this.proxy, pid)));
    }

    public static String getCpuTime(long total) {
        long t = total / 1000;
        return new StringBuffer().append(t / 60).append(":").append(t % 60).toString();
    }

    public static String getCpuTime(ProcTime time) {
        return getCpuTime(time.getTotal());
    }

    private static String getStartTime(long time) {
        if (time == 0) {
            return Constants.DEFAULT_START_TIME;
        }
        long timeNow = System.currentTimeMillis();
        String fmt = "MMMd";
        if (timeNow - time < 86400000) {
            fmt = "HH:mm";
        }
        return new SimpleDateFormat(fmt).format(new Date(time));
    }

    public static void main(String[] args) throws Exception {
        new Ps().processCommand(args);
    }
}
