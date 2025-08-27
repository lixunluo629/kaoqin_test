package ch.qos.logback.core.util;

import org.bouncycastle.i18n.TextBundle;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/util/ContentTypeUtil.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/util/ContentTypeUtil.class */
public class ContentTypeUtil {
    public static boolean isTextual(String contextType) {
        if (contextType == null) {
            return false;
        }
        return contextType.startsWith(TextBundle.TEXT_ENTRY);
    }

    public static String getSubType(String contextType) {
        int index;
        int subTypeStartIndex;
        if (contextType != null && (index = contextType.indexOf(47)) != -1 && (subTypeStartIndex = index + 1) < contextType.length()) {
            return contextType.substring(subTypeStartIndex);
        }
        return null;
    }
}
