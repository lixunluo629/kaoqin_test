package org.hyperic.sigar.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarInvoker;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.springframework.beans.PropertyAccessor;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy.class */
public class Proxy {
    private static final String HOME = System.getProperty("user.home");
    private static boolean sameArg = true;
    private String ourPid;
    private Sigar sigar;
    private SigarProxy proxy;
    private PidList pids;
    private NetifList netif;
    private FsList fs;
    static Class class$java$lang$String;
    static Class class$org$hyperic$sigar$SigarProxy;
    private boolean pause = false;
    private boolean verbose = true;
    private boolean leakVerbose = false;
    private boolean fukksor = false;
    private boolean useReflection = false;
    private PrintStream out = System.out;
    private long lastChange = 0;
    private long startSize = 0;
    private long currentSize = 0;
    private DirList dirs = new DirList(HOME);
    private FileList files = new FileList(HOME);

    /* renamed from: org.hyperic.sigar.test.Proxy$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$1.class */
    static class AnonymousClass1 {
    }

    public Proxy(Sigar sigar, SigarProxy proxy) {
        this.sigar = sigar;
        this.proxy = proxy;
        this.pids = new PidList(sigar);
        this.netif = new NetifList(sigar);
        this.fs = new FsList(sigar);
    }

    public void setOutputStream(PrintStream out) {
        this.out = out;
    }

    public void setVerbose(boolean value) {
        this.verbose = value;
    }

    public void setLeakVerbose(boolean value) {
        this.leakVerbose = value;
    }

    private void output() {
        this.out.println();
    }

    private void output(String s) {
        String name = Thread.currentThread().getName();
        this.out.println(new StringBuffer().append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(name).append("] ").append(s).toString());
    }

    private long getSize() throws SigarException {
        return this.sigar.getProcMem(this.ourPid).getResident();
    }

    private boolean memstat(long i) throws SigarException {
        long size = getSize();
        if (this.currentSize != size) {
            long diff = size - this.currentSize;
            long iters = i - this.lastChange;
            String changed = new StringBuffer().append(" (change=").append(diff).append(", iters=").append(iters).append(")").toString();
            output(new StringBuffer().append(i).append(") size=").append(size).append(changed).toString());
            this.currentSize = size;
            this.lastChange = i;
            return true;
        }
        return false;
    }

    private void trace(String msg) {
        if (this.verbose) {
            output(msg);
        }
    }

