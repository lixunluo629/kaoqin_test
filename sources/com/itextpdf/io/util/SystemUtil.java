package com.itextpdf.io.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/SystemUtil.class */
public final class SystemUtil {
    @Deprecated
    public static long getSystemTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getTimeBasedSeed() {
        return System.currentTimeMillis();
    }

    public static int getTimeBasedIntSeed() {
        return (int) System.currentTimeMillis();
    }

    public static long getRelativeTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static String getPropertyOrEnvironmentVariable(String name) {
        String s = System.getProperty(name);
        if (s == null) {
            s = System.getenv(name);
        }
        return s;
    }

    public static boolean runProcessAndWait(String execPath, String params) throws InterruptedException, IOException {
        List<String> cmdArray = new ArrayList<>();
        cmdArray.add(execPath);
        Matcher m = Pattern.compile("((?:[^'\\s]|'.+?')+)\\s*").matcher(params);
        while (m.find()) {
            cmdArray.add(m.group(1).replace("'", ""));
        }
        Process p = Runtime.getRuntime().exec((String[]) cmdArray.toArray(new String[cmdArray.size()]));
        printProcessOutput(p);
        return p.waitFor() == 0;
    }

    private static void printProcessOutput(Process p) throws IOException {
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while (true) {
            String line = bri.readLine();
            if (line == null) {
                break;
            } else {
                System.out.println(line);
            }
        }
        bri.close();
        while (true) {
            String line2 = bre.readLine();
            if (line2 != null) {
                System.out.println(line2);
            } else {
                bre.close();
                return;
            }
        }
    }

    public static StringBuilder runProcessAndCollectErrors(String execPath, String params) throws IOException {
        List<String> cmdArray = new ArrayList<>();
        cmdArray.add(execPath);
        Matcher m = Pattern.compile("((?:[^'\\s]|'.+?')+)\\s*").matcher(params);
        while (m.find()) {
            cmdArray.add(m.group(1).replace("'", ""));
        }
        Process p = Runtime.getRuntime().exec((String[]) cmdArray.toArray(new String[cmdArray.size()]));
        StringBuilder errorsBuilder = printProcessErrorsOutput(p);
        return errorsBuilder;
    }

    private static StringBuilder printProcessErrorsOutput(Process p) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while (true) {
            String line = bre.readLine();
            if (line != null) {
                System.out.println(line);
                builder.append(line);
            } else {
                bre.close();
                return builder;
            }
        }
    }
}
