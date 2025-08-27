package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.jmx.SigarInvokerJMX;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Ulimit.class */
public class Ulimit extends SigarCommandBase {
    private SigarInvokerJMX invoker;
    private String mode;

    public Ulimit(Shell shell) {
        super(shell);
    }

    public Ulimit() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display system resource limits";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    private static String format(long val) {
        if (val == ResourceLimit.INFINITY()) {
            return "unlimited";
        }
        return String.valueOf(val);
    }

    private String getValue(String attr) throws SigarException {
        Long val = (Long) this.invoker.invoke(new StringBuffer().append(attr).append(this.mode).toString());
        return format(val.longValue());
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        this.mode = "Cur";
        this.invoker = SigarInvokerJMX.getInstance(this.proxy, "Type=ResourceLimit");
        for (String arg : args) {
            if (arg.equals("-H")) {
                this.mode = "Max";
            } else if (arg.equals("-S")) {
                this.mode = "Cur";
            } else {
                throw new SigarException(new StringBuffer().append("Unknown argument: ").append(arg).toString());
            }
        }
        println(new StringBuffer().append("core file size.......").append(getValue("Core")).toString());
        println(new StringBuffer().append("data seg size........").append(getValue("Data")).toString());
        println(new StringBuffer().append("file size............").append(getValue("FileSize")).toString());
        println(new StringBuffer().append("pipe size............").append(getValue("PipeSize")).toString());
        println(new StringBuffer().append("max memory size......").append(getValue("Memory")).toString());
        println(new StringBuffer().append("open files...........").append(getValue("OpenFiles")).toString());
        println(new StringBuffer().append("stack size...........").append(getValue("Stack")).toString());
        println(new StringBuffer().append("cpu time.............").append(getValue("Cpu")).toString());
        println(new StringBuffer().append("max user processes...").append(getValue("Processes")).toString());
        println(new StringBuffer().append("virtual memory.......").append(getValue("VirtualMemory")).toString());
    }

    public static void main(String[] args) throws Exception {
        new Ulimit().processCommand(args);
    }
}
