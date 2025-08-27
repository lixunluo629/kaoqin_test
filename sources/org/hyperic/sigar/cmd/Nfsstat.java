package org.hyperic.sigar.cmd;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.reflect.Method;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.naming.EjbRef;
import org.hyperic.sigar.SigarException;
import redis.clients.jedis.Protocol;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Nfsstat.class */
public class Nfsstat extends SigarCommandBase {
    public Nfsstat(Shell shell) {
        super(shell);
    }

    public Nfsstat() {
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    protected boolean validateArgs(String[] args) {
        return true;
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Display nfs stats";
    }

    private String getValue(Object obj, String attr) throws NoSuchMethodException, SecurityException {
        if (attr == "") {
            return "";
        }
        String name = new StringBuffer().append(BeanUtil.PREFIX_GETTER_GET).append(Character.toUpperCase(attr.charAt(0))).append(attr.substring(1)).toString();
        try {
            Method method = obj.getClass().getMethod(name, new Class[0]);
            return method.invoke(obj, new Object[0]).toString();
        } catch (Exception e) {
            return "EINVAL";
        }
    }

    private void printnfs(Object nfs, String[] names) {
        String[] values = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            values[i] = getValue(nfs, names[i]);
        }
        printf(names);
        printf(values);
    }

    private void outputNfsV2(String header, Object nfs) {
        println(new StringBuffer().append(header).append(":").toString());
        printnfs(nfs, new String[]{"null", "getattr", "setattr", OgnlContext.ROOT_CONTEXT_KEY, "lookup", "readlink"});
        printnfs(nfs, new String[]{"read", "writecache", "write", "create", Protocol.SENTINEL_REMOVE, "rename"});
        printnfs(nfs, new String[]{EjbRef.LINK, "symlink", "mkdir", "rmdir", "readdir", "fsstat"});
        println("");
        flush();
    }

    private void outputNfsV3(String header, Object nfs) {
        println(new StringBuffer().append(header).append(":").toString());
        printnfs(nfs, new String[]{"null", "getattr", "setattr", "lookup", "access", "readlink"});
        printnfs(nfs, new String[]{"read", "write", "create", "mkdir", "symlink", "mknod"});
        printnfs(nfs, new String[]{Protocol.SENTINEL_REMOVE, "rmdir", "rename", EjbRef.LINK, "readdir", "readdirplus"});
        printnfs(nfs, new String[]{"fsstat", "fsinfo", "pathconf", "commit", "", ""});
        println("");
        flush();
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        try {
            outputNfsV2("Client nfs v2", this.sigar.getNfsClientV2());
        } catch (SigarException e) {
        }
        try {
            outputNfsV2("Server nfs v2", this.sigar.getNfsServerV2());
        } catch (SigarException e2) {
        }
        try {
            outputNfsV3("Client nfs v3", this.sigar.getNfsClientV3());
        } catch (SigarException e3) {
        }
        try {
            outputNfsV3("Server nfs v3", this.sigar.getNfsServerV3());
        } catch (SigarException e4) {
        }
    }
}
