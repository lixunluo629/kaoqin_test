package org.hyperic.sigar.cmd;

import ch.qos.logback.classic.net.SyslogAppender;
import org.hyperic.sigar.DirUsage;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Du.class */
public class Du extends SigarCommandBase {
    public Du(Shell shell) {
        super(shell);
    }

    public Du() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display usage for a directory recursively";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length == 1;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        String dir = args[0];
        DirUsage du = this.sigar.getDirUsage(dir);
        println(new StringBuffer().append(du.getDiskUsage()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(dir).toString());
    }

    public static void main(String[] args) throws Exception {
        new Du().processCommand(args);
    }
}
