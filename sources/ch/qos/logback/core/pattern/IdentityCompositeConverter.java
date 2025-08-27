package ch.qos.logback.core.pattern;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/IdentityCompositeConverter.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/IdentityCompositeConverter.class */
public class IdentityCompositeConverter<E> extends CompositeConverter<E> {
    @Override // ch.qos.logback.core.pattern.CompositeConverter
    protected String transform(E event, String in) {
        return in;
    }
}
