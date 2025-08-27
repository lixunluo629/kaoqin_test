package org.hyperic.sigar.shell;

import java.util.HashMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_set.class */
public class ShellCommand_set extends ShellCommandBase {
    private HashMap keyDescriptions;

    public ShellCommand_set() {
        this.keyDescriptions = new HashMap();
        this.keyDescriptions = new HashMap();
        this.keyDescriptions.put(ShellBase.PROP_PAGE_SIZE, "The maximum size of a shell page");
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException, NumberFormatException {
        if (args.length < 1 || args.length > 2) {
            throw new ShellCommandUsageException(getSyntax());
        }
        if (args.length == 1) {
            System.getProperties().remove(args[0]);
            return;
        }
        if (args[0].equalsIgnoreCase(ShellBase.PROP_PAGE_SIZE)) {
            try {
                int newSize = Integer.parseInt(args[1]);
                if (newSize == 0 || newSize < -1) {
                    throw new NumberFormatException();
                }
                getShell().setPageSize(newSize);
            } catch (NumberFormatException e) {
                throw new ShellCommandUsageException(new StringBuffer().append(args[0]).append(" must be ").append("an integer > 0 or ").append("-1").toString());
            }
        }
        System.setProperty(args[0], args[1]);
    }

    public void addSetKey(String key, String description) {
        this.keyDescriptions.put(key, description);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<key> [value]";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Set system properties";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        String res = new StringBuffer().append("    ").append(getUsageShort()).append(".  If no value is provided, ").append("the key will be\n    deleted.").toString();
        if (this.keyDescriptions.size() != 0) {
            res = new StringBuffer().append(res).append("\n\n    Common keys include:").toString();
        }
        for (String key : this.keyDescriptions.keySet()) {
            String value = (String) this.keyDescriptions.get(key);
            res = new StringBuffer().append(res).append("\n      ").append(key).append(": ").append(value).toString();
        }
        return res;
    }
}
