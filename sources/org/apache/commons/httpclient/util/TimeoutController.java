package org.apache.commons.httpclient.util;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/TimeoutController.class */
public final class TimeoutController {

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/TimeoutController$TimeoutException.class */
    public static class TimeoutException extends Exception {
    }

    private TimeoutController() {
    }

    public static void execute(Thread task, long timeout) throws InterruptedException, TimeoutException {
        task.start();
        try {
            task.join(timeout);
        } catch (InterruptedException e) {
        }
        if (task.isAlive()) {
            task.interrupt();
            throw new TimeoutException();
        }
    }

    public static void execute(Runnable task, long timeout) throws InterruptedException, TimeoutException {
        Thread t = new Thread(task, "Timeout guard");
        t.setDaemon(true);
        execute(t, timeout);
    }
}
