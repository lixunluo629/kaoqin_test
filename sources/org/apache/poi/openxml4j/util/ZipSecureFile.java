package org.apache.poi.openxml4j.util;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.SuppressForbidden;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipSecureFile.class */
public class ZipSecureFile extends ZipFile {
    private static final long GRACE_ENTRY_SIZE = 102400;
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) ZipSecureFile.class);
    private static double MIN_INFLATE_RATIO = 0.01d;
    private static long MAX_ENTRY_SIZE = 4294967295L;
    private static long MAX_TEXT_SIZE = SizeBasedTriggeringPolicy.DEFAULT_MAX_FILE_SIZE;

    public static void setMinInflateRatio(double ratio) {
        MIN_INFLATE_RATIO = ratio;
    }

    public static double getMinInflateRatio() {
        return MIN_INFLATE_RATIO;
    }

    public static void setMaxEntrySize(long maxEntrySize) {
        if (maxEntrySize < 0 || maxEntrySize > 4294967295L) {
            throw new IllegalArgumentException("Max entry size is bounded [0-4GB], but had " + maxEntrySize);
        }
        MAX_ENTRY_SIZE = maxEntrySize;
    }

    public static long getMaxEntrySize() {
        return MAX_ENTRY_SIZE;
    }

    public static void setMaxTextSize(long maxTextSize) {
        if (maxTextSize < 0 || maxTextSize > 4294967295L) {
            throw new IllegalArgumentException("Max text size is bounded [0-4GB], but had " + maxTextSize);
        }
        MAX_TEXT_SIZE = maxTextSize;
    }

    public static long getMaxTextSize() {
        return MAX_TEXT_SIZE;
    }

    public ZipSecureFile(File file, int mode) throws IOException {
        super(file, mode);
    }

    public ZipSecureFile(File file) throws IOException {
        super(file);
    }

    public ZipSecureFile(String name) throws IOException {
        super(name);
    }

    @Override // java.util.zip.ZipFile
    public InputStream getInputStream(ZipEntry entry) throws IOException {
        InputStream zipIS = super.getInputStream(entry);
        return addThreshold(zipIS);
    }

    public static ThresholdInputStream addThreshold(final InputStream zipIS) throws IOException {
        ThresholdInputStream newInner;
        if (zipIS instanceof InflaterInputStream) {
            newInner = (ThresholdInputStream) AccessController.doPrivileged(new PrivilegedAction<ThresholdInputStream>() { // from class: org.apache.poi.openxml4j.util.ZipSecureFile.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressForbidden("TODO: Fix this to not use reflection (it will break in Java 9)! Better would be to wrap *before* instead of trying to insert wrapper afterwards.")
                public ThresholdInputStream run() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
                    try {
                        Field f = FilterInputStream.class.getDeclaredField("in");
                        f.setAccessible(true);
                        InputStream oldInner = (InputStream) f.get(zipIS);
                        ThresholdInputStream newInner2 = new ThresholdInputStream(oldInner, null);
                        f.set(zipIS, newInner2);
                        return newInner2;
                    } catch (Exception ex) {
                        ZipSecureFile.LOG.log(5, "SecurityManager doesn't allow manipulation via reflection for zipbomb detection - continue with original input stream", ex);
                        return null;
                    }
                }
            });
        } else {
            newInner = null;
        }
        return new ThresholdInputStream(zipIS, newInner);
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/util/ZipSecureFile$ThresholdInputStream.class */
    public static class ThresholdInputStream extends PushbackInputStream {
        long counter;
        long markPos;
        ThresholdInputStream cis;

        /*  JADX ERROR: Failed to decode insn: 0x001B: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.io.PushbackInputStream
        public void unread(int r7) throws java.io.IOException {
            /*
                r6 = this;
                r0 = r6
                java.io.InputStream r0 = r0.in
                boolean r0 = r0 instanceof java.io.PushbackInputStream
                if (r0 != 0) goto L14
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                r1 = r0
                java.lang.String r2 = "underlying stream is not a PushbackInputStream"
                r1.<init>(r2)
                throw r0
                r0 = r6
                r1 = r0
                long r1 = r1.counter
                r2 = 1
                long r1 = r1 - r2
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.counter = r1
                r0 = 0
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 >= 0) goto L29
                r-1 = r6
                r0 = 0
                r-1.counter = r0
                r-1 = r6
                java.io.InputStream r-1 = r-1.in
                java.io.PushbackInputStream r-1 = (java.io.PushbackInputStream) r-1
                r0 = r7
                r-1.unread(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream.unread(int):void");
        }

        /*  JADX ERROR: Failed to decode insn: 0x0026: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.io.PushbackInputStream
        public void unread(byte[] r7, int r8, int r9) throws java.io.IOException {
            /*
                r6 = this;
                r0 = r6
                java.io.InputStream r0 = r0.in
                boolean r0 = r0 instanceof java.io.PushbackInputStream
                if (r0 != 0) goto L14
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                r1 = r0
                java.lang.String r2 = "underlying stream is not a PushbackInputStream"
                r1.<init>(r2)
                throw r0
                r0 = r6
                r1 = r0
                long r1 = r1.counter
                r2 = r9
                long r2 = (long) r2
                long r1 = r1 - r2
                r0.counter = r1
                r0 = r6
                r1 = r0
                long r1 = r1.counter
                r2 = 1
                long r1 = r1 - r2
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.counter = r1
                r0 = 0
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 >= 0) goto L34
                r-1 = r6
                r0 = 0
                r-1.counter = r0
                r-1 = r6
                java.io.InputStream r-1 = r-1.in
                java.io.PushbackInputStream r-1 = (java.io.PushbackInputStream) r-1
                r0 = r7
                r1 = r8
                r2 = r9
                r-1.unread(r0, r1, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream.unread(byte[], int, int):void");
        }

        public ThresholdInputStream(InputStream is, ThresholdInputStream cis) {
            super(is);
            this.counter = 0L;
            this.markPos = 0L;
            this.cis = cis;
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int b = this.in.read();
            if (b > -1) {
                advance(1);
            }
            return b;
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            int cnt = this.in.read(b, off, len);
            if (cnt > -1) {
                advance(cnt);
            }
            return cnt;
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public long skip(long n) throws IOException {
            long s = this.in.skip(n);
            this.counter += s;
            return s;
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public synchronized void reset() throws IOException {
            this.counter = this.markPos;
            super.reset();
        }

        public void advance(int advance) throws IOException {
            this.counter += advance;
            if (this.counter > ZipSecureFile.MAX_ENTRY_SIZE) {
                throw new IOException("Zip bomb detected! The file would exceed the max size of the expanded data in the zip-file. This may indicates that the file is used to inflate memory usage and thus could pose a security risk. You can adjust this limit via ZipSecureFile.setMaxEntrySize() if you need to work with files which are very large. Counter: " + this.counter + ", cis.counter: " + (this.cis == null ? 0L : this.cis.counter) + "Limits: MAX_ENTRY_SIZE: " + ZipSecureFile.MAX_ENTRY_SIZE);
            }
            if (this.cis == null || this.counter <= ZipSecureFile.GRACE_ENTRY_SIZE) {
                return;
            }
            double ratio = this.cis.counter / this.counter;
            if (ratio >= ZipSecureFile.MIN_INFLATE_RATIO) {
            } else {
                throw new IOException("Zip bomb detected! The file would exceed the max. ratio of compressed file size to the size of the expanded data.\nThis may indicate that the file is used to inflate memory usage and thus could pose a security risk.\nYou can adjust this limit via ZipSecureFile.setMinInflateRatio() if you need to work with files which exceed this limit.\nCounter: " + this.counter + ", cis.counter: " + this.cis.counter + ", ratio: " + ratio + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "Limits: MIN_INFLATE_RATIO: " + ZipSecureFile.MIN_INFLATE_RATIO);
            }
        }

        public ZipEntry getNextEntry() throws IOException {
            if (!(this.in instanceof ZipInputStream)) {
                throw new UnsupportedOperationException("underlying stream is not a ZipInputStream");
            }
            this.counter = 0L;
            return ((ZipInputStream) this.in).getNextEntry();
        }

        public void closeEntry() throws IOException {
            if (!(this.in instanceof ZipInputStream)) {
                throw new UnsupportedOperationException("underlying stream is not a ZipInputStream");
            }
            this.counter = 0L;
            ((ZipInputStream) this.in).closeEntry();
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        @SuppressForbidden("just delegating")
        public int available() throws IOException {
            return this.in.available();
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public boolean markSupported() {
            return this.in.markSupported();
        }

        @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
        public synchronized void mark(int readlimit) {
            this.markPos = this.counter;
            this.in.mark(readlimit);
        }
    }
}
