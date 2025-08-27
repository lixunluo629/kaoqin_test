package org.hyperic.sigar.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tomcat.jni.Time;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ThreadCpu;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/RunThreadCpu.class */
public class RunThreadCpu {
    static Sigar sigar = new Sigar();
    static ThreadCpu cpu = new ThreadCpu();
    static int iter = 5000;

    private static long toMillis(long nano) {
        return nano / Time.APR_USEC_PER_SEC;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void printTimes(long start) {
        try {
            cpu.gather(sigar, 0L);
            System.out.println(new StringBuffer().append(Thread.currentThread().getName()).append(":").toString());
            System.out.println(new StringBuffer().append("   real.....").append((System.currentTimeMillis() - start) / 1000).toString());
            System.out.println(new StringBuffer().append("   sys......").append(toMillis(cpu.getSys())).toString());
            System.out.println(new StringBuffer().append("   user.....").append(toMillis(cpu.getUser())).toString());
            System.out.println(new StringBuffer().append("   total....").append(toMillis(cpu.getTotal())).toString());
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void pause(int sec) throws InterruptedException {
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/RunThreadCpu$RealThread.class */
    static class RealThread implements Runnable {
        RealThread() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            long start = System.currentTimeMillis();
            RunThreadCpu.pause(2);
            RunThreadCpu.printTimes(start);
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/RunThreadCpu$UserThread.class */
    static class UserThread implements Runnable {
        UserThread() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            long start = System.currentTimeMillis();
            RunThreadCpu.pause(2);
            String s = "";
            for (int i = 0; i < RunThreadCpu.iter; i++) {
                s = new StringBuffer().append(s).append(System.getProperty("java.home")).toString();
                for (int j = 0; j < s.length(); j++) {
                    s.charAt(j);
                }
            }
            RunThreadCpu.printTimes(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readRtJar() throws IOException {
        String rt = new StringBuffer().append(System.getProperty("java.home")).append("/lib/rt.jar").toString();
        int bytes = 0;
        FileInputStream is = null;
        try {
            try {
                is = new FileInputStream(new File(rt));
                while (is.read() != -1) {
                    int i = bytes;
                    bytes++;
                    if (i > 1500000) {
                        break;
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e3) {
                    }
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scanDir() {
        for (int i = 0; i < iter; i++) {
            String[] files = new File(".").list();
            for (String str : files) {
                File f = new File(str);
                f.exists();
                if (f.isDirectory()) {
                    f.list();
                }
                f.lastModified();
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/RunThreadCpu$ReadThread.class */
    static class ReadThread implements Runnable {
        ReadThread() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException, IOException {
            long start = System.currentTimeMillis();
            RunThreadCpu.pause(2);
            RunThreadCpu.readRtJar();
            RunThreadCpu.printTimes(start);
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/RunThreadCpu$ScanThread.class */
    static class ScanThread implements Runnable {
        ScanThread() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            long start = System.currentTimeMillis();
            RunThreadCpu.pause(2);
            RunThreadCpu.scanDir();
            RunThreadCpu.printTimes(start);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            iter = Integer.parseInt(args[0]);
        }
        long start = System.currentTimeMillis();
        Thread user = new Thread(new UserThread(), "user");
        Thread read = new Thread(new ReadThread(), "read");
        Thread scan = new Thread(new ScanThread(), "scan");
        Thread real = new Thread(new RealThread(), "real");
        user.start();
        read.start();
        scan.start();
        real.start();
        user.join();
        read.join();
        scan.join();
        real.join();
        pause(3);
        printTimes(start);
    }
}
