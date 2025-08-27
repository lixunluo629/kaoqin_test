package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAware;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/ConverterUtil.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/ConverterUtil.class */
public class ConverterUtil {
    public static <E> void startConverters(Converter<E> head) {
        Converter<E> next = head;
        while (true) {
            Converter<E> c = next;
            if (c != null) {
                if (c instanceof CompositeConverter) {
                    CompositeConverter<E> cc = (CompositeConverter) c;
                    Converter<E> childConverter = cc.childConverter;
                    startConverters(childConverter);
                    cc.start();
                } else if (c instanceof DynamicConverter) {
                    DynamicConverter<E> dc = (DynamicConverter) c;
                    dc.start();
                }
                next = c.getNext();
            } else {
                return;
            }
        }
    }

    public static <E> Converter<E> findTail(Converter<E> head) {
        Converter<E> p;
        Converter<E> converter = head;
        while (true) {
            p = converter;
            if (p == null) {
                break;
            }
            Converter<E> next = p.getNext();
            if (next == null) {
                break;
            }
            converter = next;
        }
        return p;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void setContextForConverters(Context context, Converter<E> head) {
        Converter<E> next = head;
        while (true) {
            Converter converter = next;
            if (converter != 0) {
                if (converter instanceof ContextAware) {
                    ((ContextAware) converter).setContext(context);
                }
                next = converter.getNext();
            } else {
                return;
            }
        }
    }
}
