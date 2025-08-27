package org.apache.xmlbeans.impl.tool;

import java.util.ArrayList;
import java.util.List;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Extension.class */
public class Extension {
    private Class className;
    private List params = new ArrayList();

    public Class getClassName() {
        return this.className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public List getParams() {
        return this.params;
    }

    public Param createParam() {
        Param p = new Param();
        this.params.add(p);
        return p;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/Extension$Param.class */
    public class Param {
        private String name;
        private String value;

        public Param() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
