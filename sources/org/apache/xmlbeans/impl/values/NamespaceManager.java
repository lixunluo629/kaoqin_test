package org.apache.xmlbeans.impl.values;

import org.apache.xmlbeans.impl.common.PrefixResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/values/NamespaceManager.class */
public interface NamespaceManager extends PrefixResolver {
    String find_prefix_for_nsuri(String str, String str2);
}
