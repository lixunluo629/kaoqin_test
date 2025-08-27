package org.apache.xmlbeans;

import org.apache.xmlbeans.SchemaComponent;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaGlobalAttribute.class */
public interface SchemaGlobalAttribute extends SchemaLocalAttribute, SchemaComponent {
    Ref getRef();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SchemaGlobalAttribute$Ref.class */
    public static final class Ref extends SchemaComponent.Ref {
        public Ref(SchemaGlobalAttribute element) {
            super(element);
        }

        public Ref(SchemaTypeSystem system, String handle) {
            super(system, handle);
        }

        @Override // org.apache.xmlbeans.SchemaComponent.Ref
        public final int getComponentType() {
            return 3;
        }

        public final SchemaGlobalAttribute get() {
            return (SchemaGlobalAttribute) getComponent();
        }
    }
}
