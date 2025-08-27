package org.hyperic.sigar.cmd;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/CpuInfo.class */
public class CpuInfo extends SigarCommandBase {
    public boolean displayTimes;

    public CpuInfo(Shell shell) {
        super(shell);
        this.displayTimes = true;
    }

    public CpuInfo() {
        this.displayTimes = true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display cpu information";
    }

    private void output(CpuPerc cpu) {
        println(new StringBuffer().append("User Time.....").append(CpuPerc.format(cpu.getUser())).toString());
        println(new StringBuffer().append("Sys Time......").append(CpuPerc.format(cpu.getSys())).toString());
        println(new StringBuffer().append("Idle Time.....").append(CpuPerc.format(cpu.getIdle())).toString());
        println(new StringBuffer().append("Wait Time.....").append(CpuPerc.format(cpu.getWait())).toString());
        println(new StringBuffer().append("Nice Time.....").append(CpuPerc.format(cpu.getNice())).toString());
        println(new StringBuffer().append("Combined......").append(CpuPerc.format(cpu.getCombined())).toString());
        println(new StringBuffer().append("Irq Time......").append(CpuPerc.format(cpu.getIrq())).toString());
        if (SigarLoader.IS_LINUX) {
            println(new StringBuffer().append("SoftIrq Time..").append(CpuPerc.format(cpu.getSoftIrq())).toString());
            println(new StringBuffer().append("Stolen Time....").append(CpuPerc.format(cpu.getStolen())).toString());
        }
        println("");
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException, InterruptedException {
        org.hyperic.sigar.CpuInfo[] infos = this.sigar.getCpuInfoList();
        CpuPerc[] cpus = this.sigar.getCpuPercList();
        org.hyperic.sigar.CpuInfo info = infos[0];
        long cacheSize = info.getCacheSize();
        println(new StringBuffer().append("Vendor.........").append(info.getVendor()).toString());
        println(new StringBuffer().append("Model..........").append(info.getModel()).toString());
        println(new StringBuffer().append("Mhz............").append(info.getMhz()).toString());
        println(new StringBuffer().append("Total CPUs.....").append(info.getTotalCores()).toString());
        if (info.getTotalCores() != info.getTotalSockets() || info.getCoresPerSocket() > info.getTotalCores()) {
            println(new StringBuffer().append("Physical CPUs..").append(info.getTotalSockets()).toString());
            println(new StringBuffer().append("Cores per CPU..").append(info.getCoresPerSocket()).toString());
        }
        if (cacheSize != -1) {
            println(new StringBuffer().append("Cache size....").append(cacheSize).toString());
        }
        println("");
        if (!this.displayTimes) {
            return;
        }
        for (int i = 0; i < cpus.length; i++) {
            println(new StringBuffer().append("CPU ").append(i).append(".........").toString());
            output(cpus[i]);
        }
        println("Totals........");
        output(this.sigar.getCpuPerc());
    }

    public static void main(String[] args) throws Exception {
        new CpuInfo().processCommand(args);
    }
}
