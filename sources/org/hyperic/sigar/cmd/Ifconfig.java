package org.hyperic.sigar.cmd;

import ch.qos.logback.classic.net.SyslogAppender;
import java.util.Arrays;
import java.util.Collection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Ifconfig.class */
public class Ifconfig extends SigarCommandBase {
    public Ifconfig(Shell shell) {
        super(shell);
    }

    public Ifconfig() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length <= 1;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[interface]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Network interface information";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public Collection getCompletions() {
        try {
            String[] ifNames = this.proxy.getNetInterfaceList();
            return Arrays.asList(ifNames);
        } catch (SigarException e) {
            return super.getCompletions();
        }
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        String[] ifNames;
        if (args.length == 1) {
            ifNames = args;
        } else {
            ifNames = this.proxy.getNetInterfaceList();
        }
        for (int i = 0; i < ifNames.length; i++) {
            try {
                output(ifNames[i]);
            } catch (SigarException e) {
                println(new StringBuffer().append(ifNames[i]).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(e.getMessage()).toString());
            }
        }
    }

    public void output(String name) throws SigarException {
        NetInterfaceConfig ifconfig = this.sigar.getNetInterfaceConfig(name);
        long flags = ifconfig.getFlags();
        String hwaddr = "";
        if (!NetFlags.NULL_HWADDR.equals(ifconfig.getHwaddr())) {
            hwaddr = new StringBuffer().append(" HWaddr ").append(ifconfig.getHwaddr()).toString();
        }
        if (!ifconfig.getName().equals(ifconfig.getDescription())) {
            println(ifconfig.getDescription());
        }
        println(new StringBuffer().append(ifconfig.getName()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append("Link encap:").append(ifconfig.getType()).append(hwaddr).toString());
        String ptp = "";
        if ((flags & 16) > 0) {
            ptp = new StringBuffer().append("  P-t-P:").append(ifconfig.getDestination()).toString();
        }
        String bcast = "";
        if ((flags & 2) > 0) {
            bcast = new StringBuffer().append("  Bcast:").append(ifconfig.getBroadcast()).toString();
        }
        println(new StringBuffer().append("\tinet addr:").append(ifconfig.getAddress()).append(ptp).append(bcast).append("  Mask:").append(ifconfig.getNetmask()).toString());
        println(new StringBuffer().append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(NetFlags.getIfFlagsString(flags)).append(" MTU:").append(ifconfig.getMtu()).append("  Metric:").append(ifconfig.getMetric()).toString());
        try {
            NetInterfaceStat ifstat = this.sigar.getNetInterfaceStat(name);
            println(new StringBuffer().append("\tRX packets:").append(ifstat.getRxPackets()).append(" errors:").append(ifstat.getRxErrors()).append(" dropped:").append(ifstat.getRxDropped()).append(" overruns:").append(ifstat.getRxOverruns()).append(" frame:").append(ifstat.getRxFrame()).toString());
            println(new StringBuffer().append("\tTX packets:").append(ifstat.getTxPackets()).append(" errors:").append(ifstat.getTxErrors()).append(" dropped:").append(ifstat.getTxDropped()).append(" overruns:").append(ifstat.getTxOverruns()).append(" carrier:").append(ifstat.getTxCarrier()).toString());
            println(new StringBuffer().append("\tcollisions:").append(ifstat.getTxCollisions()).toString());
            long rxBytes = ifstat.getRxBytes();
            long txBytes = ifstat.getTxBytes();
            println(new StringBuffer().append("\tRX bytes:").append(rxBytes).append(" (").append(Sigar.formatSize(rxBytes)).append(")").append("  ").append("TX bytes:").append(txBytes).append(" (").append(Sigar.formatSize(txBytes)).append(")").toString());
        } catch (SigarException e) {
        }
        println("");
    }

    public static void main(String[] args) throws Exception {
        new Ifconfig().processCommand(args);
    }
}
