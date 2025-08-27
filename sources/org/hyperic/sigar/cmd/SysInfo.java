package org.hyperic.sigar.cmd;

import java.util.Arrays;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/SysInfo.class */
public class SysInfo extends SigarCommandBase {
    public SysInfo(Shell shell) {
        super(shell);
    }

    public SysInfo() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display system information";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws Throwable {
        Version.printInfo(this.out);
        println("");
        new Uptime(this.shell).output(args);
        println("");
        CpuInfo cpuinfo = new CpuInfo(this.shell);
        cpuinfo.displayTimes = false;
        cpuinfo.output(args);
        println("");
        new Free(this.shell).output(args);
        println("");
        println(new StringBuffer().append("File Systems.........").append(Arrays.asList(this.sigar.getFileSystemList())).toString());
        println("");
        println(new StringBuffer().append("Network Interfaces...").append(Arrays.asList(this.sigar.getNetInterfaceList())).toString());
        println("");
        println("System resource limits:");
        new Ulimit(this.shell).output(args);
    }

    public static void main(String[] args) throws Exception {
        new SysInfo().processCommand(args);
    }
}
