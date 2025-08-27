package org.hyperic.sigar.shell;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommandBase.class */
public class ShellCommandBase implements ShellCommandHandler {
    protected String itsCommandName = null;
    protected ShellBase itsShell = null;
    private PrintStream out = null;

    public String getCommandName() {
        return this.itsCommandName;
    }

    public ShellBase getShell() {
        return this.itsShell;
    }

    public PrintStream getOutStream() {
        return getShell().getOutStream();
    }

    public PrintStream getErrStream() {
        return getShell().getErrStream();
    }

    @Override // org.hyperic.sigar.shell.ShellCommandHandler
    public void init(String commandName, ShellBase shell) throws ShellCommandInitException {
        this.itsCommandName = commandName;
        this.itsShell = shell;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        this.out.println(new StringBuffer().append("ShellCommandBase: not implemented: ").append(this.itsCommandName).toString());
    }

    @Override // org.hyperic.sigar.shell.ShellCommandHandler
    public String getSyntax() {
        return new StringBuffer().append("Syntax: ").append(getCommandName()).append(SymbolConstants.SPACE_SYMBOL).append(getSyntaxArgs()).toString();
    }

    public String getSyntaxArgs() {
        return "";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return new StringBuffer().append("Help not available for command ").append(this.itsCommandName).toString();
    }
}
