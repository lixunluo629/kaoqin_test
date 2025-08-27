package org.apache.catalina.ssi;

import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.RequestUtil;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ssi/SSIServletRequestUtil.class */
public class SSIServletRequestUtil {
    public static String getRelativePath(HttpServletRequest request) {
        if (request.getAttribute("javax.servlet.include.request_uri") != null) {
            String result = (String) request.getAttribute("javax.servlet.include.path_info");
            if (result == null) {
                result = (String) request.getAttribute("javax.servlet.include.servlet_path");
            }
            if (result == null || result.equals("")) {
                result = "/";
            }
            return result;
        }
        String result2 = request.getPathInfo();
        if (result2 == null) {
            result2 = request.getServletPath();
        }
        if (result2 == null || result2.equals("")) {
            result2 = "/";
        }
        return RequestUtil.normalize(result2);
    }
}
