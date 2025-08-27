package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/ApkPaserUtils.class */
public class ApkPaserUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ApkPaserUtils.class);
    private static final int INI_SIZE = 5;

    public static Map parseUpgradeDescFile(String filePath) throws IOException {
        try {
            ZipFile zf = new ZipFile(filePath);
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            ZipInputStream zin = new ZipInputStream(in);
            Map<String, String> result = new HashMap<>(5);
            while (true) {
                ZipEntry ze = zin.getNextEntry();
                if (ze == null) {
                    break;
                }
                if (ze.toString().endsWith("ini")) {
                    MyFileUtils.parseForReturn(zf.getInputStream(ze), result);
                    break;
                }
            }
            zin.closeEntry();
            return result;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return Collections.emptyMap();
        }
    }

    public static Map parseUpgradeDescFile2(String filePath) {
        int index;
        try {
            ZipFile zf = new ZipFile(filePath);
            Map<String, String> result = new HashMap<>(5);
            String comment = zf.getComment();
            String[] comments = comment.split("\r\n");
            for (String line : comments) {
                if (line.trim().length() != 0 && !line.startsWith("#") && (index = line.indexOf(SymbolConstants.EQUAL_SYMBOL)) != -1) {
                    result.put(line.substring(0, index), line.substring(index + 1));
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
            return null;
        }
    }
}
