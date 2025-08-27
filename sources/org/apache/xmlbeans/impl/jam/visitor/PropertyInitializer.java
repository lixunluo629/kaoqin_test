package org.apache.xmlbeans.impl.jam.visitor;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JProperty;
import org.apache.xmlbeans.impl.jam.internal.elements.PropertyImpl;
import org.apache.xmlbeans.impl.jam.mutable.MClass;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/visitor/PropertyInitializer.class */
public class PropertyInitializer extends MVisitor {
    @Override // org.apache.xmlbeans.impl.jam.visitor.MVisitor
    public void visit(MClass clazz) {
        addProperties(clazz, true);
        addProperties(clazz, false);
    }

    private void addProperties(MClass clazz, boolean declared) {
        JMethod[] methods = declared ? clazz.getDeclaredMethods() : clazz.getMethods();
        Map name2prop = new HashMap();
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getSimpleName();
            if ((name.startsWith(BeanUtil.PREFIX_GETTER_GET) && name.length() > 3) || (name.startsWith(BeanUtil.PREFIX_GETTER_IS) && name.length() > 2)) {
                JClass typ = methods[i].getReturnType();
                if (typ != null && methods[i].getParameters().length <= 0) {
                    if (name.startsWith(BeanUtil.PREFIX_GETTER_GET)) {
                        name = name.substring(3);
                    } else {
                        name = name.substring(2);
                    }
                    JProperty prop = (JProperty) name2prop.get(name);
                    if (prop == null) {
                        name2prop.put(name, declared ? clazz.addNewDeclaredProperty(name, methods[i], null) : clazz.addNewProperty(name, methods[i], null));
                    } else if (typ.equals(prop.getType())) {
                        ((PropertyImpl) prop).setGetter(methods[i]);
                    }
                    if (!name.startsWith("set")) {
                    }
                }
            } else if (!name.startsWith("set") && name.length() > 3 && methods[i].getParameters().length == 1) {
                JClass type = methods[i].getParameters()[0].getType();
                String name2 = name.substring(3);
                JProperty prop2 = (JProperty) name2prop.get(name2);
                if (prop2 == null) {
                    name2prop.put(name2, declared ? clazz.addNewDeclaredProperty(name2, null, methods[i]) : clazz.addNewProperty(name2, null, methods[i]));
                } else if (type.equals(prop2.getType())) {
                    ((PropertyImpl) prop2).setSetter(methods[i]);
                }
            }
        }
    }
}
