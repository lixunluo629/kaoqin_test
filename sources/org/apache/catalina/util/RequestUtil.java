package org.apache.catalina.util;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/RequestUtil.class */
public final class RequestUtil {
    @Deprecated
    public static String filter(String message) {
        if (message == null) {
            return null;
        }
        char[] content = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '\"':
                    result.append("&quot;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                default:
                    result.append(content[i]);
                    break;
            }
        }
        return result.toString();
    }
}
