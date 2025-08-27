package org.hyperic.sigar.cmd;

import java.util.ArrayList;
import org.hyperic.sigar.NetRoute;
import org.hyperic.sigar.SigarException;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/cmd/Route.class */
public class Route extends SigarCommandBase {
    private static final String OUTPUT_FORMAT = "%-15s %-15s %-15s %-5s %-6s %-3s %-s";
    private static final String[] HEADER = {"Destination", "Gateway", "Genmask", "Flags", "Metric", "Ref", "Iface"};

    public Route(Shell shell) {
        super(shell);
        setOutputFormat(OUTPUT_FORMAT);
    }

    public Route() {
        setOutputFormat(OUTPUT_FORMAT);
    }

    private static String flags(long flags) {
        StringBuffer sb = new StringBuffer();
        if ((flags & 1) != 0) {
            sb.append('U');
        }
        if ((flags & 2) != 0) {
            sb.append('G');
        }
        return sb.toString();
    }

    @Override // org.hyperic.sigar.shell.ShellCommandBase, org.hyperic.sigar.shell.ShellCommandHandler
    public String getUsageShort() {
        return "Kernel IP routing table";
    }

    @Override // org.hyperic.sigar.cmd.SigarCommandBase
    public void output(String[] args) throws SigarException {
        NetRoute[] routes = this.sigar.getNetRouteList();
        printf(HEADER);
        for (NetRoute route : routes) {
            ArrayList items = new ArrayList();
            items.add(route.getDestination());
            items.add(route.getGateway());
            items.add(route.getMask());
            items.add(flags(route.getFlags()));
            items.add(String.valueOf(route.getMetric()));
            items.add(String.valueOf(route.getRefcnt()));
            items.add(route.getIfname());
            printf(items);
        }
    }

    public static void main(String[] args) throws Exception {
        new Route().processCommand(args);
    }
}
