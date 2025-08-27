package org.ehcache.jsr107;

import javax.cache.integration.CompletionListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/NullCompletionListener.class */
final class NullCompletionListener implements CompletionListener {
    static final CompletionListener INSTANCE = new NullCompletionListener();

    public void onCompletion() {
    }

    public void onException(Exception e) {
    }

    private NullCompletionListener() {
    }
}
