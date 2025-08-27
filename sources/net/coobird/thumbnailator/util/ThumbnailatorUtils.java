package net.coobird.thumbnailator.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import net.coobird.thumbnailator.ThumbnailParameter;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/util/ThumbnailatorUtils.class */
public final class ThumbnailatorUtils {
    private ThumbnailatorUtils() {
    }

    public static List<String> getSupportedOutputFormats() {
        String[] writerFormatNames = ImageIO.getWriterFormatNames();
        if (writerFormatNames == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(writerFormatNames);
    }

    public static boolean isSupportedOutputFormat(String str) {
        if (str == ThumbnailParameter.ORIGINAL_FORMAT) {
            return true;
        }
        Iterator<String> it = getSupportedOutputFormats().iterator();
        while (it.hasNext()) {
            if (it.next().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getSupportedOutputFormatTypes(String str) {
        if (str == ThumbnailParameter.ORIGINAL_FORMAT) {
            return Collections.emptyList();
        }
        Iterator imageWritersByFormatName = ImageIO.getImageWritersByFormatName(str);
        if (!imageWritersByFormatName.hasNext()) {
            return Collections.emptyList();
        }
        try {
            String[] compressionTypes = ((ImageWriter) imageWritersByFormatName.next()).getDefaultWriteParam().getCompressionTypes();
            if (compressionTypes == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(compressionTypes);
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    public static boolean isSupportedOutputFormatType(String str, String str2) {
        if (!isSupportedOutputFormat(str)) {
            return false;
        }
        if (str == ThumbnailParameter.ORIGINAL_FORMAT && str2 == ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
            return true;
        }
        if (str == ThumbnailParameter.ORIGINAL_FORMAT && str2 != ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
            return false;
        }
        if (str2 == ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
            return true;
        }
        Iterator<String> it = getSupportedOutputFormatTypes(str).iterator();
        while (it.hasNext()) {
            if (it.next().equals(str2)) {
                return true;
            }
        }
        return false;
    }
}
