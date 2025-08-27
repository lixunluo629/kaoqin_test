package org.hyperic.sigar.cmd;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.catalina.Lifecycle;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.win32.Service;
import org.hyperic.sigar.win32.Win32Exception;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Win32Service.class */
public class Win32Service extends SigarCommandBase {
    private static final List COMMANDS = Arrays.asList("state", Lifecycle.START_EVENT, Lifecycle.STOP_EVENT, "pause", "resume", "restart");

    public Win32Service() {
    }

    public Win32Service(Shell shell) {
        super(shell);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "[name] [action]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Windows service commands";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return args.length == 1 || args.length == 2;
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public Collection getCompletions() {
        try {
            return Service.getServiceNames();
        } catch (Win32Exception e) {
            return null;
        }
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        Service service = null;
        String name = args[0];
        String cmd = null;
        if (args.length == 2) {
            cmd = args[1];
        }
        try {
            service = new Service(name);
            if (cmd == null || cmd.equals("state")) {
                service.list(this.out);
            } else if (cmd.equals(Lifecycle.START_EVENT)) {
                service.start();
            } else if (cmd.equals(Lifecycle.STOP_EVENT)) {
                service.stop();
            } else if (cmd.equals("pause")) {
                service.pause();
            } else if (cmd.equals("resume")) {
                service.resume();
            } else if (cmd.equals("delete")) {
                service.delete();
            } else if (cmd.equals("restart")) {
                service.stop(0L);
                service.start();
            } else {
                println(new StringBuffer().append("Unsupported service command: ").append(args[1]).toString());
                println(new StringBuffer().append("Valid commands: ").append(COMMANDS).toString());
            }
        } finally {
            if (service != null) {
                service.close();
            }
        }
    }
}
