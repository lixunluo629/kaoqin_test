package org.hyperic.sigar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/RPC.class */
public class RPC {
    private static Map programs = null;
    public static final int UDP = 32;
    public static final int TCP = 16;

    public static native int ping(String str, int i, long j, long j2);

    public static native String strerror(int i);

    public static int ping(String hostname, int protocol, String program, long version) {
        return ping(hostname, protocol, getProgram(program), version);
    }

    public static int ping(String hostname, long program) {
        return ping(hostname, 32, program, 2L);
    }

    public static int ping(String hostname, String program) {
        return ping(hostname, 32, program, 2L);
    }

    private static void parse(String fileName) throws IOException {
        programs = new HashMap();
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String line2 = line.trim();
                if (line2.length() != 0 && line2.charAt(0) != '#') {
                    StringTokenizer st = new StringTokenizer(line2, " \t");
                    if (st.countTokens() >= 2) {
                        String name = st.nextToken().trim();
                        String num = st.nextToken().trim();
                        programs.put(name, num);
                    }
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    public static long getProgram(String program) throws IOException, NumberFormatException {
        Long num;
        if (programs == null) {
            parse("/etc/rpc");
        }
        Object obj = programs.get(program);
        if (obj == null) {
            return -1L;
        }
        if (obj instanceof Long) {
            num = (Long) obj;
        } else {
            num = Long.valueOf((String) obj);
            programs.put(program, num);
        }
        return num.longValue();
    }

    public static void main(String[] args) throws Exception {
        Sigar.load();
        int retval = ping(args[0], args[1]);
        System.out.println(new StringBuffer().append("(").append(retval).append(") ").append(strerror(retval)).toString());
    }
}
