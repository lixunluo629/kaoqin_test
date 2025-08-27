package org.hyperic.sigar.cmd;

import ch.qos.logback.classic.net.SyslogAppender;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Ls.class */
public class Ls extends SigarCommandBase {
    public Ls(Shell shell) {
        super(shell);
    }

    public Ls() {
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "simple FileInfo test at the moment (like ls -l)";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length == 1;
    }

    private String getDate(long mtime) {
        return new SimpleDateFormat("MMM dd  yyyy").format(new Date(mtime));
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        String file = args[0];
        FileInfo link = this.sigar.getLinkInfo(file);
        FileInfo info = this.sigar.getFileInfo(file);
        if (link.getType() == 6) {
            try {
                file = new StringBuffer().append(file).append(" -> ").append(new File(file).getCanonicalPath()).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        println(new StringBuffer().append(link.getTypeChar()).append(info.getPermissionsString()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getUid()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getGid()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(info.getSize()).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(getDate(info.getMtime())).append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(file).toString());
    }

    public static void main(String[] args) throws Exception {
        new Ls().processCommand(args);
    }
}
