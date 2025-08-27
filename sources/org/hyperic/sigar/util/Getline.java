package org.hyperic.sigar.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/Getline.class */
public class Getline {
    private static final boolean isatty = isatty();
    private static boolean useNative;
    private BufferedReader in;
    private String prompt;

    private static native boolean isatty();

    public static native void setCompleter(GetlineCompleter getlineCompleter);

    public native void redraw();

    public native void reset();

    private native void histadd(String str);

    private native void histinit(String str);

    private native String getline(String str) throws IOException;

    static {
        useNative = !"false".equals(System.getProperty("sigar.getline.native")) && isatty;
    }

    public Getline() {
        this.in = null;
        this.prompt = "> ";
    }

    public Getline(String prompt) {
        this.in = null;
        this.prompt = "> ";
        this.prompt = prompt;
    }

    public static boolean isTTY() {
        return isatty;
    }

    public String getLine() throws IOException {
        return getLine(this.prompt, true);
    }

    public String getLine(String prompt) throws IOException {
        return getLine(prompt, true);
    }

    public String getLine(String prompt, boolean addToHistory) throws IOException {
        if (useNative) {
            String line = getline(prompt);
            if (addToHistory) {
                addToHistory(line);
            }
            return line;
        }
        if (this.in == null) {
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }
        System.out.print(prompt);
        return this.in.readLine();
    }

    public void initHistoryFile(File file) throws IOException {
        if (useNative) {
            histinit(file.getCanonicalPath());
        }
    }

    public void addToHistory(String line) {
        if (line != null && line.length() != 0 && useNative) {
            histadd(line);
        }
    }
}
