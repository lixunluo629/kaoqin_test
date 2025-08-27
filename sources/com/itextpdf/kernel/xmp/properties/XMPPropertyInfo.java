package com.itextpdf.kernel.xmp.properties;

import com.itextpdf.kernel.xmp.options.PropertyOptions;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/properties/XMPPropertyInfo.class */
public interface XMPPropertyInfo extends XMPProperty {
    String getNamespace();

    String getPath();

    @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
    String getValue();

    @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
    PropertyOptions getOptions();
}
