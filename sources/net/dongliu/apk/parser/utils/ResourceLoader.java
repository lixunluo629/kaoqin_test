package net.dongliu.apk.parser.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/ResourceLoader.class */
public class ResourceLoader {
    public static Map<Integer, String> loadSystemAttrIds() throws IOException {
        try {
            BufferedReader reader = toReader("/r_values.ini");
            Throwable th = null;
            try {
                Map<Integer, String> map = new HashMap<>();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] items = line.trim().split(SymbolConstants.EQUAL_SYMBOL);
                    if (items.length == 2) {
                        String name = items[0].trim();
                        Integer id = Integer.valueOf(items[1].trim());
                        map.put(id, name);
                    }
                }
                return map;
            } finally {
                if (reader != null) {
                    if (0 != 0) {
                        try {
                            reader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        reader.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Integer, String> loadSystemStyles() throws IOException {
        Map<Integer, String> map = new HashMap<>();
        try {
            BufferedReader reader = toReader("/r_styles.ini");
            Throwable th = null;
            while (true) {
                try {
                    try {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] items = line.trim().split(SymbolConstants.EQUAL_SYMBOL);
                        if (items.length == 2) {
                            Integer id = Integer.valueOf(items[1].trim());
                            String name = items[0].trim();
                            map.put(id, name);
                        }
                    } finally {
                    }
                } finally {
                }
            }
            if (reader != null) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    reader.close();
                }
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader toReader(String path) {
        return new BufferedReader(new InputStreamReader(ResourceLoader.class.getResourceAsStream(path)));
    }
}
