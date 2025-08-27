package com.itextpdf.kernel.xmp;

import com.itextpdf.kernel.xmp.properties.XMPAliasInfo;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/XMPSchemaRegistry.class */
public interface XMPSchemaRegistry {
    String registerNamespace(String str, String str2) throws XMPException;

    String getNamespacePrefix(String str);

    String getNamespaceURI(String str);

    Map getNamespaces();

    Map getPrefixes();

    void deleteNamespace(String str);

    XMPAliasInfo resolveAlias(String str, String str2);

    XMPAliasInfo[] findAliases(String str);

    XMPAliasInfo findAlias(String str);

    Map getAliases();
}
