package org.hyperic.sigar.cmd;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.MultiProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/MultiPs.class */
public class MultiPs extends SigarCommandBase {
    public MultiPs(Shell shell) {
        super(shell);
    }

    public MultiPs() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length == 1;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "query";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Show multi process status";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public boolean isPidCompleter() {
        return true;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        String query = args[0];
        MultiProcCpu cpu = this.proxy.getMultiProcCpu(query);
        println(new StringBuffer().append("Number of processes: ").append(cpu.getProcesses()).toString());
        println(new StringBuffer().append("Cpu usage: ").append(CpuPerc.format(cpu.getPercent())).toString());
        println(new StringBuffer().append("Cpu time: ").append(Ps.getCpuTime(cpu.getTotal())).toString());
        ProcMem mem = this.proxy.getMultiProcMem(query);
        println(new StringBuffer().append("Size: ").append(Sigar.formatSize(mem.getSize())).toString());
        println(new StringBuffer().append("Resident: ").append(Sigar.formatSize(mem.getResident())).toString());
        println(new StringBuffer().append("Share: ").append(Sigar.formatSize(mem.getShare())).toString());
    }

    public static void main(String[] args) throws Exception {
        new MultiPs().processCommand(args);
    }
}
