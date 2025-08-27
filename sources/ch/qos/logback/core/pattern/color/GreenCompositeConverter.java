package ch.qos.logback.core.pattern.color;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/color/GreenCompositeConverter.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/color/GreenCompositeConverter.class */
public class GreenCompositeConverter<E> extends ForegroundCompositeConverterBase<E> {
    @Override // ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
    protected String getForegroundColorCode(E event) {
        return ANSIConstants.GREEN_FG;
    }
}
