package org.hyperic.sigar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileWatcherThread.class */
public class FileWatcherThread implements Runnable {
    public static final int DEFAULT_INTERVAL = 300000;
    private static FileWatcherThread instance = null;
    private Thread thread = null;
    private boolean shouldDie = false;
    private long interval = 300000;
    private Set watchers = Collections.synchronizedSet(new HashSet());

    public static synchronized FileWatcherThread getInstance() {
        if (instance == null) {
            instance = new FileWatcherThread();
        }
        return instance;
    }

    public synchronized void doStart() {
        if (this.thread != null) {
            return;
        }
        this.thread = new Thread(this, "FileWatcherThread");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public synchronized void doStop() {
        if (this.thread == null) {
            return;
        }
        die();
        this.thread.interrupt();
        this.thread = null;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return this.interval;
    }

    public void add(FileWatcher watcher) {
        this.watchers.add(watcher);
    }

    public void remove(FileWatcher watcher) {
        this.watchers.remove(watcher);
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException {
        while (!this.shouldDie) {
            check();
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
            }
        }
    }

    public void die() {
        this.shouldDie = true;
    }

    public void check() {
        synchronized (this.watchers) {
            for (FileWatcher watcher : this.watchers) {
                try {
                    watcher.check();
                } catch (Exception e) {
                    FileTail.error(new StringBuffer().append("Unexpected exception: ").append(e.getMessage()).toString(), e);
                }
            }
        }
    }
}
