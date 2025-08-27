package net.dongliu.apk.parser.cert.asn1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1Class.class */
public @interface Asn1Class {
    Asn1Type type();
}
