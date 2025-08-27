package org.hyperic.sigar.shell;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_get.class */
public class ShellCommand_get extends ShellCommandBase {
    private void printProperty(String key, String value) {
        PrintStream out = getOutStream();
        out.print(new StringBuffer().append(key).append(SymbolConstants.EQUAL_SYMBOL).toString());
        if (value.trim() != value) {
            out.println(new StringBuffer().append("'").append(value).append("'").toString());
        } else {
            out.println(value);
        }
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        if (args.length < 1) {
            throw new ShellCommandUsageException(getSyntax());
        }
        for (int i = 0; i < args.length; i++) {
            String val = System.getProperty(args[i], "UNDEFINED");
            printProperty(args[i], val);
        }
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<key1> [key2] ...";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Get system properties";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        return new StringBuffer().append("    ").append(getUsageShort()).append(".").toString();
    }
}