    private boolean isNonStringArg(Method method) throws Throwable {
        Class clsClass$;
        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length < 1) {
            return false;
        }
        Class cls = paramTypes[0];
        if (class$java$lang$String == null) {
            clsClass$ = class$("java.lang.String");
            class$java$lang$String = clsClass$;
        } else {
            clsClass$ = class$java$lang$String;
        }
        if (cls != clsClass$) {
            return true;
        }
        return false;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private String argsToString(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('(').append(args[0].toString());
        for (int i = 1; i < args.length; i++) {
            sb.append(',').append(args[i].toString());
        }
        sb.append(')');
        return sb.toString();
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$ArgList.class */
    private static abstract class ArgList {
        String[] values;
        int ix;

        private ArgList() {
            this.ix = 0;
        }

        ArgList(AnonymousClass1 x0) {
            this();
        }

        public String get() {
            if (this.ix == this.values.length) {
                this.ix = 0;
            }
            String[] strArr = this.values;
            int i = this.ix;
            this.ix = i + 1;
            return strArr[i];
        }

        public String getName(int iter) {
            if (iter == 0 || Proxy.sameArg) {
                return this.values[0];
            }
            return get();
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$PidList.class */
    private static class PidList extends ArgList {
        public PidList(Sigar sigar) {
            super(null);
            try {
                long[] pids = sigar.getProcList();
                this.values = new String[pids.length + 1];
                this.values[0] = String.valueOf(sigar.getPid());
                for (int i = 0; i < pids.length; i++) {
                    this.values[i + 1] = String.valueOf(pids[i]);
                }
            } catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$NetifList.class */
    private static class NetifList extends ArgList {
        public NetifList(Sigar sigar) {
            super(null);
            try {
                this.values = sigar.getNetInterfaceList();
            } catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$DirList.class */
    private static class DirList extends ArgList {
        public DirList(String dir) {
            super(null);
            File[] dirs = new File(dir).listFiles(new FileFilter(this) { // from class: org.hyperic.sigar.test.Proxy.DirList.1
                private final DirList this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.FileFilter
                public boolean accept(File f) {
                    return f.isDirectory();
                }
            });
            this.values = new String[dirs.length];
            for (int i = 0; i < dirs.length; i++) {
                this.values[i] = dirs[i].getAbsolutePath();
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$FileList.class */
    private static class FileList extends ArgList {
        public FileList(String dir) {
            super(null);
            File[] files = new File(dir).listFiles(new FileFilter(this) { // from class: org.hyperic.sigar.test.Proxy.FileList.1
                private final FileList this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.FileFilter
                public boolean accept(File f) {
                    return !f.isDirectory();
                }
            });
            this.values = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                this.values[i] = files[i].getAbsolutePath();
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/test/Proxy$FsList.class */
    private static class FsList extends ArgList {
        public FsList(Sigar sigar) {
            super(null);
            try {
                FileSystem[] fs = sigar.getFileSystemList();
                this.values = new String[fs.length];
                for (int i = 0; i < fs.length; i++) {
                    this.values[i] = fs[i].getDirName();
                }
            } catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:95)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:95)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:124)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private void runall(int r7) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 904
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hyperic.sigar.test.Proxy.runall(int):void");
    }

    private void pause() throws IOException {
        output("hit enter to continue");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    private Object invoke(SigarInvoker invoker, Object[] args, String attr) {
        String type = invoker.getType();
        if (this.fukksor && args.length != 0 && (args[0] instanceof String)) {
            if (type.startsWith("Proc")) {
                args[0] = new String("666666");
            } else {
                args[0] = new String("bogus");
            }
        }
        if (args.length == 0) {
            args = null;
        }
        try {
            return invoker.invoke(args, attr);
        } catch (SigarException e) {
            String msg = new StringBuffer().append(type).append(" failed: ").append(e.getMessage()).toString();
            System.err.println(msg);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        SigarProxy proxy = SigarProxyCache.newInstance(sigar, 30000);
        new Proxy(sigar, proxy).run(args);
    }

    public void run(String[] args) throws Throwable {
        this.ourPid = String.valueOf(this.sigar.getPid());
        output(new StringBuffer().append("ourPid=").append(this.ourPid).toString());
        if (args.length >= 2) {
            String type = args[0];
            String attr = args[args.length - 1];
            if (type.equals("leaktest")) {
                int num = Integer.parseInt(args[1]);
                this.verbose = this.leakVerbose;
                long size = getSize();
                this.currentSize = size;
                this.startSize = size;
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < num; i++) {
                    Sigar s = new Sigar();
                    try {
                        try {
                            runall(i);
                            s.close();
                        } catch (IllegalAccessException e) {
                            throw new SigarException(e.getMessage());
                        } catch (InvocationTargetException e2) {
                            throw new SigarException(e2.getMessage());
                        }
                    } catch (Throwable th) {
                        s.close();
                        throw th;
                    }
                }
                long totalTime = System.currentTimeMillis() - startTime;
                this.proxy = null;
                output("Running garbage collector..");
                System.gc();
                memstat(this.lastChange + 1);
                output(new StringBuffer().append(num).append(" iterations took ").append(totalTime).append("ms").toString());
                output(new StringBuffer().append("startSize=").append(this.startSize).append(", endSize=").append(this.currentSize).append(", diff=").append(this.currentSize - this.startSize).toString());
                return;
            }
            Object obj = invoke(new SigarInvoker(this.proxy, type), null, attr);
            output(obj.toString());
            return;
        }
        try {
            runall(0);
        } catch (IllegalAccessException e3) {
            throw new SigarException(e3.getMessage());
        } catch (InvocationTargetException e4) {
            throw new SigarException(e4.getMessage());
        }
    }
}
