package org.apache.xmlbeans;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaComponent;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaGlobalElement.class */
public interface SchemaGlobalElement extends SchemaLocalElement, SchemaComponent {
    QName[] substitutionGroupMembers();

    SchemaGlobalElement substitutionGroup();

    boolean finalExtension();

    boolean finalRestriction();

    Ref getRef();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaGlobalElement$Ref.class */
    public static final class Ref extends SchemaComponent.Ref {
        public Ref(SchemaGlobalElement element) {
            super(element);
        }

        public Ref(SchemaTypeSystem system, String handle) {
            super(system, handle);
        }

        @Override // org.apache.xmlbeans.SchemaComponent.Ref
        public final int getComponentType() {
            return 1;
        }

        public final SchemaGlobalElement get() {
            return (SchemaGlobalElement) getComponent();
        }
    }
}
