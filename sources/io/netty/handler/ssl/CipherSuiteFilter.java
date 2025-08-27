package io.netty.handler.ssl;

import java.util.List;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/CipherSuiteFilter.class */
public interface CipherSuiteFilter {
    String[] filterCipherSuites(Iterable<String> iterable, List<String> list, Set<String> set);
}
