package ch.qos.logback.core.read;

import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/read/ListAppender.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/read/ListAppender.class */
public class ListAppender<E> extends AppenderBase<E> {
    public List<E> list = new ArrayList();

    @Override // ch.qos.logback.core.AppenderBase
    protected void append(E e) {
        this.list.add(e);
    }
}
