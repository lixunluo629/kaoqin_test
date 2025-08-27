package org.hyperic.sigar.cmd;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.util.PrintfFormat;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Uptime.class */
public class Uptime extends SigarCommandBase {
    private static Object[] loadAvg = new Object[3];
    private static PrintfFormat formatter = new PrintfFormat("%.2f, %.2f, %.2f");

    public Uptime(Shell shell) {
        super(shell);
    }

    public Uptime() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display how long the system has been running";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        System.out.println(getInfo(this.sigar));
    }

    public static String getInfo(SigarProxy sigar) throws SigarException {
        String loadAverage;
        double uptime = sigar.getUptime().getUptime();
        try {
            double[] avg = sigar.getLoadAverage();
            loadAvg[0] = new Double(avg[0]);
            loadAvg[1] = new Double(avg[1]);
            loadAvg[2] = new Double(avg[2]);
            loadAverage = new StringBuffer().append("load average: ").append(formatter.sprintf(loadAvg)).toString();
        } catch (SigarNotImplementedException e) {
            loadAverage = "(load average unknown)";
        }
        return new StringBuffer().append("  ").append(getCurrentTime()).append("  up ").append(formatUptime(uptime)).append(", ").append(loadAverage).toString();
    }

    private static String formatUptime(double uptime) {
        String retval;
        String retval2 = "";
        int days = ((int) uptime) / DateUtil.SECONDS_PER_DAY;
        if (days != 0) {
            retval2 = new StringBuffer().append(retval2).append(days).append(SymbolConstants.SPACE_SYMBOL).append(days > 1 ? "days" : "day").append(", ").toString();
        }
        int minutes = ((int) uptime) / 60;
        int hours = (minutes / 60) % 24;
        int minutes2 = minutes % 60;
        if (hours != 0) {
            retval = new StringBuffer().append(retval2).append(hours).append(":").append(minutes2).toString();
        } else {
            retval = new StringBuffer().append(retval2).append(minutes2).append(" min").toString();
        }
        return retval;
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("h:mm a").format(new Date());
    }

    public static void main(String[] args) throws Exception {
        new Uptime().processCommand(args);
    }
}
