package org.hyperic.sigar.shell;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.HashMap;
import java.util.Iterator;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommand_alias.class */
public class ShellCommand_alias extends ShellCommandBase {
    private static HashMap aliases = new HashMap();

    public static String[] getAlias(String alias) {
        return (String[]) aliases.get(alias);
    }

    public static Iterator getAliases() {
        return aliases.keySet().iterator();
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public void processCommand(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        if (args.length < 2) {
            throw new ShellCommandUsageException(getSyntax());
        }
        int aliasArgsLen = args.length - 1;
        String[] aliasArgs = new String[aliasArgsLen];
        System.arraycopy(args, 1, aliasArgs, 0, aliasArgsLen);
        aliases.put(args[0], aliasArgs);
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase
    public String getSyntaxArgs() {
        return "<alias> <command>";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Create alias command";
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageHelp(String[] args) {
        if (aliases.size() == 0) {
            return "No aliases defined";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("Defined aliases:\n");
        for (String key : aliases.keySet()) {
            String[] cmd = getAlias(key);
            sb.append(key).append(" => ");
            for (String str : cmd) {
                sb.append(str).append(SymbolConstants.SPACE_SYMBOL);
            }
            sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return sb.toString();
    }
}
