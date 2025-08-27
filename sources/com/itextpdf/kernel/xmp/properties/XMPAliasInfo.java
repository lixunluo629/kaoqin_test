package com.itextpdf.kernel.xmp.properties;

import com.itextpdf.kernel.xmp.options.AliasOptions;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/properties/XMPAliasInfo.class */
public interface XMPAliasInfo {
    String getNamespace();

    String getPrefix();

    String getPropName();

    AliasOptions getAliasForm();
}
