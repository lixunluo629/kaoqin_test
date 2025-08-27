package javax.servlet.http;

import java.text.MessageFormat;

/* compiled from: Cookie.java */
/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/RFC2109Validator.class */
class RFC2109Validator extends RFC6265Validator {
    RFC2109Validator() {
        boolean allowSlash;
        String prop = System.getProperty("org.apache.tomcat.util.http.ServerCookie.FWD_SLASH_IS_SEPARATOR");
        if (prop != null) {
            allowSlash = !Boolean.parseBoolean(prop);
        } else {
            allowSlash = !Boolean.getBoolean("org.apache.catalina.STRICT_SERVLET_COMPLIANCE");
        }
        if (allowSlash) {
            this.allowed.set(47);
        }
    }

    @Override // javax.servlet.http.CookieNameValidator
    void validate(String name) {
        super.validate(name);
        if (name.charAt(0) == '$') {
            String errMsg = lStrings.getString("err.cookie_name_is_token");
            throw new IllegalArgumentException(MessageFormat.format(errMsg, name));
        }
    }
}
