package com.adobe.xmp.properties;

import com.adobe.xmp.options.PropertyOptions;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/properties/XMPPropertyInfo.class */
public interface XMPPropertyInfo extends XMPProperty {
    String getNamespace();

    String getPath();

    @Override // com.adobe.xmp.properties.XMPProperty
    String getValue();

    @Override // com.adobe.xmp.properties.XMPProperty
    PropertyOptions getOptions();
}
