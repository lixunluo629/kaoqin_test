package org.hyperic.sigar.cmd;

import org.hyperic.sigar.CpuTimer;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Time.class */
public class Time extends SigarCommandBase {
    public Time(Shell shell) {
        super(shell);
    }

    public Time() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length >= 1;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[command] [...]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Time command";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException, NumberFormatException {
        int num;
        boolean isInteractive = this.shell.isInteractive();
        this.shell.setInteractive(false);
        CpuTimer cpu = new CpuTimer(this.sigar);
        if (Character.isDigit(args[0].charAt(0))) {
            num = Integer.parseInt(args[0]);
            String[] xargs = new String[args.length - 1];
            System.arraycopy(args, 1, xargs, 0, xargs.length);
            args = xargs;
        } else {
            num = 1;
        }
        cpu.start();
        for (int i = 0; i < num; i++) {
            try {
                this.shell.handleCommand(new StringBuffer().append("time ").append(args[0]).toString(), args);
            } finally {
                this.shell.setInteractive(isInteractive);
            }
        }
        cpu.stop();
        cpu.list(this.out);
    }

    public static void main(String[] args) throws Exception {
        new Time().processCommand(args);
    }
}
