package org.apache.commons.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/IOExceptionList.class */
public class IOExceptionList extends IOException implements Iterable<Throwable> {
    private static final long serialVersionUID = 1;
    private final List<? extends Throwable> causeList;

    public static void checkEmpty(List<? extends Throwable> causeList, Object message) throws IOExceptionList {
        if (!isEmpty(causeList)) {
            throw new IOExceptionList(Objects.toString(message, null), causeList);
        }
    }

    private static boolean isEmpty(List<? extends Throwable> causeList) {
        return size(causeList) == 0;
    }

    private static int size(List<? extends Throwable> causeList) {
        if (causeList != null) {
            return causeList.size();
        }
        return 0;
    }

    private static String toMessage(List<? extends Throwable> causeList) {
        return String.format("%,d exception(s): %s", Integer.valueOf(size(causeList)), causeList);
    }

    public IOExceptionList(List<? extends Throwable> causeList) {
        this(toMessage(causeList), causeList);
    }

    public IOExceptionList(String message, List<? extends Throwable> causeList) {
        super(message != null ? message : toMessage(causeList), isEmpty(causeList) ? null : causeList.get(0));
        this.causeList = causeList == null ? Collections.emptyList() : causeList;
    }

    public <T extends Throwable> T getCause(int index) {
        return (T) this.causeList.get(index);
    }

    public <T extends Throwable> T getCause(int index, Class<T> clazz) {
        return clazz.cast(getCause(index));
    }

    public <T extends Throwable> List<T> getCauseList() {
        return new ArrayList(this.causeList);
    }

    public <T extends Throwable> List<T> getCauseList(Class<T> clazz) {
        return new ArrayList(this.causeList);
    }

    @Override // java.lang.Iterable
    public Iterator<Throwable> iterator() {
        return getCauseList().iterator();
    }
}
