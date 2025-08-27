package org.hyperic.sigar.shell;

import java.util.Stack;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/shell/ShellIntHandler.class */
public class ShellIntHandler implements SignalHandler {
    private static ShellBase handlerShell;
    private static Stack handlers;

    public static void register(ShellBase shell) {
        handlerShell = shell;
        handlers = new Stack();
        try {
            Signal signal = new Signal("INT");
            try {
                Signal.handle(signal, new ShellIntHandler());
            } catch (Exception e) {
            }
        } catch (IllegalArgumentException e2) {
        }
    }

    public void handle(Signal signal) {
        if (handlers.empty()) {
            handlerShell.shutdown();
            Runtime.getRuntime().halt(0);
        } else {
            SIGINT handler = (SIGINT) handlers.peek();
            handler.handleSIGINT();
        }
    }

    public static void push(SIGINT handler) {
        handlers.push(handler);
    }

    public static void pop() {
        handlers.pop();
    }
}
