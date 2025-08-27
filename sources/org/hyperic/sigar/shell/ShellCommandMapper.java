package org.hyperic.sigar.shell;

import java.util.Iterator;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellCommandMapper.class */
public interface ShellCommandMapper {
    ShellCommandHandler getHandler(String str);

    Iterator getCommandNameIterator();
}
