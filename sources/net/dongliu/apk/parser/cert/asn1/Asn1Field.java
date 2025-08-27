package net.dongliu.apk.parser.cert.asn1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/Asn1Field.class */
public @interface Asn1Field {
    int index() default 0;

    Asn1TagClass cls() default Asn1TagClass.AUTOMATIC;

    Asn1Type type();

    Asn1Tagging tagging() default Asn1Tagging.NORMAL;

    int tagNumber() default -1;

    boolean optional() default false;

    Asn1Type elementType() default Asn1Type.ANY;
}
