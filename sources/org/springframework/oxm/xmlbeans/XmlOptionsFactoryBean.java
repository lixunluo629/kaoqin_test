package org.springframework.oxm.xmlbeans;

import java.util.Map;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.beans.factory.FactoryBean;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xmlbeans/XmlOptionsFactoryBean.class */
public class XmlOptionsFactoryBean implements FactoryBean<XmlOptions> {
    private XmlOptions xmlOptions = new XmlOptions();

    public void setOptions(Map<String, ?> optionsMap) {
        this.xmlOptions = new XmlOptions();
        if (optionsMap != null) {
            for (Map.Entry<String, ?> option : optionsMap.entrySet()) {
                this.xmlOptions.put(option.getKey(), option.getValue());
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public XmlOptions getObject() {
        return this.xmlOptions;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends XmlOptions> getObjectType() {
        return XmlOptions.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
