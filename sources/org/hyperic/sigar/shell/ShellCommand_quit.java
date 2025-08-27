package org.hyperic.sigar.shell;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_quit.class */
public class ShellCommand_quit extends ShellCommandBase {
    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        throw new NormalQuitCommandException();
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Terminate the shell";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return "    Terminate the shell.";
    }
}
