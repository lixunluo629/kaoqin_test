package org.apache.xmlbeans;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaComponent;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaModelGroup.class */
public interface SchemaModelGroup extends SchemaComponent, SchemaAnnotated {
    @Override // org.apache.xmlbeans.SchemaComponent
    int getComponentType();

    @Override // org.apache.xmlbeans.SchemaComponent
    QName getName();

    Object getUserData();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaModelGroup$Ref.class */
    public static final class Ref extends SchemaComponent.Ref {
        public Ref(SchemaModelGroup modelGroup) {
            super(modelGroup);
        }

        public Ref(SchemaTypeSystem system, String handle) {
            super(system, handle);
        }

        @Override // org.apache.xmlbeans.SchemaComponent.Ref
        public final int getComponentType() {
            return 6;
        }

        public final SchemaModelGroup get() {
            return (SchemaModelGroup) getComponent();
        }
    }
}
