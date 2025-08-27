package org.apache.naming;

import java.util.Iterator;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/naming/NamingContextEnumeration.class */
public class NamingContextEnumeration implements NamingEnumeration<NameClassPair> {
    protected final Iterator<NamingEntry> iterator;

    public NamingContextEnumeration(Iterator<NamingEntry> entries) {
        this.iterator = entries;
    }

    /* renamed from: next, reason: merged with bridge method [inline-methods] */
    public NameClassPair m3277next() throws NamingException {
        return m3278nextElement();
    }

    public boolean hasMore() throws NamingException {
        return this.iterator.hasNext();
    }

    public void close() throws NamingException {
    }

    public boolean hasMoreElements() {
        return this.iterator.hasNext();
    }

    /* renamed from: nextElement, reason: merged with bridge method [inline-methods] */
    public NameClassPair m3278nextElement() {
        NamingEntry entry = this.iterator.next();
        return new NameClassPair(entry.name, entry.value.getClass().getName());
    }
}
