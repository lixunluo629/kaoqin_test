package javax.servlet.http;

/* compiled from: Cookie.java */
/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/RFC6265Validator.class */
class RFC6265Validator extends CookieNameValidator {
    private static final String RFC2616_SEPARATORS = "()<>@,;:\\\"/[]?={} \t";

    RFC6265Validator() {
        super(RFC2616_SEPARATORS);
    }
}
