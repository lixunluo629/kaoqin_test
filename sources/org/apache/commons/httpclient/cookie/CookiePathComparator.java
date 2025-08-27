package org.apache.commons.httpclient.cookie;

import java.util.Comparator;
import org.apache.commons.httpclient.Cookie;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookiePathComparator.class */
public class CookiePathComparator implements Comparator {
    private String normalizePath(Cookie cookie) {
        String path = cookie.getPath();
        if (path == null) {
            path = "/";
        }
        if (!path.endsWith("/")) {
            path = new StringBuffer().append(path).append("/").toString();
        }
        return path;
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        Cookie c1 = (Cookie) o1;
        Cookie c2 = (Cookie) o2;
        String path1 = normalizePath(c1);
        String path2 = normalizePath(c2);
        if (path1.equals(path2)) {
            return 0;
        }
        if (path1.startsWith(path2)) {
            return -1;
        }
        if (path2.startsWith(path1)) {
            return 1;
        }
        return 0;
    }
}
